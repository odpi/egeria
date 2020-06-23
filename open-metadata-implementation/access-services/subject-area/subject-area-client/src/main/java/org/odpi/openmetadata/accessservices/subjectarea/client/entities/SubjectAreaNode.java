/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SubjectAreaNode implements SubjectAreaCategory, SubjectAreaTerm, SubjectAreaProject, SubjectAreaGlossary {
    private Map<Class<?>, SubjectAreaEntityClient<?>> cache = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public SubjectAreaNode(String packageToScan, SubjectAreaRestClient subjectAreaRestClient) {
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(SubjectAreaNodeClient.class);
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
                e.printStackTrace();
            }
        }
    }

    public SubjectAreaNode(SubjectAreaRestClient subjectAreaRestClient) {
        this(SubjectAreaNode.class.getPackage().getName(), subjectAreaRestClient);
    }

    @Override
    public SubjectAreaEntityClient<Category> category() {
        return getClient(Category.class);
    }

    @Override
    public SubjectAreaEntityClient<SubjectAreaDefinition> subjectAreaDefinition() {
        return getClient(SubjectAreaDefinition.class);
    }

    @Override
    public SubjectAreaEntityClient<Glossary> glossary() {
        return getClient(Glossary.class);
    }

    @Override
    public SubjectAreaEntityClient<Project> project() {
        return getClient(Project.class);
    }

    @Override
    public SubjectAreaEntityClient<Term> term() {
        return getClient(Term.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends Node> SubjectAreaEntityClient<T> getClient(Class<T> clazz) {
        return (SubjectAreaEntityClient<T>) cache.get(clazz);
    }
}
