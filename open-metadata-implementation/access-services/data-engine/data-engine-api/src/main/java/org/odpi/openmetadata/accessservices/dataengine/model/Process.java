/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Process.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Process extends Asset {
    private String name;
    private String formula;
    private String implementationLanguage;
    private List<PortImplementation> portImplementations;
    private List<PortAlias> portAliases;
    private List<LineageMapping> lineageMappings;
    private Collection collection;
    private UpdateSemantic updateSemantic;
    private List<ParentProcess> parentProcesses;

    /**
     * Instantiates a new Process.
     */
    public Process() {
        super();
    }

    /**
     * Gets the process name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets up the process name.
     *
     * @param name string name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the description of the processing performed by this process.
     *
     * @return string description
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Set up the the description of the processing performed by this process.
     *
     * @param formula string description
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * Return the name of the programming language that this process is implemented in.
     *
     * @return string name
     */
    public String getImplementationLanguage() {
        return implementationLanguage;
    }

    /**
     * Set up the name of the programming language that this process is implemented in.
     *
     * @param implementationLanguage string name
     */
    public void setImplementationLanguage(String implementationLanguage) {
        this.implementationLanguage = implementationLanguage;
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
     *  Retrieves the collection to which the process belongs
     * */
    public Collection getCollection() {
        return collection;
    }


    /**
     * Sets the collection.
     *
     * @param collection the collection to which the process belongs
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Process process = (Process) o;
        return Objects.equals(name, process.name) &&
                Objects.equals(formula, process.formula) &&
                Objects.equals(implementationLanguage, process.implementationLanguage) &&
                Objects.equals(portImplementations, process.portImplementations) &&
                Objects.equals(portAliases, process.portAliases) &&
                Objects.equals(lineageMappings, process.lineageMappings) &&
                Objects.equals(collection, process.collection) &&
                updateSemantic == process.updateSemantic &&
                Objects.equals(parentProcesses, process.parentProcesses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, formula, implementationLanguage, portImplementations, portAliases, lineageMappings,
                collection, updateSemantic, parentProcesses);
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", implementationLanguage='" + implementationLanguage + '\'' +
                ", portImplementations=" + portImplementations +
                ", portAliases=" + portAliases +
                ", lineageMappings=" + lineageMappings +
                ", collection=" + collection +
                ", updateSemantic=" + updateSemantic +
                ", parentProcesses=" + parentProcesses +
                '}';
    }
}