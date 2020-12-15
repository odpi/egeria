/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.datamanager.converters.*;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerErrorCode;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DataManagerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataManagerServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DATA_MANAGER_OMAS;

    private SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> dataManagerIntegratorHandler;
    private RelationalDataHandler<DatabaseElement,
                                  DatabaseSchemaElement,
                                  DatabaseTableElement,
                                  DatabaseViewElement,
                                  DatabaseColumnElement,
                                  SchemaTypeElement>                         relationalDataHandler;
    private FilesAndFoldersHandler<FileSystemElement,
                                   FileFolderElement,
                                   DataFileElement>                          filesAndFoldersHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DataManager is allowed to serve Assets from.
     * @param defaultZones list of zones that DataManager sets up in new Asset instances.
     * @param publishZones list of zones that DataManager sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicEventBusConnection inner event bus connection to use to build topic connection to send to client if they which
     *                                   to listen on the out topic.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DataManagerServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String>           supportedZones,
                                        List<String>           defaultZones,
                                        List<String>           publishZones,
                                        AuditLog               auditLog,
                                        String                 localServerUserId,
                                        int                    maxPageSize,
                                        Connection             outTopicEventBusConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              DataManagerOutTopicClientProvider.class.getName(),
              outTopicEventBusConnection);

        if (repositoryHandler == null)
        {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(DataManagerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }


        this.dataManagerIntegratorHandler = new SoftwareServerCapabilityHandler<>(new DatabaseManagerConverter<>(repositoryHelper, serviceName,serverName),
                                                                                  SoftwareServerCapabilityElement.class,
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


        this.relationalDataHandler = new RelationalDataHandler<>(new DatabaseConverter<>(repositoryHelper, serviceName,serverName),
                                                                 DatabaseElement.class,
                                                                 new DatabaseSchemaConverter<>(repositoryHelper, serviceName,serverName),
                                                                 DatabaseSchemaElement.class,
                                                                 new DatabaseTableConverter<>(repositoryHelper, serviceName,serverName),
                                                                 DatabaseTableElement.class,
                                                                 new DatabaseViewConverter<>(repositoryHelper, serviceName,serverName),
                                                                 DatabaseViewElement.class,
                                                                 new DatabaseColumnConverter<>(repositoryHelper, serviceName, serverName),
                                                                 DatabaseColumnElement.class,
                                                                 new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                 SchemaTypeElement.class,
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

        this.filesAndFoldersHandler = new FilesAndFoldersHandler<>(new FileSystemConverter<>(repositoryHelper, serviceName,serverName),
                                                                   FileSystemElement.class,
                                                                   new FileFolderConverter<>(repositoryHelper, serviceName,serverName),
                                                                   FileFolderElement.class,
                                                                   new DataFileConverter<>(repositoryHelper, serviceName, serverName),
                                                                   DataFileElement.class,
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
    }


    /**
     * Return the handler for managing database  objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RelationalDataHandler<DatabaseElement,
                          DatabaseSchemaElement,
                          DatabaseTableElement,
                          DatabaseViewElement,
                          DatabaseColumnElement,
                          SchemaTypeElement> getRelationalDataHandler() throws PropertyServerException
    {
        final String methodName = "getRelationalDataHandler";

        validateActiveRepository(methodName);

        return relationalDataHandler;
    }


    /**
     * Return the handler for managing software server capability objects representing the integrator.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> getDataManagerIntegratorHandler() throws PropertyServerException
    {
        final String methodName = "getDataManagerIntegratorHandler";

        validateActiveRepository(methodName);

        return dataManagerIntegratorHandler;
    }


    /**
     * Return the handler for managing folders for files objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    FilesAndFoldersHandler<FileSystemElement, FileFolderElement, DataFileElement> getFilesAndFoldersHandler() throws PropertyServerException
    {
        final String methodName = "getFilesAndFoldersHandler";

        validateActiveRepository(methodName);

        return filesAndFoldersHandler;
    }
}
