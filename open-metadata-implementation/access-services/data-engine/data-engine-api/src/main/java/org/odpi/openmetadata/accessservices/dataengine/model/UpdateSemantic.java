/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateSemantic defines the different types of update for a process
 * <ul>
 * <li>REPLACE - entities found is in the process payload will be updated or added, existing entities not included will
 * be removed</li>
 * <li>APPEND - entities found is in the process payload will be updated or added, existing entities not included will
 * be ignored </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
public enum UpdateSemantic implements Serializable {
    /**
     * Replace with new entities
     */
    REPLACE(0, "REPLACE", "Replace with new entities"),
    /**
     * Append new entities
     */
    APPEND(1, "APPEND", "Append new entities");

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * The numeric representation of the instance provenance type
     * -- GETTER --
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    private final int ordinal;

    /**
     * The default name of the instance provenance type
     * -- GETTER --
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    private final String name;

    /**
     * The default description of the instance provenance type
     * -- GETTER --
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    private final String description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal     numerical representation of the enumeration
     * @param name        default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    UpdateSemantic(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }

}