/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.survey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;

import java.util.ArrayList;
import java.util.List;


/**
 * The WSO2MIAnnotationType enum describes the annotation types used by the WSO2 Micro Integrator survey
 * action service.  v1 scope profiles the deployed API list only; annotation types for the other artifact
 * kinds (proxy services, sequences, endpoints, ...) are follow-up increments, as for the connector itself.
 */
public enum WSO2MIAnnotationType implements AnnotationType
{
    /**
     * List of REST APIs deployed on the WSO2 Micro Integrator.
     */
    API_LIST("WSO2 Micro Integrator API List",
             AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
             OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
             "List of REST APIs deployed on the WSO2 Micro Integrator.",
             "APIs are the deployed artifacts most commonly catalogued from a Micro Integrator instance."),

    /**
     * Log file of REST APIs deployed on the WSO2 Micro Integrator.
     */
    API_INVENTORY("WSO2 Micro Integrator API Inventory",
                  AnalysisStep.PRODUCE_INVENTORY,
                  OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName,
                  "Log file of REST APIs deployed on the WSO2 Micro Integrator.",
                  "APIs are the deployed artifacts most commonly catalogued from a Micro Integrator instance."),
    ;


    public final String       name;
    public final AnalysisStep analysisStep;
    public final String       openMetadataTypeName;
    public final String       summary;
    public final String       explanation;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param analysisStep associated analysis step
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     */
    WSO2MIAnnotationType(String       name,
                         AnalysisStep analysisStep,
                         String       openMetadataTypeName,
                         String       summary,
                         String       explanation)
    {
        this.name                 = name;
        this.analysisStep         = analysisStep;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary              = summary;
        this.explanation          = explanation;
    }


    /**
     * Return the defined annotation types as a list of annotation type types.
     *
     * @return list
     */
    public static List<AnnotationTypeType> getAnnotationTypeTypes()
    {
        List<AnnotationTypeType> annotationTypeTypes = new ArrayList<>();

        for (WSO2MIAnnotationType wso2MIAnnotationType : WSO2MIAnnotationType.values())
        {
            annotationTypeTypes.add(wso2MIAnnotationType.getAnnotationTypeType());
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
     * Return the name of the open metadata type used for this annotation type.
     *
     * @return type name
     */
    @Override
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Return the short summary of the annotation type.
     *
     * @return text
     */
    @Override
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the explanation of the annotation type.
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
        annotationTypeType.setAnalysisStepName(analysisStep.getName());
        annotationTypeType.setDescription(summary);
        annotationTypeType.setExplanation(explanation);

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
