/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.util.OpenLineageExceptionHandler;


public class OpenLineageClient extends FFDCRESTClient implements OpenLineageInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/open-lineage/users/{1}";

    private static final String LINEAGE = "/lineage";
    private static final String LINEAGE_SOURCES = "/sources/{2}";
    private static final String LINEAGE_SCOPES = "/scopes/{3}";
    private static final String LINEAGE_VIEWS = "/views/{4}";
    private static final String LINEAGE_ENTITIES = "/entities/{5}";
    private static final String DISPLAYNAME_CONTAINS = "displayname-contains={6}";
    private static final String INCLUDE_PROCESSES = "include-processes={7}";
    private OpenLineageExceptionHandler openLineageExceptionHandler = new OpenLineageExceptionHandler();

    /**
     * Create a new OpenLineage client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException if parameter validation fails
     */
    public OpenLineageClient(String serverName, String serverPlatformURLRoot)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public OpenLineageClient(String serverName, String serverPlatformURLRoot, String userId, String password)
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
                                           String guid,
                                           String displayNameMustContain,
                                           boolean includeProcesses)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, OpenLineageException {
        String methodName = "OpenLineageClient.lineage";
        LineageResponse lineageResponse = callGetRESTCall(methodName, LineageResponse.class,
                serverPlatformURLRoot +
                        BASE_PATH +
                        LINEAGE +
                        LINEAGE_SOURCES +
                        LINEAGE_SCOPES +
                        LINEAGE_VIEWS +
                        LINEAGE_ENTITIES +
                        "?" +
                        DISPLAYNAME_CONTAINS +
                        "&" +
                        INCLUDE_PROCESSES,
                serverName, userId, graphName.getValue(), scope.getValue(), view.getValue(), guid, displayNameMustContain, includeProcesses);
        detectExceptions(methodName, lineageResponse);
        LineageVerticesAndEdges lineageVerticesAndEdges = lineageResponse.getLineageVerticesAndEdges();
        return lineageVerticesAndEdges;
    }

    private void detectExceptions(String methodName,
                                  LineageResponse response)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, OpenLineageException {
        openLineageExceptionHandler.detectAndThrowInvalidParameterException(methodName, response);
        openLineageExceptionHandler.detectAndThrowPropertyServerException(methodName, response);
        openLineageExceptionHandler.detectAndThrowOpenLineageException(methodName, response);
    }

}