package de.jee.veranstaltungsverwaltung.view;


import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.service.UserService;
/**
 * Diese Bean ermöglicht das Registrieren eines Benutzers
 */
@Named
@RequestScoped
public class RegisterRequest {
	
	@Inject
	private Security security;
	@Inject
	private UserService userService;

	private String benutzername;;
	private String passwort1;
	
	private String passwort2;
	
	private boolean istManager;
	
	private String email;
	
	/**
	 * Registrieren des Benutzers, Speichern des Benutzers in der Datenbank
	 * @return
	 */
	public String doRegister(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = null;
		//Der Benutzername ist nicht leer
		if(!benutzername.trim().equals("")&&!passwort1.trim().equals("")){

			//Der Benutzername existiert noch nicht
			if(userService.checkUser(benutzername)){
				//Das Passwort wurde korrekt bestätigt
				if(passwort1.equals(passwort2)){
					Nutzer nutzer = userService.registerUser(benutzername, passwort1, istManager, email);
					security.login(nutzer);
				}else{
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Das Passwort wurde nicht korrekt bestätigt", null);
					context.addMessage("registerForm:user", message);
				}
			}else{
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Der Benutzername existiert bereits", null);
				context.addMessage("registerForm:user", message);
			}
		}else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Der Benutzername muss angegeben werden", null);
			context.addMessage("registerForm:user", message);
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

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
