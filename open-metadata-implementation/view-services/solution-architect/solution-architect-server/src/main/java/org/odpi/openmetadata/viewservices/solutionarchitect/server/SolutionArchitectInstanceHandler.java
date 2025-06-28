/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.SolutionHandler;


/**
 * SolutionArchitectInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SolutionArchitectAdmin class.
 */
public class SolutionArchitectInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public SolutionArchitectInstanceHandler()
    {
        super(ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceFullName());

        SolutionArchitectRegistration.registerViewService();
    }




    /**
     * This method returns a Digital Architecture OMAS client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public SolutionHandler getSolutionManagerClient(String userId,
                                                    String serverName,
                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        SolutionArchitectInstance instance = (SolutionArchitectInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSolutionManagerClient();
        }

        return null;
    }


    /**
     * This method returns a Digital Architecture OMAS client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ActorRoleHandler getSolutionRoleClient(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        SolutionArchitectInstance instance = (SolutionArchitectInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSolutionRoleClient();
        }

        return null;
    }
}
