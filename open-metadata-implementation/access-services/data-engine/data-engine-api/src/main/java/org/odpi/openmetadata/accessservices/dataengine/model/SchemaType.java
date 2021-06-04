/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Schema type.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaType extends Referenceable {
    private static final long serialVersionUID = 1L;
    private String displayName;
    private String author;
    private String usage;
    private String encodingStandard;
    private String versionNumber;
    private String type;
    @JsonProperty("columns")
    private List<Attribute> attributeList;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets usage.
     *
     * @return the usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets usage.
     *
     * @param usage the usage
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Gets encoding standard.
     *
     * @return the encoding standard
     */
    public String getEncodingStandard() {
        return encodingStandard;
    }

    /**
     * Sets encoding standard.
     *
     * @param encodingStandard the encoding standard
     */
    public void setEncodingStandard(String encodingStandard) {
        this.encodingStandard = encodingStandard;
    }

    /**
     * Gets version number.
     *
     * @return the version number
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets version number.
     *
     * @param versionNumber the version number
     */
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Gets attribute list.
     *
     * @return the attribute list
     */
    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    /**
     * Sets attribute list.
     *
     * @param attributeList the attribute list
     */
    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SchemaType{" +
                ", displayName='" + displayName + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", type=" + type +
                ", attributeList=" + attributeList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaType that = (SchemaType) o;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(author, that.author) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(encodingStandard, that.encodingStandard) &&
                Objects.equals(versionNumber, that.versionNumber) &&
                Objects.equals(type, that.type) &&
                Objects.equals(attributeList, that.attributeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, author, usage, encodingStandard, versionNumber, attributeList);
    }

}
