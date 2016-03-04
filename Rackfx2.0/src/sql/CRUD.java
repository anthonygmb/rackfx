package sql;

import java.util.List;

import org.hibernate.Session;

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
	 * Methode de création d'entité hibernate
	 * 
	 * @param obj
	 */
	public static <T> void save(T obj) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		s.save(obj);
		s.getTransaction().commit();
		s.close();
	}

	/**
	 * Methode de mise à jour d'entité hibernate
	 * 
	 * @param obj
	 */
	public static <T> void update(T obj) {
		Session s = HibernateSetUp.getSession();
		s.beginTransaction();
		s.update(obj);
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
		s.delete(obj);
		s.getTransaction().commit();
		s.close();
	}
}
