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
 * DeleteSemantic defines the different types of delete for an entity
 * <ul>
 * <li>HARD - Hard-delete strategy. Process and related metadata associated is deleted by purging it from the metadata repository. Deletion
 * happens cascading down to data flows.</li>
 * <li>SOFT - Soft-delete strategy. Process (and related metadata) will be not removed physically but flagged as 'inactive' or temporary deleted
 * from repository (deleteEntity); Process (and related metadata) can be restored in case it is needed (restoreEntity). </li>
 * <li> MEMENTO - Using Memento classification. Similarly to soft delete, process asset is classified as memento asset using memento
 * classification.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public enum DeleteSemantic implements Serializable {
    SOFT(0, "SOFT", "Soft delete"),
    HARD(1, "HARD", "Hard delete"),
    MEMENTO(1, "MEMENTO", "Memento");

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * Return the numeric representation of the delete semantic
     * -- GETTER --
     * Return the numeric representation of the delete semantic
     *
     * @return String - numeric representation of the delete semantic
     */
    private int ordinal;

    /**
     * Return the name of the delete semantic
     * -- GETTER --
     * Return the name of the delete semantic
     *
     * @return String - name of the delete semantic
     */
    private String name;

    /**
     * Return the description of the delete semantic
     * -- GETTER --
     * Return the description of the delete semantic
     *
     * @return String - description of the delete semantic
     */
    private String description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal     numerical representation of the enumeration
     * @param name        default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    DeleteSemantic(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }
}

