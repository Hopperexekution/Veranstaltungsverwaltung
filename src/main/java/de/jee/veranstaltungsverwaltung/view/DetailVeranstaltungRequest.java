package de.jee.veranstaltungsverwaltung.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.ReservierungDAO;
import de.jee.veranstaltungsverwaltung.service.TicketDAO;
import de.jee.veranstaltungsverwaltung.service.VeranstaltungDAO;

@Named
@ViewScoped
public class DetailVeranstaltungRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6137562470997474652L;
	@Inject
	private Security security;
	@Inject
	private TicketDAO ticketDAO;
	@Inject
	private ReservierungDAO reservierungDAO;
	@Inject
	private VeranstaltungDAO veranstaltungDAO;
	private Veranstaltung event;

	private boolean canEdit;

	private int tickets;

	private int anzTickets;
	private Date zeit;

	
	@SuppressWarnings("deprecation")
	public void init(){
		zeit = new Date();
		zeit.setHours(event.getDatum().getHours());
		zeit.setMinutes(event.getDatum().getMinutes());
	}
	public Date getZeit() {
		return zeit;
	}
	public void setZeit(Date zeit) {
		this.zeit = zeit;
	}
	public void editVeranstaltung() {
		anzTickets = event.getTickets().size();
		canEdit = true;
		
	}

	@SuppressWarnings("deprecation")
	public void updateVeranstaltung() {
		System.out.println(event.getName());

		if (anzTickets != event.getTickets().size()) {
			int anz = 0;
			// List<Ticket> tickets = tdao.alleTicketsEinerVeranstaltung(event);

			System.out.println(event.getTickets().size());
			List<Ticket> tickets = new ArrayList<Ticket>();
			tickets.addAll(event.getTickets());
			while (anz < event.getTickets().size()) {
				ticketDAO.loescheTicket(tickets.get(anz));
				anz++;

			}
			// event.getTickets().clear();
			for (int i = 0; i < anzTickets; i++) {
				ticketDAO.save(new Ticket(event));
			}
		}
		event.getDatum().setHours(zeit.getHours());
		event.getDatum().setMinutes(zeit.getMinutes());
		veranstaltungDAO.update(event);
		
		canEdit = false;
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("detail_veranstaltung.jsf?id="+event.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void reservieren(int tickets) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = null;
		Reservierung reservierung = new Reservierung(security.getCurrentUser());
		if (event.getZuReservierendeTickets().equals("")) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Die Anzahl der zu reservierenden Tickets muss angegeben werden", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			return;
		}
		int anzahlTickets = Integer.parseInt(event.getZuReservierendeTickets());
		if (anzahlTickets > event.getVerfuegbareTickets().size()) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Es können nicht mehr Tickets reserviert werden, als noch verfügbar sind!", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			return;
		}
		int returncode = reservierungDAO.save(reservierung, event, anzahlTickets);
		switch (returncode) {
		case -1:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Die Reservierung konnte nicht durchgeführt werden!", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			break;
		case 0:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Es sind nicht mehr genügend Tickets für die Reservierung vorhanden. Da war wohl jemand schneller...",
					null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			break;
		default:
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Die Tickets wurden erfolgreich reserviert. Die Reservierungs-ID lautet: " + returncode, null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			break;
		}

	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Veranstaltung getEvent() {
		return event;
	}

	public void setEvent(Veranstaltung event) {
		this.event = event;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

	public int getAnzTickets() {
		return anzTickets;
	}

	public void setAnzTickets(int anzTickets) {
		this.anzTickets = anzTickets;
	}
	public void setTicketDAO(TicketDAO ticketDAO) {
		this.ticketDAO = ticketDAO;
	}
	public void setReservierungDAO(ReservierungDAO reservierungDAO) {
		this.reservierungDAO = reservierungDAO;
	}
	public void setVeranstaltungDAO(VeranstaltungDAO veranstaltungDAO) {
		this.veranstaltungDAO = veranstaltungDAO;
	}

}
