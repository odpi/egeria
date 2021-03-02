/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.ffdc.GlossaryAuthorErrorCode;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumb;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.BreadCrumbTrail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The breadcrumb handler is initialised with Subject Area Node clients clients (allowing calls to the Subject Area OMAs to be made) and
 * the userId under which those calls should be issued.
 * This class exposes a method which is supplied the
 * <ul>
 *     <li>Glossary guid (the top of the bread crumb)</li>
 *     <li>the root Category the top of the Categories</li>
 *     <li>the leaf Category the bottom of the Categories </li>
 *     <li>a Term guid which if specified is the bottom of the breadcrumb trail.</li>
 * </ul>
 * The guids represent nodes that are expected to determine a breadcrumb trail. If the supplied guids do not form
 * a trail then an error occurs.
 *
 * The handler exposes methods for breadcrumb functionality for the Glossary Author view
 */
public class GraphHandler {
    static String className = GraphHandler.class.getName();
    private Set<String> guids = new HashSet<>();
    private String userId;



    public GraphHandler(SubjectAreaNodeClients clients, String userId) {
        this.userId = userId;
    }

}