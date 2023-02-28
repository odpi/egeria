/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseIdRequestBody provides a structure for the unique certificate identifier.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LicenseIdRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String licenseId = null;


    /**
     * Default constructor
     */
    public LicenseIdRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LicenseIdRequestBody(LicenseIdRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.licenseId = template.getLicenseId();
        }
    }


    /**
     * Return the unique identifier for the license.
     *
     * @return String identifier
     */
    public String getLicenseId()
    {
        return licenseId;
    }


    /**
     * Set up the unique identifier for the license.
     *
     * @param licenseId String identifier
     */
    public void setLicenseId(String licenseId)
    {
        this.licenseId = licenseId;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "LicenseIdRequestBody{" +
                "licenseId='" + licenseId + '\'' +
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
        LicenseIdRequestBody that = (LicenseIdRequestBody) objectToCompare;
        return Objects.equals(licenseId, that.licenseId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(licenseId);
    }
}
