/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;

/**
 * DataEngineSchemaAttributeHandler manages schema attributes objects from the property server. It runs server-side in the
 * DataEngine OMAS and creates and retrieves schema type entities through the OMRSRepositoryConnector.
 */
public class DataEngineSchemaAttributeHandler {
    protected static final String SCHEMA_TYPE_GUID_PARAMETER_NAME = "schemaTypeGUID";
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
    private Optional<EntityDetail> findSchemaAttributeEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                        PropertyServerException,
                                                                                                        InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, SCHEMA_ATTRIBUTE_TYPE_NAME);
    }

    /**
     * Creates or updates the list of schema attributes
     *
     * @param userId             the name of the calling user
     * @param attributeList      the list of attributes
     * @param externalSourceName the unique name of the external source
     * @param externalSourceGUID the guid of the external source
     * @param schemaTypeGUID     the guid of the schema type
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void upsertSchemaAttributes(String userId, List<Attribute> attributeList, String externalSourceName, String externalSourceGUID,
                                       String schemaTypeGUID) throws UserNotAuthorizedException, PropertyServerException,
                                                                                      InvalidParameterException {
        if (CollectionUtils.isEmpty(attributeList)) {
            return;
        }
        for (Attribute tabularColumn : attributeList) {
            Optional<EntityDetail> schemaAttributeEntity = findSchemaAttributeEntity(userId, tabularColumn.getQualifiedName());
            if (schemaAttributeEntity.isEmpty()) {
                createSchemaAttribute(userId, schemaTypeGUID, tabularColumn, externalSourceName);
            } else {
                String schemaAttributeGUID = schemaAttributeEntity.get().getGUID();
                EntityDetail updatedSchemaAttributeEntity = buildSchemaAttributeEntityDetail(schemaAttributeGUID, tabularColumn);
                EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(schemaAttributeEntity.get(),
                        updatedSchemaAttributeEntity, true);

                if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                    updateSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID, tabularColumn);
                }
            }
        }
    }

    private EntityDetail buildSchemaAttributeEntityDetail(String schemaAttributeGUID, Attribute attribute) throws InvalidParameterException {
        String methodName = "buildSchemaAttributeEntityDetail";
        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);

        return dataEngineCommonHandler.buildEntityDetail(schemaAttributeGUID, schemaAttributeBuilder.getInstanceProperties(methodName));
    }

    private SchemaAttributeBuilder getSchemaAttributeBuilder(Attribute attribute) {
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

    private void createSchemaAttribute(String userId, String schemaTypeGUID, Attribute attribute, String externalSourceName) throws
                                                                                                                             InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException {
        final String methodName = "createSchemaAttribute";

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        schemaAttributeHandler.createNestedSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaTypeGUID,
                 SCHEMA_TYPE_GUID_PARAMETER_NAME, attribute.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, attribute.getDisplayName(),
                 attribute.getDescription(), attribute.getExternalTypeGUID(), attribute.getDataType(), attribute.getDefaultValue(),
                 attribute.getFixedValue(), attribute.getValidValuesSetGUID(), null, attribute.getIsDeprecated(), attribute.getPosition(),
                 attribute.getMinCardinality(), attribute.getMaxCardinality(), attribute.getAllowsDuplicateValues(), attribute.getOrderedValues(),
                 attribute.getDefaultValueOverride(), dataEngineCommonHandler.getSortOrder(attribute), attribute.getMinimumLength(),
                 attribute.getLength(), attribute.getPrecision(), attribute.getIsNullable(), attribute.getNativeClass(), attribute.getAliases(),
                 attribute.getAdditionalProperties(), attribute.getTypeName() != null ? attribute.getTypeName() : TABULAR_COLUMN_TYPE_NAME,
                 null, null, null, false, false,
                dataEngineCommonHandler.getNow(), methodName);
    }

    private void updateSchemaAttribute(String userId, String externalSourceGUID, String externalSourceName, String schemaAttributeGUID,
                                       Attribute attribute) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "updateSchemaAttribute";

        SchemaAttributeBuilder schemaAttributeBuilder = getSchemaAttributeBuilder(attribute);
        schemaAttributeHandler.updateSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID,
                schemaAttributeBuilder.getInstanceProperties(methodName), false, false, dataEngineCommonHandler.getNow(), methodName);
    }
}
