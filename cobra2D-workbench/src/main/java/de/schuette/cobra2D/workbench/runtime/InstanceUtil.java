package de.schuette.cobra2D.workbench.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import de.schuette.cobra2D.entity.editing.Editable;
import de.schuette.cobra2D.workbench.gui.entityEditor.EntityModel;

public class InstanceUtil {

	/**
	 * Checks whether the expected subtype is a subtype of the given type.
	 * 
	 * @param type
	 * @param implementation
	 * @return
	 */
	public static boolean isSubtype(Class<?> type, Class<?> expectedSubtype) {
		boolean equal = type.equals(expectedSubtype);
		boolean correctType = type.isAssignableFrom(expectedSubtype);
		boolean isAbstract = Modifier
				.isAbstract(expectedSubtype.getModifiers());
		boolean isInterface = Modifier.isInterface(expectedSubtype
				.getModifiers());
		return !equal && correctType && !isAbstract && !isInterface;
	}

	/**
	 * This method creates an instance of a class by using the default
	 * constructor. Null is returned if it is not possible to instatiate this
	 * class, due to security exceptions, missing methods etc.
	 * 
	 * @return Returns the instance of the given class or null if it fails to
	 *         instantiate this class.
	 */
	public static <T> T createDefaultInstance(Class<? extends T> clazz)
			throws Exception {
		try {
			Constructor<? extends T> constructor = clazz.getConstructor();
			T newInstance = constructor.newInstance();
			return newInstance;
		} catch (Exception e) {
			throw new Exception("Cannot instantiate class of type "
					+ clazz.getName(), e);
		}
	}

	/**
	 * This method creates an instance of a class by using the constructor that
	 * matches the type of the given arguments. Null is returned if it is not
	 * possible to instatiate this class, due to security exceptions, missing
	 * methods etc.
	 * 
	 * @return Returns the instance of the given class or null if it fails to
	 *         instantiate this class.
	 */
	public static <T> T createInstance(Class<? extends T> clazz, Object... args)
			throws Exception {
		try {
			Class<?>[] types = new Class<?>[args.length];
			for (int i = 0; i < types.length; i++) {
				types[i] = args[i].getClass();
			}

			Constructor<? extends T> constructor = clazz.getConstructor(types);
			T newInstance = constructor.newInstance(args);
			return newInstance;
		} catch (Exception e) {
			throw new Exception("Cannot instantiate class of type "
					+ clazz.getName(), e);
		}
	}

	/**
	 * Checks an entity, whether it is defined as editable via annotation. If an
	 * entity is editable the following aspects can be accessed via the entity
	 * editor: - Texture of the entity - Entity points to create a collision
	 * model
	 * 
	 * @param forModel
	 *            The entity model specifying the entity type that is to check
	 *            for editability.
	 * @return
	 */
	public static boolean isEditable(EntityModel forModel) {
		Class<?> clazz = forModel.getClass();
		return isEditable(clazz);
	}

	/**
	 * Checks an entity, whether it is defined as editable via annotation. If an
	 * entity is editable the following aspects can be accessed via the entity
	 * editor: - Texture of the entity - Entity points to create a collision
	 * model
	 * 
	 * @param clazz
	 *            The entity class, specifying the entity type that is to check
	 *            for editability.
	 * @return
	 */
	public static boolean isEditable(Class<?> clazz) {
		Annotation[] annos = clazz.getAnnotations();
		for (Annotation anno : annos) {
			if (anno.annotationType().equals(Editable.class)) {
				return true;
			}
		}

		return false;
	}
}
