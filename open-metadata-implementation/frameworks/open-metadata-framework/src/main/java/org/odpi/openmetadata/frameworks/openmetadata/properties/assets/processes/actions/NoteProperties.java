/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "Note" describes an opinion or additional information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteProperties extends NotificationProperties
{
    /**
     * Default constructor
     */
    public NoteProperties()
    {
        super();
        super.typeName = OpenMetadataType.NOTE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteProperties(NotificationProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.NOTE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.NOTE.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteProperties{" +
                "} " + super.toString();
    }
}
