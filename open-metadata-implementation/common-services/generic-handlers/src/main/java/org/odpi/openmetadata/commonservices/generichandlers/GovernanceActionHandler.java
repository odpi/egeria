/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

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
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;

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
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name to give this governance action process
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets list of action target names to GUIDs for the resulting governance action service
     * @param requestParameters initial set of request parameters from the caller
     * @param startTime future start time or null for "as soon as possible"
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     * @param methodName calling method
     *
     * @return unique identifier of the first governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
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
                                                  String                methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String qualifiedNameParameterName  = "processQualifiedName";
        final String governanceActionTypeGUIDParameterName  = "governanceActionFlowRelationship.getEntityTwoProxy().getGUID()";

        /*
         * Effective time is set to "now" so that the process definition that is active now is
         * used.
         */
        Date effectiveTime = new Date();

        String governanceActionProcessGUID = this.getBeanGUIDByUniqueName(userId,
                                                                          processQualifiedName,
                                                                          qualifiedNameParameterName,
                                                                          OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                          false,
                                                                          false,
                                                                          supportedZones,
                                                                          effectiveTime,
                                                                          methodName);

        Relationship governanceActionFlowRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                      governanceActionProcessGUID,
                                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                                      true,
                                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_GUID,
                                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                                                                                      false,
                                                                                                      false,
                                                                                                      effectiveTime,
                                                                                                      methodName);

        if (governanceActionFlowRelationship != null)
        {
            String governanceActionTypeGUID = governanceActionFlowRelationship.getEntityTwoProxy().getGUID();

            String guard = repositoryHelper.getStringProperty(serviceName,
                                                              OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                              governanceActionFlowRelationship.getProperties(),
                                                              methodName);

            return initiateGovernanceActionFromType(userId,
                                                    null,
                                                    governanceActionTypeGUID,
                                                    governanceActionTypeGUIDParameterName,
                                                    guard,
                                                    requestParameters,
                                                    requestSourceGUIDs,
                                                    actionTargets,
                                                    startTime,
                                                    processQualifiedName,
                                                    originatorServiceName,
                                                    originatorEngineName,
                                                    effectiveTime,
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


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param userId caller's userId
     * @param anchorGUID the unique identifier of the first governance action in the governance action process (if any)
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @param governanceActionTypeGUIDParameterName parameter supplying governanceActionTypeGUID
     * @param guard guard that triggered this action
     * @param initialRequestParameters initial set of request parameters
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param requestSourceName name of calling source
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the first governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private String initiateGovernanceActionFromType(String                userId,
                                                    String                anchorGUID,
                                                    String                governanceActionTypeGUID,
                                                    String                governanceActionTypeGUIDParameterName,
                                                    String                guard,
                                                    Map<String, String>   initialRequestParameters,
                                                    List<String>          requestSourceGUIDs,
                                                    List<NewActionTarget> actionTargets,
                                                    Date                  startTime,
                                                    String                requestSourceName,
                                                    String                originatorServiceName,
                                                    String                originatorEngineName,
                                                    Date                  effectiveTime,
                                                    String                methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        Relationship governanceActionTypeExecutorRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                              governanceActionTypeGUID,
                                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                                              true,
                                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_GUID,
                                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_NAME,
                                                                                                              false,
                                                                                                              false,
                                                                                                              effectiveTime,
                                                                                                              methodName);

        if (governanceActionTypeExecutorRelationship == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_EXECUTOR.getMessageDefinition(governanceActionTypeGUID,
                                                                                                               OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_NAME),
                                                this.getClass().getName(),
                                                methodName,
                                                governanceActionTypeGUIDParameterName);
        }

        EntityDetail governanceActionTypeEntity = this.getEntityFromRepository(userId,
                                                                               governanceActionTypeGUID,
                                                                               governanceActionTypeGUIDParameterName,
                                                                               OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               supportedZones,
                                                                               effectiveTime,
                                                                               methodName);

        String governanceActionTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                             governanceActionTypeEntity.getProperties(),
                                                                             methodName);
        int domainIdentifier = repositoryHelper.getIntProperty(serviceName,
                                                               OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               governanceActionTypeEntity.getProperties(),
                                                               methodName);
        String displayName = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                governanceActionTypeEntity.getProperties(),
                                                                methodName);
        String description = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                governanceActionTypeEntity.getProperties(),
                                                                methodName);
        List<String> receivedGuards = null;

        if (guard != null)
        {
            receivedGuards = new ArrayList<>();

            receivedGuards.add(guard);
        }

        String governanceEngineName = repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                         governanceActionTypeExecutorRelationship.getEntityTwoProxy().getUniqueProperties(),
                                                                         methodName);
        String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                governanceActionTypeExecutorRelationship.getProperties(),
                                                                methodName);

        Map<String, String> requestParameters = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                          OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                          governanceActionTypeExecutorRelationship.getProperties(),
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

        List<String> mandatoryGuards = this.getMandatoryGuards(userId, governanceActionTypeGUID, effectiveTime);

        return initiateGovernanceAction(userId,
                                        governanceActionTypeName + ":" + UUID.randomUUID(),
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
                                        governanceActionTypeGUID,
                                        governanceActionTypeName,
                                        anchorGUID,
                                        requestSourceName,
                                        originatorServiceName,
                                        originatorEngineName,
                                        methodName);
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
     * @param actionTargets list of action target names to GUIDs for the resulting governance action service
     * @param mandatoryGuards list of guards that must be received in order to proceed with the governance action
     * @param receivedGuards list of guards to initiate the governance action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     * @param anchorGUID identifier of the first governance action of the process (null for standalone governance actions and the first governance
     *                   action in a governance action process).
     * @param governanceActionTypeGUID unique identifier of the governance action type that initiated this governance action as part of
     *                                 a governance action process (or null if this is standalone governance action)
     * @param governanceActionTypeName unique name of the governance action type that initiated this governance action as part of
     *                                 a governance action process (or null if this is standalone governance action)
     * @param requestSourceName where did the request come from
     * @param originatorServiceName unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine)
     * @param methodName calling method
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceAction(String                userId,
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
                                           String                requestSourceName,
                                           String                originatorServiceName,
                                           String                originatorEngineName,
                                           String                methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String engineNameParameterName     = "governanceEngineName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);

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

        if (governanceActionTypeName != null)
        {
            auditLog.logMessage(methodName, GenericHandlersAuditCode.INITIATE_GOVERNANCE_ACTION_FROM_TYPE.getMessageDefinition(qualifiedName,
                                                                                                                               governanceActionTypeName,
                                                                                                                               requestType,
                                                                                                                               governanceEngineName,
                                                                                                                               receivedGuardsString,
                                                                                                                               mandatoryGuardsString,
                                                                                                                               requestParameterNames,
                                                                                                                               startTimeString));
        }
        else
        {
            auditLog.logMessage(methodName, GenericHandlersAuditCode.INITIATE_GOVERNANCE_ACTION.getMessageDefinition(qualifiedName,
                                                                                                                     requestType,
                                                                                                                     governanceEngineName,
                                                                                                                     receivedGuardsString,
                                                                                                                     mandatoryGuardsString,
                                                                                                                     requestParameterNames,
                                                                                                                     startTimeString));
        }

        /*
         * Effective time is set to "any time" and all elements that make up the governance action
         * are set up without effectivity dates.  Any control on start time is done using the startTime property.
         */
        Date effectiveTime = null;

        String governanceEngineGUID = this.validateGovernanceEngineName(userId, governanceEngineName, engineNameParameterName, methodName);

        GovernanceActionBuilder builder = new GovernanceActionBuilder(qualifiedName,
                                                                      domainIdentifier,
                                                                      displayName,
                                                                      description,
                                                                      governanceEngineGUID,
                                                                      governanceEngineName,
                                                                      requestSourceName,
                                                                      governanceActionTypeGUID,
                                                                      governanceActionTypeName,
                                                                      requestType,
                                                                      requestParameters,
                                                                      mandatoryGuards,
                                                                      receivedGuards,
                                                                      OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL,
                                                                      startTime,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        if (anchorGUID != null)
        {
            builder.setAnchors(userId, anchorGUID, methodName);
        }

        String governanceActionGUID = this.createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                  builder,
                                                                  null,
                                                                  methodName);

        if (governanceActionGUID != null)
        {
            final String governanceActionGUIDParameterName = "governanceActionGUID";
            final String governanceActionTypeGUIDParameterName = "governanceActionTypeGUID";

            /*
             * Show the relationship to a governance action type if this governance action originated from a governance action process.
             */
            InstanceProperties originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                   null,
                                                                                                   OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
                                                                                                   originatorServiceName,
                                                                                                   methodName);

            originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                originatorProperties,
                                                                                OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
                                                                                originatorEngineName,
                                                                                methodName);


            if (governanceActionTypeGUID != null)
            {
                this.linkElementToElement(userId,
                                          null,
                                          null,
                                          governanceActionTypeGUID,
                                          governanceActionTypeGUIDParameterName,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                          governanceActionGUID,
                                          governanceActionGUIDParameterName,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                          false,
                                          false,
                                          supportedZones,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_GUID,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_NAME,
                                          originatorProperties,
                                          null,
                                          null,
                                          null,
                                          methodName);
            }

            /*
             * Identify the source of the work
             */
            if ((requestSourceGUIDs != null) && (! requestSourceGUIDs.isEmpty()))
            {
                final String requestSourceGUIDParameterName = "requestSourceGUIDs[x]";

                originatorProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                    originatorProperties,
                                                                                    OpenMetadataAPIMapper.REQUEST_SOURCE_NAME_PROPERTY_NAME,
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
                                                  OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                  governanceActionGUID,
                                                  governanceActionGUIDParameterName,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                  false,
                                                  false,
                                                  supportedZones,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_GUID,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_NAME,
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
                                  governanceActionGUID,
                                  governanceActionGUIDParameterName,
                                  qualifiedName,
                                  actionTargets,
                                  methodName);

            if ((mandatoryGuards == null) || ((receivedGuards != null) && (receivedGuards.containsAll(mandatoryGuards))))
            {
                this.updateGovernanceActionStatus(userId,
                                                  governanceActionGUID,
                                                  OpenMetadataAPIMapper.APPROVED_GA_STATUS_ORDINAL,
                                                  effectiveTime,
                                                  methodName);
            }
        }

        return governanceActionGUID;
    }


    /**
     * Link up the supplied action targets.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action to connector the targets to
     * @param governanceActionGUIDParameterName parameter passing the governanceActionGUID
     * @param governanceActionName name of governance action - for logging
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param methodName calling method
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void addActionTargets(String                userId,
                                  String                governanceActionGUID,
                                  String                governanceActionGUIDParameterName,
                                  String                governanceActionName,
                                  List<NewActionTarget> actionTargets,
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
                                                                                                     OpenMetadataAPIMapper.ACTION_TARGET_NAME_PROPERTY_NAME,
                                                                                                     actionTargetName,
                                                                                                     methodName);

                        this.linkElementToElement(userId,
                                                  null,
                                                  null,
                                                  governanceActionGUID,
                                                  governanceActionGUIDParameterName,
                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                  actionTargetGUID,
                                                  actionTargetGUIDParameterName,
                                                  OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                  false,
                                                  false,
                                                  supportedZones,
                                                  OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_GUID,
                                                  OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME,
                                                  properties,
                                                  null,
                                                  null,
                                                  null,
                                                  methodName);

                        auditLog.logMessage(methodName, GenericHandlersAuditCode.ADD_ACTION_TARGETS.getMessageDefinition(actionTargetName,
                                                                                                                         actionTargetGUID,
                                                                                                                         governanceActionName,
                                                                                                                         governanceActionGUID));
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
                                                                   false,
                                                                   false,
                                                                   supportedZones,
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

        return governanceEngineGUID;
    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                 Date   effectiveTime,
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
                                                                  null,
                                                                  null,
                                                                  true,
                                                                  true,
                                                                  supportedZones,
                                                                  effectiveTime,
                                                                  methodName);

        if (primaryEntity != null)
        {
            List<Relationship> relationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           governanceActionGUID,
                                                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
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

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME))
                    {
                        relationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_NAME))
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
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               supportedZones,
                                                                               effectiveTime,
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
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               supportedZones,
                                                                               effectiveTime,
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
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void claimGovernanceAction(String userId,
                                      String governanceActionGUID,
                                      Date   effectiveTime,
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
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           supportedZones,
                                                           effectiveTime,
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

                if ((status == GovernanceActionStatus.APPROVED) && (processingEngineUserId == null))
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
                                           false,
                                           false,
                                           supportedZones,
                                           builder.getClaimInstanceProperties(methodName),
                                           true,
                                           effectiveTime,
                                           methodName);

                    auditLog.logMessage(methodName, GenericHandlersAuditCode.SUCCESSFUL_ACTION_CLAIM_REQUEST.getMessageDefinition(userId, governanceActionGUID));
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
     * Create a string message for log messages.
     *
     * @param ordinal ordinal for the status
     * @return string name
     */
    private String getGovernanceActionStatusName(int ordinal)
    {
        String governanceActionStatusName = "UNKNOWN";
        switch(ordinal)
        {
            case OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL:
                governanceActionStatusName = "REQUESTED";
                break;

            case OpenMetadataAPIMapper.APPROVED_GA_STATUS_ORDINAL:
                governanceActionStatusName = "APPROVED";
                break;

            case OpenMetadataAPIMapper.WAITING_GA_STATUS_ORDINAL:
                governanceActionStatusName = "WAITING";
                break;

            case OpenMetadataAPIMapper.IN_PROGRESS_GA_STATUS_ORDINAL:
                governanceActionStatusName = "IN_PROGRESS";
                break;

            case OpenMetadataAPIMapper.ACTIONED_GA_STATUS_ORDINAL:
                governanceActionStatusName = "ACTIONED";
                break;

            case OpenMetadataAPIMapper.INVALID_GA_STATUS_ORDINAL:
                governanceActionStatusName = "INVALID";
                break;

            case OpenMetadataAPIMapper.IGNORED_GA_STATUS_ORDINAL:
                governanceActionStatusName = "IGNORED";
                break;

            case OpenMetadataAPIMapper.FAILED_GA_STATUS_ORDINAL:
                governanceActionStatusName = "FAILED";
                break;

            case OpenMetadataAPIMapper.OTHER_GA_STATUS_ORDINAL:
                governanceActionStatusName = "OTHER";
                break;
        }

        return  governanceActionStatusName;
    }


    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param governanceActionStatus new status ordinal
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void updateGovernanceActionStatus(String userId,
                                             String governanceActionGUID,
                                             int    governanceActionStatus,
                                             Date   effectiveTime,
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
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            InstanceProperties properties = entity.getProperties();

            if (properties != null)
            {
                int currentStatus = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                            OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME,
                                                                            properties,
                                                                            methodName);

                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (((processingEngineUserId == null) && (governanceActionStatus == OpenMetadataAPIMapper.APPROVED_GA_STATUS_ORDINAL)) ||
                            (userId.equals(processingEngineUserId)))
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
                                           false,
                                           false,
                                           supportedZones,
                                           properties,
                                           true,
                                           effectiveTime,
                                           methodName);

                    auditLog.logMessage(methodName, GenericHandlersAuditCode.GOVERNANCE_ACTION_STATUS_CHANGE.getMessageDefinition(this.getGovernanceActionStatusName(currentStatus),
                                                                                                                                  this.getGovernanceActionStatusName(governanceActionStatus),
                                                                                                                                  governanceActionGUID,
                                                                                                                                  userId));

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
     * Declare that all the processing for the governance action service is finished along with status of the work.
     * If this is part of a governance action process, and it defines that there is a follow-on governance action then this is
     * set up at this time.
     *
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param status completion status enum value
     * @param callerRequestParameters request parameters used by the caller
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargets additional elements to add to the action targets for the next phase
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(String                userId,
                                       String                governanceActionGUID,
                                       int                   status,
                                       Map<String, String>   callerRequestParameters,
                                       List<String>          outputGuards,
                                       List<NewActionTarget> newActionTargets,
                                       Date                  effectiveTime,
                                       String                methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        EntityDetail governanceActionEntity = this.getEntityFromRepository(userId,
                                                                           governanceActionGUID,
                                                                           guidParameterName,
                                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           supportedZones,
                                                                           effectiveTime,
                                                                           methodName);

        if (governanceActionEntity != null)
        {
            InstanceProperties properties = governanceActionEntity.getProperties();

            if (properties != null)
            {
                String processingEngineUserId = repositoryHelper.getStringProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                   properties,
                                                                                   methodName);

                if (userId.equals(processingEngineUserId))
                {
                    GovernanceActionBuilder builder = new GovernanceActionBuilder(repositoryHelper,
                                                                                  serviceName,
                                                                                  serverName);

                    repositoryHandler.updateEntityProperties(userId,
                                                             null,
                                                             null,
                                                             governanceActionGUID,
                                                             governanceActionEntity,
                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                             builder.getCompletionInstanceProperties(governanceActionEntity.getProperties(),
                                                                                                     status,
                                                                                                     new Date(),
                                                                                                     outputGuards,
                                                                                                     methodName),
                                                             methodName);

                    List<NewActionTarget> currentActionTargets = this.markActionTargetsAsComplete(userId,
                                                                                                  governanceActionGUID,
                                                                                                  effectiveTime,
                                                                                                  status);

                    List<NewActionTarget> nextActionTargets;

                    if ((currentActionTargets != null) && (! currentActionTargets.isEmpty()))
                    {
                        if ((newActionTargets != null) && (! newActionTargets.isEmpty()))
                        {
                            nextActionTargets = new ArrayList<>(currentActionTargets);

                            for (NewActionTarget newActionTarget : newActionTargets)
                            {
                                boolean isNewActionTarget = true;

                                for (NewActionTarget currentActionTarget : currentActionTargets)
                                {
                                    if (currentActionTarget != null)
                                    {
                                        if ((currentActionTarget.getActionTargetGUID().equals(newActionTarget.getActionTargetGUID())) &&
                                            (currentActionTarget.getActionTargetName().equals(newActionTarget.getActionTargetName())))
                                        {
                                            isNewActionTarget = false;
                                        }
                                    }
                                }

                                if (isNewActionTarget)
                                {
                                    nextActionTargets.add(newActionTarget);
                                }
                            }
                        }
                        else
                        {
                            nextActionTargets = currentActionTargets;
                        }
                    }
                    else
                    {
                        nextActionTargets = newActionTargets;
                    }

                    /*
                     * The anchor GUID is set if this is part of a governance action process.  It points to the first governance action entity
                     * created when the governance action process was initiated.
                     */
                    String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(governanceActionEntity, methodName);

                    String processName = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataAPIMapper.PROCESS_NAME_PROPERTY_NAME,
                                                                            properties,
                                                                            methodName);
                    this.initiateNextGovernanceActions(userId,
                                                       governanceActionGUID,
                                                       anchorGUID,
                                                       processName,
                                                       outputGuards,
                                                       nextActionTargets,
                                                       callerRequestParameters,
                                                       effectiveTime,
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
     * Mark all the action targets as complete if they are not set up with a status already.
     *
     * @param userId calling user
     * @param governanceActionGUID completed governance action
     * @param status completion status
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of existing governance actions
     *
     * @throws InvalidParameterException problem with guid
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private List<NewActionTarget> markActionTargetsAsComplete(String userId,
                                                              String governanceActionGUID,
                                                              Date   effectiveTime,
                                                              int    status) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "markActionTargetsAsComplete";

        List<NewActionTarget> currentActionTargets = null;

        List<Relationship> actionTargetRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                governanceActionGUID,
                                                                                                OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                                                OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_GUID,
                                                                                                OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME,
                                                                                                2,
                                                                                                false,
                                                                                                false,
                                                                                                0, 0,
                                                                                                effectiveTime,
                                                                                                methodName);

        if (actionTargetRelationships != null)
        {
            currentActionTargets = new ArrayList<>();

            for (Relationship actionTarget : actionTargetRelationships)
            {
                if (actionTarget != null)
                {
                    NewActionTarget currentActionTarget = new NewActionTarget();

                    currentActionTarget.setActionTargetGUID(actionTarget.getEntityTwoProxy().getGUID());

                    InstanceProperties actionTargetProperties = actionTarget.getProperties();

                    currentActionTarget.setActionTargetName(repositoryHelper.getStringProperty(serviceName,
                                                                                               OpenMetadataAPIMapper.ACTION_TARGET_NAME_PROPERTY_NAME,
                                                                                               actionTargetProperties,
                                                                                               methodName));

                    currentActionTargets.add(currentActionTarget);

                    if (actionTargetProperties.getPropertyValue(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME) == null)
                    {
                        InstanceProperties newActionTargetProperties;

                        try
                        {
                            newActionTargetProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                                   actionTarget.getProperties(),
                                                                                                   OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                                                   OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                                   OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                                   status,
                                                                                                   methodName);
                        }
                        catch (TypeErrorException error)
                        {
                            throw new PropertyServerException(error);
                        }

                        newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                               newActionTargetProperties,
                                                                                               OpenMetadataAPIMapper.COMPLETION_DATE_PROPERTY_NAME,
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

        return currentActionTargets;
    }


    /**
     * A governance action has completed - are there any follow-on actions to run?  This would occur if the governance action is
     * part of a GovernanceActionProcess.  In this case it would be linked to a GovernanceActionType which is part of an execution template
     * for the GovernanceActionProcess.  The GovernanceActionType is, in turn, potentially linked to one or more GovernanceActionTypes
     * that identify follow governance actions.
     *
     * @param userId calling user
     * @param previousGovernanceActionGUID unique identifier of governance action that has just completed
     * @param anchorGUID unique identifier of the first governance action to execute for the process
     * @param processName name of initiating process (if any)
     * @param outputGuards guards set up by the previous action(s)
     * @param newActionTargets unique identifiers of the elements for future governance actions to work on
     * @param currentRequestParameters set of request parameters gathered so far in the process
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private void initiateNextGovernanceActions(String                userId,
                                               String                previousGovernanceActionGUID,
                                               String                anchorGUID,
                                               String                processName,
                                               List<String>          outputGuards,
                                               List<NewActionTarget> newActionTargets,
                                               Map<String, String>   currentRequestParameters,
                                               Date                  effectiveTime,
                                               String                methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        /*
         * Locate the governance action type that initiated this action (if any).
         * There is only follow-on activity if this relationship exists.
         */
        Relationship governanceActionTypeUseRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                         previousGovernanceActionGUID,
                                                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                                                         false,
                                                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_GUID,
                                                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_NAME,
                                                                                                         false,
                                                                                                         false,
                                                                                                         effectiveTime,
                                                                                                         methodName);

        if (governanceActionTypeUseRelationship != null)
        {
            /*
             * The GovernanceActionUseType has properties to propagate to the next governance action.
             */
            String governanceActionTypeGUID = governanceActionTypeUseRelationship.getEntityOneProxy().getGUID();
            String originatorServiceName = repositoryHelper.getStringProperty(serviceName,
                                                                              OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
                                                                              governanceActionTypeUseRelationship.getProperties(),
                                                                              methodName);
            String originatorEngineName = repositoryHelper.getStringProperty(serviceName,
                                                                             OpenMetadataAPIMapper.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
                                                                             governanceActionTypeUseRelationship.getProperties(),
                                                                             methodName);

            /*
             * Once we know the governance action type, it is possible to retrieve a list of next action types.
             */
            List<Relationship> nextActionTypes = repositoryHandler.getRelationshipsByType(userId,
                                                                                          governanceActionTypeGUID,
                                                                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                          OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                                                          OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                          2,
                                                                                          false,
                                                                                          false,
                                                                                          0, 0,
                                                                                          effectiveTime,
                                                                                          methodName);
            if (nextActionTypes != null)
            {
                /*
                 * There are potential follow-on actions.  Need to loop though each one to evaluate if the output guards
                 * permit it to execute.
                 */
                for (Relationship nextActionType : nextActionTypes)
                {
                    /*
                     * Make sure we are moving forward through the process not backwards.
                     */
                    if ((nextActionType != null) && (! governanceActionTypeGUID.equals(nextActionType.getEntityOneProxy().getGUID())))
                    {
                        /*
                         * The guard property in the relationship must match one of the output guards, or it must be null
                         * to allow the governance action to proceed.
                         */
                        String guard = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                          nextActionType.getProperties(),
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
                             * However, if there is an instance of this governance action waiting for mandatory guards to begin,
                             * or already run but ignoreMultipleTriggers is set then the previous governance action is linked to this instance.
                             */
                            boolean mandatoryGuard = repositoryHelper.getBooleanProperty(serviceName,
                                                                                         OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                         nextActionType.getProperties(),
                                                                                         methodName);
                            boolean ignoreMultipleTriggers = repositoryHelper.getBooleanProperty(serviceName,
                                                                                                 OpenMetadataAPIMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
                                                                                                 nextActionType.getProperties(),
                                                                                                 methodName);

                            String nextGovernanceActionGUID = newGovernanceActionNeeded(userId,
                                                                                        governanceActionTypeGUID,
                                                                                        anchorGUID,
                                                                                        guard,
                                                                                        ignoreMultipleTriggers,
                                                                                        effectiveTime,
                                                                                        newActionTargets);

                            if (nextGovernanceActionGUID == null)
                            {
                                final String governanceActionTypeGUIDParameterName = "governanceActionTypeUseRelationship.getEntityOneProxy().getGUID()";

                                /*
                                 * A new governance action can be created.  Information is needed from the governance action type.
                                 */
                                if (anchorGUID == null)
                                {
                                    /*
                                     * The anchor is set up to be the first governance action in the chain.  If it is null then the previous governance
                                     * action was the first in the chain.
                                     */
                                    nextGovernanceActionGUID = initiateGovernanceActionFromType(userId,
                                                                                                previousGovernanceActionGUID,
                                                                                                governanceActionTypeGUID,
                                                                                                governanceActionTypeGUIDParameterName,
                                                                                                guard,
                                                                                                currentRequestParameters,
                                                                                                null,
                                                                                                newActionTargets,
                                                                                                new Date(),
                                                                                                processName,
                                                                                                originatorServiceName,
                                                                                                originatorEngineName,
                                                                                                effectiveTime,
                                                                                                methodName);
                                }
                                else
                                {
                                    nextGovernanceActionGUID = initiateGovernanceActionFromType(userId,
                                                                                                anchorGUID,
                                                                                                governanceActionTypeGUID,
                                                                                                governanceActionTypeGUIDParameterName,
                                                                                                guard,
                                                                                                currentRequestParameters,
                                                                                                null,
                                                                                                newActionTargets,
                                                                                                new Date(),
                                                                                                processName,
                                                                                                originatorServiceName,
                                                                                                originatorEngineName,
                                                                                                effectiveTime,
                                                                                                methodName);
                                }
                            }

                            /*
                             * Link the next governance action to the previous one
                             */
                            if (nextGovernanceActionGUID != null)
                            {
                                InstanceProperties nextGovernanceActionProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                                                 null,
                                                                                                                                 OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                                                                                 guard,
                                                                                                                                 methodName);
                                nextGovernanceActionProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                                               nextGovernanceActionProperties,
                                                                                                               OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                                               mandatoryGuard,
                                                                                                               methodName);

                                nextGovernanceActionProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                                               nextGovernanceActionProperties,
                                                                                                               OpenMetadataAPIMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
                                                                                                               ignoreMultipleTriggers,
                                                                                                               methodName);

                                repositoryHandler.createRelationship(userId,
                                                                     OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_GUID,
                                                                     null,
                                                                     null,
                                                                     previousGovernanceActionGUID,
                                                                     nextGovernanceActionGUID,
                                                                     nextGovernanceActionProperties,
                                                                     methodName);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Return the list of guards that must be satisfied for the governance action to run.  This is specified in the NextGovernanceActionType
     * relationships connected to the governance action type that initiates the governance action.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type initiating the governance action
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of mandatory guards (or null)
     *
     * @throws InvalidParameterException problem with GUID
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private List<String> getMandatoryGuards(String userId,
                                            String governanceActionTypeGUID,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getMandatoryGuards";

        List<Relationship> dependedOnActionTypes = repositoryHandler.getRelationshipsByType(userId,
                                                                                            governanceActionTypeGUID,
                                                                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                            OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                                                            OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                            2,
                                                                                            false,
                                                                                            false,
                                                                                            0, 0,
                                                                                            effectiveTime,
                                                                                            methodName);
        if (dependedOnActionTypes != null)
        {
            List<String> mandatoryGuards = new ArrayList<>();

            for (Relationship dependedOnActionType : dependedOnActionTypes)
            {
                /*
                 * The interesting relationships are those that have the Governance ActionType at end2.
                 */
                if ((dependedOnActionType != null) && (dependedOnActionType.getEntityTwoProxy() != null) &&
                    (governanceActionTypeGUID.equals(dependedOnActionType.getEntityTwoProxy().getGUID())))
                {
                    boolean mandatoryGuard = repositoryHelper.getBooleanProperty(serviceName,
                                                                                 OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                 dependedOnActionType.getProperties(),
                                                                                 methodName);
                    if (mandatoryGuard)
                    {
                        String guard = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
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
     * The GovernanceActionTypeUse relationships will list all the governance actions that
     * have been triggered for this GovernanceActionType.  If a governance action for this governance action type has already been
     * triggered in this governance action process then the linked governance action will have a matching anchor classification.
     *
     *
     * If the ignoreMultipleTriggers flag is set in the next governance action type relationship, then we can only trigger one copy of
     * this next action for the process.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @param anchorGUID identifier for the process instance
     * @param guard the guard that triggered this action
     * @param ignoreMultipleTriggers are multiple instances of the corresponding governance action allowed in a process instance?
     * @param newActionTargets next set of elements to work on
     * @param effectiveTime   the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of existing governance action that needs to be used. Null indicate that a new governance action can be started.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    private String newGovernanceActionNeeded(String                userId,
                                             String                governanceActionTypeGUID,
                                             String                anchorGUID,
                                             String                guard,
                                             boolean               ignoreMultipleTriggers,
                                             Date                  effectiveTime,
                                             List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        if ((anchorGUID != null) && (governanceActionTypeGUID != null))
        {
            final String methodName = "newGovernanceActionNeeded";
            final String governanceActionGUIDParameterName = "governanceActionTypeUse.getEntityTwoProxy().getGUID()";

            /*
             * Check that this next governance action is not already triggered.
             */
            List<Relationship> governanceActionTypeUses = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   governanceActionTypeGUID,
                                                                                                   OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                                   OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_GUID,
                                                                                                   OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_USE_TYPE_NAME,
                                                                                                   2,
                                                                                                   false,
                                                                                                   false,
                                                                                                   0, 0,
                                                                                                   effectiveTime,
                                                                                                   methodName);
            if (governanceActionTypeUses != null)
            {
                for (Relationship governanceActionTypeUse : governanceActionTypeUses)
                {
                    if ((governanceActionTypeUse != null) && (governanceActionTypeUse.getEntityTwoProxy() != null))
                    {
                        EntityDetail nextGovernanceAction = this.getEntityFromRepository(userId,
                                                                                         governanceActionTypeUse.getEntityTwoProxy().getGUID(),
                                                                                         governanceActionGUIDParameterName,
                                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                                         null,
                                                                                         null,
                                                                                         false,
                                                                                         false,
                                                                                         supportedZones,
                                                                                         effectiveTime,
                                                                                         methodName);

                        if (nextGovernanceAction != null)
                        {
                            String nextGovernanceActionAnchorGUID = this.getAnchorGUIDFromAnchorsClassification(nextGovernanceAction, methodName);

                            if (anchorGUID.equals(nextGovernanceActionAnchorGUID))
                            {
                                /*
                                 * This governance action is for the same process.
                                 */
                                List<String> receivedGuards = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                      OpenMetadataAPIMapper.RECEIVED_GUARDS_PROPERTY_NAME,
                                                                                                      nextGovernanceAction.getProperties(),
                                                                                                      methodName);
                                List<String> mandatoryGuards = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                       OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                                       nextGovernanceAction.getProperties(),
                                                                                                       methodName);
                                int governanceActionStatus = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                                                     OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME,
                                                                                                     nextGovernanceAction.getProperties(),
                                                                                                     methodName);

                                /*
                                 * Should the previous governance action be linked to this one?
                                 */
                                if ((governanceActionStatus != OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL) || /* governance action is running or completed */
                                    (mandatoryGuards == null) ||                                /* there are no mandatory guards holding up the governance action */
                                    ((receivedGuards != null) && (receivedGuards.contains(guard))))                       /* This guard has already been received */
                                {
                                    if (! ignoreMultipleTriggers)
                                    {
                                        /*
                                         * If a new element is allowed then create a new one.
                                         */
                                        return null;
                                    }
                                }

                                /*
                                 * Need to use this governance action rather than creating a new one.  It needs updating with the
                                 * additional action targets and the output guards.  It may then be ready to run.
                                 */
                                String nextGovernanceActionName = repositoryHelper.getStringProperty(serviceName,
                                                                                                     OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                                     nextGovernanceAction.getProperties(),
                                                                                                     methodName);
                                this.addActionTargets(userId,
                                                      nextGovernanceAction.getGUID(),
                                                      governanceActionGUIDParameterName,
                                                      nextGovernanceActionName,
                                                      newActionTargets,
                                                      methodName);



                                if (receivedGuards == null)
                                {
                                    receivedGuards = new ArrayList<>();

                                    receivedGuards.add(guard);
                                }
                                else if (! receivedGuards.contains(guard))
                                {
                                    receivedGuards.add(guard);
                                }

                                InstanceProperties entityProperties = nextGovernanceAction.getProperties();

                                entityProperties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                                                     entityProperties,
                                                                                                     OpenMetadataAPIMapper.RECEIVED_GUARDS_PROPERTY_NAME,
                                                                                                     receivedGuards,
                                                                                                     methodName);

                                if ((governanceActionStatus == OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL) &&
                                            ((mandatoryGuards == null) || (receivedGuards.containsAll(mandatoryGuards))))
                                {
                                    try
                                    {
                                        entityProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                                      entityProperties,
                                                                                                      OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME,
                                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                                      OpenMetadataAPIMapper.APPROVED_GA_STATUS_ORDINAL,
                                                                                                      methodName);
                                    }
                                    catch (TypeErrorException error)
                                    {
                                        throw new PropertyServerException(error);
                                    }
                                }

                                repositoryHandler.updateEntityProperties(userId,
                                                                         null,
                                                                         null,
                                                                         nextGovernanceAction.getGUID(),
                                                                         nextGovernanceAction,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                         entityProperties,
                                                                         methodName);

                                return nextGovernanceAction.getGUID();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the governance actions that are known to this server.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B>  getGovernanceActions(String userId,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                             invalidParameterHandler,
                                                                             userId,
                                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
            EntityDetail nextGovernanceAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                B bean = this.getGovernanceAction(userId, nextGovernanceAction.getGUID(), effectiveTime, methodName);

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
     * Retrieve the governance actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B>  getActiveGovernanceActions(String userId,
                                               int    startFrom,
                                               int    pageSize,
                                               Date   effectiveTime,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                             invalidParameterHandler,
                                                                             userId,
                                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
            EntityDetail nextGovernanceAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                int status = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                     nextGovernanceAction.getProperties(),
                                                                     methodName);

                if ((status == OpenMetadataAPIMapper.REQUESTED_GA_STATUS_ORDINAL) || (status == OpenMetadataAPIMapper.APPROVED_GA_STATUS_ORDINAL) ||
                    (status == OpenMetadataAPIMapper.WAITING_GA_STATUS_ORDINAL) || (status == OpenMetadataAPIMapper.IN_PROGRESS_GA_STATUS_ORDINAL))
                {
                    B bean = this.getGovernanceAction(userId, nextGovernanceAction.getGUID(), effectiveTime, methodName);

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
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                      Date   effectiveTime,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "governanceEngineGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                     userId,
                                                                                     methodName);

        RepositorySelectedEntitiesIterator iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                                             invalidParameterHandler,
                                                                                             userId,
                                                                                             OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_GUID,
                                                                                             properties,
                                                                                             MatchCriteria.ANY,
                                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
            EntityDetail nextGovernanceAction = iterator.getNext();

            if (entityCount > startFrom)
            {
                int status = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                     nextGovernanceAction.getProperties(),
                                                                     methodName);

                if ((status == OpenMetadataAPIMapper.WAITING_GA_STATUS_ORDINAL) || (status == OpenMetadataAPIMapper.IN_PROGRESS_GA_STATUS_ORDINAL))
                {
                    B bean = this.getGovernanceAction(userId, nextGovernanceAction.getGUID(), effectiveTime, methodName);

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
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                         Date   effectiveTime,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String guidParameterName = "actionTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTargetGUID, guidParameterName, methodName);

        Relationship actionTarget = repositoryHandler.getRelationshipByGUID(userId,
                                                                            actionTargetGUID,
                                                                            guidParameterName,
                                                                            OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME,
                                                                            effectiveTime,
                                                                            methodName);

        if (actionTarget != null)
        {
            InstanceProperties newActionTargetProperties;

            try
            {
                newActionTargetProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                       actionTarget.getProperties(),
                                                                                       OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                                       OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.GOVERNANCE_ACTION_STATUS_ENUM_TYPE_NAME,
                                                                                       status,
                                                                                       methodName);
            }
            catch (TypeErrorException error)
            {
                throw new PropertyServerException(error);
            }

            newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                   newActionTargetProperties,
                                                                                   OpenMetadataAPIMapper.START_DATE_PROPERTY_NAME,
                                                                                   startDate,
                                                                                   methodName);

            newActionTargetProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                   newActionTargetProperties,
                                                                                   OpenMetadataAPIMapper.COMPLETION_DATE_PROPERTY_NAME,
                                                                                   completionDate,
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
