/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Software server capability.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoftwareServerCapability implements Serializable {
    private static final long serialVersionUID = 1L;

    private String qualifiedName;
    private String displayName;
    private String description;
    private String engineType;
    private String engineVersion;
    private String patchLevel;
    private String source;

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
     * Gets engine type.
     *
     * @return the engine type
     */
    public String getEngineType() {
        return engineType;
    }

    /**
     * Sets engine type.
     *
     * @param engineType the engine type
     */
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    /**
     * Gets engine version.
     *
     * @return the engine version
     */
    public String getEngineVersion() {
        return engineVersion;
    }

    /**
     * Sets engine version.
     *
     * @param engineVersion the engine version
     */
    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets qualified name.
     *
     * @param qualifiedName the qualified name
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param source the source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets patch level.
     *
     * @return the patch level
     */
    public String getPatchLevel() {
        return patchLevel;
    }

    /**
     * Sets patch level.
     *
     * @param patchLevel the patch level
     */
    public void setPatchLevel(String patchLevel) {
        this.patchLevel = patchLevel;
    }

    @Override
    public String toString() {
        return "DataEngineRegistrationRequestBody{" +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", engineType='" + engineType + '\'' +
                ", engineVersion='" + engineVersion + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareServerCapability that = (SoftwareServerCapability) o;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(engineType, that.engineType) &&
                Objects.equals(engineVersion, that.engineVersion) &&
                Objects.equals(patchLevel, that.patchLevel) &&
                Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, displayName, description, engineType, engineVersion, patchLevel, source);
    }

}
