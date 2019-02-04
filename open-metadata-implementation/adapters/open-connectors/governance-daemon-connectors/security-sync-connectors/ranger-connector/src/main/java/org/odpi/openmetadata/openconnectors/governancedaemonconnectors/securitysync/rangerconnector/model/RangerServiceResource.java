/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class RangerServiceResource {

    private Long id;
    private String guid;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
    private Long version;

    private String serviceName;
    private Map<String, RangerPolicyResource> resourceElements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, RangerPolicyResource> getResourceElements() {
        return resourceElements;
    }

    public void setResourceElements(Map<String, RangerPolicyResource> resourceElements) {
        this.resourceElements = resourceElements;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "RangerServiceResource{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", serviceName='" + serviceName + '\'' +
                ", resourceElements=" + resourceElements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangerServiceResource resource = (RangerServiceResource) o;
        return Objects.equals(serviceName, resource.serviceName) &&
                Objects.equals(resourceElements, resource.resourceElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, resourceElements);
    }
}
