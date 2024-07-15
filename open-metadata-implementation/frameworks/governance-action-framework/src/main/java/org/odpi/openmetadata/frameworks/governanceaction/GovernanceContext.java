/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * GovernanceContext provides the governance action service with access to information about
 * the governance request along with the open metadata repository interfaces.
 * The abstract methods are implemented by the technology that supports the real metadata store.
 */
public interface GovernanceContext
{
    /**
     * Return the unique identifier of the engine action that this service request is associated with.
     *
     * @return string guid
     */
    String getEngineActionGUID();


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     */
    String getRequestType();


    /**
     * Return the properties that hold the parameters used to drive the governance action service's processing.
     *
     * @return property map
     */
    Map<String, String> getRequestParameters();


    /**
     * Return the userId of the original person, process, service that requested this action.
     *
     * @return string userId
     */
    String getRequesterUserId();


    /**
     * Return the list of metadata elements associated with the request to the governance action service.
     * This list will not change during the lifetime of the service.
     *
     * @return list of request source elements
     */
    List<RequestSourceElement> getRequestSourceElements();


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    List<ActionTargetElement> getActionTargetElements();


    /**
     * Return the client to access metadata from the open metadata repositories.  This enables the
     * governance action service to retrieve more information about the metadata elements linked to the
     * request source and action target elements.
     *
     * @return  metadata store client
     */
    OpenMetadataStore getOpenMetadataStore();


