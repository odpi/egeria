/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;

import java.util.*;

/**
 * Defines the tables used in a repository database schema.
 */
public enum RepositoryTable implements PostgreSQLTable
{
    /**
     * Control table to identify the server, metadata collection and version that this repository serves.
     */
    CONTROL("control",
               "Control table to identify the server, metadata collection and version that this repository serves.",
               null,
               new RepositoryColumn[]{
                       RepositoryColumn.SERVER_NAME,
                       RepositoryColumn.LOCAL_METADATA_COLLECTION_GUID,
                       RepositoryColumn.SCHEMA_VERSION}
    ),


    /**
     * Common information about an entity instance.
     */
    ENTITY("entity",
           "Common information about an entity instance.",
           new RepositoryColumn[]{
                   RepositoryColumn.INSTANCE_GUID,
                   RepositoryColumn.VERSION},
           new RepositoryColumn[]{
                   RepositoryColumn.VERSION_START_TIME,
                   RepositoryColumn.VERSION_END_TIME,
                   RepositoryColumn.IS_PROXY,
                   RepositoryColumn.TYPE_GUID,
                   RepositoryColumn.TYPE_NAME,
                   RepositoryColumn.CURRENT_STATUS,
                   RepositoryColumn.INSTANCE_PROVENANCE_TYPE,
                   RepositoryColumn.METADATA_COLLECTION_GUID,
                   RepositoryColumn.METADATA_COLLECTION_NAME,
                   RepositoryColumn.MAPPING_PROPERTIES,
                   RepositoryColumn.EFFECTIVE_FROM_TIME,
                   RepositoryColumn.EFFECTIVE_TO_TIME,
                   RepositoryColumn.REPLICATED_BY,
                   RepositoryColumn.CREATED_BY,
                   RepositoryColumn.UPDATED_BY,
                   RepositoryColumn.MAINTAINED_BY,
                   RepositoryColumn.CREATE_TIME,
                   RepositoryColumn.UPDATE_TIME,
                   RepositoryColumn.STATUS_ON_DELETE,
                   RepositoryColumn.INSTANCE_LICENCE,
                   RepositoryColumn.REIDENTIFIED_FROM_GUID}
    ),

    /**
     * Common information about a relationship instance.
     */
    RELATIONSHIP("relationship",
                 "Common information about a relationship instance.",
                 new RepositoryColumn[]{
                         RepositoryColumn.INSTANCE_GUID,
                         RepositoryColumn.VERSION},
                 new RepositoryColumn[]{
                         RepositoryColumn.VERSION_START_TIME,
                         RepositoryColumn.VERSION_END_TIME,
                         RepositoryColumn.TYPE_GUID,
                         RepositoryColumn.TYPE_NAME,
                         RepositoryColumn.END_1_GUID,
                         RepositoryColumn.END_2_GUID,
                         RepositoryColumn.CURRENT_STATUS,
                         RepositoryColumn.INSTANCE_PROVENANCE_TYPE,
                         RepositoryColumn.METADATA_COLLECTION_GUID,
                         RepositoryColumn.METADATA_COLLECTION_NAME,
                         RepositoryColumn.MAPPING_PROPERTIES,
                         RepositoryColumn.EFFECTIVE_FROM_TIME,
                         RepositoryColumn.EFFECTIVE_TO_TIME,
                         RepositoryColumn.REPLICATED_BY,
                         RepositoryColumn.CREATED_BY,
                         RepositoryColumn.UPDATED_BY,
                         RepositoryColumn.MAINTAINED_BY,
                         RepositoryColumn.CREATE_TIME,
                         RepositoryColumn.UPDATE_TIME,
                         RepositoryColumn.STATUS_ON_DELETE,
                         RepositoryColumn.INSTANCE_LICENCE,
                         RepositoryColumn.REIDENTIFIED_FROM_GUID}
    ),


    CLASSIFICATION("classification",
                   "Common information about a classification attached to an entity instance.",
                   new RepositoryColumn[]{
                           RepositoryColumn.INSTANCE_GUID,
                           RepositoryColumn.CLASSIFICATION_NAME,
                           RepositoryColumn.VERSION},
                   new RepositoryColumn[]{
                           RepositoryColumn.VERSION_START_TIME,
                           RepositoryColumn.VERSION_END_TIME,
                           RepositoryColumn.TYPE_GUID,
                           RepositoryColumn.TYPE_NAME,
                           RepositoryColumn.CURRENT_STATUS,
                           RepositoryColumn.INSTANCE_PROVENANCE_TYPE,
                           RepositoryColumn.METADATA_COLLECTION_GUID,
                           RepositoryColumn.METADATA_COLLECTION_NAME,
                           RepositoryColumn.MAPPING_PROPERTIES,
                           RepositoryColumn.EFFECTIVE_FROM_TIME,
                           RepositoryColumn.EFFECTIVE_TO_TIME,
                           RepositoryColumn.REPLICATED_BY,
                           RepositoryColumn.CREATED_BY,
                           RepositoryColumn.UPDATED_BY,
                           RepositoryColumn.MAINTAINED_BY,
                           RepositoryColumn.CREATE_TIME,
                           RepositoryColumn.UPDATE_TIME,
                           RepositoryColumn.STATUS_ON_DELETE}),

