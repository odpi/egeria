/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupplementaryProperties describe additional information about a technical element (typically assets and schemas)
 * that has been added as part of a governance process.
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
                @JsonSubTypes.Type(value = EndpointProperties.class, name = "EndpointProperties"),
                @JsonSubTypes.Type(value = SoftwareCapabilityProperties.class, name = "SoftwareCapabilityProperties"),
        })
public class SupplementaryProperties extends ReferenceableProperties
{
    private String displayName        = null;
    private String displaySummary     = null;
    private String displayDescription = null;
    private String abbreviation       = null;
    private String usage              = null;


    /**
     * Default constructor
     */
    public SupplementaryProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public SupplementaryProperties(SupplementaryProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName    = template.getDisplayName();
            displaySummary     = template.getDisplaySummary();
            displayDescription = template.getDisplayDescription();
            abbreviation       = template.getAbbreviation();
            usage = template.getUsage();
        }
    }


    /**
     * Returns the stored display name property for the technical element.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the technical element.
     *
     * @param resourceName String name
     */
    public void setDisplayName(String resourceName)
    {
        this.displayName = resourceName;
    }


    /**
     * Return the short (1-2 sentence) description of the technical element.
     *
     * @return string text
     */
    public String getDisplaySummary()
    {
        return displaySummary;
    }


    /**
     * Set up the short (1-2 sentence) description of the technical element.
     *
     * @param displaySummary string text
     */
    public void setDisplaySummary(String displaySummary)
    {
        this.displaySummary = displaySummary;
    }


    /**
     * Returns the stored description property for the technical element.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDisplayDescription()
    {
        return displayDescription;
    }


    /**
     * Set up the stored description property for the technical element.
     *
     * @param resourceDescription String text
     */
    public void setDisplayDescription(String resourceDescription)
    {
        this.displayDescription = resourceDescription;
    }


    /**
     * Return the abbreviation used for this technical element.
     *
     * @return string text
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Set up the abbreviation used for this technical element.
     *
     * @param abbreviation string text
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    /**
     * Return details of the expected usage of this technical element.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up details of the expected usage of this technical element.
     *
     * @param usage string text
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
        return "SupplementaryProperties{" +
                "displayName='" + displayName + '\'' +
                ", displaySummary='" + displaySummary + '\'' +
                ", displayDescription='" + displayDescription + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
                "} " + super.toString();
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
        SupplementaryProperties that = (SupplementaryProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDisplaySummary(), that.getDisplaySummary()) &&
                       Objects.equals(getDisplayDescription(), that.getDisplayDescription()) &&
                       Objects.equals(getAbbreviation(), that.getAbbreviation()) &&
                       Objects.equals(getUsage(), that.getUsage());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDisplaySummary(), getDisplayDescription(), getAbbreviation(), getUsage());
    }
}
