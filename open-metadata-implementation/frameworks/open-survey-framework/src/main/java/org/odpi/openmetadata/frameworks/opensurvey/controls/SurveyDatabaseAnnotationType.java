/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;

import java.util.*;


/**
 * The SurveyDatabaseAnnotationType enum describes the annotation types used by the PostgreSQL server survey action service.
 */
public enum SurveyDatabaseAnnotationType implements AnnotationType
{
    SCHEMA_LIST("Capture List of Schemas",
                OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                "Extract the list of visible schema in the surveyed resource.",
                "Schemas listed include their catalog name and schema name.  If schemas are missing, check the security permissions of the survey service's userId.",
                getProfilePropertiesPropertyName(),
                null),

    TABLE_LIST("Capture List of Tables",
               OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
               "Extract the list of visible tables in the surveyed Unity Catalog (UC) resource (server, catalog or schema).",
               "Tables listed include their catalog name, schema name and table name.  If tables are missing, check the security permissions of the survey service's userId.",
               getProfilePropertiesPropertyName(),
               null),

    COLUMN_LIST("Capture List of Table Columns",
                OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                "Extract the list of visible columns within the tables in the surveyed resource.",
                "Tables listed include their catalog name, schema name and table name.  If tables are missing, check the security permissions of the survey service's userId.",
                getProfilePropertiesPropertyName(),
                null),

    DATABASE_TABLE_SIZES("Capture Database Table Sizes",
                         OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                         "Extract the sizes of the visible tables in a database.",
                         "Tables listed include their database name and schema name.  If tables are missing, check the security permissions of the survey service's database userId.",
                         null,
                         null),

    DATABASE_SCHEMA_TABLE_SIZES("Capture Table Sizes for a Database Schema",
                                OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                "Extract the sizes of the visible tables in a database schema.",
                                "Tables listed include their database name and schema name.  If tables are missing, check the security permissions of the survey service's database userId.",
                                null,
                                null),

    DATABASE_MEASUREMENTS("Capture Database Measurements",
                          OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                          "Capture summary statistics about a database.",
                          "This annotation retrieves statistics about a database and its usage.",
                          null,
                          RelationalDatabaseMetric.getMetrics()),

    SCHEMA_MEASUREMENTS("Capture Database Schema Measurements",
                        OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                        "Capture summary statistics about the database tables in a database schema.",
                        "This annotation retrieves statistics about individual tables and columns, and aggregates them into a summary for the schema.",
                        null,
                        RelationalSchemaMetric.getMetrics()),

    TABLE_MEASUREMENTS("Capture Database Table Measurements",
                       OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                       "Capture summary statistics about a database table.",
                       "This annotation retrieves statistics about individual columns and aggregates them into a summary for the table.",
                       null,
                       RelationalTableMetric.getMetrics()),

    COLUMN_MEASUREMENTS("Capture Database Column Measurements",
                        OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                        "Capture summary statistics about a database column.",
                        "This annotation retrieves statistics about an individual column.",
                        null,
                        RelationalColumnMetric.getMetrics()),

    COLUMN_VALUES("Capture Frequent Values for Column",
                  OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                  "Capture the most common values stored in the column and their frequencies.",
                  "This annotation extracts the statistics maintained by the database.",
                  null,
                  null),
    ;


    public final String             name;
    public final String             openMetadataTypeName;
    public final String             summary;
    public final String             explanation;
    public final List<String>       profilePropertyNames;
    public final List<SurveyMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param profilePropertyNames list of property names filled out in the ResourceProfileAnnotation
     * @param metrics optional metrics
     */
    SurveyDatabaseAnnotationType(String                name,
                                 String                openMetadataTypeName,
                                 String                summary,
                                 String                explanation,
                                 List<String>          profilePropertyNames,
                                 List<SurveyMetric>    metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
        this.profilePropertyNames = profilePropertyNames;
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

        annotationTypeTypes.add(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getAnnotationTypeType());

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

        for (SurveyDatabaseAnnotationType surveyDatabaseAnnotationType : SurveyDatabaseAnnotationType.values())
        {
            annotationTypeTypes.add(surveyDatabaseAnnotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
    }


    /**
     * Identify the properties being used in the annotation.
     *
     * @return list of property names (fixed to resourceProperties)
     */
    private static List<String> getProfilePropertiesPropertyName()
    {
        List<String> propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.PROFILE_PROPERTIES.name);

        return propertyNames;
    }


    /**
     * Return the name of the annotation type.
     *
     * @return string name
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * Return the analysis step that produces this type of annotation.
     *
     * @return analysis step name
     */
    @Override
    public String getAnalysisStep()
    {
        return AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName();
    }


    /**
     * Return the name of the open metadata type used for this type of annotation.
     *
     * @return type name
     */
    @Override
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Return the short description of the annotation type.
     *
     * @return text
     */
    @Override
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description of the annotation type.
     *
     * @return text
     */
    @Override
    public String getExplanation()
    {
        return explanation;
    }

    /**
     * Return the expression used in the annotation type processing.
     *
     * @return string
     */
    @Override
    public String getExpression()
    {
        return null;
    }


    /**
     * Return the list of property names that make up this profile entry
     *
     * @return list of property names
     */
    public List<String> getProfilePropertyNames()
    {
        return profilePropertyNames;
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

            for (SurveyMetric relationalDatabaseMetric : metrics)
            {
                metricsMap.put(relationalDatabaseMetric.getDisplayName(), relationalDatabaseMetric.getDescription());
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
        return "SurveyDatabaseAnnotationType{ name='" + name + "}";
    }
}
