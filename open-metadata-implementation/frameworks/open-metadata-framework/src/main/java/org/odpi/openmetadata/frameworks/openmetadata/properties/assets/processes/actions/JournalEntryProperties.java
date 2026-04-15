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
 * The "JournalEntry" describes a private journal entry published by an actor.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class JournalEntryProperties extends NotificationProperties
{
    /**
     * Default constructor
     */
    public JournalEntryProperties()
    {
        super();
        super.typeName = OpenMetadataType.JOURNAL_ENTRY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public JournalEntryProperties(NotificationProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.JOURNAL_ENTRY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public JournalEntryProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.JOURNAL_ENTRY.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "JournalEntryProperties{" +
                "} " + super.toString();
    }
}
