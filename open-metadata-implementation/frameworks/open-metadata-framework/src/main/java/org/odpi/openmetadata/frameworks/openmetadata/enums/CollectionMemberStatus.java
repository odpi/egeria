/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CollectionMemberStatus specifies the status of the member in a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CollectionMemberStatus implements OpenMetadataEnum
{
    /**
     * The status of the member is not known or not specified. This is the default value.
     */
    UNKNOWN     (0,  "c278c32d-52c3-44b3-aa3c-5a755a454af9",  "Unknown",    "The status of the member is not known or not specified. This is the default value.", true),

    /**
     * The member was added by an automated process.
     */
    DISCOVERED  (1,  "eca11907-5088-4536-a9ca-d0d4e82b4040",  "Discovered", "The member was added by an automated process.", false),

    /**
     * The member was proposed by a consumer.
     */
    PROPOSED    (2,  "24b1c123-21b9-4754-b95b-4641d02672b1",  "Proposed",   "The member was proposed by a consumer.", false),

    /**
     * The member was imported from another system.
     */
    IMPORTED    (3,  "76410870-bf5c-4594-84ea-c4ce809f8ffd",  "Imported",   "The member was imported from another system.", false),

    /**
     * The member has been validated by a custodian/steward/approver and can be trusted.
     */
    VALIDATED   (4,  "e6351cfb-d48d-413b-97db-89be80c2fcb2",  "Validated",  "The member has been validated by a custodian/steward/approver and can be trusted.", false),

    /**
     * The membership has been deprecated. Consider stopping using it.
     */
    DEPRECATED  (5,  "9736b9fb-250e-48ed-9b9a-2ed6e0e76743",  "Deprecated", "The membership has been deprecated. Consider stopping using it.", false),

    /**
     * The membership is obsolete and should not be used.
     */
    OBSOLETE    (6,  "1e1a8f3a-2962-4b1d-8cf5-255a4569ea27",  "Obsolete",   "The membership is obsolete and should not be used.", false),

    /**
     * The membership has a different status not covered by the open metadata types.
     */
    OTHER       (99, "d1867eca-f908-4c79-b548-cb86482a8e2a", "Other",      "The membership has a different status not covered by the open metadata types.", false);


    private static final String ENUM_TYPE_GUID  = "a3bdb2ac-c28e-4e5a-8ab7-76aa01038832";
    private static final String ENUM_TYPE_NAME  = "MembershipStatus";
    private static final String ENUM_DESCRIPTION = "Defines the provenance and confidence that a member belongs in a collection.";
    private static final String ENUM_DESCRIPTION_GUID = "bd00ab69-de86-4461-8062-6aceabf8ef99";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0021_COLLECTIONS;


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
    CollectionMemberStatus(int     ordinal,
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
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
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
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.MEMBERSHIP_STATUS.name,
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
        return constructValidValueNamespace(ENUM_TYPE_NAME,
                                            OpenMetadataProperty.MEMBERSHIP_STATUS.name,
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
        return "CollectionMemberStatus{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';

    }
}
