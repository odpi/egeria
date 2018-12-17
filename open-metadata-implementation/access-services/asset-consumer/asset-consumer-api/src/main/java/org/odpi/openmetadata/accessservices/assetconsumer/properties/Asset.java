/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Asset describes the basic properties of an Asset that include its name, type, description and owner.
 * With this information it is possible to drill down and understand more details such as its location,
 * classification and internal structure through the Asset Consumer's integration with the Connected Asset OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Asset.class, name = "AssetCollectionMember")
})
public class Asset extends ReferenceableHeader
{
    private String               displayName          = null;
    private String               description          = null;
    private String               owner                = null;
    private List<String>         zoneMembership       = null;
    private String               lastChange           = null;


    /**
     * Default constructor
     */
    public Asset()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public Asset(Asset   template)
    {
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.owner = template.getOwner();
            this.zoneMembership = template.getZoneMembership();
            this.lastChange = template.getLastChange();
        }
    }



    /**
     * Return the display name for this asset (normally a shortened for of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened for of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this asset.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this asset.
     *
     * @param description string
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
     * Return the description of the last change to the asset.  This is free form text.
     *
     * @return string description
     */
    public String getLastChange()
    {
        return lastChange;
    }


    /**
     * Set up the description of the last change to the asset.  This is free form text.
     *
     * @param lastChange string description
     */
    public void setLastChange(String lastChange)
    {
        this.lastChange = lastChange;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Asset{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", zoneMembership=" + zoneMembership +
                ", lastChange='" + lastChange + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", classifications=" + getClassifications() +
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
        Asset asset = (Asset) objectToCompare;
        return Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
                Objects.equals(getOwner(), asset.getOwner()) &&
                Objects.equals(getZoneMembership(), asset.getZoneMembership()) &&
                Objects.equals(getLastChange(), asset.getLastChange());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getOwner(), getZoneMembership(),
                            getLastChange());
    }
}
