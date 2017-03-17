package de.jee.veranstaltungsverwaltung.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;


@Named
@ApplicationScoped
public class EventService {

	public static void addEvent(String titel, String beschreibung, Date datum, boolean published, Nutzer nutzer) {
		VeranstaltungDAO dao= new VeranstaltungDAO();
		Veranstaltung event = new Veranstaltung(titel, beschreibung, datum, published, nutzer);
		//dao.save(event);
		System.out.println("Hier muss die Veranstaltung gespeichert werden: " + titel + " " + beschreibung + " " + datum.toString() + " " +  (published?"Veröffentlicht ":"") + nutzer.getId()  );
		
	}

	public static List<Veranstaltung> holeAlleVeranstaltungen() {
		// TODO Auto-generated method stub
		VeranstaltungDAO dao = new VeranstaltungDAO();
		System.out.println("Hier sollen alle Veranstaltungen geholt werden");
		List<Veranstaltung> events = new ArrayList<Veranstaltung>();
		return events;
		
	}


	
	

}
