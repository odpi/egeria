/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * OrderBy specifies the sequencing to use in a specific collection.
 * <ul>
 *     <li>QUALIFIED_NAME - Order the collection by the qualified name of the members in the collection.</li>
 *     <li>NAME - Order the collection by the name of the members in the collection.</li>
 *     <li>OWNER - Order the collection by the owner of the members in the collection.</li>
 *     <li>DATE_ADDED - Order the collection by the date that the members were added to the collection.</li>
 *     <li>DATE_UPDATED - Order the collection by the date that the members were updated in the collection.</li>
 *     <li>DATE_CREATED - Order the collection by the date that the members were created in the collection.</li>
 *     <li>OTHER - Order the collection by another value.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OrderBy
{
    /**
     * Order the collection by the names of the members in the collection.
     */
    NAME           (0,  "9787a4cf-d8d7-411c-8865-0adbb72bec16",  "Name",           "Order the collection by the names of the members in the collection.", false),

    /**
     * Order the collection by the owners of the members in the collection.
     */
    OWNER          (1,  "e37eda88-a982-4789-8cdc-f40c3920b72a",  "Owner",          "Order the collection by the owners of the members in the collection.", false),

    /**
     * Order the collection by the date that the members were added to the collection.
     */
    DATE_ADDED     (2,  "f4ee561c-f172-4649-919f-74099bf30bcc",  "DateAdded",     "Order the collection by the date that the members were added to the collection.", false),

    /**
     * Order the collection by the date that the members were updated in the collection.
     */
    DATE_UPDATED   (3,  "2230266c-e2f1-4cc2-960b-91dbe715683c",  "DateUpdated",   "Order the collection by the date that the members were updated in the collection.", false),

    /**
     * Order the collection by the date that the members were created in the collection.
     */
    DATE_CREATED   (4,  "e1e07c2f-2210-44cc-8901-45dbb35daa0d",  "DateCreated",   "Order the collection by the date that the members were created in the collection.", false),

    /**
     * Order the collection by another value.
     */
    OTHER          (99, "34cd86b5-9ae1-4a33-a7d9-0689493e65b3", "Other",          "Order the collection by another value.", false);


    private static final String ENUM_TYPE_GUID  = "1d412439-4272-4a7e-a940-1065f889fc56";
    private static final String ENUM_TYPE_NAME  = "OrderBy";

    private static final String ENUM_DESCRIPTION = "Defines the sequencing for a collection.";
    private static final String ENUM_DESCRIPTION_GUID = "421bd0cd-1fb2-46b6-8943-b74dd6a36b9f";


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
    OrderBy(int     ordinal,
            String  descriptionGUID,
            String  name,
            String  description,
            boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.descriptionGUID = descriptionGUID;
        this.name            = name;
        this.description     = description;
        this.isDefault       = isDefault;
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
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    public boolean isDefault()
    {
        return isDefault;
    }

    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.COLLECTION_ORDER.name,
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
        return constructValidValueCategory(ENUM_TYPE_NAME,
                                           OpenMetadataProperty.COLLECTION_ORDER.name,
                                           null);
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OrderBy : " + name;
    }
}
