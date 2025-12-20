/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openwatchdog;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceCompletionInterface;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.opengovernance.client.WatchdogEventInterface;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openwatchdog.handlers.NotificationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * WatchdogContext provides a watchdog action service with access to information about
 * the watchdog request along with access to the open metadata repository interfaces.
 */
public class WatchdogContext extends ConnectorContextBase implements WatchdogEventInterface
{
    private final String                    notificationTypeGUID;
    private final String                    requestType;
    private final Map<String, String>       requestParameters;
    private final List<ActionTargetElement> actionTargetElements;
    private final String                    watchdogActionServiceName;
    private final String                    requesterUserId;
    private final AuditLog                  auditLog;

    private final NotificationHandler notificationHandler;
    private final MessageFormatter    messageFormatter = new MessageFormatter();


    private final WatchdogEventInterface        watchdogEventClient;
    private final GovernanceCompletionInterface governanceCompletionClient;


    /*
     * Values set by the watchdog action service for completion.
     */
    private CompletionStatus          completionStatus            = null;
    private final String             engineActionGUID;




    /**
     * Constructor sets up the key parameters for using the context.
     *
     * @param localServerName name of local server
     * @param localServiceName name of the service to call
     * @param externalSourceGUID metadata collection unique id
     * @param externalSourceName metadata collection unique name
     * @param connectorId id of this connector instance
     * @param connectorName name of this connector instance
     * @param connectorUserId userId to use when issuing open metadata requests
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param generateIntegrationReport should the context generate an integration report?
     * @param openMetadataClient client to access open metadata store
     * @param openGovernanceClient client to the open governance services for use by the governance action service
     * @param governanceCompletionClient client to the open governance services for use by the governance action service
     * @param watchdogEventClient client to the open governance services for use by the governance action service
     * @param auditLog logging destination
     * @param maxPageSize max number of results
     * @param deleteMethod default delete method
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param notificationTypeGUID unique identifier of the notification type to process
     * @param requestType request type used to initiate the watchdog action service
     * @param requestParameters name-value properties to control the watchdog action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param watchdogActionServiceName name of the running service
     * @param requesterUserId original user requesting this governance service
     */
    public WatchdogContext(String                        localServerName,
                           String                        localServiceName,
                           String                        externalSourceGUID,
                           String                        externalSourceName,
                           String                        connectorId,
                           String                        connectorName,
                           String                        connectorUserId,
                           String                        connectorGUID,
                           boolean                       generateIntegrationReport,
                           OpenMetadataClient            openMetadataClient,
                           OpenGovernanceClient          openGovernanceClient,
                           GovernanceCompletionInterface governanceCompletionClient,
                           WatchdogEventInterface        watchdogEventClient,
                           AuditLog                      auditLog,
                           int                           maxPageSize,
                           DeleteMethod                  deleteMethod,
                           String                        engineActionGUID,
                           String                        notificationTypeGUID,
                           String                        requestType,
                           Map<String, String>           requestParameters,
                           List<ActionTargetElement>     actionTargetElements,
                           String                        watchdogActionServiceName,
                           String                        requesterUserId)
    {
        super(localServerName,
              localServiceName,
              externalSourceGUID,
              externalSourceName,
              connectorId,
              connectorName,
              connectorUserId,
              connectorGUID,
              generateIntegrationReport,
              openMetadataClient,
              auditLog,
              maxPageSize,
              deleteMethod);

        this.notificationHandler        = new NotificationHandler(localServerName, auditLog, localServiceName, openMetadataClient, openGovernanceClient);
        this.notificationTypeGUID       = notificationTypeGUID;
        this.governanceCompletionClient = governanceCompletionClient;
        this.watchdogEventClient        = watchdogEventClient;
        this.engineActionGUID           = engineActionGUID;
        this.requestType                = requestType;
        this.requestParameters          = requestParameters;
        this.actionTargetElements       = actionTargetElements;
        this.watchdogActionServiceName  = watchdogActionServiceName;
        this.requesterUserId            = requesterUserId;
        this.auditLog                   = auditLog;
    }


