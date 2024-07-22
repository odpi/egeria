/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.controls;

/**
 * The AnnotationType interface is implemented by an enum that describes the annotation types produced
 * by a survey action service.  This interface is a template for survey writers to use to take advantage of
 * common functions in the SurveyActionService base class.
 */
public interface AnnotationType
{
    /**
     * Return the name of the annotation type.
     *
     * @return string name
     */
    String getName();


    /**
     * Return the analysis step that produces this type of annotation.
     *
     * @return analysis step name
     */
    String getAnalysisStep();


    /**
     * Return the name of the open metadata type used for this type of annotation.
     *
     * @return type name
     */
    String getOpenMetadataTypeName();


    /**
     * Return the short description of the annotation type.
     *
     * @return text
     */
    String getSummary();


    /**
     * Return the description of the processing that produces the annotation type.
     *
     * @return text
     */
    String getExplanation();


    /**
     * Return the expression used in the annotation type processing.
     *
     * @return string
     */
     String getExpression();
}
