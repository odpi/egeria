/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.handlers.LineageWarehouseHandler;


/**
 * OpenLineageServiceInstanceHandler retrieves information from the instance map for the
 * Open Lineage instances.  The instance map is thread-safe.  Instances are added
 * and removed by the OpenLineageOperationalServices class.
 */
class LineageWarehouseInstanceHandler extends OMAGServerServiceInstanceHandler {

    /**
     * Default constructor registers the governance server
     */
    LineageWarehouseInstanceHandler() {
        super(GovernanceServicesDescription.LINEAGE_WAREHOUSE_SERVICES.getServiceName());
    }


    /**
     * Retrieve the handler for the Open Lineage Services.
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public LineageWarehouseHandler getOpenLineageHandler(String userId,
                                                         String serverName,
                                                         String serviceOperationName) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, LineageWarehouseException
    {

        LineageWarehouseServerInstance instance = (LineageWarehouseServerInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null) {
            return instance.getOpenLineageHandler();
        }

        return null;
    }
}
