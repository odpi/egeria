/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The CocoClinicalTrialsAnnotationType enum describes the annotation types used by the Certify Weekly Measurements survey action service.
 */
public enum CocoClinicalTrialsAnnotationType implements AnnotationType
{
    SCHEMA_VALIDATION("Schema Validation",
                      OpenMetadataType.QUALITY_ANNOTATION.typeName,
                      AnalysisStep.SCHEMA_VALIDATION,
                      "Validate that the structure of the data in the resource matches the schema attached to the asset.",
                      "The assumption is that the schema defines the specification for the data.  Any deviation from the schema is an error.",
                      CocoClinicalTrialQualityDimension.getSchemaValidationQualityDimensions()),

    PATIENT_ID_VALIDATION("Patient Identifier Validation",
                    OpenMetadataType.QUALITY_ANNOTATION.typeName,
                    AnalysisStep.DATA_VALIDATION,
                    "Validate that the patient identifier is between 1 and 32.",
                    "The clinical trial has recruited 32 patients, each is given an identifier number to hide their identity. .",
                    CocoClinicalTrialQualityDimension.getPatientIdValidationQualityDimensions()),

    DATE_VALIDATION("Date Validation",
                      OpenMetadataType.QUALITY_ANNOTATION.typeName,
                      AnalysisStep.DATA_VALIDATION,
                      "Validate that the date of the measurement is within the allowed date range.",
                      "The clinical trial runs for a specific period.  Therefore the measurements should be dated within this period.",
                      CocoClinicalTrialQualityDimension.getDateValidationQualityDimensions()),

    ANGLE_VALIDATION("Angle Validation",
                    OpenMetadataType.QUALITY_ANNOTATION.typeName,
                    AnalysisStep.DATA_VALIDATION,
                    "Validate that the angle measured for each leg is within the allowed -90 to +90 degree range.",
                    "The measurements are taken on a flat surface so the angle cannot be outside the range shown above.  A negative value means that the hip rotates inwards.  A positive value means that the hip rotates outwards",
                    CocoClinicalTrialQualityDimension.getAngleValidationQualityDimensions()),

    FAILED_TO_PASS_QUALITY_GATE("Failed To Pass Quality Gate",
                 OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName,
                 AnalysisStep.PRODUCE_ACTIONS,
                 "Determine whether the quality checks passed.",
                 "If all the quality checks passed, then the supplied certification is added to the asset.  If not, this request for action is created and is linked to the failing annotations and asset.",
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
    CocoClinicalTrialsAnnotationType(String              name,
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
    public static List<AnnotationTypeType> getAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (CocoClinicalTrialsAnnotationType annotationType : CocoClinicalTrialsAnnotationType.values())
        {
            annotationTypeTypes.add(annotationType.getAnnotationTypeType());
        }

        return annotationTypeTypes;
    }


    /**
     * Return the defined annotation types as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getCertifyWeeklyMeasurementsSurveyAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        annotationTypeTypes.add(SCHEMA_VALIDATION.getAnnotationTypeType());
        annotationTypeTypes.add(PATIENT_ID_VALIDATION.getAnnotationTypeType());
        annotationTypeTypes.add(DATE_VALIDATION.getAnnotationTypeType());
        annotationTypeTypes.add(ANGLE_VALIDATION.getAnnotationTypeType());
        annotationTypeTypes.add(FAILED_TO_PASS_QUALITY_GATE.getAnnotationTypeType());

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
        return "AnnotationType{ name='" + name + "}";
    }
}
