/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataPrimitiveDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BeanValueFactory fills a bean in by calling every setter it has with a value of the right type.  It is
 * what lets {@link OpenMetadataBeanContractTest} cover every bean in the framework rather than the handful
 * anyone would write by hand.
 * <br><br>
 * Two things matter about the values it makes.  They are derived from a <b>seed</b>, so that asking for two
 * differently seeded copies of a bean gives two beans that differ in every field - which is what makes
 * "these two are not equal" a real test rather than a coincidence.  And they are <b>deterministic</b>, so a
 * failure is reproducible and is not a different bean each run.
 * <br><br>
 * A setter whose parameter type the factory does not know how to make is left alone and counted.  Failing
 * instead would make every new property type a build break in this class rather than in the bean, but a
 * silent skip would let coverage drain away as the model grows, so the count is asserted on by the test.
 */
class BeanValueFactory
{
    /**
     * A fixed instant, so a Date value is reproducible.  Any value does; this one is simply not zero and
     * not now.
     */
    private static final long BASE_TIME = 1_600_000_000_000L;

    /**
     * How far down a nest of beans to keep filling in.  Beans refer to each other and in places to
     * themselves - {@code OpenMetadataRootElement} holds lists of related elements which hold elements -
     * so the recursion has to stop somewhere.  Two levels is enough to put a non-null value in every
     * setter of the bean under test and of the beans it holds.
     */
    private static final int MAX_DEPTH = 2;

    private final Set<String> beanClassNames;
    private int               populatedSetters = 0;
    private final Set<String> skippedTypes     = new LinkedHashSet<>();
    private int               skippedSetters   = 0;


    /**
     * Create a factory that treats the supplied class names as beans it may build and fill in.
     *
     * @param beanClassNames fully qualified names of the framework's beans
     */
    BeanValueFactory(Set<String> beanClassNames)
    {
        this.beanClassNames = beanClassNames;
    }


    /**
     * Create an instance of the supplied bean with every setter it has called.
     *
     * @param beanClass bean to build
     * @param seed distinguishes one filled-in bean from another
     * @return the bean
     * @throws ReflectiveOperationException the bean could not be created
     */
    Object filledBean(Class<?> beanClass, int seed) throws ReflectiveOperationException
    {
        return filledBean(beanClass, seed, 0);
    }


    /**
     * The number of setters that were given a value.
     *
     * @return count
     */
    int getPopulatedSetters()
    {
        return populatedSetters;
    }


    /**
     * The number of setters that were left alone because their type is not one this factory makes.
     *
     * @return count
     */
    int getSkippedSetters()
    {
        return skippedSetters;
    }


    /**
     * The parameter types this factory does not make, so that a type that starts to matter can be added.
     *
     * @return type names
     */
    Set<String> getSkippedTypes()
    {
        return skippedTypes;
    }


    /**
     * Create an instance of the supplied bean and, unless the nest is already too deep, call every setter
     * it has.
     *
     * @param beanClass bean to build
     * @param seed distinguishes one filled-in bean from another
     * @param depth how far into a nest of beans this is
     * @return the bean
     * @throws ReflectiveOperationException the bean could not be created
     */
    private Object filledBean(Class<?> beanClass, int seed, int depth) throws ReflectiveOperationException
    {
        Object bean = beanClass.getDeclaredConstructor().newInstance();

        if (depth >= MAX_DEPTH)
        {
            return bean;
        }

        for (Method method : beanClass.getMethods())
        {
            if (! isSetter(method))
            {
                continue;
            }

            Object value = valueFor(method.getGenericParameterTypes()[0], seed, depth + 1);

            if (value == null)
            {
                skippedSetters++;
                skippedTypes.add(method.getGenericParameterTypes()[0].getTypeName());
                continue;
            }

            try
            {
                method.invoke(bean, value);
                populatedSetters++;
            }
            catch (InvocationTargetException | IllegalArgumentException error)
            {
                /*
                 * A setter that refuses the value is not the subject of this test - the bean contract is -
                 * so it is counted as a skip and the field is left at its default.
                 */
                skippedSetters++;
                skippedTypes.add(method.getGenericParameterTypes()[0].getTypeName() + " (setter refused it)");
            }
        }

        return bean;
    }


    /**
     * Is this a bean setter the factory should call?
     * <br><br>
     * {@code typeName} is left alone deliberately.  A bean owns its type name - its constructor sets it
     * from the {@link org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType} it belongs to,
     * and several copy constructors set it again - so putting an arbitrary string there makes a bean the
     * model would never produce, and then reports the constructors that correct it as though they had lost
     * something.  Leaving it at the value the constructor chose keeps it in the JSON round trip and in the
     * equality comparison, with a value that is real.
     *
     * @param method method to consider
     * @return true if it is a setter to call
     */
    private boolean isSetter(Method method)
    {
        return method.getName().startsWith("set")
                       && method.getName().length() > 3
                       && ! "setTypeName".equals(method.getName())
                       && method.getParameterCount() == 1
                       && method.getReturnType() == void.class
                       && method.getDeclaringClass() != Object.class;
    }


