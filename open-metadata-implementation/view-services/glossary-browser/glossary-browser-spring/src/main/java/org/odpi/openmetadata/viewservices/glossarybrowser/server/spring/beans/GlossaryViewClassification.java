/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that represents a Classification
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryViewClassification {

    private String name;
    private String classificationType;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
    private String status;
    private Map<String, String> properties = new HashMap<>();

    public String getName() {
        return name;
    }

    public String getClassificationType() {
        return classificationType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GlossaryViewClassification setClassificationType(String classificationType) {
        this.classificationType = classificationType;
        return this;
    }

    public GlossaryViewClassification setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public GlossaryViewClassification setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public GlossaryViewClassification setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public GlossaryViewClassification setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public GlossaryViewClassification setStatus(String status) {
        this.status = status;
        return this;
    }

    public GlossaryViewClassification setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public GlossaryViewClassification addProperties(Map<String, String> properties) {
        if(this.properties == null){
            this.properties = new HashMap<>();
        }
        this.properties.putAll(properties);
        return this;
    }

    public GlossaryViewClassification addProperty(String key, String value) {
        if(this.properties == null){
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
        return this;
    }

}
