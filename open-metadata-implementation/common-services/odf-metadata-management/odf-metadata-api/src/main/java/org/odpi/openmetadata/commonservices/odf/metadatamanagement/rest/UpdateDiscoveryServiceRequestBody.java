/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateDiscoveryServiceRequestBody provides a structure for passing the updated properties of a discovery service
 * as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateDiscoveryServiceRequestBody extends NewDiscoveryServiceRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              shortDescription     = null;
    private String              owner                = null;
    private OwnerType           ownerType            = null;
    private List<String>        zoneMembership       = null;
    private Map<String, String> origin               = null;
    private String              latestChange         = null;
    private Map<String, String> additionalProperties = null;
    private Map<String, Object> extendedProperties   = null;


    /**
     * Default constructor
     */
    public UpdateDiscoveryServiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateDiscoveryServiceRequestBody(UpdateDiscoveryServiceRequestBody template)
    {
        super(template);

        if (template != null)
        {
            shortDescription = template.getShortDescription();
            owner = template.getOwner();
            ownerType = template.getOwnerType();
            zoneMembership = template.getZoneMembership();
            origin = template.getOrigin();
            latestChange = template.getLatestChange();
            additionalProperties = template.getAdditionalProperties();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Returns the short description of the discovery service from relationship with Connection.
     *
     * @return shortDescription String
     */
    public String getShortDescription()
    {
        return shortDescription;
    }


    /**
     * Set up the short description of the discovery service from relationship with Connection.
     *
     * @param shortDescription String text
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }


    /**
     * Returns the name of the owner for this discovery service.
     *
     * @return owner String
     */
    public String getOwner() {
        return owner;
    }


    /**
     * Set up the name of the owner for this discovery service.
     *
     * @param owner String name
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return the type of owner stored in the owner property.
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the owner type for this discovery service.
     *
     * @param ownerType OwnerType enum
     */
    public void setOwnerType(OwnerType ownerType)
    {
        this.ownerType = ownerType;
    }


    /**
     * Return the names of the zones that this discovery service is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership()
    {
        if (zoneMembership == null)
        {
            return null;
        }
        else if (zoneMembership.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(zoneMembership);
        }
    }


    /**
     * Set up the names of the zones that this discovery service is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Set up the properties that describe the origin of the discovery service.
     *
     * @param origin map object
     */
    public void setOrigin(Map<String, String> origin)
    {
        this.origin = origin;
    }


    /**
     * Return the properties that describe the origin of the discovery service.
     *
     * @return map
     */
    public Map<String, String> getOrigin()
    {
        if (origin == null)
        {
            return null;
        }
        else if (origin.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(origin);
        }
    }


    /**
     * Return a short description of the last change to the asset.
     *
     * @return string description
     */
    public String getLatestChange()
    {
        return latestChange;
    }


    /**
     * Set up a short description of the last change to the asset.
     *
     * @param latestChange string description
     */
    public void setLatestChange(String latestChange)
    {
        this.latestChange = latestChange;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up properties from subclasses properties.
     *
     * @param extendedProperties asset properties map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return a copy of the properties from subclasses.  Null means no extended properties are available.
     *
     * @return asset property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateDiscoveryServiceRequestBody{" +
                "shortDescription='" + shortDescription + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", origin=" + origin +
                ", latestChange=" + latestChange +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", connection=" + getConnection() +
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
        UpdateDiscoveryServiceRequestBody that = (UpdateDiscoveryServiceRequestBody) objectToCompare;
        return Objects.equals(getShortDescription(), that.getShortDescription()) &&
                Objects.equals(getOwner(), that.getOwner()) &&
                getOwnerType() == that.getOwnerType() &&
                Objects.equals(getZoneMembership(), that.getZoneMembership()) &&
                Objects.equals(getOrigin(), that.getOrigin()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                Objects.equals(getLatestChange(), that.getLatestChange());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getShortDescription(), getOwner(), getOwnerType(), getZoneMembership(),
                            getOrigin(), getLatestChange(), getAdditionalProperties(), getExtendedProperties());
    }
}
