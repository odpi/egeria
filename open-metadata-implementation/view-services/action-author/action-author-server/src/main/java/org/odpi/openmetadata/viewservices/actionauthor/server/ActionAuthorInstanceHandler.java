/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actionauthor.server;


import org.odpi.openmetadata.accessservices.governanceprogram.client.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;



/**
 * ActionAuthorInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ActionAuthorAdmin class.
 */
public class ActionAuthorInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ActionAuthorInstanceHandler()
    {
        super(ViewServiceDescription.ACTION_AUTHOR.getViewServiceName());

        ActionAuthorRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ExternalReferenceManager getExternalReferenceManager(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        ActionAuthorInstance instance = (ActionAuthorInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalReferenceManager();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ConnectedAssetClient getConnectedAssetClient(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        ActionAuthorInstance instance = (ActionAuthorInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectedAssetClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        ActionAuthorInstance instance = (ActionAuthorInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenGovernanceClient getOpenGovernanceClient(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        ActionAuthorInstance instance = (ActionAuthorInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenGovernanceClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient(String userId,
                                                                        String serverName,
                                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        ActionAuthorInstance instance = (ActionAuthorInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenIntegrationServiceClient();
        }

        return null;
    }
}
