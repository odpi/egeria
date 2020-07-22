/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The OMAS client library implementation of the Subject Area OMAS.
 * This interface provides entities {@link Node} authoring interface for subject area experts.
 * A standard set of customers is described in {@link SubjectAreaCategory}, {@link SubjectAreaTerm},
 * {@link SubjectAreaProject}, {@link SubjectAreaGlossary}
 */
public class SubjectAreaNode implements SubjectAreaCategory, SubjectAreaTerm, SubjectAreaProject, SubjectAreaGlossary {
    private Map<Class<?>, SubjectAreaNodeClient<?>> cache = new HashMap<>();
    private static final String DEFAULT_SCAN_PACKAGE = SubjectAreaNode.class.getPackage().getName();

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient}
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     * */
    @SuppressWarnings("rawtypes")
    public SubjectAreaNode(SubjectAreaRestClient subjectAreaRestClient, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(DEFAULT_SCAN_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient.class);
        for (Class<?> declaredClass : clientClasses) {
            try {
                if (AbstractSubjectAreaEntity.class.isAssignableFrom(declaredClass)) {
                    Constructor<?> ctor = declaredClass.getDeclaredConstructor(SubjectAreaRestClient.class);
                    ctor.setAccessible(true);
                    final AbstractSubjectAreaEntity newInstance =
                            (AbstractSubjectAreaEntity) ctor.newInstance(subjectAreaRestClient);
                    cache.put(newInstance.type(), newInstance);
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new ExceptionInInitializerError(
                        "During initialization SubjectAreaNode an error has occurred - "
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
    public SubjectAreaNode(SubjectAreaRestClient subjectAreaRestClient) {
        this(subjectAreaRestClient, DEFAULT_SCAN_PACKAGE);
    }

    @Override
    public SubjectAreaNodeClient<Category> category() {
        return getClient(Category.class);
    }

    @Override
    public SubjectAreaNodeClient<SubjectAreaDefinition> subjectAreaDefinition() {
        return getClient(SubjectAreaDefinition.class);
    }

    @Override
    public SubjectAreaNodeClient<Glossary> glossary() {
        return getClient(Glossary.class);
    }

    @Override
    public SubjectAreaNodeClient<Project> project() {
        return getClient(Project.class);
    }

    @Override
    public SubjectAreaNodeClient<Term> term() {
        return getClient(Term.class);
    }

    /**
     * @param <T> - {@link Line} type of object
     * @param clazz - the class for which you want to get the client from cache
     *
     * @return SubjectAreaEntityClient or null if this client is not present
     * */
    @SuppressWarnings("unchecked")
    public <T extends Node> SubjectAreaNodeClient<T> getClient(Class<T> clazz) {
        if (cache.containsKey(clazz)) {
            return (SubjectAreaNodeClient<T>) cache.get(clazz);
        }
        return null;
    }
}
