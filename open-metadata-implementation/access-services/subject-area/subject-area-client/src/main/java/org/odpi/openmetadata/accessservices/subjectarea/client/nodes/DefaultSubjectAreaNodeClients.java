/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects.SubjectAreaProjectClient;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The OMAS client library implementation of the Subject Area OMAS.
 * This interface provides entities {@link Node} authoring interface for subject area experts.
 */
public class DefaultSubjectAreaNodeClients implements SubjectAreaNodeClients {
    private final Map<Class<?>, SubjectAreaNodeClient<?>> cache = new HashMap<>();
    private static final String DEFAULT_SCAN_PACKAGE = DefaultSubjectAreaNodeClients.class.getPackage().getName();

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient}
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     * */
    @SuppressWarnings("rawtypes")
    public DefaultSubjectAreaNodeClients(SubjectAreaRestClient subjectAreaRestClient, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(DEFAULT_SCAN_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient.class);
        for (Class<?> declaredClass : clientClasses) {
            try {
                if (AbstractSubjectAreaNode.class.isAssignableFrom(declaredClass)) {
                    Constructor<?> ctor = declaredClass.getDeclaredConstructor(SubjectAreaRestClient.class);
                    ctor.setAccessible(true);
                    final AbstractSubjectAreaNode newInstance =
                            (AbstractSubjectAreaNode) ctor.newInstance(subjectAreaRestClient);
                    cache.put(newInstance.resultType(), newInstance);
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new ExceptionInInitializerError(
                        "During initialization `DefaultSubjectAreaNodeClients` an error has occurred - "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     *  The constructor uses the current package to scan "org.odpi.openmetadata.accessservices.subjectarea.client.nodes"
     *  to search for classes placed by annotation {@link org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient}.
     *
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     */
    public DefaultSubjectAreaNodeClients(SubjectAreaRestClient subjectAreaRestClient) {
        this(subjectAreaRestClient, DEFAULT_SCAN_PACKAGE);
    }

    @Override
    public <G extends Glossary> SubjectAreaNodeClient<G> glossaries() {
        return (SubjectAreaNodeClient<G>) getClient(Glossary.class);
    }

    @Override
    public <T extends Term> SubjectAreaNodeClient<T> terms() {
        return (SubjectAreaNodeClient<T>) getClient(Term.class);
    }

    @Override
    public <C extends Category> SubjectAreaNodeClient<C> categories() {
        return (SubjectAreaNodeClient<C>) getClient(Category.class);
    }

    @Override
    public <P extends Project> SubjectAreaProjectClient<P> projects() {
        return (SubjectAreaProjectClient<P>) getClient(Project.class);
    }

    /**
     * @param <T> - {@link Relationship} type of object
     * @param clazz - the class for which you want to get the client from cache
     *
     * @return SubjectAreaNodeClient or null if this client is not present
     * */
    @SuppressWarnings("unchecked")
    public <T extends Node> SubjectAreaNodeClient<T> getClient(Class<T> clazz) {
        if (cache.containsKey(clazz)) {
            return (SubjectAreaNodeClient<T>) cache.get(clazz);
        }
        final ExceptionMessageDefinition messageDefinition =
                SubjectAreaErrorCode.NOT_FOUND_CLIENT.getMessageDefinition(clazz.getName());
        final SubjectAreaCheckedException exc =
                new SubjectAreaCheckedException(messageDefinition, getClass().getName(), messageDefinition.getSystemAction());
        throw new IllegalArgumentException(exc);
    }
}