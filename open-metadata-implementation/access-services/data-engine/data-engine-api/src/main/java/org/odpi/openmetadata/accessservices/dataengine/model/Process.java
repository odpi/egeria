/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Process extends Asset {
    private static final long serialVersionUID = 1L;

    private String name;
    private String formula;
    private List<PortImplementation> portImplementations;
    private List<PortAlias> portAliases;
    private List<LineageMapping> lineageMappings;
    private UpdateSemantic updateSemantic;

    public Process() {
    }

    public Process(Asset asset) {
        super(asset);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<PortAlias> getPortAliases() {
        return portAliases;
    }

    public void setPortAliases(List<PortAlias> portAliases) {
        this.portAliases = portAliases;
    }

    public List<LineageMapping> getLineageMappings() {
        return lineageMappings;
    }

    public void setLineageMappings(List<LineageMapping> lineageMappings) {
        this.lineageMappings = lineageMappings;
    }

    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }

        return updateSemantic;
    }

    public void setUpdateSemantic(UpdateSemantic updateSemantic) {
        this.updateSemantic = updateSemantic;
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
                ", lineageMappings=" + lineageMappings +
                ", updateSemantic=" + updateSemantic +
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
                Objects.equals(lineageMappings, process.lineageMappings) &&
                updateSemantic == process.updateSemantic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, name, description, latestChange, zoneMembership, displayName, owner,
                ownerType, formula, portImplementations, portAliases, lineageMappings, updateSemantic);
    }
}

