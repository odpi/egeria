/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.measurements;

import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RelationalTableMetric describes the metrics for a Relational Table.
 */
public enum RelationalTableMetric implements SurveyMetric
{
    /**
     * Number of stored bytes in the table.
     */
    TABLE_SIZE ("tableSize", DataType.LONG.getName(), "Table Size", "Number of stored bytes in the table."),

    /**
     * Name of table.
     */
    TABLE_NAME ("tableName", DataType.STRING.getName(),"Table Name", "Name of table."),

    /**
     * Qualified name of table showing the database name and schema name.
     */
    TABLE_QNAME ("tableQualifiedName", DataType.STRING.getName(),"Table Qualified Name", "Qualified name of table showing the database name and schema name."),

    /**
     * Is this a standard table, view or materialized view?
     */
    TABLE_TYPE ("tableType", DataType.STRING.getName(),"Table Type", "Is this a standard table, view or materialized view?"),

    /**
     * Who owns this table?
     */
    TABLE_OWNER("tableOwner", DataType.STRING.getName(),"Table Owner", "Who owns this table?"),

    /**
     * Count of columns in the table/view.
     */
    COLUMN_COUNT ("columnCount", DataType.LONG.getName(),"Number of columns", "Count of columns in the table/view."),

    /**
     * Count of the number of rows inserted into this table since the last statistics reset.
     */
    NUMBER_OF_ROWS_INSERTED("numberOfRowsInserted", DataType.LONG.getName(), "Number Of Rows Inserted", "Count of the number of rows inserted into this table since the last statistics reset."),

    /**
     * Count of the number of rows updated in this table since the last statistics reset.
     */
    NUMBER_OF_ROWS_UPDATED("numberOfRowsUpdated", DataType.LONG.getName(), "Number Of Rows Updated", "Count of the number of rows updated in this table since the last statistics reset."),

    /**
     * Count of the number of rows deleted from this table since the last statistics reset.
     */
    NUMBER_OF_ROWS_DELETED("numberOfRowsDeleted", DataType.LONG.getName(), "Number Of Rows Deleted", "Count of the number of rows deleted from this table since the last statistics reset."),

    /**
     * Is this table populated (typically true)?
     */
    IS_POPULATED("isPopulated", DataType.BOOLEAN.getName(), "Is Populated", "Is this table populated (typically true)?"),

    /**
     * Is this table indexed?
     */
    HAS_INDEXES("hasIndexes", DataType.BOOLEAN.getName(), "Has Indexes", "Is this table indexed?"),

    /**
     * Does this table have rules attached?
     */
    HAS_RULES("hasRules", DataType.BOOLEAN.getName(), "Has Rules", "Does this table have rules attached?"),

    /**
     * Does this table have triggers attached?
     */
    HAS_TRIGGERS("hasTriggers", DataType.BOOLEAN.getName(), "Has Triggers", "Does this table have triggers attached?"),

    /**
     * Is this table configured for row-based security?
     */
    HAS_ROW_SECURITY("hasRowSecurity", DataType.BOOLEAN.getName(), "Has Row Security", "Is this table configured for row-based security?"),

    /**
     * Which query, if any, is used to create this view or materialized view?
     */
    QUERY_DEFINITION("queryDefinition", DataType.STRING.getName(), "Query Definition", "Which query, if any, is used to create this view or materialized view?"),

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
    RelationalTableMetric(String propertyName,
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
        return new ArrayList<>(Arrays.asList(RelationalTableMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RelationalTableMetric{" + displayName + "}";
    }
}
