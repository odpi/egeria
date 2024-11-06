/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.mappers.BaseMapper;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlMapper extends BaseMapper
{
    private String serverName                  = null;
    private String localMetadataCollectionGUID = null;
    private String schemaVersion               = null;


    /**
     * Constructor used to write to the control table.
     *
     * @param repositoryName name of the repository connector
     * @param serverName name of consuming server
     * @param localMetadataCollectionGUID local metadata collection for this repository
     * @param schemaVersion version of the schema in use
     */
    public ControlMapper(String repositoryName,
                         String serverName,
                         String localMetadataCollectionGUID,
                         String schemaVersion)
    {
        super(repositoryName);

        this.serverName                  = serverName;
        this.localMetadataCollectionGUID = localMetadataCollectionGUID;
        this.schemaVersion               = schemaVersion;
    }


    /**
     * Constructor when receiving values from the database table.
     *
     * @param repositoryName name of this repository
     * @param controlTable results from the database
     * @throws RepositoryErrorException missing value from database table
     */
    public ControlMapper(String                           repositoryName,
                         List<Map<String, JDBCDataValue>> controlTable) throws RepositoryErrorException
    {
        super(repositoryName);

        if ((controlTable != null) && (!controlTable.isEmpty()))
        {
            serverName = super.getStringPropertyFromColumn(RepositoryColumn.SERVER_NAME.getColumnName(),
                                                           controlTable.get(0),
                                                           true);
            localMetadataCollectionGUID = super.getStringPropertyFromColumn(RepositoryColumn.LOCAL_METADATA_COLLECTION_GUID.getColumnName(),
                                                           controlTable.get(0),
                                                           true);
            schemaVersion = super.getStringPropertyFromColumn(RepositoryColumn.SCHEMA_VERSION.getColumnName(),
                                                              controlTable.get(0),
                                                              true);
        }
    }


    /**
     * Return the name of the server that is using this repository.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Return the local metadata collection identifier.
     *
     * @return string guid
     */
    public String getLocalMetadataCollectionGUID()
    {
        return localMetadataCollectionGUID;
    }


    /**
     * Return the schema version.
     *
     * @return string
     */
    public String getSchemaVersion()
    {
        return schemaVersion;
    }


    /**
     * Return the formatted roe for the control table.
     *
     * @return row
     * @throws RepositoryErrorException missing value
     */
    public Map<String, JDBCDataValue> getControlTableRow() throws RepositoryErrorException
    {
        Map<String, JDBCDataValue> newControlTableRow = new HashMap<>();

        super.setUpStringValueInRow(newControlTableRow, serverName, RepositoryColumn.SERVER_NAME.getColumnName(), true);
        super.setUpStringValueInRow(newControlTableRow, localMetadataCollectionGUID, RepositoryColumn.LOCAL_METADATA_COLLECTION_GUID.getColumnName(), true);
        super.setUpStringValueInRow(newControlTableRow, schemaVersion, RepositoryColumn.SCHEMA_VERSION.getColumnName(), true);

        return newControlTableRow;
    }
}
