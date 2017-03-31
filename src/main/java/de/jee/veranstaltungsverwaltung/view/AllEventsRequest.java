package de.jee.veranstaltungsverwaltung.view;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;

@Named
@RequestScoped
public class AllEventsRequest {
	
	@Inject
	private Security security;
	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}
}
