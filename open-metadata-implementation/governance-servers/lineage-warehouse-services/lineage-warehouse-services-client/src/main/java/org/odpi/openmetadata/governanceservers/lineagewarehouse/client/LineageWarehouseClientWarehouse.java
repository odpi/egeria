/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.client;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageQueryParameters;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageTypesResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.responses.LineageVertexResponse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.util.LineageWarehouseExceptionHandler;

import java.util.List;


public class LineageWarehouseClientWarehouse extends FFDCRESTClient implements LineageWarehouseInterface
{

    private static final String BASE_PATH = "/servers/{0}/open-metadata/lineage-warehouse/users/{1}";

    private static final String LINEAGE = "/lineage/";
    private static final String ENTITIES = "/entities/{2}";
    private static final String DETAILS = "/details";
    public static final String TYPES = "types";
    public static final String NODES = "nodes?type={2}&name={3}&limit={4}";
    private static final String SEARCH = "/search";
    private static final String                           HIERARCHY                        = "/elements/hierarchy";
    private              LineageWarehouseExceptionHandler lineageWarehouseExceptionHandler = new LineageWarehouseExceptionHandler();

    /**
     * Create a new OpenLineage client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException if parameter validation fails
     */
    public LineageWarehouseClientWarehouse(String serverName, String serverPlatformURLRoot)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public LineageWarehouseClientWarehouse(String serverName, String serverPlatformURLRoot, String userId, String password)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * {@inheritDoc}
     */
    public LineageVerticesAndEdges lineage(String userId,
                                           Scope scope,
                                           String guid,
                                           boolean includeProcesses)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.lineage";
        LineageQueryParameters postBody = new LineageQueryParameters(scope, includeProcesses);

        LineageResponse lineageResponse = callPostRESTCall(methodName, LineageResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + ENTITIES, postBody, serverName, userId, guid);

        detectExceptions(methodName, lineageResponse);
        return lineageResponse.getLineageVerticesAndEdges();
    }

    public LineageVertex getEntityDetails(String userId, String guid) throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.getEntityDetails";
        LineageVertexResponse lineageVertexResponse = callGetRESTCall(methodName, LineageVertexResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + ENTITIES + DETAILS, serverName, userId, guid);

        detectExceptions(methodName, lineageVertexResponse);
        return lineageVertexResponse.getLineageVertex();
    }

    public List<String> getTypes(String userId) throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.getTypes";
        LineageTypesResponse lineageTypesResponse = callGetRESTCall(methodName, LineageTypesResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + TYPES, serverName, userId);

        detectExceptions(methodName, lineageTypesResponse);
        return lineageTypesResponse.getTypes();
    }

    public List<String> getNodes(String userId, String type, String searchValue, int limit) throws PropertyServerException,
                                                                                                   InvalidParameterException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.getNodes";
        LineageNodeNamesResponse nodeNamesResponse = callGetRESTCall(methodName, LineageNodeNamesResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + NODES, serverName, userId, type, searchValue, limit);

        detectExceptions(methodName, nodeNamesResponse);
        return nodeNamesResponse.getNames();
    }

    public List<LineageVertex> search(String userId, LineageSearchRequest lineageSearchRequest) throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.getEntityDetails";
        LineageSearchResponse lineageSearchResponse = callPostRESTCall(methodName, LineageSearchResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + SEARCH, lineageSearchRequest, serverName, userId);

        detectExceptions(methodName, lineageSearchResponse);
        return lineageSearchResponse.getVertices();
    }

    public LineageVerticesAndEdges getElementHierarchy(String userId, ElementHierarchyRequest elementHierarchyRequest) throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String methodName = "OpenLineageClient.getElementHierarchy";
        LineageResponse response = callPostRESTCall(methodName, LineageResponse.class,
                serverPlatformURLRoot + BASE_PATH + HIERARCHY , elementHierarchyRequest, serverName, userId);

        detectExceptions(methodName, response);
        return response.getLineageVerticesAndEdges();
    }

    private void detectExceptions(String methodName,
                                  FFDCResponseBase response)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        lineageWarehouseExceptionHandler.detectAndThrowInvalidParameterException(response);
        lineageWarehouseExceptionHandler.detectAndThrowPropertyServerException(response);
        lineageWarehouseExceptionHandler.detectAndThrowOpenLineageException(methodName, response);
    }

}