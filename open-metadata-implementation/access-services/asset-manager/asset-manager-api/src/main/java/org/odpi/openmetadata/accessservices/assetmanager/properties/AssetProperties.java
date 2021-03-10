/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataAssetProperties is a java bean used to create assets associated with the external asset manager.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ProcessProperties.class, name = "ProcessProperties"),
        })
public class AssetProperties extends SupplementaryProperties
{
    private static final long     serialVersionUID = 1L;

    private String              technicalName                = null;
    private String              technicalDescription         = null;
    private String              owner                        = null;
    private OwnerCategory       ownerCategory                = null;
    private List<String>        zoneMembership               = null;
    private String              originOrganizationGUID       = null;
    private String              originBusinessCapabilityGUID = null;
    private Map<String, String> otherOriginValues            = null;


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
            technicalName = template.getTechnicalName();
            technicalDescription = template.getTechnicalDescription();
            owner = template.getOwner();
            ownerCategory = template.getOwnerCategory();
            zoneMembership = template.getZoneMembership();
            originOrganizationGUID = template.getOriginOrganizationGUID();
            originBusinessCapabilityGUID = template.getOriginBusinessCapabilityGUID();
            otherOriginValues = template.getOtherOriginValues();
        }
    }


    /**
     * Returns the stored name property for the asset.  This is the technical name of the asset rather than the name
     * that it is commonly known as.  If no technical name is available then null is returned.
     *
     * @return String name
     */
    public String getTechnicalName()
    {
        return technicalName;
    }


    /**
     * Set up the stored name property for the asset. This is the technical name of the asset rather than the name
     * that it is commonly known as.
     *
     * @param technicalName String name
     */
    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }


    /**
     * Returns the stored technical description property for the asset.
     * If no technical description is provided then null is returned.
     *
     * @return String text
     */
    public String getTechnicalDescription()
    {
        return technicalDescription;
    }


    /**
     * Set up the stored technical description property associated with the asset.
     *
     * @param description String text
     */
    public void setTechnicalDescription(String description)
    {
        this.technicalDescription = description;
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
     * Return the unique identifier for the organization that originated this asset.
     *
     * @return string guid
     */
    public String getOriginOrganizationGUID()
    {
        return originOrganizationGUID;
    }


    /**
     * Set up the unique identifier for the organization that originated this asset.
     *
     * @param originOrganizationGUID string guid
     */
    public void setOriginOrganizationGUID(String originOrganizationGUID)
    {
        this.originOrganizationGUID = originOrganizationGUID;
    }


    /**
     * Return the unique identifier of the business capability that originated this asset.
     *
     * @return string guid
     */
    public String getOriginBusinessCapabilityGUID()
    {
        return originBusinessCapabilityGUID;
    }


    /**
     * Set up the unique identifier of the business capability that originated this asset.
     *
     * @param originBusinessCapabilityGUID string guid
     */
    public void setOriginBusinessCapabilityGUID(String originBusinessCapabilityGUID)
    {
        this.originBusinessCapabilityGUID = originBusinessCapabilityGUID;
    }


    /**
     * Return the properties that characterize where this asset is from.
     *
     * @return map of name value pairs, all strings
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
     * Set up the properties that characterize where this asset is from.
     *
     * @param otherOriginValues map of name value pairs, all strings
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues)
    {
        this.otherOriginValues = otherOriginValues;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataAssetProperties{" +
                       "technicalName='" + technicalName + '\'' +
                       ", technicalDescription='" + technicalDescription + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerCategory=" + ownerCategory +
                       ", zoneMembership=" + zoneMembership +
                       ", originOrganizationGUID='" + originOrganizationGUID + '\'' +
                       ", originBusinessCapabilityGUID='" + originBusinessCapabilityGUID + '\'' +
                       ", otherOriginValues=" + otherOriginValues +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
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
        AssetProperties that = (AssetProperties) objectToCompare;
        return Objects.equals(getTechnicalName(), that.getTechnicalName()) &&
                       Objects.equals(getTechnicalDescription(), that.getTechnicalDescription()) &&
                       Objects.equals(getOwner(), that.getOwner()) &&
                       getOwnerCategory() == that.getOwnerCategory() &&
                       Objects.equals(getZoneMembership(), that.getZoneMembership()) &&
                       Objects.equals(getOriginOrganizationGUID(), that.getOriginOrganizationGUID()) &&
                       Objects.equals(getOriginBusinessCapabilityGUID(), that.getOriginBusinessCapabilityGUID()) &&
                       Objects.equals(getOtherOriginValues(), that.getOtherOriginValues());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), technicalName, technicalDescription, owner, ownerCategory, zoneMembership, originOrganizationGUID,
                            originBusinessCapabilityGUID, otherOriginValues);
    }
}
