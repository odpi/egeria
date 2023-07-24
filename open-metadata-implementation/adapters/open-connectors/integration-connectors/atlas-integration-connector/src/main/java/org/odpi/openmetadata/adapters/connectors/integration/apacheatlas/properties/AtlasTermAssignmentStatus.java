/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTermAssignmentStatus describes the status of a semantic assignment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasTermAssignmentStatus
{
    /**
     * The assignment has been discovered by an automated process.
     */
    DISCOVERED(0),

    /**
     * The assignment has been proposed by a consumer.
     */
    PROPOSED(1),

    /**
     * The assignment has been imported from an external system.
     */
    IMPORTED(2),

    /**
     * The assignment has been validated by a subject-matter expert,
     */
    VALIDATED(3),

    /**
     * The assignment has been deprecated and should not be used.
     */
    DEPRECATED(4),

    /**
     * The assignment must not be used.
     */
    OBSOLETE(5),

    /**
     * Another assignment status.
     */
    OTHER(6);

    private final int value;


    /**
     * Constructor.
     *
     * @param value integer value for the status.
     */
    AtlasTermAssignmentStatus(final int value)
    {
        this.value = value;
    }


    /**
     * Retrieve the value.
     *
     * @return int
     */
    public int getValue()
    {
        return value;
    }


    @Override
    public String toString()
    {
        return "AtlasTermAssignmentStatus{" +
                       "value=" + value +
                       '}';
    }
}
