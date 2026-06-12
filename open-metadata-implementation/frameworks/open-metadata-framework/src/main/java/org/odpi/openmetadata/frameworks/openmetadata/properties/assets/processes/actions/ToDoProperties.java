/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingRequestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "To Do" describes an action - it may be assigned to a person role (see ActionAssignment).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataSharingRequestProperties.class, name = "DataSharingRequestProperties")
        })
public class ToDoProperties extends ActionProperties
{
    /**
     * Default constructor
     */
    public ToDoProperties()
    {
        super();
        super.typeName = OpenMetadataType.TO_DO.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoProperties(ToDoProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.TO_DO.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ToDoProperties{" +
                "} " + super.toString();
    }
}
