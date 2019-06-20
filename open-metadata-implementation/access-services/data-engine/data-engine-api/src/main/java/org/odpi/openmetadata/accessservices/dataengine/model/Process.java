/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Process implements Serializable {
    private static final long serialVersionUID = 1L;

    private String qualifiedName;
    private String name;
    private String description;
    private String latestChange;
    private List<String> zoneMembership;
    private String displayName;
    private String owner;
    private OwnerType ownerType;
    private String formula;
    private List<PortImplementation> portImplementations;
    private List<Port> portAliases;
    private List<PortDelegation> portDelegations;
    private List<LineageMapping> lineageMappings;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
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

    public String getLatestChange() {
        return latestChange;
    }

    public void setLatestChange(String latestChange) {
        this.latestChange = latestChange;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<PortImplementation> getPortImplementations() {
        return portImplementations;
    }

    public void setPortImplementations(List<PortImplementation> portImplementations) {
        this.portImplementations = portImplementations;
    }

    public List<Port> getPortAliases() {
        return portAliases;
    }

    public void setPortAliases(List<Port> portAliases) {
        this.portAliases = portAliases;
    }

    public List<PortDelegation> getPortDelegations() {
        return portDelegations;
    }

    public void setPortDelegations(List<PortDelegation> portDelegations) {
        this.portDelegations = portDelegations;
    }

    public List<LineageMapping> getLineageMappings() {
        return lineageMappings;
    }

    public void setLineageMappings(List<LineageMapping> lineageMappings) {
        this.lineageMappings = lineageMappings;
    }

    @Override
    public String toString() {
        return "Process{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latestChange='" + latestChange + '\'' +
                ", zoneMembership=" + zoneMembership +
                ", displayName='" + displayName + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", formula='" + formula + '\'' +
                ", portImplementations=" + portImplementations +
                ", portAliases=" + portAliases +
                ", portDelegations=" + portDelegations +
                ", lineageMappings=" + lineageMappings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return Objects.equals(qualifiedName, process.qualifiedName) &&
                Objects.equals(name, process.name) &&
                Objects.equals(description, process.description) &&
                Objects.equals(latestChange, process.latestChange) &&
                Objects.equals(zoneMembership, process.zoneMembership) &&
                Objects.equals(displayName, process.displayName) &&
                Objects.equals(owner, process.owner) &&
                ownerType == process.ownerType &&
                Objects.equals(formula, process.formula) &&
                Objects.equals(portImplementations, process.portImplementations) &&
                Objects.equals(portAliases, process.portAliases) &&
                Objects.equals(portDelegations, process.portDelegations) &&
                Objects.equals(lineageMappings, process.lineageMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, name, description, latestChange, zoneMembership, displayName, owner,
                ownerType, formula, portImplementations, portAliases, portDelegations, lineageMappings);
    }
}

