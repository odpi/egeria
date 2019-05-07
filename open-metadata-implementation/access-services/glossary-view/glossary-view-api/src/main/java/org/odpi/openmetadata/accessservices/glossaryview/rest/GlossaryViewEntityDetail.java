/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that holds the data of any entity queried from the OMRS, that needs to be returned to the client
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryViewEntityDetail{

    private String entityClass;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
    private long version;
    private Date effectiveFromTime;
    private Date effectiveToTime;

    private String qualifiedName;
    private String guid;
    private String displayName;
    private String description;
    private String language;
    private String usage;
    private String status;

    public GlossaryViewEntityDetail(){

    }

    public GlossaryViewEntityDetail(String entityClass, String createdBy, String updatedBy, Date createTime, Date updateTime, long version,
                                    Date effectiveFromTime, Date effectiveToTime, String qualifiedName, String guid, String displayName,
                                    String description, String language, String usage, String status){
        this.entityClass = entityClass;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.version = version;
        this.effectiveFromTime = effectiveFromTime;
        this.effectiveToTime = effectiveToTime;
        this.qualifiedName = qualifiedName;
        this.guid = guid;
        this.displayName = displayName;
        this.description = description;
        this.language = language;
        this.usage = usage;
        this.status = status;
    }

    public String getEntityClass(){ return entityClass; }

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

    public long getVersion() {
        return version;
    }

    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getGuid() {
        return guid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getUsage() {
        return usage;
    }

    public String getStatus() {
        return status;
    }

    public GlossaryViewEntityDetail setEntityClass(String entityClass) {
        this.entityClass = entityClass;
        return this;
    }
    public GlossaryViewEntityDetail setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public GlossaryViewEntityDetail setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public GlossaryViewEntityDetail setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public GlossaryViewEntityDetail setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public GlossaryViewEntityDetail setVersion(long version) {
        this.version = version;
        return this;
    }

    public GlossaryViewEntityDetail setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
        return this;
    }

    public GlossaryViewEntityDetail setEffectiveToTime(Date effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
        return this;
    }

    public GlossaryViewEntityDetail setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
        return this;
    }

    public GlossaryViewEntityDetail setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public GlossaryViewEntityDetail setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public GlossaryViewEntityDetail setDescription(String description) {
        this.description = description;
        return this;
    }

    public GlossaryViewEntityDetail setLanguage(String language) {
        this.language = language;
        return this;
    }

    public GlossaryViewEntityDetail setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public GlossaryViewEntityDetail setStatus(String status) {
        this.status = status;
        return this;
    }
}
