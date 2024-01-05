/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeHandler manages Schema Attribute objects.  It runs server-side in
 * the OMAG Server Platform and retrieves SchemaElement entities through the OMRSRepositoryConnector.
 * This handler does not support effectivity dates but probably should.
 */
public class SchemaAttributeHandler<SCHEMA_ATTRIBUTE, SCHEMA_TYPE> extends SchemaElementHandler<SCHEMA_ATTRIBUTE>
{
    private final OpenMetadataAPIGenericConverter<SCHEMA_ATTRIBUTE> schemaAttributeConverter;
    private final Class<SCHEMA_TYPE>                                schemaTypeBeanClass;
    private final SchemaTypeHandler<SCHEMA_TYPE>                    schemaTypeHandler;


    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param schemaAttributeConverter specific converter for the SCHEMA_ATTRIBUTE bean class
     * @param schemaAttributeBeanClass name of bean class that is represented by the generic class SCHEMA_ATTRIBUTE
     * @param schemaTypeConverter specific converter for the SCHEMA_TYPE bean class
     * @param schemaTypeBeanClass name of bean class that is represented by the generic class SCHEMA_TYPE
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
    public SchemaAttributeHandler(OpenMetadataAPIGenericConverter<SCHEMA_ATTRIBUTE> schemaAttributeConverter,
                                  Class<SCHEMA_ATTRIBUTE>                                 schemaAttributeBeanClass,
                                  OpenMetadataAPIGenericConverter<SCHEMA_TYPE> schemaTypeConverter,
                                  Class<SCHEMA_TYPE>                                      schemaTypeBeanClass,
                                  String                                                  serviceName,
                                  String                                                  serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  RepositoryHandler repositoryHandler,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String                                                  localServerUserId,
                                  OpenMetadataServerSecurityVerifier securityVerifier,
                                  List<String>                                            supportedZones,
                                  List<String>                                            defaultZones,
                                  List<String>                                            publishZones,
                                  AuditLog auditLog)
    {
        super(schemaAttributeConverter,
              schemaAttributeBeanClass,
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
         * Schema Attributes need their own specialized converter because the schema type may be represented by multiple entities.
         */
        this.schemaAttributeConverter = schemaAttributeConverter;
        this.schemaTypeBeanClass      = schemaTypeBeanClass;
        this.schemaTypeHandler        = new SchemaTypeHandler<>(schemaTypeConverter,
                                                                schemaTypeBeanClass,
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
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param parentElementGUID  element to connect this schema attribute to
     * @param parentElementGUIDParameterName parameter supplying parentElementGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the schema attribute - used in other configuration
     * @param displayName short display name for the schema attribute
     * @param description description of the schema attribute
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String  userId,
                                                    String  externalSourceGUID,
                                                    String  externalSourceName,
                                                    String  parentElementGUID,
                                                    String  parentElementGUIDParameterName,
                                                    String  templateGUID,
                                                    String  qualifiedName,
                                                    String  displayName,
                                                    String  description,
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
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        SchemaAttributeBuilder builder = new SchemaAttributeBuilder(qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        String schemaAttributeGUID = this.createBeanFromTemplate(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 templateGUID,
                                                                 templateGUIDParameterName,
                                                                 OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                                 OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                 qualifiedName,
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                 builder,
                                                                 supportedZones,
                                                                 methodName);

        if (schemaAttributeGUID != null)
        {
            final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

            EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                     parentElementGUID,
                                                                     parentElementGUIDParameterName,
                                                                     OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     supportedZones,
                                                                     effectiveTime,
                                                                     methodName);

            String parentElementTypeName = OpenMetadataType.SCHEMA_TYPE_TYPE_NAME;
            String parentElementRelationshipTypeGUID = OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
            String parentElementRelationshipTypeName = OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

            if ((parentEntity != null) && (parentEntity.getType() != null))
            {
                if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME))
                {
                    parentElementTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;
                    parentElementRelationshipTypeGUID = OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
                    parentElementRelationshipTypeName = OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;
                }
            }

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               parentElementGUID,
                                               parentElementGUIDParameterName,
                                               parentElementTypeName,
                                               schemaAttributeGUID,
                                               schemaAttributeGUIDParameterName,
                                               builder.getTypeName(),
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               parentElementRelationshipTypeGUID,
                                               parentElementRelationshipTypeName,
                                               null,
                                               effectiveTime,
                                               methodName);
        }

