/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RetentionBasis defines the retention requirements associated with a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RetentionBasis implements OpenMetadataEnum
{
    /**
     * There is no assessment of the retention requirements for this data.
     */
    UNCLASSIFIED       ("08020d82-c144-4ead-ac43-6b3921e564da", 0, "Unclassified",
                               "There is no assessment of the retention requirements for this data.", true),

    /**
     * This data is temporary. There are no formal retention requirements.
     */
    TEMPORARY          ("95df2e1b-5b50-40c8-afd2-fe39ee4ea70e", 1, "Temporary",
                               "This data is temporary. There are no formal retention requirements.", false),

    /**
     * The data is needed for the lifetime of the referenced project.
     */
    PROJECT_LIFETIME   ("aec445e7-5fa2-43ce-bd53-7b233b35bf7e", 2, "Project Lifetime",
                               "The data is needed for the lifetime of the referenced project.", false),

    /**
     * The data is needed for the lifetime of the referenced team.
     */
    TEAM_LIFETIME      ("b763e061-d647-42d9-a7d5-9d4dbdcdf868", 3, "Team Lifetime",
                               "The data is needed for the lifetime of the referenced team.", false),

    /**
     * The data is needed for the lifetime of the referenced contract.
     */
    CONTRACT_LIFETIME  ("5a99d883-ab89-41e9-8ba5-9e03646e8e0a", 4, "Contract Lifetime",
                               "The data is needed for the lifetime of the referenced contract.", false),

    /**
     * The retention period for the data is defined by the referenced regulation.
     */
    REGULATED_LIFETIME ("d085448b-2683-4666-a5b4-9f5b597fe945", 5, "Regulated Lifetime",
                               "The retention period for the data is defined by the referenced regulation.", false),

    /**
     * The data is needed for the specified time.
     */
    TIMEBOXED_LIFETIME ("ed7aa0e3-854c-4a4a-9535-eac319137826", 6, "Time Boxed Lifetime",
                               "The data is needed for the specified time.", false),

    /**
     * Another basis for determining the retention requirement.
     */
    OTHER             ("d32edb1e-8ee0-498a-859d-333e8c1f5de1", 99, "Other",
                               "Another basis for determining the retention requirement.", false);

    private static final String ENUM_TYPE_GUID  = "de79bf78-ecb0-4fd0-978f-ecc2cb4ff6c7";
    private static final String ENUM_TYPE_NAME  = "RetentionBasis";

    private static final String ENUM_DESCRIPTION = "Defines the retention requirements associated with a data item.";
    private static final String ENUM_DESCRIPTION_GUID = "f88ba42b-0422-4c6f-b25f-7cbc4e0cfc7f";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0421_GOVERNANCE_CLASSIFICATION;

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
    RetentionBasis(String  descriptionGUID,
                   int     ordinal,
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RetentionBasis{name='" + name + '}';
    }
}
