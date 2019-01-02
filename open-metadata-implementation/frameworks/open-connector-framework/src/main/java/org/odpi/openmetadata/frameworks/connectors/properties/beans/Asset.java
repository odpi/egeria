/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Asset holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>type - metadata type information for the asset</li>
 *     <li>guid - globally unique identifier for the asset</li>
 *     <li>url - external link for the asset</li>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the assetQualifiedName
 *     for use on user interfaces and messages.   The assetDisplayName should be only be used for audit logs and error
 *     messages if the assetQualifiedName is not set. (Sourced from attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>owner - name of the person or organization that owns the asset.
 *     (Sourced from attribute owner within Asset - model 0010)</li>
 *     <li>zoneMembership - name of the person or organization that owns the asset.
 *     (Sourced from attribute zoneMembership within Asset - model 0010)</li>
 *     <li>classifications - list of classifications assigned to the asset</li>
 *     <li>schemaProperties - list of properties assigned to the asset from the Asset subclasses</li>
 *     <li>additionalProperties - list of properties assigned to the asset as additional properties</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Asset extends Referenceable
{
    protected String               displayName      = null;
    protected String               shortDescription = null;
    protected String               description      = null;
    protected String               owner            = null;
    protected List<String>         zoneMembership   = null;
    protected Map<String,Object>   assetProperties  = null;


    /**
     * Default constructor
     */
    public Asset()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param templateAsset template values for asset summary
     */
    public Asset(Asset templateAsset)
    {
        super(templateAsset);

        if (templateAsset != null)
        {
            displayName = templateAsset.getDisplayName();
            shortDescription = templateAsset.getShortDescription();
            description = templateAsset.getDescription();
            owner = templateAsset.getOwner();
            zoneMembership = templateAsset.getZoneMembership();
            assetProperties = templateAsset.getAssetProperties();
        }
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the short description of the asset from relationship with Connection.
     *
     * @return shortDescription String
     */
    public String getShortDescription()
    {
        return shortDescription;
    }


    /**
     * Set up the short description of the asset from relationship with Connection.
     *
     * @param shortDescription String text
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Returns the name of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwner() {
        return owner;
    }


    /**
     * Set up the name of the owner for this asset.
     *
     * @param owner String name
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
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
            return new ArrayList<>(zoneMembership);
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
     * Set up asset properties.
     *
     * @param assetProperties asset properties map
     */
    public void setAssetProperties(Map<String,Object> assetProperties)
    {
        this.assetProperties = assetProperties;
    }


    /**
     * Return a copy of the asset properties.  Null means no asset properties are available.
     *
     * @return asset property map
     */
    public Map<String,Object> getAssetProperties()
    {
        if (assetProperties == null)
        {
            return null;
        }
        else if (assetProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(assetProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSummary{" +
                "type=" + type +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", classifications=" + classifications + '\'' +
                ", zoneMembership=" + zoneMembership + '\'' +
                ", schemaProperties=" + assetProperties +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof Asset))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Asset asset = (Asset) objectToCompare;
        return Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getShortDescription(), asset.getShortDescription()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
                Objects.equals(getOwner(), asset.getOwner()) &&
                Objects.equals(getZoneMembership(), asset.getZoneMembership()) &&
                Objects.equals(getAssetProperties(), asset.getAssetProperties());
    }
}