    /**
     * Return the unique identifier of the associated notification.
     *
     * @return string guid
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public String getNotificationTypeGUID() throws UserNotAuthorizedException
    {
        final String methodName = "getNotificationTypeGUID";

        validateIsActive(methodName);

        return notificationTypeGUID;
    }


    /**
     * Return details of notification type to monitor along with its linked resources and subscribers, the actions it
     * has caused and any additional context.
     *
     * @return string guid
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    public OpenMetadataRootElement getNotificationType() throws UserNotAuthorizedException,
                                                                InvalidParameterException,
                                                                PropertyServerException
    {
        final String methodName = "getNotificationType";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            return this.governanceDefinitionClient.getGovernanceDefinitionByGUID(notificationTypeGUID,
                                                                                 openMetadataStore.getQueryOptions());
        }

        return null;
    }


    /**
     * Return an integer request parameter the default value is used if the request parameter is not provided.
     *
     * @param requestParameterName name of the request parameter
     * @param defaultValue value to ues if the request parameter is not set
     * @return int
     */
    public int getIntRequestParameter(String requestParameterName, int defaultValue)
    {
        int requestParameterValue = defaultValue;

        if ((this.requestParameters != null) && (this.requestParameters.containsKey(requestParameterName)))
        {
            requestParameterValue = Integer.parseInt(this.requestParameters.get(requestParameterName));
        }

        return requestParameterValue;
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog action service can change the
     * listener and the parameters that control the types of events received while it is running.
     * <br><br>
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     * <br><br>
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     * The type name specified in the interestingMetadataTypes refers to the subject of the event - so it is the type of the metadata element
     * for metadata element types, the type of the relationship for related elements events and the name of the classification
     * for classification events.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance (metadata element or relationship) to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public void registerListener(WatchdogGovernanceListener  listener,
                                 List<OpenMetadataEventType> interestingEventTypes,
                                 List<String>                interestingMetadataTypes,
                                 String                      specificInstance) throws InvalidParameterException
    {
        watchdogEventClient.registerListener(listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    @Override
    public void disconnectListener()
    {
        watchdogEventClient.disconnectListener();
    }


    /**
     * Convert the message definitions into properties for a notification.  This includes setting
     * the qualified name and status.
     *
     * @param notificationDescription description of the notification
     * @return notification properties
     */
    public NotificationProperties getNotificationProperties(MessageDefinition notificationDescription)
    {
        NotificationProperties notificationProperties = new NotificationProperties();

        notificationProperties.setQualifiedName(notificationProperties.getTypeName() + "::"  + connectorName + "::" + notificationTypeGUID + "::" + new Date());

        if (notificationDescription != null)
        {
            notificationProperties.setActivityStatus(ActivityStatus.FOR_INFO);
            notificationProperties.setDisplayName(messageFormatter.getFormattedMessage(notificationDescription));
            notificationProperties.setSituation(notificationDescription.getSystemAction());
            notificationProperties.setExpectedBehaviour(notificationDescription.getUserAction());
        }

        return  notificationProperties;
    }


    /**
     * Return the elements that are linked to the notification type as monitored resources.
     *
     * @return string guid
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    public List<OpenMetadataRootElement> getMonitoredResources() throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
    {
        final String methodName = "getMonitoredResources";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            return this.notificationHandler.getMonitoredResources(connectorUserId,
                                                                  notificationTypeGUID,
                                                                  openMetadataStore.getQueryOptions());
        }

        return null;
    }


    /**
     * Return the elements that are linked to the notification type as a subscriber.
     *
     * @return string guid
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    public List<OpenMetadataRootElement> getNotificationSubscribers() throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
        final String methodName = "getNotificationSubscribers";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            return this.notificationHandler.getNotificationSubscribers(connectorUserId,
                                                                       notificationTypeGUID,
                                                                       openMetadataStore.getQueryOptions());
        }

        return null;
    }


    /**
     * Create a notification/action for one of the subscribers.
     *
     * @param subscriberGUID unique identifier of the subscriber
     * @param notificationProperties properties for the notification
     * @param requestParameters properties to pass to the next governance service
     * @param newActionTargets map of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void notifySubscriber(String                 subscriberGUID,
                                 NotificationProperties notificationProperties,
                                 Map<String, String>    requestParameters,
                                 List<NewActionTarget>  newActionTargets)  throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "notifySubscriber";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            this.notificationHandler.notifySubscriber(connectorUserId,
                                                      subscriberGUID,
                                                      notificationProperties,
                                                      notificationTypeGUID,
                                                      requestParameters,
                                                      connectorGUID,
                                                      newActionTargets);
        }
    }


    /**
     * Create a notification/action for each of the subscribers.
     *
     * @param notificationProperties properties for the notification
     * @param requestParameters properties to pass to the next governance service
     * @param newActionTargets map of action target names to GUIDs for the resulting engine action
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void notifySubscribers(NotificationProperties notificationProperties,
                                  Map<String, String>    requestParameters,
                                  List<NewActionTarget>  newActionTargets,
                                  ActivityStatus         newSubscriberStatus) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "notifySubscribers";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            this.notificationHandler.notifySubscribers(connectorUserId,
                                                       notificationProperties,
                                                       notificationTypeGUID,
                                                       requestParameters,
                                                       connectorGUID,
                                                       newActionTargets,
                                                       newSubscriberStatus);
        }
    }


    /**
     * Return the request type that was used to start this watchdog action service.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public String getRequestType() throws UserNotAuthorizedException
    {
        final String methodName = "getRequestType";

        validateIsActive(methodName);

        return requestType;
    }


    /**
     * Return the properties that hold the parameters used to drive this watchdog action service.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public Map<String, String> getRequestParameters() throws UserNotAuthorizedException
    {
        final String methodName = "getRequestParameters";

        validateIsActive(methodName);

        return requestParameters;
    }


    /**
     * Return the list of elements that this watchdog action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Return the requester user identifier.
     *
     * @return userId
     */
    public String getRequesterUserId()
    {
        return requesterUserId;
    }


    /**
     * Declare that all the processing for the governance service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters properties to pass to the next governance service
     * @param newActionTargets map of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(CompletionStatus          status,
                                       List<String>              outputGuards,
                                       Map<String, String>       newRequestParameters,
                                       List<NewActionTarget>     newActionTargets,
                                       AuditLogMessageDefinition completionMessage) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        if (completionMessage != null)
        {
            this.recordCompletionStatus(status,
                                        outputGuards,
                                        newRequestParameters,
                                        newActionTargets,
                                        messageFormatter.getFormattedMessage(completionMessage));
        }
        else
        {
            this.recordCompletionStatus(status,
                                        outputGuards,
                                        newRequestParameters,
                                        newActionTargets,
                                        (String) null);
        }
    }


    /**
     * Declare that all the processing for the watchdog action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public  void recordCompletionStatus(CompletionStatus      status,
                                        List<String>          outputGuards,
                                        Map<String, String>   newRequestParameters,
                                        List<NewActionTarget> newActionTargets,
                                        String                completionMessage) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        this.completionStatus = status;

        Map<String, String> combinedRequestParameters = new HashMap<>();

        if (requestParameters != null)
        {
            combinedRequestParameters.putAll(requestParameters);
        }

        if (newRequestParameters != null)
        {
            combinedRequestParameters.putAll(newRequestParameters);
        }

        governanceCompletionClient.recordCompletionStatus(connectorUserId,
                                                          engineActionGUID,
                                                          combinedRequestParameters,
                                                          status,
                                                          outputGuards,
                                                          newActionTargets,
                                                          completionMessage);
    }


    /**
     * Return the watchdog action service.
     *
     * @return qualified name
     */
    public String getWatchdogActionServiceName()
    {
        return watchdogActionServiceName;
    }


    /**
     * Return the completion status provided by the watchdog action service.
     *
     * @return enum
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogContext{" +
                "notificationTypeGUID='" + notificationTypeGUID + '\'' +
                ", requestParameters=" + requestParameters +
                ", actionTargetElements=" + actionTargetElements +
                ", watchdogActionServiceName='" + watchdogActionServiceName + '\'' +
                ", requesterUserId='" + requesterUserId + '\'' +
                ", auditLog=" + auditLog +
                ", completionStatus=" + completionStatus +
                "} " + super.toString();
    }
}
