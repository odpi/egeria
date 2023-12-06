/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;


import org.odpi.openmetadata.accessservices.assetmanager.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.CollaborationManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.StewardshipManagementClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.regex.Pattern;


/**
 * GlossaryBrowserInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GlossaryBrowserAdmin class.
 */
public class GlossaryBrowserInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public GlossaryBrowserInstanceHandler()
    {
        super(ViewServiceDescription.GLOSSARY_BROWSER.getViewServiceName());

        GlossaryBrowserRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public CollaborationManagementClient getCollaborationManagementClient(String userId,
                                                                          String serverName,
                                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        GlossaryBrowserInstance instance = (GlossaryBrowserInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCollaborationManagementClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public GlossaryManagementClient getGlossaryManagementClient(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        GlossaryBrowserInstance instance = (GlossaryBrowserInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGlossaryManagementClient();
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public StewardshipManagementClient getStewardshipManagementClient(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        GlossaryBrowserInstance instance = (GlossaryBrowserInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipManagementClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
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
        GlossaryBrowserInstance instance = (GlossaryBrowserInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient();
        }

        return null;
    }
}
