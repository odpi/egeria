/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRelationshipCategory is used in an OpenMetadataRelationshipDef to indicate how many relationships of this type
 * can be connected between two element instances:
 * <ul>
 *     <li>
 *         UNI_LINK: Most common setting.  Only one relationship of this type can be connected between two element instances in a specific direction. If a second relationship is connected between them in a specific direction, the properties of the original relationship are simply updated.
 *     </li>
 *     <li>
 *         REVERSIBLE: Relationship can be created in both directions between two elements.  However, only one relationship will be stored and later "creates" will simply update the properties of the existing relationship.
 *     </li>
 *     <li>
 *         MULTI_LINK: Means that unlimited relationships of this type can be created between two elements. Updates to a relationship's properties need to use the updateRelationship call specifying the GUID of the relationship to change.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OpenMetadataRelationshipCategory
{
    /**
     * Only one relationship of a specific type is allowed between two elements in a specific direction.
     */
    UNI_LINK(0, "Uni-link", "Only one relationship of a specific type is allowed between two elements in a specific direction."),

    /**
     * At most one relationship of a specific type is allowed between two elements in either direction.
     */
    REVERSIBLE(1, "Reversible", "Only one relationship of a specific type is allowed between two elements in either direction."),

    /**
     * Multiple relationships of a specific type are allowed between two elements in either direction.
     */
    MULTI_LINK(2, "Multi-link", "Multiple relationships of a specific type are allowed between two elements in either direction.");

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal     numerical representation of the cardinality
     * @param name        default string name of the cardinality
     * @param description default string description of the cardinality
     */
    OpenMetadataRelationshipCategory(int ordinal, String name, String description)
    {
        this.ordinal     = ordinal;
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the cardinality.
     *
     * @return int ordinal
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name of the cardinality.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description of the cardinality.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationshipEndCardinality{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
