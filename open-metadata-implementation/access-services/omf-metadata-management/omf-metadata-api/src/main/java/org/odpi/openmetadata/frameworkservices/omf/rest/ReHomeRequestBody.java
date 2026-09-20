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
 * ReHomeRequestBody provides a structure for passing the details of the new home metadata collection for a metadata
 * element or relationship that is being re-homed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReHomeRequestBody extends MetadataSourceOptions
{
    private String newHomeMetadataCollectionId   = null;
    private String newHomeMetadataCollectionName = null;


    /**
     * Default constructor
     */
    public ReHomeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReHomeRequestBody(ReHomeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            newHomeMetadataCollectionId   = template.getNewHomeMetadataCollectionId();
            newHomeMetadataCollectionName = template.getNewHomeMetadataCollectionName();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReHomeRequestBody(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the new home metadata collection/repository.
     *
     * @return string identifier
     */
    public String getNewHomeMetadataCollectionId()
    {
        return newHomeMetadataCollectionId;
    }


    /**
     * Set up the unique identifier of the new home metadata collection/repository.
     *
     * @param newHomeMetadataCollectionId string identifier
     */
    public void setNewHomeMetadataCollectionId(String newHomeMetadataCollectionId)
    {
        this.newHomeMetadataCollectionId = newHomeMetadataCollectionId;
    }


    /**
     * Return the display name of the new home metadata collection/repository.
     *
     * @return string name
     */
    public String getNewHomeMetadataCollectionName()
    {
        return newHomeMetadataCollectionName;
    }


    /**
     * Set up the display name of the new home metadata collection/repository.
     *
     * @param newHomeMetadataCollectionName string name
     */
    public void setNewHomeMetadataCollectionName(String newHomeMetadataCollectionName)
    {
        this.newHomeMetadataCollectionName = newHomeMetadataCollectionName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ReHomeRequestBody{" +
                "newHomeMetadataCollectionId='" + newHomeMetadataCollectionId + '\'' +
                ", newHomeMetadataCollectionName='" + newHomeMetadataCollectionName + '\'' +
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
        ReHomeRequestBody that = (ReHomeRequestBody) objectToCompare;
        return Objects.equals(newHomeMetadataCollectionId, that.newHomeMetadataCollectionId) &&
                       Objects.equals(newHomeMetadataCollectionName, that.newHomeMetadataCollectionName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), newHomeMetadataCollectionId, newHomeMetadataCollectionName);
    }
}
