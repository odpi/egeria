/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;


import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;



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
        super(ViewServiceDescription.GLOSSARY_BROWSER.getViewServiceFullName());

        GlossaryBrowserRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset manager API
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
        GlossaryBrowserInstance instance = (GlossaryBrowserInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGlossaryExchangeClient();
        }

        return null;
    }
}
