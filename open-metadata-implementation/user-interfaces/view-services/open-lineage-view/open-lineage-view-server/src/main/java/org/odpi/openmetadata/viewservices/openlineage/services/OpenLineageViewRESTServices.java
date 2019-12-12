/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.openlineage.services;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.*;
import org.odpi.openmetadata.viewservices.openlineage.admin.handlers.OpenLineageViewInstanceHandler;
import org.odpi.openmetadata.viewservices.openlineage.objects.graph.Edge;
import org.odpi.openmetadata.viewservices.openlineage.objects.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class responsibility is to interact with Open Lineage Services(OLS), process the returned response and return it in a format understood by view
 */

public class OpenLineageViewRESTServices {

    public static final String EDGES_LABEL = "edges";
    public static final String NODES_LABEL = "nodes";
    public static final String GLOSSARY_TERM = "glossaryTerm";

    protected static OpenLineageViewInstanceHandler instanceHandler     = new OpenLineageViewInstanceHandler();

  //  private com.fasterxml.jackson.databind.ObjectMapper mapper;
    //@Value("${open.lineage.graph.source}")
    private GraphName graphName;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageViewRESTServices.class);

    /**
     * Default constructor
     */

    public OpenLineageViewRESTServices() {
       // mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    /**
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources for the asset
     */
    public Map<String, List> getUltimateSource(String serverName, String userId, View view, String guid)  {
        OpenLineageClient openLineageClient = instanceHandler.getOpenLineageClient(serverName, userId, "getUltimateSource");
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_SOURCE, view, guid, "", true);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PropertyServerException e) {
            e.printStackTrace();
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        return processResponse(response);
    }

    /**
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the end to end flow
     */
    public Map<String, List> getEndToEndLineage(String serverName, String userId, View view, String guid)  {
        OpenLineageClient openLineageClient = instanceHandler.getOpenLineageClient(serverName, userId, "getEndToEndLineage");
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.END_TO_END, view, guid, "", true);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PropertyServerException e) {
            e.printStackTrace();
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        return processResponse(response);
    }

    /**
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate destinations of the asset
     */
    public Map<String, List> getUltimateDestination(String serverName, String userId, View view, String guid)  {
        OpenLineageClient openLineageClient = instanceHandler.getOpenLineageClient(serverName, userId, "getEndToEndLineage");
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_DESTINATION, view, guid, "", true);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PropertyServerException e) {
            e.printStackTrace();
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        return processResponse(response);

    }

    /**
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the glossary terms linked to the asset
     */
    public Map<String, List> getGlossaryLineage(String serverName, String userId, View view, String guid)  {
        OpenLineageClient openLineageClient = instanceHandler.getOpenLineageClient(serverName, userId, "getGlossaryLineage");
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.GLOSSARY, view, guid, "", true);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PropertyServerException e) {
            e.printStackTrace();
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        return processResponse(response);
    }

    /**
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources and destinations of the asset
     */
    public Map<String, List> getSourceAndDestination(String serverName, String userId, View view, String guid)  {
         OpenLineageClient openLineageClient = instanceHandler.getOpenLineageClient(serverName, userId, "getSourceAndDestination");
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.SOURCE_AND_DESTINATION, view, guid, "", true);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (PropertyServerException e) {
            e.printStackTrace();
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        return processResponse(response);
    }

    /**
     *
     * @param response string returned from Open Lineage Services to be processed
     * @return map of nodes and edges describing the end to end flow
     */
    private Map<String, List> processResponse(LineageVerticesAndEdges response)  {
        Map<String, List> graphData = new HashMap<>();
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        LOG.debug("Received response from open lineage service: {}", response);
        Set<LineageVertex> lineageVertices = response.getLineageVertices();

        if (response == null || lineageVertices == null || lineageVertices.isEmpty()) {
            graphData.put(EDGES_LABEL, listEdges);
            graphData.put(NODES_LABEL, listNodes);
        }

        listNodes = Optional.ofNullable(response.getLineageVertices())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(v -> createNode(v))
                .collect(Collectors.toList());

        listEdges = Optional.ofNullable(response.getLineageEdges())
                .map(e -> e.stream())
                .orElseGet(Stream::empty)
                .map(e -> {Edge newEdge = new Edge( e.getSourceNodeID(),
                        e.getDestinationNodeID());
                    newEdge.setLabel(e.getEdgeType());
                    return newEdge;})
                .collect(Collectors.toList());

        graphData.put(EDGES_LABEL, listEdges);
        graphData.put(NODES_LABEL, listNodes);

        return graphData;
    }

    /**
     * This method will create a new node in ui specific format based on the properties of the currentNode to be processed
     * @param currentNode current node to be processed
     * @return the node in the format to be understand by the ui
     *
     */
    private Node createNode(LineageVertex currentNode) {
        String displayName = currentNode.getDisplayName();
        String glossaryTerm = "";
        Map<String, String> currentAttributeMap = currentNode.getAttributes();

        if(currentAttributeMap !=null && !currentAttributeMap.isEmpty()) {
            glossaryTerm = currentNode.getAttributes().get(GLOSSARY_TERM);
        }

        if (glossaryTerm !=null && !glossaryTerm.isEmpty()) {
            displayName = displayName + "\n" + glossaryTerm;
        }
        Node node = new Node(currentNode.getNodeID(), displayName);
        node.setGroup(currentNode.getDisplayName());
        node.setProperties(currentNode.getAttributes());
        return node;
    }


}