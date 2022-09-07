/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * SchemaElementHandler manages common methods fof SchemaType and Schema Attribute objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities and relationships through the OMRSRepositoryConnector.
 * This handler does not support effectivity dates but probably should.
 */
class SchemaElementHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the asset handler with information needed to work with B objects.
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
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    SchemaElementHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create/retrieve the unique identifier of the SchemaType that sits between the asset and a top-level
     * schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller - if null a local element is created
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the database schema where the database table is located
     * @param assetGUIDParameterName name of the parameter of assetGUID
     * @param assetTypeName the name of the asset's type
     * @param schemaTypeTypeGUID unique identifier of the type
     * @param schemaTypeTypeName unique name of the type
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return properties of the anchor schema type for the database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String getAssetSchemaTypeGUID(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  assetGUID,
                                  String  assetGUIDParameterName,
                                  String  assetTypeName,
                                  String  schemaTypeTypeGUID,
                                  String  schemaTypeTypeName,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        /*
         * This method verifies the visibility of this asset to the calling user.  It also returns the qualified name of the
         * asset which is used to create the qualified name of the schema type if it does not exist.
         */
        String assetQualifiedName = this.getBeanStringPropertyFromRepository(userId,
                                                                             assetGUID,
                                                                             assetGUIDParameterName,
                                                                             assetTypeName,
                                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                             effectiveTime,
                                                                             methodName);

        if (assetQualifiedName == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        assetGUID,
                                                        assetTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
            /* unreachable */
            return null;
        }

        /*
         * Now retrieve the schema type attached to the asset
         */
        EntityDetail schemaTypeEntity = this.getAttachedEntity(userId,
                                                               assetGUID,
                                                               assetGUIDParameterName,
                                                               assetTypeName,
                                                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                               schemaTypeTypeName,
                                                               2,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               supportedZones,
                                                               effectiveTime,
                                                               methodName);

        if (schemaTypeEntity == null)
        {
            /*
             * The schema type does not exist so need to create one and link it ito the asset
             */
            String schemaTypeQualifiedName = "SchemaOf:" + assetQualifiedName;

            SchemaTypeBuilder builder = new SchemaTypeBuilder(schemaTypeQualifiedName,
                                                              schemaTypeTypeGUID,
                                                              schemaTypeTypeName,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

            builder.setAnchors(userId, assetGUID, methodName);

            String schemaTypeGUID = this.createBeanInRepository(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                schemaTypeTypeGUID,
                                                                schemaTypeTypeName,
                                                                builder,
                                                                effectiveTime,
                                                                methodName);

            final String schemaTypeGUIDParameterName = "schemaTypeGUID";

            if (schemaTypeGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          assetTypeName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }

            return schemaTypeGUID;
        }
        else
        {
            return schemaTypeEntity.getGUID();
        }
    }


    /**
     * Add additional schema types that are part of a schema type.  Specifically for maps and schema type choices.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaTypeGUID schema type entity to link to
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
     * @param schemaTypeTypeName name of schema type's type
     * @param schemaTypeBuilder properties of complete schema type
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void addEmbeddedTypes(String            userId,
                          String            externalSourceGUID,
                          String            externalSourceName,
                          String            schemaTypeGUID,
                          String            schemaTypeGUIDParameterName,
                          String            schemaTypeTypeName,
                          SchemaTypeBuilder schemaTypeBuilder,
                          Date              effectiveFrom,
                          Date              effectiveTo,
                          boolean           forLineage,
                          boolean           forDuplicateProcessing,
                          Date              effectiveTime,
                          String            methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String mapFromParameterName = "mapFromSchemaType";
        final String mapToParameterName = "mapToSchemaType";
        final String externalParameterName = "externalSchemaType";
        final String optionParameterName = "optionSchemaType";

        if (repositoryHelper.isTypeOf(serviceName, schemaTypeBuilder.getTypeName(), OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME))
        {
            String externalSchemaGUID = schemaTypeBuilder.getExternalSchemaTypeGUID();

            if (externalSchemaGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          externalSchemaGUID,
                                          externalParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaTypeBuilder.getTypeName(), OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
        {
            /*
             * The caller may have set up the maps as builders (requiring the schema type to be created first) or as GUIDs.
             */
            SchemaTypeBuilder mapFromBuilder = schemaTypeBuilder.getMapFrom();
            SchemaTypeBuilder mapToBuilder   = schemaTypeBuilder.getMapTo();
            String mapFromGUID = schemaTypeBuilder.getMapFromGUID();
            String mapToGUID = schemaTypeBuilder.getMapToGUID();

            if (mapFromBuilder != null)
            {
                mapFromGUID = addSchemaType(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            mapFromBuilder.qualifiedName,
                                            mapFromBuilder,
                                            effectiveFrom,
                                            effectiveTo,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }

            if (mapFromGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          mapFromGUID,
                                          mapFromParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }

            if (mapToBuilder != null)
            {
                mapToGUID = addSchemaType(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          mapToBuilder.qualifiedName,
                                          mapToBuilder,
                                          effectiveFrom,
                                          effectiveTo,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }

            if (mapToGUID != null)
            {
                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          schemaTypeTypeName,
                                          mapToGUID,
                                          mapToParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaTypeBuilder.getTypeName(), OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
        {
            List<SchemaTypeBuilder>  schemaOptionBuilders = schemaTypeBuilder.getSchemaOptions();

            if (schemaOptionBuilders != null)
            {
                for (SchemaTypeBuilder schemaOptionBuilder : schemaOptionBuilders)
                {
                    if (schemaOptionBuilder != null)
                    {
                        String optionGUID = addSchemaType(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          schemaOptionBuilder.qualifiedName,
                                                          schemaOptionBuilder,
                                                          effectiveFrom,
                                                          effectiveTo,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

                        if (optionGUID != null)
                        {
                            this.linkElementToElement(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      schemaTypeGUID,
                                                      schemaTypeGUIDParameterName,
                                                      schemaTypeTypeName,
                                                      optionGUID,
                                                      optionParameterName,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      supportedZones,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME,
                                                      null,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      effectiveTime,
                                                      methodName);
                        }
                    }
                }
            }
        }
    }


    /**
     * Store a new schema type (and optional attributes in the repository and return its unique identifier (GUID)).
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaTypeBuilder properties for new schemaType
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String  addSchemaType(String            userId,
                                  String            externalSourceGUID,
                                  String            externalSourceName,
                                  String            qualifiedName,
                                  SchemaTypeBuilder schemaTypeBuilder,
                                  Date              effectiveFrom,
                                  Date              effectiveTo,
                                  boolean           forLineage,
                                  boolean           forDuplicateProcessing,
                                  Date              effectiveTime,
                                  String            methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        String schemaTypeGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            schemaTypeBuilder.getTypeGUID(),
                                                            schemaTypeBuilder.getTypeName(),
                                                            schemaTypeBuilder,
                                                            effectiveTime,
                                                            methodName);

        addEmbeddedTypes(userId,
                         externalSourceGUID,
                         externalSourceName,
                         schemaTypeGUID,
                         schemaTypeGUIDParameterName,
                         OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                         schemaTypeBuilder,
                         effectiveFrom,
                         effectiveTo,
                         forLineage,
                         forDuplicateProcessing,
                         effectiveTime,
                         methodName);

        return schemaTypeGUID;
    }


    /**
     * Count the number of attributes attached to a parent schema attribute or schema type.
     *
     * @param userId     calling user
     * @param schemaElementGUID identifier for the parent complex schema type - this could be a schemaAttribute or a
     *                          schema type
     * @param guidParameterName name of guid parameter
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    int countSchemaAttributes(String userId,
                              String schemaElementGUID,
                              String guidParameterName,
                              Date   effectiveTime,
                              String methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, guidParameterName, methodName);

        int count = repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                       schemaElementGUID,
                                                                       OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                       2,
                                                                       false,
                                                                       false,
                                                                       effectiveTime,
                                                                       methodName);

        return count;
    }


    /**
     * Create a new query relationship for a derived schema element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the DBMS
     * @param externalSourceName unique name of software capability representing the DBMS
     * @param schemaElementGUID unique identifier of the schema element that this query supports
     * @param schemaElementGUIDParameterName  parameter name for schemaElementGUID
     * @param schemaElementTypeName name of type for schema element
     * @param queryId identifier for the query - used as a placeholder in the formula (stored in the column's CalculatedValue classification)
     * @param query the query that is made on the targetGUID
     * @param queryTargetGUID the unique identifier of the target (this is a schema element - typically a schema attribute)
     * @param queryTargetGUIDParameterName parameter supplying queryTargetGUID
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupQueryTargetRelationship(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  schemaElementGUID,
                                             String  schemaElementGUIDParameterName,
                                             String  schemaElementTypeName,
                                             String  queryId,
                                             String  query,
                                             String  queryTargetGUID,
                                             String  queryTargetGUIDParameterName,
                                             Date    effectiveFrom,
                                             Date    effectiveTo,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String queryParameterName = "query";

        invalidParameterHandler.validateObject(query, queryParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.QUERY_PROPERTY_NAME,
                                                                                     query,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.QUERY_ID_PROPERTY_NAME,
                                                                  queryId,
                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  schemaElementGUID,
                                  schemaElementGUIDParameterName,
                                  schemaElementTypeName,
                                  queryTargetGUID,
                                  queryTargetGUIDParameterName,
                                  OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Update the query properties for a query relationship for a derived schema element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the DBMS
     * @param externalSourceName unique name of software capability representing the DBMS
     * @param schemaElementGUID unique identifier of the schema element that this query supports
     * @param schemaElementGUIDParameterName  parameter name for schemaElementGUID
     * @param schemaElementTypeName name of type for schema element
     * @param queryId identifier for the query - used as a placeholder in the formula (stored in the column's CalculatedValue classification)
     * @param query the query that is made on the targetGUID
     * @param queryTargetGUID the unique identifier of the target (this is a schema element - typically a schema attribute)
     * @param queryTargetGUIDParameterName parameter supplying queryTargetGUID
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateQueryTargetRelationship(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  schemaElementGUID,
                                              String  schemaElementGUIDParameterName,
                                              String  schemaElementTypeName,
                                              String  queryId,
                                              String  query,
                                              String  queryTargetGUID,
                                              String  queryTargetGUIDParameterName,
                                              Date    effectiveFrom,
                                              Date    effectiveTo,
                                              boolean isMergeUpdate,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String queryParameterName = "query";

        invalidParameterHandler.validateObject(query, queryParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.QUERY_PROPERTY_NAME,
                                                                                     query,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.QUERY_ID_PROPERTY_NAME,
                                                                  queryId,
                                                                  methodName);

        this.updateElementToElementLink(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        schemaElementGUID,
                                        schemaElementGUIDParameterName,
                                        schemaElementTypeName,
                                        queryTargetGUID,
                                        queryTargetGUIDParameterName,
                                        OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME,
                                        isMergeUpdate,
                                        this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                        effectiveTime,
                                        methodName);
    }



    /**
     * Update the query properties for a query relationship for a derived schema element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the DBMS
     * @param externalSourceName unique name of software capability representing the DBMS
     * @param schemaElementGUID unique identifier of the schema element that this query supports
     * @param schemaElementGUIDParameterName  parameter name for schemaElementGUID
     * @param schemaElementTypeName name of type for schema element
     * @param queryTargetGUID the unique identifier of the target (this is a schema element - typically a schema attribute)
     * @param queryTargetGUIDParameterName parameter supplying queryTargetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearQueryTargetRelationship(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String schemaElementGUID,
                                             String schemaElementGUIDParameterName,
                                             String schemaElementTypeName,
                                             String queryTargetGUID,
                                             String queryTargetGUIDParameterName,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      schemaElementGUID,
                                      schemaElementGUIDParameterName,
                                      schemaElementTypeName,
                                      queryTargetGUID,
                                      queryTargetGUIDParameterName,
                                      OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_GUID,
                                      OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                      false,
                                      false,
                                      OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME,
                                      null,
                                      methodName);
    }


}
