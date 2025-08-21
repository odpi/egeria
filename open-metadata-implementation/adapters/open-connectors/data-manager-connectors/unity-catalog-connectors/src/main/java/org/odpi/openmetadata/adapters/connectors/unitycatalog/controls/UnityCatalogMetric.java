/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RelationalDatabaseMetric describes the metrics for a Relational Database.
 */
public enum UnityCatalogMetric implements SurveyMetric
{
    NO_OF_CATALOGS ( "numberOfCatalogs", "long", "Number of Catalogs", "Number of catalogs defined in this server."),
    NO_OF_FUNCTIONS ( "numberOfFunctions", "long", "Number of Functions", "Number of functions found in the surveyed resource."),
    NO_OF_VOLUMES ( "numberOfVolumes", "long", "Number of Volumes", "Number of volumes found in the surveyed resource."),
    NO_OF_MODELS ( "numberOfModels", "long", "Number of Models", "Number of models found in the surveyed resource."),

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
    UnityCatalogMetric(String propertyName,
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
     * Return the defined server metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getServerMetrics()
    {
        return new ArrayList<>(Arrays.asList(UnityCatalogMetric.values()));
    }


    /**
     * Return the defined catalog metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getCatalogMetrics()
    {
        List<SurveyMetric> metrics = new ArrayList<>();

        metrics.add(RelationalDatabaseMetric.SCHEMA_COUNT);
        metrics.add(UnityCatalogMetric.NO_OF_FUNCTIONS);
        metrics.add(RelationalDatabaseMetric.TABLE_COUNT);
        metrics.add(RelationalDatabaseMetric.COLUMN_COUNT);
        metrics.add(UnityCatalogMetric.NO_OF_VOLUMES);
        metrics.add(UnityCatalogMetric.NO_OF_MODELS);

        return metrics;
    }


    /**
     * Return the defined schema metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getSchemaMetrics()
    {
        List<SurveyMetric> metrics = new ArrayList<>();

        metrics.add(RelationalSchemaMetric.TOTAL_TABLE_SIZE);
        metrics.add(RelationalSchemaMetric.TABLE_COUNT);
        metrics.add(RelationalSchemaMetric.COLUMN_COUNT);

        return metrics;
    }


    /**
     * Return the defined table metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getTableMetrics()
    {
        return RelationalTableMetric.getMetrics();
    }


    /**
     * Return the defined column metrics as a list
     *
     * @return list
     */
    public static List<SurveyMetric> getColumnMetrics()
    {
        return RelationalColumnMetric.getMetrics();
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "UnityCatalogMetric{" + displayName + "}";
    }
}
