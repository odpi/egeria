/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.NoSchemaAttributeException;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DataEngineSchemaTypeHandler manages schema types objects from the property server. It runs server-side in the
 * DataEngine OMAS and creates and retrieves schema type entities through the OMRSRepositoryConnector.
 */
public class DataEngineSchemaTypeHandler {
    private static final String TYPE_SUFFIX = "_type";
    private static final String SEPARATOR = "::";
    private static final String EQUALS = "=";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";

    private static final Logger log = LoggerFactory.getLogger(DataEngineSchemaTypeHandler.class);

    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final SchemaTypeHandler schemaTypeHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                       name of this service
     * @param serverName                        name of the local server
     * @param invalidParameterHandler           handler for managing parameter errors
     * @param repositoryHandler                 manages calls to the repository services
     * @param repositoryHelper                  provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler     provides calls for retrieving external data engine guid
     */
    public DataEngineSchemaTypeHandler(String serviceName, String serverName,
                                       InvalidParameterHandler invalidParameterHandler,
                                       RepositoryHandler repositoryHandler,
                                       OMRSRepositoryHelper repositoryHelper,
                                       SchemaTypeHandler schemaTypeHandler,
                                       DataEngineRegistrationHandler dataEngineRegistrationHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.schemaTypeHandler = schemaTypeHandler;
        this.dataEngineRegistrationHandler= dataEngineRegistrationHandler;
    }

