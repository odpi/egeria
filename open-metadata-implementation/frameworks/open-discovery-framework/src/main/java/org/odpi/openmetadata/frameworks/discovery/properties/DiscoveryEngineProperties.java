/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryEngineProperties describe the properties needed to describe a specific discovery engine.
 * These properties are augmented with a list of registered discovery services (see RegisteredDiscoveryService).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryEngineProperties extends PropertyBase
{
    private static final long    serialVersionUID = 1L;

    private String              qualifiedName        = null;
    private String              displayName          = null;
    private String              description          = null;
    private String              typeDescription      = null;
    private String              version              = null;
    private String              patchLevel           = null;
    private String              source               = null;

    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor.
     */
    public DiscoveryEngineProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DiscoveryEngineProperties(DiscoveryEngineProperties  template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName        = template.getQualifiedName();
            displayName          = template.getDisplayName();
            description          = template.getDescription();
            typeDescription      = template.getTypeDescription();
            version              = template.getVersion();
            patchLevel           = template.getPatchLevel();
            source               = template.getSource();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the display name for messages and UI.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for messages and UI.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the discovery engine.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the discovery engine.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description of the type of discovery engine this is.
     *
     * @return string description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }


    /**
     * Set up the description of the type of discovery engine this is.
     *
     * @param typeDescription string
     */
    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
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
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineProperties{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", version='" + version + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
                ", source='" + source + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
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
        DiscoveryEngineProperties that = (DiscoveryEngineProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getTypeDescription(), that.getTypeDescription()) &&
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
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getTypeDescription(), getVersion(),
                            getPatchLevel(), getSource());
    }
}
