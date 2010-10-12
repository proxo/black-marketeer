package pl.empit.spiv.service
import pl.empit.spiv.model.Investor
import pl.empit.spiv.model.Paper
import pl.empit.spiv.model.PaperQuotation;
import pl.empit.spiv.model.PaperTransaction
import pl.empit.spiv.model.PaperTransactionSummary
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import pl.empit.spiv.model.TransactionSide;
import java.text.SimpleDateFormat

class InvestorPaperService {
    boolean transactional = true
	
    def getPaperTransactions(Investor investor) {
    		
			// find all papers but not historic
			def papers = Paper.withCriteria {
				and {
					eq("investor", investor)
					isNotEmpty("transactions")
					summary {
						gt("numOfPapers",0)
					}
				}
				order("paperCode")
			}
			papers
    }
    
    
    /**
    *
    */
    def updatePaperSummary(Paper paper, PaperTransaction trans) {
    	paper.addToTransactions(trans)
		createPaperSummary paper
    }
    
    /**
    * Creates summary for paper
    */
    def createPaperSummary(Paper paper) {
		PaperTransactionSummary sum = null
		
		if (paper.summary == null)
    		sum = new PaperTransactionSummary(paper: paper)
		else 
			sum = paper.summary
			
		sum.totalProvision = 0.0
		sum.provision = 0.0
		sum.averagePrice = 0.0
		sum.totalAveragePrice = 0.0
		sum.totalInvested = 0.0
		sum.numOfPapers = 0.0
		sum.profit = null
			
    	int cnt = 0
    	int cntS = 0
		
    	paper.transactions.each { t-> 
    		if (t.side == TransactionSide.BUY) {
    			cnt += t.numOfPapers
    			sum.totalProvision += t.provision
    			sum.provision += t.provision
				def avg = t.price * t.numOfPapers
    			sum.averagePrice += avg 
    			sum.totalAveragePrice += avg
				sum.totalInvested -= avg + t.provision
    			sum.numOfPapers += t.numOfPapers
    		} else {
    			cntS += t.numOfPapers
    			sum.numOfPapers -= t.numOfPapers
				sum.totalProvision += t.provision
				sum.provision += t.provision
			
				def avg = t.price * t.numOfPapers
				sum.totalInvested = sum.totalInvested + avg - t.provision 
				sum.averagePrice -= avg
    			sum.totalAveragePrice -= avg
				
    			if (sum.numOfPapers == 0) {
    				sum.provision = 0.0
    				sum.averagePrice = 0.0
    				log.info("Found complete sell out point at ${t.tradeDate}}!")
    			} 
    		}
    	}
			
		//  
		//
    	if (sum.numOfPapers > 0) {
			sum.averagePrice /= sum.numOfPapers
			sum.totalAveragePrice /= cnt
			// adjust using current average price
			sum.breakEvenPrice = (Math.abs(sum.totalInvested) + getBidProvision(sum.numOfPapers, sum.averagePrice)) / sum.numOfPapers 
    	
			// set summary
			paper.summary = sum
			calculateStopLoss(paper)
    	}
    }
    
    public def getBidProvision(Integer numOfPapers, BigDecimal price) {
		def defaultProvision = ConfigurationHolder.config.empit.provision ?: 0.003;
    	def p = numOfPapers * price * defaultProvision		
    	log.info "Estimated provision: ${p}"
		return p
    }
    
	
	def calculateProfit(Paper paper) {
		if (paper?.summary && paper?.summary?.lastPrice != null) {
			paper.summary.profit = (paper.summary.lastPrice - paper.summary.breakEvenPrice) * paper.summary?.numOfPapers
		}		
	}
	
    def calculateStopLoss(Paper paper) {
	
    	//FIXME: add formula - loss < 1.5 % of total money (include estimated provisions)
    	paper?.summary?.averagePrice * 0.9	
    }
	
	private boolean quotationExists(String code, Date d) {
		def q = PaperQuotation.withCriteria() {
			and {
				eq("tradeDate", d)
				paper {
					eq("paperCode", code)
				}
			}
		}
		
		q?.size() > 0
	}
	
	/**
	 * 
	 * @param fileText
	 * @return
	 */
	def refreshPrices(String fileText, Investor investor) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		def lines = fileText.split("\n")*.trim().findAll { it != '' }
		
		//
		// 0 - code, 1 - date, 2 - open, 3 -max, 4 - min, 5 - close, 6 - volume
		//
		def qMap = [:]
		lines.each { def i = it.split(",");  qMap[i[0].trim()] = i[1..-1] }
		boolean refreshed = false
		def papers = getPaperTransactions(investor)
		if (papers?.size() > 0) {
			papers.each {
				if (qMap[it.paperCode.toUpperCase()] != null) {
					def l = qMap[it.paperCode.toUpperCase()]
					Date d = sdf.parse(l[0])
					if (!quotationExists(it.paperCode, d)) {
						PaperQuotation pq = new PaperQuotation(paper: it, tradeDate: d, openValue: l[1].toBigDecimal(), minValue: l[2].toBigDecimal(), maxValue: l[3].toBigDecimal(), closeValue: l[4].toBigDecimal(), volume: l[5].toBigDecimal())
						
						pq.save(flush: false)
						
						if (it.summary != null) {
							it.summary?.lastPrice = pq.closeValue
							it.save(flush: true)
						}
						refreshed = true
						log.info "Saved quotation for date: ${d} paper: ${it.paperCode}"
					}
				}
			}
		}
		refreshed
	}
}
