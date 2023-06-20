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
 * PortType defines the different port types used for open metadata. It is used in a port implementation
 * definition.
 * <ul>
 * <li>INPUT_PORT - Input Port.</li>
 * <li>OUTPUT_PORT - Output Port.</li>
 * <li>INOUT_PORT - Input Output Port.</li>
 * <li>OUTIN_PORT - Output Input Port.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
public enum PortType implements Serializable {
    /**
     * Input Port
     */
    INPUT_PORT(0, "INPUT_PORT", "Input Port."),
    /**
     * Output Port
     */
    OUTPUT_PORT(1, "OUTPUT_PORT", "Output Port."),
    /**
     * IInput Output Port
     */
    INOUT_PORT(2, "INOUT_PORT", "Input Output Port."),
    /**
     * Output Input Port
     */
    OUTIN_PORT(3, "OUTIN_PORT", "Output Input Port."),
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
     * @return int ordinal
     */
    private final int ordinal;

    /**
     * The default name of the instance provenance type
     * -- GETTER --
     * Return the default name of the instance provenance type.
     * @return String name
     */
    private final String name;

    /**
     * The default description of the instance provenance type
     * -- GETTER --
     * Return the default description of the instance provenance type.
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
    PortType(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }

}