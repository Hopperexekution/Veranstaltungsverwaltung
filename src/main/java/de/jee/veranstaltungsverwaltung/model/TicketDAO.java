package de.jee.veranstaltungsverwaltung.model;

import org.hibernate.SessionFactory;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class TicketDAO {
	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

}
