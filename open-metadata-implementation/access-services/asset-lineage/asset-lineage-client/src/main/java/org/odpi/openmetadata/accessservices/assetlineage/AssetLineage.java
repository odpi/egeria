/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class AssetLineage extends FFDCRESTClient implements AssetLineageInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/access-services/asset-lineage/users/{1}/";
    private static final String PUBLISH_ENTITIES = "publish-entities/{2}";
    private static final String PUBLISH_CONTEXT = "publish-context/{2}/{3}";
    private static final String UPDATED_AFTER_DATE = "?updatedAfterDate={3}";

    private String userId;

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Instantiates a new Asset lineage Client.
     *
     * @param serverName            the server name
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public AssetLineage(String serverName, String serverPlatformURLRoot) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    /**
     * Instantiates a new Asset Lineage Client.
     *
     * @param serverName            the server name
     * @param serverPlatformURLRoot the server platform url root
     * @param userId                the user id
     * @param password              the password
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public AssetLineage(String serverName, String serverPlatformURLRoot, String userId, String password)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
        this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> publishEntities(String serverName, String userId, String entityType, Optional<Date> updatedAfterDate)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "publishEntities";

        invalidParameterHandler.validateUserId(methodName, userId);
        String urlTemplate = serverPlatformURLRoot + BASE_PATH + PUBLISH_ENTITIES;

        GUIDListResponse response;
        if(updatedAfterDate.isPresent()) {
            String updatedAfterAsString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                    .format(updatedAfterDate.get());
            response = callGUIDListGetRESTCall(methodName, urlTemplate + UPDATED_AFTER_DATE,
                    serverName, userId, entityType, updatedAfterAsString);
        } else {
            response = callGUIDListGetRESTCall(methodName, urlTemplate, serverName, userId, entityType);
        }
        exceptionHandler.detectAndThrowInvalidParameterException(response);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(response);
        exceptionHandler.detectAndThrowPropertyServerException(response);

        return response.getGUIDs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> publishAssetContext(String userId, String guid, String entityType)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "publishAssetContext";

        invalidParameterHandler.validateUserId(methodName, userId);

        String urlTemplate = serverPlatformURLRoot + BASE_PATH + PUBLISH_CONTEXT;
        GUIDListResponse response = callGetRESTCall(methodName, GUIDListResponse.class, urlTemplate, serverName,
                userId, entityType, guid);

        exceptionHandler.detectAndThrowInvalidParameterException(response);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(response);
        exceptionHandler.detectAndThrowPropertyServerException(response);

        return response.getGUIDs();
    }

}
