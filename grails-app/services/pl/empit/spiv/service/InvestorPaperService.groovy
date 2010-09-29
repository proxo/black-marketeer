package pl.empit.spiv.service
import pl.empit.spiv.model.Investor
import pl.empit.spiv.model.Paper
import pl.empit.spiv.model.PaperTransaction
import pl.empit.spiv.model.PaperTransactionSummary
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import pl.empit.spiv.model.TransactionSide;
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
}
