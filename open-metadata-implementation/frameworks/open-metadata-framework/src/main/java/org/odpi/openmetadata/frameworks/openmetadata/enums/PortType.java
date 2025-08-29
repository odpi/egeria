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
 * PortType defines the different port types used for open metadata. It is used in a port definition.
 * <ul>
 * <li>INPUT_PORT - Data is passed into the process.</li>
 * <li>OUTPUT_PORT - Data is produced by the process.</li>
 * <li>INOUT_PORT - A request response interface is provided by the process.</li>
 * <li>OUTIN_PORT - A request response call is made by the process.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum
    PortType implements OpenMetadataEnum
{
    /**
     * Input Port - Data is passed into the process.
     */
    INPUT_PORT("dbca8005-949d-4cc9-bc5f-f5aa66f09906", 0, "Input Port", "Data is passed into the process."),

    /**
     * Output Port - Data is produced by the process.
     */
    OUTPUT_PORT("e6182593-ce3c-43ca-9163-5300b57b5017", 1, "Output Port", "Data is produced by the process."),

    /**
     * Input-Output Port - A request-response interface is provided by the process.
     */
    INOUT_PORT("59fcdb6c-b57a-46db-bb79-256ed9af0a67", 2, "Input-Output Port", "A request response interface is provided by the process."),

    /**
     * Output-Input Port - A request-response call is made by the process.
     */
    OUTIN_PORT("4564ff30-68eb-46b6-8887-83d8575d19b6", 3, "Output-Input Port", "A request response call is made by the process."),

    /**
     * Other - None of the above.
     */
    OTHER("a3fa3fa4-8f25-408d-8093-f84619c2859e", 99, "Other", "None of the above.");


    private static final String ENUM_TYPE_GUID        = "b57Fbce7-42ac-71D1-D6a6-9f62Cb7C6dc3";
    private static final String ENUM_TYPE_NAME        = "PortType";
    private static final String ENUM_DESCRIPTION      = "Descriptor for a port that indicates the direction that data is flowing.";
    private static final String ENUM_DESCRIPTION_GUID = "d5142643-0bc3-4a77-a1ec-1537db2fbe79";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0217_PORTS;
    private final        String descriptionGUID;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal         numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name            default string name of the enumeration
     * @param description     default string description of the enumeration
     */
    PortType(String descriptionGUID,
             int ordinal,
             String name,
             String description)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
    }


    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
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
        return false;
    }

    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID()
    {
        return ENUM_TYPE_GUID;
    }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName()
    {
        return ENUM_TYPE_NAME;
    }


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
                                                OpenMetadataProperty.CONTAINMENT_TYPE.name,
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
                                            OpenMetadataProperty.PORT_TYPE.name,
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
        return "PortType{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            '}';

    }
}
