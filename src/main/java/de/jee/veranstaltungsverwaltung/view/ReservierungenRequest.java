package de.jee.veranstaltungsverwaltung.view;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.NutzerDAO;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;

@Named
@RequestScoped
public class ReservierungenRequest implements Serializable{
	
	@Inject
	private Security security;
	private List<Reservierung> reservierungen;
	
	public void init(){
		reservierungen = new ArrayList<Reservierung>();
		reservierungen.addAll(security.getCurrentUser().getReservierungen());
		
		
						
	}
	
	public int getAnzTickets(Reservierung r){
		ReservierungDAO dao = new ReservierungDAO();
		List<Ticket> tickets = dao.getTicketsByReservierungsID(r.getId());
		return tickets.size();
	}
	
	public Veranstaltung getEvent(Reservierung r){
		ReservierungDAO dao = new ReservierungDAO();
		List<Ticket> tickets = dao.getTicketsByReservierungsID(r.getId());
		return tickets.get(0).getVeranstaltung();
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
