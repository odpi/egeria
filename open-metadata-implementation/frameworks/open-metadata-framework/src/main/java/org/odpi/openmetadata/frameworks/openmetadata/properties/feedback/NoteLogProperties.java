/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLogHeader manages a list of notes for an element
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogProperties extends ReferenceableProperties
{
    /**
     * Default constructor
     */
    public NoteLogProperties()
    {
        super();
        super.typeName = OpenMetadataType.NOTE_LOG.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template note log to copy
     */
    public NoteLogProperties(NoteLogProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template note log to copy
     */
    public NoteLogProperties(ReferenceableProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NoteLogProperties{" +
                "} " + super.toString();
    }
}