/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdatePropertiesRequestBody provides a structure for passing the properties for updating metadata elements, relationships or classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdatePropertiesRequestBody extends MetadataSourceRequestBody
{
    private boolean           replaceProperties = false;
    private ElementProperties properties        = null;


    /**
     * Default constructor
     */
    public UpdatePropertiesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdatePropertiesRequestBody(UpdatePropertiesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            replaceProperties = template.getReplaceProperties();
            properties = template.getProperties();
        }
    }


    /**
     * Return the flag to indicate whether to completely replace the existing properties with the new properties, or just update
     * the individual properties specified on the request.
     *
     * @return boolean flag
     */
    public boolean getReplaceProperties()
    {
        return replaceProperties;
    }


    /**
     * Set up the flag to indicate whether to completely replace the existing properties with the new properties, or just update
     * the individual properties specified on the request.
     *
     * @param replaceProperties boolean flag
     */
    public void setReplaceProperties(boolean replaceProperties)
    {
        this.replaceProperties = replaceProperties;
    }


    /**
     * Return the properties for the update.
     *
     * @return list of properties
     */
    public ElementProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the update.
     *
     * @param properties list of properties
     */
    public void setProperties(ElementProperties properties)
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
        return "UpdatePropertiesRequestBody{" +
                "replaceProperties=" + replaceProperties +
                ", properties=" + properties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        UpdatePropertiesRequestBody that = (UpdatePropertiesRequestBody) objectToCompare;
        return replaceProperties == that.replaceProperties &&
                       Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), replaceProperties, properties);
    }
}
