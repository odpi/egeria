/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReTypeRequestBody provides a structure for passing the new type name for a metadata element or relationship that
 * is being re-typed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReTypeRequestBody extends MetadataSourceOptions
{
    private String newTypeName = null;


    /**
     * Default constructor
     */
    public ReTypeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReTypeRequestBody(ReTypeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            newTypeName = template.getNewTypeName();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReTypeRequestBody(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return the name of the new type for the instance.
     *
     * @return string type name
     */
    public String getNewTypeName()
    {
        return newTypeName;
    }


    /**
     * Set up the name of the new type for the instance.
     *
     * @param newTypeName string type name
     */
    public void setNewTypeName(String newTypeName)
    {
        this.newTypeName = newTypeName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ReTypeRequestBody{" +
                "newTypeName='" + newTypeName + '\'' +
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
        ReTypeRequestBody that = (ReTypeRequestBody) objectToCompare;
        return Objects.equals(newTypeName, that.newTypeName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), newTypeName);
    }
}
