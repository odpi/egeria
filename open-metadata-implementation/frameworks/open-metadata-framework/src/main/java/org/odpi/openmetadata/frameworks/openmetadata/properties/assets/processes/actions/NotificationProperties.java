/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "Notification" describes information that needs to be passed to an actor
 * (see AssignmentScope). It is typically assigned to a person-role.  The actor
 * then acts on the information and closes it when no longer relevant.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActivityEntryProperties.class, name = "ActivityEntryProperties"),
        @JsonSubTypes.Type(value = BlogEntryProperties.class, name = "BlogEntryProperties"),
        @JsonSubTypes.Type(value = JournalEntryProperties.class, name = "JournalEntryProperties"),
})
public class NotificationProperties extends ActionProperties
{
    /**
     * Default constructor
     */
    public NotificationProperties()
    {
        super();
        super.typeName = OpenMetadataType.NOTIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NotificationProperties(NotificationProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NotificationProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.NOTIFICATION.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NotificationProperties{" +
                "} " + super.toString();
    }
}
