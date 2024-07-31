/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OwnerRequestBody provides a structure for passing information about an asset owner as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OwnerRequestBody
{
    private String    ownerId   = null;
    private String    ownerTypeName = null;
    private String    ownerPropertyName = null;

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
        if (template != null)
        {
            this.ownerId = template.getOwnerId();
            this.ownerTypeName = template.getOwnerTypeName();
            this.ownerPropertyName = template.getOwnerPropertyName();
        }
    }


    /**
     * Return the owner identifier.
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
     * Return the type of element used to represent the owner.
     *
     * @return string type name
     */
    public String getOwnerTypeName()
    {
        return ownerTypeName;
    }


    /**
     * Set up the type of element used to represent the owner.
     *
     * @param ownerTypeName string type name
     */
    public void setOwnerTypeName(String ownerTypeName)
    {
        this.ownerTypeName = ownerTypeName;
    }


    /**
     * Return the property name used to identify the owner. For example guid or qualifiedName.
     *
     * @return string property name
     */
    public String getOwnerPropertyName()
    {
        return ownerPropertyName;
    }


    /**
     * Set up the property name used to identify the owner. For example guid or qualifiedName.
     *
     * @param ownerPropertyName string property name
     */
    public void setOwnerPropertyName(String ownerPropertyName)
    {
        this.ownerPropertyName = ownerPropertyName;
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
                       "ownerId='" + ownerId + '\'' +
                       ", ownerTypeName='" + ownerTypeName + '\'' +
                       ", ownerPropertyName='" + ownerPropertyName + '\'' +
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
        return Objects.equals(ownerId, that.ownerId) &&
               Objects.equals(ownerTypeName, that.ownerTypeName) &&
               Objects.equals(ownerPropertyName, that.ownerPropertyName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getOwnerId(), getOwnerTypeName(), getOwnerPropertyName());
    }
}
