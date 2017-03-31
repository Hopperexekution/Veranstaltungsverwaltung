package de.jee.veranstaltungsverwaltung.view;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;


@Named
@RequestScoped
public class ReservierungenRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -207215583642055024L;
	@Inject
	private Security security;
	@Inject
	private ReservierungDAO reservierungDAO;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	private List<Reservierung> reservierungen;
	
	public void init(){
		if(!security.isLoggedIn()){
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		reservierungen = reservierungDAO.selectbyUserID(security.getCurrentUser());
		System.out.println(reservierungen.size());		
	}
	
	public List<Ticket> getTickets(Reservierung r){
		List<Ticket> tickets = reservierungDAO.getTicketsByReservierungsID(r.getId());
		return tickets;
	}
	
	public Veranstaltung getEvent(Reservierung r){
		Veranstaltung event = veranstaltungDAO.findByID(getTickets(r).get(0).getVeranstaltung().getId());
		
		return event;
	}
	
	public double getGesamtPreis(Reservierung r){
		return getEvent(r).getTicketPreis()*getTickets(r).size();
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

	public void setReservierungDAO(ReservierungDAO reservierungDAO) {
		this.reservierungDAO = reservierungDAO;
	}

	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}
	
	
}
