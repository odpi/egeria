/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * APIOperationHandler provides the exchange of metadata about APIOperation schema types between the repository and the OMAS.
 *
 * @param <B> class that represents the API Operation
 */
public class APIOperationHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public APIOperationHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the API Operation object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiGUID unique identifier of the owning topic
     * @param apiGUIDParameterName parameter supplying apiGUID
     * @param qualifiedName unique name for the API Operation - used in other configuration
     * @param displayName short display name for the API Operation
     * @param description description of the API Operation
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties for an API Operation
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for an API Operation subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new API Operation object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createAPIOperation(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              apiGUID,
                                     String              apiGUIDParameterName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              versionNumber,
                                     boolean             isDeprecated,
                                     String              author,
                                     String              usage,
                                     String              encodingStandard,
                                     String              namespace,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, apiGUIDParameterName, methodName);

        String typeName = OpenMetadataType.API_OPERATION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.API_OPERATION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        final String apiSchemaTypeGUIDParameterName = "apiSchemaTypeGUID";

        String apiSchemaTypeGUID = this.getAPISchemaTypeGUID(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             apiGUID,
                                                             apiGUIDParameterName,
                                                             qualifiedName,
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          versionNumber,
                                                          isDeprecated,
                                                          author,
                                                          usage,
                                                          encodingStandard,
                                                          namespace,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    apiGUID,
                                    apiGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String apiOperationGUID = this.createBeanInRepository(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              typeGUID,
                                                              typeName,
                                                              builder,
                                                              effectiveTime,
                                                              methodName);

        if (apiOperationGUID != null)
        {
            /*
             * Link the API Operation to the topic's event list.
             */
            final String apiOperationGUIDParameterName = "apiOperationGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               apiSchemaTypeGUID,
                                               apiSchemaTypeGUIDParameterName,
                                               OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                               apiOperationGUID,
                                               apiOperationGUIDParameterName,
                                               OpenMetadataType.API_OPERATION_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataType.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                               OpenMetadataType.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
                                               null,
                                               effectiveTime,
                                               methodName);
        }

        return apiOperationGUID;
    }


    /**
     * Create an API Operation from a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiGUID unique identifier of the owning topic
     * @param apiGUIDParameterName parameter supplying apiGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the API Operation - used in other configuration
     * @param displayName short display name for the API Operation
     * @param description description of the API Operation
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIOperationFromTemplate(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  apiGUID,
                                                 String  apiGUIDParameterName,
                                                 String  templateGUID,
                                                 String  qualifiedName,
                                                 String  displayName,
                                                 String  description,
                                                 Date    effectiveFrom,
                                                 Date    effectiveTo,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String apiSchemaTypeGUIDParameterName = "apiSchemaTypeGUID";

        String apiSchemaTypeGUID = this.getAPISchemaTypeGUID(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             apiGUID,
                                                             apiGUIDParameterName,
                                                             qualifiedName,
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        String apiOperationGUID = this.createBeanFromTemplate(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              templateGUID,
                                                              templateGUIDParameterName,
                                                              OpenMetadataType.API_OPERATION_TYPE_GUID,
                                                              OpenMetadataType.API_OPERATION_TYPE_NAME,
                                                              qualifiedName,
                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                              builder,
                                                              supportedZones,
                                                              true,
                                                              false,
                                                              null,
                                                              methodName);

        /*
         * Link the API Operation to the topic's event list.
         */
        final String apiOperationGUIDParameterName = "apiOperationGUID";

        this.uncheckedLinkElementToElement(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           apiSchemaTypeGUID,
                                           apiSchemaTypeGUIDParameterName,
                                           OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                           apiOperationGUID,
                                           apiOperationGUIDParameterName,
                                           OpenMetadataType.API_OPERATION_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                           OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                           null,
                                           effectiveTime,
                                           methodName);

        return apiOperationGUID;
    }


    /**
     * Update the API Operation.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiOperationGUID unique identifier for the API Operation to update
     * @param apiOperationGUIDParameterName parameter supplying the API Operation
     * @param qualifiedName unique name for the API Operation - used in other configuration
     * @param displayName short display name for the API Operation
     * @param description description of the governance API Operation
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties for an API Operation
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance API Operation subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateAPIOperation(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              apiOperationGUID,
                                     String              apiOperationGUIDParameterName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              versionNumber,
                                     boolean             isDeprecated,
                                     String              author,
                                     String              usage,
                                     String              encodingStandard,
                                     String              namespace,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     boolean             isMergeUpdate,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, apiOperationGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.API_OPERATION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.API_OPERATION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          versionNumber,
                                                          isDeprecated,
                                                          author,
                                                          usage,
                                                          encodingStandard,
                                                          namespace,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    apiOperationGUID,
                                    apiOperationGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the metadata element representing an API Operations.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param apiOperationGUIDParameterName parameter for apiOperationGUID
     * @param qualifiedName validating property
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIOperation(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  apiOperationGUID,
                                   String  apiOperationGUIDParameterName,
                                   String  qualifiedName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    apiOperationGUID,
                                    apiOperationGUIDParameterName,
                                    OpenMetadataType.API_OPERATION_TYPE_GUID,
                                    OpenMetadataType.API_OPERATION_TYPE_NAME,
                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                    qualifiedName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of API Operations metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findAPIOperations(String  userId,
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
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.API_OPERATION_TYPE_GUID,
                              OpenMetadataType.API_OPERATION_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of API Operation metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getAPIOperationsByName(String  userId,
                                            String  name,
                                            String  nameParameterName,
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
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.API_OPERATION_TYPE_GUID,
                                    OpenMetadataType.API_OPERATION_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the API Operations metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the requested metadata element
     * @param apiGUIDParameterName parameter name of the apiGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of API Operations element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getAPIOperationsForAPI(String  userId,
                                          String  apiGUID,
                                          String  apiGUIDParameterName,
                                          int     startFrom,
                                          int     pageSize,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return this.getAPIOperationsForAPI(userId, apiGUID, apiGUIDParameterName, supportedZones, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve the API Operations metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the requested metadata element
     * @param apiGUIDParameterName parameter name of the apiGUID
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of API Operations element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getAPIOperationsForAPI(String       userId,
                                          String       apiGUID,
                                          String       apiGUIDParameterName,
                                          List<String> serviceSupportedZones,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        /*
         * The API Operations are attached via an event list.
         */
        EntityDetail apiSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                  apiGUID,
                                                                  apiGUIDParameterName,
                                                                  OpenMetadataType.DEPLOYED_API.typeName,
                                                                  OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                  OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                  OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                                  2,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  serviceSupportedZones,
                                                                  effectiveTime,
                                                                  methodName);

        if (apiSchemaTypeEntity != null)
        {
            final String apiSchemaTypeGUIDParameterName = "apiSchemaTypeGUID";

            return this.getAttachedElements(userId,
                                            null,
                                            null,
                                            apiSchemaTypeEntity.getGUID(),
                                            apiSchemaTypeGUIDParameterName,
                                            OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                            OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                            OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                            OpenMetadataType.API_OPERATION_TYPE_NAME,
                                            null,
                                            null,
                                            2,
                                            forLineage,
                                            forDuplicateProcessing,
                                            serviceSupportedZones,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }

        return null;
    }


    /**
     * Retrieve the API Operations metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param apiSchemaTypeGUID unique identifier of the requested metadata element
     * @param apiSchemaTypeGUIDParameterName parameter name of the apiSchemaTypeGUID
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of API Operations element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getAPIOperationsForAPISchemaType(String       userId,
                                                    String       apiSchemaTypeGUID,
                                                    String       apiSchemaTypeGUIDParameterName,
                                                    List<String> serviceSupportedZones,
                                                    int          startFrom,
                                                    int          pageSize,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        /*
         * The API Operations are attached via an event list.
         */
        EntityDetail apiSchemaTypeEntity = this.getEntityFromRepository(userId,
                                                                        apiSchemaTypeGUID,
                                                                        apiSchemaTypeGUIDParameterName,
                                                                        OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                                        null,
                                                                        null,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        serviceSupportedZones,
                                                                        effectiveTime,
                                                                        methodName);

        if (apiSchemaTypeEntity != null)
        {
            return this.getAttachedElements(userId,
                                            null,
                                            null,
                                            apiSchemaTypeEntity.getGUID(),
                                            apiSchemaTypeGUIDParameterName,
                                            OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                            OpenMetadataType.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                            OpenMetadataType.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
                                            OpenMetadataType.API_OPERATION_TYPE_NAME,
                                            null,
                                            null,
                                            2,
                                            forLineage,
                                            forDuplicateProcessing,
                                            serviceSupportedZones,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }

        return null;
    }


    /**
     * Create/retrieve the API schema type for the operation.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiGUID topic to retrieve from
     * @param apiGUIDParameterName parameter name or apiGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of event list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private String getAPISchemaTypeGUID(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  apiGUID,
                                        String  apiGUIDParameterName,
                                        String  topicQualifiedName,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        String apiSchemaTypeGUID;

        EntityDetail apiSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                  apiGUID,
                                                                  apiGUIDParameterName,
                                                                  OpenMetadataType.DEPLOYED_API.typeName,
                                                                  OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                  OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                  OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                                  2,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  supportedZones,
                                                                  effectiveTime,
                                                                  methodName);

        if (apiSchemaTypeEntity == null)
        {
            SchemaTypeBuilder builder = new SchemaTypeBuilder(topicQualifiedName + "_EventList",
                                                              OpenMetadataType.API_SCHEMA_TYPE_TYPE_GUID,
                                                              OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

            this.addAnchorGUIDToBuilder(userId,
                                        apiGUID,
                                        apiGUIDParameterName,
                                        false,
                                        false,
                                        effectiveTime,
                                        supportedZones,
                                        builder,
                                        methodName);

            apiSchemaTypeGUID = repositoryHandler.createEntity(userId,
                                                               OpenMetadataType.API_SCHEMA_TYPE_TYPE_GUID,
                                                               OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               builder.getInstanceProperties(methodName),
                                                               builder.getEntityClassifications(),
                                                               builder.getInstanceStatus(),
                                                               methodName);

            if (apiSchemaTypeGUID != null)
            {
                final String apiSchemaTypeGUIDParameterName = "apiSchemaTypeGUID";

                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          apiGUID,
                                          apiGUIDParameterName,
                                          OpenMetadataType.DEPLOYED_API.typeName,
                                          apiSchemaTypeGUID,
                                          apiSchemaTypeGUIDParameterName,
                                          OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }
            else
            {
                errorHandler.logNullInstance(OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME, methodName);
            }
        }
        else
        {
            apiSchemaTypeGUID = apiSchemaTypeEntity.getGUID();
        }

        return apiSchemaTypeGUID;
    }


    /**
     * Retrieve the API Operation metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getAPIOperationByGUID(String  userId,
                                   String  guid,
                                   String  guidParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.API_OPERATION_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

    }
}
