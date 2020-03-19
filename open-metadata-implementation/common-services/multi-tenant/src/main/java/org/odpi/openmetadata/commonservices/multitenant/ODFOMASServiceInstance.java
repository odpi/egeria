/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.AnnotationHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DataFieldHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DiscoveryAnalysisReportHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * ODFOMASServiceInstance provides an instance base class for Open Metadata Access Services (OMASs)
 * that are built around the Open Discovery Framework (ODF).
 */
public class ODFOMASServiceInstance extends OCFOMASServiceInstance
{
    protected AnnotationHandler              annotationHandler;
    protected DataFieldHandler               dataFieldHandler;
    protected DiscoveryAnalysisReportHandler discoveryAnalysisReportHandler;



    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param serviceName name of this service
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     * @param localServerUserId userId used for server initiated actions
     * @param auditLog logging destination
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public ODFOMASServiceInstance(String                  serviceName,
                                  OMRSRepositoryConnector repositoryConnector,
                                  List<String>            supportedZones,
                                  List<String>            defaultZones,
                                  AuditLog                auditLog,
                                  String                  localServerUserId,
                                  int                     maxPageSize) throws NewInstanceException
    {
        super(serviceName, repositoryConnector, supportedZones, defaultZones, auditLog, localServerUserId, maxPageSize);

        this.dataFieldHandler               = new DataFieldHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper);
        this.annotationHandler              = new AnnotationHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, dataFieldHandler);
        this.discoveryAnalysisReportHandler = new DiscoveryAnalysisReportHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, annotationHandler);
    }


    /**
     * Return the handler for managing discovery analysis report objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DiscoveryAnalysisReportHandler getDiscoveryAnalysisReportHandler() throws PropertyServerException
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
    AnnotationHandler getAnnotationHandler() throws PropertyServerException
    {
        final String methodName = "getAnnotationHandler";

        validateActiveRepository(methodName);

        return annotationHandler;
    }


    /**
     * Return the handler for managing annotation objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DataFieldHandler getDataFieldHandler() throws PropertyServerException
    {
        final String methodName = "getDataFieldHandler";

        validateActiveRepository(methodName);

        return dataFieldHandler;
    }
}
