package pl.empit.spiv.model

class PaperTransaction {
	Date tradeDate = new Date()
	String comment
	BigDecimal provision
	BigDecimal price
	Integer numOfPapers
	TransactionSide side
	
    static constraints = {
		tradeDate()
		comment(maxSize: 1000, nullable: true, blank: true)
		provision(nullable : false, min: 0.0)
		price(nullable : false, min: 0.0)
		numOfPapers(nullable: false)
		side(nullable: false)
    }
	
	static mapping  = {
		table 'paper_transaction'
		tradeDate column:"trade_date"
		comment column: "comment_text"
	}
	
	static belongsTo = [paper: Paper]
}
