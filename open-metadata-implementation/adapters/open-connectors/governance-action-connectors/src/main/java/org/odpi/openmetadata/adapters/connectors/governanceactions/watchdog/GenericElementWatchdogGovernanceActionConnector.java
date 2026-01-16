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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * MonitorForNewAssetGovernanceActionConnector is a Watchdog Governance Action Service that listens for new or refreshed Assets
 * and kicks off a governance action process to validate that this Asset is correctly set up.
 * It is designed to run continuously and so does not set up completion status or guards.  This means its
 * Governance Action entity is never completed and this service is restarted each time the hosting engine is restarted.
 */
public class GenericElementWatchdogGovernanceActionConnector extends GenericWatchdogGovernanceActionConnector
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start(OpenMetadataType.OPEN_METADATA_ROOT.typeName);
    }


    /**
     * This method is called each time a potentially new asset is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    void processEvent(OpenMetadataOutTopicEvent event) throws GovernanceServiceException
    {
        final String methodName = "processEvent";

        if (! completed)
        {
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
                            initiateProcess(newElementProcessName,
                                            null,
                                            actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
                        {
                            requestParameters.put(GenericElementRequestParameter.CHANGED_PROPERTIES.getName(),
                                                  this.diffProperties(event.getPreviousElementProperties(),
                                                                      event.getElementProperties()));

                            initiateProcess(updatedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            initiateProcess(deletedElementProcessName,
                                            null,
                                            actionTargets);
                        }
                        else
                        {
                            requestParameters.put("ClassificationName", event.getClassificationName());

                            if (event.getEventType() == OpenMetadataEventType.ELEMENT_CLASSIFIED)
                            {
                                initiateProcess(classifiedElementProcessName,
                                                requestParameters,
                                                actionTargets);
                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_RECLASSIFIED)
                            {
                                requestParameters.put("ChangedProperties", this.diffProperties(event.getPreviousClassificationProperties(),
                                                                                               event.getClassificationProperties()));

                                initiateProcess(reclassifiedElementProcessName,
                                                requestParameters,
                                                actionTargets);

                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DECLASSIFIED)
                            {
                                initiateProcess(declassifiedElementProcessName,
                                                requestParameters,
                                                actionTargets);
                            }
                        }

                        if (GenericElementRequestType.PROCESS_SINGLE_EVENT.getRequestType().equals(governanceContext.getRequestType()))
                        {
                            completed = true;
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
                            initiateProcess(newRelationshipProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
                        {
                            requestParameters.put("ChangedProperties", this.diffProperties(event.getPreviousElementProperties(),
                                                                                           event.getElementProperties()));

                            initiateProcess(updatedRelationshipProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            initiateProcess(deletedRelationshipProcessName,
                                            requestParameters,
                                            actionTargets);
                        }

                        if (GenericElementRequestType.PROCESS_SINGLE_EVENT.getRequestType().equals(governanceContext.getRequestType()))
                        {
                            completed = true;

                        }
                    }
                }
            }
            catch (Exception error)
            {
                try
                {
                    List<String> outputGuards = new ArrayList<>();
                    outputGuards.add(GenericWatchdogGuard.MONITORING_FAILED.getName());

                    governanceContext.recordCompletionStatus(GenericWatchdogGuard.MONITORING_FAILED.getCompletionStatus(), outputGuards, null, null, error.getMessage());
                }
                catch (Exception completionError)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(governanceServiceName,
                                                                                                                                       completionError.getClass().getName(),
                                                                                                                                       completionError.getMessage()),
                                              error);
                    }
                }
            }

            if (completed)
            {
                try
                {
                    List<String> outputGuards = new ArrayList<>();
                    outputGuards.add(GenericWatchdogGuard.MONITORING_STOPPED.getName());

                    governanceContext.recordCompletionStatus(GenericWatchdogGuard.MONITORING_STOPPED.getCompletionStatus(), outputGuards);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(governanceServiceName,
                                                                                                                                       error.getClass().getName(),
                                                                                                                                       error.getMessage()),
                                              error);
                    }
                }
            }
        }
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this governance action service stop running or the hosting server is shutting down.
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
