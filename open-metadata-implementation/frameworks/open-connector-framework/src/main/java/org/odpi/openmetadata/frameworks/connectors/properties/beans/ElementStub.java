/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementStub is used to identify an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementStub extends ElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String uniqueName = null;

    /**
     * Default constructor
     */
    public ElementStub()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ElementStub(ElementStub template)
    {
        super(template);

        if (template != null)
        {
            this.uniqueName = template.getUniqueName();
        }
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ElementStub(ElementHeader template)
    {
        super(template);
    }


    /**
     * Return the unique name - if known
     *
     * @return string name
     */
    public String getUniqueName()
    {
        return uniqueName;
    }


    /**
     * Set up unique name - if known
     *
     * @param uniqueName string name
     */
    public void setUniqueName(String uniqueName)
    {
        this.uniqueName = uniqueName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ElementStub{" +
                       "uniqueName='" + uniqueName + '\'' +
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", classifications=" + getClassifications() +
                       '}';
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ElementStub that = (ElementStub) objectToCompare;
        return Objects.equals(uniqueName, that.uniqueName);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), uniqueName);
    }
}
