package de.jee.veranstaltungsverwaltung.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import de.jee.veranstaltungsverwaltung.service.NutzerDAO;
@FacesValidator("Username")
public class UsernameValidator implements Validator {

	@Inject
	private NutzerDAO nutzerDAO = new NutzerDAO();

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String username = value.toString().trim();
		if (username.equals("")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Benutzername darf nicht leer sein" , null));
		}
		else if(nutzerDAO.findByUsername(username) != null){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Benutzername wird bereits verwendet" , null));
		}
	}
	public void setNutzerDAO(NutzerDAO nutzerDAO) {
		this.nutzerDAO = nutzerDAO;
	}

}
