/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
public class Process extends Asset {

    /**
     * -- GETTER --
     * Gets the process name.
     * @return the name
     * -- SETTER --
     * Sets up the process name.
     * @param name string name
     */
    private String name;

    /**
     * -- GETTER --
     * Return the description of the processing performed by this process.
     * @return string description
     * -- SETTER --
     * Set up the the description of the processing performed by this process.
     * @param formula string description
     */
    private String formula;

    /**
     * -- GETTER --
     * Return the name of the programming language that this process is implemented in.
     * @return string name
     * -- SETTER --
     * Set up the name of the programming language that this process is implemented in.
     * @param implementationLanguage string name
     */
    private String implementationLanguage;

    /**
     * -- GETTER --
     * Gets port implementations.
     * @return the port implementations
     * -- SETTER --
     * Sets port implementations.
     * @param portImplementations the port implementations
     */
    private List<PortImplementation> portImplementations;

    /**
     * -- GETTER --
     * Gets port aliases.
     * @return the port aliases
     * -- SETTER --
     * Sets port aliases.
     * @param portAliases the port aliases
     */
    private List<PortAlias> portAliases;

    /**
     * -- GETTER --
     * Gets lineage mappings.
     * @return the lineage mappings
     * -- SETTER --
     * Sets lineage mappings.
     * @param lineageMappings the lineage mappings
     */
    private List<LineageMapping> lineageMappings;

    /**
     * -- GETTER --
     *  Retrieves the collection to which the process belongs
     * @return collection the collection to which it belongs
     * -- SETTER --
     * Sets the collection.
     * @param collection the collection to which the process belongs
     */
    private Collection collection;

    /**
     * -- SETTER --
     * Sets update semantic.
     * @param updateSemantic the update semantic
     */
    private UpdateSemantic updateSemantic;

    /**
     * -- GETTER --
     * Gets parent processes.
     * @return the parent processes
     * -- SETTER --
     * Sets parent processes.
     * @param parentProcesses the parent processes
     */
    private List<ParentProcess> parentProcesses;

    /**
     * Gets update semantic.
     * @return the update semantic
     */
    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }
        return updateSemantic;
    }

}