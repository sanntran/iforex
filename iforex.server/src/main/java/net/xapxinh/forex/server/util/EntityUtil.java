package net.xapxinh.forex.server.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import net.xapxinh.forex.server.entity.Pojo;

public final class EntityUtil {

	private EntityUtil() {
		// prevent installation
	}

	public static <T extends Pojo> boolean isInstance(T obj, Class<?> clazz) {
		if (obj instanceof HibernateProxy) {
			return (Hibernate.getClass(obj).equals(clazz));
		}
		return clazz.isInstance(obj);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Pojo> T castUnproxy(Pojo obj, Class<T> clazz) {
		if (obj instanceof HibernateProxy) {
			final Object implObj = ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getImplementation();
			if (clazz.isInstance(implObj)) {
				return (T)(implObj);
			}
		}
		else if (clazz.isInstance(obj)) {
			return (T)(obj);
		}
		throw new ClassCastException();
	}
}
