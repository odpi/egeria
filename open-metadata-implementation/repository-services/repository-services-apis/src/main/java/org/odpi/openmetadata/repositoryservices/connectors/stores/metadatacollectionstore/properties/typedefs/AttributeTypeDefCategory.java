/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AttributeTypeDefCategory defines the list of valid types of a attribute (property) for an open metadata instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AttributeTypeDefCategory implements Serializable
{
    UNKNOWN_DEF        (0, "<Unknown>",         "Uninitialized AttributeTypeDef object."),
    PRIMITIVE          (1, "Primitive",         "A primitive type."),
    COLLECTION         (2, "Collection",        "A collection object."),
    ENUM_DEF           (4, "EnumDef",           "A pre-defined list of valid values.");

    private static final long serialVersionUID = 1L;

    private int            typeCode;
    private String         typeName;
    private String         typeDescription;


    /**
     * Constructor to set up a single instances of the enum.
     */
    AttributeTypeDefCategory(int     typeCode, String   typeName, String   typeDescription)
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
        return "AttributeTypeDefCategory{" +
                "typeCode=" + typeCode +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }
}
