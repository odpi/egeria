/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The AtlasAnnotationType enum describes the annotation types used by the Apache Atlas survey action service.
 */
public enum AtlasAnnotationType
{
    MEASUREMENTS("Apache Atlas Server Metrics",
                 AnalysisStep.MEASURE_RESOURCE,
                 OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                 "Metrics maintained by Apache Atlas.",
                 "These metrics are extracted through the Apache Atlas REST API.",
                 AtlasMetric.getAtlasMetrics()),

    TYPE_ANALYSIS("Apache Atlas Type Analysis",
                  AnalysisStep.SCHEMA_EXTRACTION,
                  OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName,
                  "Extract the types defined by the Apache Atlas server.",
                  "The Apache Atlas types are converted into a graph schema that is attached to the SoftwareServer asset that represents the Apache Atlas server.",
                  null),

    ENTITY_ATTACHED_CLASSIFICATIONS("Apache Atlas Attached Classification Types",
                                    AnalysisStep.PROFILE_DATA,
                                    OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                    "Count of classification types attached to this type of entity.",
                                    "Classifications are used to group entities with similar characteristics together.",
                                    null),

    ENTITY_ATTACHED_TO_END1("Apache Atlas End 1 Attached Relationship Types",
                                    AnalysisStep.PROFILE_DATA,
                                    OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                    "Count of different types of relationships attached to this type of entity at End 1.",
                                    "Entities are linked together using relationships.  These are the relationships where this entity type is attached to end 1 of the relationship type.",
                                    null),

    ENTITY_ATTACHED_TO_END2("Apache Atlas End 2 Attached Relationship Types",
                            AnalysisStep.PROFILE_DATA,
                            OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                            "Count of different types of relationships attached to this type of entity at End 2.",
                            "Entities are linked together using relationships.  These are the relationships where this entity type is attached to end 2 of the relationship type.",
                            null),

    ENTITY_ATTACHED_LABELS("Apache Atlas Attached Labels",
                            AnalysisStep.PROFILE_DATA,
                            OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                            "Count of different types of labels attached to this type of entity.",
                            "Entities may optionally have informal labels attached to them.",
                            null),

    ENTITY_ATTACHED_BIZ_METADATA("Apache Atlas Attached Business Metadata Types",
                           AnalysisStep.PROFILE_DATA,
                           OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                           "Count of the different types of business metadata properties attached to this type of entity.",
                           "Entities may optionally have business metadata properties attached to them.",
                           null),

    CLASSIFICATION_ATTACHED_ENTITIES("Apache Atlas Attached Entity Types By Classification",
                                 AnalysisStep.PROFILE_DATA,
                                 OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                 "Count of entities by type where this classification is attached, organized by entity type.",
                                 "Classifications are used to group entities with similar characteristics together.",
                                 null),

    BIZ_METADATA_ATTACHED_ENTITIES("Apache Atlas Attached Entity Types By Business Metadata",
                                     AnalysisStep.PROFILE_DATA,
                                     OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                     "Count of entities where this type of business metadata properties are attached, organized by entity type.",
                                     "Entities may optionally have business metadata properties attached to them.",
                                     null),

    END1_ENTITY_TYPES("Apache Atlas Attached End 1 Entity Types",
                                   AnalysisStep.PROFILE_DATA,
                                   OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                   "Count of entity types attached at end 1 of this type of relationship.",
                                   "Entities are linked together using relationships.  There are different types of relationships. These are the entity types attached to end 1 of this relationship type.",
                                   null),

    END2_ENTITY_TYPES("Apache Atlas Attached End 2 Entity Types",
                      AnalysisStep.PROFILE_DATA,
                      OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                      "Count of entity types attached at end 2 of this type of relationship.",
                      "Entities are linked together using relationships.  There are different types of relationships. These are the entity types attached to end 1 of this relationship type.",
                      null),

    PAIRED_ENTITY_TYPES("Apache Atlas Attached Entity Type Pairs",
                      AnalysisStep.PROFILE_DATA,
                      OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                      "Count of entity type pairs for this type of relationship.",
                      "Entities are linked together using relationships.  There are different types of relationships. These are the entity types is attached to both ends of this relationship type.",
                      null),
    ;


    public final String            name;
    public final AnalysisStep      analysisStep;
    public final String            openMetadataTypeName;
    public final String            summary;
    public final String            explanation;
    public final List<AtlasMetric> metrics;


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
    AtlasAnnotationType(String              name,
                        AnalysisStep        analysisStep,
                        String              openMetadataTypeName,
                        String              summary,
                        String              explanation,
                        List<AtlasMetric>  metrics)
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

        for (AtlasAnnotationType atlasAnnotationType : AtlasAnnotationType.values())
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

            for (AtlasMetric atlasMetric : metrics)
            {
                metricsMap.put(atlasMetric.getName(), atlasMetric.getDescription());
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
