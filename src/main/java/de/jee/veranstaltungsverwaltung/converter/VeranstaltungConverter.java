package de.jee.veranstaltungsverwaltung.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;
/**
 * Über diesen Converter wird eine Veranstaltung zu einer ID gefundne
 * @author anwender
 *
 */
@Named
public class VeranstaltungConverter implements Converter{
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		//Der Wert ist null
		if(null == value || value.isEmpty()){
			return null;
		}
		//Suchen der Veranstaltung
		Veranstaltung veranstaltung = veranstaltungDAO.findByID(Integer.parseInt(value));
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

	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}


}
