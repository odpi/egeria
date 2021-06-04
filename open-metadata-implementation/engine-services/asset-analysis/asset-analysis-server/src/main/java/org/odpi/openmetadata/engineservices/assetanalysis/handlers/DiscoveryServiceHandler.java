/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.engineservices.assetanalysis.ffdc.AssetAnalysisErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.assetanalysis.ffdc.AssetAnalysisAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryContext;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnalysisReportStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Date;

/**
 * DiscoveryServiceHandler provides the support to run a discovery service.  A new instance is created for each request and it is assigned its
 * own thread.
 */
public class DiscoveryServiceHandler extends GovernanceServiceHandler
{
    private DiscoveryService discoveryService;
    private DiscoveryContext discoveryContext;
    private String           discoveryReportGUID;


    /**
     * Constructor sets up the key parameters for running the discovery service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param discoveryEngineProperties properties of the discovery engine - used for message logging
     * @param discoveryEngineGUID unique Identifier of the discovery engine - used for message logging
     * @param engineHostUserId userId for making updates to the governance actions
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param governanceActionClient client for processing governance actions
     * @param requestType requestType - used for message logging
     * @param discoveryServiceGUID name of this discovery service - used for message logging
     * @param discoveryServiceName name of this discovery service - used for message logging
     * @param discoveryServiceConnector connector that does the work
     * @param discoveryContext context for the connector
     * @param discoveryReportGUID unique identifier of the report for this discovery request
     * @param auditLog destination for log messages
     */
    DiscoveryServiceHandler(GovernanceEngineProperties discoveryEngineProperties,
                            String                     discoveryEngineGUID,
                            String                     engineHostUserId,
                            String                     governanceActionGUID,
                            GovernanceEngineClient     governanceActionClient,
                            String                     requestType,
                            String                     discoveryServiceGUID,
                            String                     discoveryServiceName,
                            Connector                  discoveryServiceConnector,
                            DiscoveryContext           discoveryContext,
                            String                     discoveryReportGUID,
                            AuditLog                   auditLog) throws InvalidParameterException
    {
        super(discoveryEngineProperties,
              discoveryEngineGUID,
              engineHostUserId,
              governanceActionGUID,
              governanceActionClient,
              requestType,
              discoveryServiceGUID,
              discoveryServiceName,
              discoveryServiceConnector,
              auditLog);


        this.requestType          = requestType;
        this.discoveryContext     = discoveryContext;
        this.discoveryReportGUID  = discoveryReportGUID;
        this.auditLog             = auditLog;

        try
        {
            this.discoveryService = (DiscoveryService) discoveryServiceConnector;
        }
        catch (Exception error)
        {
            final String discoveryServiceConnectorParameterName = "discoveryServiceConnector";
            final String actionDescription = "Cast connector to DiscoveryService";

            auditLog.logException(actionDescription,
                                  AssetAnalysisAuditCode.INVALID_DISCOVERY_SERVICE.getMessageDefinition(discoveryServiceName,
                                                                                                        requestType,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                  error);
            throw new InvalidParameterException(AssetAnalysisErrorCode.INVALID_DISCOVERY_SERVICE.getMessageDefinition(discoveryServiceName,
                                                                                                                      requestType,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                discoveryServiceConnectorParameterName);
        }
    }


    /**
     * Return the unique identifier of the discovery analysis report for this discovery request.
     *
     * @return string guid
     */
    String getDiscoveryReportGUID()
    {
        return discoveryReportGUID;
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        Date startTime;
        Date endTime;

        final String actionDescription = "Analyse an Asset";

        String discoveryReportGUID = null;

        try
        {
            DiscoveryAnalysisReportStore discoveryReport = discoveryContext.getAnnotationStore().getDiscoveryReport();

            discoveryReportGUID = discoveryReport.getDiscoveryReportGUID();

            auditLog.logMessage(actionDescription,
                                AssetAnalysisAuditCode.DISCOVERY_SERVICE_STARTING.getMessageDefinition(governanceServiceName,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       requestType,
                                                                                                       governanceEngineProperties.getQualifiedName(),
                                                                                                       governanceEngineGUID,
                                                                                                       discoveryReport.getDiscoveryReportGUID()));


            discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.IN_PROGRESS);

            discoveryService.setDiscoveryContext(discoveryContext);
            discoveryService.setDiscoveryServiceName(governanceServiceName);

            startTime = new Date();
            discoveryService.start();
            endTime = new Date();

            auditLog.logMessage(actionDescription,
                                AssetAnalysisAuditCode.DISCOVERY_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       requestType,
                                                                                                       Long.toString(endTime.getTime() - startTime.getTime()),
                                                                                                       discoveryReport.getDiscoveryReportGUID()));

            discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.COMPLETED);
            super.disconnect();
            discoveryService.setDiscoveryContext(null);

            super.recordCompletionStatus(CompletionStatus.ACTIONED, null, null, null);
        }
        catch (Throwable  error)
        {
            auditLog.logException(actionDescription,
                                  AssetAnalysisAuditCode.DISCOVERY_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                       error.getClass().getName(),
                                                                                                       discoveryReportGUID,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       requestType,
                                                                                                       governanceEngineProperties.getQualifiedName(),
                                                                                                       governanceEngineGUID,
                                                                                                       error.getMessage()),
                                  error.toString(),
                                  error);

            try
            {
                DiscoveryAnalysisReportStore discoveryReport = discoveryContext.getAnnotationStore().getDiscoveryReport();
                discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.FAILED);
            }
            catch (Throwable statusError)
            {
                auditLog.logException(actionDescription,
                                      AssetAnalysisAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(governanceEngineProperties.getQualifiedName(),
                                                                                                             governanceServiceName,
                                                                                                             statusError.getClass().getName(),
                                                                                                             statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
