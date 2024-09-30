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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CommunityMembershipType specifies the role of a member in the community.
 * <ul>
 *     <li>CONTRIBUTOR - Individual is able to be a contributing member of the community.  This is the default.</li>
 *     <li>ADMINISTER - Individual is able to administer the community.</li>
 *     <li>LEADER - Individual sets the direction of the community.</li>
 *     <li>OBSERVER - Individual is receiving notifications about the community.</li>
 *     <li>OTHER - Another meaning.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityMembershipType implements OpenMetadataEnum
{
    CONTRIBUTOR   (0, "959cae9b-218f-4254-8a79-721d568372bd",  "Contributor",   "Individual is able to be a contributing member of the community.", true),
    ADMINISTRATOR (1, "35201771-3ca2-4b7a-9dd1-77aa0224abe8",  "Administrator", "Individual is able to administer the community.", false),
    LEADER        (2, "d4f5dfdc-39d6-47c1-8dfb-077b5f7244d7",  "Leader",        "Individual sets the direction of the community.", false),
    OBSERVER      (3, "c1bd838c-f5be-4464-aee9-bcf0ff07ec32",  "Observer",      "Individual is receiving notifications about the community.", false),
    OTHER         (99, "1c9fd81c-d22a-4aca-b2f3-469088ff7887", "Other",         "Another meaning.", false);

    private static final String ENUM_TYPE_GUID  = "b0ef45bf-d12b-4b6f-add6-59c14648d750";
    private static final String ENUM_TYPE_NAME  = "CommunityMembershipType";

    private static final String ENUM_DESCRIPTION = "Type of membership to a community.";
    private static final String ENUM_DESCRIPTION_GUID = "69eeac39-f405-4196-97af-d25910fb93b6";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0140_COMMUNITIES;

    private final String         descriptionGUID;
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
    CommunityMembershipType(int     ordinal,
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
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */@Override
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
                                                OpenMetadataProperty.SORT_ORDER.name,
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
                                           OpenMetadataProperty.SORT_ORDER.name,
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
        return "CommunityMembershipType : " + name;
    }
}
