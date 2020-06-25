/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MappersFactory {
    private final Map<Class<?>, Supplier<Object>> mappers = new HashMap<>();
    private final String SUBJECT_AREA_PACKAGE = "org.odpi.openmetadata.accessservices.subjectarea";

    public MappersFactory(final OMRSAPIHelper omrsApiHelper) {
        Reflections reflections = new Reflections(SUBJECT_AREA_PACKAGE);
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
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T>T get(Class<T> mapperClass) {
        if (mappers.containsKey(mapperClass)) {
            return (T) this.mappers.get(mapperClass).get();
        }
        //TODO throw Error
        return null;
    }

    public Set<Class<?>> getAllMapperClasses() {
        return mappers.keySet();
    }
}