    /**
     * Make a value of the supplied type, or null when this factory does not know how to.
     *
     * @param type the setter's parameter type, with its type arguments where it has them
     * @param seed distinguishes one filled-in bean from another
     * @param depth how far into a nest of beans this is
     * @return a value, or null
     * @throws ReflectiveOperationException a nested bean could not be created
     */
    private Object valueFor(Type type, int seed, int depth) throws ReflectiveOperationException
    {
        if (type instanceof ParameterizedType parameterizedType)
        {
            return collectionValueFor(parameterizedType, seed, depth);
        }

        if (type instanceof Class<?> rawType)
        {
            return simpleValueFor(rawType, seed, depth);
        }

        return null;
    }


    /**
     * Make a single element collection or map of the supplied type.  One element is enough: this test is
     * about whether a value survives a round trip and takes part in equality, not about how many of them do.
     *
     * @param parameterizedType the collection type, with its type arguments
     * @param seed distinguishes one filled-in bean from another
     * @param depth how far into a nest of beans this is
     * @return a value, or null
     * @throws ReflectiveOperationException a nested bean could not be created
     */
    private Object collectionValueFor(ParameterizedType parameterizedType, int seed, int depth) throws ReflectiveOperationException
    {
        Type   rawType       = parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        if (rawType == List.class && typeArguments.length == 1)
        {
            Object element = valueFor(typeArguments[0], seed, depth);

            if (element == null)
            {
                return null;
            }

            List<Object> list = new ArrayList<>();

            list.add(element);

            return list;
        }

        if (rawType == Set.class && typeArguments.length == 1)
        {
            Object element = valueFor(typeArguments[0], seed, depth);

            if (element == null)
            {
                return null;
            }

            Set<Object> set = new LinkedHashSet<>();

            set.add(element);

            return set;
        }

        if (rawType == Map.class && typeArguments.length == 2)
        {
            Object key   = valueFor(typeArguments[0], seed, depth);
            Object value = valueFor(typeArguments[1], seed, depth);

            if ((key == null) || (value == null))
            {
                return null;
            }

            Map<Object, Object> map = new LinkedHashMap<>();

            map.put(key, value);

            return map;
        }

        return null;
    }


    /**
     * Make a value of the supplied class - a primitive, a string, a date, an enum constant, or a nested
     * bean.
     *
     * @param rawType class to make a value of
     * @param seed distinguishes one filled-in bean from another
     * @param depth how far into a nest of beans this is
     * @return a value, or null
     * @throws ReflectiveOperationException a nested bean could not be created
     */
    private Object simpleValueFor(Class<?> rawType, int seed, int depth) throws ReflectiveOperationException
    {
        if (rawType == String.class)
        {
            return "value-" + seed;
        }
        if ((rawType == boolean.class) || (rawType == Boolean.class))
        {
            /*
             * A boolean only has two values, so the two seeds have to land on different ones for
             * "differently filled beans are not equal" to mean anything.
             */
            return seed % 2 == 0;
        }
        if ((rawType == int.class) || (rawType == Integer.class))
        {
            return 100 + seed;
        }
        if ((rawType == long.class) || (rawType == Long.class))
        {
            return 1_000L + seed;
        }
        if ((rawType == short.class) || (rawType == Short.class))
        {
            return (short) (10 + seed);
        }
        if ((rawType == byte.class) || (rawType == Byte.class))
        {
            return (byte) (1 + seed);
        }
        if ((rawType == float.class) || (rawType == Float.class))
        {
            return 1.5F + seed;
        }
        if ((rawType == double.class) || (rawType == Double.class))
        {
            return 2.5D + seed;
        }
        if (rawType == Date.class)
        {
            return new Date(BASE_TIME + seed);
        }
        if (rawType == Object.class)
        {
            /*
             * A Map<String, Object> of additional properties.  A string keeps the JSON round trip honest -
             * Jackson has no type to restore an arbitrary object to, and would hand back a map.
             */
            return "object-" + seed;
        }
        if (rawType.isEnum())
        {
            Object[] constants = rawType.getEnumConstants();

            return (constants.length == 0) ? null : constants[seed % constants.length];
        }
        if (rawType == OpenMetadataTypeDef.class)
        {
            /*
             * Abstract, like PropertyValue, so it needs a concrete stand-in.  Without one a type def gallery
             * is built empty, and a gallery compared against another empty gallery proves nothing.
             */
            return filledBean(OpenMetadataEntityDef.class, seed, depth);
        }
        if (rawType == OpenMetadataAttributeTypeDef.class)
        {
            return filledBean(OpenMetadataPrimitiveDef.class, seed, depth);
        }
        if (rawType == PropertyValue.class)
        {
            /*
             * PropertyValue is abstract, so it needs a concrete stand-in.
             */
            PrimitiveTypePropertyValue propertyValue = new PrimitiveTypePropertyValue();

            propertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
            propertyValue.setPrimitiveValue("value-" + seed);
            propertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getDisplayName());

            return propertyValue;
        }
        if (rawType == ElementProperties.class)
        {
            ElementProperties elementProperties = new ElementProperties();

            elementProperties.setProperty("property-" + seed, (PropertyValue) simpleValueFor(PropertyValue.class, seed, depth));

            return elementProperties;
        }
        if (beanClassNames.contains(rawType.getName()) && ! rawType.isInterface())
        {
            return filledBean(rawType, seed, depth);
        }

        return null;
    }
}
