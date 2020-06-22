/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaGraphHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGraphRESTServices extends SubjectAreaRESTServicesInstance {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGraphRESTServices.class);
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaGraphRESTServices() {}

    /**
     * Get the graph of nodes and Lines radiating out from a node.
     *
     * Return the nodes and Lines that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of Lines, types of nodes and classifications as well as level.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param lineFilterStr comma separated list of line names to include in the query results.  Null means include
     *                                all Lines found, irrespective of their type.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level the number of the Lines (relationships) out from the starting node that the query will traverse to
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
    public  SubjectAreaOMASAPIResponse getGraph(String serverName,
                                                String userId,
                                                String guid,
                                                Date asOfTime,
                                                String nodeFilterStr,
                                                String lineFilterStr,
                                                StatusFilter statusFilter,   // may need to extend this for controlled terms
                                                Integer level ) {

        final String methodName = "getGraph";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            SubjectAreaGraphHandler handler = instanceHandler.getSubjectAreaGraphHandler(userId, serverName, methodName);
            response = handler.getGraph(
                                        userId,
                                        guid,
                                        asOfTime,
                                        nodeFilterStr,
                                        lineFilterStr,
                                        statusFilter,   // may need to extend this for controlled terms
                                        level);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase e) {
            response = ExceptionMapper.getResponseFromOCFCheckedExceptionBase(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }
}