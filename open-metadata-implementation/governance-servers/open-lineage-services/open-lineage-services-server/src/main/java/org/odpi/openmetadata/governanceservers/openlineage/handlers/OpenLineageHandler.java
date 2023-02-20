/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageQueryService;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.NodeNamesSearchCriteria;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.openlineage.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageTypesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;

public class OpenLineageHandler {

    private final OpenLineageQueryService openLineageQueryService;

    public OpenLineageHandler(OpenLineageQueryService openLineageQueryService) {
        this.openLineageQueryService = openLineageQueryService;
    }

    /**
     * Returns a lineage subgraph.
     *
     * @param scope            source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param guid             The guid of the node of which the lineage is queried from.
     * @param includeProcesses
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public LineageResponse lineage(Scope scope, String guid, boolean includeProcesses) throws OpenLineageException {
        return openLineageQueryService.lineage(scope, guid, includeProcesses);
    }

    /**
     * Gets entity details.
     *
     * @param guid the guid
     * @return the entity details
     */
    public LineageVertexResponse getEntityDetails(String guid) {
        return openLineageQueryService.getEntityDetails(guid);
    }

    /**
     * Gets entity details.
     *
     * @param request the request
     * @return the entity details
     */
    public LineageSearchResponse search(LineageSearchRequest request) {
        return openLineageQueryService.search(request);
    }

    /**
     * Gets available entities types from lineage repository.
     *
     * @return the available entities types
     */
    public LineageTypesResponse getTypes() {
        return openLineageQueryService.getTypes();
    }

    /**
     * Gets nodes names of certain type with display name containing a certain value - case insensitive
     * @param searchCriteria contains the type of the node names to search for, a search string being part
     *                      of the display name of the nodes, the maximum number of node names to retrieve
     * @return the node names that match criteria
     */
    public LineageNodeNamesResponse getNodes(NodeNamesSearchCriteria searchCriteria) {
        return openLineageQueryService.getNodes(searchCriteria);
    }

    /**
     * Returns a subraph representing the hierarchy of a certain node, based on the request
     *
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType
     *                                of the display name of the nodes, the maximum number of node names to retrieve
     *
     * @return nodes and edges representing the element hierarchy
     */
    public LineageResponse getElementHierarchy(ElementHierarchyRequest elementHierarchyRequest) {
        return openLineageQueryService.getElementHierarchy(elementHierarchyRequest);
    }
}
