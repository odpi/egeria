/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

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
public enum PortType implements Serializable
{
    INPUT_PORT  (0,  "INPUT_PORT", "Input Port",        "Data is passed into the process."),
    OUTPUT_PORT (1,  "OUTPUT_PORT","Output Port",       "Data is produced by the process."),
    INOUT_PORT  (2,  "INOUT_PORT", "Input-Output Port", "A request response interface is provided by the process."),
    OUTIN_PORT  (3,  "OUTIN_PORT", "Output-Input Port", "A request response call is made by the process."),
    OTHER       (99, "OTHER",      "Other",             "None of the above.");

    private static final long serialVersionUID = 1L;

    public static final String ENUM_TYPE_GUID  = "b57Fbce7-42ac-71D1-D6a6-9f62Cb7C6dc3";
    public static final String ENUM_TYPE_NAME  = "PortType";

    private int    ordinal;
    private String openTypeSymbolicName;
    private String name;
    private String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeSymbolicName code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    PortType(int    ordinal,
             String openTypeSymbolicName,
             String name,
             String description)
    {
        this.ordinal              = ordinal;
        this.openTypeSymbolicName = openTypeSymbolicName;
        this.name                 = name;
        this.description          = description;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the symbolic name for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int symbolic name
     */
    public String getOpenTypeSymbolicName()
    {
        return openTypeSymbolicName;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PortType{" +
                       "codeValue=" + ordinal +
                       ", codeName='" + name + '\'' +
                       ", description='" + description +
                       '}';
    }
}

