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
 * ReIdentifyRequestBody provides a structure for passing the new unique identifier for a metadata element or
 * relationship that is being re-identified.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReIdentifyRequestBody extends MetadataSourceOptions
{
    private String newGUID = null;


    /**
     * Default constructor
     */
    public ReIdentifyRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReIdentifyRequestBody(ReIdentifyRequestBody template)
    {
        super(template);

        if (template != null)
        {
            newGUID = template.getNewGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReIdentifyRequestBody(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return the new unique identifier for the instance.
     *
     * @return string guid
     */
    public String getNewGUID()
    {
        return newGUID;
    }


    /**
     * Set up the new unique identifier for the instance.
     *
     * @param newGUID string guid
     */
    public void setNewGUID(String newGUID)
    {
        this.newGUID = newGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ReIdentifyRequestBody{" +
                "newGUID='" + newGUID + '\'' +
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
        ReIdentifyRequestBody that = (ReIdentifyRequestBody) objectToCompare;
        return Objects.equals(newGUID, that.newGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), newGUID);
    }
}
