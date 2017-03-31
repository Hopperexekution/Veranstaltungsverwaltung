package de.jee.veranstaltungsverwaltung.view;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.service.UserService;

@Named
public class UserConverter implements Converter{
	@Inject
	UserService userService = new UserService();
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(null == value || value.isEmpty()){
			return null;
		}
		Nutzer user= userService.findByUsername(value);
				if (user==null){
					FacesMessage msg =
							new FacesMessage("Der Benutzername oder das Passwort ist nicht korrekt");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						throw new ConverterException(msg);
				}
		
		return user ;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null){
			return null;
		}
		return Nutzer.class.cast(value).getBenutzername();
	}
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
