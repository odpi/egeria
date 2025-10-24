/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.handlers;

import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceContext;
import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceServiceConnector;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Date;

/**
 * RepositoryGovernanceServiceHandler provides the support to run a repository governance service.
 * A new instance is created for each request, and it is assigned its own thread.
 */
public class RepositoryGovernanceServiceHandler extends GovernanceServiceHandler
{
    private final RepositoryGovernanceServiceConnector repositoryGovernanceService;
    private final RepositoryGovernanceContext          repositoryGovernanceContext;


    /**
     * Constructor sets up the key parameters for running the repository governance service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param repositoryGovernanceEngineProperties properties of the repository governance engine - used for message logging
     * @param repositoryGovernanceEngineGUID unique Identifier of the repository governance engine - used for message logging
     * @param engineHostUserId userId for making updates to the governance actions
     * @param engineActionGUID unique identifier of the governance action that triggered this governance service
     * @param engineActionClient client for processing governance actions
     * @param serviceRequestType requestType - used for message logging
     * @param repositoryGovernanceServiceGUID name of this repository governance service - used for message logging
     * @param repositoryGovernanceServiceName name of this repository governance service - used for message logging
     * @param repositoryGovernanceServiceConnector connector that does the work
     * @param repositoryGovernanceContext context for the connector
     * @param startDate date/time that the governance service should start executing
     * @param auditLog destination for log messages
     */
    RepositoryGovernanceServiceHandler(GovernanceEngineProperties  repositoryGovernanceEngineProperties,
                                       String                      repositoryGovernanceEngineGUID,
                                       String                      engineHostUserId,
                                       String                      engineActionGUID,
                                       GovernanceContextClient     engineActionClient,
                                       String                      serviceRequestType,
                                       String                      repositoryGovernanceServiceGUID,
                                       String                      repositoryGovernanceServiceName,
                                       Connector                   repositoryGovernanceServiceConnector,
                                       RepositoryGovernanceContext repositoryGovernanceContext,
                                       Date                        startDate,
                                       AuditLog                    auditLog) throws InvalidParameterException
    {
        super(repositoryGovernanceEngineProperties,
              repositoryGovernanceEngineGUID,
              engineHostUserId,
              engineActionGUID,
              engineActionClient,
              serviceRequestType,
              repositoryGovernanceServiceGUID,
              repositoryGovernanceServiceName,
              repositoryGovernanceServiceConnector,
              startDate,
              auditLog);

        this.repositoryGovernanceContext = repositoryGovernanceContext;

        try
        {
            this.repositoryGovernanceService = (RepositoryGovernanceServiceConnector) repositoryGovernanceServiceConnector;
        }
        catch (Exception error)
        {
            final String repositoryGovernanceServiceConnectorParameterName = "repositoryGovernanceServiceConnector";
            final String actionDescription = "Cast connector to RepositoryGovernanceServiceConnector";

            auditLog.logException(actionDescription,
                                  RepositoryGovernanceAuditCode.INVALID_REPOSITORY_GOVERNANCE_SERVICE.getMessageDefinition(repositoryGovernanceServiceName,
                                                                                                                           serviceRequestType,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                  error);
            throw new InvalidParameterException(RepositoryGovernanceErrorCode.INVALID_REPOSITORY_GOVERNANCE_SERVICE.getMessageDefinition(repositoryGovernanceServiceName,
                                                                                                                                         serviceRequestType,
                                                                                                                                         error.getClass().getName(),
                                                                                                                                         error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                repositoryGovernanceServiceConnectorParameterName);
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

        final String actionDescription = "Run a repository governance service";

        try
        {
            super.waitForStartDate(engineHostUserId);

            auditLog.logMessage(actionDescription,
                                RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_STARTING.getMessageDefinition(governanceServiceName,
                                                                                                                          serviceRequestType,
                                                                                                                          governanceEngineProperties.getQualifiedName(),
                                                                                                                          governanceEngineGUID));



            repositoryGovernanceService.setRepositoryGovernanceContext(repositoryGovernanceContext);
            repositoryGovernanceService.setRepositoryGovernanceServiceName(governanceServiceName);

            startTime = new Date();
            repositoryGovernanceService.start();
            endTime = new Date();

            CompletionStatus completionStatus = repositoryGovernanceContext.getCompletionStatus();

            if (completionStatus == null)
            {
                auditLog.logMessage(actionDescription,
                                    RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_RETURNED.getMessageDefinition(governanceServiceName,
                                                                                                                              serviceRequestType,
                                                                                                          Long.toString(endTime.getTime() - startTime.getTime())));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_COMPLETE.getMessageDefinition(governanceServiceName,
                                                                                                                              serviceRequestType,
                                                                                                          completionStatus.getName(),
                                                                                                          Long.toString(endTime.getTime() - startTime.getTime())));
                super.disconnect();
                repositoryGovernanceService.setRepositoryGovernanceContext(null);
            }
        }
        catch (Exception  error)
        {
            auditLog.logException(actionDescription,
                                  RepositoryGovernanceAuditCode.REPOSITORY_GOVERNANCE_SERVICE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                      error.getClass().getName(),
                                                                                                                          serviceRequestType,
                                                                                                      governanceEngineProperties.getQualifiedName(),
                                                                                                      governanceEngineGUID,
                                                                                                      error.getMessage()),
                                  error.toString(),
                                  error);
            try
            {
                CompletionStatus completionStatus = repositoryGovernanceContext.getCompletionStatus();

                if (completionStatus == null)
                {
                    repositoryGovernanceContext.recordCompletionStatus(CompletionStatus.FAILED, null, null, null, error.getMessage());
                    super.disconnect();
                }
            }
            catch (Exception statusError)
            {
                auditLog.logException(actionDescription,
                                      RepositoryGovernanceAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(governanceEngineProperties.getDisplayName(),
                                                                                                                governanceServiceName,
                                                                                                                statusError.getClass().getName(),
                                                                                                                statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
