package pl.empit.spiv.action
import pl.empit.spiv.model.Investor
import pl.empit.spiv.model.Paper

class HomeController {
	
	def investorPaperService
	
	def defaultAction = "index"
	
	def index = {
		def papers = []
		log.info "No investor"
		papers = Paper.listOrderByPaperCode()	
		log.info "Got list of ${papers.size()} papers"
		
		if (!session.user) {
			def i = Investor.findByUsername("pmasko@o2.pl")	
			if (i) {
				session.user = i
				log.info "Connected user ${i.id} to session"
			}
		}
		
		[papers: papers]
	}
}