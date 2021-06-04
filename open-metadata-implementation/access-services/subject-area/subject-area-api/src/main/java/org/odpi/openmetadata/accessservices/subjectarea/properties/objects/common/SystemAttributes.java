/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;

import java.io.Serializable;
import java.util.Date;


/**
 * These are the core ' system' attributes
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SystemAttributes implements Serializable {
    protected static final long serialVersionUID = 1L;
    /*
     * system attributes
     */
    private String GUID  = null;
    private Status status     = null;
    private String createdBy  = null;
    private String updatedBy  = null;
    private Long createTime   = null;
    private Long updateTime   = null;
    private Long version    = null;

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    /**
     * Status indicates the status of the object - the values are specified in the Status enumeration
     * @return status of the object
     */
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * User who created this object
     * @return user who created this.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * User who last updated this object
     * @return use who last updated this
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * The time at which this object was created.
     * @return create time in milliseconds since epoch
     */
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    /**
     * The time at which this object was last updated.
     * @return update time in milliseconds since epoch
     */
    public Long getUpdateTime() {
        return updateTime;
    }
    /**
     * Update the time at which this object was last updated in milliseconds since epoch
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * version of the object.
     * @return version
     */
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public StringBuilder toString(StringBuilder sb)
    {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("GUID=").append(GUID).append(",");
        sb.append("status=").append(this.status).append(",");
        sb.append("createdBy=").append(createdBy).append(",");
        sb.append("updatedBy=").append(updatedBy).append(",");
        sb.append("createTime=").append(createTime).append(",");
        sb.append("updateTime=").append(updateTime).append(",");
        sb.append("version=").append(version).append(",");
        return sb;
    }
}
