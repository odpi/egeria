/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EngineActionBuilder creates the parts for an entity that represents an engine action.
 */
public class EngineActionBuilder extends ReferenceableBuilder
{
    private int                 domainIdentifier                = 0;
    private String              displayName                     = null;
    private String              description                     = null;
    private String              governanceEngineGUID            = null;
    private String              governanceEngineName            = null;
    private String              governanceActionTypeGUID        = null;
    private String              governanceActionTypeName        = null;
    private String              processName                     = null;
    private String              governanceActionProcessStepGUID = null;
    private String              governanceActionProcessStepName = null;
    private String              requesterUserId                 = null;
    private String              requestType                     = null;
    private Map<String, String> requestParameters               = null;
    private List<String>        mandatoryGuards                 = null;
    private List<String>        receivedGuards                  = null;
    private int                 actionStatus                    = 0;
    private Date                requestedStartDate              = null;
    private Date                startDate                       = null;
    private String              processingEngineUserId          = null;
    private Date                completionDate                  = null;
    private List<String>        completionGuards                = null;

    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the engine action
     * @param domainIdentifier governance domain for this engine action
     * @param displayName short display name for the engine action
     * @param description description of the engine action
     * @param governanceEngineGUID GUID of the governance engine that should execute the request
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param governanceActionTypeGUID unique identifier of the governance action type that initiated this engine action
     * @param governanceActionTypeName unique name of the governance action process step that initiated this engine action
     * @param processName name of the process that requested the engine action
     * @param processStepGUID unique identifier of the governance action process step that initiated this engine action
     * @param processStepName unique name of the governance action process step that initiated this engine action
     * @param requesterUserId  the caller
     * @param requestType request type from the caller
     * @param requestParameters properties to pass to the governance service
     * @param mandatoryGuards list of guards that must be supplied before this engine action can proceed
     * @param receivedGuards list of guards that triggered this engine action
     * @param actionStatus status for the engine action
     * @param requestedStartDate the date/time to start this engine action
     * @param processingEngineUserId userId of the engine host processing this engine action
     * @param completionDate the date/time that the engine action completed
     * @param completionGuards the guards produced by the governance service that ran on behalf of this engine action
     * @param additionalProperties additional properties for a engine action
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    EngineActionBuilder(String               qualifiedName,
                        int                  domainIdentifier,
                        String               displayName,
                        String               description,
                        String               governanceEngineGUID,
                        String               governanceEngineName,
                        String               governanceActionTypeGUID,
                        String               governanceActionTypeName,
                        String               processName,
                        String               processStepGUID,
                        String               processStepName,
                        String               requesterUserId,
                        String               requestType,
                        Map<String, String>  requestParameters,
                        List<String>         mandatoryGuards,
                        List<String>         receivedGuards,
                        int                  actionStatus,
                        Date                 requestedStartDate,
                        String               processingEngineUserId,
                        Date                 completionDate,
                        List<String>         completionGuards,
                        Map<String, String>  additionalProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.ENGINE_ACTION.typeGUID,
              OpenMetadataType.ENGINE_ACTION.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.domainIdentifier = domainIdentifier;
        this.displayName = displayName;
        this.description = description;
        this.governanceEngineGUID = governanceEngineGUID;
        this.governanceEngineName = governanceEngineName;
        this.governanceActionTypeGUID = governanceActionTypeGUID;
        this.governanceActionTypeName = governanceActionTypeName;
        this.processName = processName;
        this.governanceActionProcessStepGUID = processStepGUID;
        this.governanceActionProcessStepName = processStepName;
        this.requesterUserId = requesterUserId;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.mandatoryGuards = mandatoryGuards;
        this.receivedGuards = receivedGuards;
        this.actionStatus = actionStatus;
        this.requestedStartDate = requestedStartDate;
        this.processingEngineUserId = processingEngineUserId;
        this.completionDate = completionDate;
        this.completionGuards = completionGuards;
    }


