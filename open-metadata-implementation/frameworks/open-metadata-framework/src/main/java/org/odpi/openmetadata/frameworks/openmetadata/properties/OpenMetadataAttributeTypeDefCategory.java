/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The OpenMetadataAttributeTypeDefCategory defines the list of valid types of an attribute (property) for an open metadata instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataAttributeTypeDefCategory
{
    /**
     * Uninitialized OpenMetadataAttributeTypeDef object.
     */
    UNKNOWN_DEF        (0, "Unknown",           "Uninitialized OpenMetadataAttributeTypeDef object."),

    /**
     * A primitive type.
     */
    PRIMITIVE          (1, "Primitive",         "A primitive type."),

    /**
     * A collection object.
     */
    COLLECTION         (2, "Collection",        "A collection object."),

    /**
     * A pre-defined list of valid values.
     */
    ENUM_DEF           (4, "OpenMetadataEnumDef",           "A pre-defined list of valid values.");

    private final int            typeCode;
    private final String         typeName;
    private final String         typeDescription;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param typeCode ordinal
     * @param typeName short name
     * @param typeDescription longer explanation
     */
    OpenMetadataAttributeTypeDefCategory(int     typeCode, String   typeName, String   typeDescription)
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
     * @return int type code
     */
    public int getOrdinal()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
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
        return "OpenMetadataAttributeTypeDefCategory{" +
                "typeCode=" + typeCode +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }
}
