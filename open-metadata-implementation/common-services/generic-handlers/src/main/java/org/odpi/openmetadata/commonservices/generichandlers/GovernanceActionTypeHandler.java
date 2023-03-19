/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceActionTypeHandler manages GovernanceActionType entities and their relationships.
 */
public class GovernanceActionTypeHandler<B> extends OpenMetadataAPIGenericHandler<B>
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
    public GovernanceActionTypeHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param supportedGuards list of guards that triggered this governance action
     * @param additionalProperties additional properties for a governance action
     * @param governanceEngineGUID unique identifier of governance engine to execute the request
     * @param requestType type of request
     * @param requestParameters properties for the request type
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGovernanceActionType(String               userId,
                                             String               qualifiedName,
                                             int                  domainIdentifier,
                                             String               displayName,
                                             String               description,
                                             List<String>         supportedGuards,
                                             Map<String, String>  additionalProperties,
                                             String               governanceEngineGUID,
                                             String               requestType,
                                             Map<String, String>  requestParameters,
                                             boolean              ignoreMultipleTriggers,
                                             int                  waitTime,
                                             Date                 effectiveFrom,
                                             Date                 effectiveTo,
                                             boolean              forLineage,
                                             boolean              forDuplicateProcessing,
                                             Date                 effectiveTime,
                                             String               methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GovernanceActionTypeBuilder builder = new GovernanceActionTypeBuilder(qualifiedName,
                                                                              domainIdentifier,
                                                                              displayName,
                                                                              description,
                                                                              supportedGuards,
                                                                              ignoreMultipleTriggers,
                                                                              waitTime,
                                                                              additionalProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String governanceActionTypeGUID = this.createBeanInRepository(userId,
                                                                      null,
                                                                      null,
                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                                      OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                      builder,
                                                                      effectiveTime,
                                                                      methodName);

        if ((governanceActionTypeGUID != null) && (governanceEngineGUID != null))
        {
            this.linkGovernanceActionExecutor(userId,
                                              governanceActionTypeGUID,
                                              governanceEngineGUID,
                                              requestType,
                                              requestParameters,
                                              effectiveFrom,
                                              effectiveTo,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
        }

        return governanceActionTypeGUID;
    }


    /**
     * Link the governance action type to a governance engine.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param governanceEngineGUID unique identifier of governance engine to execute the request
     * @param requestType type of request
     * @param requestParameters properties for the request type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void linkGovernanceActionExecutor(String              userId,
                                              String              governanceActionTypeGUID,
                                              String              governanceEngineGUID,
                                              String              requestType,
                                              Map<String, String> requestParameters,
                                              Date                effectiveFrom,
                                              Date                effectiveTo,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              Date                effectiveTime,
                                              String              methodName)  throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String governanceActionTypeGUIDParameterName = "governanceActionTypeGUID";
        final String governanceEngineGUIDParameterName = "governanceEngineGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                 null,
                                                                                                 OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                                                 requestType,
                                                                                                 methodName);

        relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                 relationshipProperties,
                                                                                 OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                 requestParameters,
                                                                                 methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  governanceActionTypeGUID,
                                  governanceActionTypeGUIDParameterName,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                  governanceEngineGUID,
                                  governanceEngineGUIDParameterName,
                                  OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_GUID,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_NAME,
                                  setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param supportedGuards list of guards that triggered this governance action
     * @param additionalProperties additional properties for a governance action
     * @param governanceEngineGUID unique identifier of governance engine to execute the request
     * @param requestType type of request
     * @param requestParameters properties for the request type
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGovernanceActionType(String              userId,
                                           String              actionTypeGUID,
                                           boolean             isMergeUpdate,
                                           String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           List<String>        supportedGuards,
                                           Map<String, String> additionalProperties,
                                           String              governanceEngineGUID,
                                           String              requestType,
                                           Map<String, String> requestParameters,
                                           boolean             ignoreMultipleTriggers,
                                           int                 waitTime,
                                           Date                effectiveFrom,
                                           Date                effectiveTo,
                                           boolean             forLineage,
                                           boolean             forDuplicateProcessing,
                                           Date                effectiveTime,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String actionTypeGUIDParameterName = "actionTypeGUID";
        final String governanceEngineGUIDParameterName = "governanceEngineGUID";
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, actionTypeGUIDParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        /*
         * Handle the relationship with the governance engine.
         */
        if ((! isMergeUpdate) || (governanceEngineGUID != null) || (requestType != null) || (requestParameters != null))
        {
            Relationship executorRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                              actionTypeGUID,
                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_GUID,
                                                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_EXECUTOR_TYPE_NAME,
                                                                                              2,
                                                                                              forLineage,
                                                                                              forDuplicateProcessing,
                                                                                              effectiveTime,
                                                                                              methodName);

            if (executorRelationship == null)
            {
                /*
                 * No relationship exists so need to set it up.
                 */
                invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameterName, methodName);

                this.linkGovernanceActionExecutor(userId,
                                                  actionTypeGUID,
                                                  governanceEngineGUID,
                                                  requestType,
                                                  requestParameters,
                                                  effectiveFrom,
                                                  effectiveTo,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
            }
            else
            {
                if (governanceEngineGUID == null)
                {
                    /*
                     * Remove the link to the governance engine if this is not a merge update.  If it is a merge update then
                     * the current relationship is valid and only the properties need updating.
                     */
                    if (! isMergeUpdate)
                    {
                        repositoryHandler.removeRelationship(userId,
                                                             null,
                                                             null,
                                                             executorRelationship,
                                                             methodName);
                    }
                    else
                    {
                        /*
                         * Update the properties
                         */
                        updateExecutorRelationship(userId,
                                                   executorRelationship,
                                                   requestType,
                                                   requestParameters,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   true,
                                                   methodName);
                    }
                }
                else if (governanceEngineGUID.equals(executorRelationship.getEntityTwoProxy().getGUID()))
                {
                    /*
                     * The governance engine is still correct so only need to update the properties.
                     */
                    updateExecutorRelationship(userId,
                                               executorRelationship,
                                               requestType,
                                               requestParameters,
                                               effectiveFrom,
                                               effectiveTo,
                                               isMergeUpdate,
                                               methodName);
                }
                else
                {
                    /*
                     * The governance action is currently linked to the wrong governance engine
                     */
                    repositoryHandler.removeRelationship(userId,
                                                         null,
                                                         null,
                                                         executorRelationship,
                                                         methodName);

                    this.linkGovernanceActionExecutor(userId,
                                                      actionTypeGUID,
                                                      governanceEngineGUID,
                                                      requestType,
                                                      requestParameters,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
                }
            }
        }

        /*
         * Now update the governance action type entity
         */
        GovernanceActionTypeBuilder builder = new GovernanceActionTypeBuilder(qualifiedName,
                                                                              domainIdentifier,
                                                                              displayName,
                                                                              description,
                                                                              supportedGuards,
                                                                              ignoreMultipleTriggers,
                                                                              waitTime,
                                                                              additionalProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    actionTypeGUID,
                                    actionTypeGUIDParameterName,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Maintain the properties in the relationship between a governance action type and a governance engine.
     *
     * @param userId calling user
     * @param executorRelationship existing relationship
     * @param requestType type of request
     * @param requestParameters properties for the request type
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void updateExecutorRelationship(String              userId,
                                            Relationship        executorRelationship,
                                            String              requestType,
                                            Map<String, String> requestParameters,
                                            Date                effectiveFrom,
                                            Date                effectiveTo,
                                            boolean             isMergeUpdate,
                                            String              methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        InstanceProperties relationshipProperties = null;

        if (isMergeUpdate)
        {
            relationshipProperties = executorRelationship.getProperties();
        }

        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              relationshipProperties,
                                                                              OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                              requestType,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                 relationshipProperties,
                                                                                 OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                 requestParameters,
                                                                                 methodName);

        repositoryHandler.updateRelationshipProperties(userId,
                                                       null,
                                                       null,
                                                       executorRelationship,
                                                       setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                                       methodName);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceActionType(String  userId,
                                           String  actionTypeGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    actionTypeGUID,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName parameter supplying search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGovernanceActionTypes(String  userId,
                                             String  searchString,
                                             String  searchStringParameterName,
                                             int     startFrom,
                                             int     pageSize,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName name of parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGovernanceActionTypesByName(String userId,
                                                  String name,
                                                  String nameParameterName,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  Date   effectiveTime,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    false,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceActionTypeByGUID(String userId,
                                           String actionTypeGUID,
                                           Date   effectiveTime,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        return this.getBeanFromRepository(userId,
                                          actionTypeGUID,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                          false,
                                          false,
                                          effectiveTime,
                                          methodName);
    }



    /**
     * Set up a link between a governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param guard initial guard to pass to the first governance service called
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupFirstActionType(String  userId,
                                     String  processGUID,
                                     String  actionTypeGUID,
                                     String  guard,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String processGUIDParameterName = "processGUID";
        final String actionTypeGUIDParameterName = "actionTypeGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                 null,
                                                                                                 OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                                                 guard,
                                                                                                 methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  processGUID,
                                  processGUIDParameterName,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                  actionTypeGUID,
                                  actionTypeGUIDParameterName,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_GUID,
                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                  setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param firstActionTypeLink Governance Action Flow relationship (if known)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getFirstActionType(String       userId,
                                String       processGUID,
                                Relationship firstActionTypeLink,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String processGUIDParameterName = "processGUID";

        if ((firstActionTypeLink != null) && (firstActionTypeLink.getEntityTwoProxy() != null))
        {
            return this.getGovernanceActionTypeByGUID(userId, firstActionTypeLink.getEntityTwoProxy().getGUID(), effectiveTime, methodName);
        }
        else
        {
            return this.getAttachedElement(userId,
                                           processGUID,
                                           processGUIDParameterName,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                           0,
                                           false,
                                           false,
                                           supportedZones,
                                           effectiveTime,
                                           methodName);
        }
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Relationship getFirstActionTypeLink(String userId,
                                               String processGUID,
                                               Date   effectiveTime,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String processGUIDParameterName = "processGUID";

        return this.getUniqueAttachmentLink(userId,
                                            processGUID,
                                            processGUIDParameterName,
                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_GUID,
                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                            null,
                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                            2,
                                            false,
                                            false,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeFirstActionType(String userId,
                                      String processGUID,
                                      Date   effectiveTime,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String processGUIDParameterName = "processGUID";

        this.unlinkConnectedElement(userId,
                                    false,
                                    null,
                                    null,
                                    processGUID,
                                    processGUIDParameterName,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                    false,
                                    false,
                                    supportedZones,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    effectiveTime,
                                    methodName);
    }



    /**
     * Add a link between two governance action types to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param userId calling user
     * @param currentActionTypeGUID unique identifier of the governance action type that defines the previous step in the governance action process
     * @param nextActionTypeGUID unique identifier of the governance action type that defines the next step in the governance action process
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return unique identifier of the new link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupNextActionType(String   userId,
                                      String   currentActionTypeGUID,
                                      String   nextActionTypeGUID,
                                      String   guard,
                                      boolean  mandatoryGuard,
                                      Date     effectiveFrom,
                                      Date     effectiveTo,
                                      boolean  forLineage,
                                      boolean  forDuplicateProcessing,
                                      Date     effectiveTime,
                                      String   methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String currentGUIDParameterName = "currentActionTypeGUID";
        final String nextGUIDParameterName = "nextActionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentActionTypeGUID, currentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextActionTypeGUID, nextGUIDParameterName, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                 null,
                                                                                                 OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                                                 guard,
                                                                                                 methodName);

        relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                               relationshipProperties,
                                                                               OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                               mandatoryGuard,
                                                                               methodName);

        return this.linkElementToElement(userId,
                                         null,
                                         null,
                                         currentActionTypeGUID,
                                         currentGUIDParameterName,
                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                         nextActionTypeGUID,
                                         nextGUIDParameterName,
                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                         forLineage,
                                         forDuplicateProcessing,
                                         supportedZones,
                                         OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                         OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                         this.setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNextActionType(String  userId,
                                     String  nextActionLinkGUID,
                                     String  guard,
                                     boolean mandatoryGuard,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String guidParameterName = "nextActionLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(nextActionLinkGUID, guidParameterName, methodName);

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                 null,
                                                                                                 OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                                                 guard,
                                                                                                 methodName);

        relationshipProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                               relationshipProperties,
                                                                               OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                               mandatoryGuard,
                                                                               methodName);

        this.setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo);

        repositoryHandler.updateRelationshipProperties(userId,
                                                       null,
                                                       null,
                                                       nextActionLinkGUID,
                                                       relationshipProperties,
                                                       methodName);
    }


    /**
     * Return the list of next action type defined for the governance action process.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Relationship> getNextGovernanceActionTypes(String userId,
                                                           String actionTypeGUID,
                                                           int    startFrom,
                                                           int    pageSize,
                                                           Date   effectiveTime,
                                                           String methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        return repositoryHandler.getRelationshipsByType(userId,
                                                        actionTypeGUID,
                                                        OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                        OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                        2,
                                                        false,
                                                        false,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNextActionType(String userId,
                                     String actionLinkGUID,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionLinkGUID, guidParameterName, methodName);

        repositoryHandler.removeRelationship(userId,
                                             null,
                                             null,
                                             OpenMetadataAPIMapper.NEXT_GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                             actionLinkGUID,
                                             methodName);
    }
}
