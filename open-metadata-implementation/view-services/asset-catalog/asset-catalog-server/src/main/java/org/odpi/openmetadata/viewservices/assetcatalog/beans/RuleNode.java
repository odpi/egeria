/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.Map;

public class RuleNode
{

    private String id;
    private String label;
    private String group;
    private Map<String, String> properties;
    private Integer level = 0;
    private String qualifiedName;

    public RuleNode(String id, String label)
    {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String toString() {
        return "RuleNode{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", group='" + group + '\'' +
                ", properties=" + properties +
                ", level=" + level +
                ", qualifiedName=" + qualifiedName +
                '}';
    }
}
