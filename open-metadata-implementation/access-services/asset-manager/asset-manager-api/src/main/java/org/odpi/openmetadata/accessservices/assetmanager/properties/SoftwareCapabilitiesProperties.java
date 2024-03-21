/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SoftwareCapabilitiesProperties describes a functional capability of a
 * software server.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetManagerProperties.class, name = "AssetManagerProperties")
        })
public class SoftwareCapabilitiesProperties extends SupplementaryProperties
{
    private String technicalName              = null;
    private String technicalDescription       = null;
    private String deployedImplementationType = null;
    private String version                    = null;
    private String patchLevel                 = null;
    private String source                     = null;


    /**
     * Default constructor.
     */
    public SoftwareCapabilitiesProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareCapabilitiesProperties(SoftwareCapabilitiesProperties template)
    {
        super(template);

        if (template != null)
        {
            technicalName = template.getTechnicalName();
            technicalDescription       = template.getTechnicalDescription();
            deployedImplementationType = template.getDeployedImplementationType();
            version                    = template.getVersion();
            patchLevel = template.getPatchLevel();
            source = template.getSource();
        }
    }


    /**
     * Return the technical name for the capability.
     *
     * @return string name
     */
    public String getTechnicalName()
    {
        return technicalName;
    }


    /**
     * Set up the technical name for the capability.
     *
     * @param technicalName string name
     */
    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }


    /**
     * Return the technical description of the capability.
     *
     * @return string description
     */
    public String getTechnicalDescription()
    {
        return technicalDescription;
    }


    /**
     * Set up the technical description of the capability.
     *
     * @param description string
     */
    public void setTechnicalDescription(String description)
    {
        this.technicalDescription = description;
    }


    /**
     * Return the description of the type of capability this is.
     *
     * @return string description
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the description of the type of capability this is.
     *
     * @param deployedImplementationType string
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the version of the discovery engine.
     *
     * @return version string
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version string of the discovery engine.
     *
     * @param version string
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the patch level of the discovery engine.
     *
     * @return patch level string
     */
    public String getPatchLevel()
    {
        return patchLevel;
    }


    /**
     * Set up the patch level of the discovery engine.
     *
     * @param patchLevel string
     */
    public void setPatchLevel(String patchLevel)
    {
        this.patchLevel = patchLevel;
    }


    /**
     * Return the source of the discovery engine implementation.
     *
     * @return string url
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of the discovery engine implementation.
     *
     * @param source string url
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SoftwareCapabilitiesProperties{" +
                       "technicalName='" + technicalName + '\'' +
                       ", technicalDescription='" + technicalDescription + '\'' +
                       ", typeDescription='" + deployedImplementationType + '\'' +
                       ", version='" + version + '\'' +
                       ", patchLevel='" + patchLevel + '\'' +
                       ", source='" + source + '\'' +
                       ", technicalDescription='" + getTechnicalDescription() + '\'' +
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
        SoftwareCapabilitiesProperties that = (SoftwareCapabilitiesProperties) objectToCompare;
        return Objects.equals(getTechnicalName(), that.getTechnicalName()) &&
                       Objects.equals(getTechnicalDescription(), that.getTechnicalDescription()) &&
                       Objects.equals(getDeployedImplementationType(), that.getDeployedImplementationType()) &&
                       Objects.equals(getVersion(), that.getVersion()) &&
                       Objects.equals(getPatchLevel(), that.getPatchLevel()) &&
                       Objects.equals(getSource(), that.getSource());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTechnicalName(), getTechnicalDescription(), getDeployedImplementationType(), getVersion(),
                            getPatchLevel(), getSource());
    }
}
