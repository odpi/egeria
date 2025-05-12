/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DataManagerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DataManagerServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.DATA_MANAGER_OMAS;

    private final ElementStubConverter<ElementStub>                    elementStubConverter;
    private final OpenMetadataAPIGenericHandler<ElementStub>           genericHandler;
    private final SoftwareCapabilityHandler<SoftwareCapabilityElement> dataManagerIntegratorHandler;
    private final RelationalDataHandler<DatabaseElement,
                                        DatabaseSchemaElement,
                                        DatabaseTableElement,
                                        DatabaseViewElement,
                                        DatabaseColumnElement,
                                        SchemaTypeElement>            relationalDataHandler;
    private final FilesAndFoldersHandler<FileSystemElement,
                                         FileFolderElement,
                                         DataFileElement>              filesAndFoldersHandler;

    private final AssetHandler<TopicElement>                                       topicHandler;
    private final EventTypeHandler<EventTypeElement>                               eventTypeHandler;

    private final AssetHandler<APIElement>                                         apiHandler;
    private final APIOperationHandler<APIOperationElement>                         apiOperationHandler;
    private final APIParameterListHandler<APIParameterListElement>                 apiParameterListHandler;

    private final AssetHandler<FormElement>                                        formHandler;
    private final AssetHandler<ReportElement>                                      reportHandler;
    private final AssetHandler<QueryElement>                                       queryHandler;
    private final DisplayDataContainerHandler<DataContainerElement,
                                              SchemaTypeElement>                   dataContainerHandler;


    private final SchemaTypeHandler<SchemaTypeElement>                             schemaTypeHandler;
    private final SchemaAttributeHandler<SchemaAttributeElement,
                                         SchemaTypeElement>                        schemaAttributeHandler;

    private final ConnectionHandler<ConnectionElement>       connectionHandler;
    private final ConnectorTypeHandler<ConnectorTypeElement> connectorTypeHandler;
    private final EndpointHandler<EndpointElement>           endpointHandler;

    private final ValidValuesHandler<ValidValueSetElement> validValuesSetHandler;
    private final ReferenceableHandler<RelatedElementStub>          relatedElementHandler;
    private final ValidValuesHandler<ValidValueElement>    validValuesHandler;


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

        this.elementStubConverter = new ElementStubConverter<>(repositoryHelper, serviceName, serverName);

        this.genericHandler = new OpenMetadataAPIGenericHandler<>(new DataManagerOMASConverter<>(repositoryHelper, serviceName,serverName),
                                                                  ElementStub.class,
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

        this.dataManagerIntegratorHandler = new SoftwareCapabilityHandler<>(new DatabaseManagerConverter<>(repositoryHelper, serviceName,serverName),
                                                                            SoftwareCapabilityElement.class,
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

        this.topicHandler = new AssetHandler<>(new TopicConverter<>(repositoryHelper, serviceName,serverName),
                                               TopicElement.class,
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

        this.eventTypeHandler = new EventTypeHandler<>(new EventTypeConverter<>(repositoryHelper, serviceName,serverName),
                                                       EventTypeElement.class,
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

        this.apiHandler = new AssetHandler<>(new APIConverter<>(repositoryHelper, serviceName,serverName),
                                             APIElement.class,
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


        this.apiOperationHandler = new APIOperationHandler<>(new APIOperationConverter<>(repositoryHelper, serviceName,serverName),
                                                             APIOperationElement.class,
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

        this.apiParameterListHandler = new APIParameterListHandler<>(new APIParameterListConverter<>(repositoryHelper, serviceName,serverName),
                                                                     APIParameterListElement.class,
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

        this.formHandler = new AssetHandler<>(new FormConverter<>(repositoryHelper, serviceName,serverName),
                                              FormElement.class,
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

        this.reportHandler = new AssetHandler<>(new ReportConverter<>(repositoryHelper, serviceName,serverName),
                                                ReportElement.class,
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

        this.queryHandler = new AssetHandler<>(new QueryConverter<>(repositoryHelper, serviceName,serverName),
                                               QueryElement.class,
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

        this.dataContainerHandler = new DisplayDataContainerHandler<>(new DataContainerConverter<>(repositoryHelper, serviceName,serverName),
                                                                      DataContainerElement.class,
                                                                      new SchemaTypeConverter<>(repositoryHelper, serviceName,serverName),
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

        this.schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName,serverName),
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

        this.schemaAttributeHandler = new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName,serverName),
                                                                   SchemaAttributeElement.class,
                                                                   new SchemaTypeConverter<>(repositoryHelper, serviceName,serverName),
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

        this.connectionHandler = new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                         ConnectionElement.class,
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

        this.connectorTypeHandler = new ConnectorTypeHandler<>(new ConnectorTypeConverter<>(repositoryHelper, serviceName,serverName),
                                                               ConnectorTypeElement.class,
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

        this.endpointHandler = new EndpointHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                                                     EndpointElement.class,
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

        this.validValuesSetHandler = new ValidValuesHandler<>(new ValidValueSetConverter<>(repositoryHelper, serviceName, serverName),
                                                              ValidValueSetElement.class,
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

        this.validValuesHandler = new ValidValuesHandler<>(new ValidValueConverter<>(repositoryHelper, serviceName, serverName),
                                                           ValidValueElement.class,
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

        this.relatedElementHandler = new ReferenceableHandler<>(new RelatedElementConverter<>(repositoryHelper, serviceName,serverName),
                                                                RelatedElementStub.class,
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
     * Return the element stub converter
     *
     * @return converter
     */
    ElementStubConverter<ElementStub> getElementStubConverter()
    {
        return elementStubConverter;
    }



    /**
     * Return the handler for managing generic objects.
     *
     * @return  handler object
     */
    public OpenMetadataAPIGenericHandler<ElementStub> getGenericHandler()
    {
        return genericHandler;
    }



    /**
     * Return the handler for managing database objects.
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
    SoftwareCapabilityHandler<SoftwareCapabilityElement> getDataManagerIntegratorHandler() throws PropertyServerException
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


    /**
     * Return the handler for managing TopicElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<TopicElement> getTopicHandler() throws PropertyServerException
    {
        final String methodName = "getTopicHandler";

        validateActiveRepository(methodName);

        return topicHandler;
    }


    /**
     * Return the handler for managing EventTypeElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    EventTypeHandler<EventTypeElement> getEventTypeHandler() throws PropertyServerException
    {
        final String methodName = "getEventTypeHandler";

        validateActiveRepository(methodName);

        return eventTypeHandler;
    }


    /**
     * Return the handler for managing APIElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<APIElement> getAPIHandler() throws PropertyServerException
    {
        final String methodName = "getAPIHandler";

        validateActiveRepository(methodName);

        return apiHandler;
    }


    /**
     * Return the handler for managing APIOperationElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    APIOperationHandler<APIOperationElement> getAPIOperationHandler() throws PropertyServerException
    {
        final String methodName = "getAPIOperationHandler";

        validateActiveRepository(methodName);

        return apiOperationHandler;
    }


    /**
     * Return the handler for managing APIParameterListElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    APIParameterListHandler<APIParameterListElement> getAPIParameterListHandler() throws PropertyServerException
    {
        final String methodName = "getAPIParameterListHandler";

        validateActiveRepository(methodName);

        return apiParameterListHandler;
    }


    /**
     * Return the handler for managing FormElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<FormElement> getFormHandler() throws PropertyServerException
    {
        final String methodName = "getFormHandler";

        validateActiveRepository(methodName);

        return formHandler;
    }


    /**
     * Return the handler for managing ReportElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<ReportElement> getReportHandler() throws PropertyServerException
    {
        final String methodName = "getReportHandler";

        validateActiveRepository(methodName);

        return reportHandler;
    }


    /**
     * Return the handler for managing QueryElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<QueryElement> getQueryHandler() throws PropertyServerException
    {
        final String methodName = "getQueryHandler";

        validateActiveRepository(methodName);

        return queryHandler;
    }


    /**
     * Return the handler for managing DataContainerElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> getDisplayDataContainerHandler() throws PropertyServerException
    {
        final String methodName = "getDisplayDataContainerHandler";

        validateActiveRepository(methodName);

        return dataContainerHandler;
    }


    /**
     * Return the handler for managing SchemaTypeElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SchemaTypeHandler<SchemaTypeElement> getSchemaTypeHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaTypeHandler";

        validateActiveRepository(methodName);

        return schemaTypeHandler;
    }


    /**
     * Return the handler for managing SchemaAttributeElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> getSchemaAttributeHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaAttributeHandler";

        validateActiveRepository(methodName);

        return schemaAttributeHandler;
    }


    /**
     * Return the handler for managing ConnectionElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectionHandler<ConnectionElement> getConnectionHandler() throws PropertyServerException
    {
        final String methodName = "getConnectionHandler";

        validateActiveRepository(methodName);

        return connectionHandler;
    }


    /**
     * Return the handler for managing ConnectorTypeElement objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectorTypeHandler<ConnectorTypeElement> getConnectorTypeHandler() throws PropertyServerException
    {
        final String methodName = "getConnectorTypeHandler";

        validateActiveRepository(methodName);

        return connectorTypeHandler;
    }


    /**
     * Return the handler for managing Endpoint objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    EndpointHandler<EndpointElement> getEndpointHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaTypeHandler";

        validateActiveRepository(methodName);

        return endpointHandler;
    }


    /**
     * Return the handler for managing valid values - used by schema.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ValidValuesHandler<ValidValueSetElement> getValidValuesSetHandler() throws PropertyServerException
    {
        final String methodName = "getValidValuesSetHandler";

        validateActiveRepository(methodName);

        return validValuesSetHandler;
    }


    /**
     * Return the handler for managing valid values.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ValidValuesHandler<ValidValueElement> getValidValuesHandler() throws PropertyServerException
    {
        final String methodName = "getValidValuesHandler";

        validateActiveRepository(methodName);

        return validValuesHandler;
    }



    /**
     * Return the handler for managing valid values.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceableHandler<RelatedElementStub> getRelatedElementHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedElementHandler";

        validateActiveRepository(methodName);

        return relatedElementHandler;
    }
}
