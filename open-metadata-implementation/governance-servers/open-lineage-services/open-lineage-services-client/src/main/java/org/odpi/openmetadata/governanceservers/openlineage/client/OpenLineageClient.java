/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageQueryParameters;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.util.OpenLineageExceptionHandler;


public class OpenLineageClient extends FFDCRESTClient implements OpenLineageInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/open-lineage/users/{1}";

    private static final String LINEAGE = "/lineage/";
    private static final String ENTITIES = "/entities/{2}";
    private static final String DETAILS = "/details";
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
                                           Scope scope,
                                           String guid,
                                           String displayNameMustContain,
                                           boolean includeProcesses)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, OpenLineageException {
        String methodName = "OpenLineageClient.lineage";
        LineageQueryParameters postBody = new LineageQueryParameters(scope, displayNameMustContain, includeProcesses);

        LineageResponse lineageResponse = callPostRESTCall(methodName, LineageResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + ENTITIES, postBody, serverName, userId, guid);

        detectExceptions(methodName, lineageResponse);
        LineageVerticesAndEdges lineageVerticesAndEdges = lineageResponse.getLineageVerticesAndEdges();
        return lineageVerticesAndEdges;
    }

    public LineageVertex getEntityDetails(String userId, String guid) throws InvalidParameterException, PropertyServerException, OpenLineageException {
        String methodName = "OpenLineageClient.getEntityDetails";
        LineageVertexResponse lineageVertexResponse = callGetRESTCall(methodName, LineageVertexResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINEAGE + ENTITIES + DETAILS, serverName, userId, guid);

        detectExceptions(methodName, lineageVertexResponse);
        LineageVertex entityDetails = lineageVertexResponse.getLineageVertex();
        return entityDetails;
    }

    private void detectExceptions(String methodName,
                                  FFDCResponseBase response)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, OpenLineageException {
        openLineageExceptionHandler.detectAndThrowInvalidParameterException(response);
        openLineageExceptionHandler.detectAndThrowPropertyServerException(response);
        openLineageExceptionHandler.detectAndThrowOpenLineageException(methodName, response);
    }

}