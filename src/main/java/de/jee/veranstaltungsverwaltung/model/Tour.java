package de.jee.veranstaltungsverwaltung.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tour")
public class Tour {
	@Id
	@Column(name="id", length=11)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(name="name", length=255)
	private String name;
	@Column(name="beschreibung")
	private String beschreibung;
	@OneToMany(mappedBy="tour", cascade=CascadeType.ALL)
	private Set<Veranstaltung> veranstaltungen;
	
	public Tour(){}
	public Tour(String name){
		this.name = name;
	}
	public Tour(String name, Set<Veranstaltung> veranstaltungen) {
		this.name = name;
		this.veranstaltungen = veranstaltungen;
		
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
	public Set<Veranstaltung> getVeranstaltungen() {
		return veranstaltungen;
	}
	public void setVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
		this.veranstaltungen = veranstaltungen;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
}
