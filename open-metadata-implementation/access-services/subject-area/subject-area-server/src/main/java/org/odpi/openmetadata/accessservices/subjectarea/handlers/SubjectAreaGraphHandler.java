/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph.LineTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph.NodeTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGraphHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaGraphHandler.class.getName();

    /**
     * Construct the Subject Area Graph Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper           omrs API helper
     */
    public SubjectAreaGraphHandler(OMRSAPIHelper oMRSAPIHelper) {
        super(oMRSAPIHelper);
    }

    /**
     * Get the graph of nodes and Lines radiating out from a node.
     * <p>
     * Return the nodes and Lines that radiate out from the supplied node (identified by a GUID).
     * The results are scoped by types of Lines, types of nodes and classifications as well as level.
     *
     * @param userId        userId under which the request is performed
     * @param guid          the starting point of the query.
     * @param nodeFilterStr Comma separated list of node names to include in the query results.  Null means include
     *                      all entities found, irrespective of their type.
     * @param lineFilterStr comma separated list of line names to include in the query results.  Null means include
     *                      all Lines found, irrespective of their type.
     * @param asOfTime      Requests a historical query of the relationships for the entity.  Null means return the
     *                      present values.
     * @param statusFilter  By default only active instances are returned. Specify ALL to see all instance in any status.
     * @param level         the number of the Lines (relationships) out from the starting node that the query will traverse to
     *                      gather results. If not specified then it defaults to 3.
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
    public SubjectAreaOMASAPIResponse<Graph> getGraph(String userId,
                                                      String guid,
                                                      Date asOfTime,
                                                      String nodeFilterStr,
                                                      String lineFilterStr,
                                                      StatusFilter statusFilter,   // may need to extend this for controlled terms
                                                      Integer level) {

        final String methodName = "getGraph";
        SubjectAreaOMASAPIResponse<Graph> response = new SubjectAreaOMASAPIResponse<>();

        try {
            List<InstanceStatus> requestedInstanceStatus = new ArrayList<>();
            if (statusFilter == null || statusFilter == StatusFilter.ACTIVE) {
                requestedInstanceStatus.add(SubjectAreaUtils.convertStatusToInstanceStatus(Status.ACTIVE));
            } else {
                // request all status instances.
                for (Status omasStatus : Status.values()) {
                    requestedInstanceStatus.add(SubjectAreaUtils.convertStatusToInstanceStatus(omasStatus));
                }
            }
            if (level == null) {
                level = 3;
            }

            InstanceGraph instanceGraph = oMRSAPIHelper.callGetEntityNeighbourhood(
                    methodName,
                    userId,
                    guid,
                    getEntityGuids(nodeFilterStr),
                    getRelationshipTypeGuids(lineFilterStr),
                    requestedInstanceStatus,
                    null,
                    asOfTime,
                    level
            );
            Graph graph = new Graph();
            if (CollectionUtils.isNotEmpty(instanceGraph.getRelationships())) {
                List<Line> lines = getLinesFromRelationships(instanceGraph.getRelationships());
                graph.setLines(new HashSet<>(lines));
            }

            if (CollectionUtils.isNotEmpty(instanceGraph.getEntities())) {
                List<Node> nodes = getNodesFromEntityDetails(instanceGraph.getEntities());
                graph.setNodes(new HashSet<>(nodes));
            }
            // end of if after getEntityNeighbourhood call
            response.addResult(graph);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    private List<String> getEntityGuids(String nodeFilterStr) {
        // if there was no NodeFilter supplied then limit to the the NodeType values,
        // so we only get the types that this omas is interested in.
        Stream<NodeType> nodeTypeStream = Arrays.stream(NodeType.values());
        if (nodeFilterStr == null) {
           return nodeTypeStream
                    .filter(type -> type != NodeType.Unknown)
                    .map(NodeTypeMapper::mapNodeTypeToEntityTypeGuid)
                    .collect(toList());
        } else {
            Set<String> typeNames = nodeTypeStream.map(NodeType::name).collect(Collectors.toSet());
            return Arrays.stream(nodeFilterStr.split(","))
                    .filter(typeNames::contains)
                    .map(NodeType::valueOf)
                    .map(NodeTypeMapper::mapNodeTypeToEntityTypeGuid)
                    //set of entity type guids so we do not have duplicates
                    .distinct()
                    .collect(toList());
        }
    }

    private List<String> getRelationshipTypeGuids(String lineFilterStr) {
        // if there was no Line filter supplied then limit to the the LineType values,
        // so we only get the types that this omas is interested in.
        Stream<LineType> lineTypeStream = Arrays.stream(LineType.values());
        if (lineFilterStr == null) {
          return lineTypeStream
                    .filter(type -> type != LineType.Unknown)
                    .map(LineTypeMapper::mapLineTypeToRelationshipTypeGuid)
                    .collect(toList());
        } else {
            Set<String> typeNames = lineTypeStream.map(LineType::name).collect(Collectors.toSet());
            return Arrays.stream(lineFilterStr.split(","))
                    .filter(typeNames::contains)
                    .map(LineType::valueOf)
                    .map(LineTypeMapper::mapLineTypeToRelationshipTypeGuid)
                    .distinct()
                    .collect(toList());
        }
    }
}