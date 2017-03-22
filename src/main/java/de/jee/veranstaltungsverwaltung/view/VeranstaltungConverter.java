package de.jee.veranstaltungsverwaltung.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;
import de.jee.veranstaltungsverwaltung.service.UserService;

@Named
public class VeranstaltungConverter implements Converter{
	@Inject
	VeranstaltungDAO dao = new VeranstaltungDAO();
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(null == value || value.isEmpty()){
			return null;
		}
		Veranstaltung veranstaltung = dao.findByID(Integer.parseInt(value));
		System.out.println(veranstaltung.getName());
		return veranstaltung;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null){
			return null;
		}
		return Veranstaltung.class.cast(value).getId()+"";
	}


}
