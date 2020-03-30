/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetRequestBody carries the parameters for created a new asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetRequestBody extends ReferenceableRequestBody
{
    private static final long    serialVersionUID = 1L;

    protected String       displayName      = null;
    protected String       description      = null;
    protected String       owner            = null;
    protected OwnerType    ownerType        = null;
    protected List<String> zoneMembership   = null;
    protected String       latestChange     = null;


    /**
     * Default constructor
     */
    public AssetRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetRequestBody(AssetRequestBody template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            owner = template.getOwner();
            ownerType = template.getOwnerType();
            zoneMembership = template.getZoneMembership();
            latestChange = template.getLatestChange();
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
     * Return the type of owner stored in the owner property.
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the owner type for this asset.
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetRequestBody{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", latestChange='" + latestChange + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
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
        AssetRequestBody requestBody = (AssetRequestBody) objectToCompare;
        return Objects.equals(getDisplayName(), requestBody.getDisplayName()) &&
                Objects.equals(getDescription(), requestBody.getDescription()) &&
                Objects.equals(getOwner(), requestBody.getOwner()) &&
                getOwnerType() == requestBody.getOwnerType() &&
                Objects.equals(getZoneMembership(), requestBody.getZoneMembership()) &&
                Objects.equals(getLatestChange(), requestBody.getLatestChange());
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
                            getOwnerType(), getZoneMembership(), getLatestChange());
    }
}