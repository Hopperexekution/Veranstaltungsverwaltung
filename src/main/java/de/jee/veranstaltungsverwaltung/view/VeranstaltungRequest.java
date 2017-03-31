package de.jee.veranstaltungsverwaltung.view;

import java.io.IOException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;

@Named
@RequestScoped
public class VeranstaltungRequest {
	@Inject
	private Security security;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	private String name;
	private String beschreibung;
	private Date datum;
	private Date zeit;
	private double ticketPreis;
	private String ort;
	private int anzahlTickets;
	private boolean istVeroeffentlicht;
	
	
	public void init(){
		if(!security.isManager()){
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public String erstelleVeranstaltung(){
		if(beschreibung == null)
			beschreibung = "";
		Veranstaltung veranstaltung = new Veranstaltung(name, beschreibung, datum, ort, istVeroeffentlicht, security.getCurrentUser());
		veranstaltung.setTicketPreis(ticketPreis);
		if(zeit.getHours() < 23)
			veranstaltung.getDatum().setHours(zeit.getHours() + 1);
		else
			veranstaltung.getDatum().setHours(0);	
		veranstaltung.getDatum().setMinutes(zeit.getMinutes());
		
		int returncode = veranstaltungDAO.save(veranstaltung, anzahlTickets);
		if(returncode <= 1)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Veranstaltung wurde erfolgreich angelegt", null));
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Veranstaltung konnte nicht angelegt werden", null));		
		return "home.jsf";
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public boolean isIstVeroeffentlicht() {
		return istVeroeffentlicht;
	}

	public void setIstVeroeffentlicht(boolean istVeroeffentlicht) {
		this.istVeroeffentlicht = istVeroeffentlicht;
	}

	public int getAnzahlTickets() {
		return anzahlTickets;
	}

	public void setAnzahlTickets(int anzahlTickets) {
		this.anzahlTickets = anzahlTickets;
	}

	public Date getZeit() {
		return zeit;
	}

	public void setZeit(Date zeit) {
		this.zeit = zeit;
	}

	public double getTicketPreis() {
		return ticketPreis;
	}

	public void setTicketPreis(double ticketPreis) {
		this.ticketPreis = ticketPreis;
	}

	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}
}