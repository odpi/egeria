/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.handlers;

import org.odpi.openmetadata.discoveryserver.auditlog.DiscoveryServerAuditCode;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnnotationStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryContext;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.Date;
import java.util.List;


public class DiscoveryServiceHandler implements Runnable
{
    private DiscoveryEngineProperties discoveryEngineProperties;
    private String                    assetType;
    private String                    discoveryServiceName;
    private DiscoveryService          discoveryService;
    private DiscoveryContext          discoveryContext;
    private OMRSAuditLog              auditLog;


    /**
     * Constructor sets up the key parameters for running the discovery service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param discoveryEngineProperties properties of the discovery engine - used for message logging
     * @param assetType type of asset to analyse - used for message logging
     * @param discoveryServiceName name of this discovery service - used for message logging
     * @param discoveryService connector that does the work
     * @param discoveryContext context for the connector
     * @param auditLog destination for log messages
     */
     DiscoveryServiceHandler(DiscoveryEngineProperties discoveryEngineProperties,
                             String                    assetType,
                             String                    discoveryServiceName,
                             DiscoveryService          discoveryService,
                             DiscoveryContext          discoveryContext,
                             OMRSAuditLog              auditLog)
    {
        this.discoveryEngineProperties = discoveryEngineProperties;
        this.assetType = assetType;
        this.discoveryServiceName = discoveryServiceName;
        this.discoveryService = discoveryService;
        this.discoveryContext = discoveryContext;
        this.auditLog = auditLog;
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        DiscoveryServerAuditCode auditCode;
        Date                     startTime;
        Date                     endTime;

        final String actionDescription = "discoverAsset";

        auditCode = DiscoveryServerAuditCode.DISCOVERY_SERVICE_STARTING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(discoveryServiceName,
                                                            discoveryContext.getAssetGUID(),
                                                            assetType,
                                                            discoveryEngineProperties.getQualifiedName(),
                                                            discoveryEngineProperties.getGUID(),
                                                            discoveryContext.getDiscoveryReportGUID()),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try
        {
            discoveryService.setDiscoveryContext(discoveryContext);
            startTime = new Date();
            discoveryService.start();
            endTime = new Date();
            auditCode = DiscoveryServerAuditCode.DISCOVERY_SERVICE_COMPLETE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(discoveryServiceName,
                                                                discoveryContext.getAssetGUID(),
                                                                assetType,
                                                                Long.toString(endTime.getTime() - startTime.getTime()),
                                                                discoveryContext.getDiscoveryReportGUID()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            discoveryService.disconnect();
        }
        catch (Throwable  error)
        {
            auditCode = DiscoveryServerAuditCode.DISCOVERY_SERVICE_FAILED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(discoveryServiceName,
                                                                error.getClass().getName(),
                                                                discoveryContext.getDiscoveryReportGUID(),
                                                                discoveryContext.getAssetGUID(),
                                                                assetType,
                                                                discoveryEngineProperties.getQualifiedName(),
                                                                discoveryEngineProperties.getGUID(),
                                                                error.getMessage()),
                               error.toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }
}
