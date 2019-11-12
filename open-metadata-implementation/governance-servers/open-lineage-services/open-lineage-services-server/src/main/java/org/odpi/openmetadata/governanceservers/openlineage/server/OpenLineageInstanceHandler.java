/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;


/**
 * OpenLineageServiceInstanceHandler retrieves information from the instance map for the
 * Open Lineage instances.  The instance map is thread-safe.  Instances are added
 * and removed by the OpenLineageOperationalServices class.
 */
class OpenLineageInstanceHandler extends GovernanceServerServiceInstanceHandler {

    private static OpenLineageInstanceMap instanceMap = new OpenLineageInstanceMap();

    /**
     * Default constructor registers the governance server
     */
    OpenLineageInstanceHandler(){
        super(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName());
    }


    /**
     * Retrieve the specific handler for the Open Lineage Services.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param openLineageGUID unique identifier of the Open Lineage server
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException the service name is not known - indicating a logic error
     */
    public OpenLineageHandler queryHandler(String userId,
                                           String serverName,
                                           String openLineageGUID,
                                           String serviceOperationName) throws PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException, InvalidParameterException {

        OpenLineageInstance instance = (OpenLineageInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);


        if (instance != null)
        {
            return instance.getOpenLineage(openLineageGUID);
        }

        return null;
    }
}
