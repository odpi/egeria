/* SPDX-License-Identifier: Apache-2.0 */
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
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Date;
import java.util.List;
import java.util.Optional;



/**
 * DataEngineRelationalDataHandler manages Databases and RelationalTables objects from the property server.  It runs server-side in the DataEngine
 * OMAS and creates entities and relationships through the OMRSRepositoryConnector.
 */
public class DataEngineRelationalDataHandler {

    private static final String DATABASE_SCHEMA_GUID = "databaseSchemaGUID";
    private static final String DATABASE_GUID = "databaseGUID";
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
     * @param databaseSchemaAssetHandler             provides utilities for manipulating database schema assets
     * @param dataEngineCommonHandler                provides utilities for manipulating entities
     * @param registrationHandler                    creates engine entities
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
     * Create or update the database and the inside entities, if any (a database schema and relational tables)
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

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseEntity = findDatabaseEntity(userId, database.getQualifiedName());

        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(database.getOwnerType());
        Date now = dataEngineCommonHandler.getNow();
        String databaseGUID;
        if (originalDatabaseEntity.isEmpty()) {
            databaseGUID = relationalDataHandler.createDatabase(userId, externalSourceGUID, externalSourceName, database.getQualifiedName(),
                    database.getDisplayName(), null, database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(),
                    database.getPathName(), database.getCreateTime(), database.getModifiedTime(), database.getEncodingType(),
                    database.getEncodingLanguage(), database.getEncodingDescription(), database.getEncodingProperties(), database.getDatabaseType(),
                    database.getDatabaseVersion(), database.getDatabaseInstance(), database.getDatabaseImportedFrom(),
                    database.getAdditionalProperties(), OpenMetadataType.DATABASE_TYPE_NAME, null, null,
                    null, null, false, false, now, methodName);
        } else {
            databaseGUID = originalDatabaseEntity.get().getGUID();
            relationalDataHandler.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    database.getOriginOrganizationGUID(), database.getOriginBusinessCapabilityGUID(), database.getOtherOriginValues(),
                    database.getCreateTime(), database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(),
                    database.getEncodingDescription(), database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(),
                    database.getDatabaseInstance(), database.getDatabaseImportedFrom(), database.getAdditionalProperties(),
                                                 OpenMetadataType.DATABASE_TYPE_NAME, null, null, null, null, true,
                    false, false, now, methodName);
        }

        if (database.getIncomplete()) {
            databaseSchemaAssetHandler.setClassificationInRepository(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    DATABASE_GUID, OpenMetadataType.DATABASE_TYPE_NAME, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                    null, true, false, false, now, methodName);
        }

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(database.getQualifiedName(),
                databaseGUID, OpenMetadataType.DATABASE_TYPE_NAME, database.getProtocol(), database.getNetworkAddress(),
                externalSourceGUID, externalSourceName, userId);

        DatabaseSchema databaseSchema = database.getDatabaseSchema();
        if (databaseSchema != null) {
            upsertDatabaseSchema(userId, databaseGUID, databaseSchema, externalSourceName);
            List<RelationalTable> tables = database.getTables();
            if (CollectionUtils.isNotEmpty(tables)) {
                for (RelationalTable table : tables) {
                    String databaseSchemaQualifiedName = databaseSchema.getQualifiedName();
                    upsertRelationalTable(userId, databaseSchemaQualifiedName, table, externalSourceName);
                }
            }
        }

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
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, OpenMetadataType.DATABASE_TYPE_NAME);
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
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
    }

    /**
     * Create or update the database schema
     *
     * @param userId             the name of the calling user
     * @param databaseGUID       the unique identifier of the database
     * @param databaseSchema     the values of the database schema
     * @param externalSourceName the unique name of the external source
     *
     * @return database schema GUID
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabaseSchema(String userId, String databaseGUID, DatabaseSchema databaseSchema, String externalSourceName) throws
                                                                                                                                     InvalidParameterException,
                                                                                                                                     PropertyServerException,
                                                                                                                                     UserNotAuthorizedException {

        final String methodName = "upsertDatabaseSchema";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(databaseSchema.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseSchemaEntity = dataEngineCommonHandler.findEntity(userId, databaseSchema.getQualifiedName(),
                                                                                                 OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        String databaseSchemaGUID;
        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(databaseSchema.getOwnerType());
        Date now = dataEngineCommonHandler.getNow();
        if (originalDatabaseSchemaEntity.isEmpty()) {
            databaseSchemaGUID = relationalDataHandler.createDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null,
                    null, null, null, false, false, now, methodName);
        } else {
            databaseSchemaGUID = originalDatabaseSchemaEntity.get().getGUID();
            relationalDataHandler.updateDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), null, databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null,
                    null, null, null, true, false, false, now, methodName);

            if (StringUtils.isNotEmpty(databaseGUID)) {
                databaseSchemaAssetHandler.linkElementToElement(userId, externalSourceGUID, externalSourceName, databaseGUID,
                                                                DATABASE_GUID, OpenMetadataType.DATABASE_TYPE_NAME, databaseSchemaGUID, DATABASE_SCHEMA_GUID,
                                                                OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, false, false,
                                                                OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeGUID, OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName,
                                                                (InstanceProperties) null, null, null, now, methodName);
            }
        }

        if (databaseSchema.getIncomplete()) {
            databaseSchemaAssetHandler.setClassificationInRepository(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID, DATABASE_SCHEMA_GUID,
                                                                     OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                     OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME,
                    null, true, false, false, now, methodName);
        }
        return databaseSchemaGUID;
    }

    /**
     * Create or update the relational table
     *
     * @param userId                      the name of the calling user
     * @param databaseSchemaQualifiedName the database qualified name
     * @param relationalTable             the values of the relational table
     * @param externalSourceName          the unique name of the external source
     *
     * @return unique identifier of the relational table in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertRelationalTable(String userId, String databaseSchemaQualifiedName, RelationalTable relationalTable,
                                        String externalSourceName) throws InvalidParameterException,
                                                                                              PropertyServerException, UserNotAuthorizedException {
        final String methodName = "upsertRelationalTable";
        validateParameters(userId, methodName, relationalTable.getQualifiedName(), relationalTable.getDisplayName());
        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        String relationalTableGUID;
        Optional<EntityDetail> originalRelationalTableEntity = dataEngineCommonHandler.findEntity(userId,
                relationalTable.getQualifiedName(), OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME);
        Date now = dataEngineCommonHandler.getNow();
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
                                                                            OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null, null,
                    false, false, now, methodName);
        } else {
            relationalTableGUID = originalRelationalTableEntity.get().getGUID();
            relationalDataHandler.updateDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.getIsDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                                                      OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, null, null, null, null,
                    true, false, false, now, methodName);
        }

        upsertRelationalColumns(userId, externalSourceGUID, externalSourceName, relationalTableGUID, relationalTable.getColumns());

        if (relationalTable.getIncomplete()) {
            databaseSchemaAssetHandler.setClassificationInRepository(userId, externalSourceGUID, externalSourceName,
                    relationalTableGUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_GUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID,
                                                                     OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME, null, true, false,
                    false, now, methodName);
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

        Date now = dataEngineCommonHandler.getNow();
        for (RelationalColumn column : columns) {
            int sortOrder = dataEngineCommonHandler.getSortOrder(column);

            Optional<EntityDetail> originalRelationalColumnEntity = dataEngineCommonHandler.findEntity(userId, column.getQualifiedName(),
                                                                                                       OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME);
            if (originalRelationalColumnEntity.isEmpty()) {
                relationalDataHandler.createDatabaseColumn(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                        column.getQualifiedName(), column.getDisplayName(), column.getDescription(), column.getExternalTypeGUID(),
                        column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getValidValuesSetGUID(), column.getFormula(),
                        column.getIsDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                        column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder,
                        column.getMinimumLength(), column.getLength(), column.getPrecision(), column.getIsNullable(), column.getNativeClass(),
                        column.getAliases(), column.getAdditionalProperties(), OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, null, null, false, false, now, methodName);
            } else {
                relationalDataHandler.updateDatabaseColumn(userId, externalSourceGUID, externalSourceName,
                        originalRelationalColumnEntity.get().getGUID(), column.getQualifiedName(), column.getDisplayName(), column.getDescription(),
                        column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getFormula(), column.getIsDeprecated(),
                        column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(),
                        column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder, column.getMinimumLength(), column.getLength(),
                        column.getPrecision(), column.getIsNullable(), column.getNativeClass(), column.getAliases(),
                        column.getAdditionalProperties(), OpenMetadataType.RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, null, null, true, false,
                        false, now, methodName);
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
        invalidParameterHandler.validateName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        invalidParameterHandler.validateName(displayName, OpenMetadataProperty.DISPLAY_NAME.name, methodName);
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
        invalidParameterHandler.validateGUID(databaseGUID, OpenMetadataProperty.GUID.name, methodName);

        Optional<EntityDetail> databaseOptional = dataEngineCommonHandler.getEntityDetails(userId, databaseGUID, OpenMetadataType.DATABASE_TYPE_NAME);
        if (databaseOptional.isPresent()) {
            EntityDetail databaseEntity = databaseOptional.get();
            String databaseQualifiedName = databaseEntity.getProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString();
            String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
            relationalDataHandler.removeDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, databaseQualifiedName,
                                                 false, false, dataEngineCommonHandler.getNow(), methodName);
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
        invalidParameterHandler.validateGUID(relationalTableGUID, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);

        Optional<EntityDetail> tableEntity = dataEngineCommonHandler.getEntityDetails(userId, relationalTableGUID,
                                                                                      OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME);
        if (tableEntity.isEmpty()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, relationalTableGUID);
        }
        String tableQualifiedName = tableEntity.get().getProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString();

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        relationalDataHandler.removeDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID, OpenMetadataProperty.GUID.name,
                tableQualifiedName, false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Removes the database schema
     *
     * @param userId             the name of the calling user
     * @param databaseSchemaGUID unique identifier of the database schema to be removed
     * @param externalSourceName the external data engine name
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeDatabaseSchema(String userId, String databaseSchemaGUID, String externalSourceName, DeleteSemantic deleteSemantic)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        final String methodName = "removeDatabaseSchema";

        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, OpenMetadataProperty.GUID.name, methodName);

        Optional<EntityDetail> databaseSchemaOptional = dataEngineCommonHandler.getEntityDetails(userId, databaseSchemaGUID,
                                                                                                 OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
        if (databaseSchemaOptional.isPresent()) {
            EntityDetail databaseSchemaEntity = databaseSchemaOptional.get();
            String databaseSchemaQualifiedName = databaseSchemaEntity.getProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString();
            String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
            relationalDataHandler.removeDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
            databaseSchemaQualifiedName, false, false, dataEngineCommonHandler.getNow(), methodName);

        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, databaseSchemaGUID);
        }
    }
}
