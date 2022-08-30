/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationOrigin describes the provenance of a classification attached to an entity.  Most classifications
 * are explicitly assigned to an entity.  However, it is possible for some classifications to flow along
 * relationships to other entities.  These are the propagated classifications.  Each entity can only have one
 * classification of a certain type.  A propagated classification can not override an assigned classification.
 * Classifications can only be attached to entities of specific types.  However a propagated classification can
 * flow through an entity that does not support the particular type of classification and then on to other
 * relationships attached to the entity.  The ClassificationPropagateRule in the relationship's RelationshipDef
 * defines where the classification can flow to.
 *
 * Note: the repository services have no mechanism to automatically propagate classifications.  This is set up by higher
 * level services working with knowledge of the specific instance types of metadata and their significance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ClassificationOrigin implements Serializable
{
    ASSIGNED       (0, "Assigned",   "The classification is explicitly assigned to the entity"),
    PROPAGATED     (1, "Propagated", "The classification has propagated along a relationship to this entity");

    private static final long serialVersionUID = 1L;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the classification origin.
     *
     * @param ordinal numerical representation of the classification origin
     * @param name default string name of the classification origin
     * @param description default string description of the classification origin
     */
    ClassificationOrigin(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the classification origin.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the classification origin.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the classification origin.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ClassificationOrigin{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
