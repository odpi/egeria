/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Element object holds properties that are used for displaying details of an entity.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Element implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guid;
    private Type type;
    private String name;
    private String createdBy;
    private Date createTime;
    private String updatedBy;
    private Date updateTime;
    private Long version;
    private String status;
    private String url;
    private Map<String, String> properties;
    private Map<String, String> additionalProperties;
    private List<Classification> classifications;
    private Element parentElement;

    /**
     * Return the asset unique identifier
     *
     * @return String - unique identified of the asset
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Set up the unique identifier of the asset
     *
     * @param guid of the asset
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Returns the type definition of the asset
     *
     * @return the type definition of the asset
     */
    public Type getType() {
        return type;
    }

    /**
     * Set up the type definition of the asset
     *
     * @param type - the type definition of the asset
     */
    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the name of the user that created the asset.
     *
     * @return the name of the users that created the asset
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set up the name of the user that created the asset
     *
     * @param createdBy - the name of the user that created the asset
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Returns the date when the asset has been created
     *
     * @return date when for the asset creation
     */
    public Date getCreateTime() {
        return createTime;
    }


    /**
     * Set up the date when the asset has been created
     *
     * @param createTime - creation date of the asset
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Return the name of the user that updated the asset last time
     *
     * @return string - the name of the user that updated the asset last time
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Set up the name of the user that updated the asset last time
     *
     * @param updatedBy the name of the user that updated the asset last time
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Returns the date when the asset has been created
     *
     * @return date - the date when the asset has been created
     */

    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Set up the date when the asset has been created
     *
     * @param updateTime - the date when the asset has been created
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Returns the version of the asset
     *
     * @return long - the version of the asset
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Set up the version of the asset
     *
     * @param version -  the version of the asset
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the status of the asset
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set up the status of the asset
     *
     * @param status - enum that describes the asset's status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    public Element getParentElement() {
        return parentElement;
    }

    public void setParentElement(Element parentElement) {
        this.parentElement = parentElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(guid, element.guid) &&
                Objects.equals(type, element.type) &&
                Objects.equals(name, element.name) &&
                Objects.equals(createdBy, element.createdBy) &&
                Objects.equals(createTime, element.createTime) &&
                Objects.equals(updatedBy, element.updatedBy) &&
                Objects.equals(updateTime, element.updateTime) &&
                Objects.equals(version, element.version) &&
                Objects.equals(status, element.status) &&
                Objects.equals(url, element.url) &&
                Objects.equals(properties, element.properties) &&
                Objects.equals(additionalProperties, element.additionalProperties) &&
                Objects.equals(classifications, element.classifications) &&
                Objects.equals(parentElement, element.parentElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, type, name, createdBy, createTime, updatedBy, updateTime, version, status, url, properties, additionalProperties, classifications, parentElement);
    }
}
