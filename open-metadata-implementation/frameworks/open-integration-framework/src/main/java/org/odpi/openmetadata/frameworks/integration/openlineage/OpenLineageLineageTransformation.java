/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a transformation applied to source data on its way to a target in the lineage facets.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageTransformation
{
    private String  type = null;
    private String  subtype = null;
    private String  description = null;
    private Boolean masking = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageTransformation()
    {
    }


    /**
     * Return the transformation type, such as DIRECT or INDIRECT.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the transformation type, such as DIRECT or INDIRECT.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the transformation subtype, such as IDENTITY, AGGREGATION, FILTER, JOIN, GROUP_BY, WINDOW, SORT or CONDITIONAL.
     *
     * @return string
     */
    public String getSubtype()
    {
        return subtype;
    }


    /**
     * Set up the transformation subtype, such as IDENTITY, AGGREGATION, FILTER, JOIN, GROUP_BY, WINDOW, SORT or CONDITIONAL.
     *
     * @param subtype string
     */
    public void setSubtype(String subtype)
    {
        this.subtype = subtype;
    }


    /**
     * Return the string representation of the transformation.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the string representation of the transformation.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the whether the transformation masks the source data.
     *
     * @return boolean
     */
    public Boolean getMasking()
    {
        return masking;
    }


    /**
     * Set up the whether the transformation masks the source data.
     *
     * @param masking boolean
     */
    public void setMasking(Boolean masking)
    {
        this.masking = masking;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLineageTransformation{" +
                       "type='" + type + '\'' +
                       ", subtype='" + subtype + '\'' +
                       ", description='" + description + '\'' +
                       ", masking=" + masking +
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
        OpenLineageLineageTransformation that = (OpenLineageLineageTransformation) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(subtype, that.subtype) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(masking, that.masking);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, subtype, description, masking);
    }
}
