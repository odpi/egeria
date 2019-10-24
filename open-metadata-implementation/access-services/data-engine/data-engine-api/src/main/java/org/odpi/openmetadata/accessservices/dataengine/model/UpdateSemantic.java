/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public enum UpdateSemantic implements Serializable {
    REPLACE(0, "REPLACE", "Replace with new entities"),
    APPEND(1, "APPEND", "Append new entities");

    private static final long serialVersionUID = 1L;

    private int ordinal;
    private String name;
    private String description;


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


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString() {
        return "UpdateSemantic{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }}

