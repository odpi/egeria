/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineage;
import org.odpi.openmetadata.governanceservers.openlineage.model.Graph;
import org.odpi.openmetadata.governanceservers.openlineage.model.Query;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.accessservices.beans.Edge;
import org.odpi.openmetadata.userinterface.accessservices.beans.Node;
import org.odpi.openmetadata.userinterface.accessservices.service.response.Property;
import org.odpi.openmetadata.userinterface.accessservices.service.response.Response;
import org.odpi.openmetadata.userinterface.accessservices.service.response.Vertice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenLineageService {

    private final OpenLineage openLineageClient;
    //TODO add authentication
    private final String user = "demo";
    private com.fasterxml.jackson.databind.ObjectMapper mapper;
    private @Value("${open.lineage.graph.source}") Graph graph;
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
        String exportedGraph = openLineageClient.exportGraph(user, graph);
        Map<String, Object> graphData = processResponse(exportedGraph);
        return graphData;
    }

    public Map<String, Object> getUltimateSource(String userId, Scope scope, String guid) throws IOException {
        String response = openLineageClient.queryLineage(user, scope, Query.ULTIMATESOURCE, graph, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    public Map<String, Object> getEndToEndLineage(String userId, Scope scope, String guid) throws IOException {
        String response = openLineageClient.queryLineage(user, scope, Query.ENDTOEND, graph, guid);
        Map<String, Object> graphData = processResponse(response);
        return graphData;
    }

    private Map<String, Object> processResponse(String response) throws IOException {
        final ObjectMapper mapper = this.mapper;
        Map<String, Object> graphObject;
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();
        try {
            LOG.debug(" Received response:{}", response);
            Response responseObj = mapper.readValue(response, Response.class);

            for(Vertice vertice : responseObj.getVertices()){
                String labelRoot = vertice.getLabel();
                String idRoot = String.valueOf(vertice.getId().getValue());
                Node node = new Node(idRoot, labelRoot + idRoot);
                node.setGroup(labelRoot);
                Map<String, String> properties = new HashMap<>();
                if(vertice.getProperties()!= null && !vertice.getProperties().isEmpty()){
                    for(Map.Entry<String, List<Property>> propertyEntry: vertice.getProperties().entrySet()){
                        properties.put(propertyEntry.getKey(), propertyEntry.getValue().get(0).getValue());
                    }
                }
                node.setProperties(properties);
                String displayName = properties.get("vedisplayName");
                String glossaryTerm = properties.get("veglossaryTerm");
                if(StringUtils.isEmpty(displayName)) {
                    displayName = properties.get("displayName");
                }
                if(!StringUtils.isEmpty(glossaryTerm)){
                    displayName = displayName + "\n" + glossaryTerm;
                }
                node.setLabel(displayName);
                listNodes.add(node);
                if(vertice.getInE()!= null && !vertice.getInE().isEmpty()) {
                    for (Map.Entry<String, List<org.odpi.openmetadata.userinterface.accessservices.service.response.Edge>> entry :  vertice.getInE().entrySet()) {

                        for(org.odpi.openmetadata.userinterface.accessservices.service.response.Edge edge: entry.getValue()) {
                            String value = String.valueOf(edge.getOutV().getValue());
                            Edge newEdge = new Edge(idRoot, value);
                            newEdge.setLabel(entry.getKey());
                            listEdges.add(newEdge);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }

        Map<String, Object> graphData = new HashMap<>();
        graphData.put("edges", listEdges);
        graphData.put("nodes", listNodes);
        return graphData;
    }


}
