/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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

        int ownerTypeOrdinal = getOwnerTypeOrdinal(database.getOwnerType());
        String databaseGUID;
        if (!originalDatabaseEntity.isPresent()) {
            databaseGUID = relationalDataHandler.createDatabase(userId, externalSourceGUID, externalSourceName, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    null, null, null, database.getPathName(), database.getCreateTime(),
                    database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(),
                    database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(),
                    database.getDatabaseImportedFrom(), database.getAdditionalProperties(), null, database.getExtendedProperties(),
                    null, methodName);
        } else {
            databaseGUID = originalDatabaseEntity.get().getGUID();
            relationalDataHandler.updateDatabase(userId, externalSourceGUID, externalSourceName, databaseGUID, database.getQualifiedName(),
                    database.getDisplayName(), database.getDescription(), database.getOwner(), ownerTypeOrdinal, database.getZoneMembership(),
                    null, null, null, database.getCreateTime(),
                    database.getModifiedTime(), database.getEncodingType(), database.getEncodingLanguage(), database.getEncodingDescription(),
                    database.getEncodingProperties(), database.getDatabaseType(), database.getDatabaseVersion(), database.getDatabaseInstance(),
                    database.getDatabaseImportedFrom(), database.getAdditionalProperties(), null, database.getExtendedProperties(),
                    null, methodName);
        }

        return databaseGUID;
    }

    private int getOwnerTypeOrdinal(OwnerType ownerType) {
        int ownerTypeOrdinal = 0;

        if (ownerType != null) {
            ownerTypeOrdinal = ownerType.getOpenTypeOrdinal();
        }
        return ownerTypeOrdinal;
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
    private Optional<EntityDetail> findDatabaseEntity(String userId, String qualifiedName) throws InvalidParameterException, PropertyServerException,
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
        Optional<EntityDetail> originalDatabaseSchemaEntity = findDatabaseEntity(userId, databaseSchema.getQualifiedName());

        int ownerTypeOrdinal = getOwnerTypeOrdinal(databaseSchema.getOwnerType());
        String databaseSchemaGUID;
        if (!originalDatabaseSchemaEntity.isPresent()) {
            databaseSchemaGUID = relationalDataHandler.createDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), null, null, null,
                    databaseSchema.getAdditionalProperties(), OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                    databaseSchema.getExtendedProperties(), null, methodName);
        } else {
            databaseSchemaGUID = originalDatabaseSchemaEntity.get().getGUID();
            relationalDataHandler.updateDatabaseSchema(userId, externalSourceGUID, externalSourceName, databaseSchemaGUID,
                    databaseSchema.getQualifiedName(), databaseSchema.getDisplayName(), databaseSchema.getDescription(), databaseSchema.getOwner(),
                    ownerTypeOrdinal, databaseSchema.getZoneMembership(), null, null, null,
                    databaseSchema.getAdditionalProperties(), OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                    databaseSchema.getExtendedProperties(), null, methodName);
        }

        return databaseSchemaGUID;
    }

    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, SchemaTypePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);
    }


}