    /**
     * Create the schema type entity, with the corresponding schema attributes and relationships
     *
     * @param userId           the name of the calling user
     * @param qualifiedName    the qualifiedName name of the schema type
     * @param displayName      the display name of the schema type
     * @param author           the author of the schema type
     * @param encodingStandard the encoding for the schema type
     * @param usage            the usage for the schema type
     * @param versionNumber    the version number for the schema type
     * @param attributeList    the list of attributes for the schema type
     * @param externalSourceName     the unique name of the external source
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createOrUpdateSchemaType(String userId, String qualifiedName, String displayName, String author,
                                           String encodingStandard, String usage, String versionNumber,
                                           List<Attribute> attributeList, String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "createOrUpdateSchemaType";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);

        SchemaType newSchemaType = createTabularSchemaType(qualifiedName, displayName, author, encodingStandard,
                usage, versionNumber);

        Map<SchemaAttribute, SchemaType> newSchemaAttributes = createSchemaAttributes(attributeList);

        String newSchemaTypeGUID = schemaTypeHandler.saveExternalSchemaType(userId, newSchemaType,
                new ArrayList<>(newSchemaAttributes.keySet()),externalSourceGUID,externalSourceName, methodName);

        //TODO update SchemaTypeHandler to save attributes along with the attribute schema types and
        // SchemaAttributeType relationships
        saveAttributeSchemaTypes(userId, newSchemaAttributes, externalSourceName);

        return newSchemaTypeGUID;
    }

    private void saveAttributeSchemaTypes(String userId, Map<SchemaAttribute, SchemaType> newSchemaAttributes,
                                          String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "saveAttributeSchemaTypes";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        for (Map.Entry<SchemaAttribute, SchemaType> schemaAttribute : newSchemaAttributes.entrySet()) {
            String attributeSchemaTypeGUID = schemaTypeHandler.saveExternalSchemaType(userId, schemaAttribute.getValue(),
                    null, externalSourceGUID, externalSourceName, methodName);

            String schemaAttributeGUID = findSchemaAttribute(userId, schemaAttribute.getKey().getQualifiedName());

            TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                    SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME);

            Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, schemaAttributeGUID,
                    SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, attributeSchemaTypeGUID,
                    relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), methodName);

            if (relationship == null) {
                repositoryHandler.createExternalRelationship(userId, relationshipTypeDef.getGUID(),externalSourceGUID,
                        externalSourceName, schemaAttributeGUID, attributeSchemaTypeGUID, null, methodName);
            }
        }
    }

    /**
     * Create LineageMapping relationship between two schema attribute types
     *
     * @param userId                             the name of the calling user
     * @param sourceSchemaAttributeQualifiedName the qualified name of the source schema attribute
     * @param targetSchemaAttributeQualifiedName the qualified name of the target schema attribute
     * @param externalSourceName     the unique name of the external source
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addLineageMappingRelationship(String userId,
                                              String sourceSchemaAttributeQualifiedName,
                                              String targetSchemaAttributeQualifiedName,
                                              String externalSourceName) throws
                                                                                         InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException,
                                                                                         NoSchemaAttributeException {
        final String methodName = "addLineageMappingRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceSchemaAttributeQualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(targetSchemaAttributeQualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        SchemaType sourceSchemaType = getSchemaTypeForSchemaAttribute(userId, sourceSchemaAttributeQualifiedName);
        SchemaType targetSchemaType = getSchemaTypeForSchemaAttribute(userId, targetSchemaAttributeQualifiedName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_NAME);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, sourceSchemaType.getGUID(),
                SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME, targetSchemaType.getGUID(), relationshipTypeDef.getGUID(),
                relationshipTypeDef.getName(), methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        if (relationship == null) {
            repositoryHandler.createExternalRelationship(userId, relationshipTypeDef.getGUID(),externalSourceGUID, externalSourceName,
                    sourceSchemaType.getGUID(), targetSchemaType.getGUID(), null, methodName);
        }
    }

    /**
     * Remove the schema type, the associated schema attributes and the attribute's schema types
     *
     * @param userId         the name of the calling user
     * @param schemaTypeGUID the unique identifier of the schema type
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeSchemaType(String userId, String schemaTypeGUID) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException {
        final String methodName = "removeSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, SchemaTypePropertiesMapper.GUID_PROPERTY_NAME,
                methodName);

        Set<String> oldSchemaAttributeGUIDs = getSchemaAttributesForSchemaType(userId, schemaTypeGUID);

        for (String oldSchemaAttributeGUID : oldSchemaAttributeGUIDs) {
            SchemaType attributeSchemaType = schemaTypeHandler.getSchemaTypeForAttribute(userId, oldSchemaAttributeGUID,
                    methodName);

            removeSchemaAttribute(userId, oldSchemaAttributeGUID);
            schemaTypeHandler.removeSchemaType(userId, attributeSchemaType.getGUID());
        }

        schemaTypeHandler.removeSchemaType(userId, schemaTypeGUID);
    }

    private SchemaType getSchemaTypeForSchemaAttribute(String userId, String schemaAttributeQualifiedName) throws
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException,
                                                                                                           NoSchemaAttributeException,
                                                                                                           InvalidParameterException {
        final String methodName = "getSchemaTypeForSchemaAttribute";

        String schemaAttributeGUID = findSchemaAttribute(userId, schemaAttributeQualifiedName);

        if (schemaAttributeGUID == null) {
            throwNoSchemaAttributeException(schemaAttributeQualifiedName);
        }

        return schemaTypeHandler.getSchemaTypeForAttribute(userId, schemaAttributeGUID, methodName);
    }

    private void throwNoSchemaAttributeException(String qualifiedName) throws NoSchemaAttributeException {
        final String methodName = "throwNoSchemaAttributeException";

        DataEngineErrorCode errorCode = DataEngineErrorCode.NO_SCHEMA_ATTRIBUTE;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(qualifiedName);

        throw new NoSchemaAttributeException(errorCode.getHTTPErrorCode(), this.getClass().getName(), methodName,
                errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), qualifiedName);
    }

    private String findSchemaAttribute(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException {
        final String methodName = "findSchemaAttribute";

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, entityTypeDef.getGUID(),
                entityTypeDef.getName(), methodName);

        if (retrievedEntity == null) {
            log.debug("Searching for entity with qualifiedName: {}. Result is null", qualifiedName);
            return null;
        }
        log.debug("Searching for entity with qualifiedName: {}. Result is {}", qualifiedName,
                retrievedEntity.getGUID());

        return retrievedEntity.getGUID();
    }

    private Set<String> getSchemaAttributesForSchemaType(String userId, String schemaTypeGUID) throws
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException,
                                                                                               InvalidParameterException {
        final String methodName = "getSchemaAttributesForSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, SchemaTypePropertiesMapper.GUID_PROPERTY_NAME,
                methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId,
                SchemaElementMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForRelationshipType(userId, schemaTypeGUID,
                SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME, relationshipTypeDef.getGUID(),
                relationshipTypeDef.getName(), 0, 0, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().map(InstanceHeader::getGUID).collect(Collectors.toSet());
    }

    /**
     * Remove the schema attribute
     *
     * @param userId              the name of the calling user
     * @param schemaAttributeGUID the unique identifier of the schemaType to be removed
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void removeSchemaAttribute(String userId, String schemaAttributeGUID) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException {
        final String methodName = "removeSchemaAttribute";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, SchemaTypePropertiesMapper.GUID_PROPERTY_NAME,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME);

        repositoryHandler.removeEntity(userId, schemaAttributeGUID, entityTypeDef.getGUID(), entityTypeDef.getName(),
                null, null, methodName);
    }

    private Map<SchemaAttribute, SchemaType> createSchemaAttributes(List<Attribute> attributeList) throws
                                                                                                   InvalidParameterException {
        final String methodName = "createSchemaAttributes";

        Map<SchemaAttribute, SchemaType> schemaAttributes = new HashMap<>();

        for (Attribute attribute : attributeList) {
            SchemaAttribute schemaAttribute = schemaTypeHandler.getEmptySchemaAttribute();

            String qualifiedName = attribute.getQualifiedName();
            String displayName = attribute.getDisplayName();

            invalidParameterHandler.validateName(qualifiedName, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                    methodName);
            invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                    methodName);

            schemaAttribute.setQualifiedName(qualifiedName);
            schemaAttribute.setAttributeName(displayName);
            schemaAttribute.setCardinality(attribute.getCardinality());
            schemaAttribute.setElementPosition(attribute.getElementPosition());
            schemaAttribute.setDefaultValueOverride(attribute.getDefaultValueOverride());

            SchemaType tabularColumnType = createTabularColumnType(attribute);

            schemaAttributes.put(schemaAttribute, tabularColumnType);
        }

        return schemaAttributes;
    }

    private SchemaType createTabularColumnType(Attribute attribute) throws InvalidParameterException {
        final String methodName = "createTabularColumnType";

        PrimitiveSchemaType schemaType = schemaTypeHandler.getEmptyPrimitiveSchemaType(
                SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME);

        String displayName = attribute.getDisplayName();
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);
        displayName += TYPE_SUFFIX;

        schemaType.setDisplayName(displayName);
        schemaType.setQualifiedName(buildPrimitiveSchemaTypeQualifiedName(attribute.getQualifiedName(),
                attribute.getDisplayName()));
        schemaType.setDataType(attribute.getDataType());
        schemaType.setDefaultValue(attribute.getDefaultValue());

        return schemaType;
    }

    private String buildPrimitiveSchemaTypeQualifiedName(String qualifiedName, String attributeName) {
        return qualifiedName + SEPARATOR + OPEN_BRACKET + SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME
                + CLOSE_BRACKET + EQUALS + attributeName + TYPE_SUFFIX;
    }

    private SchemaType createTabularSchemaType(String qualifiedName, String displayName, String author,
                                               String encodingStandard, String usage, String versionNumber) {
        ComplexSchemaType schemaType = schemaTypeHandler.getEmptyComplexSchemaType(
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME);

        schemaType.setQualifiedName(qualifiedName);
        schemaType.setDisplayName(displayName);
        schemaType.setAuthor(author);
        schemaType.setEncodingStandard(encodingStandard);
        schemaType.setUsage(usage);
        schemaType.setVersionNumber(versionNumber);

        return schemaType;
    }
}