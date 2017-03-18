package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;
public class VeranstaltungDAO {
		private static final Logger logger = Logger.getLogger(VeranstaltungDAO.class);
		
		public Veranstaltung findByID(int id){
			Veranstaltung veranstaltung = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				em.find(Veranstaltung.class, id);
			}
			catch(NullPointerException np){
				logger.log(Level.INFO, "Es befindet sich keine Veranstaltung mit der ID: " + id + " in der Datenbank");
				return null;
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltung mit der ID: " + id + " konnte nicht bezogen werden\n" + e.getStackTrace());
				return null;
			}
			finally{
				em.close();
			}
			return veranstaltung;
		}
		
		@SuppressWarnings("unchecked")
		public List<Veranstaltung> findByName(String name){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				FullTextEntityManager ftEM = Search.getFullTextEntityManager(em);
				QueryBuilder qb = ftEM.getSearchFactory()
					    .buildQueryBuilder().forEntity(Veranstaltung.class).get();
				org.apache.lucene.search.Query luceneQuery = qb.keyword().onField("name").matching(name).createQuery();
				javax.persistence.Query jpaQuery = ftEM.createFullTextQuery(luceneQuery, Veranstaltung.class);
				veranstaltungen = jpaQuery.getResultList();
			}
			catch(NullPointerException e){
				logger.log(Level.INFO, "Es konnte keine Veranstaltung mit dem Namen " + name + " gefunden werden");
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Es konnten Veranstaltungen mit dem Namen: " + name + " nicht bezogen werden\n" + e.getStackTrace());
			}
			finally{
				em.close();
			}
			return veranstaltungen;
		}
		
		public List<Veranstaltung> findByManager(Nutzer manager){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			if(!manager.getIstManager()){
				logger.log(Level.INFO, "Der übergebene Benutzer ist kein Manager, folglich kann er keine Veranstaltungen besitzen");
				return null;
			}
			try{
				if(manager.getBenutzername() == null){
					logger.log(Level.DEBUG, "Der Benutzername im Nutzer-Objekt ist null");
					return null;
				}
				em = HibernateUtil.getEntityManager();
				CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
				CriteriaQuery<Veranstaltung> query = cb.createQuery(Veranstaltung.class);
				Root<Veranstaltung> root = query.from(Veranstaltung.class);
				query.select(root);
				query.where(cb.equal(root.get("manager").as(Integer.class), manager.getId()));
				veranstaltungen = em.createQuery(query).getResultList();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Es konnten keine Veranstaltungen zu dem Manager mit dem Benutzernamen" 
						+ manager.getBenutzername() + " bezogen werden" + e.getStackTrace());
			}
			finally{
				em.close();
			}
			return veranstaltungen;
		}
		public List<Veranstaltung> findByManager(String benutzername){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			Nutzer manager = null;
			try{
				NutzerDAO nutzerDAO = new NutzerDAO();
				manager = nutzerDAO.findByUsername(benutzername);						
			}
			catch(NullPointerException np){
				logger.log(Level.INFO, "Es befindet sich kein Benutzer mit dem Benutzernamen " + benutzername + " in der Datenbank");
				return null;
			}
			if(!manager.getIstManager()){
				logger.log(Level.INFO, "Der übergebene Benutzer ist kein Manager, folglich kann er keine Veranstaltungen besitzen");
				return null;
			}
			try{
				em = HibernateUtil.getEntityManager();
				CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
				CriteriaQuery<Veranstaltung> query = cb.createQuery(Veranstaltung.class);
				Root<Veranstaltung> root = query.from(Veranstaltung.class);
				query.select(root);
				query.where(cb.equal(root.get("manager").as(Integer.class), manager.getId()));
				veranstaltungen = em.createQuery(query).getResultList();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Es konnten keine Veranstaltungen zu dem Manager mit dem Benutzernamen" 
						+ manager.getBenutzername() + " bezogen werden" + e.getStackTrace());
			}
			finally{
				em.close();
			}
			return veranstaltungen;
		}
		public int save(Veranstaltung veranstaltung){
			int returncode = 0;
			EntityManager em = null;
			try{
				if(veranstaltung.getName() == null)
					return -2;
				em = HibernateUtil.getEntityManager();
				em.getTransaction().begin();
				em.persist(veranstaltung);
				em.getTransaction().commit();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltung mit dem Namen: " + veranstaltung.getName() + " konnte nicht gespeichert werden\n" + e.getStackTrace());
				returncode = -1;
			}
			finally{
				em.close();
			}
			return returncode;
		}
}