    /**
     * Attributes for an entity, or for entity properties that are collections.
     */
    ENTITY_ATTRIBUTE_VALUE("entity_attribute_value",
                           "Attributes for an entity, or for entity properties that are collections.",
                           new RepositoryColumn[]{
                                   RepositoryColumn.INSTANCE_GUID,
                                   RepositoryColumn.VERSION,
                                   RepositoryColumn.PROPERTY_NAME},
                           new RepositoryColumn[]{
                                   RepositoryColumn.ATTRIBUTE_NAME,
                                   RepositoryColumn.PROPERTY_VALUE,
                                   RepositoryColumn.PROPERTY_CATEGORY,
                                   RepositoryColumn.IS_UNIQUE_ATTRIBUTE,
                                   RepositoryColumn.ATTRIBUTE_TYPE_GUID,
                                   RepositoryColumn.ATTRIBUTE_TYPE_NAME}
    ),

    /**
     * Attributes for a classification, or for classification properties that are collections.
     */
    CLASSIFICATION_ATTRIBUTE_VALUE("classification_attribute_value",
                                   "Attributes for a classification, or for classification properties that are collections.",
                                   new RepositoryColumn[]{
                                           RepositoryColumn.INSTANCE_GUID,
                                           RepositoryColumn.CLASSIFICATION_NAME,
                                           RepositoryColumn.VERSION,
                                           RepositoryColumn.PROPERTY_NAME},
                                   new RepositoryColumn[]{
                                           RepositoryColumn.ATTRIBUTE_NAME,
                                           RepositoryColumn.PROPERTY_VALUE,
                                           RepositoryColumn.PROPERTY_CATEGORY,
                                           RepositoryColumn.IS_UNIQUE_ATTRIBUTE,
                                           RepositoryColumn.ATTRIBUTE_TYPE_GUID,
                                           RepositoryColumn.ATTRIBUTE_TYPE_NAME}
    ),

    /**
     * Attributes for a relationship, or for relationship properties that are collections.
     */
    RELATIONSHIP_ATTRIBUTE_VALUE("relationship_attribute_value",
                                 "Attributes for a relationship, or for relationship properties that are collections.",
                                 new RepositoryColumn[]{
                                         RepositoryColumn.INSTANCE_GUID,
                                         RepositoryColumn.VERSION,
                                         RepositoryColumn.PROPERTY_NAME},
                                 new RepositoryColumn[]{
                                         RepositoryColumn.ATTRIBUTE_NAME,
                                         RepositoryColumn.PROPERTY_VALUE,
                                         RepositoryColumn.PROPERTY_CATEGORY,
                                         RepositoryColumn.IS_UNIQUE_ATTRIBUTE,
                                         RepositoryColumn.ATTRIBUTE_TYPE_GUID,
                                         RepositoryColumn.ATTRIBUTE_TYPE_NAME}
    ),

    ;

    private final String                 tableName;
    private final String                 tableDescription;
    private final RepositoryColumn[]     primaryKeys;
    private final RepositoryColumn[]     dataColumns;


    /**
     * Define a repository table.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param primaryKeys list of primary keys
     * @param dataColumns list of additional columns
     */
    RepositoryTable(String                 tableName,
                    String                 tableDescription,
                    RepositoryColumn[]     primaryKeys,
                    RepositoryColumn[]     dataColumns)
    {
        this.tableName        = tableName;
        this.tableDescription = tableDescription;
        this.primaryKeys      = primaryKeys;
        this.dataColumns      = dataColumns;
    }


    /**
     * Return the name of the table.
     *
     * @return name
     */
    @Override
    public String getTableName()
    {
        return tableName;
    }




    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    @Override
    public String getTableName(String schemaName)
    {
        return schemaName + "." + tableName;
    }



    /**
     * Return the description of the table.
     *
     * @return text
     */
    @Override
    public String getTableDescription()
    {
        return tableDescription;
    }


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getPrimaryKeys()
    {
        if (primaryKeys != null)
        {
            return Arrays.asList(primaryKeys);
        }

        return null;
    }


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getDataColumns()
    {
        if (dataColumns != null)
        {
            return Arrays.asList(dataColumns);
        }

        return null;
    }


    /**
     * Return the name to type map for the columns in this table.
     *
     * @return map
     */
    public Map<String, Integer> getColumnNameTypeMap()
    {
        Map<String, Integer> columnNameTypeMap = new HashMap<>();

        if (primaryKeys != null)
        {
            for (RepositoryColumn column: primaryKeys)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }

        if (dataColumns != null)
        {
            for (RepositoryColumn column: dataColumns)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }

        return columnNameTypeMap;
    }


    public List<String> getQualifiedColumnNames()
    {
        List<String> columnNames = new ArrayList<>();

        if (primaryKeys != null)
        {
            for (RepositoryColumn column: primaryKeys)
            {
                columnNames.add(column.getColumnName(tableName));
            }
        }

        if (dataColumns != null)
        {
            for (RepositoryColumn column: dataColumns)
            {
                columnNames.add(column.getColumnName(tableName));
            }
        }

        return columnNames;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<PostgreSQLForeignKey> getForeignKeys()
    {
        return null;
    }


    /**
     * Return the tables for schema building.
     *
     * @return list of tables
     */
    public static List<PostgreSQLTable> getTables()
    {
        return new ArrayList<>(Arrays.asList(RepositoryTable.values()));
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "RepositoryTable{" + tableName + "}";
    }
}
