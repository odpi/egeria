/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineage;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
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

@Service
public class OpenLineageService {

    private final OpenLineage openLineageClient;
    //TODO add authentication
    private final String user = "demo";
    private com.fasterxml.jackson.databind.ObjectMapper mapper;
    private @Value("${open.lineage.graph.source}")
    GraphName graphName;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    @Autowired
    public OpenLineageService(OpenLineage openLineageClient) {
        this.openLineageClient = openLineageClient;
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public String generateMockGraph(String userId){
        return openLineageClient.generateMockGraph(user);
    }

    public Map<String, Object> exportGraph(String userId) throws IOException {
        String exportedGraph = openLineageClient.exportGraph(user, graphName);
        Map<String, Object> graphData = processResponse(exportedGraph);
        return graphData;
    }

    public Map<String, Object> getUltimateSource(String userId, View view, String guid) throws IOException {
        String response = openLineageClient.lineage(user, graphName, Scope.ULTIMATESOURCE, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    public Map<String, Object> getEndToEndLineage(String userId, View view, String guid) throws IOException {
        String response = openLineageClient.lineage(user, graphName, Scope.ENDTOEND, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    public Map<String, Object> getUltimateDestination(String userId, View view, String guid) throws IOException {
        String response = openLineageClient.lineage(user, graphName, Scope.ULTIMATEDESTINATION, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    public Map<String, Object> getGlossaryLineage(String userId, View view, String guid) throws IOException {
        String response = openLineageClient.lineage(user, graphName, Scope.GLOSSARY, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    public Map<String, Object> getSourceAndDestination(String userId, View view, String guid) throws IOException {
        String response = openLineageClient.lineage(user, graphName, Scope.SOURCEANDDESTINATION, view, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    private Map<String, Object> processResponse(String response) throws IOException {
        final ObjectMapper mapper = this.mapper;
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        LOG.debug("Received response:{}", response);
        Response responseObj = mapper.readValue(response, Response.class);
        Optional.ofNullable(responseObj.getVertices())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(v -> addNodeAndEdges(v, listNodes, listEdges));

        Map<String, Object> graphData = new HashMap<>();
        graphData.put("edges", listEdges);
        graphData.put("nodes", listNodes);
        return graphData;
    }

    private void addNodeAndEdges(Vertice vertice, List<Node> listNodes, List<Edge> listEdges) {
        String labelRoot = vertice.getLabel();
        String idRoot = String.valueOf(vertice.getId().getValue());

        Map<String, String> properties = Optional.ofNullable(vertice.getProperties())
                .map(e -> e.entrySet().stream())
                .orElseGet(Stream::empty)
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().get(0).getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String displayName = properties.get("vedisplayName");
        String glossaryTerm = properties.get("veglossaryTerm");
        if(StringUtils.isEmpty(displayName)) {
            displayName = properties.get("displayName");
        }
        if(!StringUtils.isEmpty(glossaryTerm)){
            displayName = displayName + "\n" + glossaryTerm;
        }
        Node node = new Node(idRoot, displayName);
        node.setGroup(labelRoot);
        node.setProperties(properties);

        listEdges.addAll(Optional.ofNullable(vertice.getInE())
                .map(e -> e.entrySet().stream())
                .orElseGet(Stream::empty)
                .flatMap(e -> createEdges(idRoot, e.getKey(),  e.getValue()).stream())
                .collect(Collectors.toList()));

        listNodes.add(node);
    }

    private List<Edge> createEdges(String idRoot, String key, List<org.odpi.openmetadata.userinterface.accessservices.service.response.Edge> values) {
        return Optional.ofNullable(values)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(e -> { Edge newEdge = new Edge(idRoot, String.valueOf(e.getOutV().getValue()));
                            newEdge.setLabel(key);
                            return newEdge;})
                .collect(Collectors.toList());
    }


}
