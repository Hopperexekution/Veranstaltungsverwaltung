package de.jee.veranstaltungsverwaltung.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
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
		return userService.findByUsername(value);
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
