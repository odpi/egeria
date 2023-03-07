/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FLOW_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

/**
 * DataEngineSchemaTypeHandler manages schema types objects from the property server. It runs server-side in the
 * DataEngine OMAS and creates and retrieves schema type entities through the OMRSRepositoryConnector.
 */
public class DataEngineSchemaTypeHandler {
    public static final String SCHEMA_TYPE_GUID_PARAMETER_NAME = "schemaTypeGUID";
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final SchemaTypeHandler<SchemaType> schemaTypeHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                      name of this service
     * @param serverName                       name of the local server
     * @param invalidParameterHandler          handler for managing parameter errors
     * @param repositoryHelper                 provides utilities for manipulating the repository services objects
     * @param schemaTypeHandler                handler for managing schema elements in the metadata repositories
     * @param dataEngineRegistrationHandler    provides calls for retrieving external data engine guid
     * @param dataEngineCommonHandler          provides utilities for manipulating entities
     * @param dataEngineSchemaAttributeHandler provides utilities for manipulating schema attributes
     */
    public DataEngineSchemaTypeHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                       OMRSRepositoryHelper repositoryHelper, SchemaTypeHandler<SchemaType> schemaTypeHandler,
                                       DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                       DataEngineCommonHandler dataEngineCommonHandler,
                                       DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.schemaTypeHandler = schemaTypeHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.dataEngineSchemaAttributeHandler = dataEngineSchemaAttributeHandler;
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
        String methodName = "upsertSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(schemaType.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(schemaType.getDisplayName(), DISPLAY_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> originalSchemaTypeEntity = findSchemaTypeEntity(userId, schemaType.getQualifiedName());

        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        String schemaTypeGUID;
        Date now = dataEngineCommonHandler.getNow();
        if (originalSchemaTypeEntity.isEmpty()) {
            schemaTypeGUID = schemaTypeHandler.addSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeBuilder,
                      null, null, false, false, now, methodName);
        } else {
            schemaTypeGUID = originalSchemaTypeEntity.get().getGUID();
            EntityDetail updatedSchemaTypeEntity = buildSchemaTypeEntityDetail(schemaTypeGUID, schemaType);
            EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalSchemaTypeEntity.get(),
                    updatedSchemaTypeEntity, true);

            if (entityDetailDifferences.hasInstancePropertiesDifferences()) {
                schemaTypeHandler.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, SCHEMA_TYPE_GUID_PARAMETER_NAME,
                        schemaTypeBuilder, true, false, false, now, methodName);
            }
        }

        dataEngineSchemaAttributeHandler.upsertSchemaAttributes(userId, schemaType.getAttributeList(), externalSourceName, externalSourceGUID,
                schemaTypeGUID);

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
     * Create DataFlow relationship between two entities
     *
     * @param userId              the name of the calling user
     * @param dataSupplierQualifiedName the qualified name of the data supplier entity
     * @param dataConsumerQualifiedName the qualified name of the data consumer entity
     * @param externalSourceName  the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addDataFlowRelationship(String userId, String dataSupplierQualifiedName, String dataConsumerQualifiedName, String externalSourceName,
                                        String formula, String description)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        String methodName = "addDataFlowRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(dataSupplierQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(dataConsumerQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> dataSupplierEntity = getDataFlowEntity(userId, dataSupplierQualifiedName);
        Optional<EntityDetail> dataconsumertEntity = getDataFlowEntity(userId, dataConsumerQualifiedName);

        if (dataSupplierEntity.isEmpty()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.REFERENCEABLE_NOT_FOUND, methodName,
                    dataSupplierQualifiedName);
            return;
        }
        if (dataconsumertEntity.isEmpty()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.REFERENCEABLE_NOT_FOUND, methodName,
                    dataConsumerQualifiedName);
            return;
        }

        InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName, relationshipProperties,
                OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME, formula, methodName);

        dataEngineCommonHandler.upsertExternalRelationship(userId, dataSupplierEntity.get().getGUID(), dataconsumertEntity.get().getGUID(),
                DATA_FLOW_TYPE_NAME, dataSupplierEntity.get().getType().getTypeDefName(),
                dataconsumertEntity.get().getType().getTypeDefName(), externalSourceName, relationshipProperties);
    }

    /**
     * Returns the entity used to create the data flow. If the entity is of type TabularSchemaType, then it will return the attached
     * Asset
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the entity
     *
     * @return An optional containing the entity for which to create the data flow, or an empty optional
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Optional<EntityDetail> getDataFlowEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                 PropertyServerException,
                                                                                                 InvalidParameterException {
        Optional<EntityDetail> referenceableEntity = dataEngineCommonHandler.findEntity(userId, qualifiedName, REFERENCEABLE_TYPE_NAME);

        if (referenceableEntity.isPresent()) {
            EntityDetail entityDetail = referenceableEntity.get();

            if (TABULAR_SCHEMA_TYPE_TYPE_NAME.equalsIgnoreCase(entityDetail.getType().getTypeDefName())) {
                return dataEngineCommonHandler.getEntityForRelationship(userId, entityDetail.getGUID(),
                        ASSET_TO_SCHEMA_TYPE_TYPE_NAME, TABULAR_SCHEMA_TYPE_TYPE_NAME);
            }
        }

        return referenceableEntity;
    }

    /**
     * Remove the schema type with the associated schema attributes
     *
     * @param userId             the name of the calling user
     * @param schemaTypeGUID     the unique identifier of the schema type
     * @param externalSourceName the external data engine
     * @param deleteSemantic     the delete semantic
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
        String methodName = "removeSchemaType";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, GUID_PROPERTY_NAME, methodName);

        // remove the tabular columns manually, because schemaTypeHandler.removeSchemaType does not remove the columns
        Set<String> schemaAttributeGUIDs = getSchemaAttributesForSchemaType(userId, schemaTypeGUID);
        for (String schemaAttributeGUID : schemaAttributeGUIDs) {
            dataEngineCommonHandler.removeEntity(userId, schemaAttributeGUID, TABULAR_COLUMN_TYPE_NAME, externalSourceName);
        }
        dataEngineCommonHandler.removeEntity(userId, schemaTypeGUID, TABULAR_SCHEMA_TYPE_TYPE_NAME, externalSourceName);
    }

    private Set<String> getSchemaAttributesForSchemaType(String userId, String schemaTypeGUID) throws UserNotAuthorizedException,
                                                                                                      PropertyServerException,
                                                                                                      InvalidParameterException {
        Set<EntityDetail> entities = dataEngineCommonHandler.getEntitiesForRelationship(userId, schemaTypeGUID,
                TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, SCHEMA_ATTRIBUTE_TYPE_NAME, SCHEMA_TYPE_TYPE_NAME);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().map(InstanceHeader::getGUID).collect(Collectors.toSet());
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
}
