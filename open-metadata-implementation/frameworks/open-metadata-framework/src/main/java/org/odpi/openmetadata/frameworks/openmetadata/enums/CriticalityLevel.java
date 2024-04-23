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
 * CriticalityLevel defines how important a data item is to the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CriticalityLevel
{
    /**
     * There is no assessment of the criticality for this data.
     */
    UNCLASSIFIED (0, "4c2c0370-973f-479c-9251-d400d55ad7e8", "Unclassified",
                               "There is no assessment of the criticality for this data.", true),

    /**
     * The data is of minor importance to the organization.
     */
    MARGINAL     (1, "75cf926a-57f9-494d-8b08-995bd0dd7a72", "Marginal",
                               "The data is of minor importance to the organization.", false),

    /**
     * The data is important to the running of the organization.
     */
    IMPORTANT    (2, "e78e4e87-9fa1-4b81-be4e-ca6b499d9898", "Important",
                               "The data is important to the running of the organization.", false),

    /**
     * The data is critical to the operation of the organization.
     */
    CRITICAL     (3, "a0b6807e-6440-48a7-b76c-847f9e95767a", "Critical",
                               "The data is critical to the operation of the organization.", false),

    /**
     * The data is so important that its loss is catastrophic, putting the future of the organization in doubt.
     */
    CATASTROPHIC (4, "e6effba2-3861-47f3-9738-dbc452532892", "Catastrophic",
                               "The data is so important that its loss is catastrophic, putting the future of the organization in doubt.", false),

    /**
     * Another criticality level.
     */
    OTHER        (99, "81044dc1-ae06-404b-83d7-1c4180daa09e","Other",
                               "Another criticality level.", false);

    private static final String ENUM_TYPE_GUID  = "22bcbf49-83e1-4432-b008-e09a8f842a1e";
    private static final String ENUM_TYPE_NAME  = "CriticalityLevel";

    private static final String ENUM_DESCRIPTION = "Defines how important a data item is to the organization.";
    private static final String ENUM_DESCRIPTION_GUID = "96925394-0ded-4c0a-9080-6668706ab317";
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
    CriticalityLevel(int     ordinal,
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
                                                OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER.name,
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
                                           OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER.name,
                                           null);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CriticalityLevel{name='" + name + '}';
    }
}
