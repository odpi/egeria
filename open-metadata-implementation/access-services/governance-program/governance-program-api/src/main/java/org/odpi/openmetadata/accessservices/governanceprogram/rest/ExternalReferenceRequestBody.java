/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReferenceProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceRequestBody provides a structure for a new ExternalReference.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private String                      anchorGUID = null;
    private ExternalReferenceProperties properties = null;


    /**
     * Default constructor
     */
    public ExternalReferenceRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalReferenceRequestBody(ExternalReferenceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.anchorGUID = template.getAnchorGUID();
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the optional anchor unique identifier.
     *
     * @return String guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the optional anchor unique identifier.
     *
     * @param guid String guid
     */
    public void setAnchorGUID(String guid)
    {
        this.anchorGUID = guid;
    }


    /**
     * Return the properties for this external reference.
     *
     * @return date
     */
    public ExternalReferenceProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for this external reference.
     *
     * @param properties date
     */
    public void setProperties(ExternalReferenceProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceRequestBody{" +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ExternalReferenceRequestBody that = (ExternalReferenceRequestBody) objectToCompare;
        return Objects.equals(anchorGUID, that.anchorGUID) &&
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
        return Objects.hash(anchorGUID, properties);
    }
}
