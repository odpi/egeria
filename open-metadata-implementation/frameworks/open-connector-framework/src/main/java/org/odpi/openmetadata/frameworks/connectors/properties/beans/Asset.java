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
 *     <li>resourceName - name extracted from the resource.   (Sourced from attribute name within Asset - model 0010)</li>
 *     <li>resourceDescription - description extracted from the resource.   (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>displayName - A consumable name for the resource for use on user interfaces and messages.
 *     (Sourced from attribute displayName within GlossaryTerm - model 0330)</li>
 *     <li>displaySummary - A short description of the resource for use on user interfaces and messages.
 *     (Sourced from attribute summary within GlossaryTerm - model 0330)</li>
 *     <li>displayDescription - A full description of the resource in business terminology for use on user interfaces.
 *     (Sourced from attribute description within GlossaryTerm - model 0330)</li>
 *     <li>abbreviation - A short name or acronym for the resource.
 *     (Sourced from attribute abbreviation within GlossaryTerm - model 0330)</li>
 *     <li>usage - A description of how the resource is used by the business.
 *     (Sourced from attribute usage within GlossaryTerm - model 0330)</li>
 *     <li>connectionDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset relationship - model 0205)</li>
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
 *     <li>assetOrigin - origin identifiers describing the source of the asset.
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

    protected String              resourceName          = null;
    protected String              resourceDescription   = null;
    protected String              versionIdentifier     = null;
    protected String              displayName           = null;
    protected String              displaySummary        = null;
    protected String              displayDescription    = null;
    protected String              abbreviation          = null;
    protected String              usage                 = null;
    protected String              connectionDescription = null;
    protected String              owner                 = null;
    protected String              ownerTypeName         = null;
    protected String              ownerPropertyName     = null;
    protected OwnerType           ownerType             = null;
    protected List<String>        zoneMembership        = null;
    protected Map<String, String> origin                = null;
    protected boolean             isReferenceData       = false;

    /*
     * Deprecated properties
     */
    protected String name        = null;
    protected String description = null;


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
            resourceName           = template.getResourceName();
            resourceDescription    = template.getResourceDescription();
            versionIdentifier      = template.getVersionIdentifier();
            displayName            = template.getDisplayName();
            displaySummary         = template.getDisplaySummary();
            displayDescription     = template.getDisplayDescription();
            abbreviation           = template.getAbbreviation();
            usage                  = template.getUsage();
            connectionDescription  = template.getConnectionDescription();
            owner                  = template.getOwner();
            ownerTypeName          = template.getOwnerTypeName();
            ownerPropertyName      = template.getOwnerPropertyName();
            ownerType              = template.getOwnerType();
            zoneMembership         = template.getZoneMembership();
            origin                 = template.getAssetOrigin();
            isReferenceData        = template.isReferenceData();

            description            = template.getDescription();
            name                   = template.getName();
        }
    }


    /**
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    public String getResourceName()
    {
        return resourceName;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    public void setResourceName(String name)
    {
        this.resourceName = name;
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
     * If no display name is available then resource name is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        if (displayName == null)
        {
            return resourceName;
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
     * Return the short display description for tables and summaries.
     *
     * @return string description
     */
    public String getDisplaySummary()
    {
        return displaySummary;
    }


    /**
     * Set up the short display description for tables and summaries.
     *
     * @param displaySummary string description
     */
    public void setDisplaySummary(String displaySummary)
    {
        this.displaySummary = displaySummary;
    }


    /**
     * Return the full business description.
     *
     * @return string description
     */
    public String getDisplayDescription()
    {
        return displayDescription;
    }


    /**
     * Set up the full business description.
     *
     * @param displayDescription string description
     */
    public void setDisplayDescription(String displayDescription)
    {
        this.displayDescription = displayDescription;
    }


    /**
     * Return the abbreviation or acronym associated with the resources display name.
     *
     * @return string name
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Set up the abbreviation or acronym associated with the resources display name.
     *
     * @param abbreviation string name
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    /**
     * Return the usage information for the resource.  This typically describes how the organization uses the resource.
     *
     * @return string description
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the usage information for the resource.  This typically describes how the organization uses the resource.
     *
     * @param usage string description
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Returns the short description of the asset from relationship with Connection.
     *
     * @return shortDescription String
     */
    public String getConnectionDescription()
    {
        return connectionDescription;
    }


    /**
     * Set up the short description of the asset from relationship with Connection.
     *
     * @param connectionDescription String text
     */
    public void setConnectionDescription(String connectionDescription)
    {
        this.connectionDescription = connectionDescription;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getResourceDescription()
    {
        return resourceDescription;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setResourceDescription(String description)
    {
        this.resourceDescription = description;
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
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    @Deprecated
    public String getName()
    {
        if (name == null)
        {
            if (resourceName != null)
            {
                return resourceName;
            }

            return displayName;
        }

        return name;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    @Deprecated
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    @Deprecated
    public String getDescription()
    {
        if (description == null)
        {
            if (resourceDescription != null)
            {
                return resourceDescription;
            }

            return displayDescription;
        }

        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    @Deprecated
    public void setDescription(String description)
    {
        this.description = description;
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
                       "resourceName='" + resourceName + '\'' +
                       ", resourceDescription='" + resourceDescription + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", displaySummary='" + displaySummary + '\'' +
                       ", displayDescription='" + displayDescription + '\'' +
                       ", abbreviation='" + abbreviation + '\'' +
                       ", usage='" + usage + '\'' +
                       ", connectionDescription='" + connectionDescription + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerTypeName='" + ownerTypeName + '\'' +
                       ", ownerPropertyName='" + ownerPropertyName + '\'' +
                       ", ownerType=" + ownerType +
                       ", zoneMembership=" + zoneMembership +
                       ", origin=" + origin +
                       ", isReferenceData=" + isReferenceData +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", assetOrigin=" + getAssetOrigin() +
                       ", referenceData=" + isReferenceData() +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + getMeanings() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", headerVersion=" + getHeaderVersion() +
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
        return isReferenceData == asset.isReferenceData && Objects.equals(resourceName, asset.resourceName) && Objects.equals(
                resourceDescription, asset.resourceDescription) && Objects.equals(versionIdentifier,
                                                                                  asset.versionIdentifier) && Objects.equals(
                displayName, asset.displayName) && Objects.equals(displaySummary, asset.displaySummary) && Objects.equals(
                displayDescription, asset.displayDescription) && Objects.equals(abbreviation, asset.abbreviation) && Objects.equals(
                usage, asset.usage) && Objects.equals(connectionDescription, asset.connectionDescription) && Objects.equals(owner,
                                                                                                                            asset.owner) && Objects.equals(
                ownerTypeName, asset.ownerTypeName) && Objects.equals(ownerPropertyName,
                                                                      asset.ownerPropertyName) && ownerType == asset.ownerType && Objects.equals(
                zoneMembership, asset.zoneMembership) && Objects.equals(origin, asset.origin) && Objects.equals(name,
                                                                                                                asset.name) && Objects.equals(
                description, asset.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceName, resourceDescription, versionIdentifier, displayName, displaySummary, displayDescription,
                            abbreviation, usage, connectionDescription, owner, ownerTypeName, ownerPropertyName, ownerType, zoneMembership, origin,
                            isReferenceData, name, description);
    }
}