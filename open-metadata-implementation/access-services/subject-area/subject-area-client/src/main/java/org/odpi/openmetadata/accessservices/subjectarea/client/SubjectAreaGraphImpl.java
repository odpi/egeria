/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the Subject Area OMAS.
 * This interface provides graph interface for subject area experts.
 */
public class SubjectAreaGraphImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGraph {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGraphImpl.class);

    private static final String className = SubjectAreaGraphImpl.class.getName();
    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "nodes";


    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    public SubjectAreaGraphImpl(String serverName, String serverPlatformURLRoot) throws
                                                                                    org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    @Override
    public Graph getGraph(
            String userId,
            String guid,
            Date asOfTime,
            Set<NodeType> nodeFilter,
            Set<LineType> lineFilter,
            StatusFilter statusFilter,   // may need to extend this for controlled terms
            Integer level) throws
                           UserNotAuthorizedException,
                           InvalidParameterException,
                           FunctionNotSupportedException,
                           MetadataServerUncontactableException,
                           UnexpectedResponseException, PropertyServerException {
        final String methodName = "getGraph";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String urlTemplate = BASE_URL + "/%s";

        StringBuffer nodeFilterSB = new StringBuffer();
        if (nodeFilter != null && !nodeFilter.isEmpty()) {
            for (NodeType nodeType : nodeFilter) {
                nodeFilterSB.append(nodeType.name());
                nodeFilterSB.append(",");
            }
            nodeFilterSB.deleteCharAt(nodeFilterSB.lastIndexOf(","));
        }

        StringBuffer lineFilterSB = new StringBuffer();
        if (lineFilter != null && !lineFilter.isEmpty()) {
            for (LineType lineType : lineFilter) {
                lineFilterSB.append(lineType.name());
                lineFilterSB.append(",");
            }
            lineFilterSB.deleteCharAt(lineFilterSB.lastIndexOf(","));
        }

        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils.addCharacterToQuery(queryStringSB);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime=" + asOfTime);
        }
        if (nodeFilter != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("nodeFilter=" + nodeFilterSB);
        }
        if (lineFilter != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("lineFilter=" + lineFilterSB);
        }
        if (statusFilter != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("statusFilter=" + statusFilter);
        }
        if (level != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("level=" + level);
        }
        if (queryStringSB.length() > 1) {
            urlTemplate = urlTemplate + queryStringSB.toString();
        }
        SubjectAreaOMASAPIResponse response = getRESTCall(userId, methodName, urlTemplate);
        Graph graph = DetectUtils.detectAndReturnGraph(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return graph;
    }
}
