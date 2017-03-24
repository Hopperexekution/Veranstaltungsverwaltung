package de.jee.veranstaltungsverwaltung.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("AnzahlTicketsReservieren")
public class AnzahlTicketsReservierenValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String anzahlTicketsString = value.toString().trim();
		FacesMessage message = null;
		if(!anzahlTicketsString.equals("")){
			try{
				int anzahlTicketsInt = Integer.parseInt(anzahlTicketsString);
				if(anzahlTicketsInt <= 0){
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die eingegebene Zahl muss größer als 0 sein!" , null);
					context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
					throw new ValidatorException(message);
				}
			}
			catch(NumberFormatException e){
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültige Eingabe! Es dürfen nur Zahlen eingegeben werden und diese müssen größer als 0 sein!" , null);
				context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
				throw new ValidatorException(message);
				
			}
		}
		else{
		}
	}

}
