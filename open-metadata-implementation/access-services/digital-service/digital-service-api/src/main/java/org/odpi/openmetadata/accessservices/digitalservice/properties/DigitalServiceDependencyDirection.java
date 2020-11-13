/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceDependencyDirection documents the direction of the digital service dependency graph.
 * <ul>
 *     <li>
 *         DEPENDENT_SERVICES - The digital services that call this digital service.
 *     </li>
 *     <li>
 *         DEPENDED_UPON_SERVICES - The digital services that this digital service calls.
 *     </li>
 *     <li>
 *         BOTH_DIRECTIONS - Both the dependent and depended on digital services.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DigitalServiceDependencyDirection implements Serializable
{
    DEPENDENT_SERVICES      (0,  "Dependent",        "The digital services that call this digital service."),
    DEPENDED_UPON_SERVICES  (1,  "Depended Upon",    "The digital services that this digital service calls."),
    BOTH_DIRECTIONS         (2,  "Both Directions",  "Both the dependent and depended on digital services.");

    private static final long serialVersionUID = 1L;
    private int            ordinal;
    private String         name;
    private String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    DigitalServiceDependencyDirection(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalServiceDependencyDirection{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
