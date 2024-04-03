/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The KafkaAnnotationType enum describes the annotation types used by the Apache Kafka survey action service.
 */
public enum KafkaAnnotationType
{
    /**
     * List of topics known to the Apache Kafka server.
     */
    TOPIC_LIST("Apache Kafka Topic List",
                      AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                      OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                      "List of topics known to the Apache Kafka server.",
                      "Topics provide the mechanism to organize events.  They may be explicitly configured, or created dynamically.",
                      null),
    ;


    public final String            name;
    public final AnalysisStep      analysisStep;
    public final String            openMetadataTypeName;
    public final String            summary;
    public final String            explanation;
    public final List<KafkaMetric> metrics;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param analysisStep associated analysis step
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     * @param metrics metrics
     */
    KafkaAnnotationType(String              name,
                        AnalysisStep        analysisStep,
                        String              openMetadataTypeName,
                        String              summary,
                        String              explanation,
                        List<KafkaMetric>  metrics)
    {
        this.name                 = name;
        this.analysisStep         = analysisStep;
        this.openMetadataTypeName = openMetadataTypeName;
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

        for (KafkaAnnotationType kafkaAnnotationType : KafkaAnnotationType.values())
        {
            annotationTypeTypes.add(kafkaAnnotationType.getAnnotationTypeType());
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
        return analysisStep.getName();
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
        annotationTypeType.setAnalysisStepName(analysisStep.getName());
        annotationTypeType.setSummary(summary);
        annotationTypeType.setExplanation(explanation);

        if (metrics != null)
        {
            Map<String, String> metricsMap = new HashMap<>();

            for (KafkaMetric kafkaMetric : metrics)
            {
                metricsMap.put(kafkaMetric.getName(), kafkaMetric.getDescription());
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
