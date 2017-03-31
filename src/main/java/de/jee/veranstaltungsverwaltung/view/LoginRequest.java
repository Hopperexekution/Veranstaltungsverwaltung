package de.jee.veranstaltungsverwaltung.view;


import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.service.UserService;

@Named
@RequestScoped
public class LoginRequest {
	
	@Inject
	private Security security;
	@Inject
	private UserService userService;

	private Nutzer user;

	private String passwort;
	
	public String doLogin(){
		if(user!=null&&!user.getBenutzername().trim().equals("")){

			if(userService.checkPasswort(user, passwort)){
				security.login(user);
			}else{
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = null;
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Der Benutzername oder das Passwort ist nicht korrekt", null);
				context.addMessage("loginForm:user", message);
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
	public Nutzer getUser() {
		return user;
	}
	public void setUser(Nutzer user) {
		this.user = user;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
