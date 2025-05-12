/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APIParameterListHandler provides the exchange of metadata about APIParameterList schema types between the repository and the OMAS.
 *
 * @param <B> class that represents the API parameter list
 */
public class APIParameterListHandler<B> extends ReferenceableHandler<B>
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
    public APIParameterListHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Add the required property into the extended properties
     *
     * @param suppliedExtendedProperties supplied by the caller
     * @param required subtype's property
     * @return combines extended properties
     */
    private Map<String, Object> getExtendedProperties(Map<String, Object> suppliedExtendedProperties,
                                                      boolean             required)
    {
        Map<String, Object> schemaTypeExtendedProperties = suppliedExtendedProperties;

        if (schemaTypeExtendedProperties == null)
        {
            schemaTypeExtendedProperties = new HashMap<>();
        }

        schemaTypeExtendedProperties.put(OpenMetadataProperty.REQUIRED.name, required);

        return schemaTypeExtendedProperties;
    }


    /**
     * Create the API parameter list object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiOperationGUID unique identifier of the owning API operation
     * @param apiOperationGUIDParameterName parameter supplying apiOperationGUID
     * @param qualifiedName unique name for the API parameter list - used in other configuration
     * @param displayName short display name for the API parameter list
     * @param description description of the API parameter list
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema
     * @param namespace namespace where the schema is defined.
     * @param required is this parameter list required when the API is called?
     * @param additionalProperties additional properties for an API parameter list
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for an API parameter list subtype
     * @param relationshipTypeName which relationship should connect the APIOperation and the APIParameterList?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new API parameter list object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createAPIParameterList(String              userId,
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
                                         boolean             required,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         String              relationshipTypeName,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, apiOperationGUIDParameterName, methodName);

        String typeName = OpenMetadataType.API_PARAMETER_LIST.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.API_PARAMETER_LIST.typeName,
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
                                                          getExtendedProperties(extendedProperties, required),
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    apiOperationGUID,
                                    apiOperationGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        String apiParameterListGUID = this.createBeanInRepository(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  typeGUID,
                                                                  typeName,
                                                                  builder,
                                                                  effectiveTime,
                                                                  methodName);

        if (apiParameterListGUID != null)
        {
            /*
             * Link the API parameter list to the API operation's event list.
             */
            final String apiParameterListGUIDParameterName = "apiParameterListGUID";

            TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(serviceName, relationshipTypeName);

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               apiOperationGUID,
                                               apiOperationGUIDParameterName,
                                               apiParameterListGUID,
                                               apiParameterListGUIDParameterName,
                                               relationshipTypeDef.getGUID(),
                                               null,
                                               methodName);
        }

        return apiParameterListGUID;
    }


    /**
     * Create an API parameter list from a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiOperationGUID unique identifier of the owning API operation
     * @param apiOperationGUIDParameterName parameter supplying apiOperationGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the API parameter list - used in other configuration
     * @param displayName short display name for the API parameter list
     * @param description description of the API parameter list
     * @param relationshipTypeName which relationship should connect the APIOperation and the APIParameterList?
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
    public String createAPIParameterListFromTemplate(String  userId,
                                                     String  externalSourceGUID,
                                                     String  externalSourceName,
                                                     String  apiOperationGUID,
                                                     String  apiOperationGUIDParameterName,
                                                     String  templateGUID,
                                                     String  qualifiedName,
                                                     String  displayName,
                                                     String  description,
                                                     String  relationshipTypeName,
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

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        String apiParameterListGUID = this.createBeanFromTemplate(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  templateGUID,
                                                                  templateGUIDParameterName,
                                                                  OpenMetadataType.API_PARAMETER_LIST.typeGUID,
                                                                  OpenMetadataType.API_PARAMETER_LIST.typeName,
                                                                  qualifiedName,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  builder,
                                                                  supportedZones,
                                                                  true,
                                                                  false,
                                                                  null,
                                                                  methodName);

        /*
         * Link the API parameter list to the API operation's event list.
         */
        if (apiParameterListGUID != null)
        {
            final String apiParameterListGUIDParameterName = "apiParameterListGUID";

            TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(serviceName, relationshipTypeName);

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               apiOperationGUID,
                                               apiOperationGUIDParameterName,
                                               apiParameterListGUID,
                                               apiParameterListGUIDParameterName,
                                               relationshipTypeDef.getGUID(),
                                               null,
                                               methodName);
        }

        return apiParameterListGUID;
    }


    /**
     * Update the API parameter list.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiParameterListGUID unique identifier for the API parameter list to update
     * @param apiParameterListGUIDParameterName parameter supplying the API parameter list
     * @param qualifiedName unique name for the API parameter list - used in other configuration
     * @param displayName short display name for the API parameter list
     * @param description description of the governance API parameter list
     * @param versionNumber version of the schema type
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used
     * @param encodingStandard format of the schema
     * @param namespace namespace where the schema is defined
     * @param required is this parameter required when the API is invoked
     * @param additionalProperties additional properties for an API parameter list
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance API parameter list subtype
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
    public void   updateAPIParameterList(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              apiParameterListGUID,
                                         String              apiParameterListGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         String              versionNumber,
                                         boolean             isDeprecated,
                                         String              author,
                                         String              usage,
                                         String              encodingStandard,
                                         String              namespace,
                                         boolean             required,
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
        invalidParameterHandler.validateGUID(apiParameterListGUID, apiParameterListGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.API_PARAMETER_LIST.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.API_PARAMETER_LIST.typeName,
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
                                                          getExtendedProperties(extendedProperties, required),
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    apiParameterListGUID,
                                    apiParameterListGUIDParameterName,
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
     * Remove the metadata element representing an API parameter lists.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param apiParameterListGUIDParameterName parameter for apiParameterListGUID
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
    public void removeAPIParameterList(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  apiParameterListGUID,
                                       String  apiParameterListGUIDParameterName,
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
                                    apiParameterListGUID,
                                    apiParameterListGUIDParameterName,
                                    OpenMetadataType.API_PARAMETER_LIST.typeGUID,
                                    OpenMetadataType.API_PARAMETER_LIST.typeName,
                                    false,
                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                    qualifiedName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of API parameter lists metadata elements that contain the search string.
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
    public List<B> findAPIParameterLists(String  userId,
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
        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        OpenMetadataType.API_PARAMETER_LIST.typeGUID,
                                                        OpenMetadataType.API_PARAMETER_LIST.typeName,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        null,
                                                        null,
                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        return this.getBeansForEntities(userId,
                                        entities,
                                        searchStringParameterName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of API parameter list metadata elements with a matching qualified or display name.
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
    public List<B>   getAPIParameterListsByName(String  userId,
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

        List<EntityDetail> entities = this.getEntitiesByValue(userId,
                                                              name,
                                                              nameParameterName,
                                                              OpenMetadataType.API_PARAMETER_LIST.typeGUID,
                                                              OpenMetadataType.API_PARAMETER_LIST.typeName,
                                                              specificMatchPropertyNames,
                                                              true,
                                                              false,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              supportedZones,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              methodName);

        return this.getBeansForEntities(userId,
                                        entities,
                                        nameParameterName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Convert entities into beans.
     *
     * @param userId calling user
     * @param entities retrieved entities
     * @param guidParameterName parameter name used to retrieve the entities
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of beans
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<B> getBeansForEntities(String              userId,
                                        List<EntityDetail>  entities,
                                        String              guidParameterName,
                                        boolean             forLineage,
                                        boolean             forDuplicateProcessing,
                                        Date                effectiveTime,
                                        String              methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = getBeanForEntity(userId,
                                              entity,
                                              guidParameterName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
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
     * Retrieve the API parameter lists metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the requested metadata element
     * @param apiOperationGUIDParameterName parameter name of the apiOperationGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of API parameter lists element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getAPIParameterListsForOperation(String  userId,
                                                    String  apiOperationGUID,
                                                    String  apiOperationGUIDParameterName,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String endTwoGUIDParameterName = "endTwo.guid";

        /*
         * The API parameter lists are attached via relationships.
         */
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   apiOperationGUID,
                                                                   apiOperationGUIDParameterName,
                                                                   OpenMetadataType.API_OPERATION.typeName,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   OpenMetadataType.API_PARAMETER_LIST.typeName,
                                                                   0,
                                                                   null,
                                                                   null,
                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
                                                                   methodName);

        if (relationships != null)
        {
            List<B> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy endTwo = relationship.getEntityTwoProxy();

                    if (repositoryHelper.isTypeOf(serviceName,
                                                  endTwo.getType().getTypeDefName(),
                                                  OpenMetadataType.SCHEMA_TYPE.typeName))
                    {
                        B bean = this.getAPIParameterListByGUID(userId,
                                                                endTwo.getGUID(),
                                                                endTwoGUIDParameterName,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
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
     * Retrieve the API parameter list metadata element with the supplied unique identifier.
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
    public B getAPIParameterListByGUID(String  userId,
                                       String  guid,
                                       String  guidParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           OpenMetadataType.SCHEMA_TYPE.typeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        return getBeanForEntity(userId, entity, guidParameterName, forLineage, forDuplicateProcessing,effectiveTime, methodName);
    }


    /**
     * Convert an entity into a bean
     *
     * @param userId calling user
     * @param entity entity to convert
     * @param guidParameterName parameter used to retrieve entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return bean
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private B getBeanForEntity(String       userId,
                               EntityDetail entity,
                               String       guidParameterName,
                               boolean      forLineage,
                               boolean      forDuplicateProcessing,
                               Date         effectiveTime,
                               String       methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        if (entity != null)
        {
            List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                       entity,
                                                                       guidParameterName,
                                                                       OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       OpenMetadataType.SCHEMA_ELEMENT.typeName,
                                                                       0,
                                                                       null,
                                                                       null,
                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       supportedZones,
                                                                       0,
                                                                       0,
                                                                       effectiveTime,
                                                                       methodName);

            return converter.getNewComplexBean(beanClass,
                                               entity,
                                               relationships,
                                               methodName);
        }

        return null;
    }
}
