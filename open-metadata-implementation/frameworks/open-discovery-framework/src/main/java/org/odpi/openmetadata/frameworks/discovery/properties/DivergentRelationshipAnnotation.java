/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DivergentRelationshipAnnotation identifies a relationship and its properties that are diverging in 2 assets that are linked as
 * duplicates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DivergentRelationshipAnnotation extends DivergentDuplicateAnnotation
{
    private static final long serialVersionUID = 1L;

    private String       divergentRelationshipGUID          = null;
    private List<String> divergentRelationshipPropertyNames = null;


    /**
     * Default constructor
     */
    public DivergentRelationshipAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DivergentRelationshipAnnotation(DivergentRelationshipAnnotation template)
    {
        super(template);

        if (template != null)
        {
            divergentRelationshipGUID          = template.getDivergentRelationshipGUID();
            divergentRelationshipPropertyNames = template.getDivergentRelationshipPropertyNames();
        }
    }


    /**
     * Return the unique identifier of the relationship that is diverging.
     *
     * @return string guid
     */
    public String getDivergentRelationshipGUID()
    {
        return divergentRelationshipGUID;
    }


    /**
     * Set up the unique identifier of the relationship that is diverging.
     *
     * @param divergentRelationshipGUID string guid
     */
    public void setDivergentRelationshipGUID(String divergentRelationshipGUID)
    {
        this.divergentRelationshipGUID = divergentRelationshipGUID;
    }


    /**
     * Return the properties that are diverging.
     *
     * @return list of property names
     */
    public List<String> getDivergentRelationshipPropertyNames()
    {
        if (divergentRelationshipPropertyNames == null)
        {
            return null;
        }
        else if (divergentRelationshipPropertyNames.isEmpty())
        {
            return null;
        }

        return divergentRelationshipPropertyNames;
    }


    /**
     * Set up the properties that are diverging.
     *
     * @param divergentRelationshipPropertyNames list of property names
     */
    public void setDivergentRelationshipPropertyNames(List<String> divergentRelationshipPropertyNames)
    {
        this.divergentRelationshipPropertyNames = divergentRelationshipPropertyNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DivergentRelationshipAnnotation{" +
                "divergentRelationshipGUID='" + divergentRelationshipGUID + '\'' +
                ", divergentRelationshipPropertyNames=" + divergentRelationshipPropertyNames +
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
        DivergentRelationshipAnnotation that = (DivergentRelationshipAnnotation) objectToCompare;
        return Objects.equals(divergentRelationshipGUID, that.divergentRelationshipGUID) &&
                Objects.equals(divergentRelationshipPropertyNames, that.divergentRelationshipPropertyNames);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), divergentRelationshipGUID, divergentRelationshipPropertyNames);
    }
}
