/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;


import org.odpi.openmetadata.accessservices.assetowner.converters.*;
import org.odpi.openmetadata.accessservices.assetowner.ffdc.AssetOwnerErrorCode;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetOwnerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetOwnerServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.ASSET_OWNER_OMAS;

    private AssetHandler<AssetElement>                                        assetHandler;
    private SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> schemaAttributeHandler;
    private SchemaTypeHandler<SchemaTypeElement>                              schemaTypeHandler;

    private ReferenceableHandler<ReferenceableElement> referenceableHandler;

    private FilesAndFoldersHandler<FileSystemElement,
                                   FolderElement,
                                   FileElement> filesAndFoldersHandler;

    private ValidValuesHandler<ValidValueElement,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean> validValuesHandler;

    private DataFieldHandler<DataField>                             dataFieldHandler;
    private AnnotationHandler<Annotation>                           annotationHandler;
    private DiscoveryAnalysisReportHandler<DiscoveryAnalysisReport> discoveryAnalysisReportHandler;

    private ConnectionHandler<OpenMetadataAPIDummyBean>    connectionHandler;
    private ConnectorTypeHandler<OpenMetadataAPIDummyBean> connectorTypeHandler;
    private EndpointHandler<OpenMetadataAPIDummyBean>      endpointHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that AssetOwner is allowed to serve Assets from.
     * @param defaultZones list of zones that AssetOwner sets up in new Asset instances.
     * @param publishZones list of zones that AssetOwner sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetOwnerServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                      List<String>            supportedZones,
                                      List<String>            defaultZones,
                                      List<String>            publishZones,
                                      AuditLog                auditLog,
                                      String                  localServerUserId,
                                      int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize);

        if (repositoryHandler == null)
        {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(AssetOwnerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }

        this.assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName),
                                               AssetElement.class,
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

        this.schemaAttributeHandler = new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName, serverName),
                                               SchemaAttributeElement.class,
                                               new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                               SchemaTypeElement.class,serviceName,
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

        this.schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
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

        this.referenceableHandler = new ReferenceableHandler<>(new ReferenceableConverter<>(repositoryHelper, serviceName, serverName),
                                                               ReferenceableElement.class,
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

        this.filesAndFoldersHandler = new FilesAndFoldersHandler<>(new FileSystemConverter<>(repositoryHelper, serviceName, serverName),
                                                                   FileSystemElement.class,
                                                                   new FileFolderConverter<>(repositoryHelper, serviceName, serverName),
                                                                   FolderElement.class,
                                                                   new DataFileConverter<>(repositoryHelper, serviceName, serverName),
                                                                   FileElement.class,
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
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
                                                           new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                           OpenMetadataAPIDummyBean.class,
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

        this.dataFieldHandler               = new DataFieldHandler<>(new DataFieldConverter<>(repositoryHelper, serviceName, serverName),
                                                                     DataField.class,
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
        this.annotationHandler              = new AnnotationHandler<>(new AnnotationConverter<>(repositoryHelper, serviceName, serverName),
                                                                      Annotation.class,
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
        this.discoveryAnalysisReportHandler = new DiscoveryAnalysisReportHandler<>(new DiscoveryAnalysisReportConverter<>(repositoryHelper,
                                                                                                                          serviceName,
                                                                                                                          serverName),
                                                                                   DiscoveryAnalysisReport.class,
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

        OpenMetadataAPIDummyBeanConverter<OpenMetadataAPIDummyBean> dummyConverter =
                new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName);

        this.connectionHandler = new ConnectionHandler<>(dummyConverter,
                                                         OpenMetadataAPIDummyBean.class,
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

        this.connectorTypeHandler = new ConnectorTypeHandler<>(dummyConverter,
                                                               OpenMetadataAPIDummyBean.class,
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

        this.endpointHandler = new EndpointHandler<>(dummyConverter,
                                                     OpenMetadataAPIDummyBean.class,
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
     * Return the handler for managing referenceable objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceableHandler<ReferenceableElement> getReferenceableHandler() throws PropertyServerException
    {
        final String methodName = "getReferenceableHandler";

        validateActiveRepository(methodName);

        return referenceableHandler;
    }


    /**
     * Return the handler for managing assets.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<AssetElement> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
    }


    /**
     * Return the handler for managing schema attributes.
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
     * Return the handler for managing schema types.
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
     * Return the handler for managing folders for files objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    FilesAndFoldersHandler<FileSystemElement, FolderElement, FileElement> getFilesAndFoldersHandler() throws PropertyServerException
    {
        final String methodName = "getFilesAndFoldersHandler";

        validateActiveRepository(methodName);

        return filesAndFoldersHandler;
    }


    /**
     * Return the handler for managing valid values.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ValidValuesHandler<ValidValueElement,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean,
            OpenMetadataAPIDummyBean> getValidValuesHandler() throws PropertyServerException
    {
        final String methodName = "getValidValuesHandler";

        validateActiveRepository(methodName);

        return validValuesHandler;
    }


    /**
     * Return the handler for managing discovery analysis report objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DiscoveryAnalysisReportHandler<DiscoveryAnalysisReport> getDiscoveryAnalysisReportHandler() throws PropertyServerException
    {
        final String methodName = "getDiscoveryAnalysisReportHandler";

        validateActiveRepository(methodName);

        return discoveryAnalysisReportHandler;
    }


    /**
     * Return the handler for managing annotation objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AnnotationHandler<Annotation> getAnnotationHandler() throws PropertyServerException
    {
        final String methodName = "getAnnotationHandler";

        validateActiveRepository(methodName);

        return annotationHandler;
    }


    /**
     * Return the handler for managing data field objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DataFieldHandler<DataField> getDataFieldHandler() throws PropertyServerException
    {
        final String methodName = "getDataFieldHandler";

        validateActiveRepository(methodName);

        return dataFieldHandler;
    }


    /**
     * Return the handler for managing connection objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectionHandler<OpenMetadataAPIDummyBean> getConnectionHandler() throws PropertyServerException
    {
        final String methodName = "getConnectionHandler";

        validateActiveRepository(methodName);

        return connectionHandler;
    }


    /**
     * Return the handler for managing connector type objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectorTypeHandler<OpenMetadataAPIDummyBean> getConnectorTypeHandler() throws PropertyServerException
    {
        final String methodName = "getConnectorTypeHandler";

        validateActiveRepository(methodName);

        return connectorTypeHandler;
    }


    /**
     * Return the handler for managing endpoint objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    EndpointHandler<OpenMetadataAPIDummyBean> getEndpointHandler() throws PropertyServerException
    {
        final String methodName = "getEndpointHandler";

        validateActiveRepository(methodName);

        return endpointHandler;
    }

}
