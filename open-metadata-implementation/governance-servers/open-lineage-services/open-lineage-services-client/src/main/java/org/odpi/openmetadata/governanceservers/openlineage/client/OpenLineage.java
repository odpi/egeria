/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

/**
 * The Asset Catalog Open Metadata Access Service (OMAS) provides an interface to search for assets including
 * data stores, event feeds, APIs and data sets, related assets and relationships.
 * Also, it can return the connection details for the asset metadata.
 * The Asset Catalog OMAS includes:
 * <ul>
 * <li>Client-side  provides language-specific client packages to make it easier for data tools and applications to call the interface.</li>
 * <li>OMAS Server calls to retrieve assets and information related to the assets.</li>
 * </ul>
 */
public class OpenLineage extends FFDCRESTClient implements OpenLineageInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/open-lineage/users/{1}";

    private static final String LINEAGE = "/lineage/";
    private static final String LINEAGE_SOURCES = "/sources/{2}";
    private static final String LINEAGE_SCOPES = "/scopes/{3}";
    private static final String LINEAGE_VIEWS = "/views/{4}";
    private static final String LINEAGE_ENTITIES = "/entities/{5}";
    private RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    /**
     * Create a new AssetConsumer client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException if parameter validation fails
     */
    public OpenLineage(String serverName, String serverPlatformURLRoot)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public OpenLineage(String serverName, String serverPlatformURLRoot, String userId, String password)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * {@inheritDoc}
     */
    public LineageVerticesAndEdges lineage(String userId,
                                           GraphName graphName,
                                           Scope scope,
                                           View view,
                                           String guid)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "lineage";
        LineageResponse lineageResponse = callGetRESTCall(methodName, LineageResponse.class,
                serverPlatformURLRoot +
                        BASE_PATH +
                        LINEAGE +
                        LINEAGE_SOURCES +
                        LINEAGE_SCOPES +
                        LINEAGE_VIEWS +
                        LINEAGE_ENTITIES,
                serverName, userId, graphName.getText(), scope.getValue(), view.getValue(), guid);
        detectExceptions(methodName, lineageResponse);
        LineageVerticesAndEdges lineageVerticesAndEdges = lineageResponse.getLineageVerticesAndEdges();
        return lineageVerticesAndEdges;
    }

    private void detectExceptions(String methodName,
                                  LineageResponse response)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, response);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, response);
    }

}