/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Process.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Process implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String formula;
    private List<PortImplementation> portImplementations;
    private List<PortAlias> portAliases;
    private List<LineageMapping> lineageMappings;
    private TransformationProject transformationProject;
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

    /**
     * Instantiates a new Process.
     */
    public Process() {
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets formula.
     *
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Sets formula.
     *
     * @param formula the formula
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * Gets port implementations.
     *
     * @return the port implementations
     */
    public List<PortImplementation> getPortImplementations() {
        return portImplementations;
    }

    /**
     * Sets port implementations.
     *
     * @param portImplementations the port implementations
     */
    public void setPortImplementations(List<PortImplementation> portImplementations) {
        this.portImplementations = portImplementations;
    }

    /**
     * Gets port aliases.
     *
     * @return the port aliases
     */
    public List<PortAlias> getPortAliases() {
        return portAliases;
    }

    /**
     * Sets port aliases.
     *
     * @param portAliases the port aliases
     */
    public void setPortAliases(List<PortAlias> portAliases) {
        this.portAliases = portAliases;
    }

    /**
     * Gets lineage mappings.
     *
     * @return the lineage mappings
     */
    public List<LineageMapping> getLineageMappings() {
        return lineageMappings;
    }

    /**
     * Sets lineage mappings.
     *
     * @param lineageMappings the lineage mappings
     */
    public void setLineageMappings(List<LineageMapping> lineageMappings) {
        this.lineageMappings = lineageMappings;
    }

    /**
     * Gets update semantic.
     *
     * @return the update semantic
     */
    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }

        return updateSemantic;
    }

    /**
     * Sets update semantic.
     *
     * @param updateSemantic the update semantic
     */
    public void setUpdateSemantic(UpdateSemantic updateSemantic) {
        this.updateSemantic = updateSemantic;
    }

    /**
     * Gets parent processes.
     *
     * @return the parent processes
     */
    public List<ParentProcess> getParentProcesses() {
        return parentProcesses;
    }

    /**
     * Sets parent processes.
     *
     * @param parentProcesses the parent processes
     */
    public void setParentProcesses(List<ParentProcess> parentProcesses) {
        this.parentProcesses = parentProcesses;
    }

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
     * Gets owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets owner type.
     *
     * @return the owner type
     */
    public int getOwnerType() {
        return ownerType;
    }

    /**
     * Sets owner type.
     *
     * @param ownerType the owner type
     */
    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * Gets zone membership.
     *
     * @return the zone membership
     */
    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    /**
     * Sets zone membership.
     *
     * @param zoneMembership the zone membership
     */
    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public Map<String, String> getOrigin() {
        return origin;
    }

    /**
     * Sets origin.
     *
     * @param origin the origin
     */
    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    /**
     * Gets type guid.
     *
     * @return the type guid
     */
    public String getTypeGUID() {
        return typeGUID;
    }

    /**
     * Sets type guid.
     *
     * @param typeGUID the type guid
     */
    public void setTypeGUID(String typeGUID) {
        this.typeGUID = typeGUID;
    }

    /**
     * Gets type name.
     *
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets type name.
     *
     * @param typeName the type name
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets guid.
     *
     * @return the guid
     */
    public String getGUID() {
        return GUID;
    }

    /**
     * Sets guid.
     *
     * @param GUID the guid
     */
    public void setGUID(String GUID) {
        this.GUID = GUID;
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
     * Gets additional properties.
     *
     * @return the additional properties
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets additional properties.
     *
     * @param additionalProperties the additional properties
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Gets extended properties.
     *
     * @return the extended properties
     */
    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    /**
     * Sets extended properties.
     *
     * @param extendedProperties the extended properties
     */
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
                ", transformationProject='" + transformationProject + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                '}';
    }

    public TransformationProject getTransformationProject() {
        return transformationProject;
    }

    public void setTransformationProject(TransformationProject transformationProject) {
        this.transformationProject = transformationProject;
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
                Objects.equals(transformationProject, process.transformationProject) &&
                Objects.equals(extendedProperties, process.extendedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, formula, portImplementations, portAliases, lineageMappings,
                updateSemantic, parentProcesses, displayName, description, owner, ownerType, zoneMembership, origin,
                typeGUID, typeName, GUID, qualifiedName, transformationProject,
                additionalProperties, extendedProperties);
    }
}