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
	@Column(name="istmanager", nullable=false)
	private boolean istManager;
	@OneToMany(mappedBy="nutzer", cascade=CascadeType.ALL)
	private Set<Reservierung> reservierungen;
	
	public Nutzer(){}
	public Nutzer(String benutzername, String passwort, boolean istManager){
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.istManager = istManager;
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
	
}
