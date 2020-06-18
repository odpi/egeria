/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceEventHeader provides a common base for all events from the access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DigitalServiceEvent.class, name = "DigitalServiceEvent")
})
public abstract class DigitalServiceEventHeader implements Serializable
{
    private static final long serialVersionUID = 1L;


    private long eventVersionId = 1L;


    /**
     * Default Constructor sets the properties to nulls
     */
    DigitalServiceEventHeader()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template object to copy
     */
    DigitalServiceEventHeader(DigitalServiceEventHeader template)
    {
        if (template != null)
        {
            this.eventVersionId = template.getEventVersionId();
        }
    }


    /**
     * Return the event version id for this event structure.
     *
     * @return long
     */
    public long getEventVersionId()
    {
        return eventVersionId;
    }


    /**
     * Set up the event version id for this event structure.
     *
     * @param eventVersionId long
     */
    public void setEventVersionId(long eventVersionId)
    {
        this.eventVersionId = eventVersionId;
    }


    /**
     * {@inheritDoc}
     *
     * JSON-style toString
     */
    @Override
    public String toString()
    {
        return "DigitalServiceEventHeader{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }


    /**
     * {@inheritDoc}
     *
     * Return comparison result based on the content of the properties.
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof DigitalServiceEventHeader))
        {
            return false;
        }
        DigitalServiceEventHeader that = (DigitalServiceEventHeader) objectToCompare;
        return getEventVersionId() == that.getEventVersionId();
    }


    /**
     * {@inheritDoc}
     *
     * Return hash code for this object
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEventVersionId());
    }

}
