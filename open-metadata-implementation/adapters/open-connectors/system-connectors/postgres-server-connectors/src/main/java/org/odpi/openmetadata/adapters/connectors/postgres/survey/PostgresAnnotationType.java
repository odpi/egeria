/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The PostgresAnnotationType enum describes the annotation types used by the PostgreSQL server survey action service.
 */
public enum PostgresAnnotationType
{
    DATABASE_SIZES("Capture Database Sizes",
                   OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                   "Extract the sizes of the visible databases.",
                   "The databases listed are the ones that are visible to the survey action connector.  A size of zero means the the database size can not be extracted. Otherwise the profileCount value is set to the size in bytes of each database.",
                   null),

    DATABASE_TABLE_SIZES("Capture Database Table Sizes",
                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                 "Extract the sizes of the visible tables in a database.",
                 "Tables listed include their database name and schema name.  If tables are missing, check the security permissions of the survey service's database userId.",
                 null),

    DATABASE_SCHEMA_TABLE_SIZES("Capture Table Sizes for a Database Schema",
                         OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                         "Extract the sizes of the visible tables in a database schema.",
                         "Tables listed include their database name and schema name.  If tables are missing, check the security permissions of the survey service's database userId.",
                         null),

    DATABASE_METRICS("Capture Database Metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about a database.",
                   "This annotation retrieves statistics about individual tables and columns, and aggregates them into a summary for the database.",
                   PostgresMetric.getDatabaseMetrics()),
    SCHEMA_METRICS("Capture Database Schema Metrics",
                  OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                  "Capture summary statistics about the database tables in a database schema.",
                  "This annotation retrieves statistics about individual tables and columns, and aggregates them into a summary for the schema.",
                  PostgresMetric.getSchemaMetrics()),

    TABLE_METRICS("Capture Database Table Metrics",
                OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                "Capture summary statistics about a database table.",
                "This annotation retrieves statistics about individual columns and aggregates them into a summary for the table.",
                PostgresMetric.getTableMetrics()),

    COLUMN_METRICS("Capture Database Column Metrics",
                  OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                  "Capture summary statistics about a database column.",
                  "This annotation retrieves statistics about an individual column.",
                  PostgresMetric.getColumnMetrics()),

    COLUMN_VALUES("Capture Frequent Values for Column",
                  OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                  "Capture the most common values stored in the column and their frequencies.",
                  "This annotation extracts the statistics maintained by the database.",
                  null),
    ;


    public final String               name;
    public final String               openMetadataTypeName;
    public final String               summary;
    public final String               explanation;
    public final List<PostgresMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param metrics optional metrics
     */
    PostgresAnnotationType(String                name,
                           String                openMetadataTypeName,
                           String                summary,
                           String                explanation,
                           List<PostgresMetric>  metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
        this.metrics              = metrics;
    }


    /**
     * Return the defined annotation types for the PostgreSQL Server
     * survey action service as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getPostgresServerAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(PostgresAnnotationType.DATABASE_SIZES.getAnnotationTypeType());

        return annotationTypeTypes;
    }


    /**
     * Return the defined annotation types for the PostgreSQL Database.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getPostgresDatabaseAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (PostgresAnnotationType atlasAnnotationType : PostgresAnnotationType.values())
        {
            annotationTypeTypes.add(atlasAnnotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
    }


    /**
     * Return the name of the annotation type.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the analysis step that produces this type of annotation.
     *
     * @return analysis step name
     */
    public String getAnalysisStep()
    {
        return AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName();
    }


    /**
     * Return the name of the open metadata type used for this type of annotation.
     *
     * @return type name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Return the short description of the annotation type.
     *
     * @return text
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the annotation type.
     *
     * @return text
     */
    public String getExplanation()
    {
        return explanation;
    }


    /**
     * Return the description of this annotation type that can be used in a Connector Provider for a
     * Survey Action Service.
     *
     * @return annotationType type
     */
    public AnnotationTypeType getAnnotationTypeType()
    {
        AnnotationTypeType annotationTypeType = new AnnotationTypeType();

        annotationTypeType.setName(name);
        annotationTypeType.setOpenMetadataTypeName(openMetadataTypeName);
        annotationTypeType.setAnalysisStepName(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());
        annotationTypeType.setSummary(summary);
        annotationTypeType.setExplanation(explanation);

        if (metrics != null)
        {
            Map<String, String> metricsMap = new HashMap<>();

            for (PostgresMetric postgresMetric : metrics)
            {
                metricsMap.put(postgresMetric.getName(), postgresMetric.getDescription());
            }

            annotationTypeType.setOtherPropertyValues(metricsMap);
        }

        return annotationTypeType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "AnnotationType{ name='" + name + "}";
    }
}
