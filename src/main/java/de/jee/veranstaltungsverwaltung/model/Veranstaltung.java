package de.jee.veranstaltungsverwaltung.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
@Entity
@Indexed
@Analyzer(impl = org.apache.lucene.analysis.standard.StandardAnalyzer.class)
@Table(name="veranstaltung")
public class Veranstaltung {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@DocumentId
	private int id;
	@Column(name="name")
	@Field(index=Index.YES, analyze=Analyze.YES, store = Store.YES)
	private String name;
	@Column(name="beschreibung")
	@Field(index=Index.YES, analyze=Analyze.YES, store = Store.YES)
	private String beschreibung;
	@Column(name="datum")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution=Resolution.MILLISECOND)
	private Date datum;
	@Column(name="ort")
	@Field(index=Index.YES, analyze=Analyze.YES, store = Store.YES)
	private String ort;
	@Column(name="istveroeffentlicht")
	private boolean istVeroeffentlicht;
	@Column(name="ticketpreis")
	private double ticketPreis;
	@ManyToOne
	@JoinColumn(name="manager", nullable=false)
	private Nutzer manager;
	@OneToMany(fetch=FetchType.EAGER, mappedBy="veranstaltung", cascade=CascadeType.ALL)
	private Set<Ticket> tickets;
	@ManyToOne
	@JoinColumn(name="tour", nullable=true)
	private Tour tour;
	@Transient
	private String zuReservierendeTickets;
	
	public Veranstaltung(){}
	public Veranstaltung(String name, String beschreibung, Date datum, boolean istVeroeffentlicht, Nutzer manager){
		this.name = name;
		this.beschreibung = beschreibung;
		this.datum = datum;
		this.istVeroeffentlicht = istVeroeffentlicht;
		this.manager = manager;
	}
	public Veranstaltung(String name, String beschreibung, Date datum, String ort, boolean istVeroeffentlicht, Nutzer manager){
		this.name = name;
		this.beschreibung = beschreibung;
		this.datum = datum;
		this.ort = ort;
		this.istVeroeffentlicht = istVeroeffentlicht;
		this.manager = manager;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public boolean getIstVeroeffentlicht() {
		return istVeroeffentlicht;
	}
	public void setIstVeroeffentlicht(boolean istVeroeffentlicht) {
		this.istVeroeffentlicht = istVeroeffentlicht;
	}
	public Nutzer getManager() {
		return manager;
	}
	public void setManager(Nutzer manager) {
		this.manager = manager;
	}
	public Set<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
	public Tour getTour() {
		return tour;
	}
	public void setTour(Tour tour) {
		this.tour = tour;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public double getTicketPreis() {
		return ticketPreis;
	}
	public void setTicketPreis(double ticketPreis) {
		this.ticketPreis = ticketPreis;
	}
	public int anzahlVerfuegbareTickets(){
		int anzahlMitReservierung = 0;
		for(Ticket ticket: this.getTickets()){
			if(ticket.getReservierung() == null)
				anzahlMitReservierung++;
		}
		return anzahlMitReservierung;
	}
	public List<Ticket> getVerfuegbareTickets(){
		List<Ticket> ticketListe = new ArrayList<Ticket>();
		for(Ticket ticket: this.getTickets()){
			if(ticket.getReservierung() == null)
				ticketListe.add(ticket);
		}
		return ticketListe;
	}
	public String getZuReservierendeTickets() {
		return zuReservierendeTickets;
	}
	public void setZuReservierendeTickets(String zuReservierendeTickets) {
		this.zuReservierendeTickets = zuReservierendeTickets;
	}
	
}
