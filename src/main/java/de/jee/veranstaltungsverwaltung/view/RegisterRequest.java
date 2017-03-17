package de.jee.veranstaltungsverwaltung.view;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.service.UserService;

@Named
@RequestScoped
public class RegisterRequest {
	
	@Inject
	private Security security;

	private String benutzername;

	private String passwort1;
	
	private String passwort2;
	
	private boolean istManager;
	
	private String email;
	
	public String doRegister(){
		if(!benutzername.trim().equals("")&&!passwort1.trim().equals("")){

			if(UserService.checkUser(benutzername)){
				if(passwort1.equals(passwort2)){
					Nutzer nutzer = UserService.registerUser(benutzername, passwort1, istManager, email);
					security.login(nutzer);
				}
			}
		}
		return "home";
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public String getBenutzername() {
		return benutzername;
	}
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}
	public String getPasswort1() {
		return passwort1;
	}
	public void setPasswort1(String passwort1) {
		this.passwort1 = passwort1;
	}
	public String getPasswort2() {
		return passwort2;
	}
	public void setPasswort2(String passwort2) {
		this.passwort2 = passwort2;
	}
	public boolean isIstManager() {
		return istManager;
	}
	public void setIstManager(boolean istManager) {
		this.istManager = istManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
