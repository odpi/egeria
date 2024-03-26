/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.controls;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

/**
 * The AnnotationType enum describes the annotation types used by a survey action service.  This class is a template for
 * survey writers to copy and fill out.
 */
public enum AnnotationType
{
    EXAMPLE_ANNOTATION_TYPE("Example Annotation Type",
                            AnalysisStep.PROFILE_DATA,
                            OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                            "This is an example annotation type.",
                            "This is the explanation of the annotation type's processing.",
                            "This is the expression used in the annotation type's processing."),

    ;


    public final String       name;
    public final AnalysisStep analysisStep;
    public final String       openMetadataTypeName;
    public final String       summary;
    public final String       explanation;
    public final String       expression;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the annotation type
     * @param analysisStep associated analysis step
     * @param openMetadataTypeName the open metadata type used for this annotation type
     * @param summary short explanation of the annotation type
     * @param explanation explanation of the annotation type
     */
    AnnotationType(String       name,
                   AnalysisStep analysisStep,
                   String       openMetadataTypeName,
                   String       summary,
                   String       explanation,
                   String       expression)
    {
        this.name                 = name;
        this.analysisStep         = analysisStep;
        this.openMetadataTypeName = openMetadataTypeName;
        this.summary     = summary;
        this.explanation = explanation;
        this.expression  = expression;
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
        if (analysisStep != null)
        {
            return analysisStep.getName();
        }

        return null;
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
     * Return the expression used in the annotation type processing.
     *
     * @return string
     */
    public String getExpression()
    {
        return expression;
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
        annotationTypeType.setExpression(expression);

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
