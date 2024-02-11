/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceEngineProperties;
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
    private final GovernanceActionServiceConnector governanceActionService;
    private final String                           governanceActionServiceType;
    private final GovernanceContext                governanceContext;


    /**
     * Constructor sets up the key parameters for running the governance action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param governanceActionEngineProperties properties of the governance action engine - used for message logging
     * @param governanceActionEngineGUID unique Identifier of the governance action engine - used for message logging
     * @param governanceActionUserId user id for use by the engine host services
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param engineActionClient client for managing engine actions
     * @param serviceRequestType incoming request
     * @param requestParameters parameters associated with the request type
     * @param requestSourceElements the elements that caused this service to run
     * @param actionTargetElements the elements for the service to work on
     * @param governanceActionServiceGUID unique identifier of entity defining this governance service
     * @param governanceActionServiceName unique name of this governance action service - used for message logging
     * @param governanceActionServiceConnector connector that does the work
     * @param partnerServerName name of the metadata server used by the governance service
     * @param partnerServerPlatformURLRoot location of the metadata server used by the governance service
     * @param auditLog destination for log messages
     * @throws InvalidParameterException problem with the governance service definitions
     */
    GovernanceActionServiceHandler(GovernanceEngineProperties governanceActionEngineProperties,
                                   String                     governanceActionEngineGUID,
                                   String                     governanceActionUserId,
                                   String                     engineActionGUID,
                                   GovernanceContextClient    engineActionClient,
                                   String                     serviceRequestType,
                                   Map<String, String>        requestParameters,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   String                     governanceActionServiceGUID,
                                   String                     governanceActionServiceName,
                                   Connector                  governanceActionServiceConnector,
                                   String                     partnerServerName,
                                   String                     partnerServerPlatformURLRoot,
                                   GovernanceContextClient    governanceContextClient,
                                   AuditLog                   auditLog) throws InvalidParameterException
    {
        super(governanceActionEngineProperties,
              governanceActionEngineGUID,
              governanceActionUserId,
              engineActionGUID,
              engineActionClient,
              serviceRequestType,
              governanceActionServiceGUID,
              governanceActionServiceName,
              governanceActionServiceConnector,
              auditLog);

        final String actionDescription = "Initializing GeneralGovernanceActionService";
        final String governanceActionServiceConnectorParameterName = "governanceActionServiceConnector";

        final String genericGovernanceActionServiceType      = "GeneralGovernanceActionService";
        final String watchdogGovernanceActionServiceType     = "WatchdogGovernanceActionService";
        final String provisioningGovernanceActionServiceType = "ProvisioningGovernanceActionService";
        final String verificationGovernanceActionServiceType = "VerificationGovernanceActionService";
        final String triageGovernanceActionServiceType       = "TriageGovernanceActionService";
        final String remediationGovernanceActionServiceType  = "RemediationGovernanceActionService";

        try
        {
            OpenMetadataStoreClient openMetadataClient = new OpenMetadataStoreClient(partnerServerName, partnerServerPlatformURLRoot);

            if (governanceActionServiceConnector instanceof GovernanceActionServiceConnector service)
            {
                GovernanceActionContext context = new GovernanceActionContext(governanceActionUserId,
                                                                              engineActionGUID,
                                                                              serviceRequestType,
                                                                              requestParameters,
                                                                              requestSourceElements,
                                                                              actionTargetElements,
                                                                              openMetadataClient,
                                                                              governanceContextClient,
                                                                              governanceContextClient,
                                                                              governanceContextClient,
                                                                              governanceContextClient,
                                                                              governanceContextClient);

                service.setGovernanceContext(context);
                service.setAuditLog(auditLog);
                service.setGovernanceServiceName(governanceServiceName);

                this.governanceContext = context;
                this.governanceActionService = service;

                if (governanceActionServiceConnector instanceof GeneralGovernanceActionService)
                {
                    this.governanceActionServiceType = genericGovernanceActionServiceType;
                }
                else if (governanceActionServiceConnector instanceof WatchdogGovernanceActionService)
                {
                    this.governanceActionServiceType = watchdogGovernanceActionServiceType;
                }
                else if (governanceActionServiceConnector instanceof VerificationGovernanceActionService)
                {
                    this.governanceActionServiceType = verificationGovernanceActionServiceType;
                }
                else if (governanceActionServiceConnector instanceof TriageGovernanceActionService)
                {
                    this.governanceActionServiceType = triageGovernanceActionServiceType;
                }
                else if (governanceActionServiceConnector instanceof RemediationGovernanceActionService)
                {
                    this.governanceActionServiceType = remediationGovernanceActionServiceType;
                }
                else if (governanceActionServiceConnector instanceof ProvisioningGovernanceActionService)
                {
                    this.governanceActionServiceType = provisioningGovernanceActionServiceType;
                }
                else
                {
                    auditLog.logMessage(actionDescription,
                                        GovernanceActionAuditCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                         serviceRequestType,
                                                                                                                         governanceActionServiceConnector.getClass().getName()));
                    throw new InvalidParameterException(
                            GovernanceActionErrorCode.UNKNOWN_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                             serviceRequestType,
                                                                                                             governanceActionServiceConnector.getClass().getName()),
                            this.getClass().getName(),
                            actionDescription,
                            governanceActionServiceConnectorParameterName);
                }

                auditLog.logMessage(actionDescription,
                                    GovernanceActionAuditCode.GOVERNANCE_ACTION_INITIALIZED.getMessageDefinition(governanceActionServiceName,
                                                                                                                 governanceActionServiceConnector.getClass().getName(),
                                                                                                                 serviceRequestType,
                                                                                                                 getGovernanceEngineName()));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                      GovernanceActionAuditCode.NOT_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                   serviceRequestType,
                                                                                                                   governanceActionServiceConnector.getClass().getName()));
                throw new InvalidParameterException(GovernanceActionErrorCode.NOT_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                                 serviceRequestType,
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
                                                                                                                   serviceRequestType,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                  error);
            throw new InvalidParameterException(GovernanceActionErrorCode.INVALID_GOVERNANCE_ACTION_SERVICE.getMessageDefinition(governanceActionServiceName,
                                                                                                                                 serviceRequestType,
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
                                                                                                                  serviceRequestType,
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
                                                                                                                      serviceRequestType));
            }
            else
            {
                auditLog.logMessage(actionDescription,
                                    GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_COMPLETE.getMessageDefinition(governanceActionServiceType,
                                                                                                                      governanceServiceName,
                                                                                                                      serviceRequestType,
                                                                                                                      completionStatus.getName(),
                                                                                                                      Long.toString(endTime.getTime() - startTime.getTime())));
                super.disconnect();
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
                    super.disconnect();
                }
            }
            catch (Exception statusError)
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
