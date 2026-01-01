/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLView;

import java.util.*;

/**
 * Defines the tables used in a survey report database schema.
 */
public enum HarvestSurveysView implements PostgreSQLView
{
    /**
     * Details of known relational schemas.
     */
    RELATIONAL_SCHEMA("sr_relational_schema",
                "Details of known relational schemas.",
                      new HarvestSurveysTable[]{},
                      new HarvestSurveysColumn[]{
                        HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.DESCRIPTION}),


    /**
     * Details of known relational tables.
     */
    RELATIONAL_TABLE("sr_relational_table",
                "Details of known relational tables.",
                     new HarvestSurveysTable[]{},
                     new HarvestSurveysColumn[]{
                        HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.DESCRIPTION}),


    /**
     * Details of columns in known relational tables.
     */
    RELATIONAL_COLUMN("sr_relational_column",
               "Details of columns in known relational tables.",
                      new HarvestSurveysTable[]{},
                      new HarvestSurveysColumn[]{
                       HarvestSurveysColumn.ANNOTATION_GUID,
                       HarvestSurveysColumn.SURVEY_REPORT_GUID,
                       HarvestSurveysColumn.METADATA_COLLECTION_ID,
                       HarvestSurveysColumn.SUBJECT_GUID,
                       HarvestSurveysColumn.SUBJECT_TYPE,
                       HarvestSurveysColumn.CREATION_TIME,
                       HarvestSurveysColumn.RESOURCE_NAME,
                       HarvestSurveysColumn.DESCRIPTION}),

    /**
     * Details of a directory identified as a file volume.
     */
    FILE_VOLUME("sr_file_volume",
                "Details of a directory identified as a file volume.",
                new HarvestSurveysTable[]{},
                new HarvestSurveysColumn[]{
                        HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.DESCRIPTION}),

    /**
     * Details of known executable functions.
     */
    EXECUTABLE_FUNCTION("sr_executable_function",
                  "Details of known executable functions.",
                        new HarvestSurveysTable[]{},
                        new HarvestSurveysColumn[]{
                          HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.DESCRIPTION}),

    /**
     * Details of known analytical models.
     */
    ANALYTICAL_MODEL("sr_analytical_model",
                  "Details of known analytical models.",
                     new HarvestSurveysTable[]{},
                     new HarvestSurveysColumn[]{
                          HarvestSurveysColumn.ANNOTATION_GUID,
                          HarvestSurveysColumn.SURVEY_REPORT_GUID,
                          HarvestSurveysColumn.METADATA_COLLECTION_ID,
                          HarvestSurveysColumn.SUBJECT_GUID,
                          HarvestSurveysColumn.SUBJECT_TYPE,
                          HarvestSurveysColumn.CREATION_TIME,
                          HarvestSurveysColumn.RESOURCE_NAME,
                          HarvestSurveysColumn.DESCRIPTION}),

    /**
     * Details of known event topics.
     */
    EVENT_TOPIC("sr_event_topic",
                "Details of known event topics.",
                new HarvestSurveysTable[]{},
                new HarvestSurveysColumn[]{
                        HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.DESCRIPTION}),

    /**
     * Details of known API operations that can be called across the network.
     */
    API_OPERATION("sr_api_operation",
                "Details of known API operations that can be called across the network.",
                new HarvestSurveysTable[]{},
                new HarvestSurveysColumn[]{
                        HarvestSurveysColumn.SURVEY_REPORT_GUID,
                        HarvestSurveysColumn.SUBJECT_GUID,
                        HarvestSurveysColumn.ANNOTATION_GUID,
                        HarvestSurveysColumn.RESOURCE_NAME,
                        HarvestSurveysColumn.METADATA_COLLECTION_ID,
                        HarvestSurveysColumn.SUBJECT_TYPE,
                        HarvestSurveysColumn.CREATION_TIME,
                        HarvestSurveysColumn.DESCRIPTION}),

    ;

    private final String                 viewName;
    private final String                 viewDescription;
    private final HarvestSurveysTable[]  tables;
    private final HarvestSurveysColumn[] dataColumns;


    /**
     * Define a repository table.
     *
     * @param viewName name of the view
     * @param viewDescription description of the view
     * @param tables list of primary keys
     * @param dataColumns list of additional columns
     */
    HarvestSurveysView(String                 viewName,
                       String                 viewDescription,
                       HarvestSurveysTable[]  tables,
                       HarvestSurveysColumn[] dataColumns)
    {
        this.viewName        = viewName;
        this.viewDescription = viewDescription;
        this.tables          = tables;
        this.dataColumns     = dataColumns;
    }


    /**
     * Return the name of the table.
     *
     * @return name
     */
    @Override
    public String getViewName()
    {
        return viewName;
    }




    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    @Override
    public String getViewName(String schemaName)
    {
        return schemaName + "." + viewName;
    }



    /**
     * Return the description of the table.
     *
     * @return text
     */
    @Override
    public String getViewDescription()
    {
        return viewDescription;
    }


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    public List<PostgreSQLTable> getTables()
    {
        if (tables != null)
        {
            return Arrays.asList(tables);
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

        if (dataColumns != null)
        {
            for (HarvestSurveysColumn column: dataColumns)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }


        return columnNameTypeMap;
    }


    @Override
    public String toString()
    {
        return "HarvestSurveysTable{" + viewName + "}";
    }
}
