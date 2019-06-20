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


public class DataEngineSchemaTypeHandler {
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private SchemaTypeHandler schemaTypeHandler;

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
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

        schemaTypeHandler = new SchemaTypeHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                repositoryHelper);
    }


    public String createSchemaType(String userId, String qualifiedName, String displayName, String author,
                                   String encodingStandard, String usage, List<Column> columnList) throws
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException {
        final String methodName = "createSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);


        //TODO check displayName issue when finding existing schema
        SchemaType newSchemaType = createTabularSchemaType(qualifiedName, qualifiedName + "::" + displayName,
                author, encodingStandard, usage);

        Map<SchemaAttribute, SchemaType> newSchemaAttributes = createSchemaAttributes(columnList);

        String newSchemaTypeGUID = schemaTypeHandler.saveSchemaType(userId, newSchemaType,
                new ArrayList<>(newSchemaAttributes.keySet()), methodName);

        //TODO update SchemaTypeHandler to save attributes along with the type schema types and
        // SchemaAttributeType relationship
        for (SchemaAttribute schemaAttribute : newSchemaAttributes.keySet()) {
            String schemaTypeGUID = schemaTypeHandler.saveSchemaType(userId, newSchemaAttributes.get(schemaAttribute),
                    null, methodName);

            String schemaAttributeGUID = findSchemaAttribute(userId, schemaAttribute.getQualifiedName());

            repositoryHandler.createRelationship(userId, SchemaElementMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                    schemaAttributeGUID, schemaTypeGUID, null, methodName);
        }

        return newSchemaTypeGUID;
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

        return retrievedEntity.getGUID();
    }

    private Map<SchemaAttribute, SchemaType> createSchemaAttributes(List<Column> columnList) {
        Map<SchemaAttribute, SchemaType> schemaAttributes = new HashMap<>();

        for (Column column : columnList) {
            SchemaAttribute schemaAttribute = schemaTypeHandler.getEmptySchemaAttribute();

            schemaAttribute.setQualifiedName(column.getQualifiedName());
            schemaAttribute.setAttributeName(column.getAttributeName());
            schemaAttribute.setCardinality(column.getCardinality());
            schemaAttribute.setElementPosition(column.getElementPosition());
            schemaAttribute.setDefaultValueOverride(column.getDefaultValueOverride());
            SchemaType schemaAttributeType = createSchemaTypeAttribute(column);
            schemaAttributes.put(schemaAttribute, schemaAttributeType);
        }

        return schemaAttributes;
    }

    private SchemaType createSchemaTypeAttribute(Column column) {

        PrimitiveSchemaType schemaType = schemaTypeHandler.getEmptyPrimitiveSchemaType(
                SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_COLUMN_TYPE_TYPE_NAME);
        schemaType.setDataType(column.getDataType().name());
        schemaType.setDefaultValue(column.getDefaultValue());
        schemaType.setDisplayName(column.getQualifiedName() + "::" + "type" + column.getAttributeName());
        schemaType.setQualifiedName(column.getQualifiedName() + "::" + "type");

        return schemaType;
    }

    private SchemaType createTabularSchemaType(String qualifiedName, String displayName, String author,
                                               String encodingStandard, String usage) {
        ComplexSchemaType schemaType = schemaTypeHandler.getEmptyComplexSchemaType(
                SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID, SchemaElementMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME);

        schemaType.setQualifiedName(qualifiedName);
        schemaType.setDisplayName(displayName);
        schemaType.setAuthor(author);
        schemaType.setEncodingStandard(encodingStandard);
        schemaType.setUsage(usage);

        return schemaType;
    }

    public String findSchemaType(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                             PropertyServerException {
        final String methodName = "findSchemaType";

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                SchemaTypePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties,
                SchemaElementMapper.SCHEMA_TYPE_TYPE_GUID,
                SchemaElementMapper.SCHEMA_TYPE_TYPE_NAME, methodName);

        return retrievedEntity.getGUID();
    }

    public void addLineageMappingRelationship(String userId, String sourceSchemaTypeGUID,
                                              String targetSchemaTypeGUID) throws
                                                                           org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException {
        final String methodName = "addLineageMappingRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceSchemaTypeGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(targetSchemaTypeGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        repositoryHandler.createRelationship(userId, SchemaTypePropertiesMapper.LINEAGE_MAPPINGS_TYPE_GUID,
                sourceSchemaTypeGUID, targetSchemaTypeGUID, null, methodName);
    }
}
