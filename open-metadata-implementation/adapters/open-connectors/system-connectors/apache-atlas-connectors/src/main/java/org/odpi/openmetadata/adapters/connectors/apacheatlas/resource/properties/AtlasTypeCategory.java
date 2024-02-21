/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTypeCategory describes the different categories of type definition supported by Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasTypeCategory
{
    /**
     * An enumeration type (EnumDef).
     */
    ENUM,

    /**
     * A structure - list of attribute types (StructDef)
     */
    STRUCT,

    /**
     * A tag with a list or map of attributes (ClassificationDef)
     */
    CLASSIFICATION,

    /**
     * An entity (EntityDef)
     */
    ENTITY,

    /**
     * A relationship between two entities (RelationshipDef)
     */
    RELATIONSHIP,

    /**
     * A collection of attributes to provide context to an entity.
     */
    BUSINESS_METADATA
}
