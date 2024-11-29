/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
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
     * @param additionalProperties additional properties for a governance action
     * @param governanceEngineGUID unique identifier of governance engine to execute the request
     * @param calledRequestType type of request
     * @param fixedRequestParameters properties for the request type
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGovernanceActionType(String                     userId,
                                             String                     qualifiedName,
                                             int                        domainIdentifier,
                                             String                     displayName,
                                             String                     description,
                                             Map<String, String>        additionalProperties,
                                             String                     governanceEngineGUID,
                                             String                     calledRequestType,
                                             Map<String, String>        fixedRequestParameters,
                                             int                        waitTime,
                                             Date                       effectiveFrom,
                                             Date                       effectiveTo,
                                             boolean                    forLineage,
                                             boolean                    forDuplicateProcessing,
                                             List<String>               serviceSupportedZones,
                                             Date                       effectiveTime,
                                             String                     methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String qualifiedNameParameterName = "properties.getQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GovernanceActionTypeBuilder builder = new GovernanceActionTypeBuilder(qualifiedName,
                                                                              domainIdentifier,
                                                                              displayName,
                                                                              description,
                                                                              waitTime,
                                                                              additionalProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String governanceActionTypeGUID = this.createBeanInRepository(userId,
                                                                      null,
                                                                      null,
                                                                      OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                                                      OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                      builder,
                                                                      effectiveTime,
                                                                      methodName);

        if ((governanceActionTypeGUID != null) && (governanceEngineGUID != null))
        {
            this.linkGovernanceActionExecutor(userId,
                                              governanceActionTypeGUID,
                                              governanceEngineGUID,
                                              calledRequestType,
                                              fixedRequestParameters,
                                              effectiveFrom,
                                              effectiveTo,
                                              forLineage,
                                              forDuplicateProcessing,
                                              serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
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
                                              List<String>        serviceSupportedZones,
                                              Date                effectiveTime,
                                              String              methodName)  throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String governanceActionTypeGUIDParameterName = "governanceActionTypeGUID";
        final String governanceEngineGUIDParameterName = "governanceEngineGUID";

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                 null,
                                                                                                 OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                                 requestType,
                                                                                                 methodName);

        relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                 relationshipProperties,
                                                                                 OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                 requestParameters,
                                                                                 methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  governanceActionTypeGUID,
                                  governanceActionTypeGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                  governanceEngineGUID,
                                  governanceEngineGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID,
                                  OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
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
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param additionalProperties additional properties for a governance action
     * @param governanceEngineGUID unique identifier of governance engine to execute the request
     * @param requestType type of request
     * @param requestParameters properties for the request type
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGovernanceActionType(String              userId,
                                           String              governanceActionTypeGUID,
                                           boolean             isMergeUpdate,
                                           String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           Map<String, String> additionalProperties,
                                           String              governanceEngineGUID,
                                           String              requestType,
                                           Map<String, String> requestParameters,
                                           int                 waitTime,
                                           Date                effectiveFrom,
                                           Date                effectiveTo,
                                           boolean             forLineage,
                                           boolean             forDuplicateProcessing,
                                           List<String>        serviceSupportedZones,
                                           Date                effectiveTime,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String governanceActionTypeGUIDParameterName = "governanceActionTypeGUID";
        final String governanceEngineGUIDParameterName = "governanceEngineGUID";
        final String qualifiedNameParameterName = "properties.getQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionTypeGUID, governanceActionTypeGUIDParameterName, methodName);

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
                                                                                              governanceActionTypeGUID,
                                                                                              OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                                              OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID,
                                                                                              OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                                              2,
                                                                                              null,
                                                                                              null,
                                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                                              null,
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
                                                  governanceActionTypeGUID,
                                                  governanceEngineGUID,
                                                  requestType,
                                                  requestParameters,
                                                  effectiveFrom,
                                                  effectiveTo,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
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
                                                      governanceActionTypeGUID,
                                                      governanceEngineGUID,
                                                      requestType,
                                                      requestParameters,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      serviceSupportedZones,
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
                                                                              waitTime,
                                                                              additionalProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    governanceActionTypeGUID,
                                    governanceActionTypeGUIDParameterName,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
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
                                                                              OpenMetadataProperty.REQUEST_TYPE.name,
                                                                              requestType,
                                                                              methodName);

        relationshipProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                 relationshipProperties,
                                                                                 OpenMetadataProperty.REQUEST_PARAMETERS.name,
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
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceActionType(String       userId,
                                           String       governanceActionTypeGUID,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           List<String> serviceSupportedZones,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String guidParameterName = "governanceActionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionTypeGUID, guidParameterName, methodName);

        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    governanceActionTypeGUID,
                                    guidParameterName,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
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
    public List<B> findGovernanceActionTypes(String       userId,
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

        return this.getBeansByValue(userId,
                                    searchString,
                                    searchStringParameterName,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    false,
                                    null,
                                    null,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
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
    public List<B> getGovernanceActionTypesByName(String       userId,
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

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    false,
                                    false,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSupportedZones supported zones for calling service
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceActionTypeByGUID(String       userId,
                                           String       governanceActionTypeGUID,
                                           List<String> serviceSupportedZones,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String guidParameterName = "governanceActionTypeGUID";

        return this.getBeanFromRepository(userId,
                                          governanceActionTypeGUID,
                                          guidParameterName,
                                          OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                          false,
                                          false,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);
    }
}
