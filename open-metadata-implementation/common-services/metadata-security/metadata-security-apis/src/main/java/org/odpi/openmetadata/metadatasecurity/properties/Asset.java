/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import java.io.Serial;
import java.util.*;

/**
 * Asset is a set of properties that describes an open metadata asset.  It is designed to convey the important properties
 * needed to make a security decision.
 */
public class Asset extends Referenceable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String              displayName      = null;
    private String              description      = null;
    private List<String>        zoneMembership   = null;
    private Map<String, String> origin           = null;


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
            displayName            = template.getDisplayName();
            description            = template.getDescription();

            zoneMembership         = template.getZoneMembership();
            origin                 = template.getOrigin();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Asset{" +
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", zoneMembership=" + zoneMembership +
                       ", origin=" + origin +
                       ", typeGUID='" + getTypeGUID() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", status=" + getStatus() +
                       ", GUID='" + getGUID() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerType=" + getOwnerType() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", securityLabels=" + getSecurityLabels() +
                       ", securityProperties=" + getSecurityProperties() +
                       ", accessGroups=" + getAccessGroups() +
                       ", confidentiality=" + getConfidentiality() +
                       ", confidence=" + getConfidence() +
                       ", criticality=" + getCriticality() +
                       ", impact=" + getImpact() +
                       ", retention=" + getRetention() +
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
        Asset asset = (Asset) objectToCompare;
        return Objects.equals(getDisplayName(), asset.getDisplayName()) &&
                Objects.equals(getDescription(), asset.getDescription()) &&
                Objects.equals(getZoneMembership(), asset.getZoneMembership()) &&
                Objects.equals(getOrigin(), asset.getOrigin());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getZoneMembership(), getOrigin());
    }
}
