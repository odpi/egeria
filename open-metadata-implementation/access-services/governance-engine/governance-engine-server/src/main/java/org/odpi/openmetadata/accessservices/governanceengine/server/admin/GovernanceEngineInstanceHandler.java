/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * GovernanceEngineInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceEngineAdmin class.
 */
public class GovernanceEngineInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public GovernanceEngineInstanceHandler() {
        super(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName() + " OMAS");

        GovernanceEngineRegistration.registerAccessService();
    }

    /**
     * Retrieve the governance assets handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public GovernedAssetHandler getGovernedAssetHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        GovernanceEngineServicesInstance instance = (GovernanceEngineServicesInstance) super
                .getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getGovernedAssetHandler();
    }

}
