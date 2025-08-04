/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.locations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecureLocationProperties carries the parameters for marking a location as secure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecureLocationProperties extends ClassificationBeanProperties
{
    private String description   = null;
    private String level = null;


    /**
     * Default constructor
     */
    public SecureLocationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SECURE_LOCATION_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecureLocationProperties(SecureLocationProperties template)
    {
        super(template);

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
        return "SecureLocationProperties{" +
                "description='" + description + '\'' +
                ", level='" + level + '\'' +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        SecureLocationProperties that = (SecureLocationProperties) objectToCompare;
        return Objects.equals(description, that.description) && Objects.equals(level, that.level);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), description, level);
    }
}
