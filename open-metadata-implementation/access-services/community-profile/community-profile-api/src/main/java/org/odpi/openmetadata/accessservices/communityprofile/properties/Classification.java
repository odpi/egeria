/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Classification bean defines a single classification for an asset.  This can be used for REST calls and other
 * JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classification extends CommunityProfileElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String              name       = null;
    private Map<String, Object> properties = null;

    /**
     * Default constructor
     */
    public Classification()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public Classification(Classification template)
    {
        super(template);

        if (template != null)
        {
            name = template.getName();
            properties = template.getProperties();
        }
    }


    /**
     * Set up the name of the classification
     *
     * @param name  name of classification
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the name of the classification
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @param properties  properties for the classification
     */
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }


    /**
     * Return a collection of the additional stored properties for the classification.
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
        return "Classification{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
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
        if (!(objectToCompare instanceof Classification))
        {
            return false;
        }
        Classification that = (Classification) objectToCompare;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getProperties(), that.getProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }
}
