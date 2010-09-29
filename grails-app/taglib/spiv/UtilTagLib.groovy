package spiv

class UtilTagLib {
 // namespace for tag
	static namespace = "lame"
	
	def lameBrowser = { attrs,body -> 
		if (request.getHeaders('User-Agent') =~attrs.userAgent) {
			out << body()
		}
	}
}
