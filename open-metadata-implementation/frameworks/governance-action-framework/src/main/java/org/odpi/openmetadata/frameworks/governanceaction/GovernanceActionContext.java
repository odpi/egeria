/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentDependency;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentImpactedElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.PortType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private final List<RequestSourceElement> requestSourceElements;
    private final List<ActionTargetElement>  actionTargetElements;


    private volatile CompletionStatus        completionStatus = null;

    private final String               governanceActionGUID;
    private final OpenGovernanceClient openGovernanceClient;
    private final OpenMetadataClient   openMetadataClient;
    private final OpenMetadataStore    openMetadataStore;
    private final PropertyHelper       propertyHelper = new PropertyHelper();


    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openGovernanceClient client to the metadata store for use by the governance action service
     */
    public GovernanceActionContext(String                     userId,
                                   String                     governanceActionGUID,
                                   String                     requestType,
                                   Map<String, String>        requestParameters,
                                   List<RequestSourceElement> requestSourceElements,
                                   List<ActionTargetElement>  actionTargetElements,
                                   OpenMetadataClient         openMetadataClient,
                                   OpenGovernanceClient       openGovernanceClient)
    {
        this.userId = userId;
        this.governanceActionGUID = governanceActionGUID;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.requestSourceElements = requestSourceElements;
        this.actionTargetElements = actionTargetElements;
        this.openMetadataClient = openMetadataClient;
        this.openGovernanceClient = openGovernanceClient;
        this.openMetadataStore = new OpenMetadataStore(openMetadataClient, userId);
    }


    /**
     * Return the unique identifier of the governance action that this service request is associated with.
     *
     * @return string guid
     */
    @Override
    public String getGovernanceActionGUID()
    {
        return governanceActionGUID;
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
                                                         governanceActionGUID);
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
    public void updateActionTargetStatus(String                 actionTargetGUID,
                                         GovernanceActionStatus status,
                                         Date                   startDate,
                                         Date                   completionDate,
                                         String                 completionMessage) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openGovernanceClient.updateActionTargetStatus(userId, actionTargetGUID, status, startDate, completionDate, completionMessage);
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
    public synchronized  void recordCompletionStatus(CompletionStatus    status,
                                                     List<String>        outputGuards) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        this.completionStatus = status;

        openGovernanceClient.recordCompletionStatus(userId, governanceActionGUID, requestParameters, status, outputGuards, null, null);
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
    public synchronized  void recordCompletionStatus(CompletionStatus      status,
                                                     List<String>          outputGuards,
                                                     List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        this.completionStatus = status;

        openGovernanceClient.recordCompletionStatus(userId, governanceActionGUID, requestParameters, status, outputGuards, newActionTargets, null);
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
    public synchronized  void recordCompletionStatus(CompletionStatus      status,
                                                     List<String>          outputGuards,
                                                     Map<String, String>   newRequestParameters,
                                                     List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        this.recordCompletionStatus(status, outputGuards, newRequestParameters, newActionTargets, null);
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
    public synchronized  void recordCompletionStatus(CompletionStatus      status,
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

        openGovernanceClient.recordCompletionStatus(userId,
                                                    governanceActionGUID,
                                                    combinedRequestParameters,
                                                    status,
                                                    outputGuards,
                                                    newActionTargets,
                                                    completionMessage);
    }


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    @Override
    public synchronized CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


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

        return openMetadataClient.createMetadataElementInStore(userId, assetTypeName, ElementStatus.ACTIVE, null, null, properties, null);
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

        return openMetadataClient.createMetadataElementInStore(userId, assetTypeName, ElementStatus.ACTIVE, null, null, properties, null);
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

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        return openMetadataClient.createMetadataElementInStore(userId, "Asset", ElementStatus.ACTIVE, null, null, properties, templateGUID);
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

        String metadataElementTypeName = "Asset";

        if (assetTypeName != null)
        {
            metadataElementTypeName = assetTypeName;
        }

        ElementProperties properties = packBasicProperties(qualifiedName, name, versionIdentifier, description, extendedProperties, methodName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 metadataElementTypeName,
                                                                 ElementStatus.ACTIVE,
                                                                 null,
                                                                 null,
                                                                 properties,
                                                                 templateGUID);
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
                                                                 properties,
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
                                                                 properties,
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

        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 null,
                                                                 initialStatus,
                                                                 null,
                                                                 null,
                                                                 properties,
                                                                 templateGUID);
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

        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 null,
                                                                 initialStatus,
                                                                 null,
                                                                 null,
                                                                 properties,
                                                                 templateGUID);
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

        String processGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                               processTypeName,
                                                                               initialStatus,
                                                                               null,
                                                                               null,
                                                                               properties,
                                                                               null);

        if (processGUID != null)
        {
            ElementProperties relationshipProperties = propertyHelper.addEnumProperty(null,
                                                                                      "containmentType",
                                                                                      "ProcessContainmentType",
                                                                                      "OWNED");

            openMetadataClient.createRelatedElementsInStore(userId,
                                                              "ProcessHierarchy",
                                                              parentGUID,
                                                              processGUID,
                                                              true,
                                                              false,
                                                              null,
                                                              null,
                                                              relationshipProperties,
                                                              new Date());
        }

        return processGUID;
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

        String processGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                               processTypeName,
                                                                               initialStatus,
                                                                               null,
                                                                               null,
                                                                               properties,
                                                                               null);

        if (processGUID != null)
        {
            ElementProperties relationshipProperties = propertyHelper.addEnumProperty(null,
                                                                                      "containmentType",
                                                                                      "ProcessContainmentType",
                                                                                      "OWNED");

            openMetadataClient.createRelatedElementsInStore(userId,
                                                              "ProcessHierarchy",
                                                              parentGUID,
                                                              processGUID,
                                                              true,
                                                              false,
                                                              null,
                                                              null,
                                                              relationshipProperties,
                                                              new Date());
        }

        return processGUID;
    }


    /**
     * Add a port to a process.
     *
     * @param processGUID unique identifier of the process
     * @param qualifiedName unique name for the port
     * @param displayName display name for the port
     * @param portType type of port (direction of data flow)
     * @param templateGUID optional unique identifier of a template port to copy
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
                             PortType portType,
                             String   templateGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "createProcess";

        final String portTypeName        = "PortImplementation";
        final String processPortTypeName = "ProcessPort";

        final String displayNamePropertyName = "displayName";
        final String portTypePropertyName = "portType";
        final String portTypeTypeName = "PortType";

        final String processGUIDParameterName = "processGUID";

        propertyHelper.validateGUID(processGUID, processGUIDParameterName, methodName);

        ElementProperties properties = packBasicProperties(qualifiedName, null, null, null,null, methodName);

        if (displayName != null)
        {
            properties = propertyHelper.addStringProperty(properties, displayNamePropertyName, displayName);
        }

        if (portType != null)
        {
            properties = propertyHelper.addEnumProperty(properties, portTypePropertyName, portTypeTypeName, portType.getOpenTypeSymbolicName());
        }

        String portGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                            portTypeName,
                                                                            ElementStatus.ACTIVE,
                                                                            null,
                                                                            null,
                                                                            properties,
                                                                            templateGUID);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                          processPortTypeName,
                                                          processGUID,
                                                          portGUID,
                                                          true,
                                                          false,
                                                          null,
                                                          null,
                                                          null,
                                                          new Date());

        return portGUID;
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
     * @param qualifiedName     qualifiedName of the information supply chain
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
                                            String qualifiedName,
                                            String description,
                                            String formula,
                                            String targetElementGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "createLineageRelationship";
        final String lineageMappingTypeName = "LineageMapping";

        final String sourceElementGUIDParameterName = "sourceElementGUID";
        final String targetElementGUIDParameterName = "targetElementGUID";

        propertyHelper.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        propertyHelper.validateGUID(targetElementGUID, targetElementGUIDParameterName, methodName);

        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null, "qualifiedName", qualifiedName);

        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, "description", description);
        relationshipProperties = propertyHelper.addStringProperty(relationshipProperties, "formula", formula);

        if (relationshipName == null)
        {
            return openMetadataClient.createRelatedElementsInStore(userId,
                                                                     lineageMappingTypeName,
                                                                     sourceElementGUID,
                                                                     targetElementGUID,
                                                                     true,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     relationshipProperties,
                                                                     new Date());
        }
        else
        {
            return openMetadataClient.createRelatedElementsInStore(userId,
                                                                     relationshipName,
                                                                     sourceElementGUID,
                                                                     targetElementGUID,
                                                                     true,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     relationshipProperties,
                                                                     new Date());
        }
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
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElement(String            metadataElementTypeName,
                                        ElementProperties properties,
                                        String            templateGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 metadataElementTypeName,
                                                                 ElementStatus.ACTIVE,
                                                                 null,
                                                                 null,
                                                                 properties,
                                                                 templateGUID);
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
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
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
                                        ElementProperties properties,
                                        String            templateGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                                 metadataElementTypeName,
                                                                 initialStatus,
                                                                 effectiveFrom,
                                                                 effectiveTo,
                                                                 properties,
                                                                 templateGUID);
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
     * @param metadataElementGUID unique identifier of the metadata element to update
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
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId,
                                                          metadataElementGUID,
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
        openMetadataClient.updateRelatedElementsInStore(userId,
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
        openMetadataClient.updateRelatedElementsEffectivityInStore(userId,
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
        openMetadataClient.deleteRelatedElementsInStore(userId,
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
        openGovernanceClient.linkElementsAsPeerDuplicates(userId,
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
        openGovernanceClient.linkConsolidatedDuplicate(userId,
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
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String openToDo(String toDoQualifiedName,
                           String title,
                           String instructions,
                           int    priority,
                           Date   dueDate,
                           String assignTo) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName = "openToDo";

        final String todoTypeName             = "ToDo";
        final String personRoleTypeName       = "PersonRole";
        final String actionAssignmentTypeName = "ActionAssignment";

        final String qualifiedNamePropertyName = "qualifiedName";
        final String titlePropertyName         = "name";
        final String instructionsPropertyName  = "description";
        final String priorityPropertyName      = "priority";
        final String dueDatePropertyName       = "dueTime";
        final String statusPropertyName        = "status";
        final String statusPropertyTypeName    = "ToDoStatus";
        final String openEnumPropertyValue     = "Open";

        final String toDoQualifiedNameParameterName = "toDoQualifiedName";
        final String assignToParameterName          = "assignTo";

        propertyHelper.validateMandatoryName(toDoQualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignTo, assignToParameterName, methodName);

        SearchProperties        searchProperties = new SearchProperties();
        List<PropertyCondition> conditions = new ArrayList<>();
        PropertyCondition          condition                  = new PropertyCondition();
        PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

        primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitiveTypePropertyValue.setPrimitiveValue(assignTo);
        primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(qualifiedNamePropertyName);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitiveTypePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        /*
         * Validate that there is a person role to assign the "to do" to
         */
        List<OpenMetadataElement> personRoleMatches = openMetadataClient.findMetadataElements(userId,
                                                                                                personRoleTypeName,
                                                                                                null,
                                                                                                searchProperties,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                false,
                                                                                                false,
                                                                                                new Date(),
                                                                                                0,
                                                                                                0);

        if ((personRoleMatches == null) || personRoleMatches.isEmpty())
        {
            throw new InvalidParameterException(GAFErrorCode.UNKNOWN_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                  toDoQualifiedNameParameterName,
                                                                                                  methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }
        else if (personRoleMatches.size() > 1)
        {
            List<String> matchingGUIDs = new ArrayList<>();

            for (OpenMetadataElement element : personRoleMatches)
            {
                if (element != null)
                {
                    matchingGUIDs.add(element.getElementGUID());
                }
            }

            throw new InvalidParameterException(GAFErrorCode.DUPLICATE_ELEMENT.getMessageDefinition(toDoQualifiedName,
                                                                                                    toDoQualifiedNameParameterName,
                                                                                                    methodName,
                                                                                                    matchingGUIDs.toString()),
                                                this.getClass().getName(),
                                                methodName,
                                                toDoQualifiedNameParameterName);
        }

        OpenMetadataElement personRoleElement = personRoleMatches.get(0);
        String personRoleGUID = null;

        if ((personRoleElement != null) && (personRoleElement.getElementGUID() != null))
        {
            personRoleGUID = personRoleElement.getElementGUID();
        }

        /*
         * Create the to do entity
         */
        ElementProperties properties = propertyHelper.addStringProperty(null, qualifiedNamePropertyName, toDoQualifiedName);

        if (title != null)
        {
            properties = propertyHelper.addStringProperty(properties, titlePropertyName, title);
        }

        if (instructions != null)
        {
            properties = propertyHelper.addStringProperty(properties, instructionsPropertyName, instructionsPropertyName);
        }

        if (dueDate != null)
        {
            properties = propertyHelper.addDateProperty(properties, dueDatePropertyName, dueDate);
        }

        properties = propertyHelper.addIntProperty(properties, priorityPropertyName, priority);
        properties = propertyHelper.addEnumProperty(properties, statusPropertyName, statusPropertyTypeName, openEnumPropertyValue);

        String todoGUID = openMetadataClient.createMetadataElementInStore(userId, todoTypeName, ElementStatus.ACTIVE, null, null, properties, null);

        /*
         * Link the "to do" and the person role
         */
        openMetadataClient.createRelatedElementsInStore(userId,
                                                          actionAssignmentTypeName,
                                                          personRoleGUID,
                                                          todoGUID,
                                                          false,
                                                          false,
                                                          null,
                                                          null,
                                                          null,
                                                          new Date());

        return todoGUID;
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
     *
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     *
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
        openGovernanceClient.registerListener(listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }


    /**
     * Unregister the listener permanently from the event infrastructure.
     */
    @Override
    public void disconnectListener()
    {
        openGovernanceClient.disconnectListener();
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
     *
     * @return unique identifier of the governance action
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceAction(String                qualifiedName,
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
        return openGovernanceClient.initiateGovernanceAction(userId,
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
                                                             governanceActionGUID,
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
    public String initiateGovernanceAction(String                qualifiedName,
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
        return openGovernanceClient.initiateGovernanceAction(userId,
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
                                                             governanceActionGUID,
                                                             governanceEngineName);
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
     * @return unique identifier of the first governance action of the process
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
        return openGovernanceClient.initiateGovernanceActionProcess(userId,
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
        return "GovernanceContext{" +
                       "userId='" + userId + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       ", completionStatus=" + completionStatus +
                       ", openMetadataStore=" + openGovernanceClient +
                       ", propertyHelper=" + propertyHelper +
                       '}';
    }
}
