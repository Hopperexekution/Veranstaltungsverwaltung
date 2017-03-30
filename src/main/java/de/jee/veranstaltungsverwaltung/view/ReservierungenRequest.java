package de.jee.veranstaltungsverwaltung.view;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;

@Named
@RequestScoped
public class ReservierungenRequest implements Serializable{
	
	@Inject
	private Security security;
	private List<Reservierung> reservierungen;
	
	public void init(){
		System.out.println("init");
		ReservierungDAO dao = new ReservierungDAO();
		
		reservierungen = dao.selectbyUserID(security.getCurrentUser());
		System.out.println(reservierungen.size());
		
		
		
						
	}
	
	public List<Ticket> getTickets(Reservierung r){
		System.out.println("anz");
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

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public List<Reservierung> getReservierungen() {
		return reservierungen;
	}

	public void setReservierungen(List<Reservierung> reservierungen) {
		this.reservierungen = reservierungen;
	}
	
	
}
