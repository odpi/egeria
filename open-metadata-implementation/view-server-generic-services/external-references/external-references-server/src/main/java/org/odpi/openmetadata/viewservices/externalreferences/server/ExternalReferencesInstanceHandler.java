/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externalreferences.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;


/**
 * ExternalReferencesInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ExternalReferencesAdmin class.
 */
public class ExternalReferencesInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ExternalReferencesInstanceHandler()
    {
        super(ViewServiceDescription.EXTERNAL_REFERENCES.getViewServiceFullName());

        ExternalReferencesRegistration.registerViewService();
    }


    /**
     * This method returns an Open Metadata Store client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ExternalReferenceHandler getExternalReferenceHandler(String userId,
                                                                String serverName,
                                                                String urlMarker,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        ExternalReferencesInstance instance = (ExternalReferencesInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalReferenceHandler(urlMarker, serviceOperationName);
        }

        return null;
    }




    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param urlMarker  view service URL marker
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataClient getOpenMetadataStoreClient(String userId,
                                                         String serverName,
                                                         String urlMarker,
                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        ExternalReferencesInstance instance = (ExternalReferencesInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient(urlMarker, serviceOperationName);
        }

        return null;
    }

}
