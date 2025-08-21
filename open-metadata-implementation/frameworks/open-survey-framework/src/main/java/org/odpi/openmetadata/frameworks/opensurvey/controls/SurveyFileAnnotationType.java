/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.FileMetric;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The SurveyFileAnnotationType enum describes the annotation types used by the File survey action service.
 */
public enum SurveyFileAnnotationType implements AnnotationType
{
    MEASUREMENTS("Extract File Properties",
                 OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                 AnalysisStep.MEASURE_RESOURCE,
                 "Extract properties from the file.",
                 "Extract the properties that visible through the standard File interface and classify the file using reference data.",
                 FileMetric.getMetrics()),

    DERIVE_SCHEMA_FROM_DATA("Derive Schema From Data",
                 OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName,
                 AnalysisStep.SCHEMA_EXTRACTION,
                 "Extract schema from column names and values.",
                 "The schema is built using the values in the first line of the CSV file.  The survey action service will attempt to derive the type of data in each column based on the values stored. If this is not possible the type of the column is assumed to be string.",
                 null),
    ;


    public final String             name;
    public final String             openMetadataTypeName;
    public final AnalysisStep       analysisStep;
    public final String             summary;
    public final String             explanation;
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
    SurveyFileAnnotationType(String              name,
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
     * Return the defined annotation types as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getCSVSurveyAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (SurveyFileAnnotationType atlasAnnotationType : SurveyFileAnnotationType.values())
        {
            annotationTypeTypes.add(atlasAnnotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
    }


    /**
     * Return the defined annotation types as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getFileSurveyAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(MEASUREMENTS.getAnnotationTypeType());

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

            for (SurveyMetric folderMetric : metrics)
            {
                metricsMap.put(folderMetric.getDisplayName(), folderMetric.getDescription());
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
        return "SurveyFileAnnotationType{ name='" + name + "}";
    }
}
