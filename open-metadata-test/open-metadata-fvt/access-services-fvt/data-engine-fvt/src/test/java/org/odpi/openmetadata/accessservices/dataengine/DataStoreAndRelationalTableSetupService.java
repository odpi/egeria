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

    private final String DATABASE_QUALIFIED_NAME = "database-qualified-name";

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
        database.setDisplayName("database-display-name");
        database.setDescription("database-description");
        database.setDatabaseType("database-type");
        database.setDatabaseVersion("database-version");
        database.setDatabaseInstance("database-instance");
        database.setDatabaseImportedFrom("database-imported-from");
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
        dataFile.setQualifiedName("data-file-qualified-name");
        dataFile.setDisplayName("data-file-display-name");
        dataFile.setDescription("data-file-description");
        dataFile.setFileType("data-file-type");
        dataFile.setProtocol("data-file-protocol");
        dataFile.setNetworkAddress("data-file-network-address");
        dataFile.setPathName("/data-file-pathname");
        dataFile.setColumns(buildTabularColumns());
        dataEngineClient.upsertDataFile(userId, dataFile);
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
        relationalTable.setQualifiedName("relational-table-qualified-name");
        relationalTable.setDisplayName("relational-table-display-name");
        relationalTable.setDescription("relational-table-description");
        relationalTable.setType("relational-table-type");
        relationalTable.setColumns(buildRelationalColumns());
        dataEngineClient.upsertRelationalTable(userId, relationalTable, DATABASE_QUALIFIED_NAME);
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

}
