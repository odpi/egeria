/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.rest;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyException;
import org.odpi.openmetadata.governanceservers.dataengineproxy.processor.DataEngineProxyService;


/**
 * DataEngineProxyInstanceHandler retrieves information from the instance map for the
 *  * Data Engine Proxy. The instance map is thread-safe.  Instances are added
 *  * and removed by the DataEngineProxyOperationalServices class.
 */
class DataEngineProxyInstanceHandler extends OMAGServerServiceInstanceHandler {

    /**
     * Default constructor registers the governance server
     */
    DataEngineProxyInstanceHandler() {
        super(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName());
    }


    /**
     * Retrieve the handler for the Data Engine Proxy Services
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DataEngineProxyService getDataEngineProxyService(String userId,
                                                            String serverName,
                                                            String serviceOperationName)
            throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, DataEngineProxyException {

        DataEngineProxyServerInstance instance = (DataEngineProxyServerInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null) {
            return instance.getDataEngineProxyService();
        }

        return null;
    }
}
