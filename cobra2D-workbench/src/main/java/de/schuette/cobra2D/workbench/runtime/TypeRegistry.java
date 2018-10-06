package de.schuette.cobra2D.workbench.runtime;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class TypeRegistry {

	/**
	 * Field of class array to define which class types should be collected from
	 * the classloader.
	 */
	protected Class<?>[] typeDefinitions;

	/**
	 * This registry is a mapping of typeDefinition to class. It maps all the
	 * types in the typeDefinition-field above to its concrete implementation.
	 */
	protected Hashtable<Class<?>, List<Class<?>>> typeRegistry;

	public TypeRegistry(Class<?>[] typeDefinitions) {
		if (typeDefinitions == null)
			throw new IllegalArgumentException(
					"The array of type definitions cannot be null.");
		this.typeDefinitions = typeDefinitions;
		this.typeRegistry = initializeRegistry();
	}

	/**
	 * Creates an empty type registry for initialization of this instance.
	 * 
	 * @return Returns an empty type registry.
	 */
	protected Hashtable<Class<?>, List<Class<?>>> initializeRegistry() {
		Hashtable<Class<?>, List<Class<?>>> newRegistry = new Hashtable<Class<?>, List<Class<?>>>();

		for (int i = 0; i < typeDefinitions.length; i++) {
			Class<?> type = typeDefinitions[i];
			List<Class<?>> impls = new ArrayList<Class<?>>();
			newRegistry.put(type, impls);
		}

		return newRegistry;
	}

	/**
	 * Adds an implementation of a given type to this registry. If the given
	 * implementation is a subtype of the given type class, this implementation
	 * is accepted and added to the registry. Otherwise it will not be added.
	 * 
	 * @param type
	 *            The type class, the implementation must be a subtype to.
	 * @param implementation
	 *            A subtype to the given type.
	 * @return Returns true, if the implementation is accepted. Otherwise false.
	 */
	public boolean addImplementationType(Class<?> type, Class<?> implementation) {
		boolean accepted = InstanceUtil.isSubtype(type, implementation);
		if (accepted) {
			List<Class<?>> impls = typeRegistry.get(type);
			impls.add(implementation);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the list of implementations to a given supertype.
	 * 
	 * @param type
	 *            The supertype of the implementations.
	 * @return The list of all classes that are subtype to the given type. If
	 *         this supertype does not exist in the registry an empty list is
	 *         returned.
	 */
	public List<Class<?>> getImplementationsByType(Class<?> type) {
		if (typeRegistry.containsKey(type)) {
			List<Class<?>> impls = typeRegistry.get(type);
			List<Class<?>> copy = new ArrayList<Class<?>>(impls);
			return copy;
		} else {
			return new ArrayList<Class<?>>();
		}
	}

	public Class<?>[] getTypeDefinitions() {
		return typeDefinitions;
	}

	public void loadClasspathTypeRegistry() {
		this.typeRegistry = initializeRegistry();

		for (Class<?> type : typeDefinitions) {
			URL url = ClasspathHelper.forClass(type);
			Reflections reflections = new Reflections(
					new ConfigurationBuilder().addUrls(url));

			Set<?> entities = reflections.getSubTypesOf(type);
			@SuppressWarnings("unchecked")
			Set<Class<?>> implClasses = (Set<Class<?>>) entities;
			for (Class<?> c : implClasses) {
				this.addImplementationType(type, c);
			}
		}
	}

	@Override
	public String toString() {
		String outStr = "Type registry:\n";

		Set<Class<?>> keySet = typeRegistry.keySet();
		for (Class<?> c : keySet) {
			List<Class<?>> classes = typeRegistry.get(c);
			outStr += classes.size() + " members found on type " + c.getName()
					+ "\n";
		}

		return outStr;
	}
}
