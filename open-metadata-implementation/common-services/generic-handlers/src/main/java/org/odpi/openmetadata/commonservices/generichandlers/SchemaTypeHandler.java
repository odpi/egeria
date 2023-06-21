/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SchemaTypeHandler manages SchemaType objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities through the OMRSRepositoryConnector.
 * This handler does not support effectivity dates but probably should.
 */
public class SchemaTypeHandler<B> extends SchemaElementHandler<B>
{
    private final OpenMetadataAPIGenericConverter<B> schemaTypeConverter;

    /**
     * Construct the handler with information needed to work with B objects.
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
    public SchemaTypeHandler(OpenMetadataAPIGenericConverter<B>       converter,
                             Class<B>                                 beanClass,
                             String                                   serviceName,
                             String                                   serverName,
                             InvalidParameterHandler                  invalidParameterHandler,
                             RepositoryHandler                        repositoryHandler,
                             OMRSRepositoryHelper                     repositoryHelper,
                             String                                   localServerUserId,
                             OpenMetadataServerSecurityVerifier       securityVerifier,
                             List<String>                             supportedZones,
                             List<String>                             defaultZones,
                             List<String>                             publishZones,
                             AuditLog                                 auditLog)
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

        /*
         * Schema types need their own specialized converter because the schema type may be represented by multiple entities.
         */
        this.schemaTypeConverter = converter;
    }


    /**
     * Store a new schema type (and optional attributes) in the repository and return its unique identifier (GUID).
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaTypeBuilder properties for new schemaType
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  addSchemaType(String            userId,
                                 String            externalSourceGUID,
                                 String            externalSourceName,
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
                         schemaTypeBuilder.getTypeName(),
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
     * Create a schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addSchemaType(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
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
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
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

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a primitive schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param dataType string name
     * @param defaultValue string value
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addPrimitiveSchemaType(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         String              versionNumber,
                                         boolean             isDeprecated,
                                         String              author,
                                         String              usage,
                                         String              encodingStandard,
                                         String              namespace,
                                         String              dataType,
                                         String              defaultValue,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
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

        builder.setDataType(dataType);
        builder.setDefaultValue(defaultValue);
        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a literal schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param dataType string name
     * @param fixedValue string value
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addLiteralSchemaType(String              userId,
                                       String              externalSourceGUID,
                                       String              externalSourceName,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       String              versionNumber,
                                       boolean             isDeprecated,
                                       String              author,
                                       String              usage,
                                       String              encodingStandard,
                                       String              namespace,
                                       String              dataType,
                                       String              fixedValue,
                                       Map<String, String> additionalProperties,
                                       String              suppliedTypeName,
                                       Map<String, Object> extendedProperties,
                                       Date                effectiveFrom,
                                       Date                effectiveTo,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME,
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

        builder.setDataType(dataType);
        builder.setFixedValue(fixedValue);
        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }



    /**
     * Create an enum schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param dataType string name
     * @param defaultValue string value
     * @param validValuesSetGUID unique identifier of the valid values set to used
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addEnumSchemaType(String              userId,
                                    String              externalSourceGUID,
                                    String              externalSourceName,
                                    String              qualifiedName,
                                    String              displayName,
                                    String              description,
                                    String              versionNumber,
                                    boolean             isDeprecated,
                                    String              author,
                                    String              usage,
                                    String              encodingStandard,
                                    String              namespace,
                                    String              dataType,
                                    String              defaultValue,
                                    String              validValuesSetGUID,
                                    Map<String, String> additionalProperties,
                                    String              suppliedTypeName,
                                    Map<String, Object> extendedProperties,
                                    Date                effectiveFrom,
                                    Date                effectiveTo,
                                    boolean             forLineage,
                                    boolean             forDuplicateProcessing,
                                    Date                effectiveTime,
                                    String              methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.ENUM_SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.ENUM_SCHEMA_TYPE_TYPE_NAME,
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

        builder.setDataType(dataType);
        builder.setDefaultValue(defaultValue);
        builder.setValidValuesSetGUID(validValuesSetGUID);
        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a struct schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addStructSchemaType(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
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
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.STRUCT_SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.STRUCT_SCHEMA_TYPE_TYPE_NAME,
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

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a schema type choice.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addSchemaTypeChoice(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
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
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME,
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

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a map schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the schemaType in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addMapSchemaType(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              versionNumber,
                                   boolean             isDeprecated,
                                   String              author,
                                   String              usage,
                                   String              encodingStandard,
                                   String              namespace,
                                   String              mapFromSchemaTypeGUID,
                                   String              mapToSchemaTypeGUID,
                                   Map<String, String> additionalProperties,
                                   String              suppliedTypeName,
                                   Map<String, Object> extendedProperties,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   boolean             forLineage,
                                   boolean             forDuplicateProcessing,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String fromGUIDParameterName       = "mapFromSchemaTypeGUID";
        final String toGUIDParameterName         = "mapToSchemaTypeGUID";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateGUID(mapFromSchemaTypeGUID, fromGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(mapToSchemaTypeGUID, toGUIDParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME,
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

        builder.setMapGUIDs(mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return addSchemaType(userId,
                             externalSourceGUID,
                             externalSourceName,
                             builder,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema type.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the schema type - used in other configuration
     * @param displayName short display name for the schema type
     * @param description description of the schema type
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String templateGUID,
                                               String qualifiedName,
                                               String displayName,
                                               String description,
                                               String methodName) throws InvalidParameterException,
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

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           supportedZones,
                                           methodName);
    }


    /**
     * Update a stored schemaType.  Note - this only updates the main schema - it does not travel
     * through the nested schemas.
     *
     * @param userId                 userId
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param existingSchemaTypeGUID unique identifier of the existing schemaType entity
     * @param existingSchemaTypeGUIDParameterName name of parameter for existingSchemaTypeGUID
     * @param builder                new schemaType values
     * @param isMergeUpdate          should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName             calling method
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void   updateSchemaType(String            userId,
                                   String            externalSourceGUID,
                                   String            externalSourceName,
                                   String            existingSchemaTypeGUID,
                                   String            existingSchemaTypeGUIDParameterName,
                                   SchemaTypeBuilder builder,
                                   boolean           isMergeUpdate,
                                   boolean           forLineage,
                                   boolean           forDuplicateProcessing,
                                   Date              effectiveTime,
                                   String            methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        InstanceProperties properties = builder.getInstanceProperties(methodName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    existingSchemaTypeGUID,
                                    existingSchemaTypeGUIDParameterName,
                                    this.getSchemaTypeTypeGUID(builder),
                                    this.getSchemaTypeTypeName(builder),
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    properties,
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Create a schema type.
     *
     * @param userId calling userId
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties
     * @param suppliedTypeName unique name of schema sub type
     * @param extendedProperties  properties from the subtype.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updateSchemaType(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              schemaTypeGUID,
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
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String qualifiedNameParameterName  = "qualifiedName";
        final String elementGUIDParameterName    = "schemaTypeGUID";

        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
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

        updateSchemaType(userId,
                         externalSourceGUID,
                         externalSourceName,
                         schemaTypeGUID,
                         elementGUIDParameterName,
                         builder,
                         isMergeUpdate,
                         forLineage,
                         forDuplicateProcessing,
                         effectiveTime,
                         methodName);
    }

    /**
     * Return the type guid contained in this builder bean.
     *
     * @param builder bean to interrogate
     * @return guid of the type
     */
    private String getSchemaTypeTypeGUID(SchemaTypeBuilder   builder)
    {
        if (builder.getTypeGUID() != null)
        {
            return builder.getTypeGUID();
        }
        else
        {
            return OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID;
        }
    }


    /**
     * Return the type name contained in this builder bean.
     *
     * @param builder bean to interrogate
     * @return name of the type
     */
    private String getSchemaTypeTypeName(SchemaTypeBuilder   builder)
    {
        if (builder.getTypeName() != null)
        {
            return builder.getTypeName();
        }
        else
        {
            return OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
        }
    }


    /**
     * Remove the requested schemaType if it is no longer connected to any other entity.
     *
     * @param userId       calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller - null for local cohort
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaTypeGUID object to delete
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeSchemaType(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  schemaTypeGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String guidParameterName = "schemaTypeGUID";

        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaTypeGUID,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Is there an attached schema for the Asset?
     *
     * @param userId     calling user
     * @param assetGUID identifier for the entity that the object is attached to
     * @param assetGUIDParameterName name of parameter for assetGUID
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaTypeForAsset(String   userId,
                                   String   assetGUID,
                                   String   assetGUIDParameterName,
                                   boolean  forLineage,
                                   boolean  forDuplicateProcessing,
                                   Date     effectiveTime,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getSchemaTypeForParent(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Is there an attached schema for the Port?
     *
     * @param userId     calling user
     * @param portGUID identifier for the entity that the object is attached to
     * @param portGUIDParameterName name of parameter for portGUID
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaTypeForPort(String   userId,
                                  String   portGUID,
                                  String   portGUIDParameterName,
                                  boolean  forLineage,
                                  boolean  forDuplicateProcessing,
                                  Date     effectiveTime,
                                  String   methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.getSchemaTypeForParent(userId,
                                           portGUID,
                                           portGUIDParameterName,
                                           OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                           OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                           OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Is there an attached schema for the parent entity?  This is either an asset or a port entity.  This method
     * should not be used to get the schema type for a schema attribute.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the object is attached to
     * @param parentGUIDParameterName parameter supplying parentGUID
     * @param parentTypeName type name of anchor
     * @param relationshipTypeGUID unique identifier of the relationship type to search along
     * @param relationshipTypeName unique name of the relationship type to search along
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schemaType object or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaTypeForParent(String   userId,
                                    String   parentGUID,
                                    String   parentGUIDParameterName,
                                    String   parentTypeName,
                                    String   relationshipTypeGUID,
                                    String   relationshipTypeName,
                                    boolean  forLineage,
                                    boolean  forDuplicateProcessing,
                                    Date     effectiveTime,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = this.getAttachedEntity(userId,
                                                                parentGUID,
                                                                parentGUIDParameterName,
                                                                parentTypeName,
                                                                relationshipTypeGUID,
                                                                relationshipTypeName,
                                                                OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                2,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
                                                                methodName);

        return getSchemaTypeFromEntity(userId,
                                       schemaTypeEntity,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }

    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaTypeParent(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        if (repositoryHelper.isTypeOf(serviceName, parentElementTypeName, OpenMetadataAPIMapper.PORT_TYPE_NAME))
        {
            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      parentElementGUID,
                                      parentElementGUIDParameterName,
                                      parentElementTypeName,
                                      schemaTypeGUID,
                                      schemaTypeGUIDParameterName,
                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                      null,
                                      effectiveFrom,
                                      effectiveTo,
                                      effectiveTime,
                                      methodName);
        }
        else
        {
            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      parentElementGUID,
                                      parentElementGUIDParameterName,
                                      parentElementTypeName,
                                      schemaTypeGUID,
                                      schemaTypeGUIDParameterName,
                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                      OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                      null,
                                      effectiveFrom,
                                      effectiveTo,
                                      effectiveTime,
                                      methodName);
        }
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        if ((parentElementTypeName != null) && repositoryHelper.isTypeOf(serviceName,
                                                                         parentElementTypeName,
                                                                         OpenMetadataAPIMapper.PORT_TYPE_NAME))
        {
            this.unlinkElementFromElement(userId,
                                          false,
                                          externalSourceGUID,
                                          externalSourceName,
                                          parentElementGUID,
                                          parentElementGUIDParameterName,
                                          parentElementTypeName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                          OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                          effectiveTime,
                                          methodName);
        }
        else
        {
            this.unlinkElementFromElement(userId,
                                          false,
                                          externalSourceGUID,
                                          externalSourceName,
                                          parentElementGUID,
                                          parentElementGUIDParameterName,
                                          parentElementTypeName,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                          effectiveTime,
                                          methodName);
        }
    }

    /**
     * Return the list of schema types nested in the parent schema type - this is typically schema type options or APIOperations in a APISchemaType.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the object is attached to
     * @param parentGUIDParameterName parameter supplying parentGUID
     * @param parentTypeName type name of anchor
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param startingFrom              paging start point
     * @param pageSize                  maximum results that can be returned
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of schemaType objects or null
     *
     * @throws InvalidParameterException  the schemaType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getNestedSchemaTypesForParent(String       userId,
                                                 String       parentGUID,
                                                 String       parentGUIDParameterName,
                                                 String       parentTypeName,
                                                 List<String> serviceSupportedZones,
                                                 boolean      forLineage,
                                                 boolean      forDuplicateProcessing,
                                                 int          startingFrom,
                                                 int          pageSize,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                 parentGUID,
                                                                 parentGUIDParameterName,
                                                                 parentTypeName,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 serviceSupportedZones,
                                                                 effectiveTime,
                                                                 methodName);

        if (parentEntity != null)
        {
            List<EntityDetail> schemaTypeEntities;

            if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.API_SCHEMA_TYPE_TYPE_NAME))
            {
                schemaTypeEntities = this.getAttachedEntities(userId,
                                                              parentEntity,
                                                              parentGUIDParameterName,
                                                              parentTypeName,
                                                              OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                                              OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                              null,
                                                              null,
                                                              2,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              startingFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              methodName);
            }
            else
            {
                schemaTypeEntities = this.getAttachedEntities(userId,
                                                              parentEntity,
                                                              parentGUIDParameterName,
                                                              parentTypeName,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                              OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                              null,
                                                              null,
                                                              2,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              startingFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              methodName);
            }

            if (schemaTypeEntities != null)
            {
                List<B> results = new ArrayList<>();

                for (EntityDetail schemaTypeEntity : schemaTypeEntities)
                {
                    B schemaTypeBean = getSchemaTypeFromEntity(userId,
                                                               schemaTypeEntity,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

                    results.add(schemaTypeBean);
                }

                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve a specific schema type based on its unique identifier (GUID).  This is used to do updates
     * and to retrieve a linked schema.
     *
     * @param userId calling user
     * @param schemaTypeGUID guid of schema type to retrieve.
     * @param guidParameterName parameter describing where the guid came from
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schema type or null depending on whether the object is found
     * @throws InvalidParameterException  the guid is null
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public B getSchemaType(String   userId,
                           String   schemaTypeGUID,
                           String   guidParameterName,
                           boolean  forLineage,
                           boolean  forDuplicateProcessing,
                           Date     effectiveTime,
                           String   methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        EntityDetail  schemaTypeEntity = this.getEntityFromRepository(userId,
                                                                      schemaTypeGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                      null,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      effectiveTime,
                                                                      methodName);

        return getSchemaTypeFromEntity(userId,
                                       schemaTypeEntity,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param suppliedTypeName optional type name for the schema type - used to restrict the search results
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findSchemaTypes(String  userId,
                                   String  suppliedTypeName,
                                   String  searchString,
                                   int     startFrom,
                                   int     pageSize,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        typeGUID,
                                                        typeName,
                                                        null,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        return getSchemaTypesFromEntities(userId,
                                          entities,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param suppliedTypeName optional type name for the schema type - used to restrict the search results
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getSchemaTypeByName(String  userId,
                                       String  suppliedTypeName,
                                       String  name,
                                       int     startFrom,
                                       int     pageSize,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String nameParameterName = "name";

        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);


        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        List<EntityDetail> entities = this.getEntitiesByValue(userId,
                                                              name,
                                                              nameParameterName,
                                                              typeGUID,
                                                              typeName,
                                                              specificMatchPropertyNames,
                                                              true,
                                                              false,
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

        return getSchemaTypesFromEntities(userId,
                                          entities,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime, methodName);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
    */
    public B getSchemaTypeForElement(String  userId,
                                     String  parentElementGUID,
                                     String  parentElementTypeName,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeEnd.getGUID";

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                      parentElementGUID,
                                                                      parentElementGUIDParameterName,
                                                                      parentElementTypeName,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      0,
                                                                      0,
                                                                      effectiveTime,
                                                                      methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.SOLUTION_PORT_SCHEMA_RELATIONSHIP_TYPE_NAME))
                {
                    return this.getSchemaType(userId, relationship.getEntityTwoProxy().getGUID(), schemaTypeGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
                }
            }
        }

        return null;
    }


    /**
     * Transform a list of schema type entities into a list of schema type beans.  To completely fill out each schema type
     * it may be necessary to retrieve additional entities.  For example, a map schema type includes links
     * to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaTypeEntities list of entities retrieved from the repositories
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     *
     * @throws InvalidParameterException  problem with an entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private List<B> getSchemaTypesFromEntities(String             userId,
                                               List<EntityDetail> schemaTypeEntities,
                                               boolean            forLineage,
                                               boolean            forDuplicateProcessing,
                                               Date               effectiveTime,
                                               String             methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        List<B> results = new ArrayList<>();

        if (schemaTypeEntities != null)
        {
            for (EntityDetail entity : schemaTypeEntities)
            {
                if (entity != null)
                {
                    results.add(this.getSchemaTypeFromEntity(userId,
                                                             entity,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName));
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
     * Transform a schema type entity into a schema type bean.  To completely fill out the schema type
     * it may be necessary to retrieve additional entities.  For example, a map schema type includes links
     * to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaTypeEntity entity retrieved from the repository
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schema type bean
     *
     * @throws InvalidParameterException  problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private B getSchemaTypeFromEntity(String       userId,
                                      EntityDetail schemaTypeEntity,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        if ((schemaTypeEntity != null) && (schemaTypeEntity.getType() != null))
        {
            return getSchemaTypeFromInstance(userId,
                                             schemaTypeEntity,
                                             schemaTypeEntity.getType().getTypeDefName(),
                                             schemaTypeEntity.getProperties(),
                                             schemaTypeEntity.getClassifications(),
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
        }

        return null;
    }


    /**
     * Transform the schema type information stored either as a schema type entity or in the TypeClassifiedAttribute classification into a schema type
     * bean.  To completely fill out the schema type it may be necessary to retrieve additional entities.  For example, a map schema type includes
     * links to the two types that are being mapped together.
     *
     * @param userId calling user
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaRootTypeName name of type of the schema element that holds the root information
     * @param instanceProperties properties describing the schema type
     * @param entityClassifications classifications from the root entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return schema type bean
     * @throws InvalidParameterException  problem with the entity
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public  B getSchemaTypeFromInstance(String               userId,
                                        InstanceHeader       schemaRootHeader,
                                        String               schemaRootTypeName,
                                        InstanceProperties   instanceProperties,
                                        List<Classification> entityClassifications,
                                        boolean              forLineage,
                                        boolean              forDuplicateProcessing,
                                        Date                 effectiveTime,
                                        String               methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String schemaGUIDParameterName = "schemaRootGUID";

        int          attributeCount         = 0;
        String       validValuesSetGUID     = null;
        String       externalSchemaTypeGUID = null;
        B            externalSchemaType     = null;
        String       mapToSchemaTypeGUID    = null;
        B            mapToSchemaType        = null;
        String       mapFromSchemaTypeGUID  = null;
        B            mapFromSchemaType      = null;
        List<String> schemaTypeOptionGUIDs  = null;
        List<B>      schemaTypeOptions      = null;

        /*
         * Look for an external schema type - the real content will be hanging off of this element.
         */
        if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail externalSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                           schemaRootHeader.getGUID(),
                                                                           schemaGUIDParameterName,
                                                                           OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                           2,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           supportedZones,
                                                                           effectiveTime,
                                                                           methodName);

            if (externalSchemaTypeEntity != null)
            {
                externalSchemaTypeGUID = externalSchemaTypeEntity.getGUID();
                externalSchemaType = this.getSchemaTypeFromEntity(userId,
                                                                  externalSchemaTypeEntity,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);
            }
        }

        /*
         * Collect up the interesting information about the schema type that is outside the entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
        {
            attributeCount = this.countSchemaAttributes(userId,
                                                        schemaRootHeader.getGUID(),
                                                        schemaGUIDParameterName,
                                                        effectiveTime,
                                                        methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.ENUM_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail validValuesSetEntity = this.getAttachedEntity(userId,
                                                                       schemaRootHeader.getGUID(),
                                                                       schemaGUIDParameterName,
                                                                       OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                                                       2,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       supportedZones,
                                                                       effectiveTime,
                                                                       methodName);

            if (validValuesSetEntity != null)
            {
                validValuesSetGUID = validValuesSetEntity.getGUID();
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
        {
            EntityDetail mapFromSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                          schemaRootHeader.getGUID(),
                                                                          schemaGUIDParameterName,
                                                                          OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                          2,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          supportedZones,
                                                                          effectiveTime,
                                                                          methodName);

            if (mapFromSchemaTypeEntity != null)
            {
                mapFromSchemaTypeGUID = mapFromSchemaTypeEntity.getGUID();
                mapFromSchemaType = this.getSchemaTypeFromEntity(userId,
                                                                 mapFromSchemaTypeEntity,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
            }

            EntityDetail mapToSchemaTypeEntity = this.getAttachedEntity(userId,
                                                                        schemaRootHeader.getGUID(),
                                                                        schemaGUIDParameterName,
                                                                        OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                        2,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        supportedZones,
                                                                        effectiveTime,
                                                                        methodName);

            if (mapToSchemaTypeEntity != null)
            {
                mapToSchemaTypeGUID = mapToSchemaTypeEntity.getGUID();
                mapToSchemaType = this.getSchemaTypeFromEntity(userId,
                                                               mapToSchemaTypeEntity,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, schemaRootTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
        {
            List<EntityDetail> schemaTypeOptionsEntities = getAttachedEntities(userId,
                                                                               schemaRootHeader.getGUID(),
                                                                               schemaGUIDParameterName,
                                                                               OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                                               OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               2,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               supportedZones,
                                                                               0,
                                                                               invalidParameterHandler.getMaxPagingSize(),
                                                                               effectiveTime,
                                                                               methodName);

            if ((schemaTypeOptionsEntities != null) && (! schemaTypeOptionsEntities.isEmpty()))
            {
                schemaTypeOptionGUIDs = new ArrayList<>();
                schemaTypeOptions = new ArrayList<>();

                for (EntityDetail schemaTypeOptionEntity : schemaTypeOptionsEntities)
                {
                    if (schemaTypeOptionEntity != null)
                    {
                        schemaTypeOptionGUIDs.add(schemaTypeOptionEntity.getGUID());
                        schemaTypeOptions.add(this.getSchemaTypeFromEntity(userId,
                                                                           schemaTypeOptionEntity,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName));
                    }
                }
            }
        }

        List<Relationship> queryTargets = this.getAttachmentLinks(userId,
                                                                  schemaRootHeader.getGUID(),
                                                                  schemaGUIDParameterName,
                                                                  OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                  null,
                                                                  OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                  2,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  supportedZones,
                                                                  0,
                                                                  0,
                                                                  effectiveTime,
                                                                  methodName);


        return schemaTypeConverter.getNewSchemaTypeBean(beanClass,
                                                        schemaRootHeader,
                                                        schemaRootTypeName,
                                                        instanceProperties,
                                                        entityClassifications,
                                                        attributeCount,
                                                        validValuesSetGUID,
                                                        externalSchemaTypeGUID,
                                                        externalSchemaType,
                                                        mapFromSchemaTypeGUID,
                                                        mapFromSchemaType,
                                                        mapToSchemaTypeGUID,
                                                        mapToSchemaType,
                                                        schemaTypeOptionGUIDs,
                                                        schemaTypeOptions,
                                                        queryTargets,
                                                        methodName);
    }
}
