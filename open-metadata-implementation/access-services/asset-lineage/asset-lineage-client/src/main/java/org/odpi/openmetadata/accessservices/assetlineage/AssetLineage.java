/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * {@inheritDoc}
 */
public class AssetLineage extends FFDCRESTClient implements AssetLineageInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/access-services/asset-lineage/users/{1}/";
    private static final String PUBLISH_ENTITIES = "/publish-entities/{2}";

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new AssetLineage client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException if parameter validation fails
     */
    public AssetLineage(String serverName, String serverPlatformURLRoot) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public AssetLineage(String serverName, String serverPlatformURLRoot, String userId, String password)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<String> publishEntities(String serverName, String userId, String entityType)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "publishEntities";

        invalidParameterHandler.validateUserId(methodName, userId);

        GUIDListResponse response = callGUIDListGetRESTCall(methodName,
                serverPlatformURLRoot + BASE_PATH + PUBLISH_ENTITIES,
                serverName,
                userId,
                entityType);

        exceptionHandler.detectAndThrowInvalidParameterException(response);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(response);
        exceptionHandler.detectAndThrowPropertyServerException(response);

        return response.getGUIDs();
    }
}
