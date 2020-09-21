/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;


import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * The lineage entity represents the single node in lineage graph with self contained properties.
 */
public class LineageEntity {

    private String guid;
    private String typeDefName;
    private String createdBy;
    private String updatedBy = null;
    private Date createTime;
    private Date updateTime = null;
    private long version;
    private String metadataCollectionId;
    private Map<String, String> properties;

    /**
     * Gets guid.
     *
     * @return the guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets guid.
     *
     * @param guid the guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Gets type def name.
     *
     * @return the type def name
     */
    public String getTypeDefName() {
        return typeDefName;
    }

    /**
     * Sets type def name.
     *
     * @param typeDefName the type def name
     */
    public void setTypeDefName(String typeDefName) {
        this.typeDefName = typeDefName;
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets created by.
     *
     * @param createdBy the created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets updated by.
     *
     * @return the updated by
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets updated by.
     *
     * @param updatedBy the updated by
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets update time.
     *
     * @return the update time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets update time.
     *
     * @param updateTime the update time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(long version) {
        this.version = version;
    }


    /**
     * Gets metadataCollectionId.
     *
     * @return the metadataCollectionId
     */
    public String getMetadataCollectionId() {
        return metadataCollectionId;
    }

    /**
     * Sets metadataCollectionId.
     *
     * @param metadataCollectionId the metadataCollectionId
     */
    public void setMetadataCollectionId(String metadataCollectionId) {
        this.metadataCollectionId = metadataCollectionId;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageEntity that = (LineageEntity) o;
        return version == that.version &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(typeDefName, that.typeDefName) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, typeDefName, createdBy, updatedBy, createTime, updateTime, version, properties);
    }

    @Override
    public String toString() {
        return "LineageEntity{" +
                "guid='" + guid + '\'' +
                ", typeDefName='" + typeDefName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", properties=" + properties +
                '}';
    }
}
