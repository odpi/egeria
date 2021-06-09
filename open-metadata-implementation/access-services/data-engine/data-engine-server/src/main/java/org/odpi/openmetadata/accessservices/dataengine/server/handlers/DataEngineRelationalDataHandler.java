/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATABASE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME;

/**
 * DataEngineRelationalDataHandler manages Databases and RelationalTables objects from the property server.  It runs server-side in the DataEngine
 * OMAS and creates entities and relationships through the OMRSRepositoryConnector.
 */
public class DataEngineRelationalDataHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn, SchemaType> relationalDataHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;
    private final DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param relationalDataHandler   provides utilities for manipulating the repository services assets
     * @param dataEngineCommonHandler provides utilities for manipulating entities
     * @param registrationHandler     creates software server capability entities
     **/
    public DataEngineRelationalDataHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                           RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                                           RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn, SchemaType> relationalDataHandler,
                                           DataEngineRegistrationHandler registrationHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                           DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.relationalDataHandler = relationalDataHandler;
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
        if (!originalDatabaseEntity.isPresent()) {
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

        DatabaseSchema databaseSchema = database.getDatabaseSchema();
        if (databaseSchema == null) {
            databaseSchema = createDefaultDatabaseSchema(database.getQualifiedName());
        }
        addAssetProperties(databaseSchema, database.getOwner(), database.getOwnerType(), database.getZoneMembership());
        upsertDatabaseSchema(userId, databaseGUID, databaseSchema, externalSourceName);

        if(database.getProtocol() != null && database.getNetworkAddress() != null) {
            this.dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(database.getQualifiedName(),
                    DATABASE_TYPE_NAME, database.getProtocol(), database.getNetworkAddress(),
                    externalSourceGUID, externalSourceName, userId, methodName);
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
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, DATABASE_TYPE_NAME);
    }

    /**
     * Create or update the database schema
     *
     * @param userId             the name of the calling user
     * @param databaseSchema     the values of the database schema
     * @param databaseGUID       the unique identifier of the database
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void upsertDatabaseSchema(String userId, String databaseGUID, DatabaseSchema databaseSchema, String externalSourceName) throws
                                                                                                                                    InvalidParameterException,
                                                                                                                                    PropertyServerException,
                                                                                                                                    UserNotAuthorizedException {
        final String methodName = "upsertDatabaseSchema";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(databaseSchema.getQualifiedName(), QUALIFIED_NAME_PROPERTY_NAME, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseSchemaEntity = dataEngineCommonHandler.findEntity(userId, databaseSchema.getQualifiedName(),
                DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(databaseSchema.getOwnerType());
        if (!originalDatabaseSchemaEntity.isPresent()) {
            relationalDataHandler.createDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(),DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null, null, methodName);
        } else {
            String databaseSchemaGUID = originalDatabaseSchemaEntity.get().getGUID();
            relationalDataHandler.updateDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), databaseSchema.getOriginOrganizationGUID(),
                    databaseSchema.getOriginBusinessCapabilityGUID(), databaseSchema.getOtherOriginValues(),
                    databaseSchema.getAdditionalProperties(), DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, null, null, methodName);
        }
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
    private Optional<String> findSchemaForDatabase(String userId, String databaseGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException {
        final String methodName = "findSchemaForDatabase";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, DATA_CONTENT_FOR_DATA_SET_TYPE_NAME);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId, databaseGUID, DATABASE_TYPE_NAME,
                relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), methodName);

        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(entity.getGUID());
    }

    /**
     * Create or update the relational table
     *
     * @param userId                the name of the calling user
     * @param databaseQualifiedName the database qualified name
     * @param relationalTable       the values of the relational table
     * @param externalSourceName    the unique name of the external source
     *
     * @return unique identifier of the relationa table in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertRelationalTable(String userId, String databaseQualifiedName, RelationalTable relationalTable, String externalSourceName) throws
                                                                                                                                                 InvalidParameterException,
                                                                                                                                                 PropertyServerException,
                                                                                                                                                 UserNotAuthorizedException {
        final String methodName = "upsertRelationalTable";

        validateParameters(userId, methodName, relationalTable.getQualifiedName(), relationalTable.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        Optional<EntityDetail> originalRelationalTableEntity = dataEngineCommonHandler.findEntity(userId, relationalTable.getQualifiedName(),
                RELATIONAL_TABLE_TYPE_NAME);

        String relationalTableGUID;
        if (!originalRelationalTableEntity.isPresent()) {
            String databaseSchemaGUID = getDatabaseSchemaGUID(userId, databaseQualifiedName, methodName);
            relationalTableGUID = relationalDataHandler.createDatabaseTable(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    RELATIONAL_TABLE_TYPE_NAME, null, null, methodName);
        } else {
            relationalTableGUID = originalRelationalTableEntity.get().getGUID();
            relationalDataHandler.updateDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    RELATIONAL_TABLE_TYPE_NAME, null, null, methodName);
        }

        upsertRelationalColumns(userId, externalSourceGUID, externalSourceName, relationalTableGUID, relationalTable.getColumns());

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
                        column.isDeprecated(), column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(),
                        column.getAllowsDuplicateValues(), column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder,
                        column.getMinimumLength(), column.getLength(), column.getPrecision(), column.isNullable(), column.getNativeClass(),
                        column.getAliases(), column.getAdditionalProperties(), RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, methodName);
            } else {
                relationalDataHandler.updateDatabaseColumn(userId, externalSourceGUID, externalSourceName,
                        originalRelationalColumnEntity.get().getGUID(), column.getQualifiedName(), column.getDisplayName(), column.getDescription(),
                        column.getDataType(), column.getDefaultValue(), column.getFixedValue(), column.getFormula(), column.isDeprecated(),
                        column.getPosition(), column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(),
                        column.getOrderedValues(), column.getDefaultValueOverride(), sortOrder, column.getMinimumLength(), column.getLength(),
                        column.getPrecision(), column.isNullable(), column.getNativeClass(), column.getAliases(),
                        column.getAdditionalProperties(), RELATIONAL_COLUMN_TYPE_NAME, null,
                        null, methodName);
            }
        }
    }

    /**
     * Retrieve the unique identifier of the schema type that is linked to the database
     *
     * @param userId                the name of the calling user
     * @param databaseQualifiedName the qualified name of the database
     *
     * @return The unique identifier for the retrieved schema type or null
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String getDatabaseSchemaGUID(String userId, String databaseQualifiedName, String methodName) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException {
        Optional<EntityDetail> databaseOptional = findDatabaseEntity(userId, databaseQualifiedName);
        if (!databaseOptional.isPresent()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.DATABASE_NOT_FOUND, methodName, databaseQualifiedName);
        } else {
            Optional<String> databaseSchemaOptional = findSchemaForDatabase(userId, databaseOptional.get().getGUID());
            if (!databaseSchemaOptional.isPresent()) {
                dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.DATABASE_NOT_FOUND, methodName, databaseQualifiedName);
            } else {
                return databaseSchemaOptional.get();
            }
        }
        return null;
    }

    /**
     * Adds the common asset properties to the database schema
     *
     * @param databaseSchema the database schema
     * @param owner          string the owner
     * @param ownerType      enum OwnerType
     * @param zoneMembership list of zone names¬
     */
    private void addAssetProperties(DatabaseSchema databaseSchema, String owner, OwnerType ownerType, List<String> zoneMembership) {
        databaseSchema.setOwner(owner);
        databaseSchema.setOwnerType(ownerType);
        databaseSchema.setZoneMembership(zoneMembership);
    }

    /**
     * Creates a default Database Schema object with qualified name composed from the Database qualified name
     *
     * @param databaseQualifiedName the qualified name
     *
     * @return The DatabaseSchema object
     */
    private DatabaseSchema createDefaultDatabaseSchema(String databaseQualifiedName) {
        String postfix = ":schema";
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.setQualifiedName(databaseQualifiedName + postfix);
        return databaseSchema;
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
}