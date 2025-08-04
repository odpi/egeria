/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdentifierProperties describes the  properties used to pass information about an external identifier.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalIdentifierProperties
{
    private PermittedSynchronization permittedSynchronization       = null;
    private String                   synchronizationDescription     = null;
    private String                   identifier                     = null;
    private String                   externalIdentifierName         = null;
    private String                   externalInstanceTypeName       = null;
    private String                   externalIdentifierUsage        = null;
    private String                   externalIdentifierSource       = null;
    private KeyPattern               keyPattern                     = null;
    private String                   externalInstanceCreatedBy      = null;
    private Date                     externalInstanceCreationTime   = null;
    private String                   externalInstanceLastUpdatedBy  = null;
    private Date                     externalInstanceLastUpdateTime = null;
    private long                     externalInstanceVersion        = 0L;
    private Map<String, String>      mappingProperties              = null;

    /**
     * Default constructor
     */
    public ExternalIdentifierProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdentifierProperties(ExternalIdentifierProperties template)
    {
        if (template != null)
        {
            permittedSynchronization       = template.getSynchronizationDirection();
            synchronizationDescription = template.getSynchronizationDescription();
            identifier                 = template.getIdentifier();
            externalIdentifierName     = template.getExternalIdentifierName();
            externalInstanceTypeName       = template.getExternalInstanceTypeName();
            externalIdentifierUsage        = template.getExternalIdentifierUsage();
            externalIdentifierSource       = template.getExternalIdentifierSource();
            keyPattern                     = template.getKeyPattern();
            externalInstanceCreatedBy      = template.getExternalInstanceCreatedBy();
            externalInstanceCreationTime   = template.getExternalInstanceCreationTime();
            externalInstanceLastUpdatedBy  = template.getExternalInstanceLastUpdatedBy();
            externalInstanceLastUpdateTime = template.getExternalInstanceLastUpdateTime();
            externalInstanceVersion        = template.getExternalInstanceVersion();
            mappingProperties              = template.getMappingProperties();
        }
    }


    /**
     * Return details of the synchronization direction.
     *
     * @return enum
     */
    public PermittedSynchronization getSynchronizationDirection()
    {
        return permittedSynchronization;
    }


    /**
     * Set up details of the synchronization direction.
     *
     * @param permittedSynchronization enum
     */
    public void setSynchronizationDirection(PermittedSynchronization permittedSynchronization)
    {
        this.permittedSynchronization = permittedSynchronization;
    }


    /**
     * Return optional short description of the asset manager.
     *
     * @return string summary
     */
    public String getSynchronizationDescription()
    {
        return synchronizationDescription;
    }


    /**
     * Set up optional short description of the asset manager.
     *
     * @param synchronizationDescription string summary
     */
    public void setSynchronizationDescription(String synchronizationDescription)
    {
        this.synchronizationDescription = synchronizationDescription;
    }


    /**
     * Return the unique identifier used in the external asset manager for this element.
     *
     * @return string identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the unique identifier used in the external asset manager for this element.
     *
     * @param identifier string identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return a short description of the external identifier (such as style or property name).
     *
     * @return string summary
     */
    public String getExternalIdentifierName()
    {
        return externalIdentifierName;
    }


    /**
     * Set up a short description of the external identifier (such as style or property name).
     *
     * @param externalIdentifierName string summary
     */
    public void setExternalIdentifierName(String externalIdentifierName)
    {
        this.externalIdentifierName = externalIdentifierName;
    }


    /**
     * Return the type of element described by this external identifier in the external system.
     *
     * @return string
     */
    public String getExternalInstanceTypeName()
    {
        return externalInstanceTypeName;
    }


    /**
     * Set up the type of element described by this external identifier in the external system.
     *
     * @param externalInstanceTypeName string
     */
    public void setExternalInstanceTypeName(String externalInstanceTypeName)
    {
        this.externalInstanceTypeName = externalInstanceTypeName;
    }


    /**
     * Return a short description of how the external identifier is used.
     *
     * @return string description
     */
    public String getExternalIdentifierUsage()
    {
        return externalIdentifierUsage;
    }


    /**
     * Set up a short description of how the external identifier is used.
     *
     * @param externalIdentifierUsage string description
     */
    public void setExternalIdentifierUsage(String externalIdentifierUsage)
    {
        this.externalIdentifierUsage = externalIdentifierUsage;
    }


    /**
     * Return the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @return component name
     */
    public String getExternalIdentifierSource()
    {
        return externalIdentifierSource;
    }


    /**
     * Set up the component (connector/client) that created/maintained this external identifier and its relationship
     * to the open metadata element(s).
     *
     * @param externalIdentifierSource component name
     */
    public void setExternalIdentifierSource(String externalIdentifierSource)
    {
        this.externalIdentifierSource = externalIdentifierSource;
    }


    /**
     * Set up the key pattern used in the asset manager for the external identifier.
     *
     * @param keyPattern String name
     */
    public void setKeyPattern(KeyPattern keyPattern)
    {
        this.keyPattern = keyPattern;
    }


    /**
     * Returns the key pattern used in the asset manager  for the external identifier.
     *
     * @return String name
     */
    public KeyPattern getKeyPattern()
    {
        return keyPattern;
    }


    /**
     * Return the username of the person or process that created the instance in the external system.
     *
     * @return name
     */
    public String getExternalInstanceCreatedBy()
    {
        return externalInstanceCreatedBy;
    }


    /**
     * Set up the username of the person or process that created the instance in the external system.
     *
     * @param externalInstanceCreatedBy name
     */
    public void setExternalInstanceCreatedBy(String externalInstanceCreatedBy)
    {
        this.externalInstanceCreatedBy = externalInstanceCreatedBy;
    }


    /**
     * Return the date/time when the instance in the external system was created.
     *
     * @return date
     */
    public Date getExternalInstanceCreationTime()
    {
        return externalInstanceCreationTime;
    }


    /**
     * Set up the date/time when the instance in the external system was created.
     *
     * @param externalInstanceCreationTime date
     */
    public void setExternalInstanceCreationTime(Date externalInstanceCreationTime)
    {
        this.externalInstanceCreationTime = externalInstanceCreationTime;
    }


    /**
     * Return the username of the person or process that last updated the instance in the external system.
     *
     * @return name
     */
    public String getExternalInstanceLastUpdatedBy()
    {
        return externalInstanceLastUpdatedBy;
    }


    /**
     * Set up the username of the person or process that last updated the instance in the external system.
     *
     * @param externalInstanceLastUpdatedBy name
     */
    public void setExternalInstanceLastUpdatedBy(String externalInstanceLastUpdatedBy)
    {
        this.externalInstanceLastUpdatedBy = externalInstanceLastUpdatedBy;
    }


    /**
     * Return the date/time that the instance in the external system was last updated.
     *
     * @return date
     */
    public Date getExternalInstanceLastUpdateTime()
    {
        return externalInstanceLastUpdateTime;
    }


    /**
     * Set up the date/time that the instance in the external system was last updated.
     *
     * @param externalInstanceLastUpdateTime date
     */
    public void setExternalInstanceLastUpdateTime(Date externalInstanceLastUpdateTime)
    {
        this.externalInstanceLastUpdateTime = externalInstanceLastUpdateTime;
    }


    /**
     * Return the latest version of the element in the external system.
     *
     * @return long
     */
    public long getExternalInstanceVersion()
    {
        return externalInstanceVersion;
    }


    /**
     * Set up the latest version of the element in the external system.
     *
     * @param externalInstanceVersion long
     */
    public void setExternalInstanceVersion(long externalInstanceVersion)
    {
        this.externalInstanceVersion = externalInstanceVersion;
    }


    /**
     * Return any additional properties to help with the mapping of the external identifier to open
     * metadata elements.
     *
     * @return name-value pairs
     */
    public Map<String, String> getMappingProperties()
    {
        return mappingProperties;
    }


    /**
     * Set up any additional properties to help with the mapping of the external identifier to open
     * metadata elements.
     *
     * @param mappingProperties name-value pairs
     */
    public void setMappingProperties(Map<String, String> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdentifierProperties{" +
                "permittedSynchronization=" + permittedSynchronization +
                ", synchronizationDescription='" + synchronizationDescription + '\'' +
                ", externalIdentifier='" + identifier + '\'' +
                ", externalIdentifierName='" + externalIdentifierName + '\'' +
                ", externalIdentifierTypeName='" + externalInstanceTypeName + '\'' +
                ", externalIdentifierUsage='" + externalIdentifierUsage + '\'' +
                ", externalIdentifierSource='" + externalIdentifierSource + '\'' +
                ", keyPattern=" + keyPattern +
                ", externalInstanceCreatedBy='" + externalInstanceCreatedBy + '\'' +
                ", externalInstanceCreationTime=" + externalInstanceCreationTime +
                ", externalInstanceLastUpdatedBy='" + externalInstanceLastUpdatedBy + '\'' +
                ", externalInstanceLastUpdateTime=" + externalInstanceLastUpdateTime +
                ", externalInstanceVersion=" + externalInstanceVersion +
                ", mappingProperties=" + mappingProperties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        ExternalIdentifierProperties that = (ExternalIdentifierProperties) objectToCompare;
        return externalInstanceVersion == that.externalInstanceVersion &&
                permittedSynchronization == that.permittedSynchronization &&
                Objects.equals(synchronizationDescription, that.synchronizationDescription) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(externalIdentifierName, that.externalIdentifierName) &&
                Objects.equals(externalInstanceTypeName, that.externalInstanceTypeName) &&
                Objects.equals(externalIdentifierUsage, that.externalIdentifierUsage) &&
                Objects.equals(externalIdentifierSource, that.externalIdentifierSource) &&
                keyPattern == that.keyPattern &&
                Objects.equals(externalInstanceCreatedBy, that.externalInstanceCreatedBy) &&
                Objects.equals(externalInstanceCreationTime, that.externalInstanceCreationTime) &&
                Objects.equals(externalInstanceLastUpdatedBy, that.externalInstanceLastUpdatedBy) &&
                Objects.equals(externalInstanceLastUpdateTime, that.externalInstanceLastUpdateTime) &&
                Objects.equals(mappingProperties, that.mappingProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(permittedSynchronization, synchronizationDescription, identifier,
                            externalIdentifierName, externalInstanceTypeName, externalIdentifierUsage,
                            externalIdentifierSource, keyPattern, externalInstanceCreatedBy,
                            externalInstanceCreationTime, externalInstanceLastUpdatedBy,
                            externalInstanceLastUpdateTime, externalInstanceVersion, mappingProperties);
    }
}
