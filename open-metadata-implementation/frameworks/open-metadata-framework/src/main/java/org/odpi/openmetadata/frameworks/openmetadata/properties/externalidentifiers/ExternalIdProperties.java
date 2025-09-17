/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalIdProperties describes the properties used to pass information about an external identifier.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalIdProperties extends ReferenceableProperties
{
    private String     key                            = null;
    private KeyPattern keyPattern                     = null;
    private String     externalInstanceTypeName       = null;
    private String     externalInstanceCreatedBy      = null;
    private Date       externalInstanceCreationTime   = null;
    private String     externalInstanceLastUpdatedBy  = null;
    private Date       externalInstanceLastUpdateTime = null;
    private long       externalInstanceVersion        = 0L;


    /**
     * Default constructor
     */
    public ExternalIdProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ExternalIdProperties(ExternalIdProperties template)
    {
        super(template);

        if (template != null)
        {
            key                      = template.getKey();
            externalInstanceTypeName = template.getExternalInstanceTypeName();
            keyPattern                     = template.getKeyPattern();
            externalInstanceCreatedBy      = template.getExternalInstanceCreatedBy();
            externalInstanceCreationTime   = template.getExternalInstanceCreationTime();
            externalInstanceLastUpdatedBy  = template.getExternalInstanceLastUpdatedBy();
            externalInstanceLastUpdateTime = template.getExternalInstanceLastUpdateTime();
            externalInstanceVersion        = template.getExternalInstanceVersion();
        }
    }


    /**
     * Return the unique identifier used in the external asset manager for this element.
     *
     * @return string identifier
     */
    public String getKey()
    {
        return key;
    }


    /**
     * Set up the unique identifier used in the external asset manager for this element.
     *
     * @param key string identifier
     */
    public void setKey(String key)
    {
        this.key = key;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalIdProperties{" +
                "identifier='" + key + '\'' +
                ", keyPattern=" + keyPattern +
                ", externalInstanceTypeName='" + externalInstanceTypeName + '\'' +
                ", externalInstanceCreatedBy='" + externalInstanceCreatedBy + '\'' +
                ", externalInstanceCreationTime=" + externalInstanceCreationTime +
                ", externalInstanceLastUpdatedBy='" + externalInstanceLastUpdatedBy + '\'' +
                ", externalInstanceLastUpdateTime=" + externalInstanceLastUpdateTime +
                ", externalInstanceVersion=" + externalInstanceVersion +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ExternalIdProperties that = (ExternalIdProperties) objectToCompare;
        return externalInstanceVersion == that.externalInstanceVersion &&
                Objects.equals(key, that.key) &&
                Objects.equals(externalInstanceTypeName, that.externalInstanceTypeName) &&
                keyPattern == that.keyPattern &&
                Objects.equals(externalInstanceCreatedBy, that.externalInstanceCreatedBy) &&
                Objects.equals(externalInstanceCreationTime, that.externalInstanceCreationTime) &&
                Objects.equals(externalInstanceLastUpdatedBy, that.externalInstanceLastUpdatedBy) &&
                Objects.equals(externalInstanceLastUpdateTime, that.externalInstanceLastUpdateTime);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), key, externalInstanceTypeName, keyPattern, externalInstanceCreatedBy,
                            externalInstanceCreationTime, externalInstanceLastUpdatedBy,
                            externalInstanceLastUpdateTime, externalInstanceVersion);
    }
}
