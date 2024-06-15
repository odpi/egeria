/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ReferenceableProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ReferenceableRequestBody describes the request body used when working with referenceables.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceableRequestBody extends ExternalSourceRequestBody
{
    private String                  parentGUID = null;
    private ReferenceableProperties properties = null;


    /**
     * Default constructor
     */
    public ReferenceableRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReferenceableRequestBody(ReferenceableRequestBody template)
    {
        super(template);

        if (template != null)
        {
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceableRequestBody{" +
                       "externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       ", anchorGUID='" + parentGUID + '\'' +
                       ", properties=" + properties +
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
        if (! (objectToCompare instanceof ReferenceableRequestBody))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ReferenceableRequestBody that = (ReferenceableRequestBody) objectToCompare;
        return Objects.equals(parentGUID, that.parentGUID) && Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parentGUID, properties);
    }
}
