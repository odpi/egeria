/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.DigitalService;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalArchitectureEvent describes the structure of the events emitted by the Digital Architecture OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalArchitectureEvent extends DigitalArchitectureEventHeader
{
    private static final long    serialVersionUID = 1L;

    private DigitalArchitectureEventType eventType              = null;
    private DigitalService               originalDigitalService = null;
    private DigitalService               digitalService         = null;


    /**
     * Default constructor
     */
    public DigitalArchitectureEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalArchitectureEvent(DigitalArchitectureEvent template)
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
    public DigitalArchitectureEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(DigitalArchitectureEventType eventType)
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DigitalArchitectureEvent{" +
                "eventType=" + eventType +
                ", originalDigitalService=" + originalDigitalService +
                ", digitalService=" + digitalService +
                ", eventVersionId=" + getEventVersionId() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof DigitalArchitectureEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DigitalArchitectureEvent that = (DigitalArchitectureEvent) objectToCompare;
        return getEventType() == that.getEventType() &&
                Objects.equals(getOriginalDigitalService(), that.getOriginalDigitalService()) &&
                Objects.equals(getDigitalService(), that.getDigitalService());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEventType(), getOriginalDigitalService(), getDigitalService());
    }
}
