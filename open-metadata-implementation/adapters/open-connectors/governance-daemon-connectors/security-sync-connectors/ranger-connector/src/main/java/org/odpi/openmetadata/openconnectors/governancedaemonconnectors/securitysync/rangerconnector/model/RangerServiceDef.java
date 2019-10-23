/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Map;

public class RangerServiceDef extends RangerBaseObject {

    private String name;
    private String implClass;
    private String label;
    private String description;
    private String rbKeyLabel;
    private String rbKeyDescription;
    private Map<String, String> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRbKeyLabel() {
        return rbKeyLabel;
    }

    public void setRbKeyLabel(String rbKeyLabel) {
        this.rbKeyLabel = rbKeyLabel;
    }

    public String getRbKeyDescription() {
        return rbKeyDescription;
    }

    public void setRbKeyDescription(String rbKeyDescription) {
        this.rbKeyDescription = rbKeyDescription;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}