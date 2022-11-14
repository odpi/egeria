/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaGraphHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGraphRESTServices extends SubjectAreaRESTServicesInstance {
    private static final String className = SubjectAreaGraphRESTServices.class.getName();
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaGraphRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SubjectAreaGraphRESTServices() {}

    /**
     * Get the graph of nodes and relationships radiating out from a node.
     *
     * Return the nodes and relationships that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of relationships, types of nodes and classifications as well as level.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipFilterStr comma separated list of relationship names to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level the number of the relationships (relationships) out from the starting node that the query will traverse to
     *              gather results. If not specified then it defaults to 3.
     * @return A graph of nodeTypes.
     *
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Graph> getGraph(String serverName,
                                                      String userId,
                                                      String guid,
                                                      Date asOfTime,
                                                      String nodeFilterStr,
                                                      String relationshipFilterStr,
                                                      StatusFilter statusFilter,   // may need to extend this for controlled terms
                                                      Integer level ) {

        final String methodName = "getGraph";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Graph> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaGraphHandler handler = instanceHandler.getSubjectAreaGraphHandler(userId, serverName, methodName);
            response = handler.getGraph(
                                        userId,
                                        guid,
                                        asOfTime,
                                        nodeFilterStr,
                                        relationshipFilterStr,
                                        statusFilter,   // may need to extend this for controlled terms
                                        level);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}