/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * AssetLineageInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetLineageAdmin class.
 */
public class AssetLineageInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public AssetLineageInstanceHandler() {
        super(AccessServiceDescription.ASSET_LINEAGE_OMAS.getAccessServiceFullName());
    }

    public void registerAccessService() {
        AssetLineageRegistration.registerAccessService();
    }

    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the calling operation
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    error in the requested server
     */
    public GlossaryHandler getGlossaryHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        AssetLineageServicesInstance instance = (AssetLineageServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        if (instance != null)
            return instance.getGlossaryHandler();
        return null;
    }

    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the calling operation
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    error in the requested server
     */
    public AssetContextHandler getAssetContextHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        AssetLineageServicesInstance instance = (AssetLineageServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        if (instance != null)
            return instance.getAssetContextHandler();
        return null;
    }

    public ProcessContextHandler getProcessHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        AssetLineageServicesInstance instance = (AssetLineageServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        if (instance != null)
            return instance.getProcessContextHandler();
        return null;
    }

    /**
     * Retrieve classification handler for the access service.
     *
     * @param userId               the user id
     * @param serverName           the server name
     * @param serviceOperationName the service operation name
     * @return the classification handler
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     */
    public ClassificationHandler getClassificationHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        AssetLineageServicesInstance instance = (AssetLineageServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);
        if (instance != null)
            return instance.getClassificationHandler();
        return null;
    }
}
