/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.*;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The UnityCatalogAnnotationType enum describes the annotation types used by the unity catalog survey action service.
 */
public enum UnityCatalogAnnotationType implements AnnotationType
{
    RESOURCE_INVENTORY("Log of Unity Catalog (UC) Resources",
                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                 "Log file of resource name, description and deployed implementation type.",
                 "If resource are missing, check the security permissions of the survey service's userId.",
                 AnalysisStep.PRODUCE_INVENTORY.getName(),
                 getProfilePropertiesPropertyName(),
                 null),

    CATALOG_LIST("Capture List of Unity Catalog (UC) Catalogs",
                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                 "Extract the list of visible catalogs in a Unity Catalog (UC) server.",
                 "If catalogs are missing, check the security permissions of the survey service's userId.",
                 AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName(),
                 getProfilePropertiesPropertyName(),
                 null),

    CATALOG_TABLE_SIZES("Capture Unity Catalog (UC) Catalog Table Sizes",
                         OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                         "Extract the sizes of the visible tables in a Unity Catalog (UC) catalog.",
                         "Tables listed include their catalog name and schema name.  If tables are missing, check the security permissions of the survey service's userId.",
                        AnalysisStep.PROFILE_DATA.getName(),
                        null,
                         null),

    SCHEMA_TABLE_SIZES("Capture Table Sizes for a Database Schema",
                       OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                       "Extract the sizes of the visible tables in a Unity Catalog (UC) schema.",
                       "Tables listed include their catalog name and schema name.  If tables are missing, check the security permissions of the survey service's userId.",
                       AnalysisStep.PROFILE_DATA.getName(),
                       null,
                       null),

    SERVER_METRICS("Capture Unity Catalog (UC) Server Metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about a database.",
                   "This annotation retrieves statistics about a Unity Catalog (UC) server and its contents.",
                   AnalysisStep.MEASURE_RESOURCE.getName(),
                   null,
                   UnityCatalogMetric.getServerMetrics()),

    CATALOG_METRICS("Capture Unity Catalog (UC) Catalog Metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about a Unity Catalog (UC) catalog.",
                   "This annotation retrieves statistics about a catalog and its schemas, tables, volumes and functions.",
                    AnalysisStep.MEASURE_RESOURCE.getName(),
                    null,
                    UnityCatalogMetric.getCatalogMetrics()),

    SCHEMA_METRICS("Capture Unity Catalog (UC) Schema Metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about the tables in a Unity Catalog (UC) schema.",
                   "This annotation retrieves statistics about individual tables and columns, and aggregates them into a summary for the schema.",
                   AnalysisStep.MEASURE_RESOURCE.getName(),
                   null,
                   UnityCatalogMetric.getSchemaMetrics()),

    TABLE_METRICS("Capture Unity Catalog (UC) Table Metrics",
                  OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                  "Capture summary statistics about a Unity Catalog (UC) table.",
                  "This annotation retrieves statistics about individual columns and aggregates them into a summary for the table.",
                  AnalysisStep.MEASURE_RESOURCE.getName(),
                  null,
                  UnityCatalogMetric.getTableMetrics()),

    COLUMN_METRICS("Capture Unity Catalog (UC) Column Metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about a Unity Catalog (UC) column.",
                   "This annotation retrieves statistics about an individual column.",
                   AnalysisStep.MEASURE_RESOURCE.getName(),
                   null,
                   UnityCatalogMetric.getColumnMetrics()),

    ;


    public final String             name;
    public final String             openMetadataTypeName;
    public final String             summary;
    public final String             explanation;
    public final String             analysisStepName;
    public final List<String>       profilePropertyNames;
    public final List<SurveyMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param analysisStepName name of the step that produces this annotation
     * @param profilePropertyNames list of property names filled out in the ResourceProfileAnnotation
     * @param metrics optional metrics
     */
    UnityCatalogAnnotationType(String                name,
                               String                openMetadataTypeName,
                               String                summary,
                               String                explanation,
                               String                analysisStepName,
                               List<String>          profilePropertyNames,
                               List<SurveyMetric>    metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
        this.analysisStepName     = analysisStepName;
        this.profilePropertyNames = profilePropertyNames;
        this.metrics              = metrics;
    }


    /**
     * Return the defined annotation types for the Unity Catalog Server
     * survey action service as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getServerAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(RESOURCE_INVENTORY.getAnnotationTypeType());
        annotationTypeTypes.add(SERVER_METRICS.getAnnotationTypeType());
        annotationTypeTypes.add(CATALOG_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.SCHEMA_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.TABLE_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.COLUMN_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.FUNCTION_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.VOLUME_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.MODEL_LIST.getAnnotationTypeType());

        return annotationTypeTypes;
    }


    /**
     * Return the defined annotation types for the UC catalog
     * survey action service as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getCatalogAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(RESOURCE_INVENTORY.getAnnotationTypeType());
        annotationTypeTypes.add(CATALOG_METRICS.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.SCHEMA_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.TABLE_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.COLUMN_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.FUNCTION_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.VOLUME_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.MODEL_LIST.getAnnotationTypeType());

        return annotationTypeTypes;
    }


    /**
     * Return the defined annotation types for the UC schema
     * survey action service as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getSchemaAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(RESOURCE_INVENTORY.getAnnotationTypeType());
        annotationTypeTypes.add(SCHEMA_METRICS.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.TABLE_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyDatabaseAnnotationType.COLUMN_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.FUNCTION_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.VOLUME_LIST.getAnnotationTypeType());
        annotationTypeTypes.add(SurveyResourceManagerAnnotationType.MODEL_LIST.getAnnotationTypeType());

        return annotationTypeTypes;
    }


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
        return analysisStepName;
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
        annotationTypeType.setAnalysisStepName(analysisStepName);
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
        return "AnnotationType{ name='" + name + "}";
    }
}
