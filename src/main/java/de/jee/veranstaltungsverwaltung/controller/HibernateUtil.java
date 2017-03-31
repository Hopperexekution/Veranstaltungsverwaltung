package de.jee.veranstaltungsverwaltung.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
public class HibernateUtil {
	private static HibernateUtil hibernateUtil = null;
	private static EntityManagerFactory emFactory = null;
	private HibernateUtil() {
		   emFactory = Persistence.createEntityManagerFactory("veranstaltungsverwaltung");
	}
	/**
	 * Erstellen der Entity Manager Factory. Implementiert als Singleton
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory(){
		if (hibernateUtil == null)
			hibernateUtil =  new HibernateUtil();
		return emFactory;
	}
	public static CriteriaBuilder getCriteriaBuilder(){
		CriteriaBuilder builder = HibernateUtil.getEntityManagerFactory().getCriteriaBuilder();
		return  builder;
	}
	public static EntityManager getEntityManager(){
		return HibernateUtil.getEntityManagerFactory().createEntityManager();
	}

}
