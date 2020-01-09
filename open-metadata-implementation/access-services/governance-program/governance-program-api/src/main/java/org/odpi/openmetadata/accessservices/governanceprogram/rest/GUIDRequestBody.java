/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GUIDRequestBody provides a structure for passing a unique identifier (guid) as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GUIDRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String guid = null;


    /**
     * Default constructor
     */
    public GUIDRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GUIDRequestBody(GUIDRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
        }
    }


    /**
     * Return the the unique employee number for this governance officer.
     *
     * @return String identifier
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique employee number for this governance officer.
     *
     * @param guid String identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GUIDRequestBody{" +
                ", guid='" + guid +
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
        if (!(objectToCompare instanceof GUIDRequestBody))
        {
            return false;
        }
        GUIDRequestBody that = (GUIDRequestBody) objectToCompare;
        return  Objects.equals(getGUID(), that.getGUID());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid);
    }
}
