/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FindProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FindByPropertiesRequestBody is the request body structure used on OMAG REST API calls that passes a name that is used to retrieve
 * an element by name.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindByPropertiesRequestBody extends EffectiveTimeQueryRequestBody
{
    private FindProperties properties        = null;


    /**
     * Default constructor
     */
    public FindByPropertiesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindByPropertiesRequestBody(FindByPropertiesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }


    /**
     * Return the name for the query request.
     *
     * @return string name
     */
    public FindProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the name for the query request.
     *
     * @param properties string
     */
    public void setProperties(FindProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindByPropertiesRequestBody{" +
                       "properties=" + properties +
                       ", effectiveTime=" + getEffectiveTime() +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        FindByPropertiesRequestBody that = (FindByPropertiesRequestBody) objectToCompare;
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
