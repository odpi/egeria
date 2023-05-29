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

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProcessContainmentType describes the type of containment that exists between two processes.
 * <ul>
 * <li>OWNED - The parent process owns the child process in the relationship</li>
 * <li>APPEND - The child process is simply used by the parent</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
public enum ProcessContainmentType {
    /**
     * The parent process owns the child process in the relationship, such that if the parent is removed the child should also be removed.
     * A child can have at most one such parent
     */
    OWNED(0, "OWNED", "The parent process owns the child process in the relationship, such that if the parent is removed the child should also be " +
            "removed. A child can have at most one such parent."),
    /**
     * The child process is simply used by the parent. A child process can have many such relationships to parents
     */
    APPEND(1, "APPEND", "The child process is simply used by the parent. A child process can have many such relationships to parents."),
    /**
     * None of the above
     */
    OTHER(99, "OTHER", "None of the above.");

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
    ProcessContainmentType(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }

}

