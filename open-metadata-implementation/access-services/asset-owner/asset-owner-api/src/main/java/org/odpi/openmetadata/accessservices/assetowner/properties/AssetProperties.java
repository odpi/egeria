/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.AssetOwnerType;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>type - metadata type information for the asset</li>
 *     <li>guid - globally unique identifier for the asset</li>
 *     <li>url - external link for the asset</li>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the assetQualifiedName
 *     for use on user interfaces and messages.   The assetDisplayName should only be used for audit logs and error
 *     messages if the assetQualifiedName is not set. (Sourced from attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>owner - name of the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerTypeName - name of the element type identifying the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerPropertyName - name of the property identifying person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerType - type of the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership attached to Asset - model 0445)</li>
 *     <li>zoneMembership - name of the person or organization that owns the asset.
 *     (Sourced from classification AssetZoneMemberShip attached to Asset - model 0424)</li>
 *     <li>origin - origin identifiers describing the source of the asset.
 *     (Sourced from classification AssetOrigin attached to Asset - model 0440)</li>
 *     <li>classifications - list of all classifications assigned to the asset</li>
 *     <li>extendedProperties - list of properties assigned to the asset from the Asset subclasses</li>
 *     <li>additionalProperties - list of properties assigned to the asset as additional properties</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataStoreProperties.class, name = "DataStoreProperties"),
})
public class AssetProperties extends SupplementaryProperties
{
    private String              name                         = null;
    private String              versionIdentifier            = null;
    private String              description                  = null;
    private String         owner         = null;
    private AssetOwnerType ownerType     = null;
    private String         ownerTypeName = null;
    private String              ownerPropertyName            = null;
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
            name                         = template.getName();
            versionIdentifier            = template.getVersionIdentifier();
            description                  = template.getDescription();
            owner                        = template.getOwner();
            ownerTypeName                = template.getOwnerTypeName();
            ownerPropertyName            = template.getOwnerPropertyName();
            ownerType                    = template.getOwnerType();
            zoneMembership               = template.getZoneMembership();
            originOrganizationGUID       = template.getOriginOrganizationGUID();
            originBusinessCapabilityGUID = template.getOriginBusinessCapabilityGUID();
            otherOriginValues            = template.getOtherOriginValues();
        }
    }


    /**
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
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
     * Returns the name of the type used to identify of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwnerTypeName()
    {
        return ownerTypeName;
    }


    /**
     * Set up the name of the type used to identify the owner for this asset.
     *
     * @param ownerTypeName String name
     */
    public void setOwnerTypeName(String ownerTypeName)
    {
        this.ownerTypeName = ownerTypeName;
    }


    /**
     * Returns the property name used to identify the owner for this asset.
     *
     * @return owner String
     */
    public String getOwnerPropertyName()
    {
        return ownerPropertyName;
    }


    /**
     * Set up the property name used to identify the owner for this asset.
     *
     * @param ownerPropertyName String name
     */
    public void setOwnerPropertyName(String ownerPropertyName)
    {
        this.ownerPropertyName = ownerPropertyName;
    }


    /**
     * Return the type of owner stored in the owner property.
     *
     * @return AssetOwnerType enum
     */
    @Deprecated
    public AssetOwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the owner type for this asset.
     *
     * @param ownerType AssetOwnerType enum
     */
    @Deprecated()
    public void setOwnerType(AssetOwnerType ownerType)
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
        return "AssetProperties{" +
                       "name='" + name + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", description='" + description + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerType=" + ownerType +
                       ", ownerTypeName='" + ownerTypeName + '\'' +
                       ", ownerPropertyName='" + ownerPropertyName + '\'' +
                       ", zoneMembership=" + zoneMembership +
                       ", originOrganizationGUID='" + originOrganizationGUID + '\'' +
                       ", originBusinessCapabilityGUID='" + originBusinessCapabilityGUID + '\'' +
                       ", otherOriginValues=" + otherOriginValues +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", displaySummary='" + getDisplaySummary() + '\'' +
                       ", displayDescription='" + getDisplayDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
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
        if (! (objectToCompare instanceof AssetProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        AssetProperties that = (AssetProperties) objectToCompare;

        if (name != null ? ! name.equals(that.name) : that.name != null)
        {
            return false;
        }
        if (versionIdentifier != null ? ! versionIdentifier.equals(that.versionIdentifier) : that.versionIdentifier != null)
        {
            return false;
        }
        if (description != null ? ! description.equals(that.description) : that.description != null)
        {
            return false;
        }
        if (owner != null ? ! owner.equals(that.owner) : that.owner != null)
        {
            return false;
        }
        if (ownerType != that.ownerType)
        {
            return false;
        }
        if (ownerTypeName != null ? ! ownerTypeName.equals(that.ownerTypeName) : that.ownerTypeName != null)
        {
            return false;
        }
        if (ownerPropertyName != null ? ! ownerPropertyName.equals(that.ownerPropertyName) : that.ownerPropertyName != null)
        {
            return false;
        }
        if (zoneMembership != null ? ! zoneMembership.equals(that.zoneMembership) : that.zoneMembership != null)
        {
            return false;
        }
        if (originOrganizationGUID != null ? ! originOrganizationGUID.equals(that.originOrganizationGUID) : that.originOrganizationGUID != null)
        {
            return false;
        }
        if (originBusinessCapabilityGUID != null ? ! originBusinessCapabilityGUID.equals(
                that.originBusinessCapabilityGUID) : that.originBusinessCapabilityGUID != null)
        {
            return false;
        }
        return otherOriginValues != null ? otherOriginValues.equals(that.otherOriginValues) : that.otherOriginValues == null;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, versionIdentifier, description, owner, ownerTypeName, ownerPropertyName, ownerType,
                            zoneMembership, originOrganizationGUID, originBusinessCapabilityGUID, otherOriginValues);
    }
}