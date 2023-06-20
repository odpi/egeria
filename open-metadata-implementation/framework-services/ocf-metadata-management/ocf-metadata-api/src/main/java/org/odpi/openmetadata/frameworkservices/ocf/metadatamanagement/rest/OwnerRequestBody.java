/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OwnerRequestBody provides a structure for passing information about an asset owner as
 * a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OwnerRequestBody extends OCFOMASAPIRequestBody
{
    private OwnerType ownerType = null;
    private String    ownerId   = null;


    /**
     * Default constructor
     */
    public OwnerRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OwnerRequestBody(OwnerRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.ownerType = template.getOwnerType();
            this.ownerId = template.getOwnerId();
        }
    }
    

    /**
     * Return the type of owner.
     * 
     * @return string
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the type of owner.
     * 
     * @param ownerType string
     */
    public void setOwnerType(OwnerType ownerType)
    {
        this.ownerType = ownerType;
    }


    /**
     * Return the owner Id.
     * 
     * @return string identifier
     */
    public String getOwnerId()
    {
        return ownerId;
    }


    /**
     * Set up the owner id.
     * 
     * @param ownerId identifier.
     */
    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OwnerRequestBody{" +
                "ownerType=" + ownerType +
                ", ownerId='" + ownerId + '\'' +
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
        OwnerRequestBody that = (OwnerRequestBody) objectToCompare;
        return getOwnerType() == that.getOwnerType() &&
                Objects.equals(getOwnerId(), that.getOwnerId());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getOwnerType(), getOwnerId());
    }
}