    /**
     * Return the maximum number of elements that can be returned on a request.
     *
     * @return int
     */
    int getMaxPageSize();


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
    String createIncidentReport(String                        qualifiedName,
                                int                           domainIdentifier,
                                String                        background,
                                List<IncidentImpactedElement> impactedResources,
                                List<IncidentDependency>      previousIncidents,
                                Map<String, Integer>          incidentClassifiers,
                                Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


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
    void updateActionTargetStatus(String             actionTargetGUID,
                                  EngineActionStatus status,
                                  Date               startDate,
                                  Date               completionDate,
                                  String             completionMessage) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


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
    void recordCompletionStatus(CompletionStatus    status,
                                List<String>        outputGuards) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


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
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;



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
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                Map<String, String>   newRequestParameters,
                                List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


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
    void recordCompletionStatus(CompletionStatus      status,
                                List<String>          outputGuards,
                                Map<String, String>   newRequestParameters,
                                List<NewActionTarget> newActionTargets,
                                String                completionMessage) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    CompletionStatus getCompletionStatus();



    /**
     * Create a new governance engine definition.
     *
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName unique name for the governance engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of governance services that will be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    String  createGovernanceEngine(String  governanceEngineType,
                                   String  qualifiedName,
                                   String  displayName,
                                   String  description) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Return the properties from a governance engine definition.
     *
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    GovernanceEngineElement getGovernanceEngineByGUID(String    guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return the properties from a governance engine definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    GovernanceEngineElement getGovernanceEngineByName(String    name) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    List<GovernanceEngineElement> getAllGovernanceEngines(String  governanceEngineType,
                                                          int     startingFrom,
                                                          int     maximumResults) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName new value for unique name of governance engine.
     * @param displayName new value for the display name.
     * @param description new description for the governance engine.
     * @param typeDescription new description of the type ofg governance engine.
     * @param version new version number for the governance engine implementation.
     * @param patchLevel new patch level for the governance engine implementation.
     * @param source new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    void    updateGovernanceEngine(String                guid,
                                   String                qualifiedName,
                                   String                displayName,
                                   String                description,
                                   String                typeDescription,
                                   String                version,
                                   String                patchLevel,
                                   String                source,
                                   Map<String, String>   additionalProperties,
                                   Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    void    deleteGovernanceEngine(String  guid,
                                   String  qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName  unique name for the governance service.
     * @param displayName   display name for the governance service.
     * @param description  description of the analysis provided by the governance service.
     * @param connection   connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    String  createGovernanceService(String     governanceServiceType,
                                    String     qualifiedName,
                                    String     displayName,
                                    String     description,
                                    Connection connection) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the properties from a governance service definition.
     *
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    GovernanceServiceElement getGovernanceServiceByGUID(String    guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return the properties from a governance service definition.
     *
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    GovernanceServiceElement getGovernanceServiceByName(String    name) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    List<GovernanceServiceElement> getAllGovernanceServices(int     startingFrom,
                                                            int     maximumResults) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    List<String>  getGovernanceServiceRegistrations(String   governanceServiceGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName new value for unique name of governance service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    void    updateGovernanceService(String                guid,
                                    String                qualifiedName,
                                    String                displayName,
                                    String                description,
                                    Connection            connection,
                                    Map<String, String>   additionalProperties,
                                    Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    void    deleteGovernanceService(String  guid,
                                    String  qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestType request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                             the context).  These values can be overridden on the actual governance service request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    void  registerGovernanceServiceWithEngine(String              governanceEngineGUID,
                                              String              governanceServiceGUID,
                                              String              requestType,
                                              Map<String, String> requestParameters) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller.
     * @param serviceRequestType mapped governance request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    void registerGovernanceServiceWithEngine(String               governanceEngineGUID,
                                             String               governanceServiceGUID,
                                             String               governanceRequestType,
                                             String               serviceRequestType,
                                             Map<String, String>  requestParameters) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    RegisteredGovernanceServiceElement getRegisteredGovernanceService(String governanceEngineGUID,
                                                                      String governanceServiceGUID) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    List<RegisteredGovernanceServiceElement>  getRegisteredGovernanceServices(String governanceEngineGUID,
                                                                              int    startingFrom,
                                                                              int    maximumResults) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    void  unregisterGovernanceServiceFromEngine(String governanceEngineGUID,
                                                String governanceServiceGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /*
     * Integration connectors
     */



    /**
     * Create a new integration group definition.
     *
     * @param properties values that will be associated with this integration group.
     *
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    String  createIntegrationGroup(IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Return the properties from an integration group definition.
     *
     * @param guid unique identifier (guid) of the integration group definition.
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    IntegrationGroupElement getIntegrationGroupByGUID(String    guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return the properties from an integration group definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    IntegrationGroupElement getIntegrationGroupByName(String    name) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration group definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definitions.
     */
    List<IntegrationGroupElement> getAllIntegrationGroups(int     startingFrom,
                                                          int     maximumResults) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties values that will be associated with this integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    void    updateIntegrationGroup(String                     guid,
                                   boolean                    isMergeUpdate,
                                   IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    void    deleteIntegrationGroup(String  guid,
                                   String  qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param properties values that will be associated with this integration connector - including the connection.
     *
     * @return unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration connector definition.
     */
    String  createIntegrationConnector(IntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Return the properties from an integration connector definition.
     *
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    IntegrationConnectorElement getIntegrationConnectorByGUID(String    guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Return the properties from an integration connector definition.
     *
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    IntegrationConnectorElement getIntegrationConnectorByName(String    name) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration connector definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definitions.
     */
    List<IntegrationConnectorElement> getAllIntegrationConnectors(int     startingFrom,
                                                                  int     maximumResults) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    List<String>  getIntegrationConnectorRegistrations(String   integrationConnectorGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties values that will be associated with this integration connector - including the connection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    void    updateIntegrationConnector(String                         guid,
                                       boolean                        isMergeUpdate,
                                       IntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param qualifiedName unique name for the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    void    deleteIntegrationConnector(String  guid,
                                       String  qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Register an integration connector with a specific integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param properties list of parameters that are used to control to the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    void  registerIntegrationConnectorWithGroup(String                                   integrationGroupGUID,
                                                String                                   integrationConnectorGUID,
                                                RegisteredIntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;


    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String integrationGroupGUID,
                                                                            String integrationConnectorGUID) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException;


    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    List<RegisteredIntegrationConnectorElement>  getRegisteredIntegrationConnectors(String integrationGroupGUID,
                                                                                    int    startingFrom,
                                                                                    int    maximumResults) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException;


    /**
     * Unregister an integration connector from the integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    void  unregisterIntegrationConnectorFromGroup(String integrationGroupGUID,
                                                  String integrationConnectorGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Add a catalog target to an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param properties properties for the relationship.
     *
     * @return catalog target GUID
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    String addCatalogTarget(String                  integrationConnectorGUID,
                            String                  metadataElementGUID,
                            CatalogTargetProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Update a catalog target for an integration connector.
     *
     * @param catalogTargetGUID unique identifier of the relationship.
     * @param properties properties for the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    void updateCatalogTarget(String                  catalogTargetGUID,
                             CatalogTargetProperties properties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target.
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    CatalogTarget getCatalogTarget(String relationshipGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;



    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    List<CatalogTarget> getCatalogTargets(String  integrationConnectorGUID,
                                          int     startingFrom,
                                          int     maximumResults) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    void removeCatalogTarget(String relationshipGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;
}
