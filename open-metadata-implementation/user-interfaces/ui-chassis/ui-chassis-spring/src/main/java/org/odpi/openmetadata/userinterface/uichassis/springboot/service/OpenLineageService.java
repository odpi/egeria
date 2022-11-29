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
import org.odpi.openmetadata.governanceservers.openlineage.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.openlineage.requests.LineageSearchRequest;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.LineageNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.OpenLineageServiceException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(LineageGraphDisplayService.class)
public class OpenLineageService {

    private final OpenLineageClient openLineageClient;
    private final LineageGraphDisplayService lineageGraphDisplayService;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    /**
     * @param openLineageClient          client to connect to open lineage services
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
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public Graph getUltimateSource(String userId,
                                   String guid,
                                   boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.ULTIMATE_SOURCE, guid, includeProcesses);
            return processResponse(response, guid);
        } catch (PropertyServerException e) {
            LOG.error("Cannot get ultimate source lineage for guid {}", guid);
            throw e;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot get ultimate source for guid {}", guid);
            throw new LineageNotFoundException("ultimate source lineage error", e);
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", guid);
            throw new OpenLineageServiceException("entity details error", e);
        }

    }


    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the end to end flow
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public Graph getEndToEndLineage(String userId,
                                    String guid,
                                    boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.END_TO_END, guid, includeProcesses);
            return processResponse(response, guid);
        } catch (PropertyServerException e) {
            LOG.error("Cannot get end2end lineage for guid {}", guid);
            throw e;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot get end2end lineage for guid {}", guid);
            throw new LineageNotFoundException("end2end lineage error", e);
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", guid);
            throw new OpenLineageServiceException("entity details error", e);
        }
    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the ultimate destinations of the asset
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public Graph getUltimateDestination(String userId,
                                        String guid,
                                        boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.ULTIMATE_DESTINATION, guid, includeProcesses);
            return processResponse(response, guid);
        } catch (PropertyServerException e) {
            LOG.error("Cannot get ultimate destination lineage for guid {}", guid);
            throw e;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot get ultimate destination for guid {}", guid);
            throw new LineageNotFoundException("ultimate destination lineage error", e);
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", guid);
            throw new OpenLineageServiceException("entity details error", e);
        }


    }

    /**
     * @param userId           id of the user triggering the request
     * @param guid             unique identifier if the asset
     * @param includeProcesses if true includes processes in the response
     * @return map of nodes and edges describing the glossary terms linked to the asset
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public Graph getVerticalLineage(String userId,
                                    String guid,
                                    boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            LineageVerticesAndEdges response = openLineageClient.lineage(userId, Scope.VERTICAL, guid, includeProcesses);
            return processResponse(response, guid);
        } catch (PropertyServerException e) {
            LOG.error("Error while trying to retrieve verical lineage {}", e.getMessage());
            throw e;
        } catch (InvalidParameterException e) {
            LOG.error("Cannot get vertical lineage for guid {}", guid);
            throw new LineageNotFoundException("vertical lineage error", e);
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", guid);
            throw new OpenLineageServiceException("entity details error", e);
        }

    }

    /**
     * Gets node details.
     *
     * @param userId the user id
     * @param guid   the guid
     * @return the node details
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public LineageVertex getEntityDetails(String userId, String guid)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            return openLineageClient.getEntityDetails(userId, guid);
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error("Cannot get node details for guid {}", guid);
            throw e;
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", guid);
            throw new OpenLineageServiceException("entity details error", e);
        }
    }

    /**
     * Gets node details.
     *
     * @param userId the user id
     * @param lineageSearchRequest the body for search
     * @return the node details
     * @throws InvalidParameterException from the underlying client
     * @throws PropertyServerException from the underlying client
     * @throws OpenLineageException from the underlying client
     */
    public List<LineageVertex> search(String userId, LineageSearchRequest lineageSearchRequest)
            throws InvalidParameterException, PropertyServerException, OpenLineageException {
        try {
            return openLineageClient.search(userId, lineageSearchRequest);
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error("Error during search with request {}", lineageSearchRequest);
            throw e;
        } catch (OpenLineageException e) {
            LOG.error("Error while calling open lineage services {}", lineageSearchRequest);
            throw new OpenLineageServiceException("entity details error", e);
        }
    }

    /**
     * Gets available entities types from lineage repository.
     * @param userId user ID
     * @return the available entities types
     */
    public List<String> getTypes(String userId) {
        try {
            return openLineageClient.getTypes(userId);
        } catch (PropertyServerException | InvalidParameterException | OpenLineageException e) {
            LOG.error("Cannot get entities types in the lineage graph");
            throw new OpenLineageServiceException("entities types retrieval error", e);
        }
    }

    /**
     * Gets nodes names of certain type with display name containing a certain value.
     * @param userId      user ID
     * @param type        the type of the nodes name to search for
     * @param searchValue the string to be contained in the qualified name of the node - case insensitive
     * @param limit       the maximum number of node names to retrieve
     * @return the list of node names
     */
    public List<String> getNodes(String userId, String type, String searchValue, int limit) {
        try {
            return openLineageClient.getNodes(userId, type, searchValue, limit);
        } catch (PropertyServerException | InvalidParameterException | OpenLineageException e) {
            LOG.error("Cannot get node names from the lineage graph");
            throw new OpenLineageServiceException("node names retrieval error", e);
        }
    }

    /**
     * Returns a subraph representing the hierarchy of a certain node, based on the request
     *
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType of the display name of the nodes
     *
     * @return a subgraph containing all relevant paths,
     */
    public Graph getElementHierarchy(String userId, ElementHierarchyRequest elementHierarchyRequest) {
        try {
            LineageVerticesAndEdges response = openLineageClient.getElementHierarchy(userId, elementHierarchyRequest);
            return processResponse(response, elementHierarchyRequest.getGuid());
        } catch (PropertyServerException | InvalidParameterException | OpenLineageException e) {
            LOG.error("Cannot get node names from the lineage graph");
            throw new OpenLineageServiceException("node names retrieval error", e);
        }
    }
    /**
     * @param response string returned from Open Lineage Services to be processed
     * @param guid     the guid to process
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
        return new Edge(currentEdge.getId().toString(), currentEdge.getSourceNodeID(),
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