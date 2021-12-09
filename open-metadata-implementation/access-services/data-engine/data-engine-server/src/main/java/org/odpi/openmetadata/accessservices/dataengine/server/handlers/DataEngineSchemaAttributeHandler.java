/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;

/**
 * DataEngineSchemaTypeHandler manages schema types objects from the property server. It runs server-side in the
 * DataEngine OMAS and creates and retrieves schema type entities through the OMRSRepositoryConnector.
 */
public class DataEngineSchemaAttributeHandler {
    public static final String SCHEMA_TYPE_GUID_PARAMETER_NAME = "schemaTypeGUID";
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     * @param dataEngineCommonHandler       provides utilities for manipulating entities
     * @param schemaAttributeHandler        handler for managing schema attributes in the metadata repositories
     */
    public DataEngineSchemaAttributeHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                            OMRSRepositoryHelper repositoryHelper,
                                            SchemaAttributeHandler<Attribute, SchemaType> schemaAttributeHandler,
                                            DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                            DataEngineCommonHandler dataEngineCommonHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.schemaAttributeHandler = schemaAttributeHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
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


    public EntityDetail buildSchemaAttributeEntityDetail(String schemaAttributeGUID, Attribute attribute) throws InvalidParameterException {
        String methodName = "buildSchemaAttributeEntityDetail";
        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);

        return dataEngineCommonHandler.buildEntityDetail(schemaAttributeGUID, schemaAttributeBuilder.getInstanceProperties(methodName));
    }

    public SchemaAttributeBuilder getSchemaAttributeBuilder(Attribute attribute) {
        HashMap<String, String> additionalProperties = new HashMap<>();
        return new SchemaAttributeBuilder(attribute.getQualifiedName(), attribute.getDisplayName(), attribute.getDescription(),
                attribute.getPosition(), attribute.getMinCardinality(), attribute.getMaxCardinality(), attribute.getIsDeprecated(),
                attribute.getDefaultValueOverride(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                dataEngineCommonHandler.getSortOrder(attribute), attribute.getMinimumLength(), attribute.getLength(), attribute.getPrecision(),
                attribute.getIsNullable(), attribute.getNativeClass(), attribute.getAliases(), additionalProperties,
                attribute.getTypeGuid() != null ? attribute.getTypeGuid() : TABULAR_COLUMN_TYPE_GUID,
                attribute.getTypeName() != null ? attribute.getTypeName() : TABULAR_COLUMN_TYPE_NAME,
                null, repositoryHelper, serviceName, serverName);
    }

    public void createSchemaAttribute(String userId, String schemaTypeGUID, Attribute attribute, String externalSourceName) throws
                                                                                                                            InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException {
        final String methodName = "createSchemaAttribute";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        int sortOrderOrdinal = ((attribute.getSortOrder()==null)?DataItemSortOrder.UNKNOWN:attribute.getSortOrder()).getOpenTypeOrdinal();
        schemaAttributeHandler.createNestedSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaTypeGUID,
                SCHEMA_TYPE_GUID_PARAMETER_NAME, attribute.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, attribute.getDisplayName(),
                attribute.getDescription(), attribute.getExternalTypeGUID(), attribute.getDataType(), attribute.getDefaultValue(),
                attribute.getFixedValue(), attribute.getValidValuesSetGUID(), null, attribute.getIsDeprecated(), attribute.getPosition(),
                attribute.getMinCardinality(), attribute.getMaxCardinality(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                attribute.getDefaultValueOverride(), sortOrderOrdinal, attribute.getMinimumLength(), attribute.getLength(), attribute.getPrecision(),
                attribute.getIsNullable(), attribute.getNativeClass(), attribute.getAliases(), attribute.getAdditionalProperties(),
                attribute.getTypeName(), null, methodName);
    }


    public void updateSchemaAttribute(String userId, String externalSourceGUID, String externalSourceName, String schemaAttributeGUID,
                                      Attribute attribute) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "createSchemaAttribute";

        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);
        schemaAttributeHandler.updateSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID,
                schemaAttributeBuilder.getInstanceProperties(methodName));
    }
}