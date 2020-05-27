/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class responsibility is to interact with Open Lineage Services(OLS), process the returned response and return it in a format understood by view
 */
@Service
public class OpenLineageService {

    public static final String EDGES_LABEL = "edges";
    public static final String NODES_LABEL = "nodes";
    private final OpenLineageClient openLineageClient;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    /**
     * @param openLineageClient client to connect to open lineage services
     */
    @Autowired
    public OpenLineageService(OpenLineageClient openLineageClient) {
        this.openLineageClient = openLineageClient;
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses
     * @return map of nodes and edges describing the ultimate sources for the asset
     */
    public Map<String, List> getUltimateSource(String userId, String guid, boolean includeProcesses) {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, Scope.ULTIMATE_SOURCE, guid, "", includeProcesses);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error(e.getErrorMessage(), e);
        }
        return processResponse(response);
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses
     * @return map of nodes and edges describing the end to end flow
     */
    public Map<String, List> getEndToEndLineage(String userId, String guid, boolean includeProcesses) {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, Scope.END_TO_END, guid, "", includeProcesses);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error(e.getErrorMessage(), e);
        }
        return processResponse(response);
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses
     * @return map of nodes and edges describing the ultimate destinations of the asset
     */
    public Map<String, List> getUltimateDestination(String userId, String guid, boolean includeProcesses) {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, Scope.ULTIMATE_DESTINATION, guid, "",
                    includeProcesses);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error(e.getErrorMessage(), e);
        }

        return processResponse(response);
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses
     * @return map of nodes and edges describing the glossary terms linked to the asset
     */
    public Map<String, List> getGlossaryLineage(String userId, String guid, boolean includeProcesses) {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, Scope.GLOSSARY, guid, "", includeProcesses);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error(e.getErrorMessage(), e);
        }

        return processResponse(response);
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses
     * @return map of nodes and edges describing the ultimate sources and destinations of the asset
     */
    public Map<String, List> getSourceAndDestination(String userId, String guid, boolean includeProcesses) {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, Scope.SOURCE_AND_DESTINATION, guid, "",
                    includeProcesses);
        } catch (InvalidParameterException | PropertyServerException | OpenLineageException e) {
            LOG.error(e.getErrorMessage(), e);
        }

        return processResponse(response);
    }

    /**
     * @param response string returned from Open Lineage Services to be processed
     * @return map of nodes and edges describing the end to end flow
     */
    private Map<String, List> processResponse(LineageVerticesAndEdges response) {
        Map<String, List> graphData = new HashMap<>();
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        LOG.debug("Received response from open lineage service: {}", response);
        if (response == null || CollectionUtils.isEmpty(response.getLineageVertices())) {
            graphData.put(EDGES_LABEL, listEdges);
            graphData.put(NODES_LABEL, listNodes);
        }

        listNodes = Optional.ofNullable(response).map(LineageVerticesAndEdges::getLineageVertices)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(this::createNode)
                .collect(Collectors.toList());

        listEdges = Optional.ofNullable(response).map(LineageVerticesAndEdges::getLineageEdges)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(e -> new Edge(e.getSourceNodeID(),
                        e.getDestinationNodeID()))
                .collect(Collectors.toList());

        graphData.put(EDGES_LABEL, listEdges);
        graphData.put(NODES_LABEL, listNodes);

        return graphData;
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
        node.setProperties(currentNode.getProperties());
        return node;
    }

}