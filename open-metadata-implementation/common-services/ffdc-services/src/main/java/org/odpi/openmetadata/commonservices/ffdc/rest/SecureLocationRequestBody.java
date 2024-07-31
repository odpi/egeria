/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecureLocationRequestBody carries the parameters for marking a location as secure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecureLocationRequestBody
{
    private String description   = null;
    private String level = null;


    /**
     * Default constructor
     */
    public SecureLocationRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecureLocationRequestBody(SecureLocationRequestBody template)
    {
        if (template != null)
        {
            description = template.getDescription();
            level = template.getLevel();

        }
    }


    /**
     * Return the description of security at the site.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up description of security at the site.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the level of security.
     *
     * @return string
     */
    public String getLevel()
    {
        return level;
    }


    /**
     * Set up the level of security.
     *
     * @param level string
     */
    public void setLevel(String level)
    {
        this.level = level;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SecureLocationRequestBody{" +
                       "description='" + description + '\'' +
                       ", level='" + level + '\'' +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        SecureLocationRequestBody that = (SecureLocationRequestBody) objectToCompare;
        return Objects.equals(description, that.description) &&
                       Objects.equals(level, that.level);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(description, level);
    }
}
