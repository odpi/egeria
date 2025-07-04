/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.GovernanceDefinitionHandler;


/**
 * GovernanceOfficerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceOfficerAdmin class.
 */
public class GovernanceOfficerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public GovernanceOfficerInstanceHandler()
    {
        super(ViewServiceDescription.GOVERNANCE_OFFICER.getViewServiceFullName());

        GovernanceOfficerRegistration.registerViewService();
    }



    /**
     * This method returns an Open Metadata Store client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public GovernanceDefinitionHandler getGovernanceDefinitionHandler(String userId,
                                                                      String serverName,
                                                                      String viewServiceURLMarker,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        GovernanceOfficerInstance instance = (GovernanceOfficerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDefinitionHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }

}
