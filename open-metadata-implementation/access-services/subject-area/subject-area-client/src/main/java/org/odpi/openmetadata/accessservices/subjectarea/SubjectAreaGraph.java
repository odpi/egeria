/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.util.Date;
import java.util.Set;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for graph
 */
public interface SubjectAreaGraph {
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
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param asOfTime     Requests a historical query of the Lines for the node.  Null means return the
     *                     present values.
     * @param level        the number of the Lines (relationships) out from the starting node that the query will traverse to
     *                     gather results. If not specified then it defaults to 3.
     * @return A graph of nodes.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     * @throws PropertyServerException              property server exception
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    Graph getGraph(
            String userId,
            String guid,
            Date asOfTime,
            Set<NodeType> nodeFilter,
            Set<LineType> lineFilter,
            StatusFilter statusFilter,
            Integer level) throws
                           UserNotAuthorizedException,
                           InvalidParameterException,
                           FunctionNotSupportedException,
                           MetadataServerUncontactableException,
                           UnexpectedResponseException,
                           PropertyServerException;

}