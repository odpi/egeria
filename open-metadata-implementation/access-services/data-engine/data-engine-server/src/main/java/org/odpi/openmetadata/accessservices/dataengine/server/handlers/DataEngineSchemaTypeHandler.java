/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.LINEAGE_MAPPING_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

/**
 * DataEngineSchemaTypeHandler manages schema types objects from the property server. It runs server-side in the
 * DataEngine OMAS and creates and retrieves schema type entities through the OMRSRepositoryConnector.
 */
public class DataEngineSchemaTypeHandler {
    public static final String SCHEMA_TYPE_GUID_PARAMETER_NAME = "schemaTypeGUID";
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final SchemaTypeHandler<SchemaType> schemaTypeHandler;
    private final SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param repositoryHandler             manages calls to the repository services
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param schemaTypeHandler             handler for managing schema elements in the metadata repositories
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     * @param dataEngineCommonHandler       provides utilities for manipulating entities
     * @param schemaAttributeHandler        handler for managing schema attributes in the metadata repositories
     */
    public DataEngineSchemaTypeHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                                       SchemaTypeHandler<SchemaType> schemaTypeHandler,
                                       SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler,
                                       DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                       DataEngineCommonHandler dataEngineCommonHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.schemaTypeHandler = schemaTypeHandler;
        this.schemaAttributeHandler = schemaAttributeHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create the schema type entity, with the corresponding schema attributes and relationships if it doesn't exist or
     * updates the existing one.
     *
     * @param userId             the name of the calling user
     * @param schemaType         the schema type values
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertSchemaType(String userId, SchemaType schemaType, String externalSourceName) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException {
        final String methodName = "upsertSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(schemaType.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(schemaType.getDisplayName(), DISPLAY_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> originalSchemaTypeEntity = findSchemaTypeEntity(userId, schemaType.getQualifiedName());

        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        String schemaTypeGUID;
        if (!originalSchemaTypeEntity.isPresent()) {
            schemaTypeGUID = schemaTypeHandler.addSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeBuilder, methodName);
        } else {
            schemaTypeGUID = originalSchemaTypeEntity.get().getGUID();
            EntityDetail updatedSchemaTypeEntity = buildSchemaTypeEntityDetail(schemaTypeGUID, schemaType);
            EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalSchemaTypeEntity.get(),
                    updatedSchemaTypeEntity, true);

