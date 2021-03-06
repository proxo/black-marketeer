package pl.empit.spiv.action
import pl.empit.spiv.model.Paper
import pl.empit.spiv.model.Company
import pl.empit.spiv.model.PaperTransactionSummary;
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.sun.net.httpserver.Headers;

import java.io.BufferedReader
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentEncoding.Type.*
/**
 * Base controller for Paper class orginally scaffolded.
 * 
 * @author Piotr Maśko
 *
 */
class PaperController {

	def investorPaperService
	def exportService
	
	/**
	 * Export paper data 
	 */
	def exportData = {
		if (params?.format != "html") {
			response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=papers.${params.extension}")
			List fields = ["company", "paperCode", "ipoDate", "company.CEO","summary.totalInvested", "summary.numOfPapers", "summary.breakEvenPrice", "summary.profit"]
			Map labels = ["company": "Company", paperCode: "Paper code", "ipoDate": "IPO date", "company.CEO" : "CEO", "summary.totalInvested":"Total invested", "summary.numOfPapers":"Num of Papers", "summary.breakEvenPrice":"Break Even Price","summary.profit":"Profit"]

			// Formatter closure
			def upperCase = { domain, value ->
				return value.toUpperCase()
			}

			Map formatters = [company: {d,v -> v.name }]
			Map parameters = [title: "Stock market papers", "column.widths": [2.2, 1.4, 2.6, 0.7]]

			exportService.export(params.format, response.outputStream, Paper.listOrderByPaperCode(params), fields, labels, formatters, parameters)			
		}	
	}
	
	/**
	 * Prepare saving a new paper - create new paper
	 */
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
	
	/**
	 * Save changes to exising paper
	 */
	def update = {
		def p = Paper.get(params.id)
		log.info "Updating paper ${p.paperCode}"
		
		if (p.summary == null) {
			p.summary = new PaperTransactionSummary()
		}
		// data
		p.paperCode = params.paperCode
		p.company.name = params.name
		
		flash.message = p.save(flush: true) ? "Updated paper ${p.paperCode}" : "Some error occured"
			
		redirect(uri: "/")	
	}
	
	def refreshPrices = {
		def user = session.user
		user.attach()
		
		withHttp(uri: "http://bossa.pl/pub/metastock/mstock/sesjaall/sesjaall.prn") {
			def html = get(contentType: TEXT) { resp, reader->
				if (investorPaperService.refreshPrices(reader.text, user)) {
					flash.message = 'Refreshed successfully prices'
				}	
			}
		}
				
		// do redirect after refreshing
		redirect(controller: "home", action: "index")
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
