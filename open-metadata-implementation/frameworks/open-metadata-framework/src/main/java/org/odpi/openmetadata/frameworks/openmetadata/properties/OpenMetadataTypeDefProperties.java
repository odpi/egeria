/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The OpenMetadataTypeDefProperties class provides support for arbitrary properties that belong to a OpenMetadataTypeDef object.
 * It is used for searching the TypeDefs.
 * It wraps a java.util.Map map object built around HashMap.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefProperties extends OpenMetadataTypeDefElementHeader
{
    private Map<String, Object> typeDefProperties = null;


    /**
     * Default constructor.
     */
    public OpenMetadataTypeDefProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public OpenMetadataTypeDefProperties(OpenMetadataTypeDefProperties templateProperties)
    {
        super(templateProperties);
        if (templateProperties != null)
        {
            this.setTypeDefProperties(templateProperties.getTypeDefProperties());
        }
    }


    /**
     * Return the list of property names.
     *
     * @return List of String property names
     */
    public Map<String, Object> getTypeDefProperties()
    {
        return typeDefProperties;
    }


    /**
     * Set up the list of property names.
     *
     * @param typeDefProperties list of property names
     */
    public void setTypeDefProperties(Map<String, Object> typeDefProperties)
    {
        this.typeDefProperties = typeDefProperties;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTypeDefProperties{" +
                "typeDefProperties=" + typeDefProperties +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OpenMetadataTypeDefProperties that = (OpenMetadataTypeDefProperties) objectToCompare;
        return Objects.equals(typeDefProperties, that.typeDefProperties);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeDefProperties);
    }
}
