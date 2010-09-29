package pl.empit.spiv.model

/**
*  Paper transaction summary
*/
class PaperTransactionSummary {
	BigDecimal totalProvision = 0.0
	BigDecimal provision = 0.0
	int numOfPapers = 0
	BigDecimal averagePrice = 0.0
	BigDecimal totalAveragePrice = 0.0
	BigDecimal stopLossPrice = 0
	BigDecimal breakEvenPrice =0 
	BigDecimal lastPrice = null
	BigDecimal totalInvested = 0.0
	BigDecimal profit = null
	
	Date lastModified
	Date dateCreated
	
	static belongsTo = [Paper]
	
    static constraints = {
    	numOfPapers()
    	totalProvision(nullable: false)
    	averagePrice(nullable: true)
    	stopLossPrice(nullable: true)		
    	breakEvenPrice(nullable: true)
    	lastPrice(nullable : true)
		lastModified(nullable : true)
		profit(nullable: true)
    }
}