        return schemaAttributeGUID;
    }


    /**
     * Create a new schema attribute with its type attached.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param parentElementGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param parentElementGUIDParameterName parameter name supplying parentElementGUID
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param schemaAttributeBuilder builder containing the properties of the schema type
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new schema attribute already linked to its parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNestedSchemaAttribute(String                 userId,
                                              String                 externalSourceGUID,
                                              String                 externalSourceName,
                                              String                 parentElementGUID,
                                              String                 parentElementGUIDParameterName,
                                              String                 qualifiedName,
                                              String                 qualifiedNameParameterName,
                                              SchemaAttributeBuilder schemaAttributeBuilder,
                                              Date                   effectiveFrom,
                                              Date                   effectiveTo,
                                              boolean                forLineage,
                                              boolean                forDuplicateProcessing,
                                              Date                   effectiveTime,
                                              String                 methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                 parentElementGUID,
                                                                 parentElementGUIDParameterName,
                                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 supportedZones,
                                                                 effectiveTime,
                                                                 methodName);

        String parentAttributeTypeName             = OpenMetadataType.COMPLEX_SCHEMA_TYPE_TYPE_NAME;
        String parentAttributeRelationshipTypeGUID = OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
        String parentAttributeRelationshipTypeName = OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

        if ((parentEntity != null) && (parentEntity.getType() != null))
        {
            if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME))
            {
                parentAttributeTypeName             = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;
                parentAttributeRelationshipTypeGUID = OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
                parentAttributeRelationshipTypeName = OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;
            }

            /*
             * If the parent is set up with an anchor then this is propagated to the schema attribute
             */
            AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(parentEntity, methodName);
            if (anchorIdentifiers != null)
            {
                schemaAttributeBuilder.setAnchors(userId, anchorIdentifiers.anchorGUID, anchorIdentifiers.anchorTypeName, methodName);
            }
            SchemaTypeBuilder schemaTypeBuilder = schemaAttributeBuilder.getSchemaTypeBuilder();

            /*
             * if there is a formula then set it into the schemaAttributeBuilder
             */
            if (schemaTypeBuilder != null && schemaTypeBuilder.isDerived())
            {
                String sourceName = "local";
                if (externalSourceName != null && externalSourceName.length() >0)
                {
                    sourceName = externalSourceName;
                }
                InstanceProperties instanceProperties = schemaTypeBuilder.getCalculatedValueProperties(methodName);
                String formula = repositoryHelper.getStringProperty(sourceName, OpenMetadataProperty.FORMULA.name, instanceProperties, methodName);

                schemaAttributeBuilder.setCalculatedValue(userId, externalSourceGUID, externalSourceName, formula, methodName);
            }

            return this.createNestedSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    parentElementGUID,
                                                    parentElementGUIDParameterName,
                                                    parentAttributeTypeName,
                                                    parentAttributeRelationshipTypeGUID,
                                                    parentAttributeRelationshipTypeName,
                                                    qualifiedName,
                                                    qualifiedNameParameterName,
                                                    schemaAttributeBuilder,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
        else
        {
            /*
             * Either the entity has not been returned or it has no type info in it
             */
            invalidParameterHandler.throwUnknownElement(userId,
                                                        parentElementGUID,
                                                        OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }

        return null;
    }


    /**
     * Create a new schema attribute with its type attached that is nested inside a complex schema type or a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param parentElementGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param parentElementGUIDParameterName parameter name supplying parentElementGUID
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param displayName the stored display name property for the attribute
     * @param description the stored description property associated with the attribute
     * @param externalSchemaTypeGUID unique identifier of a schema Type that provides the type. If null, a private schema type is used
     * @param dataType data type name - for stored values
     * @param defaultValue string containing default value - for stored values
     * @param fixedValue string containing a fixed value - for a literal
     * @param validValuesSetGUID unique identifier of a valid value set that lists the valid values for this schema
     * @param formula String formula - for derived values
     * @param isDeprecated is this table deprecated?
     * @param elementPosition the position of this attribute in its parent type.
     * @param minCardinality minimum number of repeating instances allowed for this attribute - typically 1
     * @param maxCardinality the maximum number of repeating instances allowed for this column - typically 1
     * @param allowsDuplicateValues  whether the same value can be used by more than one instance of this attribute
     * @param orderedValues whether the attribute instances are arranged in an order
     * @param sortOrder the order that the attribute instances are arranged in - if any
     * @param minimumLength the minimum length of the data
     * @param length the length of the data field
     * @param significantDigits number of significant digits to the right of decimal point
     * @param isNullable whether the field is nullable or not
     * @param nativeJavaClass equivalent Java class implementation
     * @param defaultValueOverride default value for this column
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new schema attribute already linked to its parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNestedSchemaAttribute(String               userId,
                                              String               externalSourceGUID,
                                              String               externalSourceName,
                                              String               parentElementGUID,
                                              String               parentElementGUIDParameterName,
                                              String               qualifiedName,
                                              String               qualifiedNameParameterName,
                                              String               displayName,
                                              String               description,
                                              String               externalSchemaTypeGUID,
                                              String               dataType,
                                              String               defaultValue,
                                              String               fixedValue,
                                              String               validValuesSetGUID,
                                              String               formula,
                                              boolean              isDeprecated,
                                              int                  elementPosition,
                                              int                  minCardinality,
                                              int                  maxCardinality,
                                              boolean              allowsDuplicateValues,
                                              boolean              orderedValues,
                                              String               defaultValueOverride,
                                              int                  sortOrder,
                                              int                  minimumLength,
                                              int                  length,
                                              int                  significantDigits,
                                              boolean              isNullable,
                                              String               nativeJavaClass,
                                              List<String>         aliases,
                                              Map<String, String>  additionalProperties,
                                              String               typeName,
                                              Map<String, Object>  extendedProperties,
                                              Date                 effectiveFrom,
                                              Date                 effectiveTo,
                                              boolean              forLineage,
                                              boolean              forDuplicateProcessing,
                                              Date                 effectiveTime,
                                              String               methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * Check that the type name requested for the schema attribute is valid.
         */
        String schemaAttributeTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;
        String schemaAttributeTypeId   = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
            schemaAttributeTypeId = invalidParameterHandler.validateTypeName(typeName,
                                                                             OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                             serviceName,
                                                                             methodName,
                                                                             repositoryHelper);
        }

        /*
         * Load up the builder objects.  The builders manage the properties of the metadata elements that make up the schema attribute,
         * and the schemaTypeHandler manages the type.
         */
        SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                   displayName,
                                                                                   description,
                                                                                   elementPosition,
                                                                                   minCardinality,
                                                                                   maxCardinality,
                                                                                   isDeprecated,
                                                                                   defaultValueOverride,
                                                                                   allowsDuplicateValues,
                                                                                   orderedValues,
                                                                                   sortOrder,
                                                                                   minimumLength,
                                                                                   length,
                                                                                   significantDigits,
                                                                                   isNullable,
                                                                                   nativeJavaClass,
                                                                                   aliases,
                                                                                   additionalProperties,
                                                                                   schemaAttributeTypeId,
                                                                                   schemaAttributeTypeName,
                                                                                   extendedProperties,
                                                                                   repositoryHelper,
                                                                                   serviceName,
                                                                                   serverName);

        /*
         * The formula is set if the attribute is derived
         */
        if (formula != null)
        {
            schemaAttributeBuilder.setCalculatedValue(userId, externalSourceGUID, externalSourceName, formula, methodName);
        }

        schemaAttributeBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

        SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(qualifiedName,
                                                                        externalSchemaTypeGUID,
                                                                        dataType,
                                                                        defaultValue,
                                                                        fixedValue,
                                                                        validValuesSetGUID);

        schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);

        return this.createNestedSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                parentElementGUID,
                                                parentElementGUIDParameterName,
                                                qualifiedName,
                                                qualifiedNameParameterName,
                                                schemaAttributeBuilder,
                                                effectiveFrom,
                                                effectiveTo,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Set up the schema type builder for the column's type.
     *
     * @param qualifiedName qualified name for the column
     * @param externalSchemaTypeGUID unique identifier of a schema Type that provides the type. If null, a private schema type is used
     * @param dataType data type name - for stored values
     * @param defaultValue string containing default value - for stored values
     * @param fixedValue string containing a fixed value - for a literal
     * @param validValuesSetGUID unique identifier of a valid value set that lists the valid values for this schema
     * @return filled out schema type builder
     */
    SchemaTypeBuilder getSchemaTypeBuilder(String qualifiedName,
                                           String externalSchemaTypeGUID,
                                           String dataType,
                                           String defaultValue,
                                           String fixedValue,
                                           String validValuesSetGUID)
    {
        String schemaTypeGUID = OpenMetadataType.STRUCT_SCHEMA_TYPE_TYPE_GUID;
        String schemaTypeName = OpenMetadataType.STRUCT_SCHEMA_TYPE_TYPE_NAME;

        if (externalSchemaTypeGUID != null)
        {
            schemaTypeGUID = OpenMetadataType.EXTERNAL_SCHEMA_TYPE_TYPE_GUID;
            schemaTypeName = OpenMetadataType.EXTERNAL_SCHEMA_TYPE_TYPE_NAME;
        }
        else if (validValuesSetGUID != null)
        {
            schemaTypeGUID = OpenMetadataType.ENUM_SCHEMA_TYPE_TYPE_GUID;
            schemaTypeName = OpenMetadataType.ENUM_SCHEMA_TYPE_TYPE_NAME;
        }
        else if (fixedValue != null)
        {
            schemaTypeGUID = OpenMetadataType.LITERAL_SCHEMA_TYPE_TYPE_GUID;
            schemaTypeName = OpenMetadataType.LITERAL_SCHEMA_TYPE_TYPE_NAME;
        }
        else if (dataType != null)
        {
            schemaTypeGUID = OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_GUID;
            schemaTypeName = OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;
        }

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(qualifiedName + ":Type",
                                                                    schemaTypeGUID,
                                                                    schemaTypeName,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);
        schemaTypeBuilder.setDataType(dataType);
        schemaTypeBuilder.setDefaultValue(defaultValue);
        schemaTypeBuilder.setFixedValue(fixedValue);
        schemaTypeBuilder.setExternalSchemaTypeGUID(externalSchemaTypeGUID);
        schemaTypeBuilder.setValidValuesSetGUID(validValuesSetGUID);

        return schemaTypeBuilder;
    }



    /**
     * Create a new schema attribute with its type attached.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param parentElementGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param parentElementGUIDParameterName parameter name supplying parentElementGUID
     * @param parentElementTypeName type of the parent element - may be a schema attribute or a schema type
     * @param parentAttributeRelationshipTypeGUID unique identifier of the relationship from the new schema type to the parent
     * @param parentAttributeRelationshipTypeName unique name of the relationship from the new schema type to the parent
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param schemaAttributeBuilder builder containing the properties of the schema type
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new schema attribute already linked to its parent
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNestedSchemaAttribute(String                 userId,
                                              String                 externalSourceGUID,
                                              String                 externalSourceName,
                                              String                 parentElementGUID,
                                              String                 parentElementGUIDParameterName,
                                              String                 parentElementTypeName,
                                              String                 parentAttributeRelationshipTypeGUID,
                                              String                 parentAttributeRelationshipTypeName,
                                              String                 qualifiedName,
                                              String                 qualifiedNameParameterName,
                                              SchemaAttributeBuilder schemaAttributeBuilder,
                                              Date                   effectiveFrom,
                                              Date                   effectiveTo,
                                              boolean                forLineage,
                                              boolean                forDuplicateProcessing,
                                              Date                   effectiveTime,
                                              String                 methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * Now create the nested schema attribute itself along with its schema type.
         * The returned value is the guid of the nested attribute (e.g. table).
         */
        String schemaAttributeGUID = this.createBeanInRepository(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 schemaAttributeBuilder.getTypeGUID(),
                                                                 schemaAttributeBuilder.getTypeName(),
                                                                 schemaAttributeBuilder,
                                                                 effectiveTime,
                                                                 methodName);

        if (schemaAttributeGUID != null)
        {
            final String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

            this.addEmbeddedTypes(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  schemaAttributeGUID,
                                  schemaAttributeGUIDParameterName,
                                  schemaAttributeBuilder.getTypeName(),
                                  schemaAttributeBuilder.getSchemaTypeBuilder(),
                                  effectiveFrom,
                                  effectiveTo,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               parentElementGUID,
                                               parentElementGUIDParameterName,
                                               parentElementTypeName,
                                               schemaAttributeGUID,
                                               schemaAttributeGUIDParameterName,
                                               schemaAttributeBuilder.getTypeName(),
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               parentAttributeRelationshipTypeGUID,
                                               parentAttributeRelationshipTypeName,
                                               null,
                                               effectiveTime,
                                               methodName);

            return schemaAttributeGUID;
        }

        /*
         * Not reachable because any failures result in exceptions.
         */
        return null;
    }


    /**
     * Returns a list of schema attributes that are linked to a ComplexSchemaType via the AttributeForSchema relationship.
     * It validates the unique identifier of the complex schema type, and the visibility/security of any attached asset using the supported zones
     * for this component.  Then begins to extract the schema attributes.  Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param guidParameterName String name of the parameter supplying the guid.
     * @param requiredClassificationName  String the name of the classification that must be on the schema attribute or linked schema type entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the schema attribute or linked schema type entity.
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesForComplexSchemaType(String  userId,
                                                                          String  schemaTypeGUID,
                                                                          String  guidParameterName,
                                                                          String  requiredClassificationName,
                                                                          String  omittedClassificationName,
                                                                          int     startFrom,
                                                                          int     pageSize,
                                                                          boolean forLineage,
                                                                          boolean forDuplicateProcessing,
                                                                          Date    effectiveTime,
                                                                          String  methodName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        return this.getSchemaAttributesForComplexSchemaType(userId,
                                                            schemaTypeGUID,
                                                            guidParameterName,
                                                            OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                            requiredClassificationName,
                                                            omittedClassificationName,
                                                            supportedZones,
                                                            startFrom,
                                                            pageSize,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime,
                                                            methodName);
    }


    /**
     * Returns a list of schema attributes that are linked to a ComplexSchemaType via the AttributeForSchema relationship.
     * It validates the unique identifier of the complex schema type, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaTypeGUID String   unique id for containing schema type.
     * @param schemaTypeGUIDParameterName String name of the parameter supplying the guid.
     * @param schemaAttributeTypeName unique name of associated schema attribute type
     * @param requiredClassificationName  String the name of the classification that must be on the schema attribute or linked schema type entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the schema attribute or linked schema type entity.
     * @param serviceSupportedZones list of zone names for calling service
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesForComplexSchemaType(String       userId,
                                                                          String       schemaTypeGUID,
                                                                          String       schemaTypeGUIDParameterName,
                                                                          String       schemaAttributeTypeName,
                                                                          String       requiredClassificationName,
                                                                          String       omittedClassificationName,
                                                                          List<String> serviceSupportedZones,
                                                                          int          startFrom,
                                                                          int          pageSize,
                                                                          boolean      forLineage,
                                                                          boolean      forDuplicateProcessing,
                                                                          Date         effectiveTime,
                                                                          String       methodName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String schemaAttributeGUIDParameterName = "schemaAttributeEntity.getGUID()";

        String typeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (schemaAttributeTypeName != null)
        {
            typeName = schemaAttributeTypeName;
        }

        List<EntityDetail>  entities = this.getAttachedEntities(userId,
                                                                schemaTypeGUID,
                                                                schemaTypeGUIDParameterName,
                                                                OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                typeName,
                                                                requiredClassificationName,
                                                                omittedClassificationName,
                                                                2,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                serviceSupportedZones,
                                                                startFrom,
                                                                pageSize,
                                                                effectiveTime,
                                                                methodName);

        List<SCHEMA_ATTRIBUTE>  results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail schemaAttributeEntity : entities)
            {
                if (schemaAttributeEntity != null)
                {
                    /*
                     * This method verifies the visibility of the entity and the security permission.
                     */
                    results.add(this.getSchemaAttributeFromEntity(userId,
                                                                  schemaAttributeEntity.getGUID(),
                                                                  schemaAttributeGUIDParameterName,
                                                                  schemaAttributeEntity,
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
        else
        {
            return results;
        }
    }


    /**
     * Returns a list of schema attributes that are linked to a parent schema element.  This may be a complex schema type or a
     * schema attribute.  It is necessary to find out the type of the parent schema element to be sure which type of
     * retrieval is needed.
     *
     * @param userId         String   userId of user making request.
     * @param parentElementGUID String   unique id for parent schema element.
     * @param parentElementGUIDParameterName String name of the parameter supplying the guid.
     * @param schemaAttributeTypeName subtype of schema attribute or null
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getAttachedSchemaAttributes(String  userId,
                                                              String  parentElementGUID,
                                                              String  parentElementGUIDParameterName,
                                                              String  schemaAttributeTypeName,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return getAttachedSchemaAttributes(userId,
                                           parentElementGUID,
                                           parentElementGUIDParameterName,
                                           schemaAttributeTypeName,
                                           supportedZones,
                                           startFrom,
                                           pageSize,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Returns a list of schema attributes that are linked to a parent schema element.  This may be a complex schema type or a
     * schema attribute.  It is necessary to find out the type of the parent schema element to be sure which type of
     * retrieval is needed.
      *
     * @param userId         String   userId of user making request.
     * @param parentElementGUID String   unique id for parent schema element.
     * @param parentElementGUIDParameterName String name of the parameter supplying the guid.
     * @param schemaAttributeTypeName subtype of schema attribute or null
     * @param serviceSupportedZones list of zone names for calling service
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getAttachedSchemaAttributes(String       userId,
                                                              String       parentElementGUID,
                                                              String       parentElementGUIDParameterName,
                                                              String       schemaAttributeTypeName,
                                                              List<String> serviceSupportedZones,
                                                              int          startFrom,
                                                              int          pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        EntityDetail parentEntity = this.getEntityFromRepository(userId,
                                                                 parentElementGUID,
                                                                 parentElementGUIDParameterName,
                                                                 OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 serviceSupportedZones,
                                                                 effectiveTime,
                                                                 methodName);

        if ((parentEntity != null) && (parentEntity.getType() != null))
        {
            if (repositoryHelper.isTypeOf(serviceName, parentEntity.getType().getTypeDefName(), OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME))
            {
                return this.getNestedSchemaAttributes(userId,
                                                      parentElementGUID,
                                                      parentElementGUIDParameterName,
                                                      schemaAttributeTypeName,
                                                      serviceSupportedZones,
                                                      startFrom,
                                                      pageSize,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
            }
            else
            {
                return this.getSchemaAttributesForComplexSchemaType(userId,
                                                                    parentElementGUID,
                                                                    parentElementGUIDParameterName,
                                                                    schemaAttributeTypeName,
                                                                    null,
                                                                    null,
                                                                    serviceSupportedZones,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);
            }
        }

        return null;
    }


    /**
     * Returns a list of schema attributes that are linked to a schema attribute via the NestedSchemaAttribute relationship.
     * It validates the unique identifier of the parent schema attribute, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaAttributeGUID String   unique id for containing schema attribute.
     * @param schemaAttributeGUIDParameterName String name of the parameter supplying the guid.
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getNestedSchemaAttributes(String  userId,
                                                            String  schemaAttributeGUID,
                                                            String  schemaAttributeGUIDParameterName,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return getNestedSchemaAttributes(userId,
                                         schemaAttributeGUID,
                                         schemaAttributeGUIDParameterName,
                                         OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                         supportedZones,
                                         startFrom,
                                         pageSize,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Returns a list of schema attributes that are linked to a schema attribute via the NestedSchemaAttribute relationship.
     * It validates the unique identifier of the parent schema attribute, and the visibility/security of any attached asset using the
     * supplied supported zones (needed for the calls from the OCF Metadata REST Services).
     * Then it begins to extract the schema attributes. Exceptions occur if a schema attribute does not have a type.
     *
     * @param userId         String   userId of user making request.
     * @param schemaAttributeGUID String   unique id for containing schema attribute.
     * @param schemaAttributeGUIDParameterName String name of the parameter supplying the guid.
     * @param schemaAttributeTypeName subtype of schema attribute (or null)
     * @param serviceSupportedZones list of zone names for calling service
     * @param startFrom   int      starting position for first returned element.
     * @param pageSize    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getNestedSchemaAttributes(String       userId,
                                                            String       schemaAttributeGUID,
                                                            String       schemaAttributeGUIDParameterName,
                                                            String       schemaAttributeTypeName,
                                                            List<String> serviceSupportedZones,
                                                            int          startFrom,
                                                            int          pageSize,
                                                            boolean      forLineage,
                                                            boolean      forDuplicateProcessing,
                                                            Date         effectiveTime,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String nestedSchemaAttributeGUIDParameterName = "schemaAttributeEntity.getGUID()";

        String resultTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (schemaAttributeTypeName != null)
        {
            resultTypeName = schemaAttributeTypeName;
        }

        List<EntityDetail>  entities = this.getAttachedEntities(userId,
                                                                schemaAttributeGUID,
                                                                schemaAttributeGUIDParameterName,
                                                                OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                OpenMetadataType.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                resultTypeName,
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

        List<SCHEMA_ATTRIBUTE>  results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail schemaAttributeEntity : entities)
            {
                if (schemaAttributeEntity != null)
                {
                    /*
                     * This method verifies the visibility of the entity and the security permission.
                     */
                    results.add(this.getSchemaAttributeFromEntity(userId,
                                                                  schemaAttributeEntity.getGUID(),
                                                                  nestedSchemaAttributeGUIDParameterName,
                                                                  schemaAttributeEntity,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName));
                }
            }
        }
        else
        {
            /*
             * Using the old pattern where a schema type is between the nested schema attributes.
             */
            EntityDetail entity = this.getAttachedEntity(userId,
                                                         schemaAttributeGUID,
                                                         schemaAttributeGUIDParameterName,
                                                         OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                         OpenMetadataType.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                         OpenMetadataType.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                         OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                         2,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         supportedZones,
                                                         effectiveTime,
                                                         methodName);

            if (entity != null)
            {
                String schemaTypeGUIDParameterName = "schemaTypeGUID";

                return getSchemaAttributesForComplexSchemaType(userId,
                                                               entity.getGUID(),
                                                               schemaTypeGUIDParameterName,
                                                               schemaAttributeTypeName,
                                                               null,
                                                               null,
                                                               supportedZones,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the special links (like foreign keys) between attributes.
     *
     * @param userId calling user
     * @param schemaAttributeEntity details from the repository
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of schema attribute relationships populated with information from the repository
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private List<Relationship> getSchemaAttributeRelationships(String       userId,
                                                               EntityDetail schemaAttributeEntity,
                                                               boolean      forLineage,
                                                               boolean      forDuplicateProcessing,
                                                               Date         effectiveTime,
                                                               String       methodName) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String schemaAttributeGUIDParameterName = "schemaAttributeEntity";

        List<Relationship> results = new ArrayList<>();

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   schemaAttributeEntity.getGUID(),
                                                                   schemaAttributeGUIDParameterName,
                                                                   OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                   0,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   effectiveTime,
                                                                   methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if ((relationship != null) && (relationship.getType() != null))
                {
                    String typeName = relationship.getType().getTypeDefName();

                    if (OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME.equals(typeName))
                    {
                        results.add(relationship);
                    }
                    else if (OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_NAME.equals(typeName))
                    {
                        results.add(relationship);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * From the schema attribute entity, gather the related schema type, schema link and/or schema attribute relationships
     * and create a schema attribute bean.  The caller is expected to have validated that it is ok to return this schema attribute.
     *
     * @param userId calling userId
     * @param schemaAttributeGUID unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName parameter passing the schemaAttributeGUID
     * @param schemaAttributeEntity entity retrieved for the schema attribute
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return a new schema attribute object, or null if the schema attribute is either not visible to the user, or its classifications
     *         are not what are requested.
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private SCHEMA_ATTRIBUTE getSchemaAttributeFromEntity(String       userId,
                                                          String       schemaAttributeGUID,
                                                          String       schemaAttributeGUIDParameterName,
                                                          EntityDetail schemaAttributeEntity,
                                                          boolean      forLineage,
                                                          boolean      forDuplicateProcessing,
                                                          Date         effectiveTime,
                                                          String       methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        if ((schemaAttributeEntity != null) && (schemaAttributeEntity.getType() != null))
        {
            /*
             * Extra entities are required depending on the type of the schema.
             */
            SCHEMA_TYPE  schemaType  = null;

            /*
             * The table may have its type stored as a classification, or as a linked schema type.  The column is linked to
             * the attribute in the first case, and the schema type in the second case.
             */
            try
            {
                Classification typeClassification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                 schemaAttributeEntity,
                                                                                                 OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                                                                 methodName);

                if ((typeClassification != null) && (typeClassification.getProperties() != null))
                {
                    String schemaTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                               OpenMetadataType.SCHEMA_TYPE_NAME_PROPERTY_NAME,
                                                                               typeClassification.getProperties(),
                                                                               methodName);

                    schemaType = schemaTypeHandler.getSchemaTypeFromInstance(userId,
                                                                             schemaAttributeEntity,
                                                                             schemaTypeName,
                                                                             typeClassification.getProperties(),
                                                                             schemaAttributeEntity.getClassifications(),
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);
                }
            }
            catch (ClassificationErrorException classificationNotKnown)
            {
                /*
                 * Type classification not supported.
                 */
            }


            if (schemaType == null)
            {
                /*
                 * Look for an explicit private schema type
                 */
                schemaType = schemaTypeHandler.getSchemaTypeForParent(userId,
                                                                      schemaAttributeGUID,
                                                                      schemaAttributeGUIDParameterName,
                                                                      OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                      OpenMetadataType.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                      OpenMetadataType.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
            }


            List<Relationship> attributeRelationships = this.getSchemaAttributeRelationships(userId,
                                                                                             schemaAttributeEntity,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing,
                                                                                             effectiveTime,
                                                                                             methodName);

            return schemaAttributeConverter.getNewSchemaAttributeBean(beanClass,
                                                                      schemaAttributeEntity,
                                                                      schemaTypeBeanClass,
                                                                      schemaType,
                                                                      attributeRelationships,
                                                                      methodName);
        }

        return null;
    }


    /**
     * Work through the schema attributes adding or updating the instances
     *
     * @param userId calling userId
     * @param schemaAttributeEntities list of retrieved entities
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of new schema attributes
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<SCHEMA_ATTRIBUTE> getSchemaAttributesFromEntities(String             userId,
                                                                   List<EntityDetail> schemaAttributeEntities,
                                                                   boolean            forLineage,
                                                                   boolean            forDuplicateProcessing,
                                                                   Date               effectiveTime,
                                                                   String             methodName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String parameterName = "schemaAttributeEntities";

        if (schemaAttributeEntities != null)
        {
            List<SCHEMA_ATTRIBUTE> schemaAttributes = new ArrayList<>();

            for (EntityDetail entity : schemaAttributeEntities)
            {
                if (entity != null)
                {
                    schemaAttributes.add(this.getSchemaAttributeFromEntity(userId,
                                                                           entity.getGUID(),
                                                                           parameterName,
                                                                           entity,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName));
                }
            }

            if (!schemaAttributes.isEmpty())
            {
                return schemaAttributes;
            }
        }

        return null;
    }


    /**
     * Retrieve a specific schema attribute by GUID.   It is only returned if the guid is known, the entity is of the correct type,
     * the classifications are as expected and any asset it is connected to is both visible via the zones setting and the
     * security verifier allows the update.
     *
     * @param userId calling userId
     * @param schemaAttributeGUID unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName parameter passing the schemaAttributeGUID
     * @param expectedTypeName type or subtype of schema attribute
     * @param requiredClassificationName a classification that must be either on the schema attribute or its type.
     * @param omittedClassificationName a classification that must NOT be on either the schema attribute or its type.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return guid of new schema
     *
     * @throws InvalidParameterException the guid or bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public SCHEMA_ATTRIBUTE getSchemaAttribute(String  userId,
                                               String  schemaAttributeGUID,
                                               String  schemaAttributeGUIDParameterName,
                                               String  expectedTypeName,
                                               String  requiredClassificationName,
                                               String  omittedClassificationName,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String typeParameterName = "expectedTypeName";

        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(expectedTypeName, typeParameterName, methodName);

        /*
         * This method verifies the GUID and the type
         */
        EntityDetail schemaAttributeEntity = this.getEntityFromRepository(userId,
                                                                          schemaAttributeGUID,
                                                                          schemaAttributeGUIDParameterName,
                                                                          expectedTypeName,
                                                                          requiredClassificationName,
                                                                          omittedClassificationName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          supportedZones,
                                                                          effectiveTime,
                                                                          methodName);

        /*
         * This method issues additional retrieves to the metadata repositories to build the schema attribute bean, its type and any attribute
         * relationships.
         */
        return this.getSchemaAttributeFromEntity(userId,
                                                 schemaAttributeGUID,
                                                 schemaAttributeGUIDParameterName,
                                                 schemaAttributeEntity,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Returns a list of specifically typed schema attributes with matching names - either display names or qualified names
     *
     * @param userId         String   userId of user making request.
     * @param typeGUID       String   unique identifier of type of schema attribute required.
     * @param typeName       String  unique name of type of schema attribute required.
     * @param name           String   name (qualified or display name) of schema attribute.
     * @param requiredClassificationName  String name of classification that must be present
     * @param omittedClassificationName  String name of classification that must not be present
     * @param elementStart   int      starting position for first returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesByName(String       userId,
                                                            String       typeGUID,
                                                            String       typeName,
                                                            String       name,
                                                            String       requiredClassificationName,
                                                            String       omittedClassificationName,
                                                            int          elementStart,
                                                            int          maxElements,
                                                            boolean      forLineage,
                                                            boolean      forDuplicateProcessing,
                                                            Date         effectiveTime,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return getSchemaAttributesByName(userId,
                                         typeGUID,
                                         typeName,
                                         name,
                                         requiredClassificationName,
                                         omittedClassificationName,
                                         supportedZones,
                                         elementStart,
                                         maxElements,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Returns a list of specifically typed schema attributes with matching names - either display names or qualified names
     *
     * @param userId         String   userId of user making request
     * @param typeGUID       String   unique identifier of type of schema attribute required
     * @param typeName       String  unique name of type of schema attribute required
     * @param name           String   name (qualified or display name) of schema attribute
     * @param requiredClassificationName  String name of classification that must be present
     * @param omittedClassificationName  String name of classification that must not be present
     * @param serviceSupportedZones zones that assets must be;ong in to be visible
     * @param startFrom   int      starting position for first returned element
     * @param pageSize    int      maximum number of elements to return on the call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return a schema attributes response
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<SCHEMA_ATTRIBUTE> getSchemaAttributesByName(String       userId,
                                                            String       typeGUID,
                                                            String       typeName,
                                                            String       name,
                                                            String       requiredClassificationName,
                                                            String       omittedClassificationName,
                                                            List<String> serviceSupportedZones,
                                                            int          startFrom,
                                                            int          pageSize,
                                                            boolean      forLineage,
                                                            boolean      forDuplicateProcessing,
                                                            Date         effectiveTime,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        List<EntityDetail>  schemaAttributeEntities = this.getEntitiesByValue(userId,
                                                                              name,
                                                                              nameParameterName,
                                                                              typeGUID,
                                                                              typeName,
                                                                              specificMatchPropertyNames,
                                                                              true,
                                                                              false,
                                                                              requiredClassificationName,
                                                                              omittedClassificationName,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              serviceSupportedZones,
                                                                              null,
                                                                              startFrom,
                                                                              pageSize,
                                                                              effectiveTime,
                                                                              methodName);

        return this.getSchemaAttributesFromEntities(userId, schemaAttributeEntities, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching schema attribute elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SCHEMA_ATTRIBUTE>   findSchemaAttributes(String  userId,
                                                         String  searchString,
                                                         String  searchStringParameterName,
                                                         String  resultTypeGUID,
                                                         String  resultTypeName,
                                                         String  requiredClassificationName,
                                                         String  omittedClassificationName,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        List<EntityDetail> schemaAttributeEntities = this.findEntities(userId,
                                                                       searchString,
                                                                       searchStringParameterName,
                                                                       resultTypeGUID,
                                                                       resultTypeName,
                                                                       requiredClassificationName,
                                                                       omittedClassificationName,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

        return this.getSchemaAttributesFromEntities(userId, schemaAttributeEntities, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Update the properties in a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param schemaAttributeGUIDParameterName parameter name supplying schemaAttributeGUID
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param externalSchemaTypeGUID unique identifier of an external schema identifier
     * @param dataType data type name - for stored values
     * @param defaultValue string containing default value - for stored values
     * @param fixedValue string containing fixed value - for literals
     * @param validValuesSetGUID unique identifier for a valid values set to support
     * @param formula String formula - for derived values
     * @param isDeprecated is this table deprecated?
     * @param elementPosition the position of this column in its parent table.
     * @param minCardinality minimum number of repeating instances allowed for this column - typically 1
     * @param maxCardinality the maximum number of repeating instances allowed for this column - typically 1
     * @param allowsDuplicateValues  whether the same value can be used by more than one instance of this attribute
     * @param orderedValues whether the attribute instances are arranged in an order
     * @param sortOrder the order that the attribute instances are arranged in - if any
     * @param minimumLength the minimum length of the data
     * @param length the length of the data field
     * @param significantDigits number of significant digits to the right of decimal point
     * @param isNullable whether the field is nullable or not
     * @param nativeJavaClass equivalent Java class implementation
     * @param defaultValueOverride default value for this column
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type of this element - which defines the valid extended properties
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateSchemaAttribute(String               userId,
                                        String               externalSourceGUID,
                                        String               externalSourceName,
                                        String               schemaAttributeGUID,
                                        String               schemaAttributeGUIDParameterName,
                                        String               qualifiedName,
                                        String               qualifiedNameParameterName,
                                        String               displayName,
                                        String               description,
                                        String               externalSchemaTypeGUID,
                                        String               dataType,
                                        String               defaultValue,
                                        String               fixedValue,
                                        String               validValuesSetGUID,
                                        String               formula,
                                        boolean              isDeprecated,
                                        int                  elementPosition,
                                        int                  minCardinality,
                                        int                  maxCardinality,
                                        boolean              allowsDuplicateValues,
                                        boolean              orderedValues,
                                        String               defaultValueOverride,
                                        int                  sortOrder,
                                        int                  minimumLength,
                                        int                  length,
                                        int                  significantDigits,
                                        boolean              isNullable,
                                        String               nativeJavaClass,
                                        List<String>         aliases,
                                        Map<String, String>  additionalProperties,
                                        String               typeName,
                                        Map<String, Object>  extendedProperties,
                                        Date                 effectiveFrom,
                                        Date                 effectiveTo,
                                        boolean              isMergeUpdate,
                                        boolean              forLineage,
                                        boolean              forDuplicateProcessing,
                                        Date                 effectiveTime,
                                        String               methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        /*
         * Check that the type name requested is valid.
         */
        String attributeTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;
        String attributeTypeId   = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID;

        if (typeName != null)
        {
            attributeTypeName = typeName;
            attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                         OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        }

        EntityDetail schemaAttributeEntity = this.getEntityFromRepository(userId,
                                                                          schemaAttributeGUID,
                                                                          schemaAttributeGUIDParameterName,
                                                                          attributeTypeName,
                                                                          null,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        if (schemaAttributeEntity != null)
        {
            /*
             * Load up the builder objects.  The builders manage the properties of the metadata elements that make up the schema attribute,
             * and the schemaTypeHandler manages the type.
             */
            SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                       displayName,
                                                                                       description,
                                                                                       elementPosition,
                                                                                       minCardinality,
                                                                                       maxCardinality,
                                                                                       isDeprecated,
                                                                                       defaultValueOverride,
                                                                                       allowsDuplicateValues,
                                                                                       orderedValues,
                                                                                       sortOrder,
                                                                                       minimumLength,
                                                                                       length,
                                                                                       significantDigits,
                                                                                       isNullable,
                                                                                       nativeJavaClass,
                                                                                       aliases,
                                                                                       additionalProperties,
                                                                                       attributeTypeId,
                                                                                       attributeTypeName,
                                                                                       extendedProperties,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       serverName);

            schemaAttributeBuilder.setEffectivityDates(effectiveFrom, effectiveTo);

            InstanceProperties instanceProperties = schemaAttributeBuilder.getInstanceProperties(methodName);

            this.updateBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        instanceProperties,
                                        true,
                                        effectiveTime,
                                        methodName);

            SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(qualifiedName,
                                                                            externalSchemaTypeGUID,
                                                                            dataType,
                                                                            defaultValue,
                                                                            fixedValue,
                                                                            validValuesSetGUID);

            // todo this logic assumes the schema type is stored as a classification
            setClassificationInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          schemaAttributeEntity,
                                          schemaAttributeGUIDParameterName,
                                          attributeTypeName,
                                          OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                          OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                          schemaTypeBuilder.getTypeEmbeddedInstanceProperties(methodName),
                                          isMergeUpdate,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

            /*
             * The formula is set if the column is derived
             */
            if (formula != null)
            {
                schemaAttributeBuilder.setCalculatedValue(userId, externalSourceGUID, externalSourceName, formula, methodName);

                setClassificationInRepository(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              schemaAttributeEntity,
                                              schemaAttributeGUIDParameterName,
                                              attributeTypeName,
                                              OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                              OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                              schemaTypeBuilder.getTypeEmbeddedInstanceProperties(methodName),
                                              isMergeUpdate,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              effectiveTime,
                                              methodName);
            }
        }
    }


    /**
     * Update the properties in a schema attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to connect the new schema attribute to
     * @param schemaAttributeGUIDParameterName parameter name supplying schemaAttributeGUID
     * @param qualifiedName unique identifier for this schema type
     * @param qualifiedNameParameterName name of parameter supplying the qualified name
     * @param schemaAttributeBuilder schema attribute builder
     * @param typeName name of the type of this element - which defines the valid extended properties
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateSchemaAttribute(String                 userId,
                                        String                 externalSourceGUID,
                                        String                 externalSourceName,
                                        String                 schemaAttributeGUID,
                                        String                 schemaAttributeGUIDParameterName,
                                        String                 qualifiedName,
                                        String                 qualifiedNameParameterName,
                                        SchemaAttributeBuilder schemaAttributeBuilder,
                                        String                 typeName,
                                        boolean                isMergeUpdate,
                                        boolean                forLineage,
                                        boolean                forDuplicateProcessing,
                                        Date                   effectiveTime,
                                        String                 methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        /*
         * Check that the type name requested is valid.
         */
        String attributeTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (typeName != null)
        {
            attributeTypeName = typeName;
        }

        EntityDetail schemaAttributeEntity = this.getEntityFromRepository(userId,
                                                                          schemaAttributeGUID,
                                                                          schemaAttributeGUIDParameterName,
                                                                          attributeTypeName,
                                                                          null,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        if (schemaAttributeEntity != null)
        {
            InstanceProperties instanceProperties = schemaAttributeBuilder.getInstanceProperties(methodName);

            this.updateBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        instanceProperties,
                                        true,
                                        effectiveTime,
                                        methodName);

            SchemaTypeBuilder schemaTypeBuilder = schemaAttributeBuilder.getSchemaTypeBuilder();
            if (schemaTypeBuilder != null)
            {

                /*
                 * The formula is set if the schema attribute is derived. Need to test the merge semantics.
                 */
                InstanceProperties calculatedValueProperties = schemaTypeBuilder.getCalculatedValueProperties(methodName);
                if (calculatedValueProperties == null)
                {
                    /*
                     * if we have no formula requested and we are not a merge, any existing
                     * calculated value classification should be cleared
                     */
                    if (!isMergeUpdate)
                    {
                        try {
                            String sourceName = "local";
                            if (externalSourceName != null && externalSourceName.length() >0)
                            {
                                sourceName = externalSourceName;
                            }

                            repositoryHelper.getClassificationFromEntity(sourceName, schemaAttributeEntity, OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME, methodName);
                            removeClassificationFromRepository(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               schemaAttributeGUID,
                                                               schemaAttributeGUIDParameterName,
                                                               OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                               OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                               OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
                        } catch (ClassificationErrorException e)
                        {
                            // there was no calculated value classification associated with the entity
                        }
                    }
                } else
                {
                    setClassificationInRepository(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  schemaAttributeEntity,
                                                  schemaAttributeGUIDParameterName,
                                                  attributeTypeName,
                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                  calculatedValueProperties,
                                                  isMergeUpdate,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  supportedZones,
                                                  effectiveTime,
                                                  methodName);

                }
                // todo this logic assumes the schema type is stored as a classification
                setClassificationInRepository(userId,
                                              externalSourceGUID, externalSourceName,
                                              schemaAttributeEntity,
                                              schemaAttributeGUIDParameterName,
                                              attributeTypeName,
                                              OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                              OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                              schemaTypeBuilder.getTypeEmbeddedInstanceProperties(methodName),
                                              isMergeUpdate,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              effectiveTime,
                                              methodName);
            }
        }
    }


    /**
     * Update a schema attribute
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software capability representing the caller
     * @param externalSourceName          unique name of software capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param instanceProperties          the schema attribute's properties
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String             userId,
                                      String             externalSourceGUID,
                                      String             externalSourceName,
                                      String             schemaAttributeGUID,
                                      InstanceProperties instanceProperties,
                                      boolean            forLineage,
                                      boolean            forDuplicateProcessing,
                                      Date               effectiveTime,
                                      String             methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String parameterName = "schemaAttributeGUID";

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaAttributeGUID,
                                    parameterName,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    instanceProperties,
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Update a schema attribute
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software capability representing the caller
     * @param externalSourceName          unique name of software capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName  parameter supplying schemaAttributeGUID
     * @param instanceProperties          the schema attribute's properties
     * @param isMergeUpdate               should the properties be merged with existing properties of replace them?
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                  calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String             userId,
                                      String             externalSourceGUID,
                                      String             externalSourceName,
                                      String             schemaAttributeGUID,
                                      String             schemaAttributeGUIDParameterName,
                                      InstanceProperties instanceProperties,
                                      boolean            isMergeUpdate,
                                      boolean            forLineage,
                                      boolean            forDuplicateProcessing,
                                      Date               effectiveTime,
                                      String             methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaAttributeGUID,
                                    schemaAttributeGUIDParameterName,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    instanceProperties,
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Update a schema attribute
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software capability representing the caller
     * @param externalSourceName          unique name of software capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName  parameter supplying schemaAttributeGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param instanceProperties          the schema attribute's properties
     * @param isMergeUpdate               should the properties be merged with existing properties of replace them?
     * @param methodName                  calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String             userId,
                                      String             externalSourceGUID,
                                      String             externalSourceName,
                                      String             schemaAttributeGUID,
                                      String             schemaAttributeGUIDParameterName,
                                      boolean            forLineage,
                                      boolean            forDuplicateProcessing,
                                      List<String>       serviceSupportedZones,
                                      InstanceProperties instanceProperties,
                                      boolean            isMergeUpdate,
                                      Date               effectiveTime,
                                      String             methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    schemaAttributeGUID,
                                    schemaAttributeGUIDParameterName,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                    OpenMetadataType.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    instanceProperties,
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }

    /**
     * Remove any links to schema types
     *
     * @param userId                      calling user
     * @param externalSourceGUID          unique identifier of software capability representing the caller
     * @param externalSourceName          unique name of software capability representing the caller
     * @param schemaAttributeGUID         unique identifier of schema attribute
     * @param schemaAttributeGUIDParameterName  parameter supplying schemaAttributeGUID
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                  calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaTypes(String             userId,
                                  String             externalSourceGUID,
                                  String             externalSourceName,
                                  String             schemaAttributeGUID,
                                  String             schemaAttributeGUIDParameterName,
                                  boolean            forLineage,
                                  boolean            forDuplicateProcessing,
                                  Date               effectiveTime,
                                  String             methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        // todo retrieve relationships and remove those that link a schema attribute to its type(s)
    }
}
