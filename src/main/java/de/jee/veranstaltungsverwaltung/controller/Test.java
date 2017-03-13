package de.jee.veranstaltungsverwaltung.controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
@ManagedBean(name="test")
@RequestScoped
public class Test {
	private String message;
	public String doTest() {
		return "Test.jsf";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
