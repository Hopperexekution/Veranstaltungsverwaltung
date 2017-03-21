package de.jee.veranstaltungsverwaltung.view;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;

@Named
@RequestScoped
public class VeranstaltungSuchenRequest {
	
	@Inject
	private Security security;
	private String suchString;
	private Date vonDatum;
	private Date bisDatum;
	private String anzahlTickets;
	private List<Veranstaltung> veranstaltungsListe;
	
	public void sucheVeranstaltung(){
		if((vonDatum == null && bisDatum == null) || (vonDatum != null && bisDatum != null)){
			if(vonDatum == null){
				if(anzahlTickets == null){
					
				}
				else{
					
				}
			}
			else{
				if(anzahlTickets == null){
					
				}
				else{
					
				}
			}
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

	public List<Veranstaltung> getVeranstaltungsListe() {
		return veranstaltungsListe;
	}

	public void setVeranstaltungsListe(List<Veranstaltung> veranstaltungsListe) {
		this.veranstaltungsListe = veranstaltungsListe;
	}

}
