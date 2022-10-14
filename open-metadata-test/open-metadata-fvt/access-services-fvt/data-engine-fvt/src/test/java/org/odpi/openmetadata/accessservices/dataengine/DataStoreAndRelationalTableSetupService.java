/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates test data of type RelationalTable and subtypes of DataStore, and triggers requests via client for aforementioned types
 */
public class DataStoreAndRelationalTableSetupService {

    private final String DATABASE_QUALIFIED_NAME = "database-qualified-name";
    private final String DATABASE_SCHEMA_QUALIFIED_NAME = "database-schema-qualified-name";

    /**
     * Upsert a Database using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param database database to upsert. If null, a default will be used
     *
     * @return Database instance containing sent values
     */
    public Database upsertDatabase(String userId, DataEngineClient dataEngineClient, Database database)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(database == null){
            database = getDefaultDatabase();
        }
        dataEngineClient.upsertDatabase(userId, database);
        return database;
    }

    private Database getDefaultDatabase(){
        Database database = new Database();
        database.setQualifiedName(DATABASE_QUALIFIED_NAME);
        database.setDisplayName("database-display-name");
        database.setDescription("database-description");
        database.setDatabaseType("database-type");
        database.setDatabaseVersion("database-version");
        database.setDatabaseInstance("database-instance");
        database.setDatabaseImportedFrom("database-imported-from");
        database.setNetworkAddress("database-network-address");
        return database;
    }

    /**
     * Delete a Database using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteDatabase(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete Database. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteDatabase(userId, qualifiedName, guid);
    }

    /**
     * Upsert a DatabaseSchema using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param databaseSchema database to upsert. If null, a default will be used
     * @param databaseQualifiedName the database's qualified name
     * @param incomplete determines if the database schema is complete or not
     *
     * @return DatabaseSchema instance containing sent values
     */
    public DatabaseSchema upsertDatabaseSchema(String userId, DataEngineClient dataEngineClient, DatabaseSchema databaseSchema,
                                         String databaseQualifiedName, boolean incomplete)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(databaseSchema == null) {
            databaseSchema = getDefaultDatabaseSchema();
        }
        dataEngineClient.upsertDatabaseSchema(userId, databaseSchema, databaseQualifiedName);
        return databaseSchema;
    }

    private DatabaseSchema getDefaultDatabaseSchema() {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        databaseSchema.setQualifiedName(DATABASE_SCHEMA_QUALIFIED_NAME);
        databaseSchema.setDisplayName("database-schema-display-name");
        databaseSchema.setDescription("database-schema-description");
        return databaseSchema;
    }

    /**
     * Delete a DatabaseSchema using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteDatabaseSchema(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null) {
            throw new IllegalArgumentException("Unable to delete DatabaseSchema. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteDatabaseSchema(userId, qualifiedName, guid);
    }

    /**
     * Upsert a DataFile using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param dataFile data file to upsert. If null, a default will be used
     *
     * @return DataFile instance containing sent values
     */
    public DataFile upsertDataFile(String userId, DataEngineClient dataEngineClient, DataFile dataFile)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(dataFile == null){
            dataFile = getDefaultDataFile();
        }
        dataEngineClient.upsertDataFile(userId, dataFile);
        return dataFile;
    }

    private DataFile getDefaultDataFile(){
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName("data-file-qualified-name");
        dataFile.setDisplayName("data-file-display-name");
        dataFile.setDescription("data-file-description");
        dataFile.setFileType("data-file-type");
        dataFile.setProtocol("data-file-protocol");
        dataFile.setNetworkAddress("data-file-network-address");
        dataFile.setPathName("/data-file-pathname");
        dataFile.setColumns(buildTabularColumns());
        return dataFile;
    }

    private List<Attribute> buildTabularColumns(){
        List<Attribute> columns = new ArrayList<>();

        Attribute column = new Attribute();
        column.setQualifiedName("column-qualified-name");
        column.setDisplayName("column-display-name");
        column.setDescription("column-description");
        columns.add(column);

        return columns;
    }

    /**
     * Delete a DataFile using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteDataFile(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete RelationalTable. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteDataFile(userId, qualifiedName, guid);
    }

    /**
     * Upsert a RelationalTable using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param relationalTable relational table to upsert. If null, a default will be used
     * @param databaseSchemaQualifiedName the name of the database schema to which it should be linked
     * @param incomplete determines if the relational table is incomplete or not
     *
     * @return RelationalTable instance containing sent values
     */
    public RelationalTable upsertRelationalTable(String userId, DataEngineClient dataEngineClient, RelationalTable relationalTable,
                                                 String databaseSchemaQualifiedName, boolean incomplete)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(relationalTable == null) {
            relationalTable = getDefaultRelationalTable();
        }
        dataEngineClient.upsertRelationalTable(userId, relationalTable, databaseSchemaQualifiedName);
        return relationalTable;
    }

    private RelationalTable getDefaultRelationalTable(){
        RelationalTable relationalTable = new RelationalTable();
        relationalTable.setQualifiedName("relational-table-qualified-name");
        relationalTable.setDisplayName("relational-table-display-name");
        relationalTable.setDescription("relational-table-description");
        relationalTable.setType("relational-table-type");
        relationalTable.setColumns(buildRelationalColumns());
        return relationalTable;
    }

    private List<RelationalColumn> buildRelationalColumns(){
        List<RelationalColumn> columns = new ArrayList<>();

        RelationalColumn column = new RelationalColumn();
        column.setQualifiedName("relational-column-qualified-name");
        column.setDisplayName("relational-column-display-name");
        column.setDescription("relational-column-description");
        column.setDataType("relational-column-data-type");
        columns.add(column);

        return columns;
    }

    /**
     * Delete a RelationalTable using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteRelationalTable(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete RelationalTable. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteRelationalTable(userId, qualifiedName, guid);
    }

    /**
     * Delete a FileFolder using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteFolder(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete FileFolder. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteFolder(userId, qualifiedName, guid);
    }

    /**
     * Delete a SchemaType using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteSchemaType(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {

        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete SchemaType. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteSchemaType(userId, qualifiedName, guid);
    }
}
