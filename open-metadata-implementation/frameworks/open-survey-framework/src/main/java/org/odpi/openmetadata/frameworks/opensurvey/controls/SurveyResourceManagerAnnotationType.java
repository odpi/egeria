/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.SurveyMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The SurveyResourceManagerAnnotationType enum describes the annotation types used by the PostgreSQL server survey action service.
 */
public enum SurveyResourceManagerAnnotationType implements AnnotationType
{
    RESOURCE_LIST("Capture List of Resources",
                OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                "Extract the list of visible elements in the surveyed resource manager.",
                "The name used for the resource is its fully qualified name with respect to the resource manager.",
                getProfilePropertiesPropertyName(),
                null),

    /**
     * Log file of topics known to the Apache Kafka server.
     */
    RESOURCE_INVENTORY("Resource Manager Inventory",
                       OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName,
                       "Log file of resources known to the resource manager.",
                       "The list of resources known to the resource manager is pushed to an external log file for analysis.",
                       null,
                       null),

    FUNCTION_LIST("Capture List of Executable Functions",
                  OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                  "Extract the list of visible functions in the surveyed resource (manager).",
                  "Functions are listed by their full name, with respect to the resource manager.  If functions are missing, check the security permissions of the survey service's userId.",
                  getProfilePropertiesPropertyName(),
                  null),

    VOLUME_LIST("Capture List of File Volumes",
                OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                "Extract the list of visible file-based volumes in the surveyed resource (manager).",
                "Volumes are listed by their full name, with respect to the resource manager.  If volumes are missing, check the security permissions of the survey service's userId.",
                getProfilePropertiesPropertyName(),
                null),

    MODEL_LIST("Capture List of Analytical Models",
               OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
               "Extract the list of visible analytical models in the surveyed resource (manager).",
               "Models are listed by their full name, with respect to the resource manager.  If models are missing, check the security permissions of the survey service's userId.",
               null,
               null),

    API_LIST("Capture List of Remote APIs",
             OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
             "Extract the list of visible callable APIs in the surveyed resource (manager).",
             "APIs are listed by their full name, with respect to the resource manager.  If APIs are missing, check the security permissions of the survey service's userId.",
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
    SurveyResourceManagerAnnotationType(String                name,
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
        return "SurveyResourceManagerAnnotationType{ name='" + name + "}";
    }
}
