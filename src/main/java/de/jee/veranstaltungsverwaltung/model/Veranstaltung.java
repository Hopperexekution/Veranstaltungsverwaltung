package de.jee.veranstaltungsverwaltung.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
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
	private String beschreibung;
	@Column(name="datum")
	private Date datum;
	@Column(name="ort")
	private String ort;
	@Column(name="istveroeffentlicht")
	private boolean istVeroeffentlicht;
	@ManyToOne
	@JoinColumn(name="manager", nullable=false)
	private Nutzer manager;
	@OneToMany(mappedBy="veranstaltung", cascade=CascadeType.ALL)
	private Set<Ticket> tickets;
	@ManyToOne
	@JoinColumn(name="tour", nullable=true)
	private Tour tour;
	
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
	
}
