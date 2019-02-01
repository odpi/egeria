/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Map;
import java.util.Objects;

public class RangerTag {

    private Long id;
    private String guid;
    private Boolean isEnabled;
    private String createdBy;
    private String updatedBy;
    private String createTime;
    private Long version;
    private String type;
    private Map<String, String> attributes;
    private Map<String, Object> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getCreatedBy() {
        return createdBy;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangerTag rangerTag = (RangerTag) o;
        return guid.equals(rangerTag.guid) &&
                type.equals(rangerTag.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, type);
    }

    @Override
    public String toString() {
        return "RangerTag{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", isEnabled=" + isEnabled +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", version=" + version +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                ", options=" + options +
                '}';
    }
}
