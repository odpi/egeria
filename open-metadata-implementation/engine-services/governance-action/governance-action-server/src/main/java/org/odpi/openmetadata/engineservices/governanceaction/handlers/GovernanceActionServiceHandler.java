/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.engineservices.governanceaction.context.GovernanceListenerManager;
import org.odpi.openmetadata.engineservices.governanceaction.context.OpenMetadataStoreClient;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceActionServiceHandler provides the thread to run a governance action service.  A new instance is created for each request.
 */
public class GovernanceActionServiceHandler extends GovernanceServiceHandler
{
    private GovernanceActionService    governanceActionService;
    private String                     governanceActionServiceType;
    private GovernanceContext          governanceContext;


    /**
     * Constructor sets up the key parameters for running the governance action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param governanceActionEngineProperties properties of the governance action engine - used for message logging
     * @param governanceActionEngineGUID unique Identifier of the governance action engine - used for message logging
     * @param governanceActionUserId user Id for use by the engine host services
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param governanceActionClient client for managing governance actions
     * @param requestType incoming request
     * @param requestParameters parameters associated with the request type
     * @param requestSourceElements the elements that caused this service to run
     * @param actionTargetElements the elements for the service to work on
     * @param governanceActionServiceGUID unique identifier of entity defining this governance service
     * @param governanceActionServiceName unique name of this governance action service - used for message logging
     * @param governanceActionServiceConnector connector that does the work
     * @param partnerServerName name of the metadata server used by the governance service
     * @param partnerServerPlatformURLRoot location of the metadata server used by the governance service
     * @param governanceEngineClient client for use by the engine host services
     * @param governanceListenerManager listener manager for Watchdog Governance Services
     * @param auditLog destination for log messages
     * @throws InvalidParameterException problem with the governance service definitions
     */
    GovernanceActionServiceHandler(GovernanceEngineProperties governanceActionEngineProperties,
                                   String                     governanceActionEngineGUID,
                                   String                     governanceActionUserId,
                                   String                     governanceActionGUID,
                                   GovernanceEngineClient     governanceActionClient,
                                   String                     requestType,
                                   Map<String, String>        requestParameters,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   String                     governanceActionServiceGUID,
                                   String                     governanceActionServiceName,
                                   Connector                  governanceActionServiceConnector,
                                   String                     partnerServerName,
                                   String                     partnerServerPlatformURLRoot,
                                   GovernanceEngineClient     governanceEngineClient,
                                   GovernanceListenerManager  governanceListenerManager,
                                   AuditLog                   auditLog) throws InvalidParameterException
    {
        super(governanceActionEngineProperties,
              governanceActionEngineGUID,
              governanceActionUserId,
              governanceActionGUID,
              governanceActionClient,
              requestType,
              governanceActionServiceGUID,
              governanceActionServiceName,
              governanceActionServiceConnector,
              auditLog);

        final String actionDescription = "Initializing GovernanceActionService";
        final String governanceActionServiceConnectorParameterName = "governanceActionServiceConnector";

        final String watchdogGovernanceActionServiceType     = "WatchdogGovernanceActionService";
        final String provisioningGovernanceActionServiceType = "ProvisioningGovernanceActionService";
        final String verificationGovernanceActionServiceType = "VerificationGovernanceActionService";
        final String triageGovernanceActionServiceType       = "TriageGovernanceActionService";
        final String remediationGovernanceActionServiceType  = "RemediationGovernanceActionService";

        try
        {
            OpenMetadataClient openMetadataClient = new OpenMetadataStoreClient(partnerServerName,
                                                                                partnerServerPlatformURLRoot,
                                                                                governanceEngineClient,
                                                                                governanceListenerManager,
                                                                                this,
                                                                                governanceActionUserId);

            if (governanceActionServiceConnector instanceof WatchdogGovernanceActionService)
            {
                WatchdogGovernanceContext context = new WatchdogGovernanceContext(governanceActionUserId,
                                                                                  governanceActionGUID,
                                                                                  requestType,
                                                                                  requestParameters,
                                                                                  requestSourceElements,
                                                                                  actionTargetElements,
                                                                                  openMetadataClient);

                WatchdogGovernanceActionService service = (WatchdogGovernanceActionService)governanceActionServiceConnector;

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;
                this.governanceActionServiceType = watchdogGovernanceActionServiceType;
            }
            else if (governanceActionServiceConnector instanceof VerificationGovernanceActionService)
            {
                VerificationGovernanceContext context = new VerificationGovernanceContext(governanceActionUserId,
                                                                                          governanceActionGUID,
                                                                                          requestType,
                                                                                          requestParameters,
                                                                                          requestSourceElements,
                                                                                          actionTargetElements,
                                                                                          openMetadataClient);

                VerificationGovernanceActionService service = (VerificationGovernanceActionService)governanceActionServiceConnector;

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;
                this.governanceActionServiceType = verificationGovernanceActionServiceType;
            }
            else if (governanceActionServiceConnector instanceof TriageGovernanceActionService)
            {
                TriageGovernanceContext context = new TriageGovernanceContext(governanceActionUserId,
                                                                              governanceActionGUID,
                                                                              requestType,
                                                                              requestParameters,
                                                                              requestSourceElements,
                                                                              actionTargetElements,
                                                                              openMetadataClient);

                TriageGovernanceActionService service = (TriageGovernanceActionService)governanceActionServiceConnector;

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;
                this.governanceActionServiceType = triageGovernanceActionServiceType;
            }
            else if (governanceActionServiceConnector instanceof RemediationGovernanceActionService)
            {
                RemediationGovernanceContext context = new RemediationGovernanceContext(governanceActionUserId,
                                                                                        governanceActionGUID,
                                                                                        requestType,
                                                                                        requestParameters,
                                                                                        requestSourceElements,
                                                                                        actionTargetElements,
                                                                                        openMetadataClient);

                RemediationGovernanceActionService service = (RemediationGovernanceActionService)governanceActionServiceConnector;

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;
                this.governanceActionServiceType = remediationGovernanceActionServiceType;
            }
            else if (governanceActionServiceConnector instanceof ProvisioningGovernanceActionService)
            {
                ProvisioningGovernanceContext context = new ProvisioningGovernanceContext(governanceActionUserId,
                                                                                          governanceActionGUID,
                                                                                          requestType,
                                                                                          requestParameters,
                                                                                          requestSourceElements,
                                                                                          actionTargetElements,
                                                                                          openMetadataClient);

                ProvisioningGovernanceActionService service = (ProvisioningGovernanceActionService)governanceActionServiceConnector;

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;
                this.governanceActionServiceType = provisioningGovernanceActionServiceType;
            }
            else if (governanceActionServiceConnector instanceof GovernanceActionService)
            {
                auditLog.logMessage(actionDescription,
                                      GovernanceActionAuditCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                       requestType,
                                                                                                                       governanceActionServiceConnector.getClass().getName()));
                throw new InvalidParameterException(GovernanceActionErrorCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                                     requestType,
                                                                                                                                     governanceActionServiceConnector.getClass().getName()),
                                                    this.getClass().getName(),
                                                    actionDescription,
                                                    governanceActionServiceConnectorParameterName);
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                      GovernanceActionAuditCode.NOT_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                   requestType,
                                                                                                                   governanceActionServiceConnector.getClass().getName()));
                throw new InvalidParameterException(GovernanceActionErrorCode.NOT_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                                 requestType,
                                                                                                                                 governanceActionServiceConnector.getClass().getName()),
                                                    this.getClass().getName(),
                                                    actionDescription,
                                                    governanceActionServiceConnectorParameterName);
            }
        }
        catch (InvalidParameterException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceActionAuditCode.INVALID_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                   requestType,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                  error);
            throw new InvalidParameterException(GovernanceActionErrorCode.INVALID_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                                 requestType,
                                                                                                                                 error.getClass().getName(),
                                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                actionDescription,
                                                error,
                                                governanceActionServiceConnectorParameterName);
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

        final String actionDescription = "Run governance service";

        try
        {
            auditLog.logMessage(actionDescription,
                                GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_STARTING.getMessageDefinition(governanceActionServiceType,
                                                                                                                  governanceServiceName,
                                                                                                                  requestType,
                                                                                                                  governanceEngineProperties.getQualifiedName(),
                                                                                                                  governanceEngineGUID));



            startTime = new Date();
            governanceActionService.start();
            endTime = new Date();

            CompletionStatus completionStatus = governanceContext.getCompletionStatus();

            if (completionStatus == null)
            {
                auditLog.logMessage(actionDescription,
                                    GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_RETURNED.getMessageDefinition(governanceActionServiceType,
                                                                                                                      governanceServiceName,
                                                                                                                      requestType,
                                                                                                                      Long.toString(
                                                                                                                              endTime.getTime() - startTime.getTime())));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceActionServiceType,
                                                                                                                      governanceServiceName,
                                                                                                                      requestType,
                                                                                                                      completionStatus.getName(),
                                                                                                                      Long.toString(endTime.getTime() - startTime.getTime())));
            }
        }
        catch (Exception  error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_FAILED.getMessageDefinition(governanceActionServiceType,
                                                                                                                  governanceServiceName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  governanceEngineProperties.getQualifiedName(),
                                                                                                                  governanceEngineGUID,
                                                                                                                  error.getMessage()),
                                  error.toString(),
                                  error);

            try
            {
                CompletionStatus completionStatus = governanceContext.getCompletionStatus();

                if (completionStatus == null)
                {
                    governanceContext.recordCompletionStatus(CompletionStatus.FAILED, null);
                }
            }
            catch (Throwable statusError)
            {
                auditLog.logException(actionDescription,
                                      GovernanceActionAuditCode.EXC_ON_ERROR_STATUS_UPDATE.getMessageDefinition(governanceEngineProperties.getDisplayName(),
                                                                                                                governanceServiceName,
                                                                                                                statusError.getClass().getName(),
                                                                                                                statusError.getMessage()),
                                      statusError.toString(),
                                      statusError);
            }
        }
    }
}
