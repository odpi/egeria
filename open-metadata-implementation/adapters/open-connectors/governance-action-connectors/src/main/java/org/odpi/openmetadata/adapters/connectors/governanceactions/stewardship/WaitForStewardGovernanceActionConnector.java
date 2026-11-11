/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * WaitForStewardGovernanceActionConnector is a Watchdog Action Service that waits for stewards to finish the
 * to dos supplied as its action targets.  It listens for updates to, and deletions of, these to dos and completes
 * once every one of them has finished.  The guard it produces shows whether they were all completed.
 */
public class WaitForStewardGovernanceActionConnector extends WatchdogActionServiceConnector
{
    /**
     * The activity statuses that show that a steward has finished with a to do.  This matches the statuses
     * that end an engine action.
     */
    private static final List<ActivityStatus> FINISHED_ACTIVITY_STATUSES = List.of(ActivityStatus.COMPLETED,
                                                                                   ActivityStatus.INVALID,
                                                                                   ActivityStatus.IGNORED,
                                                                                   ActivityStatus.FAILED,
                                                                                   ActivityStatus.CANCELLED,
                                                                                   ActivityStatus.ABANDONED);

    private final Set<String> unfinishedToDoGUIDs   = new LinkedHashSet<>();
    private final Set<String> completedToDoGUIDs    = new LinkedHashSet<>();
    private final Set<String> notCompletedToDoGUIDs = new LinkedHashSet<>();

    private boolean completed = false;


    /**
     * Default constructor
     */
    public WaitForStewardGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * The listener is registered before the current status of each to do is checked, so that a to do
     * that finishes while the service is starting is not missed.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        unfinishedToDoGUIDs.addAll(this.getToDoGUIDs(watchdogContext.getActionTargetElements()));

        if (unfinishedToDoGUIDs.isEmpty())
        {
            AuditLogMessageDefinition messageDefinition = GovernanceActionConnectorsAuditCode.NO_TO_DOS.getMessageDefinition(watchdogActionServiceName);

            logRecord(methodName, messageDefinition);
            this.completeWaiting(WaitForStewardGuard.NO_TO_DOS, messageDefinition);
            return;
        }

        try
        {
            List<String> interestingMetadataTypes = new ArrayList<>();
            interestingMetadataTypes.add(OpenMetadataType.TO_DO.typeName);

            List<OpenMetadataEventType> interestingEventTypes = new ArrayList<>();
            interestingEventTypes.add(OpenMetadataEventType.ELEMENT_UPDATED);
            interestingEventTypes.add(OpenMetadataEventType.ELEMENT_DELETED);

            /*
             * The listener can only be restricted to a specific instance when there is one to do.  Otherwise,
             * the events are filtered in processEvent.
             */
            String specificInstance = null;

            if (unfinishedToDoGUIDs.size() == 1)
            {
                specificInstance = unfinishedToDoGUIDs.iterator().next();
            }

            watchdogContext.registerListener(new GenericWatchdogActionListener(this),
                                             interestingEventTypes,
                                             interestingMetadataTypes,
                                             specificInstance);

            for (String toDoGUID : new ArrayList<>(unfinishedToDoGUIDs))
            {
                OpenMetadataElement toDo = this.getToDo(toDoGUID);

                if (toDo == null)
                {
                    this.recordFinishedToDo(toDoGUID, null);
                }
                else
                {
                    this.checkToDoStatus(toDoGUID, toDo.getElementProperties());
                }
            }

            if (unfinishedToDoGUIDs.isEmpty())
            {
                this.reportFinishedToDos(methodName);
            }
            else
            {
                logRecord(methodName,
                          GovernanceActionConnectorsAuditCode.WAITING_FOR_TO_DOS.getMessageDefinition(watchdogActionServiceName,
                                                                                                      Integer.toString(unfinishedToDoGUIDs.size()),
                                                                                                      unfinishedToDoGUIDs.toString()));
            }
        }
        catch (Exception error)
        {
            AuditLogMessageDefinition messageDefinition = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                        error.getClass().getName(),
                                                                                                                                        methodName,
                                                                                                                                        error.getMessage());

            logExceptionRecord(methodName, messageDefinition, error);
            this.completeWaiting(WaitForStewardGuard.WAIT_FAILED, messageDefinition);

