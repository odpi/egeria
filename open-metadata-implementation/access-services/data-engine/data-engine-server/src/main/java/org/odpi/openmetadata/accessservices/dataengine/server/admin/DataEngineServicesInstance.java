/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DataEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataEngineServicesInstance extends OCFOMASServiceInstance {
    private static final AccessServiceDescription description = AccessServiceDescription.DATA_ENGINE_OMAS;

    private ProcessHandler processHandler;
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;
    private PortHandler portHandler;
    private Connection inTopicConnection;

    /**
     * Set up the local repository connector that will service the REST Calls
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls
     * @param supportedZones      list of zones that DataEngine is allowed to serve Assets from
     * @param defaultZones        list of zones that DataEngine sets up in new Asset instances
     * @param auditLog            logging destination
     * @param localServerUserId   userId used for server initiated actions
     * @param maxPageSize         max number of results to return on single request
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    DataEngineServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones, List<String> defaultZones,
                               AuditLog auditLog, String localServerUserId, int maxPageSize, Connection inTopicConnection) throws NewInstanceException {


        super(description.getAccessServiceFullName(), repositoryConnector, supportedZones, defaultZones, auditLog,
                localServerUserId, maxPageSize);

        this.inTopicConnection = inTopicConnection;

        if (repositoryHandler != null) {
            dataEngineRegistrationHandler = new DataEngineRegistrationHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper);
            DataEngineCommonHandler dataEngineCommonHandler = new DataEngineCommonHandler(serviceName, serverName, invalidParameterHandler,
                    repositoryHandler,
                    repositoryHelper, dataEngineRegistrationHandler);
            processHandler = new ProcessHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, assetHandler,
                    dataEngineCommonHandler, defaultZones, supportedZones);
            dataEngineSchemaTypeHandler = new DataEngineSchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper, schemaTypeHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);
            portHandler = new PortHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    dataEngineCommonHandler);

            if (securityVerifier != null) {
                processHandler.setSecurityVerifier(securityVerifier);
            }

        } else {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(DataEngineErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName), this.getClass().getName(),
                    methodName);
        }
    }

    /**
     * Return the handler for process requests
     *
     * @return handler object
     */
    ProcessHandler getProcessHandler() {
        return processHandler;
    }

    /**
     * Return the handler for registration requests
     *
     * @return handler object
     */
    DataEngineRegistrationHandler getDataEngineRegistrationHandler() {
        return dataEngineRegistrationHandler;
    }

    /**
     * Return the handler for schema types requests
     *
     * @return handler object
     */
    DataEngineSchemaTypeHandler getDataEngineSchemaTypeHandler() {
        return dataEngineSchemaTypeHandler;
    }

    /**
     * Return the handler for port requests
     *
     * @return handler object
     */
    PortHandler getPortHandler() {
        return portHandler;
    }

    /**
     * Return the connection used in the client to create a connector that produces events on the input topic
     *
     * @return connection object for client
     */
    Connection getInTopicConnection() { return inTopicConnection; }
}
