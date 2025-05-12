/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.client.*;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * GovernanceContext provides the governance action service with access to information about
 * the governance request along with the open metadata repository interfaces.
 * The abstract methods are implemented by the technology that supports the real metadata store.
 */
public class GovernanceActionContext implements GovernanceContext,
                                                ProvisioningGovernanceContext,
                                                RemediationGovernanceContext,
                                                TriageGovernanceContext,
                                                VerificationGovernanceContext,
                                                WatchdogGovernanceContext
{
    private final String                     userId;

    private final String                     requestType;
    private final Map<String, String>        requestParameters;
    private final String                     requesterUserId;
    private final List<RequestSourceElement> requestSourceElements;
    private final List<ActionTargetElement>  actionTargetElements;

    private volatile CompletionStatus        completionStatus = null;

    private final String                           engineActionGUID;
    private final ActionControlInterface           actionControlClient;
    private final DuplicateManagementInterface     duplicateManagementClient;
    private final GovernanceActionProcessInterface governanceActionProcessClient;
    private final GovernanceCompletionInterface    governanceCompletionClient;
    private final WatchDogEventInterface watchDogEventClient;
    private final OpenMetadataClient     openMetadataClient;
    private final OpenMetadataStore      openMetadataStore;
    private final GovernanceConfiguration          governanceConfiguration;
    private final PropertyHelper                   propertyHelper = new PropertyHelper();

    private final MessageFormatter                 messageFormatter = new MessageFormatter();


    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requesterUserId original user requesting this governance service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openMetadataClient client to the metadata store for use by the governance action service
     * @param actionControlClient client to the open governance services for use by the governance action service
     * @param duplicateManagementClient client to the open governance services for use by the governance action service
     * @param governanceActionProcessClient client to the open governance services for use by the governance action service
     * @param governanceCompletionClient client to the open governance services for use by the governance action service
     * @param watchdogEventClient client to the open governance services for use by the governance action service
     */
    public GovernanceActionContext(String                           userId,
                                   String                           engineActionGUID,
                                   String                           requestType,
                                   Map<String, String>              requestParameters,
                                   String                           requesterUserId,
                                   List<RequestSourceElement>       requestSourceElements,
                                   List<ActionTargetElement>        actionTargetElements,
                                   OpenMetadataClient               openMetadataClient,
                                   GovernanceConfiguration          governanceConfiguration,
                                   ActionControlInterface           actionControlClient,
                                   DuplicateManagementInterface     duplicateManagementClient,
                                   GovernanceActionProcessInterface governanceActionProcessClient,
                                   GovernanceCompletionInterface    governanceCompletionClient,
                                   WatchDogEventInterface           watchdogEventClient)
    {
        this.userId = userId;
        this.engineActionGUID = engineActionGUID;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.requesterUserId = requesterUserId;
        this.requestSourceElements = requestSourceElements;
        this.actionTargetElements = actionTargetElements;
        this.openMetadataClient = openMetadataClient;
        this.governanceConfiguration = governanceConfiguration;
        this.actionControlClient = actionControlClient;
        this.duplicateManagementClient = duplicateManagementClient;
        this.governanceActionProcessClient = governanceActionProcessClient;
        this.governanceCompletionClient = governanceCompletionClient;
        this.watchDogEventClient = watchdogEventClient;
        this.openMetadataStore = new OpenMetadataStore(openMetadataClient, userId, engineActionGUID);
    }


    /**
     * Return the unique identifier of the governance action that this service request is associated with.
     *
     * @return string guid
     */
    @Override
    public String getEngineActionGUID()
    {
        return engineActionGUID;
    }


    /**
     * Return the type of request.
     *
     * @return string guid
     */
    @Override
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Return the properties that hold the parameters used to drive the governance action service's processing.
     *
     * @return property map
     */
    @Override
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return the userId of the original person, process, service that requested this action.
     *
     * @return string userId
     */
    @Override
    public String getRequesterUserId()
    {
        return requesterUserId;
    }


    /**
     * Return the list of metadata elements associated with the request to the governance action service.
     * This list will not change during the lifetime of the service.
     *
     * @return list of request source elements
     */
    @Override
    public List<RequestSourceElement> getRequestSourceElements()
    {
        return requestSourceElements;
    }


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    @Override
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Return the client to access metadata from the open metadata repositories.  This enables the
     * governance action service to retrieve more information about the metadata elements linked to the
     * request source and action target elements.
     *
     * @return  metadata store client
     */
    @Override
    public OpenMetadataStore getOpenMetadataStore()
    {
        return openMetadataStore;
    }


    /**
     * Return the maximum number of elements that can be returned on a request.
     *
     * @return int
     */
    @Override
    public int getMaxPageSize()
    {
        return openMetadataClient.getMaxPagingSize();
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     *
     * @return unique identifier of the resulting incident report
     *
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createIncidentReport(String                        qualifiedName,
                                       int                           domainIdentifier,
                                       String                        background,
                                       List<IncidentImpactedElement> impactedResources,
                                       List<IncidentDependency>      previousIncidents,
                                       Map<String, Integer>          incidentClassifiers,
                                       Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return openMetadataClient.createIncidentReport(userId,
                                                       qualifiedName,
                                                       domainIdentifier,
                                                       background,
                                                       impactedResources,
                                                       previousIncidents,
                                                       incidentClassifiers,
                                                       additionalProperties,
                                                       engineActionGUID);
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
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void updateActionTargetStatus(String             actionTargetGUID,
                                         EngineActionStatus status,
                                         Date               startDate,
                                         Date               completionDate,
                                         String              completionMessage) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        governanceCompletionClient.updateActionTargetStatus(userId, actionTargetGUID, status, startDate, completionDate, completionMessage);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public  void recordCompletionStatus(CompletionStatus    status,
                                        List<String>        outputGuards) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        this.completionStatus = status;

        governanceCompletionClient.recordCompletionStatus(userId, engineActionGUID, requestParameters, status, outputGuards, null, null);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public  void recordCompletionStatus(CompletionStatus      status,
                                        List<String>          outputGuards,
                                        List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.completionStatus = status;

        governanceCompletionClient.recordCompletionStatus(userId, engineActionGUID, requestParameters, status, outputGuards, newActionTargets, null);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public  void recordCompletionStatus(CompletionStatus      status,
                                        List<String>          outputGuards,
                                        Map<String, String>   newRequestParameters,
                                        List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.recordCompletionStatus(status, outputGuards, newRequestParameters, newActionTargets, (String) null);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
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
    @Override
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

        governanceCompletionClient.recordCompletionStatus(userId,
                                                          engineActionGUID,
                                                          combinedRequestParameters,
                                                          status,
                                                          outputGuards,
                                                          newActionTargets,
                                                          completionMessage);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status               completion status enum value
     * @param outputGuards         optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets     list of action target names to GUIDs for the resulting governance action service
     * @param completionMessage    message to describe completion results or reasons for failure
     * @throws InvalidParameterException  the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                    action service completion status
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
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
                                        newActionTargets);
        }
    }


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    @Override
    public  CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }

    /**
     * Create a new governance engine definition.
     *
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName        unique name for the governance engine.
     * @param displayName          display name for messages and user interfaces.
     * @param description          description of the types of governance services that will be associated with
     *                             this governance engine.
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance engine definition.
     */
    @Override
    public String createGovernanceEngine(String governanceEngineType, String qualifiedName, String displayName, String description) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createGovernanceEngine(userId, governanceEngineType, qualifiedName, displayName, description);
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    @Override
    public GovernanceEngineElement getGovernanceEngineByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceEngineByGUID(userId, guid);
    }

    /**
     * Return the properties from a governance engine definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    @Override
    public GovernanceEngineElement getGovernanceEngineByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceEngineByName(userId, name);
    }

    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definitions.
     */
    @Override
    public List<GovernanceEngineElement> getAllGovernanceEngines(String governanceEngineType, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllGovernanceEngines(userId, governanceEngineType, startingFrom, maximumResults);
    }

    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid                 unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName        new value for unique name of governance engine.
     * @param displayName          new value for the display name.
     * @param description          new description for the governance engine.
     * @param typeDescription      new description of the type ofg governance engine.
     * @param version              new version number for the governance engine implementation.
     * @param patchLevel           new patch level for the governance engine implementation.
     * @param source               new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties   properties to populate the subtype of the governance engine.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance engine definition.
     */
    @Override
    public void updateGovernanceEngine(String guid, String qualifiedName, String displayName, String description, String typeDescription, String version, String patchLevel, String source, Map<String, String> additionalProperties, Map<String, Object> extendedProperties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateGovernanceEngine(userId, guid, qualifiedName,displayName,description,typeDescription, version, patchLevel, source, additionalProperties, extendedProperties);
    }

    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param guid          unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    @Override
    public void deleteGovernanceEngine(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteGovernanceEngine(userId, guid, qualifiedName);
    }

    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName         unique name for the governance service.
     * @param displayName           display name for the governance service.
     * @param description           description of the analysis provided by the governance service.
     * @param connection            connection to instantiate the governance service implementation.
     * @return unique identifier of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance service definition.
     */
    @Override
    public String createGovernanceService(String governanceServiceType, String qualifiedName, String displayName, String description, Connection connection) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createGovernanceService(userId, governanceServiceType, qualifiedName, displayName, description, connection);
    }

    /**
     * Return the properties from a governance service definition.
     *
     * @param guid unique identifier (guid) of the governance service definition.
     * @return properties of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definition.
     */
    @Override
    public GovernanceServiceElement getGovernanceServiceByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceByGUID(userId, guid);
    }

    /**
     * Return the properties from a governance service definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    @Override
    public GovernanceServiceElement getGovernanceServiceByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceByName(userId, name);
    }

    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance service definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definitions.
     */
    @Override
    public List<GovernanceServiceElement> getAllGovernanceServices(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllGovernanceServices(userId, startingFrom, maximumResults);
    }

    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param governanceServiceGUID governance service to search for.
     * @return list of governance engine unique identifiers (guids)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public List<String> getGovernanceServiceRegistrations(String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceRegistrations(userId, governanceServiceGUID);
    }

    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid                 unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName        new value for unique name of governance service.
     * @param displayName          new value for the display name.
     * @param description          new value for the description.
     * @param connection           connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties   properties to populate the subtype of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance service definition.
     */
    @Override
    public void updateGovernanceService(String guid, String qualifiedName, String displayName, String description, Connection connection, Map<String, String> additionalProperties, Map<String, Object> extendedProperties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateGovernanceService(userId, guid, qualifiedName, displayName, description, connection, additionalProperties, extendedProperties);
    }

    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param guid          unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definition.
     */
    @Override
    public void deleteGovernanceService(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteGovernanceService(userId, guid, qualifiedName);
    }

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestType           request type that this governance service is able to process.
     * @param requestParameters     list of parameters that are passed to the governance service (via
     *                              the context).  These values can be overridden on the actual governance service request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public void registerGovernanceServiceWithEngine(String governanceEngineGUID, String governanceServiceGUID, String requestType, Map<String, String> requestParameters) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerGovernanceServiceWithEngine(userId, governanceEngineGUID, governanceServiceGUID, requestType, requestParameters);
    }

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller.
     * @param serviceRequestType    mapped governance request type that this governance service is able to process.
     * @param requestParameters     list of parameters that are passed to the governance service (via
     *                              the governance context).  These values can be overridden on the actual governance request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public void registerGovernanceServiceWithEngine(String governanceEngineGUID, String governanceServiceGUID, String governanceRequestType, String serviceRequestType, Map<String, String> requestParameters) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerGovernanceServiceWithEngine(userId, governanceEngineGUID, governanceServiceGUID, governanceRequestType, serviceRequestType, requestParameters);
    }

    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @return details of the governance service and the asset types it is registered for.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String governanceEngineGUID, String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredGovernanceService(userId, governanceEngineGUID, governanceServiceGUID);
    }

    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String governanceEngineGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredGovernanceServices(userId, governanceEngineGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister a governance service from the governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public void unregisterGovernanceServiceFromEngine(String governanceEngineGUID, String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.unregisterGovernanceServiceFromEngine(userId, governanceEngineGUID, governanceServiceGUID);
    }

    /**
     * Create a new integration group definition.
     *
     * @param properties values that will be associated with this integration group.
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration group definition.
     */
    @Override
    public String createIntegrationGroup(IntegrationGroupProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createIntegrationGroup(userId, properties);
    }

    /**
     * Return the properties from an integration group definition.
     *
     * @param guid unique identifier (guid) of the integration group definition.
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    @Override
    public IntegrationGroupElement getIntegrationGroupByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationGroupByGUID(userId, guid);
    }

    /**
     * Return the properties from an integration group definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    @Override
    public IntegrationGroupElement getIntegrationGroupByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationGroupByName(userId, name);
    }

    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration group definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definitions.
     */
    @Override
    public List<IntegrationGroupElement> getAllIntegrationGroups(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllIntegrationGroups(userId, startingFrom, maximumResults);
    }

    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid          unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties    values that will be associated with this integration group.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration group definition.
     */
    @Override
    public void updateIntegrationGroup(String guid, boolean isMergeUpdate, IntegrationGroupProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateIntegrationGroup(userId, guid, isMergeUpdate, properties);
    }

    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param guid          unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    @Override
    public void deleteIntegrationGroup(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteIntegrationGroup(userId, guid, qualifiedName);
    }

    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param properties values that will be associated with this integration connector - including the connection.
     * @return unique identifier of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration connector definition.
     */
    @Override
    public String createIntegrationConnector(IntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createIntegrationConnector(userId, properties);
    }

    /**
     * Return the properties from an integration connector definition.
     *
     * @param guid unique identifier (guid) of the integration connector definition.
     * @return properties of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    @Override
    public IntegrationConnectorElement getIntegrationConnectorByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorByGUID(userId, guid);
    }

    /**
     * Return the properties from an integration connector definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    @Override
    public IntegrationConnectorElement getIntegrationConnectorByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorByName(userId, name);
    }

    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration connector definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definitions.
     */
    @Override
    public List<IntegrationConnectorElement> getAllIntegrationConnectors(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllIntegrationConnectors(userId, startingFrom, maximumResults);
    }

    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param integrationConnectorGUID integration connector to search for.
     * @return list of integration group unique identifiers (guids)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public List<String> getIntegrationConnectorRegistrations(String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorRegistrations(userId, integrationConnectorGUID);
    }

    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid          unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties    values that will be associated with this integration connector - including the connection.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    @Override
    public void updateIntegrationConnector(String guid, boolean isMergeUpdate, IntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateIntegrationConnector(userId, guid, isMergeUpdate, properties);
    }

    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param guid          unique identifier of the integration connector - used to locate the definition.
     * @param qualifiedName unique name for the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    @Override
    public void deleteIntegrationConnector(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteIntegrationConnector(userId, guid, qualifiedName);
    }

    /**
     * Register an integration connector with a specific integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param properties               list of parameters that are used to control to the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public void registerIntegrationConnectorWithGroup(String integrationGroupGUID, String integrationConnectorGUID, RegisteredIntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerIntegrationConnectorWithGroup(userId, integrationGroupGUID, integrationConnectorGUID, properties);
    }

    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @return details of the integration connector and the asset types it is registered for.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String integrationGroupGUID, String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID);
    }

    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String integrationGroupGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredIntegrationConnectors(userId, integrationGroupGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister an integration connector from the integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public void unregisterIntegrationConnectorFromGroup(String integrationGroupGUID, String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.unregisterIntegrationConnectorFromGroup(userId, integrationGroupGUID, integrationConnectorGUID);
    }

    /**
     * Add a catalog target to an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID      unique identifier of the metadata element that is a catalog target.
     * @param properties               properties for the relationship.
     * @return catalog target GUID
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the catalog target definition.
     */
    @Override
    public String addCatalogTarget(String integrationConnectorGUID, String metadataElementGUID, CatalogTargetProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.addCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID, properties);
    }

    /**
     * Update a catalog target for an integration connector.
     *
     * @param catalogTargetGUID unique identifier of the relationship.
     * @param properties        properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the catalog target definition.
     */
    @Override
    public void updateCatalogTarget(String catalogTargetGUID, CatalogTargetProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateCatalogTarget(userId, catalogTargetGUID, properties);
    }

    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target.
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    @Override
    public CatalogTarget getCatalogTarget(String relationshipGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getCatalogTarget(userId, relationshipGUID);
    }

    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom             initial position in the stored list.
     * @param maximumResults           maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    @Override
    public List<CatalogTarget> getCatalogTargets(String integrationConnectorGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem accessing/updating the integration connector definition.
     */
    @Override
    public void removeCatalogTarget(String relationshipGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.removeCatalogTarget(userId, relationshipGUID);
    }


    /*
     * Governance Configuration methods
     */



    /*
     * Provisioning methods
     */


    /**
     * Pack the basic properties into an ElementProperties object.
     *
     * @param qualifiedName the unique name of the new metadata element
     * @param name the technical name of the associated resource
     * @param description the description of the associated resource
     * @param versionIdentifier version of the associated resource
     * @param extendedProperties properties from the subtype
     * @param methodName calling method
     *
     * @return ElementProperties contain the supplied properties.
     * @throws InvalidParameterException null qualifiedName
     */
    private ElementProperties packBasicProperties(String            qualifiedName,
                                                  String            name,
                                                  String            versionIdentifier,
                                                  String            description,
                                                  ElementProperties extendedProperties,
                                                  String            methodName) throws InvalidParameterException
    {
        final String qualifiedNamePropertyName     = "qualifiedName";
        final String namePropertyName              = "name";
        final String versionIdentifierPropertyName = "versionIdentifier";
        final String descriptionPropertyName       = "description";

        propertyHelper.validateMandatoryName(qualifiedName, qualifiedNamePropertyName, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(extendedProperties, qualifiedNamePropertyName, qualifiedName);

        properties = propertyHelper.addStringProperty(properties, namePropertyName, name);
        properties = propertyHelper.addStringProperty(properties, versionIdentifierPropertyName, versionIdentifier);
        properties = propertyHelper.addStringProperty(properties, descriptionPropertyName, description);

        return properties;
    }


    /**
     * Create an asset such as a data file, database, API or server.
     * This interface includes an optional templateGUID to copy the structure of an existing asset of the same type.
     *
     * @param assetTypeName the type name of the asset.  This is the name of an open metadata type that inherits from "Asset".
     * @param qualifiedName the unique name of the new asset
     * @param name the technical display name of the asset
     * @param description the description of the asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public String createAsset(String assetTypeName,
                              String qualifiedName,
                              String name,
                              String description) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "createAsset(simple)";

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               assetTypeName,
                                                               ElementStatus.ACTIVE,
                                                               null,
                                                               null,
                                                               true,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               null,
                                                               null,
                                                               null,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create an asset such as a data file, database, API or server.
     *
     * @param assetTypeName      the type name of the asset.  This is the name of an open metadata type that inherits from "Asset".
     * @param qualifiedName      the unique name of the new asset
     * @param name               the technical display name of the resource
     * @param versionIdentifier  version of the resource
     * @param description        the description of the resource
     * @param extendedProperties attributes introduced by subtypes of Asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException  the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
    public String createAsset(String            assetTypeName,
                              String            qualifiedName,
                              String            name,
                              String            versionIdentifier,
                              String            description,
                              ElementProperties extendedProperties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "createAsset";

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               assetTypeName,
                                                               ElementStatus.ACTIVE,
                                                               null,
                                                               null,
                                                               true,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               null,
                                                               null,
                                                               null,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create an asset such as a data file, database, API or server.
     * This interface includes an optional templateGUID to copy the structure of an existing asset of the same type.
     *
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments
     *                     such as nested content, schema, connection etc)
     * @param qualifiedName the unique name of the new asset
     * @param name the technical display name of the asset
     * @param description the description of the asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public String createAssetFromTemplate(String templateGUID,
                                          String qualifiedName,
                                          String name,
                                          String description) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "createAssetFromTemplate";

        ElementProperties properties = packBasicProperties(qualifiedName,
                                                           name,
                                                           null,
                                                           description,
                                                           null,
                                                           methodName);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    null,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    null);
    }


    /**
     * Create an asset such as a data file, database, API or server.  This is used if the provisioning
     * governance action service has created a new asset as part of the provisioning process.
     * This interface includes an optional templateGUID to copy the structure of an existing asset of the same type.
     *
     * @param assetTypeName      the type name of the asset.  This is the name of an open metadata type that inherits from "Asset".
     * @param templateGUID       the unique identifier of the existing asset to copy (this will copy all the attachments
     *                           such as nested content, schema, connection etc)
     * @param qualifiedName      the unique name of the new asset
     * @param name               the technical display name of the asset
     * @param versionIdentifier  version of the resource
     * @param description        the description of the asset
     * @param extendedProperties attributes introduced by subtypes of Asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException  the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
    public String createAssetFromTemplate(String            assetTypeName,
                                          String            templateGUID,
                                          String            qualifiedName,
                                          String            name,
                                          String            versionIdentifier,
                                          String            description,
                                          ElementProperties extendedProperties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        final String methodName = "createAssetFromTemplate";

        String metadataElementTypeName = OpenMetadataType.ASSET.typeName;

        if (assetTypeName != null)
        {
            metadataElementTypeName = assetTypeName;
        }

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    metadataElementTypeName,
                                                                    null,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public String createProcess(String        processTypeName,
                                ElementStatus initialStatus,
                                String        qualifiedName,
                                String        name,
                                String        description) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "createProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               initialStatus,
                                                               null,
                                                               null,
                                                               true,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               null,
                                                               null,
                                                               null,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param processTypeName    the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus      status value of the process
     * @param qualifiedName      the unique name of the new process
     * @param name               the technical display name of the process
     * @param versionIdentifier  version of the resource
     * @param description        the description of the process
     * @param formula            expression that describes the behaviour of the process
     * @param extendedProperties attributes introduced by subtypes of Process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
    public String createProcess(String            processTypeName,
                                ElementStatus     initialStatus,
                                String            qualifiedName,
                                String            name,
                                String            versionIdentifier,
                                String            description,
                                String            formula,
                                ElementProperties extendedProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "createProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        properties = propertyHelper.addStringProperty(properties, "formula", formula);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               initialStatus,
                                                               null,
                                                               null,
                                                               true,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               null,
                                                               null,
                                                               null,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param templateGUID the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                     schema, connection etc)
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public String createProcessFromTemplate(String        templateGUID,
                                            ElementStatus initialStatus,
                                            String        qualifiedName,
                                            String        name,
                                            String        description) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "createProcessFromTemplate";

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.PROCESS.typeName,
                                                                    null,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param templateGUID       the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                           schema, connection etc)
     * @param initialStatus      status value of the process
     * @param qualifiedName      the unique name of the new process
     * @param name               the technical display name of the process
     * @param versionIdentifier  version of the resource
     * @param description        the description of the process
     * @param formula            expression that describes the behaviour of the process
     * @param extendedProperties attributes introduced by subtypes of Process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
    public String createProcessFromTemplate(String            templateGUID,
                                            ElementStatus     initialStatus,
                                            String            qualifiedName,
                                            String            name,
                                            String            versionIdentifier,
                                            String            description,
                                            String            formula,
                                            ElementProperties extendedProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createProcessFromTemplate";

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        properties = propertyHelper.addStringProperty(properties, "formula", formula);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.PROCESS.typeName,
                                                                    null,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    null);
    }


    /**
     * Create a process that represents the processing instance of this governance action.
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     * @param parentGUID the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                     schema, connection etc)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public String createChildProcess(String        processTypeName,
                                     ElementStatus initialStatus,
                                     String        qualifiedName,
                                     String        name,
                                     String        description,
                                     String        parentGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "createChildProcess(simple)";

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        ElementProperties relationshipProperties = propertyHelper.addEnumProperty(null,
                                                                                  OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                                                                  ProcessContainmentType.getOpenTypeName(),
                                                                                  ProcessContainmentType.OWNED.getName());

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               initialStatus,
                                                               null,
                                                               parentGUID,
                                                               false,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               parentGUID,
                                                               OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName,
                                                               relationshipProperties,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create a process that represents the processing instance of this governance action.
     *
     * @param processTypeName    the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus      status value of the process
     * @param qualifiedName      the unique name of the new process
     * @param name               the technical display name of the process
     * @param versionIdentifier  version of the resource
     * @param description        the description of the process
     * @param formula            expression that describes the behaviour of the process
     * @param extendedProperties attributes introduced by subtypes of Process
     * @param parentGUID         the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                           schema, connection etc)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException    there is a problem connecting to the metadata store
     */
    @Override
    public String createChildProcess(String             processTypeName,
                                     ElementStatus      initialStatus,
                                     String             qualifiedName,
                                     String             name,
                                     String             versionIdentifier,
                                     String             description,
                                     String             formula,
                                     ElementProperties  extendedProperties,
                                     String             parentGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "createChildProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        properties = propertyHelper.addStringProperty(properties, "formula", formula);

        ElementProperties relationshipProperties = propertyHelper.addEnumProperty(null,
                                                                                  OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                                                                  ProcessContainmentType.getOpenTypeName(),
                                                                                  ProcessContainmentType.OWNED.getName());

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               initialStatus,
                                                               null,
                                                               parentGUID,
                                                               false,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               parentGUID,
                                                               OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName,
                                                               relationshipProperties,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Add a port to a process.
     *
     * @param processGUID unique identifier of the process
     * @param qualifiedName unique name for the port
     * @param displayName display name for the port
     * @param portType type of port (direction of data flow)
     *
     * @return unique identifier of the new port
     *
     * @throws InvalidParameterException the processGUID or qualified name is null or is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createPort(String   processGUID,
                             String   qualifiedName,
                             String   displayName,
                             PortType portType) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "createProcess";
        final String processGUIDParameterName = "processGUID";

        propertyHelper.validateGUID(processGUID, processGUIDParameterName, methodName);

        ElementProperties properties = packBasicProperties(qualifiedName, null, null, null,null, methodName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName);
        properties = propertyHelper.addEnumProperty(properties,
                                                    OpenMetadataProperty.PORT_TYPE.name,
                                                    PortType.getOpenTypeName(),
                                                    portType.getName());

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.PORT_IMPLEMENTATION.typeName,
                                                               ElementStatus.ACTIVE,
                                                               null,
                                                               processGUID,
                                                               false,
                                                               null,
                                                               null,
                                                               null,
                                                               properties,
                                                               processGUID,
                                                               OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName,
                                                               null,
                                                               true,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create a lineage mapping relationship between a source and target element.  This could be between two assets, two process ports or
     * two schema elements.
     *
     * @param sourceElementGUID unique identifier of the element that describes the source of the data.
     * @param targetElementGUID unique identifier of the element that describes the destination of the data.
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException one of the GUIDs is null or is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createLineageRelationship(String sourceElementGUID,
                                            String targetElementGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "createLineageMapping";
        final String lineageMappingTypeName = "LineageMapping";

        final String sourceElementGUIDParameterName = "sourceElementGUID";
        final String targetElementGUIDParameterName = "targetElementGUID";

        propertyHelper.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        propertyHelper.validateGUID(targetElementGUID, targetElementGUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                                 lineageMappingTypeName,
                                                                 sourceElementGUID,
                                                                 targetElementGUID,
                                                                 true,
                                                                 false,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 new Date());
    }


    /**
     * Create a lineage relationship between a source and target element.  This could be between two assets, two process ports or
     * two schema elements.
     *
     * @param relationshipName  either LineageMapping, ProcessCall, DataFlow, ControlFlow.
     * @param sourceElementGUID unique identifier of the element that describes the source of the data.
     * @param iscQualifiedName     qualifiedName of the information supply chain
     * @param label label for when the lineage relationship is visualized
     * @param formula expression summary
     * @param formulaType language used to express the formula
     * @param queryId identifier for the process query - DataMapping Relationship
     * @param query the process query - DataMapping Relationship
     * @param guard value that indicated a control path to take - ControlFlow relationship
     * @param lineNumber line number where the call is made - ProcessCall Relationship
     * @param targetElementGUID unique identifier of the element that describes the destination of the data.
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the GUIDs is null or is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createLineageRelationship(String relationshipName,
                                            String sourceElementGUID,
                                            String iscQualifiedName,
                                            String label,
                                            String description,
                                            String formula,
                                            String formulaType,
                                            String queryId,
                                            String query,
                                            String guard,
                                            String lineNumber,
                                            String targetElementGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "createLineageRelationship";

        final String sourceElementGUIDParameterName = "sourceElementGUID";
        final String targetElementGUIDParameterName = "targetElementGUID";

        propertyHelper.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        propertyHelper.validateGUID(targetElementGUID, targetElementGUIDParameterName, methodName);

        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, iscQualifiedName);

        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.LABEL.name, label);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.DESCRIPTION.name, description);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.FORMULA.name, formula);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.FORMULA_TYPE.name, formulaType);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.QUERY_ID.name, formulaType);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.QUERY.name, formulaType);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.GUARD.name, formulaType);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, OpenMetadataProperty.LINE_NUMBER.name, formulaType);

        String lineageRelationshipTypeName = OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName;
        if (relationshipName != null)
        {
            lineageRelationshipTypeName = relationshipName;
        }

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               lineageRelationshipTypeName,
                                                               sourceElementGUID,
                                                               targetElementGUID,
                                                               true,
                                                               false,
                                                               null,
                                                               null,
                                                               relationshipProperties,
                                                               new Date());

    }


    /*
     * Remediation methods
     */

    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This metadata element will be given an initial status of ACTIVE which is sufficient for most types of elements.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElement(String            metadataElementTypeName,
                                        ElementProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               metadataElementTypeName,
                                                               ElementStatus.ACTIVE,
                                                               null,
                                                               null,
                                                               properties);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               String                         anchorScopeGUID,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               null,
                                                               null,
                                                               metadataElementTypeName,
                                                               initialStatus,
                                                               initialClassifications,
                                                               anchorGUID,
                                                               isOwnAnchor,
                                                               anchorScopeGUID,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties,
                                                               parentGUID,
                                                               parentRelationshipTypeName,
                                                               parentRelationshipProperties,
                                                               parentAtEnd1,
                                                               true,
                                                               true,
                                                               null);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElement(String            metadataElementTypeName,
                                        ElementStatus     initialStatus,
                                        Date              effectiveFrom,
                                        Date              effectiveTo,
                                        ElementProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 metadataElementTypeName,
                                                                 initialStatus,
                                                                 effectiveFrom,
                                                                 effectiveTo,
                                                                 properties);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElement(String            metadataElementGUID,
                                      boolean           replaceProperties,
                                      boolean           forLineage,
                                      boolean           forDuplicateProcessing,
                                      ElementProperties properties,
                                      Date              effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                          metadataElementGUID,
                                                          replaceProperties,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          properties,
                                                          effectiveTime);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param newElementStatus new status value - or null to leave as is
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatus(String        metadataElementGUID,
                                            boolean       forLineage,
                                            boolean       forDuplicateProcessing,
                                            ElementStatus newElementStatus,
                                            Date          effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        openMetadataClient.updateMetadataElementStatusInStore(userId,
                                                                metadataElementGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                newElementStatus,
                                                                effectiveTime);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementEffectivity(String        metadataElementGUID,
                                                 boolean       forLineage,
                                                 boolean       forDuplicateProcessing,
                                                 Date          effectiveFrom,
                                                 Date          effectiveTo,
                                                 Date          effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        openMetadataClient.updateMetadataElementEffectivityInStore(userId,
                                                                   metadataElementGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   effectiveTime);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElement the metadata element to update
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteMetadataElement(OpenMetadataElement metadataElement,
                                      boolean             cascadedDelete,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "deleteMetadataElement";
        final String parameterName = "metadataElement";

        if (metadataElement != null)
        {
            if (metadataElement.getOrigin().getOriginCategory() == ElementOriginCategory.LOCAL_COHORT)
            {
                openMetadataClient.deleteMetadataElementInStore(userId,
                                                                null,
                                                                null,
                                                                metadataElement.getElementGUID(),
                                                                cascadedDelete,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime);
            }
            else
            {
                openMetadataClient.deleteMetadataElementInStore(userId,
                                                                metadataElement.getOrigin().getHomeMetadataCollectionId(),
                                                                metadataElement.getOrigin().getHomeMetadataCollectionName(),
                                                                metadataElement.getElementGUID(),
                                                                cascadedDelete,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime);
            }
        }
        else
        {
            throw new InvalidParameterException(GAFErrorCode.NO_METADATA_ELEMENT.getMessageDefinition(methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteMetadataElement(String  metadataElementGUID,
                                      boolean cascadedDelete,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId,
                                                        metadataElementGUID,
                                                        cascadedDelete,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElement(String            metadataElementGUID,
                                        String            classificationName,
                                        boolean           forLineage,
                                        boolean           forDuplicateProcessing,
                                        ElementProperties properties,
                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            null,
                                                            null,
                                                            properties,
                                                            effectiveTime);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElement(String            metadataElementGUID,
                                        String            classificationName,
                                        boolean           forLineage,
                                        boolean           forDuplicateProcessing,
                                        Date              effectiveFrom,
                                        Date              effectiveTo,
                                        ElementProperties properties,
                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveFrom,
                                                            effectiveTo,
                                                            properties,
                                                            effectiveTime);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the classification
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void reclassifyMetadataElement(String            metadataElementGUID,
                                          String            classificationName,
                                          boolean           replaceProperties,
                                          boolean           forLineage,
                                          boolean           forDuplicateProcessing,
                                          ElementProperties properties,
                                          Date              effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                              metadataElementGUID,
                                                              classificationName,
                                                              replaceProperties,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              properties,
                                                              effectiveTime);
    }



    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateClassificationStatus(String  metadataElementGUID,
                                           String  classificationName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveFrom,
                                           Date    effectiveTo,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                    metadataElementGUID,
                                                                    classificationName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    effectiveTime);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void declassifyMetadataElement(String  metadataElementGUID,
                                          String  classificationName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                              metadataElementGUID,
                                                              classificationName,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties the properties of the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createRelatedElements(String            relationshipTypeName,
                                        String            metadataElement1GUID,
                                        String            metadataElement2GUID,
                                        boolean           forLineage,
                                        boolean           forDuplicateProcessing,
                                        ElementProperties properties,
                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                                 relationshipTypeName,
                                                                 metadataElement1GUID,
                                                                 metadataElement2GUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 properties,
                                                                 effectiveTime);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createRelatedElements(String            relationshipTypeName,
                                        String            metadataElement1GUID,
                                        String            metadataElement2GUID,
                                        boolean           forLineage,
                                        boolean           forDuplicateProcessing,
                                        Date              effectiveFrom,
                                        Date              effectiveTo,
                                        ElementProperties properties,
                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                                 relationshipTypeName,
                                                                 metadataElement1GUID,
                                                                 metadataElement2GUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveFrom,
                                                                 effectiveTo,
                                                                 properties,
                                                                 effectiveTime);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElements(String            relationshipGUID,
                                      boolean           replaceProperties,
                                      boolean           forLineage,
                                      boolean           forDuplicateProcessing,
                                      ElementProperties properties,
                                      Date              effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     replaceProperties,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     properties,
                                                     effectiveTime);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsStatus(String  relationshipGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveFrom,
                                            Date    effectiveTo,
                                            Date    effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        openMetadataClient.updateRelationshipEffectivityInStore(userId,
                                                                relationshipGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveFrom,
                                                                effectiveTo,
                                                                effectiveTime);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElements(String relationshipGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime);
    }


    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkElementsAsPeerDuplicates(String  metadataElement1GUID,
                                             String  metadataElement2GUID,
                                             int     statusIdentifier,
                                             String  steward,
                                             String  stewardTypeName,
                                             String  stewardPropertyName,
                                             String  source,
                                             String  notes,
                                             boolean setKnownDuplicate) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        duplicateManagementClient.linkElementsAsPeerDuplicates(userId,
                                                               metadataElement1GUID,
                                                               metadataElement2GUID,
                                                               statusIdentifier,
                                                               steward,
                                                               stewardTypeName,
                                                               stewardPropertyName,
                                                               source,
                                                               notes,
                                                               setKnownDuplicate);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkConsolidatedDuplicate(String       consolidatedElementGUID,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        duplicateManagementClient.linkConsolidatedDuplicate(userId,
                                                            consolidatedElementGUID,
                                                            statusIdentifier,
                                                            steward,
                                                            stewardTypeName,
                                                            stewardPropertyName,
                                                            source,
                                                            notes,
                                                            sourceElementGUIDs);
    }


    /*
     * Triage methods
     */

    /**
     * Create a To-Do request for someone to work on.
     *
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignToGUID unique identifier for the recipient actor
     * @param actionTargetGUID unique identifier of the element to work on.
     * @param actionTargetName name of the element to work on.
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String openToDo(String toDoQualifiedName,
                           String title,
                           String instructions,
                           String toDoType,
                           int    priority,
                           Date   dueDate,
                           String assignToGUID,
                           String actionTargetGUID,
                           String actionTargetName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "openToDo";

        final String toDoQualifiedNameParameterName = "toDoQualifiedName";
        final String assignToParameterName          = "assignToGUID";

        propertyHelper.validateMandatoryName(toDoQualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignToGUID, assignToParameterName, methodName);

        /*
         * Create the to do entity
         */
        NewActionTarget actionTarget = new NewActionTarget();
        actionTarget.setActionTargetGUID(actionTargetGUID);
        actionTarget.setActionTargetName(actionTargetName);

        List<NewActionTarget> actionTargets = new ArrayList<>();
        actionTargets.add(actionTarget);

        return openMetadataClient.openToDo(userId,
                                           toDoQualifiedName,
                                           title,
                                           instructions,
                                           toDoType,
                                           priority,
                                           dueDate,
                                           null,
                                           assignToGUID,
                                           null,
                                           engineActionGUID,
                                           actionTargets);
    }


    /**
     * Create a new context event
     *
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->relationship properties)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String registerContextEvent(String                                       anchorGUID,
                                       Map<String, DependentContextEventProperties> parentContextEvents,
                                       Map<String, DependentContextEventProperties> childContextEvents,
                                       Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                       Map<String, ContextEventImpactProperties>    impactedElements,
                                       Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                       Map<String, RelationshipProperties>          contextEventEvidenceGUIDs,
                                       ContextEventProperties contextEventProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.registerContextEvent(userId, anchorGUID, parentContextEvents, childContextEvents, relatedContextEvents, impactedElements, effectedDataResourceGUIDs, contextEventEvidenceGUIDs, contextEventProperties);
    }

    /*
     * Verification methods
     */


    /*
     * Watchdog methods
     */



    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
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
    public void registerListener(WatchdogGovernanceListener listener,
                                 List<WatchdogEventType>    interestingEventTypes,
                                 List<String>               interestingMetadataTypes,
                                 String                     specificInstance) throws InvalidParameterException
    {
        watchDogEventClient.registerListener(listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    @Override
    public void disconnectListener()
    {
        watchDogEventClient.disconnectListener();
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this engine action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     * @param governanceEngineName name of the governance engine to run the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     *
     * @return unique identifier of the governance action
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateEngineAction(String                qualifiedName,
                                       int                   domainIdentifier,
                                       String                displayName,
                                       String                description,
                                       List<String>          requestSourceGUIDs,
                                       List<NewActionTarget> actionTargets,
                                       Date                  startTime,
                                       String                governanceEngineName,
                                       String                requestType,
                                       Map<String, String>   requestParameters) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return actionControlClient.initiateEngineAction(userId,
                                                        qualifiedName,
                                                        domainIdentifier,
                                                        displayName,
                                                        description,
                                                        requestSourceGUIDs,
                                                        actionTargets,
                                                        null,
                                                        startTime,
                                                        governanceEngineName,
                                                        requestType,
                                                        requestParameters,
                                                        null,
                                                        null,
                                                        engineActionGUID,
                                                        governanceEngineName);
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     * @param governanceEngineName name of the governance engine to run the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     * @param processName name of the process that this action is a part of
     *
     * @return unique identifier of the governance action
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateEngineAction(String                qualifiedName,
                                       int                   domainIdentifier,
                                       String                displayName,
                                       String                description,
                                       List<String>          requestSourceGUIDs,
                                       List<NewActionTarget> actionTargets,
                                       Date                  startTime,
                                       String                governanceEngineName,
                                       String                requestType,
                                       Map<String, String>   requestParameters,
                                       String                processName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return actionControlClient.initiateEngineAction(userId,
                                                        qualifiedName,
                                                        domainIdentifier,
                                                        displayName,
                                                        description,
                                                        requestSourceGUIDs,
                                                        actionTargets,
                                                        null,
                                                        startTime,
                                                        governanceEngineName,
                                                        requestType,
                                                        requestParameters,
                                                        processName,
                                                        null,
                                                        engineActionGUID,
                                                        governanceEngineName);
    }

    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param governanceActionTypeQualifiedName unique name of the governance action type to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null or unrecognized qualified name of the type
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceActionType(String                governanceActionTypeQualifiedName,
                                               List<String>          requestSourceGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               Date                  startTime,
                                               Map<String, String>   requestParameters,
                                               String                originatorServiceName,
                                               String                originatorEngineName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return actionControlClient.initiateGovernanceActionType(userId,
                                                                governanceActionTypeQualifiedName,
                                                                requestSourceGUIDs,
                                                                actionTargets,
                                                                startTime,
                                                                requestParameters,
                                                                originatorServiceName,
                                                                originatorEngineName);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestParameters request parameters to pass to the governance actions called in the governance action process
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the governance action process instance
     *
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String                processQualifiedName,
                                                  Map<String, String>   requestParameters,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return actionControlClient.initiateGovernanceActionProcess(userId,
                                                                   processQualifiedName,
                                                                   requestSourceGUIDs,
                                                                   actionTargets,
                                                                   startTime,
                                                                   requestParameters,
                                                                   null,
                                                                   null);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceActionContext{" +
                       "userId='" + userId + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       ", completionStatus=" + completionStatus +
                       ", engineActionGUID='" + engineActionGUID + '\'' +
                       ", actionControlClient=" + actionControlClient +
                       ", duplicateManagementClient=" + duplicateManagementClient +
                       ", governanceActionProcessClient=" + governanceActionProcessClient +
                       ", governanceCompletionClient=" + governanceCompletionClient +
                       ", watchDogEventClient=" + watchDogEventClient +
                       ", openMetadataClient=" + openMetadataClient +
                       ", openMetadataStore=" + openMetadataStore +
                       ", propertyHelper=" + propertyHelper +
                       '}';
    }
}
