/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineage;
import org.odpi.openmetadata.governanceservers.openlineage.model.Graphs;
import org.odpi.openmetadata.userinterface.accessservices.beans.Edge;
import org.odpi.openmetadata.userinterface.accessservices.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenLineageService {

    private final OpenLineage openLineageClient;
    //TODO add authentication
    private final String user = "demo-repo";
    private com.fasterxml.jackson.databind.ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    @Autowired
    public OpenLineageService(OpenLineage openLineageClient) {
        this.openLineageClient = openLineageClient;
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public String generateMockGraph(String userId){
        return openLineageClient.generateMockGraph(user);
    }

    public Map<String, Object> exportGraph(String userId, Graphs graph) throws IOException {
        String exportedGraph = openLineageClient.exportGraph(user, graph);
        final ObjectMapper mapper = this.mapper;
        Map<String, Object> graphObject;
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();
        try {
            List<String> lines = Arrays.asList(exportedGraph.split("\n"));
            for(String line: lines) {
                graphObject = (Map<String, Object>) mapper.readValue(line, Object.class);
                String idRoot = String.valueOf(mapper.readTree(line).get("id").get("@value"));
                String labelRoot = (String) graphObject.get("label");

                if(graphObject.get("inE") != null) {
                    //TODO adjust depending on format
                    Object newEdges =
                            ((LinkedHashMap) graphObject.get("inE")).values().stream().map(e -> ((List) e)).flatMap(
                                    e -> ((List) e).stream()).map(e -> ((LinkedHashMap) e).get("outV")).map(e -> new Edge(idRoot, String.valueOf(((Map)e).get("@value")))).collect(Collectors.toList());
                    listEdges.addAll((List) newEdges);
                }

                Node node = new Node(idRoot, labelRoot + idRoot);
                node.setGroup(labelRoot);
                listNodes.add(node);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }


        Map<String, Object> graphData = new HashMap<>();
        graphData.put("edges", listEdges);
        graphData.put("nodes", listNodes);
        return graphData;

    }


}
