/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.EmbeddedProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The  action described a process that is triggered by an event.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ToDoProperties.class, name = "ToDoProperties"),
        @JsonSubTypes.Type(value = MeetingProperties.class, name = "MeetingProperties"),
        @JsonSubTypes.Type(value = EngineActionProperties.class, name = "EngineActionProperties"),
})
public class ActionProperties extends ProcessProperties
{
    /**
     * Default constructor
     */
    public ActionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ACTION.typeName);;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActionProperties(ProcessProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionProperties{" +
                "} " + super.toString();
    }
}
