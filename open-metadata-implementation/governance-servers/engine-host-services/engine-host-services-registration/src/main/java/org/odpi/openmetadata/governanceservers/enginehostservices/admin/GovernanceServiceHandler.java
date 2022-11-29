/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * GovernanceServiceHandler provides the thread to run a governance service.  A new instance is created for each request.
 * The subclasses implement the run method.
 */
public abstract class GovernanceServiceHandler implements Runnable
{
    protected GovernanceEngineProperties governanceEngineProperties;
    protected String                     governanceEngineGUID;
    protected String                     engineHostUserId;
    protected String                     governanceServiceGUID;
    protected String                     governanceServiceName;

    private final GovernanceEngineClient governanceActionClient;

    protected Connector governanceService;
    protected String    governanceActionGUID;
    protected String    serviceRequestType;
    protected AuditLog  auditLog;


    /**
     * Constructor sets up the key parameters for running the governance action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param governanceEngineProperties properties of the governance engine - used for message logging
     * @param governanceEngineGUID unique identifier of the governance engine - used for message logging
     * @param engineHostUserId userId for making updates to the governance actions
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param governanceActionClient client for processing governance actions
     * @param serviceRequestType incoming request type
     * @param governanceServiceGUID unique identifier of the governance service
     * @param governanceServiceName name of this governance  service - used for message logging
     * @param governanceService implementation of governance service
     * @param auditLog destination for log messages
     */
    protected GovernanceServiceHandler(GovernanceEngineProperties governanceEngineProperties,
                                       String                     governanceEngineGUID,
                                       String                     engineHostUserId,
                                       String                     governanceActionGUID,
                                       GovernanceEngineClient     governanceActionClient,
                                       String                     serviceRequestType,
                                       String                     governanceServiceGUID,
                                       String                     governanceServiceName,
                                       Connector                  governanceService,
                                       AuditLog                   auditLog)
    {
        this.governanceEngineProperties = governanceEngineProperties;
        this.governanceEngineGUID       = governanceEngineGUID;
        this.engineHostUserId           = engineHostUserId;
        this.governanceServiceGUID      = governanceServiceGUID;
        this.governanceServiceName      = governanceServiceName;
        this.governanceActionGUID       = governanceActionGUID;
        this.governanceActionClient     = governanceActionClient;
        this.serviceRequestType         = serviceRequestType;
        this.governanceService          = governanceService;
        this.auditLog                   = auditLog;
    }


    /**
     * Return the unique name of this governance service.
     *
     * @return string name
     */
    public String getGovernanceServiceName()
    {
        return governanceServiceName;
    }


    /**
     * Return the unique identifier of this governance service.
     *
     * @return string guid
     */
    public String getGovernanceServiceGUID()
    {
        return governanceServiceGUID;
    }


    /**
     * Return the unique name for the hosting governance engine.
     *
     * @return string name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineProperties.getQualifiedName();
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param actionTargetGUID unique identifier of the governance action service.
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target.
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void updateActionTargetStatus(String                 actionTargetGUID,
                                         GovernanceActionStatus status,
                                         Date                   startDate,
                                         Date                   completionDate) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateActionTargetStatus";

        if (auditLog != null)
        {
            String statusString   = "<null>";
            String startTime      = "<null>";
            String completionTime = "<null>";

            if (status != null)
            {
                statusString = status.getName();
            }

            if (startDate != null)
            {
                startTime = startDate.toString();
            }

            if (completionDate != null)
            {
                completionTime = completionDate.toString();
            }

            auditLog.logMessage(methodName, EngineHostServicesAuditCode.GOVERNANCE_ACTION_TARGET_COMPLETION.getMessageDefinition(governanceActionGUID,
                                                                                                                                 governanceServiceName,
                                                                                                                                 getGovernanceEngineName(),
                                                                                                                                 serviceRequestType,
                                                                                                                                 actionTargetGUID,
                                                                                                                                 statusString,
                                                                                                                                 startTime,
                                                                                                                                 completionTime));
        }

        governanceActionClient.updateActionTargetStatus(engineHostUserId, actionTargetGUID, status, startDate, completionDate);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param requestParameters properties to pass to the next governance action service
     * @param newActionTargets map of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(CompletionStatus      status,
                                       List<String>          outputGuards,
                                       Map<String, String>   requestParameters,
                                       List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "recordCompletionStatus";

        if (auditLog != null)
        {
            String statusString          = "<null>";
            String guardsString          = "<null>";
            String requestParameterNames = "<null>";
            String actionTargets         = "<null>";

            if (status != null)
            {
                statusString = status.getName();
            }

            if (outputGuards != null)
            {
                guardsString = outputGuards.toString();
            }

            if (requestParameters != null)
            {
                requestParameterNames = requestParameters.keySet().toString();
            }

            if (newActionTargets != null)
            {
                actionTargets = newActionTargets.toString();
            }

            auditLog.logMessage(methodName, EngineHostServicesAuditCode.GOVERNANCE_ACTION_RECORD_COMPLETION.getMessageDefinition(governanceActionGUID,
                                                                                                                                 governanceServiceName,
                                                                                                                                 getGovernanceEngineName(),
                                                                                                                                 serviceRequestType,
                                                                                                                                 statusString,
                                                                                                                                 guardsString,
                                                                                                                                 requestParameterNames,
                                                                                                                                 actionTargets));
        }

        governanceActionClient.recordCompletionStatus(engineHostUserId,
                                                      governanceActionGUID,
                                                      requestParameters,
                                                      status,
                                                      outputGuards,
                                                      newActionTargets);
    }


    /**
     * Disconnect the governance action service.  Called because the governance action service had set a completion status or
     * the server is shutting down.
     *
     * @throws ConnectorCheckedException connector is in trouble
     */
    public void disconnect() throws ConnectorCheckedException
    {
        if (governanceService != null)
        {
            governanceService.disconnect();
        }
    }
}
