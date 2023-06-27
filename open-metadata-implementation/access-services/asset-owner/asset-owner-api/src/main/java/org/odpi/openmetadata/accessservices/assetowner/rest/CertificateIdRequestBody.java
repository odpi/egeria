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
 * CertificateIdRequestBody provides a structure for the unique certificate identifier.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificateIdRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private String certificateId = null;


    /**
     * Default constructor
     */
    public CertificateIdRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificateIdRequestBody(CertificateIdRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.certificateId = template.getCertificateId();
        }
    }


    /**
     * Return the unique identifier for the certificate from the certificate authority.
     *
     * @return String identifier
     */
    public String getCertificateId()
    {
        return certificateId;
    }


    /**
     * Set up the unique identifier for the certificate from the certificate authority.
     *
     * @param certificateId String identifier
     */
    public void setCertificateId(String certificateId)
    {
        this.certificateId = certificateId;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "CertificateIdRequestBody{" +
                "certificateId='" + certificateId + '\'' +
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
        CertificateIdRequestBody that = (CertificateIdRequestBody) objectToCompare;
        return Objects.equals(certificateId, that.certificateId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(certificateId);
    }
}
