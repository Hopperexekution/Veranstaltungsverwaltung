package de.jee.veranstaltungsverwaltung.validator;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("DateValidator")
public class DateValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(value != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
			try{
				Date heute = new Date(System.currentTimeMillis());
				heute = format.parse(format.format(heute));
				Date eingegebenesDatum = format.parse(format.format((Date) value));
				if(eingegebenesDatum.before(heute))
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Das eingegebene Datum darf nicht vor dem heutigen Datum liegen", null));
			}
			catch(Exception e){
				SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
				try{
					Date heute = new Date(System.currentTimeMillis());
					heute = format2.parse(format2.format(heute));
					Date eingegebenesDatum = format2.parse(format2.format((Date) value));
					if(eingegebenesDatum.before(heute))
						throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Das eingegebene Datum darf nicht vor dem heutigen Datum liegen", null));
				}
				catch(Exception e2){
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Das eingegebene Datum ist ungültig", null));
				}
				
			}
		}
	}
	
}
