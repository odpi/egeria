/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RelationalDatabaseMetric describes the metrics for a Relational Database.
 */
public enum RelationalDatabaseMetric implements SurveyMetric
{
    SCHEMA_COUNT  ("schemaCount", "long", "Number of schemas", "Number of schemas found in the surveyed resource."),
    TABLE_COUNT   ("tableCount", "long", "Number of tables", "Number of tables found in the surveyed resource."),
    COLUMN_COUNT  ("columnCount", "long","Number of columns", "Count of all columns in the table/views."),
    DATA_SIZE     ("dataSize", "long", "Data size", "Number of stored bytes of data in the surveyed resource."),
    ROWS_FETCHED  ( "rowsFetched", "long", "Rows Fetched", "Number of rows retrieved from any table in the database."),
    ROWS_INSERTED ( "rowsInserted", "long", "Rows Inserted", "Number of rows inserted from any table in the database."),
    ROWS_UPDATED  ( "rowsUpdated", "long", "Rows Updated", "Number of rows updated from any table in the database."),
    ROWS_DELETED  ( "rowsDeleted", "long", "Rows Deleted", "Number of rows deleted from any table in the database.."),
    SESSION_TIME  ( "totalSessionTime", "double", "Session Time", "The length of time (milliseconds) that the database had at least one open session from an external client."),
    ACTIVE_TIME   ( "totalActiveTime", "double", "Active Time", "The length of time (milliseconds) that the database was being actively queried."),
    LAST_STATISTICS_RESET ( "lastStatisticsReset", "date", "Last statistics reset", "Last time that the statistics were reset in the database."),

    ;

    public final String propertyName;
    public final String dataType;
    public final String displayName;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param propertyName name of the property used to store the measurement
     * @param dataType data type of property
     * @param displayName name of the request type
     * @param description description of the request type
     */
    RelationalDatabaseMetric(String propertyName,
                             String dataType,
                             String displayName,
                             String description)
    {
        this.propertyName = propertyName;
        this.dataType     = dataType;
        this.displayName  = displayName;
        this.description  = description;
    }


    /**
     * Return the property name used to store the measurement.
     *
     * @return name
     */
    @Override
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Return the data type of the property used to store the measure.
     *
     * @return data type name
     */
    @Override
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getMetrics()
    {
        return new ArrayList<>(Arrays.asList(RelationalDatabaseMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RelationalDatabaseMetric{" + displayName + "}";
    }
}
