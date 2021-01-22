/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MetadataElementHandler manages MetadataElement objects from the Governance Action Framework (GAF).
 * These objects are 1-1 with an open metadata entity.
 */
public class GovernanceActionHandler<B> extends OpenMetadataAPIGenericHandler<B>
{

    /**
     * Construct the handler for metadata elements.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public GovernanceActionHandler(OpenMetadataAPIGenericConverter<B> converter,
                                   Class<B>                           beanClass,
                                   String                             serviceName,
                                   String                             serverName,
                                   InvalidParameterHandler            invalidParameterHandler,
                                   RepositoryHandler                  repositoryHandler,
                                   OMRSRepositoryHelper               repositoryHelper,
                                   String                             localServerUserId,
                                   OpenMetadataServerSecurityVerifier securityVerifier,
                                   List<String>                       supportedZones,
                                   List<String>                       defaultZones,
                                   List<String>                       publishZones,
                                   AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance action service to run
     * @param requestProperties properties to pass to the governance action service
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     * @param methodName calling method
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceAction(String              userId,
                                           String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           List<String>        requestSourceGUIDs,
                                           List<String>        actionTargetGUIDs,
                                           Date                startTime,
                                           String              governanceEngineName,
                                           String              requestType,
                                           Map<String, String> requestProperties,
                                           String              originatorServiceName,
                                           String              originatorEngineName,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String engineNameParameterName     = "governanceEngineName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);

        String governanceEngineGUID = this.validateGovernanceEngineName(userId, governanceEngineName, engineNameParameterName, methodName);

        GovernanceActionBuilder builder = new GovernanceActionBuilder(qualifiedName,
                                                                      domainIdentifier,
                                                                      displayName,
                                                                      description,
                                                                      null,
                                                                      OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL,
                                                                      startTime,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        String governanceActionGUID = this.createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                  qualifiedName,
                                                                  OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                  builder,
                                                                  methodName);

        if (governanceActionGUID != null)
        {
            /*
             * Link the governance action to the governance engine.
             */
            final String governanceActionGUIDParameterName = "governanceActionGUID";
            final String governanceEngineGUIDParameterName = "governanceEngineGUID";

            InstanceProperties relationshipProperties = null;

