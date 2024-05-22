/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresMetric describes the metrics for a Postgres Server.
 */
public enum PostgresMetric
{
    DATABASE_SIZE ("Database size", "Number of stored bytes in the database."),
    ROWS_FETCHED ("Rows Fetched", "Number of rows retrieved from any table in the database."),
    ROWS_INSERTED ("Rows Inserted", "Number of rows inserted from any table in the database."),
    ROWS_UPDATED ("Rows Updated", "Number of rows updated from any table in the database."),
    ROWS_DELETED ("Rows Deleted", "Number of rows deleted from any table in the database.."),
    SESSION_TIME ("Session Time", "The length of time (milliseconds) that the database had at least one open session from an external client."),
    ACTIVE_TIME ("Active Time", "The length of time (milliseconds) that the database was being actively queried."),
    TABLE_SIZE ("Table size", "Number of stored bytes in the table."),
    TOTAL_TABLE_SIZE ("Total size of tables", "Sum of the sizes (in bytes) of the tables in the schema."),
    TABLE_NAME ("Table name", "Name of table."),
    TABLE_QNAME ("Table qualified name", "Qualified name of table showing the database name and schema name."),
    TABLE_TYPE ("Table type", "Is this a standard table, view or materialized view?"),
    TABLE_COUNT ("Number of tables", "Count of tables in the database/schema."),
    VIEW_COUNT ("Number of views", "Count of views in the database/schema."),
    MAT_VIEW_COUNT ("Number of materialized views", "Count of materialized views in the database/schema."),
    COLUMN_NAME ("Column name", "Name of the column."),
    COLUMN_QNAME ("Column qualified name", "Qualified name of the column showing the database name, schema name and table name."),
    COLUMN_SIZE ("Column size", "Number of stored bytes in the column."),
    COLUMN_COUNT ("Number of columns", "Count of columns in the database table/view."),
    COLUMN_TYPE ("Column type", "Data type of column."),
    LAST_UPDATE_TIME ("Last Update Time", "Number of files and directories found under the surveyed that can be executed."),
    LAST_STATISTICS_RESET ("Last statistics reset", "Last time that the statistics were reset in the database."),

    ;

    public final String name;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request type
     * @param description description of the request type
     */
    PostgresMetric(String name,
                   String description)
    {
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<PostgresMetric> getDatabaseMetrics()
    {
        List<PostgresMetric> metrics = new ArrayList<>();

        metrics.add(DATABASE_SIZE);

        return metrics;
    }

    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<PostgresMetric> getSchemaMetrics()
    {
        List<PostgresMetric> metrics = new ArrayList<>();

        metrics.add(TOTAL_TABLE_SIZE);
        metrics.add(TABLE_COUNT);
        metrics.add(VIEW_COUNT);
        metrics.add(MAT_VIEW_COUNT);

        return metrics;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<PostgresMetric> getTableMetrics()
    {
        List<PostgresMetric> metrics = new ArrayList<>();

        metrics.add(TABLE_NAME);
        metrics.add(TABLE_QNAME);
        metrics.add(TABLE_SIZE);
        metrics.add(TABLE_TYPE);
        metrics.add(COLUMN_COUNT);


        return metrics;
    }



    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<PostgresMetric> getColumnMetrics()
    {
        List<PostgresMetric> metrics = new ArrayList<>();

        metrics.add(COLUMN_NAME);
        metrics.add(COLUMN_QNAME);
        metrics.add(COLUMN_TYPE);
        metrics.add(COLUMN_SIZE);
        metrics.add(LAST_STATISTICS_RESET);

        return metrics;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PostgresMetric{" + name + "}";
    }
}
