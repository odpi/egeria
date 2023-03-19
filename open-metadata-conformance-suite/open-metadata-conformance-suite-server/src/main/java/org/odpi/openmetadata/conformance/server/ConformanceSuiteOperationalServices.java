/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ConformanceSuiteConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkbench;
import org.odpi.openmetadata.conformance.workbenches.performance.connectorconsumer.PerformanceSuiteConnectorConsumer;
import org.odpi.openmetadata.conformance.workbenches.performance.listener.PerformanceSuiteOMRSTopicListener;
import org.odpi.openmetadata.conformance.workbenches.repository.connectorconsumer.ConformanceSuiteConnectorConsumer;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchWorkPad;
import org.odpi.openmetadata.conformance.beans.TechnologyUnderTestWorkPad;
import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode;
import org.odpi.openmetadata.conformance.workbenches.repository.listener.ConformanceSuiteOMRSTopicListener;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceWorkPad;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceWorkbench;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkbench;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSEnterpriseConnectorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ConformanceSuiteOperationalServices provides the server side control for the Open Metadata Conformance Suite.
 */
public class ConformanceSuiteOperationalServices
{
    private final ConformanceServicesInstanceMap instanceMap = new ConformanceServicesInstanceMap();

    private final String                  localServerName;               /* Initialized in constructor */
    private final String                  localServerUserId;             /* Initialized in constructor */
    private final String                  localServerPassword;           /* Initialized in constructor */
    private final int                     maxPageSize;                   /* Initialized in constructor */


