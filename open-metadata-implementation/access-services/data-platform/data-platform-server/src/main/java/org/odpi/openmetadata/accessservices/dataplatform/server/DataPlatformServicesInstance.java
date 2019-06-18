/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.LoggingHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

public class DataPlatformServicesInstance extends OCFOMASServiceInstance {

    private static AccessServiceDescription myDescription = AccessServiceDescription.DATA_ENGINE_OMAS;

    private LoggingHandler loggingHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones      list of zones that DataPlatform OMAS is allowed to serve Assets from.
     * @param defaultZones        list of zones that DataPlatform OMAS should set in all new Assets.
     * @param auditLog            logging destination
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataPlatformServicesInstance(String serviceName,
                                        OMRSRepositoryConnector repositoryConnector,
                                        List<String> supportedZones,
                                        List<String> defaultZones,
                                        OMRSAuditLog auditLog,
                                        String serverName) throws NewInstanceException {
        super(myDescription.getAccessServiceName() + " OMAS",
                repositoryConnector,
                auditLog);

        this.serverName = serverName;

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)
        {
            loggingHandler = new LoggingHandler(auditLog);

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
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName         name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog            logging destination
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataPlatformServicesInstance(String serviceName, OMRSRepositoryConnector repositoryConnector, OMRSAuditLog auditLog, String serverName) throws NewInstanceException {
        super(serviceName, repositoryConnector, auditLog);
        this.serverName = serverName;
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        DataPlatformServicesInstanceMap.removeInstanceForJVM(serverName);
    }

    /**
     * Return the specialized handler for logging messages to the audit log.
     *
     * @return logging handler
     */
    LoggingHandler getLoggingHandler()
    {
        return loggingHandler;
    }

}
