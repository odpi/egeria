/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.measurements;

import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RelationalSchemaMetric describes the metrics for a Relational Schema.
 */
public enum RelationalSchemaMetric implements SurveyMetric
{
    QUALIFIED_SCHEMA_NAME("qualifiedSchemaName", DataType.STRING.getName(), "Qualified Schema Name", "Name of the schema qualified with its database name."),
    SCHEMA_NAME("schemaName", DataType.STRING.getName(), "Schema Name", "Name of the schema."),
    TOTAL_TABLE_SIZE ("totalTableSize", DataType.LONG.getName(), "Total size of tables", "Sum of the sizes (in bytes) of the tables in the schema."),
    TABLE_COUNT ("tableCount", DataType.LONG.getName(), "Number of tables", "Count of tables in the schema."),
    VIEW_COUNT ("viewCount",DataType.LONG.getName(), "Number of views", "Count of views in the schema."),
    MAT_VIEW_COUNT ("materializedViewCount", DataType.LONG.getName(), "Number of materialized views", "Count of materialized views in the database/schema."),
    COLUMN_COUNT ("columnCount", DataType.LONG.getName(),"Number of columns", "Count of all columns in the table/views."),
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
    RelationalSchemaMetric(String propertyName,
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
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Return the data type of the property used to store the measure.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
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
        return "RelationalSchemaMetric{" + displayName + "}";
    }
}
