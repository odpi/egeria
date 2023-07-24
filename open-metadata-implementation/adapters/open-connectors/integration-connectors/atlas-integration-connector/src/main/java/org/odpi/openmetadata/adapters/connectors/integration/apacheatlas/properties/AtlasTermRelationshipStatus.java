/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTermRelationshipStatus describes the status of a relationship between terms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasTermRelationshipStatus
{
    /**
     * The relationship has been proposed.
     */
    DRAFT(0),

    /**
     * The relationship is in use.
     */
    ACTIVE(1),

    /**
     * The relationship has been deprecated and should not be used.
     */
    DEPRECATED(2),

    /**
     * The relationship must not be used.
     */
    OBSOLETE(3),

    /**
     * Another relationship status.
     */
    OTHER(99);

    private final int value;


    /**
     * Constructor.
     *
     * @param value integer value for the status.
     */
    AtlasTermRelationshipStatus(final int value)
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
        return "AtlasTermRelationshipStatus{" +
                       "value=" + value +
                       '}';
    }
}
