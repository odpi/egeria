/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStore extends Asset {
    private String pathName;
    private Date createTime;
    private Date modifiedTime;
    private String encodingType;
    private String encodingLanguage;
    private String encodingDescription;
    private Map<String, String> encodingProperties;

    /**
     * Return the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @return string name
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Set up the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @param pathName string name
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * Return the time that the data store was created.
     *
     * @return date
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Set up the time that the data store was created.
     *
     * @param createTime date
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Return the last known time the data store was modified.
     *
     * @return date
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * Setup the last known time the data store was modified.
     *
     * @param modifiedTime date
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
     * Return the name of the encoding style used in the data store.
     *
     * @return string name
     */
    public String getEncodingType() {
        return encodingType;
    }

    /**
     * Set up the name of the encoding style used in the data store.
     *
     * @param encodingType string name
     */
    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    /**
     * Return the name of the natural language used for text strings within the data store.
     *
     * @return string language name
     */
    public String getEncodingLanguage() {
        return encodingLanguage;
    }

    /**
     * Set up the name of the natural language used for text strings within the data store.
     *
     * @param encodingLanguage string language name
     */
    public void setEncodingLanguage(String encodingLanguage) {
        this.encodingLanguage = encodingLanguage;
    }

    /**
     * Return the description of the encoding used in the data store.
     *
     * @return string text
     */
    public String getEncodingDescription() {
        return encodingDescription;
    }

    /**
     * Set up the description of the encoding used in the data store.
     *
     * @param encodingDescription string text
     */
    public void setEncodingDescription(String encodingDescription) {
        this.encodingDescription = encodingDescription;
    }

    /**
     * Return the additional properties associated with the encoding process.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getEncodingProperties() {
        if (encodingProperties == null) {
            return null;
        } else if (encodingProperties.isEmpty()) {
            return null;
        }
        return encodingProperties;
    }

    /**
     * Set up the additional properties associated with the encoding process.
     *
     * @param encodingProperties map of name-value pairs
     */
    public void setEncodingProperties(Map<String, String> encodingProperties) {
        this.encodingProperties = encodingProperties;
    }

    @Override
    public String toString() {
        return "DataStore{" +
                "pathName='" + pathName + '\'' +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", encodingType='" + encodingType + '\'' +
                ", encodingLanguage='" + encodingLanguage + '\'' +
                ", encodingDescription='" + encodingDescription + '\'' +
                ", encodingProperties=" + encodingProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataStore dataStore = (DataStore) o;
        return Objects.equals(pathName, dataStore.pathName) &&
                Objects.equals(createTime, dataStore.createTime) &&
                Objects.equals(modifiedTime, dataStore.modifiedTime) &&
                Objects.equals(encodingType, dataStore.encodingType) &&
                Objects.equals(encodingLanguage, dataStore.encodingLanguage) &&
                Objects.equals(encodingDescription, dataStore.encodingDescription) &&
                Objects.equals(encodingProperties, dataStore.encodingProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathName, createTime, modifiedTime, encodingType, encodingLanguage, encodingDescription, encodingProperties);
    }
}
