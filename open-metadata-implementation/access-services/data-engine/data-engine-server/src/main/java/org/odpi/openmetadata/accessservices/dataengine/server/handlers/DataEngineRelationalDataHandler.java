/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATABASE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.INCOMPLETE_CLASSIFICATION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.INCOMPLETE_CLASSIFICATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME;

/**
 * DataEngineRelationalDataHandler manages Databases and RelationalTables objects from the property server.  It runs server-side in the DataEngine
 * OMAS and creates entities and relationships through the OMRSRepositoryConnector.
 */
public class DataEngineRelationalDataHandler {
    public static final String DATABASE_SCHEMA_GUID = "databaseSchemaGUID";
    public static final String DATABASE_GUID = "databaseGUID";
    private final String serviceName;
    private final String serverName;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn, SchemaType>
            relationalDataHandler;
    private final AssetHandler<DatabaseSchema> databaseSchemaAssetHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;
    private final DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                            name of this service
     * @param serverName                             name of the local server
     * @param invalidParameterHandler                handler for managing parameter errors
     * @param relationalDataHandler                  provides utilities for manipulating the repository services assets
     * @param dataEngineCommonHandler                provides utilities for manipulating entities
     * @param registrationHandler                    creates software server capability entities
     * @param dataEngineConnectionAndEndpointHandler provides utilities specific for manipulating Connections and Endpoints
     **/
    public DataEngineRelationalDataHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                           RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn,
                                                   SchemaType> relationalDataHandler, AssetHandler<DatabaseSchema> databaseSchemaAssetHandler,
                                           DataEngineRegistrationHandler registrationHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                           DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.relationalDataHandler = relationalDataHandler;
        this.databaseSchemaAssetHandler = databaseSchemaAssetHandler;
        this.registrationHandler = registrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.dataEngineConnectionAndEndpointHandler = dataEngineConnectionAndEndpointHandler;
    }

    /**
     * Create or update the database
     *
     * @param userId             the name of the calling user
     * @param database           the values of the database
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the database in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabase(String userId, Database database, String externalSourceName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException {
        final String methodName = "upsertDatabase";
        validateParameters(userId, methodName, database.getQualifiedName(), database.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseEntity = findDatabaseEntity(userId, database.getQualifiedName());

        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(database.getOwnerType());
        String databaseGUID;
        if (originalDatabaseEntity.isEmpty()) {
            databaseGUID = relationalDataHandler.createDatabase(userId, externalSourceGUID, externalSourceName, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(),
                    database.getPathName(), database.getCreateTime(), database.getModifiedTime(), database.getEncodingType(),
                    database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(), database.getDatabaseType(),
                    database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                    database.getAdditionalProperties(), DATABASE_TYPE_NAME, null,
                    null, methodName);
        } else {
            databaseGUID = originalDatabaseEntity.get().getGUID();
            relationalDataHandler.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(),
                    database.getCreateTime(), database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(),
                    database.getEncodingDescription(), database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(),
                    database.getDatabaseInstance(), database.getDatabaseImportedFrom(), database.getAdditionalProperties(),
                    DATABASE_TYPE_NAME, null, null, methodName);
        }

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(database.getQualifiedName(),
            databaseGUID, DATABASE_TYPE_NAME, database.getProtocol(), database.getNetworkAddress(),
            externalSourceGUID, externalSourceName, userId);

        return databaseGUID;
    }

    /**
     * Find out if the Database object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the database to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Optional<EntityDetail> findDatabaseEntity(String userId, String qualifiedName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, DATABASE_TYPE_NAME);
    }

    /**
     * Find out if the DatabaseSchema object is already stored in the repository.
     * It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the database schema to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Optional<EntityDetail> findDatabaseSchemaEntity(String userId, String qualifiedName) throws InvalidParameterException,
            PropertyServerException, UserNotAuthorizedException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
    }

    /**
     * Create or update the database schema
     *
     * @param userId             the name of the calling user
     * @param databaseGUID       the unique identifier of the database
     * @param databaseSchema     the values of the database schema
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabaseSchema(String userId, String databaseGUID, DatabaseSchema databaseSchema, boolean incomplete,
                                       String externalSourceName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "upsertDatabaseSchema";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(databaseSchema.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseSchemaEntity = dataEngineCommonHandler.findEntity(userId, databaseSchema.getQualifiedName(),
                DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        String databaseSchemaGUID;
        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(databaseSchema.getOwnerType());
        if (originalDatabaseSchemaEntity.isEmpty()) {
            databaseSchemaGUID = relationalDataHandler.createDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(), DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null, null, methodName);
        } else {
            databaseSchemaGUID = originalDatabaseSchemaEntity.get().getGUID();
            relationalDataHandler.updateDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(), DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null, null, methodName);

            if(StringUtils.isNotEmpty(databaseGUID)) {
                databaseSchemaAssetHandler.linkElementToElement(userId, externalSourceGUID, externalSourceName, databaseGUID,
                        DATABASE_GUID, OpenMetadataAPIMapper.DATABASE_TYPE_NAME, databaseSchemaGUID, DATABASE_SCHEMA_GUID,
                        OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, false, false,
                        OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                        null, methodName);
            }
        }

        if (incomplete) {
            databaseSchemaAssetHandler.setClassificationInRepository(userId, databaseSchemaGUID, DATABASE_SCHEMA_GUID,
                    DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, INCOMPLETE_CLASSIFICATION_TYPE_GUID, INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                    null, methodName);
        }

        return databaseSchemaGUID;
    }

    /**
     * Retrieve the schema type that is linked to the database
     *
     * @param userId       the name of the calling user
     * @param databaseGUID the unique identifier of the database
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Optional<EntityDetail> findSchemaForDatabase(String userId, String databaseGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException {
        return dataEngineCommonHandler.getEntityForRelationship(userId, databaseGUID, DATA_CONTENT_FOR_DATA_SET_TYPE_NAME, DATABASE_TYPE_NAME);
    }

    /**
     * Create or update the relational table
     *
     * @param userId                the name of the calling user
     * @param databaseSchemaQualifiedName the database qualified name
     * @param relationalTable       the values of the relational table
     * @param externalSourceName    the unique name of the external source
     *
     * @return unique identifier of the relationa table in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertRelationalTable(String userId, String databaseSchemaQualifiedName, RelationalTable relationalTable,
                                        String externalSourceName, boolean incomplete) throws InvalidParameterException,
            PropertyServerException, UserNotAuthorizedException {
        final String methodName = "upsertRelationalTable";
        validateParameters(userId, methodName, relationalTable.getQualifiedName(), relationalTable.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        String relationalTableGUID;
        Optional<EntityDetail> originalRelationalTableEntity = dataEngineCommonHandler.findEntity(userId,
                relationalTable.getQualifiedName(), RELATIONAL_TABLE_TYPE_NAME);
        if (originalRelationalTableEntity.isEmpty()) {
            Optional<EntityDetail> databaseSchemaEntity = findDatabaseSchemaEntity(userId, databaseSchemaQualifiedName);
            if (databaseSchemaEntity.isEmpty()) {
                    dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.DATABASE_SCHEMA_NOT_FOUND,
                            methodName, databaseSchemaQualifiedName);
            }

            String databaseSchemaGUID = databaseSchemaEntity.get().getGUID();
            relationalTableGUID = relationalDataHandler.createDatabaseTable(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    RELATIONAL_TABLE_TYPE_NAME, null, null, methodName);
        } else {
            relationalTableGUID = originalRelationalTableEntity.get().getGUID();
            relationalDataHandler.updateDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    RELATIONAL_TABLE_TYPE_NAME, null, null, methodName);
        }

        upsertRelationalColumns(userId, externalSourceGUID, externalSourceName, relationalTableGUID, relationalTable.getColumns());

        if (incomplete) {
            databaseSchemaAssetHandler.setClassificationInRepository(userId, relationalTableGUID, RELATIONAL_TABLE_TYPE_GUID,
                    RELATIONAL_TABLE_TYPE_NAME, INCOMPLETE_CLASSIFICATION_TYPE_GUID, INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                    null, methodName);
        }
        return relationalTableGUID;
    }

    /**
     * Create or update the relational columns of a relational table
     *
     * @param userId              the name of the calling user
     * @param columns             the values of the columns
     * @param relationalTableGUID the unique identifier of the relational table
     * @param externalSourceName  the unique name of the external source
     * @param externalSourceGUID  the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void upsertRelationalColumns(String userId, String externalSourceGUID, String externalSourceName, String relationalTableGUID,
                                         List<RelationalColumn> columns) throws InvalidParameterException, PropertyServerException,
                                                                                UserNotAuthorizedException {
        final String methodName = "upsertRelationalColumns";

        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        for (RelationalColumn column : columns) {
            int sortOrder = dataEngineCommonHandler.getSortOrder(column);

            Optional<EntityDetail> originalRelationalColumnEntity = dataEngineCommonHandler.findEntity(userId, column.getQualifiedName(),
                    RELATIONAL_COLUMN_TYPE_NAME);
            if (!originalRelationalColumnEntity.isPresent()) {
                relationalDataHandler.createDatabaseColumn(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                        column.getQualifiedName(), column.getDisplayName(), column.getDescription(), column.getExternalTypeGUID(),
                        column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getValidValuesSetGUID(), column.getFormula(),
                        column.getIsDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                        column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder,
                        column.getMinimumLength(), column.getLength(), column.getPrecision(), column.getIsNullable(), column.getNativeClass(),
                        column.getAliases(), column.getAdditionalProperties(), RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, methodName);
            } else {
                relationalDataHandler.updateDatabaseColumn(userId, externalSourceGUID, externalSourceName,
                        originalRelationalColumnEntity.get().getGUID(), column.getQualifiedName(), column.getDisplayName(), column.getDescription(),
                        column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getFormula(), column.getIsDeprecated(),
                        column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(),
                        column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder, column.getMinimumLength(), column.getLength(),
                        column.getPrecision(), column.getIsNullable(), column.getNativeClass(), column.getAliases(),
                        column.getAdditionalProperties(), RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, methodName);
            }
        }
    }

    /**
     * Verifies if the parameters are valid for a request
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name
     * @param displayName   the display name
     * @param methodName    name of the calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     */
    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    /**
     * Remove the database
     *
     * @param userId             the name of the calling user
     * @param databaseGUID       unique identifier of the database to be removed
     * @param externalSourceName the external data engine name
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeDatabase(String userId, String databaseGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws
                                                                                                                             FunctionNotSupportedException,
                                                                                                                             InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException {
        final String methodName = "removeDatabase";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, GUID_PROPERTY_NAME, methodName);

        Optional<EntityDetail> databaseOptional = dataEngineCommonHandler.getEntityDetails(userId, databaseGUID, DATABASE_TYPE_NAME);
        if (databaseOptional.isPresent()) {
            EntityDetail databaseEntity = databaseOptional.get();
            String databaseQualifiedName = databaseEntity.getProperties().getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME).valueAsString();
            String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
            relationalDataHandler.removeDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, databaseQualifiedName, methodName);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, databaseGUID);
        }
    }

    /**
     * Remove the relational table
     *
     * @param userId              the name of the calling user
     * @param relationalTableGUID unique identifier of the relational table to be removed
     * @param externalSourceName  the external data engine name
     * @param deleteSemantic      the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeRelationalTable(String userId, String relationalTableGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws
                                                                                                                                           FunctionNotSupportedException,
                                                                                                                                           InvalidParameterException,
                                                                                                                                           PropertyServerException,
                                                                                                                                           UserNotAuthorizedException {

        final String methodName = "removeRelationalTable";

        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationalTableGUID, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        Optional<EntityDetail> tableEntity = dataEngineCommonHandler.getEntityDetails(userId, relationalTableGUID, RELATIONAL_TABLE_TYPE_NAME);
        if (!tableEntity.isPresent()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, relationalTableGUID);
        }
        String tableQualifiedName = tableEntity.get().getProperties().getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME).valueAsString();

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        relationalDataHandler.removeDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID, GUID_PROPERTY_NAME,
                tableQualifiedName, methodName);
    }

    public void removeDatabaseSchema(String userId, String databaseSchemaGUID, String externalSourceName, DeleteSemantic deleteSemantic)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        final String methodName = "removeDatabaseSchema";

        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, GUID_PROPERTY_NAME, methodName);

        Optional<EntityDetail> databaseSchemaOptional = dataEngineCommonHandler.getEntityDetails(userId, databaseSchemaGUID,
                DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
        if (databaseSchemaOptional.isPresent()) {
            EntityDetail databaseSchemaEntity = databaseSchemaOptional.get();
            String databaseSchemaQualifiedName = databaseSchemaEntity.getProperties().getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME).valueAsString();
            String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
            relationalDataHandler.removeDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
            databaseSchemaQualifiedName, methodName);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, databaseSchemaGUID);
        }
    }
}