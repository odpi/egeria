/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serializable;
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
public class CalculatedValueClassificationRequestBody implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private String                        formula                       = null;


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
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            formula = template.getFormula();
        }
    }


    /**
     * Return the properties used to correlate the external metadata element with the open metadata element.
     *
     * @return properties object
     */
    public MetadataCorrelationProperties getMetadataCorrelationProperties()
    {
        return metadataCorrelationProperties;
    }


    /**
     * Set up the properties used to correlate the external metadata element with the open metadata element.
     *
     * @param metadataCorrelationProperties properties object
     */
    public void setMetadataCorrelationProperties(MetadataCorrelationProperties metadataCorrelationProperties)
    {
        this.metadataCorrelationProperties = metadataCorrelationProperties;
    }


    /**
     * Return the scope that the terms in the glossary covers.
     *
     * @return string description
     */
    public String getFormula()
    {
        return formula;
    }


    /**
     * Set up the scope that the terms in the glossary covers.
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
                       "metadataCorrelationProperties=" + metadataCorrelationProperties +
                       ", scope='" + formula + '\'' +
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
        CalculatedValueClassificationRequestBody that = (CalculatedValueClassificationRequestBody) objectToCompare;
        return Objects.equals(getMetadataCorrelationProperties(), that.getMetadataCorrelationProperties()) &&
                       Objects.equals(getFormula(), that.getFormula());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMetadataCorrelationProperties(), getFormula());
    }
}
