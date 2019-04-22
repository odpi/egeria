/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationServerHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * DiscoveryEngineInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DiscoveryEngineAdmin class.
 */
class DiscoveryEngineInstanceHandler
{
    private static DiscoveryEngineServicesInstanceMap instanceMap = new DiscoveryEngineServicesInstanceMap();


    /**
     * Default constructor registers the access service
     */
    DiscoveryEngineInstanceHandler()
    {
        DiscoveryEngineRegistration.registerAccessService();
    }

    /**
     * Retrieve the repository connector for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    DiscoveryConfigurationServerHandler getDiscoveryConfigurationHandler(String serverName) throws PropertyServerException
    {
        DiscoveryEngineServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getDiscoveryConfigurationHandler();
        }
        else
        {
            final String methodName = "getDiscoveryConfigurationHandler";

            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.SERVICE_NOT_INITIALIZED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Retrieve the audit log for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSAuditLog getAuditLog(String serverName) throws PropertyServerException
    {
        DiscoveryEngineServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getAuditLog();
        }
        else
        {
            final String methodName = "getAuditLog";

            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.SERVICE_NOT_INITIALIZED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
