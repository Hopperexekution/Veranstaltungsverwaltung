package de.jee.veranstaltungsverwaltung.view;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;

@Named
@RequestScoped
/**
 * Diese Bean ermöglicht das Anzeigen zweier Tabellen mit Veranstaltungen.
 * Zum einen die 5 neuesten Veranstaltungen, zum anderen alle Veranstaltungen
 *
 */
public class HomeRequest {

	@Inject
	private Security security;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	private List<Veranstaltung> alleEvents;
	
	private List<Veranstaltung> neuesteEvents;
	/**
	 * Laden der Veranstaltungen
	 */
	@PostConstruct
	public void postConstruct(){
		alleEvents = veranstaltungDAO.all();
		neuesteEvents = veranstaltungDAO.latestFive();
	}


	public List<Veranstaltung> getNeuesteEvents() {
		return neuesteEvents;
	}

	public void setNeuesteEvents(List<Veranstaltung> neuesteEvents) {
		this.neuesteEvents = neuesteEvents;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public List<Veranstaltung> getAlleEvents() {
		return alleEvents;
	}

	public void setAlleEvents(List<Veranstaltung> alleEvents) {
		this.alleEvents = alleEvents;
	}

	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}

}
