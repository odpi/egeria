/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetowner.properties.ReferenceableProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableRequestBody provides a structure for passing a referenceables' properties as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ValidValuesRequestBody.class, name = "ValidValuesRequestBody")
        })
public class ReferenceableRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private String                  anchorGUID = null;
    private ReferenceableProperties properties = null;



    /**
     * Default constructor
     */
    public ReferenceableRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceableRequestBody(ReferenceableRequestBody template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            properties = template.getProperties();
        }
    }



    /**
     * Return the properties for the relationship.
     *
     * @return properties object
     */
    public ReferenceableProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param properties properties object
     */
    public void setProperties(ReferenceableProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return an optional anchor GUID to attach the new element to.
     *
     * @return guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up an optional anchor GUID to attach the new element to.
     *
     * @param anchorGUID guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ReferenceableRequestBody{" +
                       "anchorGUID='" + anchorGUID + '\'' +
                       ", properties=" + properties +
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
        if (! (objectToCompare instanceof ReferenceableRequestBody))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ReferenceableRequestBody that = (ReferenceableRequestBody) objectToCompare;
        return Objects.equals(anchorGUID, that.anchorGUID) && Objects.equals(properties, that.properties);
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, properties);
    }
}
