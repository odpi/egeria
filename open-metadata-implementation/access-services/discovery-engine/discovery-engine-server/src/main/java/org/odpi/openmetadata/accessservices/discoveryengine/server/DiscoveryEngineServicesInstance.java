/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;


import org.odpi.openmetadata.accessservices.discoveryengine.converters.AnnotationConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DataFieldConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryAnalysisReportConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.RelatedDataFieldConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.RelatedDataField;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DiscoveryEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class DiscoveryEngineServicesInstance extends OMASServiceInstance
{
    private static final AccessServiceDescription myDescription = AccessServiceDescription.DISCOVERY_ENGINE_OMAS;

    private final AssetHandler<OpenMetadataAPIDummyBean>                  assetHandler;
    private final AnnotationHandler<Annotation>                           annotationHandler;
    private final DataFieldHandler<DataField>                             dataFieldHandler;
    private final DataFieldHandler<RelatedDataField>                      relatedDataFieldHandler;
    private final DiscoveryAnalysisReportHandler<DiscoveryAnalysisReport> discoveryAnalysisReportHandler;

    private Connection                                              outTopicConnection;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     * @param publishZones list of zones that discovery engine can use to make a discovery service visible.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DiscoveryEngineServicesInstance(OMRSRepositoryConnector repositoryConnector,
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

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.assetHandler = new AssetHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
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
            this.annotationHandler = new AnnotationHandler<>(new AnnotationConverter<>(repositoryHelper, serviceName, serverName),
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
            this.dataFieldHandler = new DataFieldHandler<>(new DataFieldConverter<>(repositoryHelper, serviceName, serverName),
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
            this.relatedDataFieldHandler = new DataFieldHandler<>(new RelatedDataFieldConverter<>(repositoryHelper, serviceName, serverName),
                                                                  RelatedDataField.class,
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
            this.discoveryAnalysisReportHandler = new DiscoveryAnalysisReportHandler<>(
                    new DiscoveryAnalysisReportConverter<>(repositoryHelper, serviceName, serverName),
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
        }
        else
        {
            throw new NewInstanceException(DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    /**
     * Return the handler for managing assets.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<OpenMetadataAPIDummyBean> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
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
     * Return the handler for managing data field objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DataFieldHandler<RelatedDataField> getRelatedDataFieldHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedDataFieldHandler";

        validateActiveRepository(methodName);

        return relatedDataFieldHandler;
    }

}
