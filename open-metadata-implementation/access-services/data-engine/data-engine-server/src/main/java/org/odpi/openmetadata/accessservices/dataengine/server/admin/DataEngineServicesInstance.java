/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.admin;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.Endpoint;
import org.odpi.openmetadata.accessservices.dataengine.model.FileFolder;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.CollectionCoverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DataFileConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseColumnConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseSchemaConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.DatabaseTableConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.EndpointConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.FileFolderConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.PortConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ProcessConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PortHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
    private final DataEngineCollectionHandler dataEngineCollectionHandler;
    private final DataEnginePortHandler dataEnginePortHandler;
    private final DataEngineRelationalDataHandler dataEngineRelationalDataHandler;
    private final Connection inTopicConnection;
    private final DataEngineDataFileHandler dataEngineDataFileHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;

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
                               AuditLog auditLog, String localServerUserId, int maxPageSize, Connection inTopicConnection) throws
                                                                                                                           NewInstanceException {


        super(description.getAccessServiceFullName(), repositoryConnector, supportedZones, defaultZones, null, auditLog,
                localServerUserId, maxPageSize);

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

        final PortHandler<Port> portHandler = new PortHandler<>(new PortConverter<>(repositoryHelper, serviceName, serverName), Port.class,
                serviceName, serverName, invalidParameterHandler, repositoryHandler,repositoryHelper, localServerUserId, securityVerifier,
                supportedZones, defaultZones, publishZones, auditLog);

        dataEngineRegistrationHandler = new DataEngineRegistrationHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper);

        dataEngineCommonHandler = new DataEngineCommonHandler(serviceName, serverName, invalidParameterHandler,
                    repositoryHandler, repositoryHelper, dataEngineRegistrationHandler);

        final ReferenceableHandler<org.odpi.openmetadata.accessservices.dataengine.model.Connection> connectionHandler =
                new ReferenceableHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                        org.odpi.openmetadata.accessservices.dataengine.model.Connection.class, serviceName, serverName,
                        invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier,
                        supportedZones, defaultZones, publishZones, auditLog);
        final ReferenceableHandler<Endpoint> endpointHandler =
                new ReferenceableHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                        Endpoint.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                        localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
        final DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler =
                new DataEngineConnectionAndEndpointHandler(invalidParameterHandler, repositoryHelper, serviceName, serverName,
                        dataEngineCommonHandler, connectionHandler, endpointHandler);

        processHandler = new DataEngineProcessHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                assetHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);
        dataEngineSchemaTypeHandler = new DataEngineSchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                repositoryHelper, schemaTypeHandler, schemaAttributeHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);

        dataEngineCollectionHandler = new DataEngineCollectionHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHelper, collectionOpenMetadataAPIGenericHandler, dataEngineRegistrationHandler, dataEngineCommonHandler);

        dataEnginePortHandler = new DataEnginePortHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                dataEngineCommonHandler, portHandler, dataEngineRegistrationHandler);
        dataEngineRelationalDataHandler = new DataEngineRelationalDataHandler(serviceName, serverName, invalidParameterHandler,
                repositoryHandler, repositoryHelper, relationalDataHandler, dataEngineRegistrationHandler, dataEngineCommonHandler,
                dataEngineConnectionAndEndpointHandler);


        final AssetHandler<FileFolder> folderHandler = new AssetHandler<>(new FileFolderConverter<>(repositoryHelper, serviceName, serverName),
                FileFolder.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
        final DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler = new DataEngineFolderHierarchyHandler(invalidParameterHandler,
                repositoryHandler, dataEngineCommonHandler, folderHandler);
        final AssetHandler<DataFile> fileHandler = new AssetHandler<>(new DataFileConverter<>(repositoryHelper, serviceName, serverName),
                DataFile.class, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
        dataEngineDataFileHandler = new DataEngineDataFileHandler(invalidParameterHandler, repositoryHelper,
                dataEngineCommonHandler, fileHandler, dataEngineSchemaTypeHandler, dataEngineFolderHierarchyHandler,
                dataEngineConnectionAndEndpointHandler);
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
    DataEnginePortHandler getPortHandler() {
        return dataEnginePortHandler;
    }

    public DataEngineCollectionHandler getDataEngineCollecttionHandler() {
        return dataEngineCollectionHandler;
    }

    /**
     * Return the handler for database and relational table requests
     *
     * @return handler object
     */
    DataEngineRelationalDataHandler getDataEngineRelationalDataHandler() {
        return dataEngineRelationalDataHandler;
    }

    /**
     * Return the connection used in the client to create a connector that produces events on the input topic
     *
     * @return connection object for client
     */
    Connection getInTopicConnection() {
        return inTopicConnection;
    }

    /**
     * Return the handler for DataFile
     */
    public DataEngineDataFileHandler getDataEngineDataFileHandler() {
        return dataEngineDataFileHandler;
    }

    /**
     * Returns the common handler
     */
    public DataEngineCommonHandler getDataEngineCommonHandler() {
        return dataEngineCommonHandler;
    }
}
