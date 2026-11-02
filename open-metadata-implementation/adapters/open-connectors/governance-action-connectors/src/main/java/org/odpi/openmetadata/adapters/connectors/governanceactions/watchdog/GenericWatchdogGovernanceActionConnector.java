/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogRequestParameter;

import java.util.*;

/**
 * GenericWatchdogGovernanceActionConnector provides a base class for generic watchdog functions.
 * It runs as a watchdog action service in a watchdog action engine, such as EgeriaWatchdog.
 */
public abstract class GenericWatchdogGovernanceActionConnector extends WatchdogActionServiceConnector
{
    List<String> instancesToListenTo = new ArrayList<>();

    String actionTargetName = null;
    String actionTargetTwoName = null;
    String newElementProcessName = null;
    String updatedElementProcessName = null;
    String deletedElementProcessName = null;
    String classifiedElementProcessName = null;
    String reclassifiedElementProcessName = null;
    String declassifiedElementProcessName = null;
    String newRelationshipProcessName = null;
    String updatedRelationshipProcessName = null;
    String deletedRelationshipProcessName = null;

    volatile boolean completed = false;


    /**
     * Retrieve a property value from the request parameters, falling back to the configuration properties
     * of the connection and then the default value.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use if the property is not set
     * @return property value
     * @throws UserNotAuthorizedException the connector was disconnected
     */
    protected String getProperty(String propertyName, String defaultValue) throws UserNotAuthorizedException
    {
        Map<String, String> requestParameters       = watchdogContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            return requestParameters.get(propertyName);
        }

        if ((configurationProperties != null) && (configurationProperties.get(propertyName) != null))
        {
            return configurationProperties.get(propertyName).toString();
        }

