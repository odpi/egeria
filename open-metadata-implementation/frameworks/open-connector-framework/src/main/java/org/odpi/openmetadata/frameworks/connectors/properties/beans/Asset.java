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
    protected String              resourceName          = null;
    protected String              resourceDescription   = null;
    protected String              versionIdentifier     = null;
    protected String              displayName           = null;
    protected String              displaySummary        = null;
    protected String              displayDescription    = null;
    protected String              abbreviation          = null;
    protected String              usage                 = null;


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
     * Return the description associated with the resource.
     *
     * @return text
     */
    public String getResourceDescription()
    {
        return resourceDescription;
    }

    /**
     * Set up the description associated with the resource.
     *
     * @param resourceDescription text
     */
    public void setResourceDescription(String resourceDescription)
    {
        this.resourceDescription = resourceDescription;
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
        if (! (objectToCompare instanceof Asset asset))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(resourceName, asset.resourceName) &&
                Objects.equals(resourceDescription, asset.resourceDescription) &&
                Objects.equals(versionIdentifier, asset.versionIdentifier) &&
                Objects.equals(displayName, asset.displayName) &&
                Objects.equals(displaySummary, asset.displaySummary) &&
                Objects.equals(displayDescription, asset.displayDescription) &&
                Objects.equals(abbreviation, asset.abbreviation) &&
                Objects.equals(usage, asset.usage);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceName, resourceDescription, versionIdentifier,
                            displayName, displaySummary, displayDescription, abbreviation, usage);
    }
}