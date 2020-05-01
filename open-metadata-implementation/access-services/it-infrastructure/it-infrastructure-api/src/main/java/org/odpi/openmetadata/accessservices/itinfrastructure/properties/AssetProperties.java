/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties is a java bean used to create assets associated with the data platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetProperties.class, name = "AssetProperties"),
                @JsonSubTypes.Type(value = SoftwareServerProperties.class, name = "SoftwareServerProperties"),
        })
public class AssetProperties extends ConfigurationItemProperties
{
    private static final long     serialVersionUID = 1L;

    private String              displayName      = null;
    private String              description      = null;
    private String              owner            = null;
    private OwnerCategory       ownerCategory    = null;
    private List<String>        zoneMembership   = null;
    private Map<String, String> origin           = null;
    private String              latestChange     = null;


    /**
     * Default constructor
     */
    public AssetProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public AssetProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName      = template.getDisplayName();
            description      = template.getDescription();
            owner            = template.getOwner();
            ownerCategory    = template.getOwnerCategory();
            zoneMembership   = template.getZoneMembership();
            origin           = template.getOrigin();
            latestChange     = template.getLatestChange();
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
    public String getOwner()
    {
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
     * Return the type of owner stored in the owner property.
     *
     * @return OwnerCategory enum
     */
    public OwnerCategory getOwnerCategory()
    {
        return ownerCategory;
    }


    /**
     * Set up the owner type for this asset.
     *
     * @param ownerType OwnerCategory enum
     */
    public void setOwnerCategory(OwnerCategory ownerType)
    {
        this.ownerCategory = ownerType;
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
     * Return the properties that characterize where this asset is from.
     *
     * @return map of name value pairs, all strings
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
     * Set up the properties that characterize where this asset is from.
     *
     * @param origin map of name value pairs, all strings
     */
    public void setOrigin(Map<String, String> origin)
    {
        this.origin = origin;
    }


    /**
     * Return a short description of the last change to the asset.  If it is null it means
     * the agent that last updated the asset did not provide a description.
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReferenceDataAssetProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerCategory=" + ownerCategory +
                ", zoneMembership=" + zoneMembership +
                ", origin=" + origin +
                ", latestChange='" + latestChange + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", classifications=" + getClassifications() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetProperties asset = (AssetProperties) objectToCompare;
        return Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
                Objects.equals(getOwner(), asset.getOwner()) &&
                getOwnerCategory() == asset.getOwnerCategory() &&
                Objects.equals(getZoneMembership(), asset.getZoneMembership()) &&
                Objects.equals(getOrigin(), asset.getOrigin()) &&
                Objects.equals(getLatestChange(), asset.getLatestChange());
    }



    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getOwner(),
                            getOwnerCategory(), getZoneMembership(), getOrigin(), getLatestChange());
    }
}
