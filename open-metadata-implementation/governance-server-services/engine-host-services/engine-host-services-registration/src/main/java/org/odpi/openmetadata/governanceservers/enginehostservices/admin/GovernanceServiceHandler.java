/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.requestid.RequestId;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceServiceHandler provides the thread to run a governance service.  A new instance is created for each request.
 * The subclasses implement the run method.
 */
public abstract class GovernanceServiceHandler implements Runnable
{
    protected final GovernanceEngineProperties governanceEngineProperties;
    protected final String                     governanceEngineGUID;
    protected final String                     engineHostUserId;
    protected final String                     governanceServiceGUID;
    protected final String                     governanceServiceName;
    protected final Connector                  governanceService;
    protected final String                     engineActionGUID;
    protected final String                     serviceRequestType;
    protected final AuditLog                   auditLog;

    private final GovernanceContextClient engineActionClient;
    private final Date                    requestedStartDate;


    private static final RequestId requestId = new RequestId();


    /**
     * Constructor sets up the key parameters for running the governance action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param governanceEngineProperties properties of the governance engine - used for message logging
     * @param governanceEngineGUID unique identifier of the governance engine - used for message logging
     * @param engineHostUserId userId for making updates to the governance actions
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param engineActionClient client for processing engine actions
     * @param serviceRequestType incoming request type
     * @param governanceServiceGUID unique identifier of the governance service
     * @param governanceServiceName name of this governance service - used for message logging
     * @param governanceService implementation of governance service
     * @param requestedStartDate date/time that the governance service should start executing
     * @param auditLog destination for log messages
     */
    protected GovernanceServiceHandler(GovernanceEngineProperties governanceEngineProperties,
                                       String                     governanceEngineGUID,
                                       String                     engineHostUserId,
                                       String                     engineActionGUID,
                                       GovernanceContextClient    engineActionClient,
                                       String                     serviceRequestType,
                                       String                     governanceServiceGUID,
                                       String                     governanceServiceName,
                                       Connector                  governanceService,
                                       Date                       requestedStartDate,
                                       AuditLog                   auditLog)
    {
        this.governanceEngineProperties = governanceEngineProperties;
        this.governanceEngineGUID       = governanceEngineGUID;
        this.engineHostUserId           = engineHostUserId;
        this.governanceServiceGUID      = governanceServiceGUID;
        this.governanceServiceName      = governanceServiceName;
        this.engineActionGUID           = engineActionGUID;
        this.engineActionClient         = engineActionClient;
        this.serviceRequestType         = serviceRequestType;
        this.governanceService          = governanceService;
        this.requestedStartDate         = requestedStartDate;
        this.auditLog                   = auditLog;

        requestId.setRequestId(engineActionGUID);
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
     * If the service request has a start time in the future, mark the engine action WAITING and wait for that
     * time to arrive.  Once the service is ready to run, its status is updated to IN_PROGRESS.
     * <br><br>
     * So a delayed engine action runs ACTIVATING (claimed, service being brought up) to WAITING (up, but not
     * due yet) to IN_PROGRESS (running); one with no delay goes straight from ACTIVATING to IN_PROGRESS.
     *
     * @param serverUserId userId for this server
     * @throws InvalidParameterException error updating engine action status
     * @throws PropertyServerException error updating engine action status
     * @throws UserNotAuthorizedException error updating engine action status
     */
    protected void waitForStartDate(String serverUserId) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        Date now = new Date();

        if ((requestedStartDate != null) && (requestedStartDate.after(now)))
        {
            /*
             * The engine action was claimed and its service activated, but it is not due to run yet - so it
             * goes to WAITING, which is precisely that: "waiting for its start time or an actor to claim it".
             *
             * Without this the action sat in ACTIVATING for the whole delay, which says the service is being
             * brought up right now.  For an action scheduled hours ahead that is misleading to anyone reading
             * the status, and it hides the one thing worth knowing about a service that has not started -
             * whether it is stuck coming up, or simply not due yet.
             */
            engineActionClient.updateEngineActionStatus(serverUserId, engineActionGUID, ActivityStatus.WAITING);
        }

        while ((requestedStartDate != null) && (requestedStartDate.after(now)))
        {
            try
            {
                Thread.sleep(requestedStartDate.getTime()-now.getTime());
            }
            catch (InterruptedException interruptedException)
            {
                now = new Date();
            }
        }

        /*
         * This update indicates that the service has now started running.
         */
        engineActionClient.updateEngineActionStatus(serverUserId, engineActionGUID, ActivityStatus.IN_PROGRESS);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param requestParameters properties to pass to the next governance action service
     * @param newActionTargets map of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException a problem connecting to the metadata store
     */
    public void recordCompletionStatus(CompletionStatus      status,
                                       List<String>          outputGuards,
                                       Map<String, String>   requestParameters,
                                       List<NewActionTarget> newActionTargets,
                                       String                completionMessage) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "recordCompletionStatus";

        if (auditLog != null)
        {
            String statusString          = "<null>";
            String guardsString          = "<null>";
            String requestParameterNames = "<null>";

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

            Map<String, String> actionTargetMap = new HashMap<>();

            if (newActionTargets != null)
            {
                for (NewActionTarget newActionTarget : newActionTargets)
                {
                    if (newActionTarget != null)
                    {
                        actionTargetMap.put(newActionTarget.getActionTargetName(), newActionTarget.getActionTargetGUID());
                    }
                }
            }

            auditLog.logMessage(methodName, EngineHostServicesAuditCode.ENGINE_ACTION_RECORD_COMPLETION.getMessageDefinition(engineActionGUID,
                                                                                                                             governanceServiceName,
                                                                                                                             getGovernanceEngineName(),
                                                                                                                             serviceRequestType,
                                                                                                                             statusString,
                                                                                                                             guardsString,
                                                                                                                             requestParameterNames,
                                                                                                                             actionTargetMap.toString(),
                                                                                                                             completionMessage));
        }

        engineActionClient.recordCompletionStatus(engineHostUserId,
                                                  engineActionGUID,
                                                  requestParameters,
                                                  status,
                                                  outputGuards,
                                                  newActionTargets,
                                                  completionMessage);
    }


    /**
     * Disconnect the governance service.  Called because the governance service had set a completion status or
     * the server is shutting down.
     *
     * @throws ConnectorCheckedException connector is in trouble
     */
    public void disconnect() throws ConnectorCheckedException
    {
        requestId.clearRequestId();

        if (governanceService != null)
        {
            governanceService.disconnect();
        }
    }
}
