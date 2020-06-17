/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceEvent describes the structure of the events emitted by the Digital Service OMAS.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalServiceEvent extends DigitalServiceEventHeader
{
    private static final long    serialVersionUID = 1L;

    private DigitalServiceEventType eventType              = null;
    private DigitalService               originalDigitalService = null;
    private DigitalService               digitalService         = null;


    /**
     * Default constructor
     */
    public DigitalServiceEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalServiceEvent(DigitalServiceEvent template)
    {
        super(template);

        if (template != null)
        {
            this.eventType = template.getEventType();
            this.digitalService = template.getDigitalService();
            this.originalDigitalService = template.getOriginalDigitalService();
        }
    }

    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public DigitalServiceEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(DigitalServiceEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the original digital service description.
     *
     * @return properties about the digitalService
     */
    public DigitalService getOriginalDigitalService()
    {
        if (originalDigitalService == null)
        {
            return null;
        }
        else
        {
            return new DigitalService(originalDigitalService);
        }
    }


    /**
     * Set up the original digital service description.
     *
     * @param originalDigitalService - properties about the digitalService.
     */
    public void setOriginalDigitalService(DigitalService originalDigitalService)
    {
        this.originalDigitalService = originalDigitalService;
    }


    /**
     * Return the digital service description.
     *
     * @return properties about the digitalService
     */
    public DigitalService getDigitalService()
    {
        if (digitalService == null)
        {
            return null;
        }
        else
        {
            return new DigitalService(digitalService);
        }
    }


    /**
     * Set up the digital service description.
     *
     * @param digitalService - properties about the digitalService.
     */
    public void setDigitalService(DigitalService digitalService)
    {
        this.digitalService = digitalService;
    }


    /**
     * {@inheritDoc}
     *
     * JSON-style toString
     */
    @Override
    public String toString()
    {
        return "DigitalServiceEvent{" +
                "eventType=" + eventType +
                ", originalDigitalService=" + originalDigitalService +
                ", digitalService=" + digitalService +
                ", eventVersionId=" + getEventVersionId() +
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
        if (!(objectToCompare instanceof DigitalServiceEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DigitalServiceEvent that = (DigitalServiceEvent) objectToCompare;
        return getEventType() == that.getEventType() &&
                Objects.equals(getOriginalDigitalService(), that.getOriginalDigitalService()) &&
                Objects.equals(getDigitalService(), that.getDigitalService());
    }


    /**
     * {@inheritDoc}
     *
     * Return hash code for this object
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEventType(), getOriginalDigitalService(), getDigitalService());
    }
}
