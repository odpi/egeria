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
import org.odpi.openmetadata.viewservices.glossaryauthor.properties.NodeLineStats;
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

    public SubjectAreaOMASAPIResponse<Graph> getGraph(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String lineFilterStr, StatusFilter statusFilter) {
        final String methodName = "getGraph";
        SubjectAreaOMASAPIResponse<Graph> response = new SubjectAreaOMASAPIResponse<>();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            Graph graph = getGraphObject(serverName, userId, guid, asOfTime, nodeFilterStr, lineFilterStr, statusFilter, methodName);
            response.addResult(graph);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    public SubjectAreaOMASAPIResponse<GraphStatistics> getGraphCounts(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String lineFilterStr, StatusFilter statusFilter) {
        final String methodName = "getGraphStatistics";
        SubjectAreaOMASAPIResponse<GraphStatistics> response = new SubjectAreaOMASAPIResponse<>();

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            Graph graph = getGraphObject(serverName, userId, guid, asOfTime, nodeFilterStr, lineFilterStr, statusFilter, methodName);
            // construct graph statistics from the graph
            GraphStatistics graphStatistics = new GraphStatistics(guid, 1);

            Map<String, Node> nodes= graph.getNodes();
            Map<String, Line> lines= graph.getLines();
            Map<String, NodeLineStats>  nodeCountsMap = new HashMap<>();
            Map<String, NodeLineStats>  lineCountsMap = new HashMap<>();
            if (nodes !=null) {
                Set<String> nodeGuids = nodes.keySet();
                for (String nodeGuid: nodeGuids) {
                    Node node = nodes.get(nodeGuid);
                    String typeName = node.getNodeType().name();
                    NodeLineStats countForNodeOrLineType = nodeCountsMap.get(typeName);
                    int count =0;
                    if (countForNodeOrLineType != null) {
                        count = countForNodeOrLineType.getCount();
                    }
                    countForNodeOrLineType = new NodeLineStats(typeName, count+1);
                    nodeCountsMap.put(typeName, countForNodeOrLineType);
                }
            }
            if (lines !=null) {
                Set<String> lineGuids = lines.keySet();
                for (String lineGuid: lineGuids) {
                    Line line = lines.get(lineGuid);
                    String typeName = line.getLineType().name();
                    NodeLineStats countForNodeOrLineType = lineCountsMap.get(typeName);
                    int count =0;
                    if (countForNodeOrLineType != null) {
                        count = countForNodeOrLineType.getCount();
                    }
                    countForNodeOrLineType = new NodeLineStats(typeName, count+1);
                    lineCountsMap.put(typeName, countForNodeOrLineType);
                }
            }
            graphStatistics.setLineCounts(lineCountsMap);
            graphStatistics.setNodeCounts(nodeCountsMap);
            response.addResult(graphStatistics);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }
    private Graph getGraphObject(String serverName, String userId, String guid, Date asOfTime, String nodeFilterStr, String lineFilterStr, StatusFilter statusFilter, String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

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
        Set<LineType> lineTypes;
        final Stream<LineType> allLineTypesStream = Arrays.stream(LineType.values());
        if (lineFilterStr == null) {
            lineTypes = allLineTypesStream.collect(Collectors.toSet());
            if (lineTypes.contains(LineType.Unknown)) {
                lineTypes.remove(LineType.Unknown);
            }
        } else {
            Set<String> typeNames = allLineTypesStream.map(LineType::name).collect(Collectors.toSet());
            lineTypes = Arrays.stream(lineFilterStr.split(","))
                    .filter(typeNames::contains)
                    .map(LineType::valueOf)
                    .collect(Collectors.toSet());
        }

        return graphClient.getGraph(userId, guid, asOfTime, nodeTypes, lineTypes, statusFilter, 1);
    }
}