    /**
     * Constructor used at server startup.
     *
     * @param localServerName   name of the local server
     * @param localServerUserId user id for this server to use if processing inbound messages.
     * @param localServerPassword password for this server to use with UserId when sending REST calls.
     *                            (If null then REST calls have no authentication information in them.)
     * @param maxPageSize       maximum number of records that can be requested on the pageSize parameter
     */
    public ConformanceSuiteOperationalServices(String localServerName,
                                               String localServerUserId,
                                               String localServerPassword,
                                               int    maxPageSize)
    {
        this.localServerName = localServerName;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param conformanceSuiteConfig configuration for the conformance suite.
     * @param enterpriseTopicConnector connector that receives all events from the cohorts that this server is
     *                                 registered with.
     * @param enterpriseConnectorManager manager of the list of connectors to the remote members of the cohorts.
     * @param auditLog logging destination
     * @throws OMAGConfigurationErrorException the server is not configured correctly for the conformance suite.
     */
    public void initialize(ConformanceSuiteConfig         conformanceSuiteConfig,
                           OMRSTopicConnector             enterpriseTopicConnector,
                           OMRSEnterpriseConnectorManager enterpriseConnectorManager,
                           OMRSAuditLog                   auditLog) throws OMAGConfigurationErrorException
    {
        final String  methodName = "initialize";

        if (enterpriseTopicConnector == null)
        {
            ConformanceSuiteErrorCode errorCode              = ConformanceSuiteErrorCode.NO_ENTERPRISE_TOPIC;
            String                    errorMessage           = errorCode.getErrorMessageId()
                                                             + errorCode.getFormattedErrorMessage(localServerName);
            String[]                  errorMessageParameters = { localServerName };

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getErrorMessageId(),
                                                      errorMessageParameters,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction(),
                                                      null,
                                                      null);
        }

        if (enterpriseConnectorManager == null)
        {
            ConformanceSuiteErrorCode errorCode              = ConformanceSuiteErrorCode.NO_ENTERPRISE_CONNECTOR_MANAGER;
            String                    errorMessage           = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);
            String[]                  errorMessageParameters = { localServerName };

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getErrorMessageId(),
                                                      errorMessageParameters,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction(),
                                                      null,
                                                      null);
        }
        else if (! enterpriseConnectorManager.isEnterpriseAccessEnabled())
        {
            ConformanceSuiteErrorCode errorCode              = ConformanceSuiteErrorCode.NO_ENTERPRISE_ACCESS;
            String                    errorMessage           = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);
            String[]                  errorMessageParameters = { localServerName };

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getErrorMessageId(),
                                                      errorMessageParameters,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction(),
                                                      null,
                                                      null);
        }

        /*
         * Now start up the workbenches based on the configuration
         */
        List<OpenMetadataConformanceWorkbenchWorkPad> workbenchWorkPads = new ArrayList<>();
        List<OpenMetadataConformanceWorkbench>        runningWorkbenches = new ArrayList<>();

        if (conformanceSuiteConfig.getPlatformWorkbenchConfig() != null)
        {
            PlatformConformanceWorkPad   platformConformanceWorkPad = new PlatformConformanceWorkPad(localServerUserId,
                                                                                                     localServerPassword,
                                                                                                     maxPageSize,
                                                                                                     auditLog,
                                                                                                     conformanceSuiteConfig.getPlatformWorkbenchConfig());
            workbenchWorkPads.add(platformConformanceWorkPad);

            PlatformConformanceWorkbench   platformConformanceWorkbench = new PlatformConformanceWorkbench(platformConformanceWorkPad);
            runningWorkbenches.add(platformConformanceWorkbench);

            Thread  platformWorkbenchThread = new Thread(platformConformanceWorkbench, "Platform Conformance Workbench");
            platformWorkbenchThread.start();
        }


        if (conformanceSuiteConfig.getRepositoryWorkbenchConfig() != null)
        {
            final String workBenchName = "Repository Conformance Workbench";
            RepositoryConformanceWorkPad   repositoryConformanceWorkPad = new RepositoryConformanceWorkPad(localServerUserId,
                                                                                                           localServerPassword,
                                                                                                           maxPageSize,
                                                                                                           auditLog,
                                                                                                           conformanceSuiteConfig.getRepositoryWorkbenchConfig());
            workbenchWorkPads.add(repositoryConformanceWorkPad);

            RepositoryConformanceWorkbench repositoryConformanceWorkbench = new RepositoryConformanceWorkbench(repositoryConformanceWorkPad);
            runningWorkbenches.add(repositoryConformanceWorkbench);

            Thread repositoryWorkbenchThread = new Thread(repositoryConformanceWorkbench, workBenchName);
            repositoryWorkbenchThread.start();

            ConformanceSuiteConnectorConsumer connectorConsumer = new ConformanceSuiteConnectorConsumer(repositoryConformanceWorkPad);
            enterpriseConnectorManager.registerConnectorConsumer(connectorConsumer);

            ConformanceSuiteOMRSTopicListener omrsTopicListener = new ConformanceSuiteOMRSTopicListener(repositoryConformanceWorkPad);

            enterpriseTopicConnector.registerListener(omrsTopicListener, workBenchName);
        }

        if (conformanceSuiteConfig.getRepositoryPerformanceConfig() != null)
        {
            final String workBenchName = "Repository Performance Workbench";
            PerformanceWorkPad performanceWorkPad = new PerformanceWorkPad(localServerUserId,
                    localServerPassword,
                    maxPageSize,
                    auditLog,
                    conformanceSuiteConfig.getRepositoryPerformanceConfig());
            workbenchWorkPads.add(performanceWorkPad);

            PerformanceWorkbench performanceWorkbench = new PerformanceWorkbench(performanceWorkPad);
            runningWorkbenches.add(performanceWorkbench);

            Thread performanceWorkbenchThread = new Thread(performanceWorkbench, workBenchName);
            performanceWorkbenchThread.start();

            PerformanceSuiteConnectorConsumer connectorConsumer = new PerformanceSuiteConnectorConsumer(performanceWorkPad);
            enterpriseConnectorManager.registerConnectorConsumer(connectorConsumer);

            PerformanceSuiteOMRSTopicListener omrsTopicListener = new PerformanceSuiteOMRSTopicListener(performanceWorkPad);

            enterpriseTopicConnector.registerListener(omrsTopicListener, workBenchName);
        }

        instanceMap.setNewInstance(localServerName, new ConformanceServicesInstance(new TechnologyUnderTestWorkPad(workbenchWorkPads),
                                                                                    runningWorkbenches,
                                                                                    auditLog,
                                                                                    conformanceSuiteConfig));
    }


    /**
     * Shutdown the service.
     *
     * @param permanent is the service going away?
     */
    public void terminate(boolean permanent)
    {
        ConformanceServicesInstance instance = instanceMap.getInstance(this.localServerName);

        if (instance != null)
        {
            List<OpenMetadataConformanceWorkbench> runningWorkbenches = instance.getWorkbenches();

            if (runningWorkbenches != null)
            {
                for (OpenMetadataConformanceWorkbench workbench : runningWorkbenches)
                {
                    if (workbench != null)
                    {
                        workbench.stopRunning();
                    }
                }
            }

            instanceMap.removeInstance(this.localServerName);
        }
    }
}