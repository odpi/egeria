/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.metadataexplorer.handlers.OpenMetadataHandler;


/**
 * MetadataExplorerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the MetadataExplorerAdmin class.
 */
public class MetadataExplorerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public MetadataExplorerInstanceHandler()
    {
        super(ViewServiceDescription.METADATA_EXPLORER.getViewServiceName());

        MetadataExplorerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * Governance Action Framework Services API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataHandler getOpenMetadataHandler(String userId,
                                                      String serverName,
                                                      String viewServiceURLMarker,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        MetadataExplorerInstance instance = (MetadataExplorerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }
}
