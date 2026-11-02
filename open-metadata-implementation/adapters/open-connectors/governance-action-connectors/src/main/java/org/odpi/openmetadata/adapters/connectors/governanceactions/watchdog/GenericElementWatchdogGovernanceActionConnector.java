/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogRequestParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GenericElementWatchdogGovernanceActionConnector is a Watchdog Action Service that listens for changes to
 * open metadata elements and relationships and kicks off a governance action process for each one.
 * With the process-multiple-events request type it runs continuously and does not set up its completion status.
 * This means its engine action is never completed and this service is restarted each time the hosting engine is restarted.
 * With the process-single-event request type it completes once the first matching event is processed.
 */
public class GenericElementWatchdogGovernanceActionConnector extends GenericWatchdogGovernanceActionConnector
{
    private String requestType = null;

    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        requestType = watchdogContext.getRequestType();

        super.startMonitoring(OpenMetadataType.OPEN_METADATA_ROOT.typeName);
    }


    /**
     * This method is called each time a potentially new asset is received.  It triggers a governance action process to validate and
     * enrich the asset as required.  Events are handled one at a time so that, for process-single-event, only
     * one of several events arriving together starts a process.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    @Override
    public synchronized void processEvent(OpenMetadataOutTopicEvent event) throws GovernanceServiceException
    {
        final String methodName = "processEvent";

        if (! completed)
        {
            /*
             * Only an event that starts a process counts as the single event.  Others, such as refresh events
             * or classifications with no process configured, leave the watchdog waiting.
             */
            boolean processStarted = false;

            try
            {
                if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.ENTITY_DEF)
                {
                    String elementGUID = event.getElementHeader().getGUID();

                    Map<String, String>   requestParameters = new HashMap<>();
                    List<NewActionTarget> actionTargets     = new ArrayList<>();

                    NewActionTarget actionTarget = new NewActionTarget();

                    actionTarget.setActionTargetName(actionTargetName);
                    actionTarget.setActionTargetGUID(elementGUID);
                    actionTargets.add(actionTarget);

                    if ((instancesToListenTo == null) || (instancesToListenTo.contains(elementGUID)))
                    {
                        if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                        {
                            processStarted = initiateProcess(newElementProcessName,
                                                             null,
                                                             actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
                        {
                            requestParameters.put(WatchdogRequestParameter.CHANGED_PROPERTIES.getName(),
                                                  this.diffProperties(event.getPreviousElementProperties(),
                                                                      event.getElementProperties()));

                            processStarted = initiateProcess(updatedElementProcessName,
                                                             requestParameters,
                                                             actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            /*
                             * A deleted element cannot be an action target, so it is identified through the
                             * request parameters instead.
                             */
                            requestParameters.put("ElementGUID", elementGUID);
                            requestParameters.put("ElementTypeName", event.getElementHeader().getType().getTypeName());

                            processStarted = initiateProcess(deletedElementProcessName,
                                                             requestParameters,
                                                             null);
                        }
                        else
                        {
                            requestParameters.put("ClassificationName", event.getClassificationName());

                            if (event.getEventType() == OpenMetadataEventType.ELEMENT_CLASSIFIED)
                            {
                                processStarted = initiateProcess(classifiedElementProcessName,
                                                                 requestParameters,
                                                                 actionTargets);
                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_RECLASSIFIED)
                            {
                                requestParameters.put(WatchdogRequestParameter.CHANGED_PROPERTIES.getName(), this.diffProperties(event.getPreviousClassificationProperties(),
                                                                                               event.getClassificationProperties()));

                                processStarted = initiateProcess(reclassifiedElementProcessName,
                                                                 requestParameters,
                                                                 actionTargets);

                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DECLASSIFIED)
                            {
                                processStarted = initiateProcess(declassifiedElementProcessName,
                                                                 requestParameters,
                                                                 actionTargets);
                            }
                        }
                    }
                }
                else // Relationship event
                {
                    String end1GUID = event.getEndOneElementHeader().getGUID();
                    String end2GUID = event.getEndTwoElementHeader().getGUID();

                    if ((instancesToListenTo == null) ||
                            (instancesToListenTo.contains(end1GUID)) ||
                            (instancesToListenTo.contains(end2GUID)))
                    {
                        Map<String, String> requestParameters = new HashMap<>();

                        requestParameters.put("RelationshipGUID", event.getElementHeader().getGUID());
                        requestParameters.put("RelationshipTypeName", event.getElementHeader().getType().getTypeName());

                        List<NewActionTarget> actionTargets = new ArrayList<>();

                        NewActionTarget actionTarget = new NewActionTarget();

                        actionTarget.setActionTargetName(actionTargetName);
                        actionTarget.setActionTargetGUID(end1GUID);
                        actionTargets.add(actionTarget);

                        actionTarget = new NewActionTarget();

                        actionTarget.setActionTargetName(actionTargetTwoName);
                        actionTarget.setActionTargetGUID(end2GUID);
                        actionTargets.add(actionTarget);

                        if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                        {
                            processStarted = initiateProcess(newRelationshipProcessName,
                                                             requestParameters,
                                                             actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
                        {
                            requestParameters.put(WatchdogRequestParameter.CHANGED_PROPERTIES.getName(), this.diffProperties(event.getPreviousElementProperties(),
                                                                                           event.getElementProperties()));

                            processStarted = initiateProcess(updatedRelationshipProcessName,
                                                             requestParameters,
                                                             actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            processStarted = initiateProcess(deletedRelationshipProcessName,
                                                             requestParameters,
                                                             actionTargets);
                        }
                    }
                }
            }
            catch (Exception error)
            {
                /*
                 * A watchdog waiting for a single event has failed.  One that processes multiple events logs
                 * the error and carries on with the next event.
                 */
                if (GenericElementRequestType.PROCESS_SINGLE_EVENT.getRequestType().equals(requestType))
                {
                    completeMonitoring(WatchdogActionGuard.MONITORING_FAILED, error.getMessage());
                }
                else
                {
                    logExceptionRecord(methodName,
                                       GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                       error);
                }

                return;
            }

            if (processStarted && (GenericElementRequestType.PROCESS_SINGLE_EVENT.getRequestType().equals(requestType)))
            {
                completeMonitoring(WatchdogActionGuard.MONITORING_COMPLETED, null);
            }
        }
    }
}
