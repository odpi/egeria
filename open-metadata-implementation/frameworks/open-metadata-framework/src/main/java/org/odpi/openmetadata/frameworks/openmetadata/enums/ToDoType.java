/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoType provides an initial set of values for ToDoType.  These can be extended as required.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ToDoType implements OpenMetadataEnum
{
    /**
     * A Survey Action Service has issued a Request For Action.
     */
    REQUEST_FOR_ACTION (0, "Request For Action", "A Survey Action Service has issued a Request For Action.", "requestForAction","c64dbb28-dfec-446f-8899-95af8c6654b6", false),

    /**
     * An error was detected in the linked data.
     */
    DATA_ERROR (1, "Data Error", "An error was detected in the linked data.", "dataResource", "96cece9e-c0f0-48ac-9512-c3dda95df005", false),

    /**
     * A user has requested access to the linked resource.
     */
    ACCESS_REQUEST (2, "Access Request", "A user has requested access to the linked resource.","requestedResource","3bdf2eed-1ed8-4e8d-b792-41371fc72d94", false),

    ;

    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  actionTargetName;
    private final String  descriptionGUID;
    private final boolean isDefault;


    /**
     * Default constructor for the classification origin.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    ToDoType(int     ordinal,
             String  name,
             String  description,
             String  actionTargetName,
             String  descriptionGUID,
             boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.actionTargetName = actionTargetName;
        this.descriptionGUID = descriptionGUID;
        this.isDefault = isDefault;
    }


    /**
     * Return the numeric representation of the classification origin.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the classification origin.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the classification origin.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Name of the action target to associate with the ToDoElement.
     *
     * @return name
     */
    public String getActionTargetName()
    {
        return actionTargetName;
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
        return isDefault;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ToDoType{name='" + name + '\'' + "}" ;
    }
}
