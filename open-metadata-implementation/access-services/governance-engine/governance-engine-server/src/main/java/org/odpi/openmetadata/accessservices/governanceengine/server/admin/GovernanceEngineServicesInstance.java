/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * GovernanceEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class GovernanceEngineServicesInstance extends OCFOMASServiceInstance {

    private GovernedAssetHandler governedAssetHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public GovernanceEngineServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                            List<String> supportedZones,
                                            AuditLog auditLog,
                                            String localServerUserId,
                                            int maxPageSize) throws NewInstanceException {

        super(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(), repositoryConnector, supportedZones,
                null, auditLog, localServerUserId, maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null) {
            governedAssetHandler = new GovernedAssetHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper, errorHandler, supportedZones);

            if (securityVerifier != null) {
                governedAssetHandler.setSecurityVerifier(securityVerifier);
            }

        } else {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(), this.getClass().getName(), methodName, errorMessage,
                    errorCode.getSystemAction(), errorCode.getUserAction());
        }
    }

    /**
     * Return the handler for governed asset requests
     *
     * @return handler object
     */
    public GovernedAssetHandler getGovernedAssetHandler() {
        return governedAssetHandler;
    }
}