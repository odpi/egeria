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
 * DivergentClassificationAnnotation identifies a classification and its properties that are diverging in 2 assets that are linked as
 * duplicates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DivergentClassificationAnnotation extends DivergentDuplicateAnnotation
{
    private static final long serialVersionUID = 1L;

    protected String       divergentClassificationName  = null;
    protected List<String> divergentClassificationPropertyNames  = null;


    /**
     * Default constructor
     */
    public DivergentClassificationAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DivergentClassificationAnnotation(DivergentClassificationAnnotation template)
    {
        super(template);

        if (template != null)
        {
            divergentClassificationName = template.getDivergentClassificationName();
            divergentClassificationPropertyNames = template.getDivergentClassificationPropertyNames();
        }
    }


    /**
     * Return the name of the classification that is diverging.
     *
     * @return name of classification
     */
    public String getDivergentClassificationName()
    {
        return divergentClassificationName;
    }


    /**
     * Set up the name of the classification that is diverging.
     *
     * @param divergentClassificationName name of classification
     */
    public void setDivergentClassificationName(String divergentClassificationName)
    {
        this.divergentClassificationName = divergentClassificationName;
    }


    /**
     * Return the properties that are diverging.
     *
     * @return list of property names
     */
    public List<String> getDivergentClassificationPropertyNames()
    {
        if (divergentClassificationPropertyNames == null)
        {
            return null;
        }
        else if (divergentClassificationPropertyNames.isEmpty())
        {
            return null;
        }

        return divergentClassificationPropertyNames;
    }


    /**
     * Set up the properties that are diverging.
     *
     * @param divergentClassificationPropertyNames list of property names
     */
    public void setDivergentClassificationPropertyNames(List<String> divergentClassificationPropertyNames)
    {
        this.divergentClassificationPropertyNames = divergentClassificationPropertyNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DivergentClassificationAnnotation{" +
                "divergentClassificationName='" + divergentClassificationName + '\'' +
                ", divergentClassificationPropertyNames=" + divergentClassificationPropertyNames +
                ", duplicateAnchorGUID='" + duplicateAnchorGUID + '\'' +
                ", annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", numAttachedAnnotations=" + numAttachedAnnotations +
                ", annotationStatus=" + annotationStatus +
                ", reviewDate=" + reviewDate +
                ", steward='" + steward + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
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
        DivergentClassificationAnnotation that = (DivergentClassificationAnnotation) objectToCompare;
        return Objects.equals(divergentClassificationName, that.divergentClassificationName) &&
                Objects.equals(divergentClassificationPropertyNames, that.divergentClassificationPropertyNames);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), divergentClassificationName, divergentClassificationPropertyNames);
    }
}
