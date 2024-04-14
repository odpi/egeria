/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.collectionmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.CollectionProperties;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalProductProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewDigitalProductRequestBody describes the properties to create a new collection with a digital product
 * classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewDigitalProductRequestBody extends NewCollectionRequestBody
{
    private DigitalProductProperties digitalProductProperties = null;


    /**
     * Default constructor
     */
    public NewDigitalProductRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewDigitalProductRequestBody(NewDigitalProductRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.digitalProductProperties = template.getDigitalProductProperties();
        }
    }


    /**
     * Return the properties for the digital product classification.
     *
     * @return properties
     */
    public DigitalProductProperties getDigitalProductProperties()
    {
        return digitalProductProperties;
    }


    /**
     * Set up the properties for the digital product classification.
     *
     * @param digitalProductProperties properties
     */
    public void setDigitalProductProperties(DigitalProductProperties digitalProductProperties)
    {
        this.digitalProductProperties = digitalProductProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewDigitalProductRequestBody{" +
                "digitalProductProperties=" + digitalProductProperties +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewDigitalProductRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(digitalProductProperties, that.digitalProductProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), digitalProductProperties);
    }
}
