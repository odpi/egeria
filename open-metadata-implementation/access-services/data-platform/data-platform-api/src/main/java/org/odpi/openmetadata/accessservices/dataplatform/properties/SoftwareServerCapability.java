/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoftwareServerCapability implements Serializable{

    private static final long serialVersionUID = 1L;

    private String qualifiedName;
    private String displayName;
    private String description;
    private String dataPlatformType;
    private String dataPlatformVersion;
    private String patchLevel;
    private String source;

    public SoftwareServerCapability() {
    }

    public SoftwareServerCapability(String qualifiedName, String displayName, String description, String dataPlatformType, String dataPlatformVersion, String patchLevel, String source) {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.description = description;
        this.dataPlatformType = dataPlatformType;
        this.dataPlatformVersion = dataPlatformVersion;
        this.patchLevel = patchLevel;
        this.source = source;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataPlatformType() {
        return dataPlatformType;
    }

    public void setDataPlatformType(String dataPlatformType) {
        this.dataPlatformType = dataPlatformType;
    }

    public String getDataPlatformVersion() {
        return dataPlatformVersion;
    }

    public void setDataPlatformVersion(String dataPlatformVersion) {
        this.dataPlatformVersion = dataPlatformVersion;
    }

    public String getPatchLevel() {
        return patchLevel;
    }

    public void setPatchLevel(String patchLevel) {
        this.patchLevel = patchLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SoftwareServerCapability{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", dataPlatformType='" + dataPlatformType + '\'' +
                ", dataPlatformVersion='" + dataPlatformVersion + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
