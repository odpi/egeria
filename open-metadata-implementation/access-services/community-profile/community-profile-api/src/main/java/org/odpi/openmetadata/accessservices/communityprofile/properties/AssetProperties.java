/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.CollectionMemberHeader;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties describes an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String       name                 = null;
    private String       description          = null;
    private String       owner                = null;
    private OwnerType    ownerType            = null;
    private List<String> zoneMembership       = null;
    private String       lastChange           = null;
    private Date         dateAssetCreated     = null;
    private Date         dateAssetLastUpdated = null;


    /**
     * Default constructor
     */
    public AssetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.name = template.getName();
            this.description = template.getDescription();
            this.owner = template.getOwner();
            this.ownerType = template.getOwnerType();
            this.zoneMembership = template.getZoneMembership();
            this.dateAssetCreated = template.getDateAssetCreated();
            this.dateAssetLastUpdated = template.getDateAssetLastUpdated();
        }
    }



    /**
     * Return the name of the asset.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the asset.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the asset.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the asset.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Return the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @return string id
     */
    public String getOwner()
    {
        return owner;
    }


    /**
     * Set up the owner for this asset.  This is the user id of the person or engine that is responsible for
     * managing this asset.
     *
     * @param owner string id
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return the type of owner identifier used in the owner field (default is userId).
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the type of owner identifier used in the owner field (default is userId).
     *
     * @param ownerType OwnerType enum
     */
    public void setOwnerType(OwnerType ownerType)
    {
        this.ownerType = ownerType;
    }


    /**
     * Return the names of the zones that this asset is a member of.
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
            return zoneMembership;
        }
    }


    /**
     * Set up the names of the zones that this asset is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return the date that the asset was created.
     *
     * @return date
     */
    public Date getDateAssetCreated()
    {
        if (dateAssetCreated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetCreated.getTime());
        }
    }


    /**
     * Set up the date that the asset was created.
     *
     * @param dateAssetCreated date
     */
    public void setDateAssetCreated(Date dateAssetCreated)
    {
        this.dateAssetCreated = dateAssetCreated;
    }


    /**
     * Return the date that the asset was last updated.
     *
     * @return date
     */
    public Date getDateAssetLastUpdated()
    {
        if (dateAssetLastUpdated == null)
        {
            return null;
        }
        else
        {
            return new Date(dateAssetLastUpdated.getTime());
        }
    }


    /**
     * Set up the date that the asset was last updated.
     *
     * @param dateAssetLastUpdated date
     */
    public void setDateAssetLastUpdated(Date dateAssetLastUpdated)
    {
        this.dateAssetLastUpdated = dateAssetLastUpdated;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetProperties{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerType=" + ownerType +
                       ", zoneMembership=" + zoneMembership +
                       ", lastChange='" + lastChange + '\'' +
                       ", dateAssetCreated=" + dateAssetCreated +
                       ", dateAssetLastUpdated=" + dateAssetLastUpdated +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        AssetProperties that = (AssetProperties) objectToCompare;
        return Objects.equals(getOwner(), that.getOwner()) &&
                       Objects.equals(getName(), that.getName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getOwnerType(), that.getOwnerType()) &&
                       Objects.equals(getZoneMembership(), that.getZoneMembership()) &&
                       Objects.equals(getDateAssetCreated(), that.getDateAssetCreated()) &&
                       Objects.equals(getDateAssetLastUpdated(), that.getDateAssetLastUpdated()) &&
                       Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                       Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getOwner(), getOwnerType(), getZoneMembership(), getName(), getDescription(), getDateAssetCreated(),
                            getDateAssetLastUpdated(), getExtendedProperties(), getAdditionalProperties());
    }
}
