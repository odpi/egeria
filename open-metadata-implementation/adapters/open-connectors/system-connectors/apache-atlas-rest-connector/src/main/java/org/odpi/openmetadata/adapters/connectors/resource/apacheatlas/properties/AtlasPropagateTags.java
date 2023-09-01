/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasPropagateTags defines whether tags (Atlas classifications) are propagated along a relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasPropagateTags
{
    /**
     * Do not propagate tags through relationship
     */
    NONE,

    /**
     * Propagate tags found on the entity at end1 to the entity found at end2.
     */
    ONE_TO_TWO,

    /**
     * Propagate tags found on the entity at end2 to the entity found at end1.
     */
    TWO_TO_ONE,

    /**
     * Propagate tags in both directions.
     */
    BOTH
}
