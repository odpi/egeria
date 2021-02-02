/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DataEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataEngineServicesInstance extends OMASServiceInstance {
    private static final AccessServiceDescription description = AccessServiceDescription.DATA_ENGINE_OMAS;

    private final DataEngineProcessHandler processHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;
    private final PortHandler portHandler;
    private final AssetHandler<Asset> assetHandler;
    private final SchemaTypeHandler<SchemaType> schemaTypeHandler;
    private final SchemaAttributeHandler<SchemaAttribute, SchemaType> schemaAttributeHandler;
    private final Connection inTopicConnection;

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


        super(description.getAccessServiceFullName(), repositoryConnector, supportedZones, defaultZones, null, auditLog,
                localServerUserId, maxPageSize);

        this.inTopicConnection = inTopicConnection;

        if (repositoryHandler != null) {

            assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName), Asset.class,
                    serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                    securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

            schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                    SchemaType.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

            schemaAttributeHandler = new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName, serverName),
                    SchemaAttribute.class, new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                    SchemaType.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

            dataEngineRegistrationHandler = new DataEngineRegistrationHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper);

            DataEngineCommonHandler dataEngineCommonHandler = new DataEngineCommonHandler(serviceName, serverName, invalidParameterHandler,
                    repositoryHandler, repositoryHelper, dataEngineRegistrationHandler);
            processHandler = new DataEngineProcessHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    assetHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);
            dataEngineSchemaTypeHandler = new DataEngineSchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper, schemaTypeHandler, schemaAttributeHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);
            portHandler = new PortHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    dataEngineCommonHandler);

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
    DataEngineProcessHandler getProcessHandler() {
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
