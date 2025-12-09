/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

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
        @JsonSubTypes.Type(value = NotificationProperties.class, name = "NotificationProperties"),
        @JsonSubTypes.Type(value = ToDoProperties.class, name = "ToDoProperties"),
        @JsonSubTypes.Type(value = ReviewProperties.class, name = "ReviewProperties"),
        @JsonSubTypes.Type(value = MeetingProperties.class, name = "MeetingProperties"),
        @JsonSubTypes.Type(value = EngineActionProperties.class, name = "EngineActionProperties"),
        @JsonSubTypes.Type(value = EmbeddedProcessProperties.class, name = "EmbeddedProcessProperties"),
})
public class ActionProperties extends ProcessProperties
{
    private String situation = null;

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
    public ActionProperties(ActionProperties template)
    {
        super(template);

        if (template != null)
        {
            situation = template.getSituation();
        }
    }


    /**
     * Return the activity/discovery of the producer that resulted in this action.
     *
     * @return string
     */
    public String getSituation()
    {
        return situation;
    }


    /**
     * Set up the activity/discovery of the producer that resulted in this action.
     *
     * @param situation string
     */
    public void setSituation(String situation)
    {
        this.situation = situation;
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
                "situation='" + situation + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ActionProperties that = (ActionProperties) objectToCompare;
        return Objects.equals(situation, that.situation);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), situation);
    }
}
