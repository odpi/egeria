/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

/**
 * Factory class for {@link SubjectAreaMapper} classes */
public class MappersFactory {
    private final Map<Class<?>, Supplier<Object>> mappers = new HashMap<>();
    private static final String SUBJECT_AREA_PACKAGE = "org.odpi.openmetadata.accessservices.subjectarea";

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link SubjectAreaMapper}
     * @param omrsApiHelper - {@link OMRSAPIHelper}
     **/
    public MappersFactory(final OMRSAPIHelper omrsApiHelper, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(SUBJECT_AREA_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> mappersClasses = reflections.getTypesAnnotatedWith(SubjectAreaMapper.class);
        for (Class<?> mapperClass : mappersClasses) {
            try {
                Constructor<?> ctor = mapperClass.getDeclaredConstructor(OMRSAPIHelper.class);
                ctor.setAccessible(true);
                final Object mapper = ctor.newInstance(omrsApiHelper);
                mappers.put(mapperClass, () -> mapper);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new ExceptionInInitializerError(
                        "During initialization MappersFactory an error has occurred - "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     *  The constructor uses the current package to scan "org.odpi.openmetadata.accessservices.subjectarea";"
     *  to search for classes placed by annotation {@link SubjectAreaMapper}.
     *
     * @param omrsApiHelper - {@link OMRSAPIHelper}
     */
    public MappersFactory(final OMRSAPIHelper omrsApiHelper) {
        this(omrsApiHelper, SUBJECT_AREA_PACKAGE);
    }

    /**
     * @param <T> - type of mapper object
     * @param mapperClass - the class for which you want to get the mapper from cache
     *
     * @return mapper or null if it is not present
     * */
    @SuppressWarnings("unchecked")
    public <T>T get(Class<T> mapperClass) {
        if (mappers.containsKey(mapperClass)) {
            return (T) this.mappers.get(mapperClass).get();
        }
        return null;
    }

    /**
     * @return all mapper classes from cache */
    public Set<Class<?>> getAllMapperClasses() {
        return mappers.keySet();
    }
}
