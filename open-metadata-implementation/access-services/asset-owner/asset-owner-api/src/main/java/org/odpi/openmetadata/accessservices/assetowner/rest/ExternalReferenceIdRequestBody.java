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
 * ExternalReferenceIdRequestBody provides a structure for the unique external reference identifier.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceIdRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private String referenceId = null;


    /**
     * Default constructor
     */
    public ExternalReferenceIdRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalReferenceIdRequestBody(ExternalReferenceIdRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.referenceId = template.getReferenceId();
        }
    }


    /**
     * Return the unique identifier for the certificate from the certificate authority.
     *
     * @return String identifier
     */
    public String getReferenceId()
    {
        return referenceId;
    }


    /**
     * Set up the unique identifier for the certificate from the certificate authority.
     *
     * @param referenceId String identifier
     */
    public void setReferenceId(String referenceId)
    {
        this.referenceId = referenceId;
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
                "referenceId='" + referenceId + '\'' +
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
        ExternalReferenceIdRequestBody that = (ExternalReferenceIdRequestBody) objectToCompare;
        return Objects.equals(referenceId, that.referenceId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(referenceId);
    }
}