            if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                schemaTypeHandler.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, SCHEMA_TYPE_GUID_PARAMETER_NAME,
                        schemaTypeBuilder);
            }
        }

        upsertSchemaAttributes(userId, schemaType, schemaTypeGUID, externalSourceName);

        return schemaTypeGUID;
    }

    /**
     * Find out if the SchemaType object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the schema type to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findSchemaTypeEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                   PropertyServerException,
                                                                                                   InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, SCHEMA_TYPE_TYPE_NAME);
    }

    /**
     * Find out if the SchemaAttribute object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findSchemaAttributeEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                        PropertyServerException,
                                                                                                        InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, SCHEMA_ATTRIBUTE_TYPE_NAME);
    }

    /**
     * Create LineageMapping relationship between two schema attributes
     *
     * @param userId                             the name of the calling user
     * @param sourceSchemaAttributeQualifiedName the qualified name of the source schema attribute
     * @param targetSchemaAttributeQualifiedName the qualified name of the target schema attribute
     * @param externalSourceName                 the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addLineageMappingRelationship(String userId, String sourceSchemaAttributeQualifiedName, String targetSchemaAttributeQualifiedName,
                                              String externalSourceName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException {
        final String methodName = "addLineageMappingRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(sourceSchemaAttributeQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(targetSchemaAttributeQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> sourceSchemaAttributeEntity = findSchemaAttributeEntity(userId, sourceSchemaAttributeQualifiedName);
        Optional<EntityDetail> targetSchemaAttributeEntity = findSchemaAttributeEntity(userId, targetSchemaAttributeQualifiedName);

        if (!sourceSchemaAttributeEntity.isPresent()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND, methodName,
                    sourceSchemaAttributeQualifiedName);
            return;
        }
        if (!targetSchemaAttributeEntity.isPresent()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND, methodName,
                    targetSchemaAttributeQualifiedName);
            return;
        }

        dataEngineCommonHandler.upsertExternalRelationship(userId, sourceSchemaAttributeEntity.get().getGUID(),
                targetSchemaAttributeEntity.get().getGUID(), LINEAGE_MAPPING_TYPE_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME, externalSourceName, null);
    }

    /**
     * Remove the schema type with the associated schema attributes
     *
     * @param userId             the name of the calling user
     * @param schemaTypeGUID     the unique identifier of the schema type
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeSchemaType(String userId, String schemaTypeGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws
                                                                                                                                 InvalidParameterException,
                                                                                                                                 PropertyServerException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 FunctionNotSupportedException {
        final String methodName = "removeSchemaType";

        if (deleteSemantic != DeleteSemantic.HARD) {
            throw new FunctionNotSupportedException(OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName, this.getClass().getName(),
                    serverName), this.getClass().getName(), methodName);
        }

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        Set<String> schemaAttributeGUIDs = getSchemaAttributesForSchemaType(userId, schemaTypeGUID);
        for (String schemaAttributeGUID : schemaAttributeGUIDs) {
            dataEngineCommonHandler.removeEntity(userId, schemaAttributeGUID, TABULAR_COLUMN_TYPE_NAME, externalSourceName);
            repositoryHandler.purgeEntity(userId, schemaAttributeGUID, TABULAR_COLUMN_TYPE_GUID, TABULAR_COLUMN_TYPE_NAME, methodName);
        }
        dataEngineCommonHandler.removeEntity(userId, schemaTypeGUID, TABULAR_SCHEMA_TYPE_TYPE_NAME, externalSourceName);
        repositoryHandler.purgeEntity(userId, schemaTypeGUID, TABULAR_SCHEMA_TYPE_TYPE_GUID, TABULAR_SCHEMA_TYPE_TYPE_NAME, methodName);
    }


    private void upsertSchemaAttributes(String userId, SchemaType schemaType, String schemaTypeGUID, String externalSourceName) throws
                                                                                                                                InvalidParameterException,
                                                                                                                                PropertyServerException,
                                                                                                                                UserNotAuthorizedException {

        String methodName = "upsertSchemaAttributes";
        for (Attribute tabularColumn : schemaType.getAttributeList()) {

            Optional<EntityDetail> schemaAttributeEntity = findSchemaAttributeEntity(userId, tabularColumn.getQualifiedName());

            if (!schemaAttributeEntity.isPresent()) {
                createSchemaAttribute(userId, schemaType, schemaTypeGUID, tabularColumn, tabularColumn.getDataType(), externalSourceName);
            } else {
                String schemaAttributeGUID = schemaAttributeEntity.get().getGUID();
                EntityDetail updatedSchemaAttributeEntity = buildSchemaAttributeEntityDetail(schemaAttributeGUID, tabularColumn);
                EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity.get(),
                        updatedSchemaAttributeEntity, true);

                if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                    String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);
                    schemaAttributeHandler.updateSchemaAttribute(userId, externalSourceGUID, externalSourceName,
                            schemaAttributeGUID, getSchemaAttributeBuilder(tabularColumn).getInstanceProperties(methodName));
                }
            }
        }
    }


    private EntityDetail buildSchemaAttributeEntityDetail(String schemaAttributeGUID, Attribute attribute) throws InvalidParameterException {
        String methodName = "buildSchemaAttributeEntityDetail";
        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);

        return dataEngineCommonHandler.buildEntityDetail(schemaAttributeGUID, schemaAttributeBuilder.getInstanceProperties(methodName));
    }

    SchemaAttributeBuilder getSchemaAttributeBuilder(Attribute attribute) {
        HashMap<String, String> additionalProperties = new HashMap<>();
        return new SchemaAttributeBuilder(attribute.getQualifiedName(), attribute.getDisplayName(), attribute.getDescription(),
                attribute.getPosition(), attribute.getMinCardinality(), attribute.getMaxCardinality(), attribute.isDeprecated(),
                attribute.getDefaultValueOverride(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                dataEngineCommonHandler.getSortOrder(attribute), attribute.getMinimumLength(), attribute.getLength(), attribute.getPrecision(),
                attribute.isNullable(), attribute.getNativeClass(), attribute.getAliases(), additionalProperties,
                TABULAR_COLUMN_TYPE_GUID, TABULAR_COLUMN_TYPE_NAME, null, repositoryHelper, serviceName, serverName);
    }

    private void createSchemaAttribute(String userId, SchemaType schemaType, String schemaTypeGUID, Attribute attribute,
                                       String dataType, String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                                          PropertyServerException {
        final String methodName = "createSchemaAttribute";
        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        schemaTypeBuilder.setDataType(dataType);
        schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        schemaAttributeHandler.createNestedSchemaAttribute(userId, externalSourceGUID,
                externalSourceName, schemaTypeGUID, schemaTypeGUIDParameterName, TABULAR_SCHEMA_TYPE_TYPE_NAME,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                attribute.getQualifiedName(), qualifiedNameParameterName, schemaAttributeBuilder, methodName);
    }

    private EntityDetail buildSchemaTypeEntityDetail(String schemaTypeGUID, SchemaType schemaType) throws InvalidParameterException {
        String methodName = "buildSchemaTypeEntityDetail";

        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        return dataEngineCommonHandler.buildEntityDetail(schemaTypeGUID, schemaTypeBuilder.getInstanceProperties(methodName));
    }

    SchemaTypeBuilder getSchemaTypeBuilder(SchemaType schemaType) {
        return new SchemaTypeBuilder(schemaType.getQualifiedName(), schemaType.getDisplayName(), null,
                schemaType.getVersionNumber(), false, schemaType.getAuthor(), schemaType.getUsage(),
                schemaType.getEncodingStandard(), null, null,
                TABULAR_SCHEMA_TYPE_TYPE_GUID, TABULAR_SCHEMA_TYPE_TYPE_NAME,
                null, repositoryHelper, serviceName, serverName);
    }

    private Set<String> getSchemaAttributesForSchemaType(String userId, String schemaTypeGUID) throws UserNotAuthorizedException,
                                                                                                      PropertyServerException,
                                                                                                      InvalidParameterException {
        final String methodName = "getSchemaAttributesForSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForRelationshipType(userId, schemaTypeGUID, SCHEMA_TYPE_TYPE_NAME,
                relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), 0, 0, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().map(InstanceHeader::getGUID).collect(Collectors.toSet());
    }
}