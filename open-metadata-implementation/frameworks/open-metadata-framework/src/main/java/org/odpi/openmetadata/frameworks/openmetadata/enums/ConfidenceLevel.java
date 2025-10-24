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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ConfidenceLevel identifies the level of confidence to place in the accuracy of a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConfidenceLevel implements OpenMetadataEnum
{
    /**
     * There is no assessment of the confidence level for this data.
     */
    UNCLASSIFIED  (0, "960c2b75-3126-41c2-97b7-351ac6526f90", "Unclassified",
                               "There is no assessment of the confidence level for this data.", true),

    /**
     * The data comes from an ad hoc process.
     */
    AD_HOC        (1, "db29afb9-1758-4589-97e8-94c4a4c6f2bc", "Ad Hoc",
                               "The data comes from an ad hoc process.", false),

    /**
     * The data comes from a transactional system so it may have a narrow scope.
     */
    TRANSACTIONAL (2, "efa85f15-f3b3-43ae-ad24-d2918aa18d68", "Transactional",
                               "The data comes from a transactional system so it may have a narrow scope.", false),

    /**
     * The data comes from an authoritative source. This is the best set of values.
     */
    AUTHORITATIVE (3, "1739e5af-c810-437d-8c7b-98451fe85574", "Authoritative",
                               "The data comes from an authoritative source. This is the best set of values.", false),

    /**
     * The data is derived from other data through an analytical process.
     */
    DERIVED       (4, "b631b7e3-3793-45bd-8ab6-ead3494a3cfe", "Derived",
                               "The data is derived from other data through an analytical process.", false),

    /**
     * The data comes from an obsolete source and must no longer be used.
     */
    OBSOLETE      (5, "3f2a97ab-7276-4f92-b194-7ca215d6daa6", "Obsolete",
                               "The data comes from an obsolete source and must no longer be used.", false),

    /**
     * Another classification assignment status.
     */
    OTHER         (99, "dcfe3806-3a55-445e-995c-eb3db9ac59a5", "Other",
                               "Another classification assignment status.", false);

    private static final String ENUM_TYPE_GUID  = "ae846797-d88a-4421-ad9a-318bf7c1fe6f";
    private static final String ENUM_TYPE_NAME  = "ConfidenceLevel";

    private static final String ENUM_DESCRIPTION = "Defines the level of confidence to place in the accuracy of a data item.";
    private static final String ENUM_DESCRIPTION_GUID = "1f7a3730-8471-496b-a6e5-21eac7bb3b98";
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
    ConfidenceLevel(int     ordinal,
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
                                                OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
                                                null,
                                                name);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfidenceLevel{name='" + name + '}';
    }
}
