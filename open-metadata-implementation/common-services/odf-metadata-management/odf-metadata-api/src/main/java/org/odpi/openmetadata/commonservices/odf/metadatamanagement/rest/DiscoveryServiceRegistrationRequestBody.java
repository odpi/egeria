/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryServiceRegistrationRequestBody provides a structure for passing details of a discovery service
 * that is to be registered with a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServiceRegistrationRequestBody extends ODFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String       discoveryServiceGUID = null;
    private List<String> assetDiscoveryTypes  = null;

    /**
     * Default constructor
     */
    public DiscoveryServiceRegistrationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceRegistrationRequestBody(DiscoveryServiceRegistrationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            discoveryServiceGUID = template.getDiscoveryServiceGUID();
            assetDiscoveryTypes  = template.getAssetDiscoveryTypes();
        }
    }


    /**
     * Return the unique identifier of the discovery service.
     *
     * @return guid
     */
    public String getDiscoveryServiceGUID()
    {
        return discoveryServiceGUID;
    }


    /**
     * Set up the unique identifier of the discovery service.
     *
     * @param discoveryServiceGUID guid
     */
    public void setDiscoveryServiceGUID(String discoveryServiceGUID)
    {
        this.discoveryServiceGUID = discoveryServiceGUID;
    }


    /**
     * Return the list of asset types that this discovery service supports.
     *
     * @return list of asset type names
     */
    public List<String> getAssetDiscoveryTypes()
    {
        if (assetDiscoveryTypes == null)
        {
            return null;
        }
        else if (assetDiscoveryTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(assetDiscoveryTypes);
        }
    }


    /**
     * Set up the list of asset types that this discovery service supports.
     *
     * @param assetDiscoveryTypes list of asset type names
     */
    public void setAssetDiscoveryTypes(List<String> assetDiscoveryTypes)
    {
        this.assetDiscoveryTypes = assetDiscoveryTypes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceRegistrationRequestBody{" +
                "discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                ", assetDiscoveryTypes=" + assetDiscoveryTypes +
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
        DiscoveryServiceRegistrationRequestBody that = (DiscoveryServiceRegistrationRequestBody) objectToCompare;
        return Objects.equals(getDiscoveryServiceGUID(), that.getDiscoveryServiceGUID()) &&
                Objects.equals(getAssetDiscoveryTypes(), that.getAssetDiscoveryTypes());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getDiscoveryServiceGUID(), getAssetDiscoveryTypes());
    }
}
