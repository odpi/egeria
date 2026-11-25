/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFileAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The SurveyParquetAnnotationType enum describes the annotation types that are specific to the Parquet
 * survey action service.  (The service also produces the standard file measurements annotation.)
 */
public enum SurveyParquetAnnotationType implements AnnotationType
{
    /**
     * Extract the characteristics of the Parquet file from its footer.
     */
    PARQUET_MEASUREMENTS("Extract Parquet File Properties",
                         OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                         AnalysisStep.MEASURE_RESOURCE,
                         "Extract the characteristics of the Parquet file from its footer.",
                         "The footer of a Parquet file records the number of rows, how they are divided into row groups, and how the values are encoded and compressed.  These show which version of the Parquet format the file needs a reader to support.",
                         ParquetFileMetric.getMetrics()),

    /**
     * Extract the schema from the footer of the Parquet file.
     */
    SCHEMA_FROM_FOOTER("Extract Schema From Footer",
                       OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName,
                       AnalysisStep.SCHEMA_EXTRACTION,
                       "Extract the schema from the footer of the Parquet file.",
                       "The schema is built from the column names and types that the writer recorded in the footer of the Parquet file.  A column that contains nested data is described using array, map and struct.",
                       null),

    /**
     * Profile the values in a column using the statistics from the footer of the Parquet file.
     */
    COLUMN_PROFILE("Profile Column From Statistics",
                   OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                   AnalysisStep.PROFILE_DATA,
                   "Profile the values in a column using the statistics from the footer of the Parquet file.",
                   "The writer of a Parquet file records the number of values, the number of nulls and the minimum and maximum values of each column in each row group.  These are combined to describe the column across the whole file.  In addition, if the DuckDB JDBC driver is available, the values of each top-level column are read to discover how many times each value occurs.  This is recorded for the columns that have no more than 100 different values.",
                   ParquetColumnMetric.getMetrics()),
    ;


    /**
     * Name of the annotation type.
     */
    public final String             name;

    /**
     * Open metadata type used for the annotation.
     */
    public final String             openMetadataTypeName;

    /**
     * Analysis step that produces the annotation.
     */
    public final AnalysisStep       analysisStep;

    /**
     * Short description of the annotation type.
     */
    public final String             summary;

    /**
     * Longer description of the annotation type.
     */
    public final String             explanation;

    /**
     * The measurements stored in the annotation.
     */
    public final List<SurveyMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param analysisStep analysis step where this annotation is produced
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param metrics optional metrics
     */
    SurveyParquetAnnotationType(String              name,
                                String              openMetadataTypeName,
                                AnalysisStep        analysisStep,
                                String              summary,
                                String              explanation,
                                List<SurveyMetric>  metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.analysisStep         = analysisStep;
        this.summary              = summary;
        this.explanation          = explanation;
        this.metrics              = metrics;
    }


    /**
     * Return the annotation types produced by the Parquet survey action service as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getParquetSurveyAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(SurveyFileAnnotationType.MEASUREMENTS.getAnnotationTypeType());

        for (SurveyParquetAnnotationType annotationType : SurveyParquetAnnotationType.values())
        {
            annotationTypeTypes.add(annotationType.getAnnotationTypeType());
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
        return analysisStep.getName();
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
        if (metrics != null)
        {
            Map<String, String> metricsMap = new HashMap<>();

            for (SurveyMetric surveyMetric : metrics)
            {
                metricsMap.put(surveyMetric.getDisplayName(), surveyMetric.getDescription());
            }

            return metricsMap;
        }

        return null;
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
        annotationTypeType.setAnalysisStepName(analysisStep.getName());
        annotationTypeType.setDescription(summary);
        annotationTypeType.setExplanation(explanation);
        annotationTypeType.setOtherPropertyValues(this.getMetrics());

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
        return "SurveyParquetAnnotationType{ name='" + name + "}";
    }
}
