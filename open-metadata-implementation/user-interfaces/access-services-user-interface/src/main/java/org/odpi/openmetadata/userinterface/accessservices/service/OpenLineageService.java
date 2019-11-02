/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineage;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.accessservices.api.MalformedInputException;
import org.odpi.openmetadata.userinterface.accessservices.beans.Edge;
import org.odpi.openmetadata.userinterface.accessservices.beans.Node;
import org.odpi.openmetadata.userinterface.accessservices.service.response.Response;
import org.odpi.openmetadata.userinterface.accessservices.service.response.Vertice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.AbstractMap;
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
    private final OpenLineage openLineageClient;
    private com.fasterxml.jackson.databind.ObjectMapper mapper;
    @Value("${open.lineage.graph.source}")
    private GraphName graphName;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    /**
     * 
     * @param openLineageClient client to connect to open lineage services
     */
    @Autowired
    public OpenLineageService(OpenLineage openLineageClient) {
        this.openLineageClient = openLineageClient;
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public String generateMockGraph(String userId){
        return openLineageClient.generateMockGraph(userId);
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @return map of nodes and edges describing graph
     */
    public Map<String, Object> exportGraph(String userId)  {
        String exportedGraph = openLineageClient.exportGraph(userId, graphName);
        Map<String, Object> graphData = processResponse(exportedGraph);
        return graphData;
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources for the asset
     */
    public Map<String, Object> getUltimateSource(String userId, View view, String guid)  {
        String response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_SOURCE, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the end to end flow
     */
    public Map<String, Object> getEndToEndLineage(String userId, View view, String guid)  {
        String response = openLineageClient.lineage(userId, graphName, Scope.END_TO_END, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate destinations of the asset
     */
    public Map<String, Object> getUltimateDestination(String userId, View view, String guid)  {
        String response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_DESTINATION, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the glossary terms linked to the asset
     */
    public Map<String, Object> getGlossaryLineage(String userId, View view, String guid)  {
        String response = openLineageClient.lineage(userId, graphName, Scope.GLOSSARY, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    /**
     * 
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources and destinations of the asset
     */
    public Map<String, Object> getSourceAndDestination(String userId, View view, String guid)  {
        String response = openLineageClient.lineage(userId, graphName, Scope.SOURCE_AND_DESTINATION, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    /**
     * 
     * @param response string returned from Open Lineage Services to be processed
     * @return map of nodes and edges describing the end to end flow
     */
    private Map<String, Object> processResponse(String response)  {
        Map<String, Object> graphData = new HashMap<>();
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        LOG.debug("Received response from open lineage service: {}", response);
        if (response == null) {
            graphData.put(EDGES_LABEL, listEdges);
            graphData.put(NODES_LABEL, listNodes);
            return graphData;
        }

        Response responseObj;
        try {
            responseObj = mapper.readValue(response, Response.class);
        } catch (IOException e) {
            throw new MalformedInputException("Unable to process response", e);
        }
        Optional.ofNullable(responseObj.getVertices())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(v -> addNodeAndEdges(v, listNodes, listEdges));

        graphData.put(EDGES_LABEL, listEdges);
        graphData.put(NODES_LABEL, listNodes);
        return graphData;
    }

    /**
     * This method will add a new node to  the list of all nodes based on the properties of the currentNode to be processed
     * and all the relationships linekd to the currentNode in listEdges
     * @param currentNode current node to be processed
     * @param listNodes list of all nodes
     * @param listEdges list of all relationships
     */
    private void addNodeAndEdges(Vertice currentNode, List<Node> listNodes, List<Edge> listEdges) {
        String labelRoot = currentNode.getLabel();
        String idRoot = String.valueOf(currentNode.getId().getValue());

        Map<String, String> properties = Optional.ofNullable(currentNode.getProperties())
                .map(e -> e.entrySet().stream())
                .orElseGet(Stream::empty)
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().get(0).getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String displayName = properties.get("vedisplayName");
        String glossaryTerm = properties.get("veglossaryTerm");//TODO once returned format is finalized, this should be moved to constants
        if(StringUtils.isEmpty(displayName)) {
            displayName = properties.get("displayName");
        }
        if(!StringUtils.isEmpty(glossaryTerm)){
            displayName = displayName + "\n" + glossaryTerm;
        }
        Node node = new Node(idRoot, displayName);
        node.setGroup(labelRoot);
        node.setProperties(properties);

        listEdges.addAll(Optional.ofNullable(currentNode.getInE())
                .map(e -> e.entrySet().stream())
                .orElseGet(Stream::empty)
                .flatMap(e -> createEdges(idRoot, e.getKey(),  e.getValue()).stream())
                .collect(Collectors.toList()));

        listNodes.add(node);
    }

    /**
     * 
     * @param idRoot unique identifier of the current node
     * @param key label of the edges to be added
     * @param edges list of edges linked to the current node
     * @return map of nodes and edges describing the end to end flow
     */
    private List<Edge> createEdges(String idRoot, String key, List<org.odpi.openmetadata.userinterface.accessservices.service.response.Edge> edges) {
        return Optional.ofNullable(edges)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(e -> { Edge newEdge = new Edge(idRoot, String.valueOf(e.getOutV().getValue()));
                            newEdge.setLabel(key);
                            return newEdge;})
                .collect(Collectors.toList());
    }


}
