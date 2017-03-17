package de.jee.veranstaltungsverwaltung.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;

@Named
@SessionScoped
public class Security implements Serializable{
	

	private Nutzer currentUser = null;
	
	public Nutzer getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Nutzer currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isLoggedIn(){
		return currentUser!=null;
	}
	public boolean isManager(){
		return isLoggedIn()&&currentUser.getIstManager();
	}
	
	public void login(Nutzer user){
		currentUser = user;
	}
	public Nutzer getUser(){
		return currentUser;
	}
	public void logout(){
		currentUser=null;
	}

}
