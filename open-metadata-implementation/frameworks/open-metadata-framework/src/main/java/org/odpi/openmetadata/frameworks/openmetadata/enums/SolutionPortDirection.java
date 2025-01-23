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
 * SolutionPortDirection defines the different port types used for open metadata. It is used in a SolutionPortElement definition.
 * <ul>
 * <li>INPUT - Data is passed into the solution component.</li>
 * <li>OUTPUT - Data is produced by the solution component.</li>
 * <li>INOUT - A request response interface is provided by the solution component.</li>
 * <li>OUTIN - A request response call is made by the solution component.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum SolutionPortDirection implements OpenMetadataEnum
{
    UNKNOWN("29454899-dd9b-4855-8846-f7f1bb05e5d3", 0, "Unknown", "The direction of flow is unknown."),

    /**
     * Input Port - Data is passed into the solution component.
     */
    INPUT("d0148bd8-5240-4262-9181-c2e343111d79", 2, "Input", "Data is passed into the solution component."),

    /**
     * Output Port - Data is produced by the solution component.
     */
    OUTPUT("ba93f0c5-e502-4a19-a1dd-0fda01dbe54c", 1, "Output", "Data is produced by the solution component."),

    /**
     * Input-Output Port - A request-response interface is provided by the solution component.
     */
    INOUT("c9db81ce-6c98-480f-bf49-c05a13a832ca", 3, "Input-Output", "A request response interface is provided by the solution component."),

    /**
     * Output-Input Port - A request-response call is made by the solution component.
     */
    OUTIN("9e1339d2-92a5-41a3-9aed-8b0bd66a4a75", 4, "Output-Input", "A request response call is made by the solution component."),

    /**
     * Other - None of the above.
     */
    OTHER("a6b3233b-b38f-47ad-82aa-f8eb1748b946", 99, "Other", "None of the above.");


    private static final String ENUM_TYPE_GUID        = "4879c96e-26c7-48af-ba92-8277632be733";
    private static final String ENUM_TYPE_NAME        = "SolutionPortDirection";
    private static final String ENUM_DESCRIPTION      = "Descriptor for a solution port that indicates the direction that data is flowing.";
    private static final String ENUM_DESCRIPTION_GUID = "cad833b8-4619-49f1-a28a-e6a60f38ef4f";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0735_SOLUTION_PORTS_AND_WIRES;
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
    SolutionPortDirection(String descriptionGUID,
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
                                                OpenMetadataProperty.DIRECTION.name,
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
                                           OpenMetadataProperty.DIRECTION.name,
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
        return "PortType{" + name + '}';
    }
}