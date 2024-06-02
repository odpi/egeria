/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of an open metadata element (entity instance) retrieved from the open metadata repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataElement extends ElementHeader
{
    private Map<String, Object> properties = null;

    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public MetadataElement(MetadataElement template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public MetadataElement(ElementHeader template)
    {
        super(template);
    }




    /**
     * Set up the  properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @param properties  properties for the classification
     */
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }


    /**
     * Return a collection of the properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return properties map
     */
    public Map<String, Object> getProperties()
    {
        if (properties == null)
        {
            return null;
        }
        else if (properties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(properties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataElement{" +
                "properties=" + properties +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", origin=" + getOrigin() +
                ", versions=" + getVersions() +
                ", headerVersion=" + getHeaderVersion() +
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
        MetadataElement that = (MetadataElement) objectToCompare;
        return Objects.equals(properties, that.properties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