        return defaultValue;
    }


    /**
     * Called from the subclass's start() method, after super.start(), to register the listener.  The service
     * then runs until it records its completion status, or it is stopped by an administrator or the
     * hosting server shutting down.
     *
     * @param defaultTypeName type of element to monitor if the interestingTypeName property is not set
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     */
    void startMonitoring(String defaultTypeName) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "startMonitoring";

        /*
         * Work out what type of element to listen for.
         */
        String interestingTypeName = getProperty(WatchdogRequestParameter.INTERESTING_TYPE_NAME.getName(), defaultTypeName);
        String instanceToListenTo = getProperty(WatchdogRequestParameter.INSTANCE_TO_MONITOR.getName(), null);

        if (instanceToListenTo != null)
        {
            instancesToListenTo.add(instanceToListenTo);
        }

        List<ActionTargetElement> actionTargetElements = watchdogContext.getActionTargetElements();
        if (actionTargetElements != null)
        {
            for (ActionTargetElement actionTargetElement : actionTargetElements)
            {
                if (actionTargetElement != null)
                {
                    if (WatchdogActionTarget.INSTANCE_TO_MONITOR.getName().equals(actionTargetElement.getActionTargetName()))
                    {
                        instancesToListenTo.add(actionTargetElement.getActionTargetGUID());
                    }
                }
            }
        }

        actionTargetName = getProperty(WatchdogRequestParameter.ACTION_TARGET_NAME.getName(), "receivedElement");
        actionTargetTwoName = getProperty(WatchdogRequestParameter.ACTION_TARGET_TWO_NAME.getName(), "receivedElementTwo");
        newElementProcessName = getProperty(WatchdogRequestParameter.NEW_ELEMENT_PROCESS_NAME.getName(), null);
        updatedElementProcessName = getProperty(WatchdogRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName(), null);
        deletedElementProcessName = getProperty(WatchdogRequestParameter.DELETED_ELEMENT_PROCESS_NAME.getName(), null);
        classifiedElementProcessName = getProperty(WatchdogRequestParameter.CLASSIFIED_ELEMENT_PROCESS_NAME.getName(), null);
        reclassifiedElementProcessName = getProperty(WatchdogRequestParameter.RECLASSIFIED_ELEMENT_PROCESS_NAME.getName(), null);
        declassifiedElementProcessName = getProperty(WatchdogRequestParameter.DECLASSIFIED_ELEMENT_PROCESS_NAME.getName(), null);
        newRelationshipProcessName = getProperty(WatchdogRequestParameter.NEW_RELATIONSHIP_PROCESS_NAME.getName(), null);
        updatedRelationshipProcessName = getProperty(WatchdogRequestParameter.UPDATED_RELATIONSHIP_PROCESS_NAME.getName(), null);
        deletedRelationshipProcessName = getProperty(WatchdogRequestParameter.DELETED_RELATIONSHIP_PROCESS_NAME.getName(), null);

        try
        {
            /*
             * Setting up the listener
             */
            List<OpenMetadataEventType> interestingEventTypes = new ArrayList<>();

            if (newElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.NEW_ELEMENT_CREATED);
                interestingEventTypes.add(OpenMetadataEventType.REFRESH_ELEMENT_EVENT);
            }
            if (updatedElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_UPDATED);
            }
            if (deletedElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_DELETED);
            }
            if (classifiedElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_CLASSIFIED);
            }
            if (reclassifiedElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_RECLASSIFIED);
            }
            if (declassifiedElementProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_DECLASSIFIED);
            }
            if (newRelationshipProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.NEW_ELEMENT_CREATED);
            }
            if (updatedRelationshipProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_UPDATED);
            }
            if (deletedRelationshipProcessName != null)
            {
                interestingEventTypes.add(OpenMetadataEventType.ELEMENT_DELETED);
            }

            List<String> interestingMetadataTypes = new ArrayList<>();

            interestingMetadataTypes.add(interestingTypeName);

            GenericWatchdogActionListener listener = new GenericWatchdogActionListener(this);

            if (instancesToListenTo.isEmpty())
            {
                instancesToListenTo = null;

                watchdogContext.registerListener(listener,
                                                 interestingEventTypes,
                                                 interestingMetadataTypes,
                                                 null);
            }
            else if (instancesToListenTo.size() == 1)
            {
                watchdogContext.registerListener(listener,
                                                 interestingEventTypes,
                                                 interestingMetadataTypes,
                                                 instancesToListenTo.get(0));

                instancesToListenTo = null;
            }
            else
            {
                /*
                 * Will need to filter manually in the listener
                 */
                watchdogContext.registerListener(listener,
                                                 interestingEventTypes,
                                                 interestingMetadataTypes,
                                                 null);
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               GovernanceActionConnectorsAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                               error);

            completeMonitoring(WatchdogActionGuard.MONITORING_FAILED, error.getMessage());

            throw new GovernanceServiceException(GovernanceActionConnectorsErrorCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }
    }


    /**
     * Record that the monitoring is over and stop listening for events.  Once complete, any further
     * events that were already queued are ignored.
     *
     * @param guard guard that describes the outcome
     * @param completionMessage optional message describing the outcome
     */
    protected void completeMonitoring(WatchdogActionGuard guard,
                                      String              completionMessage)
    {
        final String methodName = "completeMonitoring";

        completed = true;

        try
        {
            watchdogContext.recordCompletionStatus(guard.getCompletionStatus(),
                                                   Collections.singletonList(guard.getName()),
                                                   null,
                                                   null,
                                                   completionMessage);
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


    /**
     * Return a comma separated list of properties that have changed.  This is saved in the request parameters
     * and can be retrieved by the receiving governance service.  The changed properties can be retrieved by the
     * StringTokenizer class.
     *
     * @param oldProperties original property values
     * @param newProperties new property values
     * @return comma separated list of property names where the value has changed
     */
    protected String diffProperties(ElementProperties oldProperties,
                                    ElementProperties newProperties)
    {
        StringBuilder propertyList = null;

        if (oldProperties == null)
        {
            if (newProperties != null)
            {
                Iterator<String> propertyIterator = newProperties.getPropertyNames();

                while (propertyIterator.hasNext())
                {
                    if (propertyList == null)
                    {
                        propertyList = new StringBuilder(propertyIterator.next());
                    }
                    else
                    {
                        propertyList.append(", ").append(propertyIterator.next());
                    }
                }
            }
        }
        else if (newProperties == null)
        {
            Iterator<String> propertyIterator = oldProperties.getPropertyNames();

            while (propertyIterator.hasNext())
            {
                if (propertyList == null)
                {
                    propertyList = new StringBuilder(propertyIterator.next());
                }
                else
                {
                    propertyList.append(", ").append(propertyIterator.next());
                }
            }
        }
        else
        {
            List<String> oldPropertyNames = new ArrayList<>();
            List<String> newPropertyNames = new ArrayList<>();

            Iterator<String> propertyIterator = oldProperties.getPropertyNames();

            while (propertyIterator.hasNext())
            {
                oldPropertyNames.add(propertyIterator.next());
            }

            propertyIterator = newProperties.getPropertyNames();

            while (propertyIterator.hasNext())
            {
                String newPropertyName = propertyIterator.next();

                newPropertyNames.add(newPropertyName);

                /*
                 * A property that is new, or whose value has changed.
                 */
                if ((! oldPropertyNames.contains(newPropertyName)) ||
                        (! Objects.equals(oldProperties.getPropertyValue(newPropertyName), newProperties.getPropertyValue(newPropertyName))))
                {
                    if (propertyList == null)
                    {
                        propertyList = new StringBuilder(newPropertyName);
                    }
                    else
                    {
                        propertyList.append(", ").append(newPropertyName);
                    }
                }
            }

            for (String oldPropertyName : oldPropertyNames)
            {
                if (! newPropertyNames.contains(oldPropertyName))
                {
                    if (propertyList == null)
                    {
                        propertyList = new StringBuilder(oldPropertyName);
                    }
                    else
                    {
                        propertyList.append(", ").append(oldPropertyName);
                    }
                }
            }
        }

        if (propertyList != null)
        {
            return propertyList.toString();
        }

        return null;
    }


    /**
     * Initiates the request for the new process to start if a process name has been configured.
     *
     * @param processName name of the process to start
     * @param requestParameters parameters to pass to the process
     * @param actionTargets the elements that this process should work on
     * @return true if a process was started; false if no process is configured for this kind of event
     * @throws GovernanceServiceException unable to start the process
     */
    protected boolean initiateProcess(String                processName,
                                   Map<String, String>   requestParameters,
                                   List<NewActionTarget> actionTargets) throws GovernanceServiceException
    {
        final String methodName = "initiateProcess";

        if (processName != null)
        {
            String requestParametersString = "<null>";
            if (requestParameters != null)
            {
                requestParametersString = requestParameters.toString();
            }

            String actionTargetsString = "<null>";
            if (actionTargets != null)
            {
                actionTargetsString = actionTargets.toString();
            }

            try
            {
                logRecord(methodName,
                          GovernanceActionConnectorsAuditCode.INITIATE_PROCESS.getMessageDefinition(watchdogActionServiceName,
                                                                                                    processName,
                                                                                                    requestParametersString,
                                                                                                    actionTargetsString));

                watchdogContext.initiateGovernanceActionProcess(processName,
                                                                requestParameters,
                                                                null,
                                                                null,
                                                                actionTargets,
                                                                null,
                                                                null);
            }
            catch (OMFCheckedExceptionBase error)
            {
                logExceptionRecord(methodName,
                                   GovernanceActionConnectorsAuditCode.INITIATE_PROCESS_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       processName,
                                                                                                                       requestParametersString,
                                                                                                                       actionTargetsString,
                                                                                                                       error.getMessage()),
                                   error);

                throw new GovernanceServiceException(error.getMessage(), error);
            }

            return true;
        }

        return false;
    }
}
