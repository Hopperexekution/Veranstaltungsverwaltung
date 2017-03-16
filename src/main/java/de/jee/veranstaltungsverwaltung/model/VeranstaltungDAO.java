package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import javax.persistence.EntityManager;
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
			catch(Exception e){
				
			}
			finally{
				em.close();
			}
			return veranstaltungen;
		}
}
