package de.jee.veranstaltungsverwaltung.model;

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

@Entity
@Table(name="reservierung")
public class Reservierung {
	@Id
	@Column(name="id", length=11)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="nutzer",nullable=false)
	private Nutzer nutzer;
	@OneToMany(mappedBy="reservierung", cascade=CascadeType.ALL)
	private Set<Ticket> tickets;
	
	public Reservierung(){}
	public Reservierung(Nutzer nutzer){
		this.nutzer = nutzer;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Nutzer getNutzer() {
		return nutzer;
	}
	public void setNutzer(Nutzer nutzer) {
		this.nutzer = nutzer;
	}
	public Set<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
	
}
