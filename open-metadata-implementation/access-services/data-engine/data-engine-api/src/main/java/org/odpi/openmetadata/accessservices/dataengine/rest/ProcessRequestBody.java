/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessRequestBody extends DataEngineOMASAPIRequestBody {
    private String name;
    private String description;
    private String latestChange;
    private List<String> zoneMembership;
    private String displayName;
    private String owner;
    private OwnerType ownerType;
    private String formula;
    private List<String> ports;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLatestChange() {
        return latestChange;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latestChange='" + latestChange + '\'' +
                ", zoneMembership=" + zoneMembership +
                ", displayName='" + displayName + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", formula='" + formula + '\'' +
                ", ports=" + ports +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(latestChange, that.latestChange) &&
                Objects.equals(zoneMembership, that.zoneMembership) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(owner, that.owner) &&
                ownerType == that.ownerType &&
                Objects.equals(formula, that.formula) &&
                Objects.equals(ports, that.ports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, latestChange, zoneMembership, displayName, owner, ownerType, formula, ports);
    }
}