/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.handlers;

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

import java.util.Date;

/**
 * DiscoveryServiceHandler provides the thread to run a discovery service.  A new instance is created for each request.
 */
public class DiscoveryServiceHandler implements Runnable
{
    private GovernanceEngineProperties discoveryEngineProperties;
    private String                     discoveryEngineGUID;
    private String                     assetDiscoveryType;
    private String                     discoveryServiceName;
    private DiscoveryService           discoveryService;
    private DiscoveryContext           discoveryContext;
    private AuditLog                   auditLog;


    /**
     * Constructor sets up the key parameters for running the discovery service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param discoveryEngineProperties properties of the discovery engine - used for message logging
     * @param discoveryEngineGUID unique Identifier of the discovery engine - used for message logging
     * @param assetDiscoveryType type of asset to analyse - used for message logging
     * @param discoveryServiceName name of this discovery service - used for message logging
     * @param discoveryServiceConnector connector that does the work
     * @param discoveryContext context for the connector
     * @param auditLog destination for log messages
     */
    DiscoveryServiceHandler(GovernanceEngineProperties discoveryEngineProperties,
                            String                     discoveryEngineGUID,
                            String                     assetDiscoveryType,
                            String                     discoveryServiceName,
                            Connector                  discoveryServiceConnector,
                            DiscoveryContext           discoveryContext,
                            AuditLog                   auditLog) throws InvalidParameterException
    {
        this.discoveryEngineProperties = discoveryEngineProperties;
        this.discoveryEngineGUID       = discoveryEngineGUID;
        this.assetDiscoveryType        = assetDiscoveryType;
        this.discoveryServiceName      = discoveryServiceName;
        this.discoveryContext          = discoveryContext;
        this.auditLog                  = auditLog;

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
                                                                                                        assetDiscoveryType,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                  error);
            throw new InvalidParameterException(AssetAnalysisErrorCode.INVALID_DISCOVERY_SERVICE.getMessageDefinition(discoveryServiceName,
                                                                                                                      assetDiscoveryType,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                discoveryServiceConnectorParameterName);
        }
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
                                AssetAnalysisAuditCode.DISCOVERY_SERVICE_STARTING.getMessageDefinition(discoveryServiceName,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       assetDiscoveryType,
                                                                                                       discoveryEngineProperties.getQualifiedName(),
                                                                                                       discoveryEngineGUID,
                                                                                                       discoveryReport.getDiscoveryReportGUID()));


            discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.IN_PROGRESS);

            discoveryService.setDiscoveryContext(discoveryContext);
            discoveryService.setDiscoveryServiceName(discoveryServiceName);

            startTime = new Date();
            discoveryService.start();
            endTime = new Date();

            auditLog.logMessage(actionDescription,
                                AssetAnalysisAuditCode.DISCOVERY_SERVICE_COMPLETE.getMessageDefinition(discoveryServiceName,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       assetDiscoveryType,
                                                                                                       Long.toString(endTime.getTime() - startTime.getTime()),
                                                                                                       discoveryReport.getDiscoveryReportGUID()));

            discoveryReport.setDiscoveryRequestStatus(DiscoveryRequestStatus.COMPLETED);
            discoveryService.disconnect();
        }
        catch (Throwable  error)
        {
            auditLog.logException(actionDescription,
                                  AssetAnalysisAuditCode.DISCOVERY_SERVICE_FAILED.getMessageDefinition(discoveryServiceName,
                                                                                                       error.getClass().getName(),
                                                                                                       discoveryReportGUID,
                                                                                                       discoveryContext.getAssetGUID(),
                                                                                                       assetDiscoveryType,
                                                                                                       discoveryEngineProperties.getQualifiedName(),
                                                                                                       discoveryEngineGUID,
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
                                      AssetAnalysisAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(discoveryEngineProperties.getDisplayName(),
                                                                                                             discoveryServiceName,
                                                                                                             statusError.getClass().getName(),
                                                                                                             statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
