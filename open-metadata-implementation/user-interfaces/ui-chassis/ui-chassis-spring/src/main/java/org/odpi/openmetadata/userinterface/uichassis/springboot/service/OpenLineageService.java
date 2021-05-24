/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class responsibility is to interact with Open Lineage Services(OLS),
 * process the returned response and return it in a format understood by view
 */
@Service
public class OpenLineageService {

    private final OpenLineageClient openLineageClient;
    private final LineageGraphDisplayService lineageGraphDisplayService;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    /**
     * @param openLineageClient client to connect to open lineage services
     * @param lineageGraphDisplayService the rules for display
     */
    @Autowired
    public OpenLineageService(OpenLineageClient openLineageClient, LineageGraphDisplayService lineageGraphDisplayService) {
        this.openLineageClient = openLineageClient;
        this.lineageGraphDisplayService = lineageGraphDisplayService;
    }


    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the ultimate sources for the asset
     */
    public Graph getUltimateSource(String userId,
                                   String guid,
                                   boolean includeProcesses) {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.ULTIMATE_SOURCE, guid, "", includeProcesses);
            return processResponse(response,guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get ultimate source lineage for guid {}", guid);
            throw new RuntimeException("ultimate source lineage error", e);
        }
    }


    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the end to end flow
     */
    public Graph getEndToEndLineage(String userId,
                                    String guid,
                                    boolean includeProcesses) {
        try {
            LineageVerticesAndEdges response =  openLineageClient.lineage(userId, Scope.END_TO_END, guid, "", includeProcesses);
            return processResponse(response,guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get end to end lineage for guid {}", guid);
            throw new RuntimeException("end2end lineage error", e);
        }
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the ultimate destinations of the asset
     */
    public Graph getUltimateDestination(String userId,
                                        String guid,
                                        boolean includeProcesses) {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.ULTIMATE_DESTINATION, guid, "",
                    includeProcesses);
            return processResponse(response,guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get ultimate destination lineage for guid {}", guid);
            throw new RuntimeException("ultimate destination lineage error", e);
        }

    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the glossary terms linked to the asset
     */
    public Graph getVerticalLineage(String userId,
                                    String guid,
                                    boolean includeProcesses) {
        try {
            LineageVerticesAndEdges response =  openLineageClient.lineage(userId, Scope.VERTICAL, guid, "", includeProcesses);
            return processResponse(response,guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get glossary lineage for guid {}", guid);
            throw new RuntimeException("glossary lineage error", e);
        }

    }

    /**
     * Gets node details.
     *
     * @param userId the user id
     * @param guid   the guid
     * @return the node details
     */
    public LineageVertex getEntityDetails(String userId, String guid) {
        try {
        return openLineageClient.getEntityDetails(userId, guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get node details for guid {}", guid);
            throw new RuntimeException("entity details error", e);
        }
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the ultimate sources and destinations of the asset
     */
    public Graph getSourceAndDestination(String userId,
                                         String guid,
                                         boolean includeProcesses) {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.SOURCE_AND_DESTINATION, guid, "",
                    includeProcesses);
            return processResponse(response,guid);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error("Cannot get source and destination lineage for guid {}", guid);
            throw new RuntimeException("source and destination lineage error ", e);
        }

    }

    /**
     * @param response string returned from Open Lineage Services to be processed
     * @param guid the guid to process
     * @return map of nodes and edges describing the end to end flow
     */
    private Graph processResponse(LineageVerticesAndEdges response, String guid) {
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        LOG.debug("Received response from open lineage service: {}", response);
        if (response == null || CollectionUtils.isEmpty(response.getLineageVertices())) {
            return new Graph(nodes, edges);

        }

        edges = Optional.ofNullable(response).map(LineageVerticesAndEdges::getLineageEdges)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(this::createEdge)
                .collect(Collectors.toList());

        nodes = Optional.ofNullable(response).map(LineageVerticesAndEdges::getLineageVertices)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(this::createNode)
                .collect(Collectors.toList());

        List<Node> startList = nodes.stream()
                .filter(n -> n.getId().equals(guid))
                .collect(Collectors.toList());

        if(startList.size() > 0) {
            lineageGraphDisplayService.setNodesLevel(startList, new ArrayList<>(nodes), new ArrayList<>(edges));
        }

        Graph graph = new Graph(nodes, edges);
        lineageGraphDisplayService.applyRules(graph, guid);

        return graph;
    }

    /**
     * This method will create a new edge in a ui specific format based on the edge being processed
     *
     * @param currentEdge current edge to be processed
     * @return the edge in the format to be understand by the ui
     */
    private Edge createEdge(LineageEdge currentEdge) {
        return new Edge(currentEdge.getSourceNodeID(),
                currentEdge.getDestinationNodeID(), currentEdge.getEdgeType());
    }

    /**
     * This method will create a new node in ui specific format based on the properties of the currentNode to be processed
     *
     * @param currentNode current node to be processed
     * @return the node in the format to be understand by the ui
     */
    private Node createNode(LineageVertex currentNode) {
        String displayName = currentNode.getDisplayName();
        Node node = new Node(currentNode.getNodeID(), displayName);
        node.setGroup(currentNode.getNodeType());
        node.setQualifiedName(currentNode.getQualifiedName());
        node.setProperties(currentNode.getProperties());
        return node;
    }
}