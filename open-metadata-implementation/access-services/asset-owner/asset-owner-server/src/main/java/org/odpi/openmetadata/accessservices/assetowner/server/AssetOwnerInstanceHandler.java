/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.handlers.FileSystemHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ODFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * AssetConsumerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetConsumerAdmin class.
 */
class AssetOwnerInstanceHandler extends ODFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetOwnerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName());

        AssetOwnerRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    FileSystemHandler getFilesystemHandler(String userId,
                                           String serverName,
                                           String serviceOperationName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getFileSystemHandler();
        }

        return null;
    }
}
