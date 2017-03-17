package de.jee.veranstaltungsverwaltung.view;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.service.EventService;

@Named
@RequestScoped
public class EventRequest {
	
	@Inject
	private Security security;

	private String titel;
	private String beschreibung;
	private Date datum;
	private Date uhrzeit;
	
	public String add(){
		datum.setTime(datum.getTime()+uhrzeit.getTime());;
		EventService.addEvent(titel, beschreibung, datum, false, security.getCurrentUser());
		return "home";
		
	}
	public String addAndPublish(){
		datum.setTime(datum.getTime()+uhrzeit.getTime());;
		EventService.addEvent(titel, beschreibung,datum, true, security.getCurrentUser());
		return "home";
		
	}
	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public Date getUhrzeit() {
		return uhrzeit;
	}
	public void setUhrzeit(Date uhrzeit) {
		this.uhrzeit = uhrzeit;
	}


}
