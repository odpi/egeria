/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.events.content.SubjectAreaEventContent;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaEvent describes the structure of the org.odpi.openmetadata.accessservices.subjectarea.common.events emitted by the Subject Area OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaEvent implements Serializable
{
    SubjectAreaEventType  eventType = SubjectAreaEventType.UNKNOWN_SUBJECTAREA_EVENT;

    SubjectAreaEventContent subjectAreaEventContent = null;


    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public SubjectAreaEvent()
    {
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public SubjectAreaEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(SubjectAreaEventType eventType)
    {
        this.eventType = eventType;
    }

    public SubjectAreaEventContent getSubjectAreaEventContent() {
        return subjectAreaEventContent;
    }

    public void setSubjectAreaEventContent(SubjectAreaEventContent subjectAreaEventContent) {
        this.subjectAreaEventContent = subjectAreaEventContent;
    }
}
