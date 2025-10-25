/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The AnnotationType enum describes the annotation types used by the survey action service.
 */
public enum OMAGServerPlatformAnnotationType
{
    SERVER_TYPES("Capture number of each type of server.",
                         OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                         "Extract the list of OMAG Servers running on the platform and count how many of each server type.",
                         "The OMAG Server Platform hosts OMAG Servers.   There are different types of servers for different purposes.  This annotation shows how many of each type of server are present.",
                         null),

    SERVER_NAMES("Capture server names and description.",
                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                 "Extract the list of OMAG Servers running on the platform and count how many of each server type.",
                 "The OMAG Server Platform hosts OMAG Servers.   There are different types of servers for different purposes.  This annotation shows how many of each type of server are present.",
                 null),

    PLATFORM_METRICS("Capture OMAG Server Platform metrics",
                   OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                   "Capture summary statistics about an OMAG Server Platform.",
                   "This annotation retrieves statistics about an OMAG Server Platform.",
                   OMAGServerPlatformMetric.getMetrics()),

    SERVER_AVAILABILITY("Capture Server Availability",
                  OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                  "Capture the percentage of time that the each server has been available since the platform started.",
                  "This annotation uses the server history and current start time to calculate the percentage of the time that the server has been available since the platform started.",
                  null),
    ;


    public final String             name;
    public final String             openMetadataTypeName;
    public final String             summary;
    public final String             explanation;
    public final List<SurveyMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param metrics optional metrics
     */
    OMAGServerPlatformAnnotationType(String                name,
                                     String                openMetadataTypeName,
                                     String                summary,
                                     String                explanation,
                                     List<SurveyMetric>    metrics)
    {
        this.name                 = name;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
        this.metrics              = metrics;
    }


    /**
     * Return the defined annotation types for the platform survey.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (OMAGServerPlatformAnnotationType annotationType : OMAGServerPlatformAnnotationType.values())
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
        annotationTypeType.setDescription(summary);
        annotationTypeType.setExplanation(explanation);

        if (metrics != null)
        {
            Map<String, String> metricsMap = new HashMap<>();

            for (SurveyMetric surveyMetric : metrics)
            {
                metricsMap.put(surveyMetric.getDisplayName(), surveyMetric.getDescription());
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
