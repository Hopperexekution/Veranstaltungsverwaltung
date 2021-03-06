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
/**
 * 
 * Diese Bean erm�glicht das Anzeigen von Detailinformationen zu einer Veranstaltung
 * Zudem k�nnen Tickets reserviert und eine nicht ver�ffentlichte Veranstaltung bearbeitet werden
 *
 */
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

	
	/**
	 * Die Uhrzeit wird gesetzt
	 */
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
	
	/**
	 * setzen des Flags zur Bearbeitung auf true
	 */
	public void editVeranstaltung() {
		anzTickets = event.getTickets().size();
		canEdit = true;
		
	}

	/**
	 * Update auf die Veranstaltung
	 */
	@SuppressWarnings("deprecation")
	public void updateVeranstaltung() {
		System.out.println(event.getName());

		//wenn sihc die Anzahl Tickets ver�ndert
		if (anzTickets != event.getTickets().size()) {
			int anz = 0;
			System.out.println(event.getTickets().size());
			List<Ticket> tickets = new ArrayList<Ticket>();
			tickets.addAll(event.getTickets());
			//Alle Tickets der Veranstaltung l�schen
			while (anz < event.getTickets().size()) {
				ticketDAO.loescheTicket(tickets.get(anz));
				anz++;

			}
			//neue Tickets gem�� der �bergebenen Anzahl erzeugen
			for (int i = 0; i < anzTickets; i++) {
				ticketDAO.save(new Ticket(event));
			}
		}
		//Zusammenf�gen von Uhrzeit und Datum
		if(zeit.getHours() < 23)
			event.getDatum().setHours(zeit.getHours() + 1);
		else
			event.getDatum().setHours(0);
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
/**
 * Reservieren von Tickets
 * @param tickets Anzahl der zu reservierenden Tickets
 */
	public void reservieren(int tickets) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = null;
		Reservierung reservierung = new Reservierung(security.getCurrentUser());
		//Anzahl zu reservierender Tickets nicht angegeben
		if (event.getZuReservierendeTickets().equals("")) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Die Anzahl der zu reservierenden Tickets muss angegeben werden", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			return;
		}
		
		int anzahlTickets = Integer.parseInt(event.getZuReservierendeTickets());
		//Mehr Tickets angegeben, als verf�gbar sind
		if (anzahlTickets > event.getVerfuegbareTickets().size()) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Es k�nnen nicht mehr Tickets reserviert werden, als noch verf�gbar sind!", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			return;
		}
		int returncode = reservierungDAO.save(reservierung, event, anzahlTickets);
		//Returncode-Handling DB-seitig
		switch (returncode) {
		case -1:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Die Reservierung konnte nicht durchgef�hrt werden!", null);
			context.addMessage("detailVeranstaltungForm:resTickets", message);
			break;
		case 0:
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Es sind nicht mehr gen�gend Tickets f�r die Reservierung vorhanden. Da war wohl jemand schneller...",
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
