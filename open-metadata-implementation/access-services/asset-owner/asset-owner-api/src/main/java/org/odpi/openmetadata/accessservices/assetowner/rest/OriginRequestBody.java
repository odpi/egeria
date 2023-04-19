/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OriginRequestBody provides a structure for passing information about an asset's origin as
 * a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OriginRequestBody extends AssetOwnerOMASAPIRequestBody
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String              organizationGUID       = null;
    private String              businessCapabilityGUID = null;
    private Map<String, String> otherOriginValues      = null;


    /**
     * Default constructor
     */
    public OriginRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OriginRequestBody(OriginRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.organizationGUID = template.getOrganizationGUID();
            this.businessCapabilityGUID = template.getBusinessCapabilityGUID();
            this.otherOriginValues = template.getOtherOriginValues();
        }
    }


    /**
     * Return the unique identifier (GUID) of the organization where this asset originated from.
     *
     * @return unique identifier (GUID)
     */
    public String getOrganizationGUID()
    {
        return organizationGUID;
    }


    /**
     * Set up the unique identifier (GUID) of the organization where this asset originated from.
     *
     * @param organizationGUID unique identifier (GUID)
     */
    public void setOrganizationGUID(String organizationGUID)
    {
        this.organizationGUID = organizationGUID;
    }


    /**
     * Return the unique identifier (GUID) of the business capability where this asset originated from.
     *
     * @return unique identifier (GUID)
     */
    public String getBusinessCapabilityGUID()
    {
        return businessCapabilityGUID;
    }


    /**
     * Set up the unique identifier (GUID) of the business capability where this asset originated from.
     *
     * @param businessCapabilityGUID unique identifier (GUID)
     */
    public void setBusinessCapabilityGUID(String businessCapabilityGUID)
    {
        this.businessCapabilityGUID = businessCapabilityGUID;
    }


    /**
     * Return any other descriptive labels describing origin of the asset.
     *
     * @return map of property values
     */
    public Map<String, String> getOtherOriginValues()
    {
        if (otherOriginValues == null)
        {
            return null;
        }
        else if (otherOriginValues.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(otherOriginValues);
        }
    }


    /**
     * Set up any descriptive labels describing origin of the asset.
     *
     * @param otherOriginValues map of property values
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues)
    {
        this.otherOriginValues = otherOriginValues;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OriginRequestBody{" +
                "organizationGUID='" + organizationGUID + '\'' +
                ", businessCapabilityGUID='" + businessCapabilityGUID + '\'' +
                ", otherOriginValues=" + otherOriginValues +
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
        OriginRequestBody that = (OriginRequestBody) objectToCompare;
        return Objects.equals(getOrganizationGUID(), that.getOrganizationGUID()) &&
                Objects.equals(getBusinessCapabilityGUID(), that.getBusinessCapabilityGUID()) &&
                Objects.equals(getOtherOriginValues(), that.getOtherOriginValues());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getOrganizationGUID(), getBusinessCapabilityGUID(), getOtherOriginValues());
    }
}
