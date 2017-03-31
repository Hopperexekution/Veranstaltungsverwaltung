package de.jee.veranstaltungsverwaltung.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class HibernateUtil {
	private static HibernateUtil hibernateUtil = null;
	private static EntityManagerFactory emFactory = null;
	private HibernateUtil() {
		   emFactory = Persistence.createEntityManagerFactory("veranstaltungsverwaltung");
	}
	public static EntityManagerFactory getEntityManagerFactory(){
		if (hibernateUtil == null){
			hibernateUtil =  new HibernateUtil();
			try{
				EntityManager em = emFactory.createEntityManager();
				FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
				fullTextEntityManager.createIndexer().startAndWait();
				if(em != null){
					em.close();
				}
			}
			catch(Exception e){
				
			}
			finally{
			}
		}
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
