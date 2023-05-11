/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CalculatedValueClassificationRequestBody is used to identify the schema elements that are calculated (derived)
 * rather than stored.  Examples of this are relational database views.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculatedValueClassificationRequestBody extends UpdateRequestBody
{
    private String formula = null;


    /**
     * Default constructor
     */
    public CalculatedValueClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public CalculatedValueClassificationRequestBody(CalculatedValueClassificationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
        }
    }


    /**
     * Return the formula used to calculate the value.
     *
     * @return string formula with placeholders
     */
    public String getFormula()
    {
        return formula;
    }


    /**
     * Set up the formula used to calculate the value.
     *
     * @param formula string description
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CalculatedValueClassificationRequestBody{" +
                       "formula='" + formula + '\'' +
                       ", metadataCorrelationProperties=" + getMetadataCorrelationProperties() +
                       ", effectiveTime=" + getEffectiveTime() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        CalculatedValueClassificationRequestBody that = (CalculatedValueClassificationRequestBody) objectToCompare;
        return Objects.equals(formula, that.formula);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula);
    }
}
