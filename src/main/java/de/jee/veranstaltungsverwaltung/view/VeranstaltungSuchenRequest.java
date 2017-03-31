package de.jee.veranstaltungsverwaltung.view;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;

@Named
@ViewScoped
public class VeranstaltungSuchenRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2829093739275040768L;
	@Inject
	private Security security;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	@Inject
	private ReservierungDAO reservierungDAO;
	private String suchString;
	private Date vonDatum;
	private Date bisDatum;
	private String anzahlTickets;
	private DataModel<Veranstaltung> veranstaltungen;
	/**
	 * Diese Methode sucht in der Datenbank nach Veranstaltungen. Verpflichtend ist der Suchbegriff, die anderen Parameter sind beliebig kombinierbar
	 */
	public void sucheVeranstaltung(){
		this.setVeranstaltungen(null);
		List<Veranstaltung> veranstaltungen = null;
		if(anzahlTickets.trim().equals("")){
			try{
				veranstaltungen = veranstaltungDAO.findByName(suchString, vonDatum, bisDatum, true, 0);
			}
			catch(NullPointerException e){
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Suche konnte nicht durchgeführt werden", null);
				context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			}
		}
		else{
			try{
				veranstaltungen = veranstaltungDAO.findByName(suchString, vonDatum, bisDatum, true, Integer.parseInt(anzahlTickets));
			}
			catch(NullPointerException e){
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Suche konnte nicht durchgeführt werden", null);
				context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			}
		}
		if(!veranstaltungen.isEmpty()){
			this.veranstaltungen = new ListDataModel<Veranstaltung>(veranstaltungen); 
		}
		else{
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es konnten keine Veranstaltungen entsprechend der Kriterien gefunden werden", null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
		}
	}
	/**
	 * Reserviert Tickets für einen Benutzer, dabei wird aus dem ListDataModel die Veranstaltung und die Anzahl der Tickets ausgewertet. 
	 */
	public void reservieren(){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = null;
		Veranstaltung veranstaltung = veranstaltungen.getRowData();
		Reservierung reservierung = new Reservierung(security.getCurrentUser());
		if(veranstaltung.getZuReservierendeTickets().equals("")){
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Anzahl der zu reservierenden Tickets muss angegeben werden", null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			return;
		}
		int anzahlTickets = Integer.parseInt(veranstaltung.getZuReservierendeTickets());
		if(anzahlTickets > veranstaltung.getVerfuegbareTickets().size()){
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es können nicht mehr Tickets reserviert werden, als noch verfügbar sind!", null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			return;
		}
		int returncode = reservierungDAO.save(reservierung, veranstaltung, anzahlTickets);
		switch(returncode){
		case -1:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Reservierung konnte nicht durchgeführt werden!", null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			break;
		case 0:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es sind nicht mehr genügend Tickets für die Reservierung vorhanden. Da war wohl jemand schneller...", null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			break;
		default:
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Tickets wurden erfolgreich reserviert. Die Reservierungs-ID lautet: " + returncode, null);
			context.addMessage("veranstaltungSuchenForm:suchergebnis", message);
			break;
		}
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public String getSuchString() {
		return suchString;
	}

	public void setSuchString(String suchString) {
		this.suchString = suchString;
	}

	public Date getVonDatum() {
		return vonDatum;
	}

	public void setVonDatum(Date vonDatum) {
		this.vonDatum = vonDatum;
	}

	public Date getBisDatum() {
		return bisDatum;
	}

	public void setBisDatum(Date bisDatum) {
		this.bisDatum = bisDatum;
	}

	public String getAnzahlTickets() {
		return anzahlTickets;
	}

	public void setAnzahlTickets(String anzahlTickets) {
		this.anzahlTickets = anzahlTickets;
	}
	public DataModel<Veranstaltung> getVeranstaltungen() {
		return veranstaltungen;
	}
	public void setVeranstaltungen(DataModel<Veranstaltung> veranstaltungen) {
		this.veranstaltungen = veranstaltungen;
	}
	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}
	public void setReservierungDAO(ReservierungDAO reservierungDAO) {
		this.reservierungDAO = reservierungDAO;
	}
}
