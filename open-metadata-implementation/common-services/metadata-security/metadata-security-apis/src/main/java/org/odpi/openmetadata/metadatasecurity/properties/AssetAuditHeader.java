/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetAuditHeader provides details of the audit header for a specific asset
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetAuditHeader implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String         createdBy     = null;
    private String         updatedBy     = null;
    private List<String>   maintainedBy  = null;
    private Date           createTime    = null;
    private Date           updateTime    = null;
    private long           version       = 0L;


    /**
     * Default constructor
     */
    public AssetAuditHeader()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetAuditHeader(AssetAuditHeader template)
    {
        if (template != null)
        {
            this.createdBy = template.getCreatedBy();
            this.updatedBy = template.getUpdatedBy();
            this.maintainedBy = template.getMaintainedBy();
            this.createTime = template.getCreateTime();
            this.updateTime = template.getUpdateTime();
            this.version = template.getVersion();
        }
    }


    /**
     * Return the name of the user that created this instance.
     *
     * @return String user name
     */
    public String getCreatedBy() { return createdBy; }


    /**
     * Set up the name of the user that created this instance.
     *
     * @param createdBy String user name
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }


    /**
     * Return the name of the user that last updated this instance.
     *
     * @return String user name
     */
    public String getUpdatedBy() { return updatedBy; }


    /**
     * Set up the name of the user that last updated this instance.
     *
     * @param updatedBy String user name
     */
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }


    /**
     * Return the list of users responsible for maintaining this instance.
     *
     * @return list of user identifiers
     */
    public List<String> getMaintainedBy()
    {
        if (maintainedBy == null)
        {
            return null;
        }
        else if (maintainedBy.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(maintainedBy);
        }
    }


    /**
     * Set up the list of users responsible for maintaining this instance.
     *
     * @param maintainedBy list of user identifiers
     */
    public void setMaintainedBy(List<String> maintainedBy)
    {
        this.maintainedBy = maintainedBy;
    }


    /**
     * Return the date/time that this instance was created.
     *
     * @return Date/Time of creation
     */
    public Date getCreateTime()
    {
        if (createTime == null)
        {
            return null;
        }
        else
        {
            return new Date(createTime.getTime());
        }
    }


    /**
     * Set up the time that this instance was created.
     *
     * @param createTime Date/Time of creation
     */
    public void setCreateTime(Date createTime) { this.createTime = createTime; }


    /**
     * Return what was the late time this instance was updated.
     *
     * @return Date/Time last updated
     */
    public Date getUpdateTime()
    {
        if (updateTime == null)
        {
            return null;
        }
        else
        {
            return new Date(updateTime.getTime());
        }
    }


    /**
     * Set up the last update time for this instance.
     *
     * @param updateTime Date/Time last updated
     */
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }


    /**
     * Return the version number for this instance.
     *
     * @return Long version number
     */
    public long getVersion() { return version; }


    /**
     * Set up the version number for this instance.
     *
     * @param version Long version number
     */
    public void setVersion(long version) { this.version = version; }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetAuditHeader{" +
                "createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", maintainedBy=" + maintainedBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
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
        AssetAuditHeader that = (AssetAuditHeader) objectToCompare;
        return getVersion() == that.getVersion() &&
                Objects.equals(getCreatedBy(), that.getCreatedBy()) &&
                Objects.equals(getUpdatedBy(), that.getUpdatedBy()) &&
                Objects.equals(getMaintainedBy(), that.getMaintainedBy()) &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getCreatedBy(), getUpdatedBy(), getMaintainedBy(), getCreateTime(), getUpdateTime(),
                            getVersion());
    }
}
