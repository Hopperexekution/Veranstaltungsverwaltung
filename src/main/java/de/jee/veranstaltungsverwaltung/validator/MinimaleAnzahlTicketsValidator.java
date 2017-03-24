package de.jee.veranstaltungsverwaltung.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
@FacesValidator("MinimaleAnzahlTickets")
public class MinimaleAnzahlTicketsValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
			String anzahlTicketsString = value.toString().trim();
			if(!anzahlTicketsString.equals("")){
			try{
				int anzahlTicketsInt = Integer.parseInt(anzahlTicketsString);
				if(anzahlTicketsInt < 0)
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die eingegebene Zahl muss gr��er oder gleich 0 sein!" , null));
			}
			catch(NumberFormatException e){
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ung�ltige Eingabe! Es d�rfen nur Zahlen eingegeben werden und diese m�ssen gr��er oder gleich 0 sein!" , null));
			}
		}
		
	}
	
}
