package de.jee.veranstaltungsverwaltung.view;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.NutzerDAO;
import de.jee.veranstaltungsverwaltung.service.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;
/**
 * Diese Bean ermöglicht das Anzeigen einer Liste aller Veranstaltungen eines Managers,
 * sowie eine Liste mit allen Reservierungen zu diesen
 * @author anwender
 *
 */
@Named
@RequestScoped
public class ManagerRequest {

	@Inject
	private Security security;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	@Inject
	private ReservierungDAO reservierungDAO;
	@Inject
	private NutzerDAO nutzerDAO;
	private List<Veranstaltung> events;
	private List<Reservierung> reservierungen;
	
	/**
	 * Weiterleiten, falls die Authorisierung fehlschlägt, Laden der Veranstaltungen und der Reservierungen
	 */
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
		events = veranstaltungDAO.allbyManager(security.getCurrentUser()); 
		reservierungen = reservierungDAO.findByEvents(events);
	}
	
	/**
	 * Suchen aller Tickets zu einer Reservierung
	 * @param r Reservierung
	 * @return Liste mit Tickets zu der Reservierung
	 */
	public List<Ticket> getTickets(Reservierung r){
		List<Ticket> tickets = reservierungDAO.getTicketsByReservierungsID(r.getId());
		return tickets;
	}
/**
 * Suchen der Veranstaltung zu der Reservierung
 * @param r Reservierung
 * @return Veranstaltung zu der Reservierung
 */
	public Veranstaltung getEvent(Reservierung r){
		Veranstaltung event = veranstaltungDAO.findByID(getTickets(r).get(0).getVeranstaltung().getId());
		return event;
	}
	/**
	 * Suchen des Nutzers zu der Reservierung
	 * @param r Reservierung
	 * @return Nutzer zu der Reservierung
	 */
	public Nutzer getUser(Reservierung r){
		Nutzer user = nutzerDAO.findByID(r.getNutzer().getId());
		return user;
	}
	

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public List<Veranstaltung> getEvents() {
		return events;
	}

	public void setEvents(List<Veranstaltung> events) {
		this.events = events;
	}

	public List<Reservierung> getReservierungen() {
		return reservierungen;
	}

	public void setReservierungen(List<Reservierung> reservierungen) {
		this.reservierungen = reservierungen;
	}

	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}

	public void setReservierungDAO(ReservierungDAO reservierungDAO) {
		this.reservierungDAO = reservierungDAO;
	}

	public void setNutzerDAO(NutzerDAO nutzerDAO) {
		this.nutzerDAO = nutzerDAO;
	}



}
