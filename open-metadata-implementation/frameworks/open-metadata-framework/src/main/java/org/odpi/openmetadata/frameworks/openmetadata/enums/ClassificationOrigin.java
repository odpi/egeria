/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
 * <br><br>
 * Note: the repository services have no mechanism to automatically propagate classifications.  This is set up by higher
 * level services working with knowledge of the specific instance types of metadata and their significance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ClassificationOrigin implements OpenMetadataEnum
{
    /**
     * The classification is explicitly assigned to the entity.
     */
    ASSIGNED       (0, "Assigned",   "The classification is explicitly assigned to the entity", "2e8942ff-aae6-4048-a481-aba41b4e839c", true),

    /**
     * The classification has propagated along a relationship to this entity.
     */
    PROPAGATED     (1, "Propagated", "The classification has propagated along a relationship to this entity", "5d2a38f7-4381-46e1-9f7b-ad38446c7a29" , false);

    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  descriptionGUID;
    private final boolean isDefault;


    /**
     * Default constructor for the classification origin.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    ClassificationOrigin(int  ordinal,
                         String name,
                         String description,
                         String descriptionGUID,
                         boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
        this.isDefault = isDefault;
    }


    /**
     * Return the numeric representation of the classification origin.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the classification origin.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the default description of the classification origin.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return isDefault;
    }


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
