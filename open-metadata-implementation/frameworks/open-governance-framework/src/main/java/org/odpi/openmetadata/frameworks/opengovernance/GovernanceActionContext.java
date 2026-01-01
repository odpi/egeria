/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.client.*;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceContext provides the governance action service with access to information about
 * the governance request along with the open metadata repository interfaces.
 * The abstract methods are implemented by the technology that supports the real metadata store.
 */
public class GovernanceActionContext extends ConnectorContextBase implements GovernanceContext,
                                                                             ProvisioningGovernanceContext,
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
    private final GovernanceActionProcessInterface governanceActionProcessClient;
    private final ConnectedAssetClient             connectedAssetClient;
    private final GovernanceCompletionInterface governanceCompletionClient;
    private final WatchdogEventInterface        watchdogEventClient;
    private final GovernanceConfiguration       governanceConfiguration;
    private final PropertyHelper                   propertyHelper = new PropertyHelper();

    private final MessageFormatter                 messageFormatter = new MessageFormatter();

    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
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
     * @param auditLog logging destination
     * @param maxPageSize max number of results
     * @param deleteMethod default delete method
     * @param engineActionGUID unique identifier of the engine action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requesterUserId original user requesting this governance service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param actionControlClient client to the open governance services for use by the governance action service
     * @param governanceActionProcessClient client to the open governance services for use by the governance action service
     * @param connectedAssetClient client for working with connectors
     * @param governanceCompletionClient client to the open governance services for use by the governance action service
     * @param watchdogEventClient client to the open governance services for use by the governance action service
     */
    public GovernanceActionContext(String                           localServerName,
                                   String                           localServiceName,
                                   String                           externalSourceGUID,
                                   String                           externalSourceName,
                                   String                           connectorId,
                                   String                           connectorName,
                                   String                           connectorUserId,
                                   String                           connectorGUID,
                                   boolean                          generateIntegrationReport,
                                   OpenMetadataClient               openMetadataClient,
                                   AuditLog                         auditLog,
                                   int                              maxPageSize,
                                   DeleteMethod                     deleteMethod,
                                   String                           engineActionGUID,
                                   String                           requestType,
                                   Map<String, String>              requestParameters,
                                   String                           requesterUserId,
                                   List<RequestSourceElement>       requestSourceElements,
                                   List<ActionTargetElement>        actionTargetElements,
                                   GovernanceConfiguration          governanceConfiguration,
                                   ActionControlInterface           actionControlClient,
                                   GovernanceActionProcessInterface governanceActionProcessClient,
                                   ConnectedAssetClient             connectedAssetClient,
                                   GovernanceCompletionInterface    governanceCompletionClient,
                                   WatchdogEventInterface watchdogEventClient)
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

        this.userId = connectorUserId;
        this.engineActionGUID = engineActionGUID;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.requesterUserId = requesterUserId;
        this.requestSourceElements = requestSourceElements;
        this.actionTargetElements = actionTargetElements;
        this.governanceConfiguration = governanceConfiguration;
        this.actionControlClient = actionControlClient;
        this.governanceActionProcessClient = governanceActionProcessClient;
        this.connectedAssetClient = connectedAssetClient;
        this.governanceCompletionClient = governanceCompletionClient;
        this.watchdogEventClient       = watchdogEventClient;
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
     * Return the connector to the requested asset.
     *
     * @param assetGUID unique identifier of the asset
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public Connector getConnectorForAsset(String assetGUID) throws InvalidParameterException,
                                                                   ConnectionCheckedException,
                                                                   ConnectorCheckedException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return connectedAssetClient.getConnectorForAsset(userId, assetGUID);
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
                                         ActivityStatus status,
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
        propertyHelper.validateMandatoryName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(extendedProperties, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, name);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, description);

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
                                                               new NewElementProperties(properties));
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
                                                               new NewElementProperties(properties));
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

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    templateOptions,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
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


        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    metadataElementTypeName,
                                                                    templateOptions,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
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
                                                               new NewElementProperties(properties));
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param processTypeName    the type name of the process.  This is the name of an open metadata type that inherits from "Process".
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

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.FORMULA.name, formula);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               new NewElementProperties(properties));
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param templateGUID the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                     schema, connection etc)
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
                                            String        qualifiedName,
                                            String        name,
                                            String        description) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "createProcessFromTemplate";

        ElementProperties properties = packBasicProperties(qualifiedName, name, null, description, null, methodName);

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.PROCESS.typeName,
                                                                    templateOptions,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param templateGUID       the unique identifier of the existing process to copy (this will copy all the attachments such as ports, nested content,
     *                           schema, connection etc)
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

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.FORMULA.name, formula);

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    OpenMetadataType.PROCESS.typeName,
                                                                    templateOptions,
                                                                    templateGUID,
                                                                    properties,
                                                                    null,
                                                                    null);
    }


    /**
     * Create a process that represents the processing instance of this governance action.
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
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

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setForLineage(true);
        newElementOptions.setAnchorGUID(parentGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(null);
        newElementOptions.setParentGUID(parentGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               newElementOptions,
                                                               null,
                                                               new NewElementProperties(properties),
                                                               new NewElementProperties(relationshipProperties));
    }


    /**
     * Create a process that represents the processing instance of this governance action.
     *
     * @param processTypeName    the type name of the process.  This is the name of an open metadata type that inherits from "Process".
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

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setForLineage(true);
        newElementOptions.setAnchorGUID(parentGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(null);
        newElementOptions.setParentGUID(parentGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               processTypeName,
                                                               newElementOptions,
                                                               null,
                                                               new NewElementProperties(properties),
                                                               new NewElementProperties(relationshipProperties));
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
        final String methodName = "createPort";
        final String processGUIDParameterName = "processGUID";

        propertyHelper.validateGUID(processGUID, processGUIDParameterName, methodName);

        ElementProperties properties = packBasicProperties(qualifiedName, null, null, null,null, methodName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName);
        properties = propertyHelper.addEnumProperty(properties,
                                                    OpenMetadataProperty.PORT_TYPE.name,
                                                    PortType.getOpenTypeName(),
                                                    portType.getName());

        NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

        newElementOptions.setForLineage(true);
        newElementOptions.setAnchorGUID(processGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(null);
        newElementOptions.setParentGUID(processGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.PORT_IMPLEMENTATION.typeName,
                                                               newElementOptions,
                                                               null,
                                                               new NewElementProperties(properties),
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

        return openMetadataStore.createRelatedElementsInStore(lineageMappingTypeName,
                                                               sourceElementGUID,
                                                               targetElementGUID,
                                                               new MakeAnchorOptions(openMetadataStore.getMetadataSourceOptions()),
                                                               null);
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

        return openMetadataStore.createRelatedElementsInStore(lineageRelationshipTypeName,
                                                              sourceElementGUID,
                                                              targetElementGUID,
                                                              new MakeAnchorOptions(openMetadataStore.getMetadataSourceOptions()),
                                                              new NewElementProperties(relationshipProperties));

    }


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
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this engine action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
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
                                       List<String>          actionSourceGUIDs,
                                       List<String>          actionCauseGUIDs,
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
                                                        actionSourceGUIDs,
                                                        actionCauseGUIDs,
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
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
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
                                       List<String>          actionSourceGUIDs,
                                       List<String>          actionCauseGUIDs,
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
                                                        actionSourceGUIDs,
                                                        actionCauseGUIDs,
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
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
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
                                               List<String>          actionSourceGUIDs,
                                               List<String>          actionCauseGUIDs,
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
                                                                actionSourceGUIDs,
                                                                actionCauseGUIDs,
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
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
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
                                                  List<String>          actionSourceGUIDs,
                                                  List<String>          actionCauseGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return actionControlClient.initiateGovernanceActionProcess(userId,
                                                                   processQualifiedName,
                                                                   actionSourceGUIDs,
                                                                   actionCauseGUIDs,
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
                ", requesterUserId='" + requesterUserId + '\'' +
                ", requestSourceElements=" + requestSourceElements +
                ", actionTargetElements=" + actionTargetElements +
                ", completionStatus=" + completionStatus +
                ", engineActionGUID='" + engineActionGUID + '\'' +
                ", actionControlClient=" + actionControlClient +
                ", governanceActionProcessClient=" + governanceActionProcessClient +
                ", governanceCompletionClient=" + governanceCompletionClient +
                ", watchDogEventClient=" + watchdogEventClient +
                ", governanceConfiguration=" + governanceConfiguration +
                ", propertyHelper=" + propertyHelper +
                ", messageFormatter=" + messageFormatter +
                ", maxPageSize=" + maxPageSize +
                "} " + super.toString();
    }
}
