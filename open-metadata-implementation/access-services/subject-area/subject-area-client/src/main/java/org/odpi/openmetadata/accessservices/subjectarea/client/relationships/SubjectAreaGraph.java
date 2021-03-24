/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.NeighborhoodHistoricalFindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.Set;

public interface SubjectAreaGraph {
    /**
     * Get the graph of nodes and relationships radiating out from a node.
     * <p>
     * The results are scoped by types of relationships, types of nodes and classifications as well as level.
     *
     * @param userId       userId under which the request is performed
     * @param guid         the starting point of the query.
     * @param nodeFilter   Set of the names of the nodes to include in the query results.  Null means include
     *                     all nodes found, irrespective of their type.
     * @param relationshipFilter   Set of names of relationships to include in the query results.  Null means include
     *                     all relationships found, irrespective of their type.
     * @param statusFilter By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param asOfTime     Requests a historical query of the relationships for the node.  Null means return the
     *                     present values.
     * @param level        the number of the relationships (relationships) out from the starting node that the query will traverse to
     *                     gather results. If not specified then it defaults to 3.
     * @return A graph of nodes.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
   default Graph getGraph(String userId,
                          String guid,
                          Date asOfTime,
                          Set<NodeType> nodeFilter,
                          Set<RelationshipType> relationshipFilter,
                          StatusFilter statusFilter,
                          int level) throws InvalidParameterException,
                                            PropertyServerException,
                                            UserNotAuthorizedException
   {
       NeighborhoodHistoricalFindRequest request = new NeighborhoodHistoricalFindRequest();
       request.setAsOfTime(asOfTime);
       request.setNodeFilter(nodeFilter);
       request.setRelationshipFilter(relationshipFilter);
       request.setLevel(level);
       request.setStatusFilter(statusFilter);
       return getGraph(userId, guid, request);
   }

    /**
     * Get the graph of nodes and relationships radiating out from a node.
     * <p>
     * The results are scoped by types of relationships, types of nodes and classifications as well as level.
     *
     * @param userId       userId under which the request is performed
     * @param guid         the starting point of the query.
     * @param request      {@link NeighborhoodHistoricalFindRequest}
     *
     * @return A graph of nodes.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    Graph getGraph(String userId, String guid, NeighborhoodHistoricalFindRequest request) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException;
}
