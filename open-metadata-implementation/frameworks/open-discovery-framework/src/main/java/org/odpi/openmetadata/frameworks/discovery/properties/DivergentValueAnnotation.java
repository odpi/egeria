/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DivergentValueAnnotation lists the name of the properties that are diverging in 2 assets that are linked as
 * duplicates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DivergentValueAnnotation extends DivergentDuplicateAnnotation
{
    private static final long serialVersionUID = 1L;

    private List<String> divergentPropertyNames  = null;


    /**
     * Default constructor
     */
    public DivergentValueAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DivergentValueAnnotation(DivergentValueAnnotation template)
    {
        super(template);

        if (template != null)
        {
            divergentPropertyNames = template.getDivergentPropertyNames();
        }
    }


    /**
     * Return the properties of the Asset that are diverging from the duplicate.
     *
     * @return list of property names
     */
    public List<String> getDivergentPropertyNames()
    {
        if (divergentPropertyNames == null)
        {
            return null;
        }
        else if (divergentPropertyNames.isEmpty())
        {
            return null;
        }
        return divergentPropertyNames;
    }


    /**
     * Set up the unique identifier for the attachment to the Asset that is diverging.
     *
     * @param divergentPropertyNames list of property names
     */
    public void setDivergentPropertyNames(List<String> divergentPropertyNames)
    {
        this.divergentPropertyNames = divergentPropertyNames;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DivergentValueAnnotation{" +
                "divergentPropertyNames=" + divergentPropertyNames +
                ", duplicateAnchorGUIDs='" + getDuplicateAnchorGUID() + '\'' +
                ", annotationType='" + getAnnotationType() + '\'' +
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
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DivergentValueAnnotation that = (DivergentValueAnnotation) objectToCompare;
        return Objects.equals(divergentPropertyNames, that.divergentPropertyNames);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), divergentPropertyNames);
    }
}
