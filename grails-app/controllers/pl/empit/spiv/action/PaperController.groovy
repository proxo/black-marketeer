package pl.empit.spiv.action
import pl.empit.spiv.model.Paper
import pl.empit.spiv.model.Company
import pl.empit.spiv.model.PaperTransactionSummary;

class PaperController {

	def scaffold = Paper
	def investorPaperService
	
	def add = {
		log.info "Starting add paper for user"
		[paper: new Paper()]
	}
	
	def prepareUpdate = {
		def user = session.user
		user.attach()
		
		def paper = Paper.get(params.id)
		if (paper) {
			render(view:"update", model: [paper: paper])
		} else {
			redirect(uri:"/")
		}
	}
	
	def update = {
		def p = Paper.get(params.id)
		log.info "Updating paper ${p.paperCode}"
		
		if (p.summary == null) {
			p.summary = new PaperTransactionSummary()
		}
		
		p?.summary?.properties = params['lastPrice']
		investorPaperService.createPaperSummary p
		investorPaperService.calculateProfit p 
		
		flash.message = p.save(flush: true) ? "Updated paper ${p.paperCode}" : "Some error occured"
			
		redirect(uri: "/")	
	}
	
	def cancel = {
		redirect(uri:"/")
	}
	
	def save = {
		def user = session.user
		user.attach()
		log.info "Saving new paper for user ${user.username}"
		def paper = new Paper(investor: user)

		// bind to params
		paper.properties  = params['paperCode', 'ipoDate' ]
		// other bind option
		def company = new Company(params)
		paper.company = company
		
		if (company.validate() && paper.validate()) {
			paper.save()
			flash.message = "Successfully created paper ${paper.paperCode}"
			redirect(uri: "/")
		} else {
			flash.message = "Error in data"
			render(view: "add", model : [paper: paper])
		}
	}
}
