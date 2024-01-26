/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryEntitiesIterator;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositorySelectedEntitiesIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;

/**
 * EngineActionHandler manages EngineAction objects from the Governance Action Framework (GAF).
 * These objects are 1-1 with an open metadata entity.
 */
public class EngineActionHandler<B> extends OpenMetadataAPIGenericHandler<B>
{

    /**
     * Construct the handler for engine actions.
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
     * @param serviceSupportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public EngineActionHandler(OpenMetadataAPIGenericConverter<B> converter,
                               Class<B>                           beanClass,
                               String                             serviceName,
                               String                             serverName,
                               InvalidParameterHandler            invalidParameterHandler,
                               RepositoryHandler                  repositoryHandler,
                               OMRSRepositoryHelper               repositoryHelper,
                               String                             localServerUserId,
                               OpenMetadataServerSecurityVerifier securityVerifier,
                               List<String>                       serviceSupportedZones,
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
              serviceSupportedZones,
              defaultZones,
              publishZones,
              auditLog);

    }



    /**
     * Using the named governance action type as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param governanceActionTypeQualifiedName unique name to give this governance action type
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param requestParameters initial set of request parameters from the caller
     * @param startTime future start time or null for "as soon as possible"
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return unique identifier of the first governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceActionType(String                userId,
                                               String                governanceActionTypeQualifiedName,
                                               List<String>          requestSourceGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               Map<String, String>   requestParameters,
                                               Date                  startTime,
                                               String                originatorServiceName,
                                               String                originatorEngineName,
                                               List<String>          serviceSupportedZones,
                                               String                methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName  = "governanceActionTypeQualifiedName";

        /*
         * Effective time is set to "now" so that the governance action type definition that is active now is used.
         */
        Date effectiveTime = new Date();

        List<String> propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        EntityDetail governanceActionTypeEntity = this.getEntityByValue(userId,
                                                                        governanceActionTypeQualifiedName,
                                                                        qualifiedNameParameterName,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                        propertyNames,
                                                                        false,
                                                                        false,
                                                                        serviceSupportedZones,
                                                                        effectiveTime,
                                                                        methodName);