            if (requestType != null)
            {
                relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          null,
                                                                          OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                          requestType,
                                                                          methodName);
            }

            if ((requestProperties != null) && (! requestProperties.isEmpty()))
            {
                relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                         relationshipProperties,
                                                                                         OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                         requestProperties,
                                                                                         methodName);
            }

            this.linkElementToElement(userId,
                                      null,
                                      null,
                                      governanceActionGUID,
                                      governanceActionGUIDParameterName,
                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                      governanceEngineGUID,
                                      governanceEngineGUIDParameterName,
                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID,
                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                      relationshipProperties,
                                      methodName);

            /*
             * Identify the source of the work
             */
            if ((requestSourceGUIDs != null) && (! requestSourceGUIDs.isEmpty()))
            {
                final String requestSourceGUIDParameterName = "requestSourceGUIDs[x]";

                relationshipProperties = null;

                if (originatorServiceName != null)
                {
                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
                                                                                          originatorServiceName,
                                                                                          methodName);
                }

                if (originatorEngineName != null)
                {
                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          relationshipProperties,
                                                                                          OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
                                                                                          originatorEngineName,
                                                                                          methodName);
                }

                for (String requestSourceGUID : requestSourceGUIDs)
                {
                    if (requestSourceGUID != null)
                    {
                        this.linkElementToElement(userId,
                                                  null,
                                                  null,
                                                  requestSourceGUID,
                                                  requestSourceGUIDParameterName,
                                                  OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                  governanceActionGUID,
                                                  governanceActionGUIDParameterName,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_GUID,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_NAME,
                                                  relationshipProperties,
                                                  methodName);
                    }
                }
            }

            /*
             * Identify the objects to work on
             */
            this.addActionTargets(userId,
                                  governanceActionGUID,
                                  governanceActionGUIDParameterName,
                                  actionTargetGUIDs,
                                  methodName);
        }

        return governanceActionGUID;
    }


    private void addActionTargets(String       userId,
                                  String       governanceActionGUID,
                                  String       governanceActionGUIDParameterName,
                                  List<String> actionTargetGUIDs,
                                  String       methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        /*
         * Identify the objects to work on
         */
        if ((actionTargetGUIDs != null) && (! actionTargetGUIDs.isEmpty()))
        {
            final String actionTargetGUIDParameterName = "actionTargetGUIDs[x]";

            for (String actionTargetGUID : actionTargetGUIDs)
            {
                if (actionTargetGUID != null)
                {
                    this.linkElementToElement(userId,
                                              null,
                                              null,
                                              governanceActionGUID,
                                              governanceActionGUIDParameterName,
                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                              actionTargetGUID,
                                              actionTargetGUIDParameterName,
                                              OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                              OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_GUID,
                                              OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME,
                                              null,
                                              methodName);
                }
            }
        }
    }

    /**
     * Retrieve the unique identifier for the governance engine that will run the governance action.
     * Throw InvalidParameterException if it is not found.
     *
     * @param userId calling user
     * @param governanceEngineName name of the engine
     * @param governanceEngineNameParameterName parameter supplying engine name
     * @param methodName calling method
     * @return unique identifier of the governance engine
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private String validateGovernanceEngineName(String userId,
                                                String governanceEngineName,
                                                String governanceEngineNameParameterName,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        String governanceEngineGUID = this.getBeanGUIDByUniqueName(userId,
                                                                   governanceEngineName,
                                                                   governanceEngineNameParameterName,
                                                                   OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                   OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                   supportedZones,
                                                                   methodName);

        if (governanceEngineGUID == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_ENGINE_NAME.getMessageDefinition(governanceEngineName,
                                                                                                                  serviceName,
                                                                                                                  serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                governanceEngineNameParameterName);
        }

        return governanceEngineGUID;
    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param methodName calling method
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public B getGovernanceAction(String userId,
                                 String governanceActionGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String governanceActionGUIDParameterName = "governanceActionGUID";
        final String actionTargetGUIDParameterName = "actionTargetGUID";
        final String requestSourceGUIDParameterName = "requestSourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, governanceActionGUIDParameterName, methodName);

        EntityDetail primaryEntity = this.getEntityFromRepository(userId,
                                                                  governanceActionGUID,
                                                                  governanceActionGUIDParameterName,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                  methodName);

        if (primaryEntity != null)
        {
            List<Relationship> relationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           userId,
                                                                                           governanceActionGUID,
                                                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    String actualTypeName = relationship.getType().getTypeDefName();

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME))
                    {
                        relationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME))
                    {
                        relationships.add(relationship);

                        String actionTargetGUID = relationship.getEntityTwoProxy().getGUID();

                        supplementaryEntities.add(this.getEntityFromRepository(userId,
                                                                               actionTargetGUID,
                                                                               actionTargetGUIDParameterName,
                                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                               methodName));

                    }
                    else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_NAME))
                    {
                        relationships.add(relationship);

                        String requestSourceGUID = relationship.getEntityOneProxy().getGUID();

                        supplementaryEntities.add(this.getEntityFromRepository(userId,
                                                                               requestSourceGUID,
                                                                               requestSourceGUIDParameterName,
                                                                               OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                               methodName));
                    }

                }
            }

            return converter.getNewComplexBean(beanClass,
                                               primaryEntity,
                                               supplementaryEntities,
                                               relationships,
                                               methodName);
        }

        return null;
    }


    /**
     * Retrieve the ActionStatus enum property from the instance properties of a Governance Action.
     *
     * @param propertyName name of property to extract the enum from
     * @param properties  entity properties
     * @return ActionStatus  enum value
     */
    public GovernanceActionStatus getActionStatus(String               propertyName,
                                                  InstanceProperties   properties)
    {
        GovernanceActionStatus governanceActionStatus = GovernanceActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            governanceActionStatus = GovernanceActionStatus.REQUESTED;
                            break;

                        case 1:
                            governanceActionStatus = GovernanceActionStatus.APPROVED;
                            break;

                        case 2:
                            governanceActionStatus = GovernanceActionStatus.WAITING;
                            break;

                        case 3:
                            governanceActionStatus = GovernanceActionStatus.ACTIVATING;
                            break;

                        case 4:
                            governanceActionStatus = GovernanceActionStatus.IN_PROGRESS;
                            break;

                        case 10:
                            governanceActionStatus = GovernanceActionStatus.ACTIONED;
                            break;

                        case 11:
                            governanceActionStatus = GovernanceActionStatus.INVALID;
                            break;

                        case 12:
                            governanceActionStatus = GovernanceActionStatus.IGNORED;
                            break;

                        case 13:
                            governanceActionStatus = GovernanceActionStatus.FAILED;
                            break;

                        case 99:
                            governanceActionStatus = GovernanceActionStatus.OTHER;
                            break;
                    }
                }
            }
        }

        return governanceActionStatus;
    }


    /**
     * Request that execution of a governance action is allocated to the caller.
     * This is only permitted if no other caller has claimed it.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void claimGovernanceAction(String userId,
                                      String governanceActionGUID,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           governanceActionGUID,
                                                           guidParameterName,
                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                GovernanceActionStatus status = this.getActionStatus(OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME,
                                                                     properties);

                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (((status == GovernanceActionStatus.REQUESTED) ||
                             (status == GovernanceActionStatus.APPROVED)) && (processingEngineUserId == null))
                {
                    GovernanceActionBuilder builder = new GovernanceActionBuilder(OpenMetadataAPIMapper.WAITING_GA_STATUS_ORDINAL,
                                                                                  userId,
                                                                                  repositoryHelper,
                                                                                  serviceName,
                                                                                  serverName);

                    updateBeanInRepository(userId,
                                           null,
                                           null,
                                           governanceActionGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                           builder.getClaimInstanceProperties(methodName),
                                           true,
                                           methodName);
                }
                else
                {
                    throw new PropertyServerException(GenericHandlersErrorCode.INVALID_GOVERNANCE_ACTION_STATUS.getMessageDefinition(userId,
                                                                                                                                     governanceActionGUID,
                                                                                                                                     processingEngineUserId,
                                                                                                                                     status.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION_PROPERTIES.getMessageDefinition(governanceActionGUID,
                                                                                                                                     guidParameterName,
                                                                                                                                     serviceName,
                                                                                                                                     methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION.getMessageDefinition(governanceActionGUID,
                                                                                                                      guidParameterName,
                                                                                                                      serviceName,
                                                                                                                      methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }



    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param governanceActionStatus new status ordinal
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void updateGovernanceActionStatus(String userId,
                                             String governanceActionGUID,
                                             int    governanceActionStatus,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           governanceActionGUID,
                                                           guidParameterName,
                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (userId.equals(processingEngineUserId))
                {
                    try
                    {
                        properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                null,
                                                                                OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME,
                                                                                OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                governanceActionStatus,
                                                                                methodName);
                    }
                    catch (TypeErrorException error)
                    {
                        throw new InvalidParameterException(error, OpenMetadataAPIMapper.ACTIVITY_TYPE_PROPERTY_NAME);
                    }

                    updateBeanInRepository(userId,
                                           null,
                                           null,
                                           governanceActionGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                           properties,
                                           true,
                                           methodName);
                }
                else
                {
                    throw new UserNotAuthorizedException(GenericHandlersErrorCode.INVALID_PROCESSING_USER.getMessageDefinition(userId,
                                                                                                                               methodName,
                                                                                                                               governanceActionGUID,
                                                                                                                               processingEngineUserId),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         userId);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION_PROPERTIES.getMessageDefinition(governanceActionGUID,
                                                                                                                                     guidParameterName,
                                                                                                                                     serviceName,
                                                                                                                                     methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION.getMessageDefinition(governanceActionGUID,
                                                                                                                      guidParameterName,
                                                                                                                      serviceName,
                                                                                                                      methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     *
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargetGUIDs list of additional elements to add to the action targets for the next phase
     * @param methodName calling method
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(String       userId,
                                       String       governanceActionGUID,
                                       int          status,
                                       List<String> outputGuards,
                                       List<String> newActionTargetGUIDs,
                                       String       methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           governanceActionGUID,
                                                           guidParameterName,
                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (userId.equals(processingEngineUserId))
                {
                    GovernanceActionBuilder builder = new GovernanceActionBuilder(status,
                                                                                  new Date(),
                                                                                  outputGuards,
                                                                                  repositoryHelper,
                                                                                  serviceName,
                                                                                  serverName);

                    updateBeanInRepository(userId,
                                           null,
                                           null,
                                           governanceActionGUID,
                                           guidParameterName,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                           builder.getCompletionInstanceProperties(methodName),
                                           true,
                                           methodName);

                    this.addActionTargets(userId,
                                          governanceActionGUID,
                                          guidParameterName,
                                          newActionTargetGUIDs,
                                          methodName);
                }
                else
                {
                    throw new UserNotAuthorizedException(GenericHandlersErrorCode.INVALID_PROCESSING_USER.getMessageDefinition(userId,
                                                                                                                               methodName,
                                                                                                                               governanceActionGUID,
                                                                                                                               processingEngineUserId),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         userId);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION_PROPERTIES.getMessageDefinition(governanceActionGUID,
                                                                                                                                     guidParameterName,
                                                                                                                                     serviceName,
                                                                                                                                     methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_GOVERNANCE_ACTION.getMessageDefinition(governanceActionGUID,
                                                                                                                      guidParameterName,
                                                                                                                      serviceName,
                                                                                                                      methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param methodName calling method
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B>  getActiveClaimedGovernanceActions(String userId,
                                                      String governanceEngineGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "governanceEngineGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        // todo

        return null;
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param userId caller's userId
     * @param actionTargetGUID unique identifier of the governance action service
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target
     * @param methodName calling method
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void updateActionTargetStatus(String userId,
                                         String actionTargetGUID,
                                         int    status,
                                         Date   startDate,
                                         Date   completionDate,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String guidParameterName = "actionTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTargetGUID, guidParameterName, methodName);


        // todo
    }

}
