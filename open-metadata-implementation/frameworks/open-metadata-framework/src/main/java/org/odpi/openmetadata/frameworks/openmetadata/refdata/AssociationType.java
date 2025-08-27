/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * AssociationType defines the type of association between two valid values, such as 'containment', 'aggregation' or 'inheritance.'.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssociationType
{
    CONTAINMENT   (0,  "404f75cf-0c0f-4ed2-85cd-6bc7c29efd5a", "containment","The end2 element is a part of the end1 element.", false),
    AGGREGATION   (1,  "be9ba3d7-b058-42b7-876f-65d90a19b1ba", "aggregation","The end1 element holds a link to the end2 element.", false),
    INHERITANCE   (2,  "c80d0e84-6443-4430-9b44-512518606a86", "inheritance","The end1 element is a subtype of the end2 element.", false),
    DEPENDENCY    (3,  "6b8244e4-ddf8-45a6-a3c8-08d4ed4cdac1", "dependency", "The end1 element is dependent on the end2 element.", false),
    USES          (4,  "1cc2fd5f-a882-44dc-8ee8-0dec5721bfce", "uses",       "The end1 element uses the end2 element.", false),

    ;


    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    AssociationType(int     ordinal,
                    String  descriptionGUID,
                    String  name,
                    String  description,
                    boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
    }



    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }



    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueNamespace(null,
                                            OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                            null);
    }



    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
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
        return "AssociationType : " + name;
    }
}