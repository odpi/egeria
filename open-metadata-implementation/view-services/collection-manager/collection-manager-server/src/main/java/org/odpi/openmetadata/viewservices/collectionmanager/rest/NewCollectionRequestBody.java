/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.collectionmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.CollectionProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewCollectionRequestBody describes the properties to create a new collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewCollectionRequestBody extends NewElementRequestBody
{
    private CollectionProperties collectionProperties = null;


    /**
     * Default constructor
     */
    public NewCollectionRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewCollectionRequestBody(NewCollectionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.collectionProperties = template.getCollectionProperties();
        }
    }


    /**
     * Return the properties for the collection.
     *
     * @return properties
     */
    public CollectionProperties getCollectionProperties()
    {
        return collectionProperties;
    }


    /**
     * Set up the properties for the collection.
     *
     * @param digitalProductProperties properties
     */
    public void setCollectionProperties(CollectionProperties digitalProductProperties)
    {
        this.collectionProperties = digitalProductProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewCollectionRequestBody{" +
                "collectionProperties=" + collectionProperties +
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
        if (! (objectToCompare instanceof NewCollectionRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(collectionProperties, that.collectionProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), collectionProperties);
    }
}
