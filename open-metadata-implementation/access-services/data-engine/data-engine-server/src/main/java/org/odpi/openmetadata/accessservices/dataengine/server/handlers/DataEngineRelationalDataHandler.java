/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
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

public class DataEngineRelationalDataHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn, SchemaType> relationalDataHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;

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
                                           RelationalDataHandler<Database, DatabaseSchema, RelationalTable, RelationalTable, RelationalColumn,
                                                   SchemaType> relationalDataHandler,
                                           DataEngineRegistrationHandler registrationHandler,
                                           DataEngineCommonHandler dataEngineCommonHandler) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.relationalDataHandler = relationalDataHandler;
        this.registrationHandler = registrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create the database
     *
     * @param userId             the name of the calling user
     * @param database           the values of the process
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the process in the repository
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

        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseEntity = findDatabaseEntity(userId, database.getQualifiedName());

        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(database.getOwnerType());
        String databaseGUID;
        if (!originalDatabaseEntity.isPresent()) {
            databaseGUID = relationalDataHandler.createDatabase(userId, externalSourceGUID, externalSourceName, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    null, null, null, database.getPathName(), database.getCreateTime(),
                    database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(),
                    database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(),
                    database.getDatabaseImportedFrom(), database.getAdditionalProperties(), null, database.getExtendedProperties(),
                    database.getVendorProperties(), methodName);
        } else {
            databaseGUID = originalDatabaseEntity.get().getGUID();
            relationalDataHandler.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    null, null, null, database.getCreateTime(),
                    database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(),
                    database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(),
                    database.getDatabaseImportedFrom(), database.getAdditionalProperties(), null, database.getExtendedProperties(),
                    database.getVendorProperties(), methodName);
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
    public Optional<EntityDetail> findDatabaseEntity(String userId, String qualifiedName) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, OpenMetadataAPIMapper.DATABASE_TYPE_NAME);
    }

    public String upsertDatabaseSchema(String userId, String databaseGUID, DatabaseSchema databaseSchema, String externalSourceName) throws
                                                                                                                                     InvalidParameterException,
                                                                                                                                     PropertyServerException,
                                                                                                                                     UserNotAuthorizedException {
        final String methodName = "upsertDatabaseSchema";
        validateParameters(userId, methodName, databaseSchema.getQualifiedName(), databaseSchema.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);
        Optional<EntityDetail> originalDatabaseSchemaEntity = findDatabaseSchemaEntity(userId, databaseSchema.getQualifiedName());

        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(databaseSchema.getOwnerType());
        String databaseSchemaGUID;
        if (!originalDatabaseSchemaEntity.isPresent()) {
            databaseSchemaGUID = relationalDataHandler.createDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), null, null, null,
                    databaseSchema.getAdditionalProperties(), OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                    databaseSchema.getExtendedProperties(), databaseSchema.getVendorProperties(), methodName);
        } else {
            databaseSchemaGUID = originalDatabaseSchemaEntity.get().getGUID();
            relationalDataHandler.updateDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), null, null, null,
                    databaseSchema.getAdditionalProperties(), OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                    databaseSchema.getExtendedProperties(), databaseSchema.getVendorProperties(), methodName);
        }

        return databaseSchemaGUID;
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
    public Optional<EntityDetail> findDatabaseSchemaEntity(String userId, String qualifiedName) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
    }

    /**
     * Retrieve the schema type that is linked to the port
     *
     * @param userId       the name of the calling user
     * @param databaseGUID the unique identifier of the port
     *
     * @return The unique identifier for the retrieved schema type or null
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<String> findSchemaForDatabase(String userId, String databaseGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException {
        final String methodName = "findSchemaForDatabase";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId, databaseGUID, OpenMetadataAPIMapper.DATABASE_TYPE_NAME,
                relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), methodName);

        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(entity.getGUID());
    }

    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    public String upsertRelationalTable(String userId, String databaseQualifiedName, RelationalTable relationalTable, String externalSourceName) throws
                                                                                                                                                 InvalidParameterException,
                                                                                                                                                 PropertyServerException,
                                                                                                                                                 UserNotAuthorizedException {

        final String methodName = "upsertRelationalTable";
        validateParameters(userId, methodName, relationalTable.getQualifiedName(), relationalTable.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);
        //maybe create find method
        Optional<EntityDetail> originalRelationalTableEntity = dataEngineCommonHandler.findEntity(userId, relationalTable.getQualifiedName(),
                OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME);

        String relationalTableGUID;
        if (!originalRelationalTableEntity.isPresent()) {
            String databaseSchemaGUID = getDatabaseSchemaGUID(userId, databaseQualifiedName, methodName);
            relationalTableGUID = relationalDataHandler.createDatabaseTable(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    relationalTable.getTypeName(), relationalTable.getExtendedProperties(), relationalTable.getVendorProperties(), methodName);
        } else {
            relationalTableGUID = originalRelationalTableEntity.get().getGUID();
            relationalDataHandler.updateDatabaseTable(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                    relationalTable.getQualifiedName(), relationalTable.getDisplayName(), relationalTable.getDescription(),
                    relationalTable.isDeprecated(), relationalTable.getAliases(), relationalTable.getAdditionalProperties(),
                    relationalTable.getTypeName(), relationalTable.getExtendedProperties(), relationalTable.getVendorProperties(), methodName);
        }

        upsertRelationalColumns(userId, externalSourceGUID, externalSourceName, relationalTableGUID, relationalTable.getColumns());

        return relationalTableGUID;
    }

    private void upsertRelationalColumns(String userId, String externalSourceGUID, String externalSourceName, String relationalTableGUID,
                                         List<RelationalColumn> columns) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException {
        final String methodName = "upsertRelationalTable";

        for (RelationalColumn column : columns) {
            //maybe create find method
            Optional<EntityDetail> originalRelationalColumnEntity = dataEngineCommonHandler.findEntity(userId, column.getQualifiedName(),
                    OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME);
            if (!originalRelationalColumnEntity.isPresent()) {
                relationalDataHandler.createDatabaseColumn(userId, externalSourceGUID, externalSourceName, relationalTableGUID,
                        column.getQualifiedName(),column.getDisplayName(), null, null, column.getDataType(),
                        column.getDefaultValue(), null, null, column.getFormula(), false, column.getPosition(),
                        column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(), column.getOrderedValues(),
                        column.getDefaultValueOverride(), 1, 1, 1, 1, false, null,
                        null, column.getAdditionalProperties(), column.getTypeName(), column.getExtendedProperties(), column.getVendorProperties(),
                        methodName);
            }
            else {
                relationalDataHandler.updateDatabaseColumn(userId, externalSourceGUID, externalSourceName, originalRelationalColumnEntity.get().getGUID(),
                        column.getQualifiedName(),column.getDisplayName(), null,  column.getDataType(),
                        column.getDefaultValue(), null, column.getFormula(), false, column.getPosition(),
                        column.getMinCardinality(), column.getMaxCardinality(), column.getAllowsDuplicateValues(), column.getOrderedValues(),
                        column.getDefaultValueOverride(), 1, 1, 1, 1, false, null,
                        null, column.getAdditionalProperties(), column.getTypeName(), column.getExtendedProperties(), column.getVendorProperties(),
                        methodName);
            }
        }
    }

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
}