/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabasePrimaryKeyProperties describes the properties of a primary key.  It is typically associated
 * with one of the database columns in a database table to indicate that the values stored uniquely
 * identify the row.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabasePrimaryKeyProperties extends ClassificationProperties
{
    private String     name             = null;
    private KeyPattern keyPattern       = null;


    /**
     * Default constructor
     */
    public DatabasePrimaryKeyProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public DatabasePrimaryKeyProperties(DatabasePrimaryKeyProperties template)
    {
        super(template);

        if (template != null)
        {
            name             = template.getName();
            keyPattern       = template.getKeyPattern();
        }
    }


    /**
     * Set up name of the primary key.
     *
     * @param name String
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the name for the primary key.
     *
     * @return String description
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the key pattern for the primary key.
     *
     * @param keyPattern String name
     */
    public void setKeyPattern(KeyPattern keyPattern)
    {
        this.keyPattern = keyPattern;
    }


    /**
     * Returns the key pattern for the primary key.
     *
     * @return String name
     */
    public KeyPattern getKeyPattern()
    {
        return keyPattern;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabasePrimaryKeyProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", name='" + name + '\'' +
                       ", keyPattern=" + keyPattern +
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
        if (! (objectToCompare instanceof DatabasePrimaryKeyProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DatabasePrimaryKeyProperties that = (DatabasePrimaryKeyProperties) objectToCompare;
        return Objects.equals(name, that.name) && keyPattern == that.keyPattern;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, keyPattern);
    }
}
