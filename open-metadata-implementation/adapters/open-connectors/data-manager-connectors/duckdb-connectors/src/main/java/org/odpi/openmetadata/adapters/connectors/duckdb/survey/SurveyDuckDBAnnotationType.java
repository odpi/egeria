/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The SurveyDuckDBAnnotationType enum describes the annotation types used by the DuckDB database survey action
 * service to describe the federation relationships that a DuckDB database makes use of.  DuckDB can federate
 * with other databases via its "ATTACH" capability, and with external file/object-store data via table
 * functions such as read_parquet(), read_csv() and so on.  Neither of these is captured by the vendor-neutral
 * SurveyDatabaseAnnotationType annotations reused from the open-survey-framework, so this module-local enum
 * adds two new annotation types, both populated as ResourceMeasureAnnotationProperties (the same open metadata
 * type used for the RelationalDataManagerMeasurement/etc measurements) carrying a free-form resourceProperties map.
 */
public enum SurveyDuckDBAnnotationType implements AnnotationType
{
    /**
     * Describes a database that has been ATTACH-ed to the surveyed DuckDB database.
     */
    ATTACHED_SOURCE("Capture Attached Data Source",
                    OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                    "Capture details of a data source that has been attached to the DuckDB database using its ATTACH federation capability.",
                    "This annotation is created once for each non-internal entry found in DuckDB's duckdb_databases() table function.",
                    null,
                    null),

    /**
     * Describes an external file, or object-store, resource that is scanned by a view in the surveyed DuckDB database.
     */
    EXTERNAL_FILE_SOURCE("Capture External File Data Source",
                         OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                         "Capture details of an external file, or object-store, resource that is scanned by a view defined in the DuckDB database using functions such as read_parquet(), read_csv(), read_json(), iceberg_scan() or delta_scan().",
                         "This annotation is created once for each external-scan view found in DuckDB's duckdb_views() table function.",
                         null,
                         null),

    ;


    public final String                          name;
    public final String                          openMetadataTypeName;
    public final String                          summary;
    public final String                          explanation;
    public final List<String>                    profilePropertyNames;
    public final Map<String,String>               metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param profilePropertyNames list of property names filled out - not used by these annotation types
     * @param metrics optional metrics - not used by these annotation types
     */
    SurveyDuckDBAnnotationType(String              name,
                               String              openMetadataTypeName,
                               String              summary,
                               String              explanation,
                               List<String>        profilePropertyNames,
                               Map<String,String>  metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
        this.profilePropertyNames = profilePropertyNames;
        this.metrics              = metrics;
    }


    /**
     * Return the defined annotation types for the DuckDB Database federation discovery processing.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getDuckDBFederationAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (SurveyDuckDBAnnotationType surveyDuckDBAnnotationType : SurveyDuckDBAnnotationType.values())
        {
            annotationTypeTypes.add(surveyDuckDBAnnotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
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
     * Return the metrics created in the survey processing.
     *
     * @return metrics
     */
    @Override
    public Map<String,String> getMetrics()
    {
        return metrics;
    }


    /**
     * Return the list of property names that make up this profile entry.
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
        annotationTypeType.setDescription(summary);
        annotationTypeType.setExplanation(explanation);
        annotationTypeType.setOtherPropertyValues(metrics);

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
        return "SurveyDuckDBAnnotationType{ name='" + name + "}";
    }
}
