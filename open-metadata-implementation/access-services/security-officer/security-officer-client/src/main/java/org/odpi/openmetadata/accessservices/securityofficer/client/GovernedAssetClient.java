/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.client;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.*;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * The Security Officer Open Metadata Access Service (OMAS) provides an interface to support a policy engine
 * such as Apache Ranger. See interface definition for more explanation
 */
public class GovernedAssetClient extends FFDCRESTClient implements GovernedAssetInterface
{

    private static final Logger log = LoggerFactory.getLogger(GovernedAssetClient.class);
    private static final String BASE_PATH = "/servers/{0}/open-metadata/access-services/security-officer/users/{1}";

    private static final String GOVERNED_ASSETS_LISTS = "/assets?entityTypes={2}&offset={3}&pageSize={4}";
    private static final String GOVERNED_ASSET = "/assets/{2}";
    private static final String CREATE_SOFTWARE_SERVER = "/software-server-capabilities";
    private static final String GET_SOFTWARE_SERVER = "/software-server-capabilities/{2}";

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Create a new Governance Engine client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException if parameter validation fails
     */
    public GovernedAssetClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public GovernedAssetClient(String serverName, String serverPlatformURLRoot, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GovernedAsset> getGovernedAssetList(String userId, String classification,
                                                          List<String> entityTypes, Integer offset, Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getGovernedAssetList";
        log.debug("Calling method: {}", methodName);

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validatePaging(offset, pageSize, methodName);

        GovernedAssetListResponse response = callGetRESTCall(methodName, GovernedAssetListResponse.class,
                                                             serverPlatformURLRoot + BASE_PATH + GOVERNED_ASSETS_LISTS, serverName, userId, classification, entityTypes, offset, pageSize);

        detectExceptions(methodName, response);
        if (response != null && !CollectionUtils.isEmpty(response.getGovernedAssetList())) {
            return response.getGovernedAssetList();
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GovernedAsset getGovernedAsset(String userId, String assetGuid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getGovernedAsset";
        log.debug("Calling method: {}", methodName);

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateGUID(assetGuid, "guid", methodName);

        GovernedAssetResponse response = callGetRESTCall(methodName, GovernedAssetResponse.class,
                                                         serverPlatformURLRoot + BASE_PATH + GOVERNED_ASSET, serverName, userId, assetGuid);

        detectExceptions(methodName, response);

        return response.getAsset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createSoftwareServerCapability(String userId, SoftwareServerCapabilityRequestBody softwareServerCapability)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "createSoftwareServerCapability";
        log.debug("Calling method: {}", methodName);

        invalidParameterHandler.validateUserId(methodName, userId);

        StringResponse response = callPostRESTCall(methodName, StringResponse.class,
                serverPlatformURLRoot + BASE_PATH + CREATE_SOFTWARE_SERVER,
                softwareServerCapability, serverName, userId);

        restExceptionHandler.detectAndThrowInvalidParameterException(methodName, response);
        restExceptionHandler.detectAndThrowPropertyServerException(methodName, response);
        restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, response);

        return response.getResultString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getSoftwareServerCapabilityByGUID";
        log.debug("Calling method: {}", methodName);

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateGUID(guid, "guid", methodName);

        SoftwareServerCapabilityResponse response = callGetRESTCall(methodName, SoftwareServerCapabilityResponse.class,
                                                                    serverPlatformURLRoot + BASE_PATH + GET_SOFTWARE_SERVER, serverName, userId, guid);

        detectExceptions(methodName, response);

        return response.getServerCapability();
    }

    private void detectExceptions(String methodName, SecurityOfficerOMASAPIResponse response)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        restExceptionHandler.detectAndThrowInvalidParameterException(methodName, response);
        restExceptionHandler.detectAndThrowPropertyServerException(methodName, response);
        restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, response);
    }
}
