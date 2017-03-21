package de.jee.veranstaltungsverwaltung.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ticket")
public class Ticket {
	@Id
	@Column(name="id", length=11)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="veranstaltung", nullable=false)
	private Veranstaltung veranstaltung;
	@ManyToOne
	@JoinColumn(name="reservierung")
	private Reservierung reservierung;
	
	public Ticket(){}
	public Ticket(Veranstaltung veranstaltung){
		this.veranstaltung = veranstaltung;
	}
	public Ticket(Veranstaltung veranstaltung, Reservierung reservierung){
		this.veranstaltung = veranstaltung;
		this.reservierung = reservierung;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Veranstaltung getVeranstaltung() {
		return veranstaltung;
	}
	public void setVeranstaltung(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}
	public Reservierung getReservierung() {
		return reservierung;
	}
	public void setReservierung(Reservierung reservierung) {
		this.reservierung = reservierung;
	}
	
}
