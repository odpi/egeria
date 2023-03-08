/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import java.io.Serializable;

public class SoftwareServerCapability implements Serializable {

    private static final long serialVersionUID = 1L;

    private String GUID;
    private String openTypeGUID;
    private String name;
    private String description;
    private String type;
    private String version;
    private String patchLevel;
    private String source;

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getOpenTypeGUID() {
        return openTypeGUID;
    }

    public void setOpenTypeGUID(String openTypeGUID) {
        this.openTypeGUID = openTypeGUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
