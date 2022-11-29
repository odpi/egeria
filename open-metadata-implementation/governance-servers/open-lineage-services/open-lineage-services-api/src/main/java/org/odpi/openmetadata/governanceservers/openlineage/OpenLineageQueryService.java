/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

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

public interface OpenLineageQueryService {


    /**
     * Retrieve lineage starting from the entity identified by the guid
     *
     * @param scope            the type of lineage to retrieve
     * @param guid             the guid of the entity from which to start
     * @param includeProcesses
     * @return the lineage vertices and edges that compose the graph
     * @throws OpenLineageException
     */
    LineageResponse lineage(Scope scope, String guid, boolean includeProcesses) throws OpenLineageException;

    /**
     * Gets entity details.
     *
     * @param guid the guid
     * @return the entity details
     */
    LineageVertexResponse getEntityDetails(String guid);

    /**
     * Search the database for entities matching the search request
     *
     * @param request the criteria for the search
     * @return all the entities in the graph that match the criteria
     */
    LineageSearchResponse search(LineageSearchRequest request);


    /**
     * Gets available entities types from lineage repository.
     *
     * @return the available entities types
     */
    LineageTypesResponse getTypes();

    /**
     * Gets nodes names of certain type with display name containing a certain value - case insensitive
     * @param searchCriteria contains the type of the node names to search for, a search string being part
     *                      of the display name of the nodes, the maximum number of node names to retrieve
     * @return the node names that match criteria
     */
    LineageNodeNamesResponse getNodes(NodeNamesSearchCriteria searchCriteria);

    /**
     * Retrieve hierarchy starting from the entity identified by the guid
     *
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType
     *                                of the display name of the nodes, the maximum number of node names to retrieve
     *
     * @return the lineage vertices and edges that compose the hierarchy
     *
     */
    LineageResponse getElementHierarchy(ElementHierarchyRequest elementHierarchyRequest);
}
