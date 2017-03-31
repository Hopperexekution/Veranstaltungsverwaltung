package de.jee.veranstaltungsverwaltung.service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

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
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Ticket;
@Named
@ApplicationScoped
public class VeranstaltungDAO implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 8376080847979098662L;
		private static final Logger logger = Logger.getLogger(VeranstaltungDAO.class);
		/**
		 * Findet eine Veranstaltung auf Basis der ID
		 * @param id Die ID der Veranstaltung
		 * @return Die Veranstaltung
		 */
		public Veranstaltung findByID(int id){
			Veranstaltung veranstaltung = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				veranstaltung = em.find(Veranstaltung.class, id);
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
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltung;
		}
		/**
		 * Findet die veröffentlichen Veranstaltungen nach dem Namen
		 * @param name Name der Veranstaltung
		 * @return Die Veranstaltungsliste
		 */
		public List<Veranstaltung> findByName(String name){
			List<Veranstaltung> veranstaltungen = null;
			try{
				veranstaltungen = findByName(name, null, null, true, 0);
			}
			catch(NullPointerException e){
			}
			return veranstaltungen;
		}
		/**
		 * Findet die veröffentlichen Veranstaltungen nach dem Namen und einer mindestens verfügbaren Anzahl Tickets
		 * @param name Name der Veranstaltung
		 * @param anzahlTickets Anzahl der Tickets 
		 * @return Die Veranstaltungsliste
		 */
		public List<Veranstaltung> findByName(String name, int anzahlTickets){
			List<Veranstaltung> veranstaltungen = null;
			try{
				veranstaltungen = findByName(name, null, null, true, anzahlTickets);
			}
			catch(NullPointerException e){
				
			}
			return veranstaltungen;
		}
		/**
		 * Findet die veröffentlichten Veranstaltungen nach dem Namen und einem Datumsbereich
		 * @param name Name der Veranstaltung
		 * @param vonDatum Startdatum
		 * @param bisDatum Endedatum 
		 * @return Die Veranstaltungsliste
		 */
		public List<Veranstaltung> findByName(String name, Date vonDatum, Date bisDatum){
			List<Veranstaltung> veranstaltungen = null;
			try{
				veranstaltungen = findByName(name, vonDatum, bisDatum, true, 0);
			}
			catch(NullPointerException e){
				
			}
			return veranstaltungen;
		}
		/**
		 * Findet die Veranstaltungen nach dem Namen und einem Datumsbereich, ob sie veröffentlicht sind, und der mindestens verfügbaren Tickets.
		 * Diese Methode führt eine Lucene-Suche durch, die auch ungenaue Suchbegriffe findet. In ihr steckt die Logik, auf die sich die anderen find by Name Methoden beziehen
		 * @param name Name der Veranstaltung
		 * @param vonDatum Startdatum
		 * @param bisDatum Endedatum
		 * @param istVeröffentlicht Legt fest ob die Veranstaltung veröffentlicht sein muss
		 * @param anzahlTickets Lebt die mindestens verfügbaren Tickets fest 
		 * @return Die Veranstaltungsliste
		 */
		@SuppressWarnings("unchecked")
		public List<Veranstaltung> findByName(String name, Date vonDatum, Date bisDatum, boolean istVeroeffentlicht, int anzahlTickets){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				FullTextEntityManager ftEM = Search.getFullTextEntityManager(em);
				QueryBuilder qb = ftEM.getSearchFactory()
					    .buildQueryBuilder().forEntity(Veranstaltung.class).get();
				org.apache.lucene.search.Query luceneQuery = qb.keyword().fuzzy().onFields("name","ort","beschreibung").matching(name).createQuery();
				if(vonDatum != null)
					luceneQuery =qb.bool().must(luceneQuery).must(qb.range().onField("datum").below(vonDatum).createQuery()).not().createQuery();
				if(bisDatum != null)
					luceneQuery = qb.bool().must(luceneQuery).must(qb.range().onField("datum").below(bisDatum).createQuery()).createQuery();
				if(istVeroeffentlicht)
					luceneQuery = qb.bool().must(luceneQuery).must(qb.keyword().onField("istVeroeffentlicht").matching("true").createQuery()).createQuery();
				else
					luceneQuery = qb.bool().must(luceneQuery).must(qb.keyword().onField("istVeroeffentlicht").matching("false").createQuery()).createQuery();
				luceneQuery = qb.bool().must(luceneQuery).must(qb.range().onField("datum").above(System.currentTimeMillis()).createQuery()).createQuery();
				javax.persistence.Query jpaQuery = ftEM.createFullTextQuery(luceneQuery, Veranstaltung.class);
				List<Veranstaltung> veranstaltungenOhneTicketKriterium = jpaQuery.getResultList();
				if(anzahlTickets == 0)
					veranstaltungen = veranstaltungenOhneTicketKriterium;
				else{
					veranstaltungen = new ArrayList<Veranstaltung>();
					for(Veranstaltung veranstaltung: veranstaltungenOhneTicketKriterium){
						int anzahlVerfuegbareTickets = 0;
						for(Ticket ticket : veranstaltung.getTickets()){
							if(ticket.getReservierung() == null)
								anzahlVerfuegbareTickets++;
						}
						if(anzahlVerfuegbareTickets >= anzahlTickets)
							veranstaltungen.add(veranstaltung);
					}
				}
			}
			catch(NullPointerException e){
				logger.log(Level.INFO, "Es konnte keine Veranstaltung mit dem Namen " + name + " gefunden werden");
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Es konnten Veranstaltungen mit dem Namen: " + name + " nicht bezogen werden\n" + e.getStackTrace());
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		/**
		 * Findet die Veranstaltungen die als letztes erstellt wurden.
		 * @return
		 */
		public List<Veranstaltung> latestFive(){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				Date date = new Date(System.currentTimeMillis());
				CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
				CriteriaQuery<Veranstaltung> query = cb.createQuery(Veranstaltung.class);
				Root<Veranstaltung> root = query.from(Veranstaltung.class);
				query.select(root);
				query.orderBy(cb.desc(root.get("id")));
				query.where(cb.equal(root.get("istVeroeffentlicht").as(Integer.class), 1));
				query.where(cb.greaterThan(root.get("datum"), date ));
				veranstaltungen = em.createQuery(query).setMaxResults(5).getResultList();				
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die aktuellsten 5 Veranstaltungen konnten nicht geladen werden\n" + e.getStackTrace());
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
			
		}
		/**
		 * Findet die meist reservierten Veranstaltungen
		 * @return Die meist resrvierten Veranstaltungen
		 */
		@SuppressWarnings("unchecked")
		
		public List<Veranstaltung> mostReserved(){
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				String hql = "from Veranstaltung as v left join v.tickets as t where t.reservierung is not null group by v.id order by count(r)";
				veranstaltungen = em.createQuery(hql).setMaxResults(5).getResultList();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltungen mit den meisten Reservierungen konnten nicht");
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		
		/**
		 * Findet die Veranstaltungen zu einem Manager
		 * @param manager Der Manager nach dem die Veranstaltungen gesucht werden sollen
		 * @return
		 */
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
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		/**
		 * Findet die Veranstaltungen die von einem Manager erstellt wurden auf Basis des Benutzernamen
		 * @param benutzername
		 * @return
		 */
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
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		/**
		 * Findet alle Veranstaltungen von einem Manager
		 * @param manager Der Manager
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public List<Veranstaltung> allbyManager(Nutzer manager){
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
				String query ="from Veranstaltung as v where v.manager="+ manager.getId();
				
				veranstaltungen = em.createQuery(query).getResultList();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Es konnten keine Veranstaltungen zu dem Manager mit dem Benutzernamen" 
						+ manager.getBenutzername() + " bezogen werden" + e.getStackTrace());
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		/**
		 * Speichert eine Veranstaltung in der Datenbank
		 * @param veranstaltung Die Veranstaltung die gespeichter werden soll
		 * @return Der Returncode. 0 = erfolgreich < 0 = nicht erfolgreich
		 */
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
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return returncode;
		}
		/**
		 * Speichert eine Veranstaltung und erzeugt dazu Tickets
		 * @param veranstaltung Die Veranstaltung
		 * @param anzahlTickets Die Anzahl an Tickets die erzeugt werden sollen
		 * @return Der Returncode. 0 = erfolgreich < 0 = nicht erfolgreich
		 */
		public int save(Veranstaltung veranstaltung, int anzahlTickets){
			int returncode = 0;
			EntityManager em = null;
			try{
				if(veranstaltung.getName() == null)
					return -2;
				em = HibernateUtil.getEntityManager();
				em.getTransaction().begin();
				em.persist(veranstaltung);
				for(int i = 0; i < anzahlTickets; i++)
					em.persist(new Ticket(veranstaltung));
				em.getTransaction().commit();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltung mit dem Namen: " + veranstaltung.getName() + " konnte nicht gespeichert werden\n" + e.getStackTrace());
				returncode = -1;
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return returncode;
		}
		/**
		 * Aktualisiert die Änderungen an einer Veranstaltung
		 * @param veranstaltung Die Veranstaltung
		 * @return
		 */
		public int update(Veranstaltung veranstaltung){
			System.out.println("Methode update");
			int returncode = 0;
			EntityManager em = null;
			try{
				if(veranstaltung.getName() == null)
					return -2;
				em = HibernateUtil.getEntityManager();
				em.getTransaction().begin();
				em.merge(veranstaltung);
				em.getTransaction().commit();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltung mit dem Namen: " + veranstaltung.getName() + " konnte nicht aktualisiert werden\n" + e.getStackTrace());
				returncode = -1;
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return returncode;
		}
		/**
		 * Gibt alle Veranstaltungen zurück die veröffentlicht sind und die später als zum jetztigen Zeitpunkt stattfinden
		 * @return Die Veranstaltungen
		 */
		@SuppressWarnings("unchecked")
		public List<Veranstaltung> all() {
			List<Veranstaltung> veranstaltungen = null;
			EntityManager em = null;
			try{
				em = HibernateUtil.getEntityManager();
				Date date = new Date(System.currentTimeMillis());
				String hql = "from Veranstaltung as v where v.istVeroeffentlicht=1 and v.datum > :now";
				veranstaltungen = em.createQuery(hql).setParameter("now", date).setMaxResults(5).getResultList();
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltungen mit den meisten Reservierungen konnten nicht");
			}
			finally{
				if(em != null)
					em.close();
				else
					logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

			}
			return veranstaltungen;
		}
		
}
