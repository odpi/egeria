/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableRequestBody provides a structure for passing a referenceables' properties as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableRequestBody extends ExternalSourceRequestBody
{
    private String                  anchorGUID = null;
    private String                  parentGUID = null;
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
        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            parentGUID = template.getParentGUID();
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
     * Return an optional anchor GUID to attach the new element to.
     *
     * @return guid
     */
    public String getParentGUID()
    {
        return parentGUID;
    }


    /**
     * Set up an optional anchor GUID to attach the new element to.
     *
     * @param parentGUID guid
     */
    public void setParentGUID(String parentGUID)
    {
        this.parentGUID = parentGUID;
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
                ", parentGUID='" + parentGUID + '\'' +
                ", properties=" + properties +
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
        if (! (objectToCompare instanceof ReferenceableRequestBody that))
        {
            return false;
        }
        return Objects.equals(anchorGUID, that.anchorGUID) &&
                Objects.equals(parentGUID, that.parentGUID) &&
                Objects.equals(properties, that.properties);
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(anchorGUID, parentGUID, properties);
    }
}
