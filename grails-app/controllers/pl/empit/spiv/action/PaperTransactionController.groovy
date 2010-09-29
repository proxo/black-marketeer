package pl.empit.spiv.action
import pl.empit.spiv.model.PaperTransaction
import pl.empit.spiv.model.Paper
import pl.empit.spiv.service.InvestorPaperService;

class PaperTransactionController {

	def scaffold = PaperTransaction
	InvestorPaperService investorPaperService
	
	def add = {
		log.info "Starting add paper transaction for paper: ${params.id}"
		PaperTransaction pt = new PaperTransaction()
		[pt: pt, paperId: params.id]
	}
	
	def cancel = {
		redirect(uri: "/")	
	}
	
	def calculateProvision = {
		def pp = null
		if (params.price && params.numOfPapers) {
			pp = investorPaperService.getBidProvision(params.numOfPapers.toInteger(), params.price.toBigDecimal())
			render(contentType : "application/json") {
				result(value:pp)
			}
		}
		render(contentType : "application/json") {
			noresult()
		}
	}
	
	def save = {
		log.info "Saving new paper transaction for paper: ${params.paperId}"
		
		def p = Paper.get(params.paperId)
		if (!p) {
			flash.message = "Cannot find paper for the new transaction"
			redirect(uri: "/")
		}
		
		def pt = new PaperTransaction(params)
		p.addToTransactions pt 
		
		if (pt.validate()) {
			// create summary
			investorPaperService.createPaperSummary p
			
			if (p.save(flush: true)) {
				flash.message = "Successfully created transaction ${p.paperCode}"				
				redirect(uri: "/")
			}
		} else {
			pt.discard()
			flash.message = "Error in data"
			render(view: "add", model : [pt: pt, paperId: params.paperId])
		}
	}
}
