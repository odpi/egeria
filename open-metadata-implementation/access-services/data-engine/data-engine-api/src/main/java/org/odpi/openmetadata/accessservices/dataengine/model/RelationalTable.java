/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationalTable extends Referenceable {
    private static final long serialVersionUID = 1L;
    private String displayName;
    private String type;
    private List<String> aliases;
    private boolean isDeprecated;
    private String description;

    private List<RelationalColumn> columns;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RelationalColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<RelationalColumn> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationalTable that = (RelationalTable) o;
        return isDeprecated == that.isDeprecated &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(type, that.type) &&
                Objects.equals(aliases, that.aliases) &&
                Objects.equals(description, that.description) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, type, aliases, isDeprecated, description, columns);
    }

    @Override
    public String toString() {
        return "RelationalTable{" +
                ", displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                ", aliases=" + aliases +
                ", isDeprecated=" + isDeprecated +
                ", description='" + description + '\'' +
                ", columns=" + columns +
                '}';
    }
}
