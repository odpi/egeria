/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TypeDefCategory defines the list of valid types of TypeDef for open metadata instances.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TypeDefCategory implements Serializable
{
    /**
     * Unknown - Uninitialized TypeDef object.
     */
    UNKNOWN_DEF        (0, "Unknown",           "Uninitialized TypeDef object."),

    /**
     * ClassificationDef - A description of a specific characteristic or grouping for entities.
     */
    CLASSIFICATION_DEF (5, "ClassificationDef", "A description of a specific characteristic or grouping for entities."),

    /**
     * EntityDef - An object or concept of interest.
     */
    ENTITY_DEF         (6, "EntityDef",         "An object or concept of interest."),

    /**
     * RelationshipDef - A link between two entities.
     */
    RELATIONSHIP_DEF   (8, "RelationshipDef",   "A link between two entities.");

    private static final long serialVersionUID = 1L;

    private final int            typeCode;
    private final String         typeName;
    private final String         typeDescription;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param typeCode ordinal
     * @param typeName short description
     * @param typeDescription longer explanation
     */
    TypeDefCategory(int     typeCode, String   typeName, String   typeDescription)
    {
        /*
         * Save the values supplied
         */
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int  type code
     */
    public int getOrdinal()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String  default name
     */
    public String getName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String  default description
     */
    public String getDescription()
    {
        return typeDescription;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "TypeDefCategory{" +
                "typeCode=" + typeCode +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }
}