        if (governanceActionTypeEntity != null)
        {
            return prepareEngineActionFromType(userId,
                                               governanceActionTypeEntity,
                                               startTime,
                                               requestParameters,
                                               requestSourceGUIDs,
                                               actionTargets,
                                               governanceActionTypeQualifiedName,
                                               originatorServiceName,
                                               originatorEngineName,
                                               serviceSupportedZones,
                                               methodName);
        }
        else
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_GOVERNANCE_ACTION_TYPE.getMessageDefinition(governanceActionTypeQualifiedName),
                                                this.getClass().getName(),
                                                methodName,
                                                qualifiedNameParameterName);
        }
    }


    /**
     * Prepare the next engine action to follow-on from the previous request.  It may not run immediately if
     * there are outstanding guards.
     *
     * @param userId caller's userId
     * @param governanceActionTypeEntity entity to create engine action from
     * @param suppliedStartTime has the caller requested a start time?
     * @param initialRequestParameters request parameters  from the caller
     * @param requestSourceGUIDs identifiers of the request sources
     * @param actionTargets action targets for the next governance action
     * @param requestSourceName name of calling source
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     * @return unique identifier of the prepared governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private String prepareEngineActionFromType(String                userId,
                                               EntityDetail          governanceActionTypeEntity,
                                               Date                  suppliedStartTime,
                                               Map<String, String>   initialRequestParameters,
                                               List<String>          requestSourceGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               String                requestSourceName,
                                               String                originatorServiceName,
                                               String                originatorEngineName,
                                               List<String>          serviceSupportedZones,
                                               String                methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String guidParameterName = "governanceActionTypeEntity.getGUID()";

        /*
         * Extract interesting properties from the governance action type entity.
         */
        String governanceActionTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             governanceActionTypeEntity.getProperties(),
                                                                             methodName);
        int domainIdentifier = repositoryHelper.getIntProperty(serviceName,
                                                               OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               governanceActionTypeEntity.getProperties(),
                                                               methodName);
        String displayName = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                governanceActionTypeEntity.getProperties(),
                                                                methodName);
        String description = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                governanceActionTypeEntity.getProperties(),
                                                                methodName);

        int waitTime = repositoryHelper.getIntProperty(serviceName,
                                                       OpenMetadataType.WAIT_TIME_PROPERTY_NAME,
                                                       governanceActionTypeEntity.getProperties(),
                                                       methodName);

        Date startDate = suppliedStartTime;

        if (suppliedStartTime == null)
        {
            long startTime  = new Date().getTime() + (waitTime * 1000L); // waitTime is in minutes

            startDate = new Date(startTime);
        }

        /*
         * Locate the governance engine (via the GovernanceActionExecutor relationship)
         */
        Relationship governanceActionExecutorRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                          governanceActionTypeEntity.getGUID(),
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                                          true,
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID,
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                                                          false,
                                                                                                          false,
                                                                                                          null,
                                                                                                          methodName);

        if (governanceActionExecutorRelationship == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_EXECUTOR.getMessageDefinition(governanceActionTypeEntity.getGUID(),
                                                                                                               OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }


        String governanceEngineName = repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         governanceActionExecutorRelationship.getEntityTwoProxy().getUniqueProperties(),
                                                                         methodName);
        String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.REQUEST_TYPE.name,
                                                                governanceActionExecutorRelationship.getProperties(),
                                                                methodName);

        Map<String, String> requestParameters = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                          OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                          governanceActionExecutorRelationship.getProperties(),
                                                                                          methodName);

        if ((initialRequestParameters != null) && (! initialRequestParameters.isEmpty()))
        {
            /*
             * Overlay the request parameters from the model with those supplied by the caller
             */
            if (requestParameters == null)
            {
                requestParameters = initialRequestParameters;
            }
            else
            {
                requestParameters.putAll(initialRequestParameters);
            }
        }


        String engineActionGUID = createEngineAction(userId,
                                                     governanceActionTypeName + ":" + UUID.randomUUID(),
                                                     domainIdentifier,
                                                     displayName,
                                                     description,
                                                     requestSourceGUIDs,
                                                     actionTargets,
                                                     null,
                                                     null,
                                                     startDate,
                                                     governanceEngineName,
                                                     requestType,
                                                     requestParameters,
                                                     governanceActionTypeEntity.getGUID(),
                                                     governanceActionTypeName,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     requestSourceName,
                                                     originatorServiceName,
                                                     originatorEngineName,
                                                     serviceSupportedZones,
                                                     methodName);


        if (engineActionGUID != null)
        {
            /*
             * Start the engine action running if all conditions are satisfied.
             */
            runEngineActionIfReady(userId,
                                   engineActionGUID,
                                   governanceActionTypeName + ":" + UUID.randomUUID(),
                                   null,
                                   startDate,
                                   governanceEngineName,
                                   requestType,
                                   requestParameters,
                                   governanceActionTypeName,
                                   requestSourceName,
                                   serviceSupportedZones,
                                   methodName);

            return engineActionGUID;
        }

        return null;
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name to give this governance action process
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param requestParameters initial set of request parameters from the caller
     * @param startTime future start time or null for "as soon as possible"
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return unique identifier of the first governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceActionProcess(String                userId,
                                                  String                processQualifiedName,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Map<String, String>   requestParameters,
                                                  Date                  startTime,
                                                  String                originatorServiceName,
                                                  String                originatorEngineName,
                                                  List<String>          serviceSupportedZones,
                                                  String                methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String qualifiedNameParameterName  = "processQualifiedName";
        final String governanceActionProcessStepGUIDParameterName  = "governanceActionProcessFlowRelationship.getEntityTwoProxy().getGUID()";

        /*
         * Effective time is set to "now" so that the process definition that is active now is
         * used.
         */
        Date effectiveTime = new Date();

        String governanceActionProcessGUID = this.getBeanGUIDByUniqueName(userId,
                                                                          processQualifiedName,
                                                                          qualifiedNameParameterName,
                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                          false,
                                                                          false,
                                                                          serviceSupportedZones,
                                                                          effectiveTime,
                                                                          methodName);

        if (governanceActionProcessGUID != null)
        {
            Relationship governanceActionFlowRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                          governanceActionProcessGUID,
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                                          true,
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_GUID,
                                                                                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME,
                                                                                                          false,
                                                                                                          false,
                                                                                                          effectiveTime,
                                                                                                          methodName);

            if (governanceActionFlowRelationship != null)
            {
                String governanceActionProcessStepGUID = governanceActionFlowRelationship.getEntityTwoProxy().getGUID();

                String guard = repositoryHelper.getStringProperty(serviceName,
                                                                  OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                  governanceActionFlowRelationship.getProperties(),
                                                                  methodName);

                return prepareEngineActionFromProcessStep(userId,
                                                          null,
                                                          governanceActionProcessStepGUID,
                                                          governanceActionProcessStepGUIDParameterName,
                                                          guard,
                                                          false,
                                                          startTime,
                                                          null,
                                                          requestParameters,
                                                          requestSourceGUIDs,
                                                          actionTargets,
                                                          processQualifiedName + UUID.randomUUID(), // Unique identifier for the process instance
                                                          processQualifiedName,
                                                          originatorServiceName,
                                                          originatorEngineName,
                                                          serviceSupportedZones,
                                                          methodName);
            }
            else
            {
                throw new InvalidParameterException(GenericHandlersErrorCode.NO_PROCESS_IMPLEMENTATION.getMessageDefinition(processQualifiedName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    qualifiedNameParameterName);
            }
        }
        else
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_PROCESS.getMessageDefinition(processQualifiedName),
                                                this.getClass().getName(),
                                                methodName,
                                                qualifiedNameParameterName);
        }
    }


    /**
     * Prepare the next engine action to follow-on from the previous request.  It may not run immediately if
     * there are outstanding guards.
     *
     * @param userId caller's userId
     * @param anchorGUID the unique identifier of the first engine action in the governance action process (if any)
     * @param governanceActionProcessStepGUID unique identifier of the governance action process step
     * @param governanceActionProcessStepGUIDParameterName parameter supplying governanceActionProcessStepGUID
     * @param guard guard that triggered this action
     * @param mandatoryGuard is this guard mandatory?
     * @param suppliedStartTime has the caller requested a start time?
     * @param previousEngineActionGUID unique identifier of the previous engine action
     * @param initialRequestParameters request parameters  from the caller
     * @param requestSourceGUIDs identifiers of the request sources
     * @param newActionTargets action targets for the next governance action
     * @param processName name of process this is a part of
     * @param requestSourceName name of calling source
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     * @return unique identifier of the prepared governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private String prepareEngineActionFromProcessStep(String                userId,
                                                      String                anchorGUID,
                                                      String                governanceActionProcessStepGUID,
                                                      String                governanceActionProcessStepGUIDParameterName,
                                                      String                guard,
                                                      boolean               mandatoryGuard,
                                                      Date                  suppliedStartTime,
                                                      String                previousEngineActionGUID,
                                                      Map<String, String>   initialRequestParameters,
                                                      List<String>          requestSourceGUIDs,
                                                      List<NewActionTarget> newActionTargets,
                                                      String                processName,
                                                      String                requestSourceName,
                                                      String                originatorServiceName,
                                                      String                originatorEngineName,
                                                      List<String>          serviceSupportedZones,
                                                      String                methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        Relationship governanceActionProcessStepExecutorRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                                     governanceActionProcessStepGUID,
                                                                                                                     OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                                     true,
                                                                                                                     OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID,
                                                                                                                     OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                                                                     false,
                                                                                                                     false,
                                                                                                                     null,
                                                                                                                     methodName);

        if (governanceActionProcessStepExecutorRelationship == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_EXECUTOR.getMessageDefinition(governanceActionProcessStepGUID,
                                                                                                               OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME),
                                                this.getClass().getName(),
                                                methodName,
                                                governanceActionProcessStepGUIDParameterName);
        }

        EntityDetail governanceActionProcessStepEntity = this.getEntityFromRepository(userId,
                                                                                      governanceActionProcessStepGUID,
                                                                                      governanceActionProcessStepGUIDParameterName,
                                                                                      OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                      null,
                                                                                      null,
                                                                                      false,
                                                                                      false,
                                                                                      serviceSupportedZones,
                                                                                      null,
                                                                                      methodName);

        boolean ignoreMultipleTriggers = repositoryHelper.getBooleanProperty(serviceName,
                                                                             OpenMetadataType.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
                                                                             governanceActionProcessStepEntity.getProperties(),
                                                                             methodName);

        String governanceActionProcessStepName = repositoryHelper.getStringProperty(serviceName,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    governanceActionProcessStepEntity.getProperties(),
                                                                                    methodName);
        int domainIdentifier = repositoryHelper.getIntProperty(serviceName,
                                                               OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               governanceActionProcessStepEntity.getProperties(),
                                                               methodName);
        String displayName = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                governanceActionProcessStepEntity.getProperties(),
                                                                methodName);
        String description = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                governanceActionProcessStepEntity.getProperties(),
                                                                methodName);

        int waitTime = repositoryHelper.getIntProperty(serviceName,
                                                       OpenMetadataType.WAIT_TIME_PROPERTY_NAME,
                                                       governanceActionProcessStepEntity.getProperties(),
                                                       methodName);

        Date startDate = suppliedStartTime;

        if (suppliedStartTime == null)
        {
            long startTime  = new Date().getTime() + (waitTime * 1000L); // waitTime is in minutes

            startDate = new Date(startTime);
        }

        List<String> receivedGuards = null;

        if (guard != null)
        {
            receivedGuards = new ArrayList<>();

            receivedGuards.add(guard);
        }

        String governanceEngineName = repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         governanceActionProcessStepExecutorRelationship.getEntityTwoProxy().getUniqueProperties(),
                                                                         methodName);
        String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataProperty.REQUEST_TYPE.name,
                                                                governanceActionProcessStepExecutorRelationship.getProperties(),
                                                                methodName);

        Map<String, String> requestParameters = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                          OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                          governanceActionProcessStepExecutorRelationship.getProperties(),
                                                                                          methodName);

        if ((initialRequestParameters != null) && (! initialRequestParameters.isEmpty()))
        {
            /*
             * Overlay the request parameters from the model with those supplied by the caller
             */
            if (requestParameters == null)
            {
                requestParameters = initialRequestParameters;
            }
            else
            {
                requestParameters.putAll(initialRequestParameters);
            }
        }

        List<String> mandatoryGuards = this.getMandatoryGuards(userId, governanceActionProcessStepGUID);

        /*
         * If the anchorGUID is null, it means the previous engine action was the first in the process.
         * Subsequent governance actions will have the first engine action as their anchorGUID.
         */
        String newAnchorGUID = anchorGUID;

        if (anchorGUID == null)
        {
            newAnchorGUID = previousEngineActionGUID;
        }

        String engineActionGUID = getEngineActionForProcessStep(userId,
                                                                governanceActionProcessStepName + ":" + UUID.randomUUID(),
                                                                domainIdentifier,
                                                                displayName,
                                                                description,
                                                                requestSourceGUIDs,
                                                                newActionTargets,
                                                                mandatoryGuards,
                                                                ignoreMultipleTriggers,
                                                                receivedGuards,
                                                                startDate,
                                                                governanceEngineName,
                                                                requestType,
                                                                requestParameters,
                                                                governanceActionProcessStepGUID,
                                                                governanceActionProcessStepName,
                                                                newAnchorGUID,
                                                                processName,
                                                                requestSourceName,
                                                                originatorServiceName,
                                                                originatorEngineName,
                                                                serviceSupportedZones,
                                                                methodName);


        if ((engineActionGUID != null) && (previousEngineActionGUID != null))
        {
            /*
             * Link the next engine action to the previous one
             */
            InstanceProperties nextEngineActionProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                         null,
                                                                                                         OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                                                         guard,
                                                                                                         methodName);

            nextEngineActionProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                       nextEngineActionProperties,
                                                                                       OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                       mandatoryGuard,
                                                                                       methodName);

            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataType.NEXT_ENGINE_ACTION_TYPE_GUID,
                                                 null,
                                                 null,
                                                 previousEngineActionGUID,
                                                 engineActionGUID,
                                                 nextEngineActionProperties,
                                                 methodName);
        }

        if (engineActionGUID != null)
        {
            /*
             * Start the engine action running if all conditions are satisfied.
             */
            runEngineActionIfReady(userId,
                                   engineActionGUID,
                                   governanceActionProcessStepName + ":" + UUID.randomUUID(),
                                   mandatoryGuards,
                                   startDate,
                                   governanceEngineName,
                                   requestType,
                                   requestParameters,
                                   governanceActionProcessStepName,
                                   requestSourceName,
                                   serviceSupportedZones,
                                   methodName);

            return engineActionGUID;
        }

        return null;
    }


    /**
     * Create an engine action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param engineActionGUID unique identifier of the engine action
     * @param qualifiedName unique identifier to give this governance action
     * @param mandatoryGuards list of guards that have to be received
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance service to run
     * @param requestParameters properties to pass to the governance service
     * @param governanceActionProcessStepName unique name of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone engine action)
     * @param requestSourceName where did the request come from
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void runEngineActionIfReady(String                userId,
                                        String                engineActionGUID,
                                        String                qualifiedName,
                                        List<String>          mandatoryGuards,
                                        Date                  startTime,
                                        String                governanceEngineName,
                                        String                requestType,
                                        Map<String, String>   requestParameters,
                                        String                governanceActionProcessStepName,
                                        String                requestSourceName,
                                        List<String>          serviceSupportedZones,
                                        String                methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        /*
         * Gather details of the guards received from previously run governance actions.
         */
        List<Relationship> previousResults = repositoryHandler.getRelationshipsByType(userId,
                                                                                      engineActionGUID,
                                                                                      OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                                      OpenMetadataType.NEXT_ENGINE_ACTION_TYPE_GUID,
                                                                                      OpenMetadataType.NEXT_ENGINE_ACTION_TYPE_NAME,
                                                                                      1,
                                                                                      false,
                                                                                      false,
                                                                                      0,
                                                                                      0,
                                                                                      null,
                                                                                      methodName);
        List<String> receivedGuards = new ArrayList<>();

        if (previousResults != null)
        {
            /*
             * There are potential follow-on actions.  Need to loop though each one to evaluate if the output guards
             * permit it to execute.
             */

            for (Relationship previousResult : previousResults)
            {
                /*
                 * Make sure we are looking backwards through the process not forwards.
                 */
                if ((previousResult != null) && (! engineActionGUID.equals(previousResult.getEntityOneProxy().getGUID())))
                {
                    /*
                     * The guard property in the relationship must match one of the output guards, or it must be null
                     * to allow the engine action to proceed.
                     */
                    String guard = repositoryHelper.getStringProperty(serviceName,
                                                                      OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                      previousResult.getProperties(),
                                                                      methodName);

                    receivedGuards.add(guard);
                }
            }
        }

        if ((mandatoryGuards == null) || (mandatoryGuards.isEmpty()) || (new HashSet<>(receivedGuards).containsAll(mandatoryGuards)))
        {
            this.approveEngineAction(userId,
                                     engineActionGUID,
                                     qualifiedName,
                                     mandatoryGuards,
                                     receivedGuards,
                                     startTime,
                                     governanceEngineName,
                                     requestType,
                                     requestParameters,
                                     governanceActionProcessStepName,
                                     requestSourceName,
                                     serviceSupportedZones,
                                     methodName);
        }
    }



    /**
     * Move an engine action from REQUESTED state to APPROVED status and log an audit message to say that the engine action
     * is starting.
     *
     * @param userId caller's userId
     * @param engineActionGUID unique identifier of the engine action
     * @param qualifiedName unique identifier of this engine action
     * @param mandatoryGuards list of guards that must be received in order to proceed with the engine action
     * @param receivedGuards list of guards to initiate the engine action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance service to run
     * @param requestParameters properties to pass to the governance service
     * @param governanceActionProcessStepName unique name of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone engine action)
     * @param requestSourceName where did the request come from
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String approveEngineAction(String                userId,
                                      String                engineActionGUID,
                                      String                qualifiedName,
                                      List<String>          mandatoryGuards,
                                      List<String>          receivedGuards,
                                      Date                  startTime,
                                      String                governanceEngineName,
                                      String                requestType,
                                      Map<String, String>   requestParameters,
                                      String                governanceActionProcessStepName,
                                      String                requestSourceName,
                                      List<String>          serviceSupportedZones,
                                      String                methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String engineNameParameterName     = "governanceEngineName";
        final String requestTypeParameterName    = "requestType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);
        invalidParameterHandler.validateName(requestType, requestTypeParameterName, methodName);

        /*
         * Log details of the requested governance action.
         */
        String receivedGuardsString  = "<null>";
        String mandatoryGuardsString = "<null>";
        String startTimeString       = "<now>";
        String requestParameterNames = "<null>";

        if (receivedGuards != null)
        {
            receivedGuardsString = receivedGuards.toString();
        }

        if (mandatoryGuards != null)
        {
            mandatoryGuardsString = mandatoryGuards.toString();
        }

        if (startTime != null)
        {
            startTimeString = startTime.toString();
        }

        if (requestParameters != null)
        {
            requestParameterNames = requestParameters.keySet().toString();
        }

        if (governanceActionProcessStepName != null)
        {
            auditLog.logMessage(methodName, GenericHandlersAuditCode.INITIATE_ENGINE_ACTION_FROM_PROCESS_STEP.getMessageDefinition(qualifiedName,
                                                                                                                                   governanceActionProcessStepName,
                                                                                                                                   requestType,
                                                                                                                                   governanceEngineName,
                                                                                                                                   receivedGuardsString,
                                                                                                                                   mandatoryGuardsString,
                                                                                                                                   requestParameterNames,
                                                                                                                                   startTimeString,
                                                                                                                                   requestSourceName));
        }
        else
        {
            auditLog.logMessage(methodName, GenericHandlersAuditCode.INITIATE_ENGINE_ACTION.getMessageDefinition(qualifiedName,
                                                                                                                 requestType,
                                                                                                                 governanceEngineName,
                                                                                                                 receivedGuardsString,
                                                                                                                 mandatoryGuardsString,
                                                                                                                 requestParameterNames,
                                                                                                                 startTimeString,
                                                                                                                 requestSourceName));
        }

        this.updateEngineActionStatus(userId,
                                      engineActionGUID,
                                      OpenMetadataType.APPROVED_EA_STATUS_ORDINAL,
                                      serviceSupportedZones,
                                      null,
                                      methodName);

        return engineActionGUID;
    }


    /**
     * Create an engine action in REQUESTED state in the metadata store with all the relationships, so it is ready to execute.
     * Nothing will happen until it moves to APPROVED state.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param mandatoryGuards list of guards that must be received in order to proceed with the governance action
     * @param receivedGuards list of guards to initiate the governance action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance service to run
     * @param requestParameters properties to pass to the governance service
     * @param governanceActionTypeGUID unique identifier for the governance action type (if any)
     * @param governanceActionTypeName unique name for the governance action type (if any)
     * @param anchorGUID identifier of the first engine action of the process (null for standalone engine actions and the first engine
     *                   action in a governance action process).
     * @param processStepGUID unique identifier of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone governance action)
     * @param processStepName unique name of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone governance action)
     * @param processName name of the process
     * @param requestSourceName where did the request come from
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createEngineAction(String                userId,
                                     String                qualifiedName,
                                     int                   domainIdentifier,
                                     String                displayName,
                                     String                description,
                                     List<String>          requestSourceGUIDs,
                                     List<NewActionTarget> actionTargets,
                                     List<String>          mandatoryGuards,
                                     List<String>          receivedGuards,
                                     Date                  startTime,
                                     String                governanceEngineName,
                                     String                requestType,
                                     Map<String, String>   requestParameters,
                                     String                governanceActionTypeGUID,
                                     String                governanceActionTypeName,
                                     String                anchorGUID,
                                     String                processName,
                                     String                processStepGUID,
                                     String                processStepName,
                                     String                requestSourceName,
                                     String                originatorServiceName,
                                     String                originatorEngineName,
                                     List<String>          serviceSupportedZones,
                                     String                methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String engineNameParameterName     = "governanceEngineName";
        final String requestTypeParameterName    = "requestType";
        final String anchorGUIDParameterName     = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);
        invalidParameterHandler.validateName(requestType, requestTypeParameterName, methodName);

        /*
         * Effective time is set to "any time" and all elements that make up the governance action
         * are set up without effectivity dates.  Any control on start time is done using the startTime property.
         */
        String governanceEngineGUID = this.validateGovernanceEngineName(userId,
                                                                        governanceEngineName,
                                                                        engineNameParameterName,
                                                                        requestType,
                                                                        serviceSupportedZones,
                                                                        methodName);

        EngineActionBuilder builder = new EngineActionBuilder(qualifiedName,
                                                              domainIdentifier,
                                                              displayName,
                                                              description,
                                                              governanceEngineGUID,
                                                              governanceEngineName,
                                                              governanceActionTypeGUID,
                                                              governanceActionTypeName,
                                                              processName,
                                                              processStepGUID,
                                                              processStepName,
                                                              requestType,
                                                              requestParameters,
                                                              mandatoryGuards,
                                                              receivedGuards,
                                                              OpenMetadataType.REQUESTED_EA_STATUS_ORDINAL,
                                                              startTime,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameterName,
                                    false,
                                    false,
                                    null,
                                    serviceSupportedZones,
                                    builder,
                                    methodName);

        String engineActionGUID = this.createBeanInRepository(userId,
                                                              null,
                                                              null,
                                                              OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                              OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                              builder,
                                                              null,
                                                              methodName);

        if (engineActionGUID != null)
        {
            final String engineActionGUIDParameterName = "engineActionGUID";

            /*
             * Show the relationship to a governance action process step if this engine action originated from a governance action process.
             */
            InstanceProperties originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                   null,
                                                                                                   OpenMetadataType.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
                                                                                                   originatorServiceName,
                                                                                                   methodName);

            originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                originatorProperties,
                                                                                OpenMetadataType.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
                                                                                originatorEngineName,
                                                                                methodName);


            /*
             * Identify the source of the work
             */
            if ((requestSourceGUIDs != null) && (! requestSourceGUIDs.isEmpty()))
            {
                final String requestSourceGUIDParameterName = "requestSourceGUIDs[x]";

                originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                    originatorProperties,
                                                                                    OpenMetadataType.REQUEST_SOURCE_NAME_PROPERTY_NAME,
                                                                                    requestSourceName,
                                                                                    methodName);

                for (String requestSourceGUID : requestSourceGUIDs)
                {
                    if (requestSourceGUID != null)
                    {
                        this.linkElementToElement(userId,
                                                  null,
                                                  null,
                                                  requestSourceGUID,
                                                  requestSourceGUIDParameterName,
                                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                  engineActionGUID,
                                                  engineActionGUIDParameterName,
                                                  OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                  true,
                                                  true,
                                                  serviceSupportedZones,
                                                  OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE_TYPE_GUID,
                                                  OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE_TYPE_NAME,
                                                  originatorProperties,
                                                  null,
                                                  null,
                                                  null,
                                                  methodName);
                    }
                }
            }

            /*
             * Identify the objects to work on
             */
            this.addActionTargets(userId,
                                  engineActionGUID,
                                  engineActionGUIDParameterName,
                                  qualifiedName,
                                  actionTargets,
                                  serviceSupportedZones,
                                  methodName);

        }

        return engineActionGUID;
    }


    /**
     * Create an engine action in REQUESTED state in the metadata store with all the relationships, so it is ready to execute.
     * Nothing will happen until it moves to APPROVED state.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param mandatoryGuards list of guards that must be received in order to proceed with the governance action
     * @param ignoreMultipleTriggers only run one engine action for this governance action process step
     * @param receivedGuards list of guards to initiate the governance action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance service to run
     * @param requestParameters properties to pass to the governance service
     * @param anchorGUID identifier of the first engine action of the process (null for standalone engine actions and the first engine
     *                   action in a governance action process).
     * @param governanceActionProcessStepGUID unique identifier of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone engine action)
     * @param governanceActionProcessStepName unique name of the governance action process step that initiated this engine action as part of
     *                                 a governance action process (or null if this is standalone engine action)
     * @param processName name of process this is a part of
     * @param requestSourceName where did the request come from
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private synchronized String getEngineActionForProcessStep(String                userId,
                                                              String                qualifiedName,
                                                              int                   domainIdentifier,
                                                              String                displayName,
                                                              String                description,
                                                              List<String>          requestSourceGUIDs,
                                                              List<NewActionTarget> actionTargets,
                                                              List<String>          mandatoryGuards,
                                                              boolean               ignoreMultipleTriggers,
                                                              List<String>          receivedGuards,
                                                              Date                  startTime,
                                                              String                governanceEngineName,
                                                              String                requestType,
                                                              Map<String, String>   requestParameters,
                                                              String                governanceActionProcessStepGUID,
                                                              String                governanceActionProcessStepName,
                                                              String                anchorGUID,
                                                              String                processName,
                                                              String                requestSourceName,
                                                              String                originatorServiceName,
                                                              String                originatorEngineName,
                                                              List<String>          serviceSupportedZones,
                                                              String                methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String engineNameParameterName     = "governanceEngineName";
        final String requestTypeParameterName    = "requestType";
        final String nameParameterName           = "governanceActionProcessStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);
        invalidParameterHandler.validateName(requestType, requestTypeParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataProperty.PROCESS_NAME.name);

        /*
         * This will return all the engine actions associated with this process.
         */
        // todo may want to switch this to an iterator if we have very large processes of better still,
        // todo a single search query.
        List<EntityDetail> entities =  this.getEntitiesByValue(userId,
                                                               processName,
                                                               nameParameterName,
                                                               OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                               OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                               specificMatchPropertyNames,
                                                               true,
                                                               false,
                                                               null,
                                                               null,
                                                               false,
                                                               false,
                                                               serviceSupportedZones,
                                                               null,
                                                               0,
                                                               0,
                                                               null,
                                                               methodName);

        if (entities != null)
        {
            String selectedEngineAction = null;

            for (EntityDetail entity : entities)
            {
                String entityGovernanceActionProcessStepGUID = repositoryHelper.getStringProperty(serviceName,
                                                                                                  OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                                                                  entity.getProperties(),
                                                                                                  methodName);

                if (governanceActionProcessStepGUID.equals(entityGovernanceActionProcessStepGUID))
                {
                    if (ignoreMultipleTriggers)
                    {
                        /*
                         * Only allowed one triggering of this engine action
                         */
                        return null;
                    }

                    /*
                     * Look for an engine action in REQUESTED state.
                     */
                    EngineActionStatus status = this.getActionStatus(OpenMetadataType.ACTION_STATUS_PROPERTY_NAME, entity.getProperties());

                    if (status == EngineActionStatus.REQUESTED)
                    {
                        selectedEngineAction = entity.getGUID();
                    }
                }
            }

            if (selectedEngineAction != null)
            {
                return selectedEngineAction;
            }
        }

        /*
         * No governance actions associated with the governance action process step in REQUESTED state so create a new one.
         */
        return createEngineAction(userId,
                                  governanceActionProcessStepName + ":" + UUID.randomUUID(),
                                  domainIdentifier,
                                  displayName,
                                  description,
                                  requestSourceGUIDs,
                                  actionTargets,
                                  mandatoryGuards,
                                  receivedGuards,
                                  startTime,
                                  governanceEngineName,
                                  requestType,
                                  requestParameters,
                                  null,
                                  null,
                                  governanceActionProcessStepGUID,
                                  governanceActionProcessStepName,
                                  anchorGUID,
                                  processName,
                                  requestSourceName,
                                  originatorServiceName,
                                  originatorEngineName,
                                  serviceSupportedZones,
                                  methodName);
    }


    /**
     * Link up the supplied action targets.
     *
     * @param userId calling user
     * @param engineActionGUID unique identifier of the engine action to connector the targets to
     * @param engineActionGUIDParameterName parameter passing the engineActionGUID
     * @param engineActionName name of engine action - for logging
     * @param actionTargets map of action target names to GUIDs for the resulting governance service
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance  service is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void addActionTargets(String                userId,
                                  String                engineActionGUID,
                                  String                engineActionGUIDParameterName,
                                  String                engineActionName,
                                  List<NewActionTarget> actionTargets,
                                  List<String>          serviceSupportedZones,
                                  String                methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        if ((actionTargets != null) && (! actionTargets.isEmpty()))
        {
            final String actionTargetGUIDParameterName = "actionTargets[x]";

            for (NewActionTarget actionTarget : actionTargets)
            {
                if (actionTarget != null)
                {
                    String actionTargetGUID = actionTarget.getActionTargetGUID();
                    String actionTargetName = actionTarget.getActionTargetName();

                    if (actionTargetGUID != null)
                    {
                        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                     null,
                                                                                                     OpenMetadataType.ACTION_TARGET_NAME_PROPERTY_NAME,
                                                                                                     actionTargetName,
                                                                                                     methodName);

                        this.linkElementToElement(userId,
                                                  null,
                                                  null,
                                                  engineActionGUID,
                                                  engineActionGUIDParameterName,
                                                  OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                  actionTargetGUID,
                                                  actionTargetGUIDParameterName,
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  true,
                                                  true,
                                                  serviceSupportedZones,
                                                  OpenMetadataType.TARGET_FOR_ACTION_TYPE_GUID,
                                                  OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME,
                                                  properties,
                                                  null,
                                                  null,
                                                  null,
                                                  methodName);

                        auditLog.logMessage(methodName, GenericHandlersAuditCode.ADD_ACTION_TARGETS.getMessageDefinition(actionTargetName,
                                                                                                                         actionTargetGUID,
                                                                                                                         engineActionName,
                                                                                                                         engineActionGUID));
                    }
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
     * @param requestType requested function
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     * @return unique identifier of the governance engine
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private String validateGovernanceEngineName(String       userId,
                                                String       governanceEngineName,
                                                String       governanceEngineNameParameterName,
                                                String       requestType,
                                                List<String> serviceSupportedZones,
                                                String       methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        String governanceEngineGUID = this.getBeanGUIDByUniqueName(userId,
                                                                   governanceEngineName,
                                                                   governanceEngineNameParameterName,
                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                   OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                                   OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                   false,
                                                                   false,
                                                                   serviceSupportedZones,
                                                                   new Date(),
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

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   governanceEngineGUID,
                                                                   governanceEngineNameParameterName,
                                                                   OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                   OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                   null,
                                                                   OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                   2,
                                                                   false,
                                                                   false,
                                                                   serviceSupportedZones,
                                                                   0,
                                                                   0,
                                                                   new Date(),
                                                                   methodName);

        if (relationships == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.NO_REQUEST_TYPE_FOR_ENGINE.getMessageDefinition(governanceEngineName,
                                                                                                                         governanceEngineGUID,
                                                                                                                         requestType,
                                                                                                                         serviceName,
                                                                                                                         serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                governanceEngineNameParameterName);
        }

        for (Relationship relationship : relationships)
        {
            String relationshipRequestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                relationship.getProperties(),
                                                                                methodName);

            if (requestType.equals(relationshipRequestType))
            {
                return governanceEngineGUID;
            }
        }

        throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_REQUEST_TYPE.getMessageDefinition(governanceEngineName,
                                                                                                               governanceEngineGUID,
                                                                                                               requestType,
                                                                                                               serviceName,
                                                                                                               serverName),
                                            this.getClass().getName(),
                                            methodName,
                                            governanceEngineNameParameterName);
    }


    /**
     * Request the status of an executing engine action request.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param effectiveTime   the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public B getEngineAction(String       userId,
                             String       engineActionGUID,
                             List<String> serviceSupportedZones,
                             Date         effectiveTime,
                             String       methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String engineActionGUIDParameterName = "engineActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, engineActionGUIDParameterName, methodName);

        EntityDetail primaryEntity = this.getEntityFromRepository(userId,
                                                                  engineActionGUID,
                                                                  engineActionGUIDParameterName,
                                                                  OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  serviceSupportedZones,
                                                                  effectiveTime,
                                                                  methodName);

        return this.getEngineAction(userId, primaryEntity, serviceSupportedZones, effectiveTime, methodName);
    }

    /**
     * Request the status of an executing engine action request.
     *
     * @param userId identifier of calling user
     * @param primaryEntity entity of the engine action request
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return status enum
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public B getEngineAction(String       userId,
                             EntityDetail primaryEntity,
                             List<String> serviceSupportedZones,
                             Date         effectiveTime,
                             String       methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String actionTargetGUIDParameterName = "actionTargetGUID";
        final String requestSourceGUIDParameterName = "requestSourceGUID";

        if (primaryEntity != null)
        {
            List<Relationship> relationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           primaryEntity.getGUID(),
                                                                                           OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           false,
                                                                                           false,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    String actualTypeName = relationship.getType().getTypeDefName();

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME))
                    {
                        relationships.add(relationship);

                        String actionTargetGUID = relationship.getEntityTwoProxy().getGUID();

                        supplementaryEntities.add(this.getEntityFromRepository(userId,
                                                                               actionTargetGUID,
                                                                               actionTargetGUIDParameterName,
                                                                               OpenMetadataType.REFERENCEABLE.typeName,
                                                                               null,
                                                                               null,
                                                                               true,
                                                                               true,
                                                                               serviceSupportedZones,
                                                                               effectiveTime,
                                                                               methodName));

                    }
                    else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE_TYPE_NAME))
                    {
                        relationships.add(relationship);

                        String requestSourceGUID = relationship.getEntityOneProxy().getGUID();

                        supplementaryEntities.add(this.getEntityFromRepository(userId,
                                                                               requestSourceGUID,
                                                                               requestSourceGUIDParameterName,
                                                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                               null,
                                                                               null,
                                                                               true,
                                                                               true,
                                                                               serviceSupportedZones,
                                                                               effectiveTime,
                                                                               methodName));
                    }
                    else
                    {
                        relationships.add(relationship);
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
    public EngineActionStatus getActionStatus(String               propertyName,
                                              InstanceProperties   properties)
    {
        EngineActionStatus engineActionStatus = EngineActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> engineActionStatus = EngineActionStatus.REQUESTED;
                        case 1 -> engineActionStatus = EngineActionStatus.APPROVED;
                        case 2 -> engineActionStatus = EngineActionStatus.WAITING;
                        case 3 -> engineActionStatus = EngineActionStatus.ACTIVATING;
                        case 4 -> engineActionStatus = EngineActionStatus.IN_PROGRESS;
                        case 10 -> engineActionStatus = EngineActionStatus.ACTIONED;
                        case 11 -> engineActionStatus = EngineActionStatus.INVALID;
                        case 12 -> engineActionStatus = EngineActionStatus.IGNORED;
                        case 13 -> engineActionStatus = EngineActionStatus.FAILED;
                    }
                }
            }
        }

        return engineActionStatus;
    }


    /**
     * Request that execution of an engine action is allocated to the caller.
     * This is only permitted if no other caller has claimed it.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void claimEngineAction(String       userId,
                                  String       engineActionGUID,
                                  List<String> serviceSupportedZones,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String guidParameterName = "engineActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           engineActionGUID,
                                                           guidParameterName,
                                                           OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                EngineActionStatus status = this.getActionStatus(OpenMetadataType.ACTION_STATUS_PROPERTY_NAME,
                                                                 properties);

                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if ((status == EngineActionStatus.APPROVED) && (processingEngineUserId == null))
                {
                    EngineActionBuilder builder = new EngineActionBuilder(OpenMetadataType.WAITING_EA_STATUS_ORDINAL,
                                                                          userId,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

                    updateBeanInRepository(userId,
                                           null,
                                           null,
                                           engineActionGUID,
                                           guidParameterName,
                                           OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                           OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                           false,
                                           false,
                                           serviceSupportedZones,
                                           builder.getClaimInstanceProperties(methodName),
                                           true,
                                           effectiveTime,
                                           methodName);

                    auditLog.logMessage(methodName, GenericHandlersAuditCode.SUCCESSFUL_ACTION_CLAIM_REQUEST.getMessageDefinition(userId, engineActionGUID));
                }
                else
                {
                    throw new PropertyServerException(GenericHandlersErrorCode.INVALID_ENGINE_ACTION_STATUS.getMessageDefinition(userId,
                                                                                                                                 engineActionGUID,
                                                                                                                                 processingEngineUserId,
                                                                                                                                 status.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION_PROPERTIES.getMessageDefinition(engineActionGUID,
                                                                                                                                 guidParameterName,
                                                                                                                                 serviceName,
                                                                                                                                 methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION.getMessageDefinition(engineActionGUID,
                                                                                                                  guidParameterName,
                                                                                                                  serviceName,
                                                                                                                  methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Create a string message for log messages.
     *
     * @param ordinal ordinal for the status
     * @return string name
     */
    private String getEngineActionStatusName(int ordinal)
    {
        return switch (ordinal)
               {
                   case OpenMetadataType.REQUESTED_EA_STATUS_ORDINAL -> "REQUESTED";
                   case OpenMetadataType.APPROVED_EA_STATUS_ORDINAL -> "APPROVED";
                   case OpenMetadataType.WAITING_EA_STATUS_ORDINAL -> "WAITING";
                   case OpenMetadataType.IN_PROGRESS_EA_STATUS_ORDINAL -> "IN_PROGRESS";
                   case OpenMetadataType.ACTIONED_EA_STATUS_ORDINAL -> "ACTIONED";
                   case OpenMetadataType.INVALID_EA_STATUS_ORDINAL -> "INVALID";
                   case OpenMetadataType.IGNORED_EA_STATUS_ORDINAL -> "IGNORED";
                   case OpenMetadataType.FAILED_EA_STATUS_ORDINAL -> "FAILED";
                   case OpenMetadataType.OTHER_EA_STATUS_ORDINAL -> "OTHER";
                   default -> "UNKNOWN";
               };
    }


    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param engineActionStatus new status ordinal
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void updateEngineActionStatus(String       userId,
                                         String       engineActionGUID,
                                         int          engineActionStatus,
                                         List<String> serviceSupportedZones,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String guidParameterName = "engineActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           engineActionGUID,
                                                           guidParameterName,
                                                           OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                int currentStatus = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                            OpenMetadataType.ACTION_STATUS_PROPERTY_NAME,
                                                                            properties,
                                                                            methodName);

                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (((processingEngineUserId == null) && (engineActionStatus == OpenMetadataType.APPROVED_EA_STATUS_ORDINAL)) ||
                            (userId.equals(processingEngineUserId)))
                {
                    try
                    {
                        properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                null,
                                                                                OpenMetadataType.ACTION_STATUS_PROPERTY_NAME,
                                                                                OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                engineActionStatus,
                                                                                methodName);
                    }
                    catch (TypeErrorException error)
                    {
                        throw new InvalidParameterException(error, OpenMetadataType.ACTIVITY_TYPE_PROPERTY_NAME);
                    }

                    updateBeanInRepository(userId,
                                           null,
                                           null,
                                           engineActionGUID,
                                           guidParameterName,
                                           OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                           OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                           false,
                                           false,
                                           serviceSupportedZones,
                                           properties,
                                           true,
                                           effectiveTime,
                                           methodName);

                    auditLog.logMessage(methodName, GenericHandlersAuditCode.ENGINE_ACTION_STATUS_CHANGE.getMessageDefinition(this.getEngineActionStatusName(currentStatus),
                                                                                                                              this.getEngineActionStatusName(engineActionStatus),
                                                                                                                              engineActionGUID,
                                                                                                                              userId));

                }
                else
                {
                    throw new UserNotAuthorizedException(GenericHandlersErrorCode.INVALID_PROCESSING_USER.getMessageDefinition(userId,
                                                                                                                               methodName,
                                                                                                                               engineActionGUID,
                                                                                                                               processingEngineUserId),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         userId);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION_PROPERTIES.getMessageDefinition(engineActionGUID,
                                                                                                                                 guidParameterName,
                                                                                                                                 serviceName,
                                                                                                                                 methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION.getMessageDefinition(engineActionGUID,
                                                                                                                  guidParameterName,
                                                                                                                  serviceName,
                                                                                                                  methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Declare that all the processing for the governance service is finished along with status of the work.
     * If this is part of a governance action process, and it defines that there is a follow-on engine action then this is
     * set up at this time.
     *
     * @param userId caller's userId
     * @param engineActionGUID unique identifier of the engine action to update
     * @param status completion status enum value
     * @param callerRequestParameters request parameters used by the caller
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets additional elements to add to the action targets for the next phase
     * @param completionMessage message to describe completion results or reasons for failure
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance service is not authorized to update the engine action status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(String                userId,
                                       String                engineActionGUID,
                                       int                   status,
                                       Map<String, String>   callerRequestParameters,
                                       List<String>          outputGuards,
                                       List<NewActionTarget> newActionTargets,
                                       String                completionMessage,
                                       List<String>          serviceSupportedZones,
                                       Date                  effectiveTime,
                                       String                methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "engineActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        EntityDetail engineActionEntity = this.getEntityFromRepository(userId,
                                                                       engineActionGUID,
                                                                       guidParameterName,
                                                                       OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       false,
                                                                       false,
                                                                       serviceSupportedZones,
                                                                       effectiveTime,
                                                                       methodName);

        if (engineActionEntity != null)
        {
            InstanceProperties properties = engineActionEntity.getProperties();

            if (properties != null)
            {
                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                String governanceActionProcessStepGUID = repositoryHelper.getStringProperty(serviceName,
                                                                                            OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                                                            properties,
                                                                                            methodName);

                if (userId.equals(processingEngineUserId))
                {
                    EngineActionBuilder builder = new EngineActionBuilder(repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

                    repositoryHandler.updateEntityProperties(userId,
                                                             null,
                                                             null,
                                                             engineActionGUID,
                                                             engineActionEntity,
                                                             OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                             OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                             builder.getCompletionInstanceProperties(engineActionEntity.getProperties(),
                                                                                                     status,
                                                                                                     new Date(),
                                                                                                     outputGuards,
                                                                                                     completionMessage,
                                                                                                     methodName),
                                                             methodName);

                    this.markActionTargetsAsComplete(userId,
                                                     engineActionGUID,
                                                     serviceSupportedZones,
                                                     effectiveTime,
                                                     status);

                    /*
                     * The anchor GUID is set if this is part of a governance action process.  It points to the first engine action entity
                     * created when the governance action process was initiated.
                     */
                    AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(engineActionEntity, methodName);

                    String anchorGUID = null;

                    if (anchorIdentifiers != null)
                    {
                        anchorGUID = anchorIdentifiers.anchorGUID;
                    }

                    String processName = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataProperty.PROCESS_NAME.name,
                                                                            properties,
                                                                            methodName);
                    this.initiateNextEngineActions(userId,
                                                   engineActionGUID,
                                                   governanceActionProcessStepGUID,
                                                   anchorGUID,
                                                   processName,
                                                   outputGuards,
                                                   newActionTargets,
                                                   callerRequestParameters,
                                                   serviceSupportedZones,
                                                   effectiveTime,
                                                   methodName);
                }
                else
                {
                    throw new UserNotAuthorizedException(GenericHandlersErrorCode.INVALID_PROCESSING_USER.getMessageDefinition(userId,
                                                                                                                               methodName,
                                                                                                                               engineActionGUID,
                                                                                                                               processingEngineUserId),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         userId);
                }

            }
            else
            {
                throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION_PROPERTIES.getMessageDefinition(engineActionGUID,
                                                                                                                                 guidParameterName,
                                                                                                                                 serviceName,
                                                                                                                                 methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MISSING_ENGINE_ACTION.getMessageDefinition(engineActionGUID,
                                                                                                                  guidParameterName,
                                                                                                                  serviceName,
                                                                                                                  methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Mark all the action targets as complete if they are not set up with a status already.
     *
     * @param userId calling user
     * @param engineActionGUID completed governance action
     * @param status completion status
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException problem with guid
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private void markActionTargetsAsComplete(String       userId,
                                             String       engineActionGUID,
                                             List<String> serviceSupportedZones,
                                             Date         effectiveTime,
                                             int          status) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "markActionTargetsAsComplete";
        final String guidParameterName = "engineActionGUID";

        this.validateAnchorEntity(userId,
                                  engineActionGUID,
                                  guidParameterName,
                                  OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                  true,
                                  false,
                                  true,
                                  false,
                                  serviceSupportedZones,
                                  effectiveTime,
                                  methodName);

        List<Relationship> actionTargetRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                engineActionGUID,
                                                                                                OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                                                OpenMetadataType.TARGET_FOR_ACTION_TYPE_GUID,
                                                                                                OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME,
                                                                                                2,
                                                                                                false,
                                                                                                false,
                                                                                                0, 0,
                                                                                                effectiveTime,
                                                                                                methodName);

        if (actionTargetRelationships != null)
        {
            for (Relationship actionTarget : actionTargetRelationships)
            {
                if (actionTarget != null)
                {
                    InstanceProperties actionTargetProperties = actionTarget.getProperties();

                    if (actionTargetProperties.getPropertyValue(OpenMetadataType.STATUS_PROPERTY_NAME) == null)
                    {
                        InstanceProperties newActionTargetProperties;

                        try
                        {
                            newActionTargetProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                                   actionTarget.getProperties(),
                                                                                                   OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                                                   OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                                   OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                                   status,
                                                                                                   methodName);
                        }
                        catch (TypeErrorException error)
                        {
                            throw new PropertyServerException(error);
                        }

                        newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                               newActionTargetProperties,
                                                                                               OpenMetadataType.COMPLETION_DATE_PROPERTY_NAME,
                                                                                               new Date(),
                                                                                               methodName);

                        repositoryHandler.updateRelationshipProperties(userId,
                                                                       null,
                                                                       null,
                                                                       actionTarget,
                                                                       newActionTargetProperties,
                                                                       methodName);
                    }
                }
            }
        }
    }


    /**
     * An engine action has completed - are there any follow-on actions to run?  This would occur if the engine action is
     * part of a GovernanceActionProcess.  In this case it would be linked to a GovernanceActionProcessStep which is part of an execution template
     * for the GovernanceActionProcess.  The GovernanceActionProcessStep is, in turn, potentially linked to one or more GovernanceActionProcessSteps
     * that identify follow-on engine actions.
     *
     * @param userId calling user
     * @param previousEngineActionGUID unique identifier of engine action that has just completed
     * @param previousGovernanceActionProcessStepGUID governance action process driving previous engine action(s)
     * @param anchorGUID unique identifier of the first engine action to execute for the process
     * @param processName name of initiating process (if any)
     * @param outputGuards guards set up by the previous action(s)
     * @param newActionTargets unique identifiers of the elements for future governance actions to work on
     * @param callerRequestParameters set of request parameters gathered so far in the process
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private void initiateNextEngineActions(String                userId,
                                           String                previousEngineActionGUID,
                                           String                previousGovernanceActionProcessStepGUID,
                                           String                anchorGUID,
                                           String                processName,
                                           List<String>          outputGuards,
                                           List<NewActionTarget> newActionTargets,
                                           Map<String, String>   callerRequestParameters,
                                           List<String>          serviceSupportedZones,
                                           Date                  effectiveTime,
                                           String                methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String nextGovernanceActionProcessStepParameterName = "nextActionProcessStep.getEntityTwoProxy().getGUID()";

        if (previousGovernanceActionProcessStepGUID != null)
        {
            /*
             * The governance action process step links to the list of next action types.
             */
            List<Relationship> nextActionProcessSteps = repositoryHandler.getRelationshipsByType(userId,
                                                                                                 previousGovernanceActionProcessStepGUID,
                                                                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                 OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID,
                                                                                                 OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                 2,
                                                                                                 false,
                                                                                                 false,
                                                                                                 0,
                                                                                                 0,
                                                                                                 effectiveTime,
                                                                                                 methodName);
            if (nextActionProcessSteps != null)
            {
                /*
                 * There are potential follow-on actions.  Need to loop though each one to evaluate if the output guards
                 * permit it to execute.
                 */
                for (Relationship nextActionProcessStep : nextActionProcessSteps)
                {
                    if (nextActionProcessStep != null)
                    {
                        String guard = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                          nextActionProcessStep.getProperties(),
                                                                          methodName);

                        boolean mandatoryGuard = repositoryHelper.getBooleanProperty(serviceName,
                                                                                     OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                     nextActionProcessStep.getProperties(),
                                                                                     methodName);

                        boolean validNextAction = (guard == null);

                        if ((guard != null) && (outputGuards != null))
                        {
                            for (String outputGuard : outputGuards)
                            {
                                if (outputGuard != null)
                                {
                                    if (outputGuard.equals(guard))
                                    {
                                        validNextAction = true;
                                    }
                                }
                            }
                        }

                        if (validNextAction)
                        {
                            /*
                             * The guard matches so the next action is valid.  A new instance of the action is typically initiated.
                             * However, if there is an instance of this engine action waiting for mandatory guards to begin,
                             * then this result is attached to the waiting engine action.
                             */
                            this.prepareEngineActionFromProcessStep(userId,
                                                                    anchorGUID,
                                                                    nextActionProcessStep.getEntityTwoProxy().getGUID(),
                                                                    nextGovernanceActionProcessStepParameterName,
                                                                    guard,
                                                                    mandatoryGuard,
                                                                    null,
                                                                    previousEngineActionGUID,
                                                                    callerRequestParameters,
                                                                    null,
                                                                    newActionTargets,
                                                                    processName,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    serviceSupportedZones,
                                                                    methodName);
                            }
                        }
                    }
                }
            }
    }


    /**
     * Return the list of guards that must be satisfied for the engine action to run.  This is specified in the NextGovernanceActionProcessStep
     * relationships connected to the governance action process step that initiates the engine action.
     *
     * @param userId calling user
     * @param governanceActionProcessStepGUID unique identifier of the governance action process step initiating the governance action
     *
     * @return list of mandatory guards (or null)
     *
     * @throws InvalidParameterException problem with GUID
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private List<String> getMandatoryGuards(String userId,
                                            String governanceActionProcessStepGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getMandatoryGuards";

        List<Relationship> dependedOnActionProcessSteps = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   governanceActionProcessStepGUID,
                                                                                                   OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                   2,
                                                                                                   false,
                                                                                                   false,
                                                                                                   0, 0,
                                                                                                   null,
                                                                                                   methodName);
        if (dependedOnActionProcessSteps != null)
        {
            List<String> mandatoryGuards = new ArrayList<>();

            for (Relationship dependedOnActionType : dependedOnActionProcessSteps)
            {
                /*
                 * The interesting relationships are those that have the Governance ActionType at end2.
                 */
                if ((dependedOnActionType != null) && (dependedOnActionType.getEntityTwoProxy() != null) &&
                    (governanceActionProcessStepGUID.equals(dependedOnActionType.getEntityTwoProxy().getGUID())))
                {
                    boolean mandatoryGuard = repositoryHelper.getBooleanProperty(serviceName,
                                                                                 OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                 dependedOnActionType.getProperties(),
                                                                                 methodName);
                    if (mandatoryGuard)
                    {
                        String guard = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                          dependedOnActionType.getProperties(),
                                                                          methodName);
                        if (guard != null)
                        {
                            mandatoryGuards.add(guard);
                        }
                    }
                }
            }

            if (mandatoryGuards.isEmpty())
            {
                return null;
            }

            return mandatoryGuards;
        }

        return null;
    }


    /**
     * Retrieve the engine actions that are known to this server.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B> getEngineActions(String       userId,
                                    int          startFrom,
                                    int          pageSize,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                             invalidParameterHandler,
                                                                             userId,
                                                                             OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                                             OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             false,
                                                                             false,
                                                                             0,
                                                                             invalidParameterHandler.getMaxPagingSize(),
                                                                             effectiveTime,
                                                                             methodName);

        List<B> results = new ArrayList<>();
        int entityCount = 0;

        while ((iterator.moreToReceive()) && ((pageSize == 0) || (results.size() < pageSize)))
        {
            entityCount ++;
            EntityDetail nextEngineAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                B bean = this.getEngineAction(userId, nextEngineAction, serviceSupportedZones, effectiveTime, methodName);

                if (bean != null)
                {
                    results.add(bean);
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Retrieve the engine actions that are still in progress.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B> getActiveEngineActions(String       userId,
                                          int          startFrom,
                                          int          pageSize,
                                          List<String> serviceSupportedZones,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                             invalidParameterHandler,
                                                                             userId,
                                                                             OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                                             OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             false,
                                                                             false,
                                                                             0,
                                                                             invalidParameterHandler.getMaxPagingSize(),
                                                                             effectiveTime,
                                                                             methodName);

        List<B> results = new ArrayList<>();
        int entityCount = 0;

        while ((iterator.moreToReceive()) && ((pageSize == 0) || (results.size() < pageSize)))
        {
            entityCount ++;
            EntityDetail nextEngineAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                int status = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                     nextEngineAction.getProperties(),
                                                                     methodName);

                if ((status == OpenMetadataType.REQUESTED_EA_STATUS_ORDINAL) || (status == OpenMetadataType.APPROVED_EA_STATUS_ORDINAL) ||
                    (status == OpenMetadataType.WAITING_EA_STATUS_ORDINAL) || (status == OpenMetadataType.IN_PROGRESS_EA_STATUS_ORDINAL))
                {
                    B bean = this.getEngineAction(userId, nextEngineAction, serviceSupportedZones, effectiveTime, methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Retrieve the engine actions that are still in progress and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B> getActiveClaimedEngineActions(String       userId,
                                                 String       governanceEngineGUID,
                                                 int          startFrom,
                                                 int          pageSize,
                                                 List<String> serviceSupportedZones,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String guidParameterName = "governanceEngineGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                     userId,
                                                                                     methodName);

        RepositorySelectedEntitiesIterator iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                                             invalidParameterHandler,
                                                                                             userId,
                                                                                             OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                                                             properties,
                                                                                             MatchCriteria.ANY,
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                             false,
                                                                                             false,
                                                                                             0,
                                                                                             invalidParameterHandler.getMaxPagingSize(),
                                                                                             effectiveTime,
                                                                                             methodName);

        List<B> results = new ArrayList<>();
        int entityCount = 0;

        while ((iterator.moreToReceive()) && ((pageSize == 0) || (results.size() < pageSize)))
        {
            entityCount ++;
            EntityDetail nextEngineAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                int status = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                     nextEngineAction.getProperties(),
                                                                     methodName);

                if ((status == OpenMetadataType.WAITING_EA_STATUS_ORDINAL) || (status == OpenMetadataType.IN_PROGRESS_EA_STATUS_ORDINAL))
                {
                    B bean = this.getEngineAction(userId, nextEngineAction, serviceSupportedZones, effectiveTime, methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName parameter supplying search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findEngineActions(String       userId,
                                     String       searchString,
                                     String       searchStringParameterName,
                                     int          startFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     List<String> serviceSupportedZones,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                        OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                        null,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entityDetail : entities)
            {
                B bean = this.getEngineAction(userId, entityDetail, serviceSupportedZones, effectiveTime, methodName);

                if (bean != null)
                {
                    results.add(bean);
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName name of parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getEngineActionsByName(String       userId,
                                          String       name,
                                          String       nameParameterName,
                                          int          startFrom,
                                          int          pageSize,
                                          List<String> serviceSupportedZones,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.PROCESS_NAME.name);

        List<EntityDetail> entities =  this.getEntitiesByValue(userId,
                                                               name,
                                                               nameParameterName,
                                                               OpenMetadataType.ENGINE_ACTION_TYPE_GUID,
                                                               OpenMetadataType.ENGINE_ACTION_TYPE_NAME,
                                                               specificMatchPropertyNames,
                                                               true,
                                                               false,
                                                               null,
                                                               null,
                                                               false,
                                                               false,
                                                               serviceSupportedZones,
                                                               null,
                                                               startFrom,
                                                               pageSize,
                                                               effectiveTime,
                                                               methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entityDetail : entities)
            {
                B bean = this.getEngineAction(userId, entityDetail, serviceSupportedZones, effectiveTime, methodName);

                if (bean != null)
                {
                    results.add(bean);
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance service.  However, if the governance service has to process many
     * target elements, then setting the status on each individual target will show the progress of the
     * governance service.
     *
     * @param userId caller's userId
     * @param actionTargetGUID unique identifier of the target element
     * @param status status enum to show its progress
     * @param startDate date/time that the governance service started processing the target
     * @param completionDate date/time that the governance process completed processing this target
     * @param completionMessage message to describe completion results or reasons for failure
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void updateActionTargetStatus(String       userId,
                                         String       actionTargetGUID,
                                         int          status,
                                         Date         startDate,
                                         Date         completionDate,
                                         String       completionMessage,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String guidParameterName = "actionTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTargetGUID, guidParameterName, methodName);

        Relationship actionTarget = repositoryHandler.getRelationshipByGUID(userId,
                                                                            actionTargetGUID,
                                                                            guidParameterName,
                                                                            OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME,
                                                                            effectiveTime,
                                                                            methodName);

        if (actionTarget != null)
        {
            InstanceProperties newActionTargetProperties;

            try
            {
                newActionTargetProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                       actionTarget.getProperties(),
                                                                                       OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                                       OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                       OpenMetadataType.ENGINE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                       status,
                                                                                       methodName);
            }
            catch (TypeErrorException error)
            {
                throw new PropertyServerException(error);
            }

            newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                   newActionTargetProperties,
                                                                                   OpenMetadataType.START_DATE_PROPERTY_NAME,
                                                                                   startDate,
                                                                                   methodName);

            newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                   newActionTargetProperties,
                                                                                   OpenMetadataType.COMPLETION_DATE_PROPERTY_NAME,
                                                                                   completionDate,
                                                                                   methodName);

            newActionTargetProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     newActionTargetProperties,
                                                                                     OpenMetadataType.COMPLETION_MESSAGE_PROPERTY_NAME,
                                                                                     completionMessage,
                                                                                     methodName);

            repositoryHandler.updateRelationshipProperties(userId,
                                                           null,
                                                           null,
                                                           actionTarget,
                                                           newActionTargetProperties,
                                                           methodName);
        }
    }
}
