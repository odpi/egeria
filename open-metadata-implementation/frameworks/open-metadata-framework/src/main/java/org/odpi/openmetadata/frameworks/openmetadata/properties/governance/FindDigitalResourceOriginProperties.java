/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindDigitalResourceOriginProperties provides a structure for passing information about a digital resource origin
 * search request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindDigitalResourceOriginProperties extends FindProperties
{
    private DigitalResourceOriginProperties properties = null;


    /**
     * Default constructor
     */
    public FindDigitalResourceOriginProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindDigitalResourceOriginProperties(FindDigitalResourceOriginProperties template)
    {
        super(template);
        if (template != null)
        {
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the origin properties to search for.
     *
     * @return properties
     */
    public DigitalResourceOriginProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the origin properties to search for.
     *
     * @param properties properties
     */
    public void setProperties(DigitalResourceOriginProperties properties)
    {
        this.properties = properties;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FindDigitalResourceOriginProperties{" +
                "properties=" + properties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindDigitalResourceOriginProperties that = (FindDigitalResourceOriginProperties) objectToCompare;
        return Objects.equals(properties, that.properties);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
