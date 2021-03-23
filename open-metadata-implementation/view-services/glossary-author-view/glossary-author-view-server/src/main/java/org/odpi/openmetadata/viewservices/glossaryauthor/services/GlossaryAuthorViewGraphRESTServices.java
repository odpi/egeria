/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.StatusFilter;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.NodeRelationshipStats;
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.GraphStatistics;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The GlossaryAuthorViewProjectRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view project authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewGraphRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewGraphRESTServices.class.getName();

    /**
     * Default constructor
     */
    public GlossaryAuthorViewGraphRESTServices() {

    }

    public SubjectAreaOMASAPIResponse<Graph> getGraph(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String relationshipFilterStr, StatusFilter statusFilter) {
        final String methodName = "getGraph";
        SubjectAreaOMASAPIResponse<Graph> response = new SubjectAreaOMASAPIResponse<>();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            Graph graph = getGraphObject(serverName, userId, guid, asOfTime, nodeFilterStr, relationshipFilterStr, statusFilter, methodName);
            response.addResult(graph);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    public SubjectAreaOMASAPIResponse<GraphStatistics> getGraphCounts(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String relationshipFilterStr, StatusFilter statusFilter) {
        final String methodName = "getGraphStatistics";
        SubjectAreaOMASAPIResponse<GraphStatistics> response = new SubjectAreaOMASAPIResponse<>();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            Graph graph = getGraphObject(serverName, userId, guid, asOfTime, nodeFilterStr, relationshipFilterStr, statusFilter, methodName);
            // construct graph statistics from the graph
            GraphStatistics graphStatistics = new GraphStatistics(guid, 1);

            Map<String, Node> nodes= graph.getNodes();
            Map<String, Relationship> relationships= graph.getRelationships();
            Map<String, NodeRelationshipStats>  nodeCountsMap = new HashMap<>();
            Map<String, NodeRelationshipStats>  relationshipCountsMap = new HashMap<>();
            if (nodes !=null) {
                Set<String> nodeGuids = nodes.keySet();
                for (String nodeGuid: nodeGuids) {
                    Node node = nodes.get(nodeGuid);
                    String typeName = node.getNodeType().name();
                    NodeRelationshipStats countForNodeOrRelationshipType = nodeCountsMap.get(typeName);
                    int count =0;
                    if (countForNodeOrRelationshipType != null) {
                        count = countForNodeOrRelationshipType.getCount();
                    }
                    countForNodeOrRelationshipType = new NodeRelationshipStats(typeName, count+1);
                    nodeCountsMap.put(typeName, countForNodeOrRelationshipType);
                }
            }
            if (relationships !=null) {
                Set<String> relationshipGuids = relationships.keySet();
                for (String relationshipGuid: relationshipGuids) {
                    Relationship relationship = relationships.get(relationshipGuid);
                    String typeName = relationship.getRelationshipType().name();
                    NodeRelationshipStats countForNodeOrRelationshipType = relationshipCountsMap.get(typeName);
                    int count =0;
                    if (countForNodeOrRelationshipType != null) {
                        count = countForNodeOrRelationshipType.getCount();
                    }
                    countForNodeOrRelationshipType = new NodeRelationshipStats(typeName, count+1);
                    relationshipCountsMap.put(typeName, countForNodeOrRelationshipType);
                }
            }
            graphStatistics.setRelationshipCounts(relationshipCountsMap);
            graphStatistics.setNodeCounts(nodeCountsMap);
            response.addResult(graphStatistics);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }
    private Graph getGraphObject(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String relationshipFilterStr, StatusFilter statusFilter, String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        SubjectAreaGraphClient graphClient = instanceHandler.getSubjectAreaGraphClient(serverName, userId, methodName);
        final Stream<NodeType> allNodeTypesStream = Arrays.stream(NodeType.values());
        Set<NodeType> nodeTypes;
        if (nodeFilterStr == null) {

            nodeTypes = allNodeTypesStream.collect(Collectors.toSet());
            // remove Unknown
            nodeTypes.remove(NodeType.Unknown);
        } else {
            Set<String> typeNames = allNodeTypesStream.map(NodeType::name).collect(Collectors.toSet());
            nodeTypes = Arrays.stream(nodeFilterStr.split(","))
                    .filter(typeNames::contains)
                    .map(NodeType::valueOf)
                    .collect(Collectors.toSet());
        }
        Set<RelationshipType> relationshipTypes;
        final Stream<RelationshipType> allRelationshipTypesStream = Arrays.stream(RelationshipType.values());
        if (relationshipFilterStr == null) {
            relationshipTypes = allRelationshipTypesStream.collect(Collectors.toSet());
            if (relationshipTypes.contains(RelationshipType.Unknown)) {
                relationshipTypes.remove(RelationshipType.Unknown);
            }
        } else {
            Set<String> typeNames = allRelationshipTypesStream.map(RelationshipType::name).collect(Collectors.toSet());
            relationshipTypes = Arrays.stream(relationshipFilterStr.split(","))
                    .filter(typeNames::contains)
                    .map(RelationshipType::valueOf)
                    .collect(Collectors.toSet());
        }

        return graphClient.getGraph(userId, guid, asOfTime, nodeTypes, relationshipTypes, statusFilter, 1);
    }
}
