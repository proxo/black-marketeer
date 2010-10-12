package pl.empit.spiv.model

class Paper {
	String paperCode
	Date ipoDate
	Date dateCreated
	PaperTransactionSummary summary
	Company company
	// unidirectional
	
	static belongsTo = [investor: Investor]
	static hasMany = [transactions : PaperTransaction, quotations: PaperQuotation]                    
	                    
    static constraints = {
		paperCode(blank: false, size: 1..5)
		ipoDate(nullable : true)
		summary(nullable: true)
		company(nullable: false)
    }
	
	static mapping = {
		sort "paperCode"
		summary lazy:false
		company lazy:false
		transactions cascade:"all-delete-orphan", sort:'tradeDate'
		paperCode name: "code", unique: true, index: "paper_code_idx"
	}
}
