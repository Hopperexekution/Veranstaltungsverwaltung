package de.jee.veranstaltungsverwaltung.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;

@Named
@RequestScoped
public class VeranstaltungSuchenRequest {
	
	@Inject
	private Security security;
	private String suchString;
	private Date vonDatum;
	private Date bisDatum;
	private int anzahlTickets;
	private List<Veranstaltung> veranstaltungsListe;
	
	public void sucheVeranstaltung(){
		List<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
		System.out.println("Ich gehe hier rein");
		VeranstaltungDAO dao = new VeranstaltungDAO();
		if((vonDatum == null && bisDatum == null) || (vonDatum != null && bisDatum != null)){
			if(vonDatum == null){
				if(anzahlTickets <= 0){
					veranstaltungen = dao.findByName(suchString);	
				}
				else{
					veranstaltungen = dao.findByName(suchString, anzahlTickets);
				}
			}
			else{
				if(anzahlTickets <= 0){
					veranstaltungen = dao.findByName(suchString, vonDatum, bisDatum);
				}
				else{
					veranstaltungen = dao.findByName(suchString, vonDatum, bisDatum, anzahlTickets);
				}
			}
		}
		for(Veranstaltung veranstaltung : veranstaltungen)
			System.out.println("Name: " + veranstaltung.getName() + " Beschreibung: " + veranstaltung.getBeschreibung() + " Anzahl Tickets: " + veranstaltung.getTickets().size());
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

	public int getAnzahlTickets() {
		return anzahlTickets;
	}

	public void setAnzahlTickets(int anzahlTickets) {
		this.anzahlTickets = anzahlTickets;
	}

	public List<Veranstaltung> getVeranstaltungsListe() {
		return veranstaltungsListe;
	}

	public void setVeranstaltungsListe(List<Veranstaltung> veranstaltungsListe) {
		this.veranstaltungsListe = veranstaltungsListe;
	}

}
