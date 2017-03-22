package de.jee.veranstaltungsverwaltung.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Tour;
import de.jee.veranstaltungsverwaltung.model.TourDAO;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;

@Named
@RequestScoped
public class TourRequest {
	@Inject
	private Security security;
	private String tourname;
	private String tourbeschreibung;
	private int tourid;
	private List<Veranstaltung> veranstaltungen;
	


	
	public String erstelleTour(){
		if(tourbeschreibung == null)
			tourbeschreibung = "";
		TourDAO dao = new TourDAO();
		
		
		setTourid(dao.save(new Tour(tourname, tourbeschreibung, new HashSet<Veranstaltung>(veranstaltungen) )));
		
		

		return "neue_tour";
	}
	public String erstelleVeranstaltung(){
		if(veranstaltungen==null){
			veranstaltungen=new ArrayList<Veranstaltung>();
		}
		if(tourbeschreibung == null)
			tourbeschreibung = "";
		veranstaltungen.add(new Veranstaltung(tourname,tourbeschreibung, new Date(), false, security.getCurrentUser()));

		return "neue_tour";
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}



	public String getTourbeschreibung() {
		return tourbeschreibung;
	}

	public void setTourbeschreibung(String tourbeschreibung) {
		this.tourbeschreibung = tourbeschreibung;
	}

	public List<Veranstaltung> getVeranstaltungen() {
		return veranstaltungen;
	}

	public void setVeranstaltungen(List<Veranstaltung> veranstaltungen) {
		this.veranstaltungen = veranstaltungen;
	}

	public String getTourname() {
		return tourname;
	}

	public void setTourname(String tourname) {
		this.tourname = tourname;
	}


	public int getTourid() {
		return tourid;
	}

	public void setTourid(int tourid) {
		this.tourid = tourid;
	}


	

	
}
