package de.jee.veranstaltungsverwaltung.controller;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;

public class HibernateUtil {
	private static SessionFactory sessionFactory ;
    static {
   	Configuration configuration = new Configuration().configure();
   	StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
   	                              .applySettings(configuration.getProperties());
       sessionFactory = configuration.addAnnotatedClass(Nutzer.class).addAnnotatedClass(Reservierung.class).addAnnotatedClass(Ticket.class).addAnnotatedClass(Veranstaltung.class).buildSessionFactory(builder.build());
    }
   public static SessionFactory getSessionFactory() {
       return sessionFactory;
   }


}
