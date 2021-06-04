/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects.SubjectAreaProjectClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

public interface SubjectAreaNodeClients {

    /**
     * Get the subject area glossary API class - use this class to issue glossary calls.
     *
     * @param <G> type for supplied and return.
     * @return subject area glossary API class
     */
    <G extends Glossary> SubjectAreaNodeClient<G> glossaries();

    /**
     * Get the subject area term API class - use this class to issue term calls.
     *
     * @param <T> type for supplied and return.
     * @return subject area term API class
     */
    <T extends Term> SubjectAreaNodeClient<T> terms();

    /**
     * Get the subject area category API class - use this class to issue category calls.
     *
     * @param <C> type for supplied and return.
     * @return subject area category API class
     */
    <C extends Category> SubjectAreaNodeClient<C> categories();

    /**
     * Get the subject area project API class - use this class to issue project calls.
     *
     * @param <P> type for supplied and return.
     * @return subject area project API class
     */
    <P extends Project> SubjectAreaNodeClient<P> projects();

}