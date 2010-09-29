package pl.empit.spiv.model

class Company {
	String name
	String CEO
 	BigDecimal freeFloat
	Date dateCreated
	Date lastModified
	
	static constraints = {
		name(blank: false, size:1..100)
		CEO(blank: true, size: 0..60, nullable: true)
		freeFloat(nullable: true)
		dateCreated()
		lastModified(nullable : true)
	}
	
	static belongsTo = Paper
	
	static mapping = {
		table "company"
		paperCode name: "comp_name", unique: true, index: "company_name_idx"
	}
}
