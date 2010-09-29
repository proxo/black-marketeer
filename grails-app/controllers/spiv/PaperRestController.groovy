package spiv

import grails.converters.XML
import grails.converters.JSON
import pl.empit.spiv.model.Paper

class PaperRestController {
	
	def list = { render Paper.list() as XML }
	
    def show = {
		
		Paper paper = Paper.get(params.id)
		withFormat {
			html {
				[paper: paper]
			}
			xml {
				[paper: paper]
			}
			JSON {
				render paper as JSON
			}
	
		}
	}
}
