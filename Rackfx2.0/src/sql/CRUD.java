package sql;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import controller.MainViewController;
import model.Groupe;
import model.Rencontre;

public abstract class CRUD {

	/**
	 * Methode de récupération de liste d'entité hibernate
	 * 
	 * @param table
	 * @return list_obj
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAll(String table) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		List<T> list_obj = s.createQuery("from " + table).list();
		s.getTransaction().commit();
		s.close();
		return list_obj;
	}

	/**
	 * Methode de récupération de liste d'entité hibernate avec close where.
	 * 
	 * @param table
	 * @param parentId
	 * @param id
	 * @return list_obj
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllWhere(String table, String parentId, long id) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		List<T> list_obj = s.createQuery("from " + table + " where " + parentId + " = " + id).list();
		s.getTransaction().commit();
		s.close();
		return list_obj;
	}

	/**
	 * Methode de comptage d'entités
	 * 
	 * @param table
	 * @param parentId
	 * @param id
	 * @return count
	 */
	public static <T> long count(String table, String parentId, String id) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		Query query = s.createQuery("select count(*) from " + table + " where " + parentId + " = " + id);
		Long count = (Long) query.uniqueResult();
		s.getTransaction().commit();
		s.close();
		return count;
	}

	/**
	 * Methode de création ou de mise à jour d'entité hibernate
	 * 
	 * @param obj
	 */
	public static <T> void saveOrUpdate(T obj) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		s.saveOrUpdate(obj);
		s.getTransaction().commit();
		s.close();
	}

	/**
	 * Methode de suppression d'entité hibernate
	 * 
	 * @param obj
	 */
	public static <T> void delete(T obj) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		if (obj instanceof Groupe) {
			Groupe groupeH = (Groupe) s.load(Groupe.class,
					MainViewController.getInstance().tv_reper.getSelectionModel().getSelectedItem().getGroupeId());
			s.delete(groupeH);
		} else if (obj instanceof Rencontre) {
			Rencontre rencontreH = (Rencontre) s.load(Rencontre.class,
					MainViewController.getInstance().tv_planif.getSelectionModel().getSelectedItem().getRencontreId());
			s.delete(rencontreH);
		} else {
			s.delete(obj);
		}
		s.getTransaction().commit();
		s.close();
	}
}
