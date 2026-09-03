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
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.opengovernance.handlers.NotificationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFAuditCode;

import java.util.*;


/**
 * WatchdogContext provides a watchdog action service with access to information about
 * the watchdog request along with access to the open metadata repository interfaces.
 */
public class WatchdogContext extends ConnectorContextBase
{
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
    private CompletionStatus completionStatus            = null;
    private final String     engineActionGUID;




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
     * Return details of the notification type to monitor along with its linked resources and subscribers, the actions it
     * has caused and any additional context.
     *
     * @param notificationTypeGUID unique identifier of the notification type to process
     * @return root element
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException a problem communicating with the metadata server (or it has a logic error).
     */
    public OpenMetadataRootElement getNotificationType(String notificationTypeGUID) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           PropertyServerException
    {
        final String methodName = "getNotificationType";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            return this.governanceDefinitionClient.getGovernanceDefinitionByGUID(notificationTypeGUID,
                                                                                 governanceDefinitionClient.getQueryOptions());
        }

        return null;
    }


    /**
     * Update the supplied properties in the governance definition - mergeUpdate=true
     *
     * @param notificationTypeGUID       unique identifier of the notification type to process
     * @param notificationTypeProperties new properties for the notification type
     * @throws InvalidParameterException  an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException    a problem communicating with the metadata server (or it has a logic error).
     */
    public void updateNotificationType(String                     notificationTypeGUID,
                                       NotificationTypeProperties notificationTypeProperties) throws UserNotAuthorizedException,
                                                                                                     InvalidParameterException,
                                                                                                     PropertyServerException
    {
        final String methodName = "updateNotificationType";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            this.governanceDefinitionClient.updateGovernanceDefinition(notificationTypeGUID,
                                                                       governanceDefinitionClient.getUpdateOptions(true),
                                                                       notificationTypeProperties);
        }
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
     * Return the display name from the properties - defaults to qualified name - or Unknown if not present.
     *
     * @param properties properties of the resource
     * @return display name
     */
    public String getDisplayName(ElementProperties properties)
    {
        return governanceDefinitionClient.getDisplayName(properties);
    }


    /**
     * Return the display name from the properties - defaults to qualified name - or Unknown if not present.
     *
     * @param properties properties of the resource
     * @return display name
     */
    public String getDisplayName(OpenMetadataRootProperties properties)
    {
        return governanceDefinitionClient.getDisplayName(properties);
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
    public void registerListener(WatchdogGovernanceListener  listener,
                                 List<OpenMetadataEventType> interestingEventTypes,
                                 List<String>                interestingMetadataTypes,
                                 String                      specificInstance) throws InvalidParameterException
    {
        watchdogEventClient.registerListener(connectorName, listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    public void disconnectListener()
    {
        watchdogEventClient.disconnectListener(connectorName);
    }


    /**
     * Return the action targets the engine action has <b>now</b>, rather than the ones it had when this
     * service was started.
     * <br>
     * A watchdog is long-running and is given work by having action targets attached to its engine action
     * while it runs - which is how a notification type created after the watchdog started reaches it.  The
     * list passed to the constructor is a snapshot taken at start-up, so a watchdog that read only that would
     * never see any of them: it would monitor whatever existed when it began and nothing afterwards, for the
     * life of the engine action.
     * <br>
     * If the current targets cannot be read - the repository is unreachable, say - the start-up list is
     * returned rather than nothing, so a transient failure narrows what the watchdog sees rather than
     * stopping it.
     *
     * @return action targets
     */
    private List<ActionTargetElement> getCurrentActionTargets()
    {
        final String methodName = "getCurrentActionTargets";

        if (engineActionGUID == null)
        {
            return actionTargetElements;
        }

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(engineActionGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                                      new QueryOptions());

            if ((relatedElements == null) || (relatedElements.getElementList() == null))
            {
                return actionTargetElements;
            }

            List<ActionTargetElement> currentActionTargets = new ArrayList<>();

            for (RelatedMetadataElement relatedElement : relatedElements.getElementList())
            {
                if (relatedElement != null)
                {
                    ActionTargetElement actionTargetElement = new ActionTargetElement();

                    actionTargetElement.setActionTargetGUID(relatedElement.getElement().getElementGUID());
                    actionTargetElement.setActionTargetRelationshipGUID(relatedElement.getRelationshipGUID());
                    actionTargetElement.setActionTargetName(propertyHelper.getStringProperty(watchdogActionServiceName,
                                                                                              OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                              relatedElement.getRelationshipProperties(),
                                                                                              methodName));
                    /*
                     * The status is deliberately left unset.  A null status is already read as "not started
                     * yet" by the caller, which is what a freshly attached action target is, and which makes
                     * it claim the target - the same thing it would do for one attached at start-up.
                     */
                    actionTargetElement.setTargetElement(relatedElement.getElement());

                    currentActionTargets.add(actionTargetElement);
                }
            }

            return currentActionTargets;
        }
        catch (Exception error)
        {
            /*
             * Reported rather than swallowed.  Falling back to the start-up list keeps the service running,
             * but where that list is empty - which it is for a service whose work is all given to it after it
             * starts - the service then monitors nothing at all, and does so silently.  That is
             * indistinguishable from having nothing to do unless this is said out loud.
             */
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OWFAuditCode.UNABLE_TO_READ_ACTION_TARGETS.getMessageDefinition(watchdogActionServiceName,
                                                                                                       engineActionGUID,
                                                                                                       Integer.toString((actionTargetElements == null) ? 0 : actionTargetElements.size()),
                                                                                                       error.getClass().getName(),
                                                                                                       error.getMessage()),
                                      error);
            }

            return actionTargetElements;
        }
    }


    /**
     * Return the notification types that are attached to the engine action.
     *
     * @return list of notification types
     * @throws InvalidParameterException the action target guid is not recognized
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ActionTargetElement> getNotificationTypesFromActionTargets() throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getNotificationTypesFromActionTargets";

        /*
         * Scan the action targets looking for notification types.  If the notification type is not
         * in an appropriate state, it is ignored.
         */
        List<ActionTargetElement> notificationTargetElements = null;
        List<String>              ignoredNotificationTypes = new ArrayList<>();

        List<ActionTargetElement> currentActionTargets = this.getCurrentActionTargets();

        if (currentActionTargets != null)
        {
            notificationTargetElements = new ArrayList<>();

            for (ActionTargetElement actionTargetElement : currentActionTargets)
            {
                if (actionTargetElement != null)
                {
                    if (propertyHelper.isTypeOf(actionTargetElement.getTargetElement(), OpenMetadataType.NOTIFICATION_TYPE.typeName))
                    {
                        if ((actionTargetElement.getStatus() == null) ||
                                (actionTargetElement.getStatus() == ActivityStatus.REQUESTED) ||
                                (actionTargetElement.getStatus() == ActivityStatus.APPROVED) ||
                                (actionTargetElement.getStatus() == ActivityStatus.WAITING) ||
                                (actionTargetElement.getStatus() == ActivityStatus.OTHER) ||
                                (actionTargetElement.getStatus() == ActivityStatus.IN_PROGRESS))
                        {
                            notificationTargetElements.add(actionTargetElement);

                            if (actionTargetElement.getStatus() != ActivityStatus.IN_PROGRESS)
                            {
                                governanceCompletionClient.updateActionTargetStatus(connectorUserId,
                                                                                    actionTargetElement.getActionTargetRelationshipGUID(),
                                                                                    ActivityStatus.IN_PROGRESS,
                                                                                    new Date(),
                                                                                    null,
                                                                                    null);
                            }
                        }
                        else
                        {
                            ignoredNotificationTypes.add(actionTargetElement.getTargetElement().getElementGUID());
                        }
                    }
                }
            }

            if (!ignoredNotificationTypes.isEmpty())
            {
                auditLog.logMessage(methodName,
                                    OWFAuditCode.IGNORING_NOTIFICATION_TYPES.getMessageDefinition(watchdogActionServiceName,
                                                                                                  requestType,
                                                                                                  engineActionGUID,
                                                                                                  ignoredNotificationTypes.toString()));
            }

            /*
             * Said on every pass, because the interesting number is usually zero and a service that is
             * monitoring nothing otherwise looks exactly like one with nothing to report.
             */
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OWFAuditCode.ACTION_TARGETS_READ.getMessageDefinition(watchdogActionServiceName,
                                                                                           Integer.toString(currentActionTargets.size()),
                                                                                           engineActionGUID,
                                                                                           Integer.toString(notificationTargetElements.size())));
            }
        }

        return notificationTargetElements;
    }


    /**
     * Return the unique identifier of the engine action that triggered this watchdog action service.
     * <br>
     * A watchdog is given the notification types it is responsible for as action targets of this engine
     * action, and more can be attached to it while the watchdog is running.  A watchdog that wants to react to
     * that - rather than waiting until its next refresh - needs to recognise an event about its own engine
     * action, which means knowing this identifier.
     *
     * @return string guid
     */
    public String getEngineActionGUID()
    {
        return engineActionGUID;
    }


    /**
     * Convert the message definitions into properties for a notification.  This includes setting
     * the qualified name and status.
     *
     * @param notificationDescription description of the notification
     * @param notificationTypeGUID unique identifier of the notification type to process
     * @param notificationCount   count of notifications sent by this notification type - used to generate unique qualified names
     * @return notification properties
     */
    public NotificationProperties getNotificationProperties(MessageDefinition notificationDescription,
                                                            String            notificationTypeGUID,
                                                            long              notificationCount)
    {
        Date notificationTime = new Date();

        NotificationProperties notificationProperties = new NotificationProperties();

        notificationProperties.setQualifiedName(notificationProperties.getTypeName() + "::"  + connectorName + "::" + notificationTypeGUID + "::" + notificationTime.getTime() + "::" + notificationCount);

        if (notificationDescription != null)
        {
            notificationProperties.setRequestedTime(notificationTime);
            notificationProperties.setStartTime(notificationTime);
            notificationProperties.setActivityStatus(ActivityStatus.FOR_INFO);
            notificationProperties.setDisplayName(notificationTime + " " + messageFormatter.getFormattedMessage(notificationDescription));
            notificationProperties.setSituation(notificationDescription.getSystemAction());
            notificationProperties.setExpectedBehaviour(notificationDescription.getUserAction());
        }

        return  notificationProperties;
    }


    /**
     * Return the elements that are linked to the notification type as monitored resources.
     *
     * @param notificationTypeGUID unique identifier of the notification type to process
     * @return list of root elements
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException a problem communicating with the metadata server (or it has a logic error).
     */
    public List<OpenMetadataRootElement> getMonitoredResources(String notificationTypeGUID) throws UserNotAuthorizedException,
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
     * @param notificationTypeGUID unique identifier of the notification type to process
     * @return list of root elements
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
     * @throws PropertyServerException a problem communicating with the metadata server (or it has a logic error).
     */
    public List<OpenMetadataRootElement> getNotificationSubscribers(String notificationTypeGUID) throws UserNotAuthorizedException,
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
     * Create a notification/action for the subscribers.
     * This method determines if the subscriber is eligible to receive the notification.  It also determines
     * if this is the first notification for the subscriber, or later one, since it affects the notification properties.
     *
     * @param notificationTypeGUID             unique identifier of the cause for the action to be raised
     * @param initialClassifications           classification to add to the action
     * @param firstNotificationProperties      properties for the first notification sent to this subscriber by this governance service instance
     * @param subsequentNotificationProperties properties for a follow-on notification sent to this subscriber by this governance service instance
     * @param lastNotificationProperties       properties for the last notification sent to this subscriber by this governance service instance
     * @param requestParameters                properties to pass to the next governance service
     * @param actionTargets                    the list of elements that should be acted upon - attached to notification (action)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the watchdog action service is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    public  void notifySubscribers(String                                notificationTypeGUID,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   NotificationProperties                firstNotificationProperties,
                                   NotificationProperties                subsequentNotificationProperties,
                                   NotificationProperties                lastNotificationProperties,
                                   Map<String, String>                   requestParameters,
                                   List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "notifySubscribers";

        validateIsActive(methodName);

        if (notificationTypeGUID != null)
        {
            this.notificationHandler.notifySubscribers(externalSourceGUID,
                                                       externalSourceName,
                                                       connectorUserId,
                                                       initialClassifications,
                                                       firstNotificationProperties,
                                                       subsequentNotificationProperties,
                                                       lastNotificationProperties,
                                                       notificationTypeGUID,
                                                       requestParameters,
                                                       connectorGUID,
                                                       actionTargets,
                                                       watchdogActionServiceName);
        }
    }


    /**
     * Return the request type that was used to start this watchdog action service.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws UserNotAuthorizedException exception thrown if the connector is no longer active
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
     * @throws PropertyServerException a problem connecting to the metadata store
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
     * @throws PropertyServerException a problem connecting to the metadata store
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
                "requestParameters=" + requestParameters +
                ", actionTargetElements=" + actionTargetElements +
                ", watchdogActionServiceName='" + watchdogActionServiceName + '\'' +
                ", requesterUserId='" + requesterUserId + '\'' +
                ", auditLog=" + auditLog +
                ", completionStatus=" + completionStatus +
                "} " + super.toString();
    }
}
