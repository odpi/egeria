/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used in DataEngineFVT to generate test data of type RelationalTable and subtypes of DataStore.
 *
 */
public class DataStoreAndRelationalTableSetupService {

    private final String DATABASE_QUALIFIED_NAME = "databaseQualifiedName";
    private final String DATABASE_DISPLAY_NAME = "databaseDisplayName";
    private final String DATABASE_DESCRIPTION = "databaseDescription";
    private final String DATABASE_TYPE = "databaseType";
    private final String DATABASE_VERSION = "databaseVersion";
    private final String DATABASE_INSTANCE = "databaseInstance";
    private final String DATABASE_IMPORTED_FROM = "databaseImportedFrom";

    private final String DATAFILE_QUALIFIED_NAME = "dataFileQualifiedName";
    private final String DATAFILE_DISPLAY_NAME = "dataFileDisplayName";
    private final String DATAFILE_DESCRIPTION = "dataFileDescription";
    private final String DATAFILE_TYPE = "dataFile";
    private final String DATAFILE_PROTOCOL = "dataFileProtocol";
    private final String DATAFILE_NETWORK_ADDRESS = "dataFileNetworkAddress";
    private final String DATAFILE_PATHNAME = "/dataFilePathname";

    private final String COLUMN_QUALIFIED_NAME = "columnQualifiedName";
    private final String COLUMN_DISPLAY_NAME = "columnDisplayName";
    private final String COLUMN_DESCRIPTION = "columnDescription";

    private final String RELATIONAL_TABLE_QUALIFIED_NAME = "relationalTableQualifiedName";
    private final String RELATIONAL_TABLE_DISPLAY_NAME = "relationalTableDisplayName";
    private final String RELATIONAL_TABLE_DESCRIPTION = "relationalTableDescription";
    private final String RELATIONAL_TABLE_TYPE = "relationalTableType";

    private final String RELATIONAL_COLUMN_QUALIFIED_NAME = "relationalColumnQualifiedName";
    private final String RELATIONAL_COLUMN_DISPLAY_NAME = "relationalColumnDisplayName";
    private final String RELATIONAL_COLUMN_DESCRIPTION = "relationalColumnDescription";
    private final String RELATIONAL_COLUMN_DATA_TYPE = "relationalColumnDataType";

    /**
     * Upsert a Database using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     *
     * @return Database instance containing sent values
     */
    public Database upsertDatabase(String userId, DataEngineClient dataEngineClient)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        Database database = new Database();
        database.setQualifiedName(DATABASE_QUALIFIED_NAME);
        database.setDisplayName(DATABASE_DISPLAY_NAME);
        database.setDescription(DATABASE_DESCRIPTION);
        database.setDatabaseType(DATABASE_TYPE);
        database.setDatabaseVersion(DATABASE_VERSION);
        database.setDatabaseInstance(DATABASE_INSTANCE);
        database.setDatabaseImportedFrom(DATABASE_IMPORTED_FROM);
        dataEngineClient.upsertDatabase(userId, database);
        return database;
    }

    /**
     * Upsert a DataFile using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     *
     * @return DataFile instance containing sent values
     */
    public DataFile upsertDataFile(String userId, DataEngineClient dataEngineClient)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName(DATAFILE_QUALIFIED_NAME);
        dataFile.setDisplayName(DATAFILE_DISPLAY_NAME);
        dataFile.setDescription(DATAFILE_DESCRIPTION);
        dataFile.setFileType(DATAFILE_TYPE);
        dataFile.setProtocol(DATAFILE_PROTOCOL);
        dataFile.setNetworkAddress(DATAFILE_NETWORK_ADDRESS);
        dataFile.setPathName(DATAFILE_PATHNAME);
        dataFile.setColumns(buildTabularColumns());
        dataEngineClient.upsertDataFile(userId, dataFile);
        return dataFile;
    }

    private List<Attribute> buildTabularColumns(){
        List<Attribute> columns = new ArrayList<>();

        Attribute column = new Attribute();
        column.setQualifiedName(COLUMN_QUALIFIED_NAME);
        column.setDisplayName(COLUMN_DISPLAY_NAME);
        column.setDescription(COLUMN_DESCRIPTION);
        columns.add(column);

        return columns;
    }

    /**
     * Upsert a RelationalTable using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     *
     * @return RelationalTable instance containing sent values
     */
    public RelationalTable upsertRelationalTable(String userId, DataEngineClient dataEngineClient)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        RelationalTable relationalTable = new RelationalTable();
        relationalTable.setQualifiedName(RELATIONAL_TABLE_QUALIFIED_NAME);
        relationalTable.setDisplayName(RELATIONAL_TABLE_DISPLAY_NAME);
        relationalTable.setDescription(RELATIONAL_TABLE_DESCRIPTION);
        relationalTable.setType(RELATIONAL_TABLE_TYPE);
        relationalTable.setColumns(buildRelationalColumns());
        dataEngineClient.upsertRelationalTable(userId, relationalTable, DATABASE_QUALIFIED_NAME);
        return relationalTable;
    }

    private List<RelationalColumn> buildRelationalColumns(){
        List<RelationalColumn> columns = new ArrayList<>();

        RelationalColumn column = new RelationalColumn();
        column.setQualifiedName(RELATIONAL_COLUMN_QUALIFIED_NAME);
        column.setDisplayName(RELATIONAL_COLUMN_DISPLAY_NAME);
        column.setDescription(RELATIONAL_COLUMN_DESCRIPTION);
        column.setDataType(RELATIONAL_COLUMN_DATA_TYPE);
        columns.add(column);

        return columns;
    }

}
