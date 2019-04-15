/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * GovernanceEngineInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceEngineAdmin class.
 */
class GovernanceEngineInstanceHandler {
    private static GovernanceEngineServicesInstanceMap instanceMap = new GovernanceEngineServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    GovernanceEngineInstanceHandler() {
        GovernanceEngineRegistration.registerAccessService();
    }

    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException {
        GovernanceEngineServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            final String methodName = "getRepositoryConnector";

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
}
