/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataClassMatchProperties is used to link an annotation from a data class and identify how closely it matches.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassMatchProperties extends RelationshipBeanProperties
{
    private String method = null;
    private int    threshold = 0;


    /**
     * Default constructor used by subclasses
     */
    public DataClassMatchProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_CLASS_MATCH_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public DataClassMatchProperties(DataClassMatchProperties template)
    {
        super(template);

        if (template != null)
        {
            this.method = template.getMethod();
            this.threshold = template.getThreshold();
        }
    }


    /**
     * Return the method used to create the match.
     *
     * @return string
     */
    public String getMethod()
    {
        return method;
    }


    /**
     * Set up the method used to create the match.
     *
     * @param method string
     */
    public void setMethod(String method)
    {
        this.method = method;
    }


    /**
     * Return the level of match - 0 means no match, 100 means perfect match.
     *
     * @return int
     */
    public int getThreshold()
    {
        return threshold;
    }


    /**
     * Set up the level of match - 0 means no match, 100 means perfect match.
     *
     * @param threshold int
     */
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataClassMatchProperties{" +
                "method='" + method + '\'' +
                ", threshold=" + threshold +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataClassMatchProperties that = (DataClassMatchProperties) objectToCompare;
        return threshold == that.threshold && Objects.equals(method, that.method);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), method, threshold);
    }
}
