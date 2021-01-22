/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Process {
    private static final long serialVersionUID = 1L;

    private String name;
    private String formula;
    private List<PortImplementation> portImplementations;
    private List<PortAlias> portAliases;
    private List<LineageMapping> lineageMappings;
    private UpdateSemantic updateSemantic;
    private List<ParentProcess> parentProcesses;
    private String displayName;
    private String description;
    private String owner;
    private int ownerType;
    private List<String> zoneMembership;
    private Map<String, String> origin;
    private String typeGUID;
    private String typeName;
    private String GUID;
    private String qualifiedName;
    private Map<String, String> additionalProperties;
    private Map<String, Object> extendedProperties;

    public Process() {
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

    public List<ParentProcess> getParentProcesses() {
        return parentProcesses;
    }

    public void setParentProcesses(List<ParentProcess> parentProcesses) {
        this.parentProcesses = parentProcesses;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    public Map<String, String> getOrigin() {
        return origin;
    }

    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    public String getTypeGUID() {
        return typeGUID;
    }

    public void setTypeGUID(String typeGUID) {
        this.typeGUID = typeGUID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(Map<String, Object> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", portImplementations=" + portImplementations +
                ", portAliases=" + portAliases +
                ", lineageMappings=" + lineageMappings +
                ", updateSemantic=" + updateSemantic +
                ", parentProcesses=" + parentProcesses +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", origin=" + origin +
                ", typeGUID='" + typeGUID + '\'' +
                ", typeName='" + typeName + '\'' +
                ", guid='" + GUID + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Process process = (Process) o;

        return Objects.equals(name, process.name) &&
                Objects.equals(formula, process.formula) &&
                Objects.equals(portImplementations, process.portImplementations) &&
                Objects.equals(portAliases, process.portAliases) &&
                Objects.equals(lineageMappings, process.lineageMappings) &&
                updateSemantic == process.updateSemantic &&
                Objects.equals(parentProcesses, process.parentProcesses) &&
                Objects.equals(displayName, process.displayName) &&
                Objects.equals(description, process.description) &&
                Objects.equals(owner, process.owner) &&
                Objects.equals(ownerType, process.ownerType) &&
                Objects.equals(zoneMembership, process.zoneMembership) &&
                Objects.equals(origin, process.origin) &&
                Objects.equals(typeGUID, process.typeGUID) &&
                Objects.equals(typeName, process.typeName) &&
                Objects.equals(GUID, process.GUID) &&
                Objects.equals(qualifiedName, process.qualifiedName) &&
                Objects.equals(additionalProperties, process.additionalProperties) &&
                Objects.equals(extendedProperties, process.extendedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, formula, portImplementations, portAliases, lineageMappings,
                updateSemantic, parentProcesses, displayName, description, owner, ownerType, zoneMembership, origin,
                typeGUID, typeName, GUID, qualifiedName, additionalProperties, extendedProperties);
    }
}