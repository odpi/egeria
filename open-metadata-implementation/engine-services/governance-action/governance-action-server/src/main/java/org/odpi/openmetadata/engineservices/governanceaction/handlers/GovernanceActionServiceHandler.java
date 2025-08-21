/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.handlers;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.*;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.*;

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
     * @param localServerName name of this engine host
     * @param governanceActionEngineProperties properties of the governance action engine - used for message logging
     * @param governanceActionEngineGUID unique Identifier of the governance action engine - used for message logging
     * @param governanceActionUserId user id for use by the engine host services
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param engineActionClient client for managing engine actions
     * @param serviceRequestType incoming request
     * @param requestParameters parameters associated with the request type
     * @param generateIntegrationReport generate integration report
     * @param deleteMethod default delete method
     * @param requesterUserId original user requesting this governance service
     * @param requestSourceElements the elements that caused this service to run
     * @param actionTargetElements the elements for the service to work on
     * @param governanceActionServiceGUID unique identifier of entity defining this governance service
     * @param governanceActionServiceName unique name of this governance action service - used for message logging
     * @param governanceActionServiceConnector connector that does the work
     * @param openMetadataClient access to the open metadata store
     * @param governanceContextClient client for services supporting the completion of a governance action service
     * @param governanceConfiguration client for services that configure the governance servers
     * @param startDate date/time that the governance service should start executing
     * @param auditLog destination for log messages
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException problem with the governance service definitions
     */
    GovernanceActionServiceHandler(String                     localServerName,
                                   GovernanceEngineProperties governanceActionEngineProperties,
                                   String                     governanceActionEngineGUID,
                                   String                     governanceActionUserId,
                                   String                     engineActionGUID,
                                   GovernanceContextClient    engineActionClient,
                                   String                     serviceRequestType,
                                   Map<String, String>        requestParameters,
                                   boolean                    generateIntegrationReport,
                                   DeleteMethod               deleteMethod,
                                   String                     requesterUserId,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   String                     governanceActionServiceGUID,
                                   String                     governanceActionServiceName,
                                   Connector                  governanceActionServiceConnector,
                                   OpenMetadataClient         openMetadataClient,
                                   GovernanceContextClient    governanceContextClient,
                                   GovernanceConfiguration    governanceConfiguration,
                                   Date                       startDate,
                                   AuditLog                   auditLog,
                                   int                        maxPageSize) throws InvalidParameterException
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
              startDate,
              auditLog);

        final String actionDescription = "Initializing GeneralGovernanceActionService";
        final String governanceActionServiceConnectorParameterName = "governanceActionServiceConnector";

        final String genericGovernanceActionServiceType = "supported";


        try
        {
            if (governanceActionServiceConnector instanceof GovernanceActionServiceConnector service)
            {
                GovernanceActionContext context = new GovernanceActionContext(localServerName,
                                                                              EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName(),
                                                                              null,
                                                                              null,
                                                                              engineActionGUID,
                                                                              governanceActionServiceName,
                                                                              governanceActionUserId,
                                                                              governanceActionServiceGUID,
                                                                              generateIntegrationReport,
                                                                              openMetadataClient,
                                                                              auditLog,
                                                                              maxPageSize,
                                                                              deleteMethod,
                                                                              engineActionGUID,
                                                                              serviceRequestType,
                                                                              requestParameters,
                                                                              requesterUserId,
                                                                              requestSourceElements,
                                                                              actionTargetElements,
                                                                              governanceConfiguration,
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

        final String actionDescription = "Run governance action service";

        try
        {
            super.waitForStartDate(engineHostUserId);

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
        catch (Exception error)
        {
            AuditLogMessageDefinition exceptionMessage = GovernanceActionAuditCode.GOVERNANCE_ACTION_SERVICE_FAILED.getMessageDefinition(governanceActionServiceType,
                                                                                                                                         governanceServiceName,
                                                                                                                                         error.getClass().getName(),
                                                                                                                                         governanceEngineProperties.getQualifiedName(),
                                                                                                                                         governanceEngineGUID,
                                                                                                                                         error.getMessage());
            auditLog.logException(actionDescription, exceptionMessage, error.toString(), error);

            try
            {
                CompletionStatus completionStatus = governanceContext.getCompletionStatus();

                if (completionStatus == null)
                {
                    governanceContext.recordCompletionStatus(Guard.SERVICE_FAILED.getCompletionStatus(), Collections.singletonList(Guard.SERVICE_FAILED.getName()), null, null, exceptionMessage);
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
        catch (NoClassDefFoundError  error)
        {
            /*
             * Exception NoClassDefFoundError means there is a missing class in the Jar file for the GovernanceActionService.
             * The governance action service can not run until this build problem is corrected.
             */
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
                    governanceContext.recordCompletionStatus(Guard.SERVICE_IMPLEMENTATION_INVALID.getCompletionStatus(), Collections.singletonList(Guard.SERVICE_IMPLEMENTATION_INVALID.getName()), null, null, error.getMessage());
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
