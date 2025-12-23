/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Allows collections of properties to be managed together and attached to a referenceable element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PropertyFacetProperties extends ReferenceableProperties
{
    private Map<String, String> properties = null;


    /**
     * Default constructor
     */
    public PropertyFacetProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROPERTY_FACET.typeName;
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public PropertyFacetProperties(PropertyFacetProperties template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }



    /**
     * Set up facet properties.
     *
     * @param properties map
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }


    /**
     * Return a copy of the facet properties.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PropertyFacetProperties{" +
                "properties=" + properties +
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
        PropertyFacetProperties that = (PropertyFacetProperties) objectToCompare;
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}