            throw new GovernanceServiceException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }
    }


    /**
     * This method is called each time a to do is updated or deleted.  Events for to dos that this service is
     * not waiting for, or that arrive after it has completed, are ignored.
     *
     * @param event event containing details of a change to an open metadata element.
     */
    @Override
    public synchronized void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((completed) || (event == null) || (event.getElementHeader() == null))
        {
            return;
        }

        String toDoGUID = event.getElementHeader().getGUID();

        if (unfinishedToDoGUIDs.contains(toDoGUID))
        {
            if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
            {
                this.recordFinishedToDo(toDoGUID, null);
            }
            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
            {
                this.checkToDoStatus(toDoGUID, event.getElementProperties());
            }

            if (unfinishedToDoGUIDs.isEmpty())
            {
                this.reportFinishedToDos(methodName);
            }
        }
    }


    /**
     * Return the unique identifiers of the to dos in the action targets.  A to do is an action target named
     * stewardAction, or one whose element is a ToDo.
     *
     * @param actionTargetElements action targets supplied to the service
     * @return set of unique identifiers (may be empty)
     */
    private Set<String> getToDoGUIDs(List<ActionTargetElement> actionTargetElements)
    {
        Set<String> toDoGUIDs = new LinkedHashSet<>();

        if (actionTargetElements != null)
        {
            for (ActionTargetElement actionTargetElement : actionTargetElements)
            {
                if ((actionTargetElement != null) && (actionTargetElement.getActionTargetGUID() != null))
                {
                    if ((SurveyActionTarget.TO_DO_ELEMENT.getName().equals(actionTargetElement.getActionTargetName())) ||
                            (this.isToDo(actionTargetElement.getTargetElement())))
                    {
                        toDoGUIDs.add(actionTargetElement.getActionTargetGUID());
                    }
                }
            }
        }

        return toDoGUIDs;
    }


    /**
     * Return whether the element is a ToDo, or one of its subtypes.
     *
     * @param element element from an action target
     * @return boolean
     */
    private boolean isToDo(OpenMetadataElement element)
    {
        if ((element != null) && (element.getType() != null))
        {
            return (OpenMetadataType.TO_DO.typeName.equals(element.getType().getTypeName())) ||
                    ((element.getType().getSuperTypeNames() != null) &&
                            (element.getType().getSuperTypeNames().contains(OpenMetadataType.TO_DO.typeName)));
        }

        return false;
    }


    /**
     * Retrieve the current version of a to do.
     *
     * @param toDoGUID unique identifier of the to do
     * @return element, or null if it no longer exists
     * @throws UserNotAuthorizedException the service is not authorized to read the to do
     * @throws PropertyServerException a problem connecting to the metadata store
     */
    private OpenMetadataElement getToDo(String toDoGUID) throws UserNotAuthorizedException,
                                                                PropertyServerException
    {
        try
        {
            return watchdogContext.getOpenMetadataStore().getMetadataElementByGUID(toDoGUID);
        }
        catch (InvalidParameterException unknownGUID)
        {
            /*
             * The to do has been deleted since it was passed to this service.
             */
            return null;
        }
    }


    /**
     * Record the to do as finished if its activity status shows that the steward has finished with it.
     *
     * @param toDoGUID unique identifier of the to do
     * @param toDoProperties current properties of the to do
     */
    private void checkToDoStatus(String            toDoGUID,
                                 ElementProperties toDoProperties)
    {
        final String methodName = "checkToDoStatus";

        String activityStatusName = propertyHelper.getEnumPropertySymbolicName(watchdogActionServiceName,
                                                                               OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                               toDoProperties,
                                                                               methodName);

        for (ActivityStatus activityStatus : FINISHED_ACTIVITY_STATUSES)
        {
            if (activityStatus.name().equals(activityStatusName))
            {
                this.recordFinishedToDo(toDoGUID, activityStatus);
                return;
            }
        }
    }


    /**
     * Move the to do from the unfinished set to the set that describes how it finished.
     *
     * @param toDoGUID unique identifier of the to do
     * @param activityStatus final activity status of the to do, or null if it has been deleted
     */
    private void recordFinishedToDo(String         toDoGUID,
                                    ActivityStatus activityStatus)
    {
        unfinishedToDoGUIDs.remove(toDoGUID);

        if (activityStatus == ActivityStatus.COMPLETED)
        {
            completedToDoGUIDs.add(toDoGUID);
        }
        else
        {
            notCompletedToDoGUIDs.add(toDoGUID);
        }
    }


    /**
     * Complete the service once every to do has finished.  The guard shows whether they were all completed.
     *
     * @param methodName calling method
     */
    private void reportFinishedToDos(String methodName)
    {
        WaitForStewardGuard       guard;
        AuditLogMessageDefinition messageDefinition;

        if (notCompletedToDoGUIDs.isEmpty())
        {
            guard = WaitForStewardGuard.TO_DOS_COMPLETED;
            messageDefinition = GovernanceActionConnectorsAuditCode.TO_DOS_COMPLETED.getMessageDefinition(watchdogActionServiceName,
                                                                                                          completedToDoGUIDs.toString());
        }
        else
        {
            guard = WaitForStewardGuard.TO_DOS_NOT_COMPLETED;
            messageDefinition = GovernanceActionConnectorsAuditCode.TO_DOS_NOT_COMPLETED.getMessageDefinition(watchdogActionServiceName,
                                                                                                              notCompletedToDoGUIDs.toString());
        }

        logRecord(methodName, messageDefinition);
        this.completeWaiting(guard, messageDefinition);
    }


    /**
     * Record the completion status of the service and stop listening for events.  Once complete, any further
     * events that were already queued are ignored.
     *
     * @param guard guard that describes the outcome
     * @param messageDefinition message describing the outcome
     */
    private void completeWaiting(WaitForStewardGuard       guard,
                                 AuditLogMessageDefinition messageDefinition)
    {
        final String methodName = "completeWaiting";

        completed = true;

        try
        {
            watchdogContext.recordCompletionStatus(guard.getCompletionStatus(),
                                                   Collections.singletonList(guard.getName()),
                                                   null,
                                                   null,
                                                   messageDefinition);
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(watchdogActionServiceName,
                                                                                                                        error.getClass().getName(),
                                                                                                                        error.getMessage()),
                               error);
        }

        watchdogContext.disconnectListener();
    }
}