    /**
     * Create constructor for claiming an engine action
     *
     * @param actionStatus status for the engine action
     * @param processingEngineUserId userId of the engine host processing this engine action
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    EngineActionBuilder(int                  actionStatus,
                        String               processingEngineUserId,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(null,
              null,
              OpenMetadataType.ENGINE_ACTION.typeGUID,
              OpenMetadataType.ENGINE_ACTION.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.actionStatus = actionStatus;
        this.processingEngineUserId = processingEngineUserId;
    }


    /**
     * Create constructor for updating the status of an engine action
     *
     * @param actionStatus new status for the engine action
     * @param startDate time that this engine action actually started (moved from activating to in progress)
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    EngineActionBuilder(int                  actionStatus,
                        Date                 startDate,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(null,
              null,
              OpenMetadataType.ENGINE_ACTION.typeGUID,
              OpenMetadataType.ENGINE_ACTION.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.actionStatus = actionStatus;
        this.startDate = startDate;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    EngineActionBuilder(OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(OpenMetadataType.ENGINE_ACTION.typeGUID,
              OpenMetadataType.ENGINE_ACTION.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                               domainIdentifier,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name,
                                                                  governanceEngineGUID,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name,
                                                                  governanceEngineName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name,
                                                                  governanceActionTypeGUID,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name,
                                                                  governanceActionTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROCESS_NAME.name,
                                                                  processName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                                  governanceActionProcessStepGUID,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROCESS_STEP_NAME.name,
                                                                  governanceActionProcessStepName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                                  requesterUserId,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.REQUEST_TYPE.name,
                                                                  requestType,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                     requestParameters,
                                                                     methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.MANDATORY_GUARDS.name,
                                                                       mandatoryGuards,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.RECEIVED_GUARDS.name,
                                                                       receivedGuards,
                                                                       methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                    ActivityStatus.getOpenTypeGUID(),
                                                                    ActivityStatus.getOpenTypeName(),
                                                                    actionStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.ACTIVITY_STATUS.name);
        }

        if (requestedStartDate == null)
        {
            this.requestedStartDate = new Date();
        }

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.REQUESTED_START_TIME.name,
                                                                requestedStartDate,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.START_TIME.name,
                                                                startDate,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROCESSING_ENGINE_USER_ID.name,
                                                                  processingEngineUserId,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.COMPLETION_TIME.name,
                                                                completionDate,
                                                                methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                                       completionGuards,
                                                                       methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getClaimInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                    ActivityStatus.getOpenTypeGUID(),
                                                                    ActivityStatus.getOpenTypeName(),
                                                                    actionStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.ACTIVITY_STATUS.name);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROCESSING_ENGINE_USER_ID.name,
                                                                  processingEngineUserId,
                                                                  methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getCancelInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                    ActivityStatus.getOpenTypeGUID(),
                                                                    ActivityStatus.getOpenTypeName(),
                                                                    actionStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.ACTIVITY_STATUS.name);
        }

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.COMPLETION_TIME.name,
                                                                new Date(),
                                                                methodName);

        return properties;
    }


    /**
     * Append the supplied bean properties in the supplied InstanceProperties object.
     *
     * @param properties existing properties
     * @param actionStatus completion status enum value
     * @param completionGuards optional guard strings for triggering subsequent action(s)
     * @param completionDate when did it finish
     * @param completionMessage message to describe completion results or reasons for failure
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getCompletionInstanceProperties(InstanceProperties properties,
                                                       int                actionStatus,
                                                       Date               completionDate,
                                                       List<String>       completionGuards,
                                                       String             completionMessage,
                                                       String             methodName) throws InvalidParameterException
    {
        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                    ActivityStatus.getOpenTypeGUID(),
                                                                    ActivityStatus.getOpenTypeName(),
                                                                    actionStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.ACTIVITY_STATUS.name);
        }

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.COMPLETION_TIME.name,
                                                                completionDate,
                                                                methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                                       completionGuards,
                                                                       methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                  completionMessage,
                                                                  methodName);

        return properties;
    }
}
