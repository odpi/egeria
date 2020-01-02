/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDomainRequestBody provides a request body with the identifiers used to verify that
 * the right object is being deleted.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDomainRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private GovernanceDomain    governanceDomain    = null;


    /**
     * Default constructor
     */
    public GovernanceDomainRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDomainRequestBody(GovernanceDomainRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.governanceDomain = template.getGovernanceDomain();
        }
    }


    /**
     * Return the governance domain over which this governance officer presides.
     *
     * @return governance domain enum value
     */
    public GovernanceDomain getGovernanceDomain()
    {
        return governanceDomain;
    }


    /**
     * Set up the governance domain over which this governance officer presides.
     *
     * @param governanceDomain enum
     */
    public void setGovernanceDomain(GovernanceDomain governanceDomain)
    {
        this.governanceDomain = governanceDomain;
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceDomainRequestBody{" +
                "governanceDomain=" + governanceDomain +
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
        GovernanceDomainRequestBody that = (GovernanceDomainRequestBody) objectToCompare;
        return  governanceDomain == that.governanceDomain;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceDomain);
    }
}
