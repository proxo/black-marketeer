import pl.empit.spiv.model.Investor;
import pl.empit.spiv.model.*;
import static pl.empit.spiv.model.TransactionSide.*;

class BootStrap {
	def springSecurityService
	
     def init = { 
		 servletContext ->
		 
		 String passwd = springSecurityService.encodePassword('password')
		 def user = new Investor(firstName: "Admin", lastName : "Admin", username: "admin@o2.pl", password: passwd)		 
		 user.save(flush: true, failOnError: true)
		 def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)  
		
		 UserRole.create user, adminRole, true

     }
     def destroy = {
     }
	 
} 