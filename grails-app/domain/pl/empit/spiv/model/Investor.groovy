package pl.empit.spiv.model

import java.util.Set;

public class Investor {
	String firstName
	String lastName
	Date dateCreated
	Date lastModified
	
	String username
	String password
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false
	BigDecimal totalInvested = 0.0
	
	static constraints = {
		username blank: false, unique: true
		password blank: true, nullable: true
		firstName(blank: false, size: 1..30)
		lastName(blank: false, size: 2..30)
		totalInvested()
		dateCreated()
		lastModified(nullable : true)
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	static hasMany = [papers: Paper]
	                  
}
