/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Classification object holds properties that are used for displaying details about the classification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Classification
{
    /**
     * The name of the classification.
     */
    private String name;

    /**
     * The origin of the classification.
     */
    private String origin;

    /**
     * The origin unique identifier.
     */
    private String originGUID;

    /**
     * The author of the classification.
     */
    private String createdBy;

    /**
     * The creation date of the classification.
     */
    private Date createTime;

    /**
     * The author of the last update of the classification.
     */
    private String updatedBy;

    /**
     * The update date of the classification.
     */
    private Date updateTime;

    /**
     * The version of the classification.
     */
    private Long version;

    /**
     * The status of the classification.
     */
    private String status;

    /**
     * The type of the classification.
     */
    private Type type;

    /**
     * The properties of the classification.
     */
    private Map<String, String> properties;


    /**
     * Default constructor
     */
    public Classification()
    {
    }


    /**
     * Return the classification's name.
     *
     * @return the classification's name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the classification.
     *
     * @param name the classification's name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the classification's origin.
     *
     * @return the classification's origin
     */
    public String getOrigin()
    {
        return origin;
    }


    /**
     * Set up the origin of the classification.
     *
     * @param origin the classification's origin
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }


    /**
     * Return the origin unique identifier.
     *
     * @return the origin unique identifier
     */
    public String getOriginGUID()
    {
        return originGUID;
    }


    /**
     * Set up the origin unique identifier.
     *
     * @param originGUID the origin unique identifier
     */
    public void setOriginGUID(String originGUID)
    {
        this.originGUID = originGUID;
    }


    /**
     * Return the author of the classification.
     *
     * @return the author of the classification
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the author of the classification.
     *
     * @param createdBy the author of the classification
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the creation date of the classification.
     *
     * @return the creation date of the classification
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the creation date of the classification.
     *
     * @param createTime the creation date of the classification
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the author of the last update of the classification.
     *
     * @return the author of the last update of the classification
     */
    public String getUpdatedBy()
    {
        return updatedBy;
    }


    /**
     * Set up the author of the last update of the classification.
     *
     * @param updatedBy the author of the last update of the classification
     */
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Return the update date of the classification.
     *
     * @return the update date of the classification
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the update date of the classification.
     *
     * @param updateTime the update date of the classification
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Return the classification's version.
     *
     * @return the classification's version
     */
    public Long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the classification.
     *
     * @param version the classification's version
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }


    /**
     * Return the classification's status.
     *
     * @return the classification's status
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the classification.
     *
     * @param status the classification's status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the classification's type.
     *
     * @return the classification's type
     */
    public Type getType()
    {
        return type;
    }


    /**
     * Set up the type of the classification.
     *
     * @param type the classification's type
     */
    public void setType(Type type)
    {
        this.type = type;
    }


    /**
     * Return the classification's properties.
     *
     * @return the classification's properties
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the classification.
     *
     * @param properties the classification's properties
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Classification{" +
                       "name='" + name + '\'' +
                       ", origin='" + origin + '\'' +
                       ", originGUID='" + originGUID + '\'' +
                       ", createdBy='" + createdBy + '\'' +
                       ", createTime=" + createTime +
                       ", updatedBy='" + updatedBy + '\'' +
                       ", updateTime=" + updateTime +
                       ", version=" + version +
                       ", status='" + status + '\'' +
                       ", type=" + type +
                       ", properties=" + properties +
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
        if (! (objectToCompare instanceof Classification that))
        {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(origin, that.origin) && Objects.equals(originGUID,
                                                                                                        that.originGUID) && Objects.equals(
                createdBy, that.createdBy) && Objects.equals(createTime, that.createTime) && Objects.equals(updatedBy,
                                                                                                            that.updatedBy) && Objects.equals(
                updateTime, that.updateTime) && Objects.equals(version, that.version) && Objects.equals(status,
                                                                                                        that.status) && Objects.equals(
                type, that.type) && Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, origin, originGUID, createdBy, createTime, updatedBy, updateTime, version, status, type, properties);
    }
}
