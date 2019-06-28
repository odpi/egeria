/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Column;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final String serviceName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final SchemaTypeHandler schemaTypeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public DataEngineSchemaTypeHandler(String serviceName, String serverName,
                                       InvalidParameterHandler invalidParameterHandler,
                                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

        schemaTypeHandler = new SchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                repositoryHelper);
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
     * @param columnList       the list of columns for the schema type.
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createSchemaType(String userId, String qualifiedName, String displayName, String author,
                                   String encodingStandard, String usage, String versionNumber,
                                   List<Column> columnList) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException {
        final String methodName = "createSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);

        SchemaType newSchemaType = createTabularSchemaType(qualifiedName, displayName, author, encodingStandard,
                usage, versionNumber);

        Map<SchemaAttribute, SchemaType> newSchemaAttributes = createSchemaAttributes(columnList);

        String newSchemaTypeGUID = schemaTypeHandler.saveSchemaType(userId, newSchemaType,
                new ArrayList<>(newSchemaAttributes.keySet()), methodName);

        //TODO update SchemaTypeHandler to save attributes along with the attribute schema types and
        // SchemaAttributeType relationships
        saveAttributeSchemaTypes(userId, newSchemaAttributes);

        return newSchemaTypeGUID;
    }

    private void saveAttributeSchemaTypes(String userId, Map<SchemaAttribute, SchemaType> newSchemaAttributes) throws
                                                                                                               InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException {
        final String methodName = "saveAttributeSchemaTypes";

        for (SchemaAttribute schemaAttribute : newSchemaAttributes.keySet()) {
            String attributeSchemaTypeGUID = schemaTypeHandler.saveSchemaType(userId,
                    newSchemaAttributes.get(schemaAttribute), null, methodName);

            String schemaAttributeGUID = findSchemaAttribute(userId, schemaAttribute.getQualifiedName());

            repositoryHandler.createRelationship(userId, SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                    schemaAttributeGUID, attributeSchemaTypeGUID, null, methodName);
        }
    }

    /**
     * Create LineageMapping relationship between two schema attribute types
     *
     * @param userId                             the name of the calling user
     * @param sourceSchemaAttributeQualifiedName the qualified name of the source schema attribute
     * @param targetSchemaAttributeQualifiedName the qualified name of the target schema attribute
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addLineageMappingRelationship(String userId, String sourceSchemaAttributeQualifiedName,
                                              String targetSchemaAttributeQualifiedName) throws
                                                                                         InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException {
        final String methodName = "addLineageMappingRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceSchemaAttributeQualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(targetSchemaAttributeQualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        String sourceSchemaAttributeGUID = findSchemaAttribute(userId, sourceSchemaAttributeQualifiedName);
        String targetSchemaAttributeGUID = findSchemaAttribute(userId, targetSchemaAttributeQualifiedName);

        SchemaType sourceSchemaType = schemaTypeHandler.getSchemaTypeForAttribute(userId, sourceSchemaAttributeGUID,
                methodName);
        SchemaType targetSchemaType = schemaTypeHandler.getSchemaTypeForAttribute(userId, targetSchemaAttributeGUID,
                methodName);

        repositoryHandler.createRelationship(userId, SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID,
                sourceSchemaType.getGUID(), targetSchemaType.getGUID(), null, methodName);
    }

    private String findSchemaAttribute(String userId, String qualifiedName) throws
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException {
        final String methodName = "findSchemaAttribute";

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME, methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
    }

    private Map<SchemaAttribute, SchemaType> createSchemaAttributes(List<Column> columnList) throws
                                                                                             org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        final String methodName = "createSchemaAttributes";

        Map<SchemaAttribute, SchemaType> schemaAttributes = new HashMap<>();

        for (Column column : columnList) {
            SchemaAttribute schemaAttribute = schemaTypeHandler.getEmptySchemaAttribute();

            String qualifiedName = column.getQualifiedName();
            String displayName = column.getDisplayName();

            invalidParameterHandler.validateName(qualifiedName, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                    methodName);
            invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                    methodName);

            schemaAttribute.setQualifiedName(qualifiedName);
            schemaAttribute.setAttributeName(displayName);
            schemaAttribute.setCardinality(column.getCardinality());
            schemaAttribute.setElementPosition(column.getElementPosition());
            schemaAttribute.setDefaultValueOverride(column.getDefaultValueOverride());

            SchemaType tabularColumnType = createTabularColumnType(column);

            schemaAttributes.put(schemaAttribute, tabularColumnType);
        }

        return schemaAttributes;
    }

    private SchemaType createTabularColumnType(Column column) throws
                                                              org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        final String methodName = "createTabularColumnType";

        PrimitiveSchemaType schemaType = schemaTypeHandler.getEmptyPrimitiveSchemaType(
                SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME);

        String displayName = column.getDisplayName();
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);
        displayName += TYPE_SUFFIX;

        schemaType.setDisplayName(displayName);
        schemaType.setQualifiedName(buildPrimitiveSchemaTypeQualifiedName(column.getQualifiedName(),
                column.getDisplayName()));
        schemaType.setDataType(column.getDataType());
        schemaType.setDefaultValue(column.getDefaultValue());

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
