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
@Table(name="nutzer")
public class Nutzer {
	@Id
	@Column(name="id",length=11)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(name="benutzername",nullable=false,unique=true,length=255)
	private String benutzername;
	@Column(name="passwort",nullable=false,length=60)
	private String passwort;
	@Column(name="istmanager",nullable=false)
	private boolean istManager;
	@Column(name="email",nullable=false,length=255)
	private String email;
	@OneToMany(mappedBy="nutzer", cascade=CascadeType.ALL)
	private Set<Reservierung> reservierungen;
	@OneToMany(mappedBy="nutzer", cascade=CascadeType.ALL)
	private Set<Veranstaltung> veranstaltungen;
	
	public Nutzer(){}
	public Nutzer(String benutzername, String passwort, boolean istManager, String email){
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.istManager = istManager;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBenutzername() {
		return benutzername;
	}
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public boolean getIstManager() {
		return istManager;
	}
	public void setIstManager(boolean istManager) {
		this.istManager = istManager;
	}
	public Set<Reservierung> getReservierungen() {
		return reservierungen;
	}
	public void setReservierungen(Set<Reservierung> reservierungen) {
		this.reservierungen = reservierungen;
	}
	public Set<Veranstaltung> getVeranstaltungen() {
		return veranstaltungen;
	}
	public void setVeranstaltungen(Set<Veranstaltung> veranstaltungen) {
		this.veranstaltungen = veranstaltungen;
	}
	
}
