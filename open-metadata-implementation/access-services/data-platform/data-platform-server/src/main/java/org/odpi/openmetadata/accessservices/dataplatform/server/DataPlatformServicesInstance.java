/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.DeployedDatabaseSchemaAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.RegistrationHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * The type Data platform services instance.
 */
public class DataPlatformServicesInstance extends OCFOMASServiceInstance {

    private static AccessServiceDescription myDescription = AccessServiceDescription.DATA_PLATFORM_OMAS;

    private RegistrationHandler registrationHandler;
    private DeployedDatabaseSchemaAssetHandler deployedDatabaseSchemaAssetHandler;


    /**
     * Set up the handler for Data Platform OMAS server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones      list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param auditLog            logging destination
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataPlatformServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones, OMRSAuditLog auditLog) throws NewInstanceException {
        super(myDescription.getAccessServiceFullName(),
                repositoryConnector,
                supportedZones,
                null,
                auditLog);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            registrationHandler = new RegistrationHandler(
                    serverName,
                    repositoryHelper,
                    repositoryHandler,
                    invalidParameterHandler);
            deployedDatabaseSchemaAssetHandler = new DeployedDatabaseSchemaAssetHandler(
                    serviceName,
                    serverName,
                    repositoryHelper,
                    repositoryHandler,
                    invalidParameterHandler
            );
        }

        else
        {
            DataPlatformErrorCode errorCode    = DataPlatformErrorCode.OMRS_NOT_INITIALIZED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Gets my description.
     *
     * @return the my description
     */
    public static AccessServiceDescription getMyDescription() {
        return myDescription;
    }

    /**
     * Gets registration handler.
     *
     * @return the registration handler
     */
    public RegistrationHandler getRegistrationHandler() {
        return registrationHandler;
    }


    public DeployedDatabaseSchemaAssetHandler getDeployedDatabaseSchemaAssetHandler() {
        return deployedDatabaseSchemaAssetHandler;
    }

}
