package de.jee.veranstaltungsverwaltung.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.NutzerDAO;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;

@Named
@RequestScoped
public class ManagerRequest {

	@Inject
	private Security security;

	private List<Veranstaltung> events;
	private List<Reservierung> reservierungen;
	
	public void init(){
		
		VeranstaltungDAO dao = new VeranstaltungDAO();
		events = dao.allbyManager(security.getCurrentUser()); 
		
		ReservierungDAO rdao = new ReservierungDAO();
		reservierungen = rdao.findByEvents(events);
	}
	
	public List<Ticket> getTickets(Reservierung r){
		
		ReservierungDAO dao = new ReservierungDAO();
		List<Ticket> tickets = dao.getTicketsByReservierungsID(r.getId());
		System.out.println(tickets.size());
		return tickets;
	}

	public Veranstaltung getEvent(Reservierung r){
		System.out.println("event");

		VeranstaltungDAO vdao = new VeranstaltungDAO();
		Veranstaltung event = vdao.findByID(getTickets(r).get(0).getVeranstaltung().getId());
		
		return event;
	}
	public Nutzer getUser(Reservierung r){
		System.out.println("event");

		NutzerDAO dao = new NutzerDAO();
		Nutzer user = dao.findByID(r.getNutzer().getId());
		
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



}
