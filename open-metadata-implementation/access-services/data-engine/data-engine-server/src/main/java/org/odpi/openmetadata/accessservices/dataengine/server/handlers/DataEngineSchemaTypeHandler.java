/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.SchemaAttributePropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @return unique identifier of the schema type in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertSchemaType(String userId, SchemaType schemaType, String externalSourceName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(schemaType.getQualifiedName(), SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(schemaType.getDisplayName(), SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> originalSchemaTypeEntity = findSchemaTypeEntity(userId, schemaType.getQualifiedName());

        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        String schemaTypeGUID;
        if (!originalSchemaTypeEntity.isPresent()) {
            schemaTypeGUID = schemaTypeHandler.addSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeBuilder, methodName);
        } else {
            schemaTypeGUID = originalSchemaTypeEntity.get().getGUID();
            EntityDetail updatedSchemaTypeEntity = buildSchemaTypeEntityDetail(schemaTypeGUID, schemaType);
            EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalSchemaTypeEntity.get(),
                    updatedSchemaTypeEntity, true);

            if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                schemaTypeHandler.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID,
                        SCHEMA_TYPE_GUID_PARAMETER_NAME, schemaTypeBuilder);
            }
        }

        upsertSchemaAttributes(userId, schemaType, schemaTypeGUID, schemaType.getAttributeList(), externalSourceName);

        return schemaTypeGUID;
    }

    /**
     * Find out if the SchemaType object is already stored in the repository. It uses the fully qualified name to retrieve the entity
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
    public Optional<EntityDetail> findSchemaTypeEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                   PropertyServerException,
                                                                                                   InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, SchemaTypePropertiesMapper.SCHEMA_TYPE_TYPE_NAME);
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
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, SchemaTypePropertiesMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);
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
        invalidParameterHandler.validateName(sourceSchemaAttributeQualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(targetSchemaAttributeQualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

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
                targetSchemaAttributeEntity.get().getGUID(), SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME,
                SchemaTypePropertiesMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, externalSourceName, null);
    }

    /**
     * Remove the schema type with the associated schema attributes
     *
     * @param userId         the name of the calling user
     * @param schemaTypeGUID the unique identifier of the schema type
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeSchemaType(String userId, String schemaTypeGUID, String externalSourceName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "removeSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, SchemaTypePropertiesMapper.GUID_PROPERTY_NAME, methodName);

        Set<String> schemaAttributeGUIDs = getSchemaAttributesForSchemaType(userId, schemaTypeGUID);

        for (String schemaAttributeGUID : schemaAttributeGUIDs) {
            dataEngineCommonHandler.removeEntity(userId, schemaAttributeGUID, SchemaTypePropertiesMapper.TABULAR_COLUMN_TYPE_NAME,
                    externalSourceName);
        }
        dataEngineCommonHandler.removeEntity(userId, schemaTypeGUID, SchemaTypePropertiesMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                externalSourceName);
    }

    /**
     * Updates the schema attribute with anchorGUID property set to process GUID
     *
     * @param userId      the name of the calling user
     * @param attribute   the properties of the schema attribute
     * @param processGUID the GUID of the process
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addAnchorGUID(String userId, Attribute attribute, String processGUID, String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException, PropertyServerException {
        final String methodName = "addAnchorGUID";

        Optional<EntityDetail> schemaAttributeEntity = findSchemaAttributeEntity(userId, attribute.getQualifiedName());
        if (!schemaAttributeEntity.isPresent()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.SCHEMA_ATTRIBUTE_NOT_FOUND, methodName,
                    attribute.getQualifiedName());
        } else {
            attribute.setAnchorGUID(processGUID);

            String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

            schemaAttributeHandler.updateSchemaAttribute(userId, externalSourceGUID, externalSourceName,
                    schemaAttributeEntity.get().getGUID(), getSchemaAttributeBuilder(attribute).getInstanceProperties(methodName));
        }
    }

    private void upsertSchemaAttributes(String userId, SchemaType schemaType, String schemaTypeGUID,
                                        List<Attribute> attributeList, String externalSourceName) throws InvalidParameterException,
            PropertyServerException, UserNotAuthorizedException {

        String methodName = "upsertSchemaAttributes";
        for (Attribute attribute : attributeList) {

            Optional<EntityDetail> schemaAttributeEntity = findSchemaAttributeEntity(userId, attribute.getQualifiedName());

            if (!schemaAttributeEntity.isPresent()) {
                createSchemaAttribute(userId, schemaType, schemaTypeGUID, attribute, attribute.getDataType(), externalSourceName);
            } else {
                String schemaAttributeGUID = schemaAttributeEntity.get().getGUID();
                EntityDetail updatedSchemaAttributeEntity = buildSchemaAttributeEntityDetail(schemaAttributeGUID, attribute);
                EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity.get(),
                        updatedSchemaAttributeEntity, true);

                if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                    String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);
                    schemaAttributeHandler.updateSchemaAttribute(userId, externalSourceGUID, externalSourceName,
                            schemaAttributeGUID, getSchemaAttributeBuilder(attribute).getInstanceProperties(methodName));
                }
            }
        }
    }


    private EntityDetail buildSchemaAttributeEntityDetail(String schemaAttributeGUID, Attribute attribute) throws
            InvalidParameterException {
        String methodName = "buildSchemaAttributeEntityDetail";
        SchemaAttributePropertiesBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);

        return dataEngineCommonHandler.buildEntityDetail(schemaAttributeGUID, schemaAttributeBuilder.getInstanceProperties(methodName));
    }

    SchemaAttributePropertiesBuilder getSchemaAttributeBuilder(Attribute attribute) {
        HashMap<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME, attribute.getAnchorGUID());
        return new SchemaAttributePropertiesBuilder(attribute.getQualifiedName(), attribute.getDisplayName(), null,
                attribute.getPosition(), attribute.getMinCardinality(), attribute.getMaxCardinality(), false,
                attribute.getDefaultValueOverride(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                0, 0, 0, 0,
                false, null, null, additionalProperties,
                OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID, OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME,
                null, repositoryHelper, serviceName, serverName, attribute.getAnchorGUID());
    }

    private void createSchemaAttribute(String userId, SchemaType schemaType, String schemaTypeGUID, Attribute attribute,
                                       String dataType, String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "createSchemaAttribute";
        SchemaAttributePropertiesBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);
        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);
        schemaTypeBuilder.setDataType(dataType);
        schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        schemaAttributeHandler.createNestedSchemaAttribute(userId, externalSourceGUID,
                externalSourceName, schemaTypeGUID, schemaTypeGUIDParameterName,
                OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
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
                SchemaTypePropertiesMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID, SchemaTypePropertiesMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                null, repositoryHelper, serviceName, serverName);
    }

    private Set<String> getSchemaAttributesForSchemaType(String userId, String schemaTypeGUID) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {
        final String methodName = "getSchemaAttributesForSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, SchemaTypePropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, SchemaTypePropertiesMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForRelationshipType(userId, schemaTypeGUID,
                SchemaTypePropertiesMapper.SCHEMA_TYPE_TYPE_NAME, relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), 0, 0, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().map(InstanceHeader::getGUID).collect(Collectors.toSet());
    }
}