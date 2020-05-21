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
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides term term authoring interface for subject area experts.
 */
public class SubjectAreaGraphImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGraph {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGraphImpl.class);

    private static final String className = SubjectAreaGraphImpl.class.getName();
    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "nodes";


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaGraphImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
    }


    /**
     * Get the graph of nodes and Lines radiating out from a node.
     * <p>
     * The results are scoped by types of Lines, types of nodes and classifications as well as level.
     *
     * @param userId       userId under which the request is performed
     * @param guid         the starting point of the query.
     * @param nodeFilter   Set of the names of the nodes to include in the query results.  Null means include
     *                     all nodes found, irrespective of their type.
     * @param lineFilter   Set of names of Lines to include in the query results.  Null means include
     *                     all Lines found, irrespective of their type.
     * @param asOfTime     Requests a historical query of the Lines for the node.  Null means return the
     *                     present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level        the number of the Lines (relationships) out from the starting node that the query will traverse to
     *                     gather results. If not specified then it defaults to 3.
     * @return A graph of nodes.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
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
                           UnexpectedResponseException {
        final String methodName = "getGraph";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);

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
            url = url + queryStringSB.toString();
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Graph graph = DetectUtils.detectAndReturnGraph(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return graph;
    }
}
