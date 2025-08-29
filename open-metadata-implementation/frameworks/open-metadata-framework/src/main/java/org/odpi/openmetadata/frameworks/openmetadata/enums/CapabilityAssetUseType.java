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
 * CapabilityAssetUseType defines how a software server capability may use an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CapabilityAssetUseType implements OpenMetadataEnum
{
    OWNS      ( 0,  "2cd9be3f-aab1-45cb-86d8-b73e5e1287d5", "Owns",      "The software server capability is accountable for the maintenance and protection of the asset.", true),
    GOVERNS   ( 1,  "264f123e-8675-4bc1-a4b4-1cfc96b95a08", "Governs",   "The software server capability provides management or oversight of the asset.", false),
    MAINTAINS ( 2, "e2bae930-4cef-431f-add3-44d3e1e32112", "Maintains", "The software server capability keeps the asset up-to-date.", false),
    USES      ( 3,  "8a91ee82-f840-4eef-aa8f-b5eda277692a", "Uses",      "The server capability consumes the content of the asset.", false),
    OTHER     (99, "e9cfe66e-6b54-46f7-8351-038088c0e83b", "Other",     "Other usage.", false);


    private static final String ENUM_TYPE_GUID  = "09439481-9489-467c-9ae5-178a6e0b6b5a";
    private static final String ENUM_TYPE_NAME  = "CapabilityAssetUseType";

    private static final String ENUM_DESCRIPTION = "Defines how a software server capability may use an asset.";
    private static final String ENUM_DESCRIPTION_GUID = "7d9d5d67-1915-4944-a985-f01cc5394da7";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0045_SERVERS_AND_ASSETS;

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
    CapabilityAssetUseType(int     ordinal,
                           String  descriptionGUID,
                           String  name,
                           String  description,
                           boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
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
                                                OpenMetadataProperty.USE_TYPE.name,
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
                                            OpenMetadataProperty.USE_TYPE.name,
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
        return "CapabilityAssetUseType{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';

    }
}
