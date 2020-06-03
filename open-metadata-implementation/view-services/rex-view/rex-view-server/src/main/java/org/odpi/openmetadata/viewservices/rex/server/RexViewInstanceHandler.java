/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.rex.handlers.RexViewHandler;

/**
 * RexViewInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the RexViewAdmin class.
 */
public class RexViewInstanceHandler extends OMVSServiceInstanceHandler
{



    /**
     * Default constructor registers the view service
     */
    public RexViewInstanceHandler() {

        super(ViewServiceDescription.REPOSITORY_EXPLORER.getViewServiceName());

        RexViewRegistration.registerViewService();
    }


    /**
     * The getRexHandler method retrieves the handler from the RexViewServicesInstance and returns it.
     * @param userId                the user performing the operation
     * @param serverName            the name of the server running the view-service
     * @param serviceOperationName  the operation to be performed
     * @return RexHandler
     * @throws InvalidParameterException  - the server name is not recognized
     * @throws UserNotAuthorizedException - the user id not permitted to perform the requested operation
     * @throws PropertyServerException    - the service name is not known - indicating a logic error
     */
    public RexViewHandler getRexViewHandler(String userId, String serverName, String serviceOperationName)
    throws
        InvalidParameterException,
        UserNotAuthorizedException,
        PropertyServerException
    {

        /*
         * Get the RexViewServicesInstance. This is an instance associated with the UI servername (tenant).
         */
        RexViewServicesInstance instance = (RexViewServicesInstance)super.getServerServiceInstance(userId,
                                                                                                   serverName,
                                                                                                   serviceOperationName);

        if (instance != null)
        {
            return instance.getRexViewHandler();
        }

        return null;
    }



}
