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
 *     <li>latestChange - description of last update to the asset.
 *     (Sourced from classification LatestChange attached to Asset - model 0010)</li>
 *     <li>isReferenceData - flag to show if asset contains reference data.
 *     (Sourced from classification ReferenceData within Asset - model 0524)</li>
 *     <li>classifications - list of all classifications assigned to the asset</li>
 *     <li>extendedProperties - list of properties assigned to the asset from the Asset subclasses</li>
 *     <li>additionalProperties - list of properties assigned to the asset as additional properties</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Asset extends GovernedReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected String              name              = null;
    protected String              versionIdentifier = null;
    protected String              displayName       = null;
    protected String              shortDescription  = null;
    protected String              description       = null;
    protected String              owner             = null;
    protected String              ownerTypeName     = null;
    protected String              ownerPropertyName = null;
    protected OwnerType           ownerType         = null;
    protected List<String>        zoneMembership    = null;
    protected Map<String, String> origin            = null;
    protected boolean             isReferenceData   = false;


    /**
     * Default constructor
     */
    public Asset()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset summary
     */
    public Asset(Asset template)
    {
        super(template);

        if (template != null)
        {
            name                   = template.getName();
            versionIdentifier      = template.getVersionIdentifier();
            displayName            = template.getDisplayName();
            shortDescription       = template.getShortDescription();
            description            = template.getDescription();
            owner                  = template.getOwner();
            ownerTypeName          = template.getOwnerTypeName();
            ownerPropertyName      = template.getOwnerPropertyName();
            ownerType              = template.getOwnerType();
            zoneMembership         = template.getZoneMembership();
            origin                 = template.getAssetOrigin();
            isReferenceData        = template.isReferenceData();
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
     * Returns the stored display name property for the asset.
     * If no display name is available then name is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        if (displayName == null)
        {
            return name;
        }

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
     * Return the properties that characterize where this asset is from.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getAssetOrigin()
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
    public void setAssetOrigin(Map<String, String> origin)
    {
        this.origin = origin;
    }


    /**
     * Return a boolean to see if the asset has been marked as reference data.
     *
     * @return true if the asset contains reference data
     */
    public boolean isReferenceData()
    {
        return isReferenceData;
    }


    /**
     * Set up the boolean to see if the asset has been marked as reference data.
     *
     * @param referenceData true if the asset contains reference data
     */
    public void setReferenceData(boolean referenceData)
    {
        isReferenceData = referenceData;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Asset{" +
                       "name='" + name + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", shortDescription='" + shortDescription + '\'' +
                       ", description='" + description + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerTypeName='" + ownerTypeName + '\'' +
                       ", ownerPropertyName='" + ownerPropertyName + '\'' +
                       ", ownerType=" + ownerType +
                       ", zoneMembership=" + zoneMembership +
                       ", origin=" + origin +
                       ", isReferenceData=" + isReferenceData +
                       ", assetOrigin=" + getAssetOrigin() +
                       ", referenceData=" + isReferenceData() +
                       ", url='" + url + '\'' +
                       ", extendedProperties=" + extendedProperties +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + meanings +
                       ", securityTags=" + securityTags +
                       ", searchKeywords=" + searchKeywords +
                       ", latestChange='" + latestChange + '\'' +
                       ", latestChangeDetails=" + latestChangeDetails +
                       ", confidentialityGovernanceClassification=" + confidentialityGovernanceClassification +
                       ", confidenceGovernanceClassification=" + confidenceGovernanceClassification +
                       ", criticalityGovernanceClassification=" + criticalityGovernanceClassification +
                       ", retentionGovernanceClassification=" + retentionGovernanceClassification +
                       ", meanings=" + getMeanings() +
                       ", securityTags=" + getSecurityTags() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", latestChange='" + getLatestChange() + '\'' +
                       ", latestChangeDetails=" + getLatestChangeDetails() +
                       ", confidentialityGovernanceClassification=" + getConfidentialityGovernanceClassification() +
                       ", confidenceGovernanceClassification=" + getConfidenceGovernanceClassification() +
                       ", criticalityGovernanceClassification=" + getCriticalityGovernanceClassification() +
                       ", retentionGovernanceClassification=" + getRetentionGovernanceClassification() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! (objectToCompare instanceof Asset))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        Asset asset = (Asset) objectToCompare;
        return isReferenceData == asset.isReferenceData && Objects.equals(name, asset.name) && Objects.equals(versionIdentifier, asset.versionIdentifier) && Objects.equals(
                displayName, asset.displayName) && Objects.equals(shortDescription, asset.shortDescription) && Objects.equals(
                description, asset.description) && Objects.equals(owner, asset.owner) && Objects.equals(ownerTypeName, asset.ownerTypeName) && Objects.equals(
                ownerPropertyName, asset.ownerPropertyName) && ownerType == asset.ownerType && Objects.equals(zoneMembership, asset.zoneMembership) && Objects.equals(
                origin, asset.origin);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, versionIdentifier, displayName, shortDescription, description, owner, ownerTypeName,
                            ownerPropertyName, ownerType, zoneMembership, origin, isReferenceData);
    }
}