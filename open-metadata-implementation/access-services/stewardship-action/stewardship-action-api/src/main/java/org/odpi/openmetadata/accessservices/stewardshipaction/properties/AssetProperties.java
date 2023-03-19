/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties is a java bean used to describe assets managed by the governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetProperties extends SupplementaryProperties
{
    private static final long     serialVersionUID = 1L;

    private String resourceName        = null;
    private String versionIdentifier   = null;
    private String resourceDescription = null;

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
            resourceName = template.getDisplayName();
            versionIdentifier = template.getVersionIdentifier();
            resourceDescription = template.getResourceDescription();
        }
    }


    /**
     * Returns the name of the resource as known to the technology that supports it.
     * If no technical name is available then null is returned.
     *
     * @return String name
     */
    public String getResourceName()
    {
        return resourceName;
    }


    /**
     * Set up name of the resource as known to the technology that supports it.
     *
     * @param resourceName String name
     */
    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
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
     * Returns the stored technical description property from the resource.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getResourceDescription()
    {
        return resourceDescription;
    }


    /**
     * Set up the stored technical description property associated with the resource.
     *
     * @param resourceDescription String text
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.resourceDescription = resourceDescription;
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
                       "resourceName='" + resourceName + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", resourceDescription='" + resourceDescription + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
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

        if (resourceName != null ? ! resourceName.equals(that.resourceName) : that.resourceName != null)
        {
            return false;
        }
        if (versionIdentifier != null ? ! versionIdentifier.equals(that.versionIdentifier) : that.versionIdentifier != null)
        {
            return false;
        }
        return resourceDescription != null ? resourceDescription.equals(that.resourceDescription) : that.resourceDescription == null;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceName, versionIdentifier, resourceDescription);
    }
}
