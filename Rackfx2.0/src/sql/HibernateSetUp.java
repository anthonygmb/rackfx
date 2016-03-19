package sql;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSetUp {

	/** The Constant sessionFactory. */
	private static final SessionFactory sessionFactory;

	static {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (HibernateException ex) {
			throw new RuntimeException("Problème de configuration : " + ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	/**
	 * Shutdown.
	 */
	public static void shutdown() {
		sessionFactory.close();
	}
}
