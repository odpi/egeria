/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldAnnotation is a common base class for annotations that are attached to a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataProfileAnnotation.class, name = "DataProfileAnnotation")
        })
public class DataFieldAnnotation extends Annotation
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public DataFieldAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataFieldAnnotation(DataFieldAnnotation template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldAnnotation{" +
                       "annotationType='" + getAnnotationType() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", confidenceLevel=" + getConfidenceLevel() +
                       ", expression='" + getExpression() + '\'' +
                       ", explanation='" + getExplanation() + '\'' +
                       ", analysisStep='" + getAnalysisStep() + '\'' +
                       ", jsonProperties='" + getJsonProperties() + '\'' +
                       ", annotationStatus=" + getAnnotationStatus() +
                       ", numAttachedAnnotations=" + getNumAttachedAnnotations() +
                       ", reviewDate=" + getReviewDate() +
                       ", steward='" + getSteward() + '\'' +
                       ", reviewComment='" + getReviewComment() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", URL='" + getURL() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
