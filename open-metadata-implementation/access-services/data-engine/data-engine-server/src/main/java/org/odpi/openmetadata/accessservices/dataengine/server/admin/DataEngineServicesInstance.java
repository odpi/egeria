/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import lombok.AccessLevel;
import lombok.Getter;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.model.ConnectorType;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.FileFolder;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.CollectionCoverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ConnectorTypeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DataFileConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseColumnConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseSchemaConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseTableConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.EndpointConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.EventTypeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.FileFolderConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.PortConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ProcessConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.TopicConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineEventTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFindHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaAttributeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PortHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DataEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataEngineServicesInstance extends OMASServiceInstance {

    private static final AccessServiceDescription description = AccessServiceDescription.DATA_ENGINE_OMAS;

    /**
     * -- GETTER --
     * Returns the Data Engine process handler.
     * @return the Data Engine process handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineProcessHandler processHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine registration handler.
     * @return the Data Engine registration handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine schema type handler.
     * @return the Data Engine schema type handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine collection handler.
     * @return the Data Engine collection handler
     */
    @Getter
    private final DataEngineCollectionHandler dataEngineCollectionHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine port handler.
     * @return the Data Engine port handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEnginePortHandler dataEnginePortHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine relational data handler.
     * @return the Data Engine relational data handler
     */
    @Getter
    private final DataEngineRelationalDataHandler dataEngineRelationalDataHandler;

    /**
     * -- GETTER --
     * Returns the connection used in the client to create a connector that produces events on the input topic.
     * @return connection object for client
     */
    @Getter
    private final Connection inTopicConnection;

    /**
     * -- GETTER --
     * Returns the Data Engine file handler.
     * @return the Data Engine file handler
     */
    @Getter
    private final DataEngineDataFileHandler dataEngineDataFileHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine common handler.
     * @return the Data Engine common handler
     */
    @Getter
    private final DataEngineCommonHandler dataEngineCommonHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine folder hierarchy handler.
     * @return the Data Engine folder hierarchy handler
     */
    @Getter
    private final DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine connection and endpoint handler.
     * @return the Data Engine connection and endpoint handler
     */
    @Getter
    private final DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine find handler.
     * @return the Data Engine find handler
     */
    @Getter
    private final DataEngineFindHandler dataEngineFindHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine topic handler.
     * @return the Data Engine topic handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineTopicHandler dataEngineTopicHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine event type handler.
     * @return the Data Engine event type handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineEventTypeHandler dataEngineEventTypeHandler;

    /**
     * -- GETTER --
     * Returns the Data Engine schema attribute handler.
     * @return the Data Engine schema attribute handler
     */
    @Getter(AccessLevel.PACKAGE)
    private final DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    /**
     * -- GETTER --
     * Returns a custom executor service to be used separate from the commonPool
     * @return a custom executor service
     */
    @Getter(AccessLevel.PACKAGE)
    private ExecutorService executorService;
    /**
     * Sets up the local repository connector that will service the REST Calls
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls
     * @param supportedZones      list of zones that DataEngine is allowed to serve Assets from
     * @param defaultZones        list of zones that DataEngine sets up in new Asset instances
     * @param threadPoolSize
     * @param auditLog            logging destination
     * @param localServerUserId   userId used for server initiated actions
     * @param maxPageSize         max number of results to return on single request
     * @throws NewInstanceException a problem occurred during initialization
     */
    DataEngineServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones, List<String> defaultZones,
                               Integer threadPoolSize, AuditLog auditLog, String localServerUserId, int maxPageSize, Connection inTopicConnection) throws
                                                                                                                           NewInstanceException {


        super(description.getAccessServiceFullName(), repositoryConnector, supportedZones, defaultZones, null, auditLog,
                localServerUserId, maxPageSize);

        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.inTopicConnection = inTopicConnection;

        if (repositoryHandler == null) {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(DataEngineErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName), this.getClass().getName(),
                    methodName);
        }

        final AssetHandler<Process> assetHandler = new AssetHandler<>(new ProcessConverter<>(repositoryHelper, serviceName, serverName),
                Process.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final OpenMetadataAPIGenericHandler<Collection> collectionOpenMetadataAPIGenericHandler =
                new OpenMetadataAPIGenericHandler<>(new CollectionCoverter<>(repositoryHelper, serviceName, serverName), Collection.class,
                        serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                        securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final SchemaTypeHandler<SchemaType> schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName,
                serverName), SchemaType.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler =
                new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName, serverName),
                        Attribute.class, new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                        SchemaType.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                        localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn, SchemaType> relationalDataHandler =
                new RelationalDataHandler<>(new DatabaseConverter<>(repositoryHelper, serviceName, serverName),
                        Database.class,
                        new DatabaseSchemaConverter<>(repositoryHelper, serviceName, serverName),
                        DatabaseSchema.class,
                        new DatabaseTableConverter<>(repositoryHelper, serviceName, serverName),
                        RelationalTable.class,
                        new DatabaseTableConverter<>(repositoryHelper, serviceName, serverName),
                        RelationalTable.class,
                        new DatabaseColumnConverter<>(repositoryHelper, serviceName, serverName),
                        RelationalColumn.class,
                        new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                        SchemaType.class,
                        serviceName,
                        serverName,
                        invalidParameterHandler,
                        repositoryHandler,
                        repositoryHelper,
                        localServerUserId,
                        securityVerifier,
                        supportedZones,
                        defaultZones,
                        publishZones,
                        auditLog);

        AssetHandler<DatabaseSchema> databaseSchemaAssetHandler = new AssetHandler<>(new DatabaseSchemaConverter<>(repositoryHelper, serviceName,serverName),
                DatabaseSchema.class,
                serviceName,
                serverName,
                invalidParameterHandler,
                repositoryHandler,
                repositoryHelper,
                localServerUserId,
                securityVerifier,
                supportedZones,
                defaultZones,
                publishZones,
                auditLog);

        final PortHandler<Port> portHandler = new PortHandler<>(new PortConverter<>(repositoryHelper, serviceName, serverName), Port.class,
                serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier,
                supportedZones, defaultZones, publishZones, auditLog);

        final SoftwareServerCapabilityHandler<SoftwareServerCapability> softwareServerCapabilityHandler =
                new SoftwareServerCapabilityHandler<>(new DatabaseTableConverter<>(repositoryHelper, serviceName, serverName),
                        SoftwareServerCapability.class, serviceName, serverName, invalidParameterHandler, repositoryHandler,
                        repositoryHelper, localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final AssetHandler<Topic> topicHandler = new AssetHandler<>(new TopicConverter<>(repositoryHelper, serviceName, serverName),
                Topic.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        final EventTypeHandler<EventType> eventTypeHandler = new EventTypeHandler<>(new EventTypeConverter<>(repositoryHelper, serviceName, serverName),
                EventType.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        dataEngineRegistrationHandler = new DataEngineRegistrationHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                repositoryHelper, softwareServerCapabilityHandler);

        dataEngineCommonHandler = new DataEngineCommonHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHandler, repositoryHelper, dataEngineRegistrationHandler);

        final ConnectionHandler<org.odpi.openmetadata.accessservices.dataengine.model.Connection> connectionHandler =
                new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                        org.odpi.openmetadata.accessservices.dataengine.model.Connection.class, serviceName, serverName,
                        invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier,
                        supportedZones, defaultZones, publishZones, auditLog);
        final EndpointHandler<Endpoint> endpointHandler =
                new EndpointHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                        Endpoint.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                        localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        ConnectorTypeHandler<ConnectorType> connectorTypeHandler = new ConnectorTypeHandler<>(
                new ConnectorTypeConverter<>(repositoryHelper, serviceName, serverName), ConnectorType.class,
                serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                securityVerifier, supportedZones, defaultZones, publishZones, auditLog);

        dataEngineConnectionAndEndpointHandler =
                new DataEngineConnectionAndEndpointHandler(invalidParameterHandler, repositoryHelper, serviceName, serverName,
                        dataEngineCommonHandler, connectionHandler, endpointHandler, connectorTypeHandler);

        processHandler = new DataEngineProcessHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper,
                assetHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);

        dataEngineSchemaAttributeHandler = new DataEngineSchemaAttributeHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHelper, schemaAttributeHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);
        dataEngineSchemaTypeHandler = new DataEngineSchemaTypeHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHelper, schemaTypeHandler, dataEngineRegistrationHandler, dataEngineCommonHandler, dataEngineSchemaAttributeHandler);

        dataEngineCollectionHandler = new DataEngineCollectionHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHelper, collectionOpenMetadataAPIGenericHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);

        dataEnginePortHandler = new DataEnginePortHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                dataEngineCommonHandler, portHandler, dataEngineRegistrationHandler);
        dataEngineRelationalDataHandler = new DataEngineRelationalDataHandler(serviceName, serverName, invalidParameterHandler,
                relationalDataHandler, databaseSchemaAssetHandler, dataEngineRegistrationHandler, dataEngineCommonHandler, dataEngineConnectionAndEndpointHandler);

        final AssetHandler<FileFolder> folderHandler = new AssetHandler<>(new FileFolderConverter<>(repositoryHelper, serviceName, serverName),
                FileFolder.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
        dataEngineFolderHierarchyHandler = new DataEngineFolderHierarchyHandler(invalidParameterHandler, repositoryHandler, dataEngineCommonHandler,
                folderHandler);
        final AssetHandler<DataFile> fileHandler = new AssetHandler<>(new DataFileConverter<>(repositoryHelper, serviceName, serverName),
                DataFile.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
        dataEngineDataFileHandler = new DataEngineDataFileHandler(invalidParameterHandler, repositoryHelper,
                repositoryHandler, dataEngineCommonHandler, fileHandler, dataEngineSchemaTypeHandler, dataEngineFolderHierarchyHandler,
                dataEngineConnectionAndEndpointHandler);

        dataEngineFindHandler = new DataEngineFindHandler(invalidParameterHandler, repositoryHelper, repositoryHandler,
                serviceName, serverName, dataEngineCommonHandler );

        dataEngineTopicHandler = new DataEngineTopicHandler(invalidParameterHandler, topicHandler,
                dataEngineRegistrationHandler, dataEngineCommonHandler);
        dataEngineEventTypeHandler = new DataEngineEventTypeHandler(invalidParameterHandler, eventTypeHandler, dataEngineRegistrationHandler,
                dataEngineCommonHandler, dataEngineSchemaAttributeHandler);
    }
}
