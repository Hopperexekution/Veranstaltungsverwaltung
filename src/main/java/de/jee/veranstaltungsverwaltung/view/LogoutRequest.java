package de.jee.veranstaltungsverwaltung.view;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;

@Named
@RequestScoped
public class LogoutRequest {
	
	@Inject
	private Security security;

	
	public String doLogout(){
		security.logout();

		return "home";
		
	}
	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

}
