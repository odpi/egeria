/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarymanager.server;


import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.CollaborationExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;


/**
 * GlossaryManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GlossaryManagerAdmin class.
 */
public class GlossaryManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public GlossaryManagerInstanceHandler()
    {
        super(ViewServiceDescription.GLOSSARY_MANAGER.getViewServiceName());

        GlossaryManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public CollaborationExchangeClient getCollaborationExchangeClient(String userId,
                                                                          String serverName,
                                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        GlossaryManagerInstance instance = (GlossaryManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCollaborationExchangeClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public GlossaryExchangeClient getGlossaryExchangeClient(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        GlossaryManagerInstance instance = (GlossaryManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGlossaryExchangeClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public StewardshipExchangeClient getStewardshipExchangeClient(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        GlossaryManagerInstance instance = (GlossaryManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipExchangeClient();
        }

        return null;
    }
}
