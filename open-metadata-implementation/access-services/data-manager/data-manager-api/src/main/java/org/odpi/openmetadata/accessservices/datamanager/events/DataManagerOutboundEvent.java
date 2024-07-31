/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataManagerOutboundEvent describes the structure of the events emitted by the Data Manager OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataManagerOutboundEvent extends DataManagerEventHeader
{
    /*
     * Always set up
     */
    private DataManagerOutboundEventType eventType        = null;
    private ElementStub                  principleElement = null;

    /*
     * For classification events
     */
    private String                       classificationName = null;

    /*
     * For relationship events
     */
    private ElementStub                  endOneElement      = null;
    private ElementStub                  endTwoElement      = null;


    /**
     * Default constructor
     */
    public DataManagerOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataManagerOutboundEvent(DataManagerOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            eventType = template.getEventType();
            principleElement = template.getPrincipleElement();

            classificationName = template.getClassificationName();

            endOneElement = template.getEndOneElement();
            endTwoElement = template.getEndTwoElement();
        }
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public DataManagerOutboundEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(DataManagerOutboundEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the element that is the subject of the event.
     *
     * @return element identifiers
     */
    public ElementStub getPrincipleElement()
    {
        return principleElement;
    }


    /**
     * Set up the element that is the subject of the event.
     *
     * @param principleElement element identifiers
     */
    public void setPrincipleElement(ElementStub principleElement)
    {
        this.principleElement = principleElement;
    }


    /**
     * Return the name of the classification that has changed.  Only set up for classify, reclassify and declassify events.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up the name of the classification that has changed.  Only set up for classify, reclassify and declassify events.
     *
     * @param classificationName string name
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the element at end one of the relationship that is described in the principleElement. This is only set up on
     * relationship events.
     *
     * @return element stub of entity at end 1
     */
    public ElementStub getEndOneElement()
    {
        return endOneElement;
    }


    /**
     * Set up the element at end one of the relationship that is described in the principleElement. This is only set up on
     * relationship events.
     *
     * @param endOneElement element stub of entity at end 1
     */
    public void setEndOneElement(ElementStub endOneElement)
    {
        this.endOneElement = endOneElement;
    }


    /**
     * Return the element at end two of the relationship that is described in the principleElement. This is only set up on
     * relationship events.
     *
     * @return element stub of entity at end 2
     */
    public ElementStub getEndTwoElement()
    {
        return endTwoElement;
    }


    /**
     * Set up the element at end two of the relationship that is described in the principleElement. This is only set up on
     * relationship events.
     *
     * @param endTwoElement element stub of entity at end 2
     */
    public void setEndTwoElement(ElementStub endTwoElement)
    {
        this.endTwoElement = endTwoElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataManagerOutboundEvent{" +
                       "eventType=" + eventType +
                       ", principleElement=" + principleElement +
                       ", classificationName='" + classificationName + '\'' +
                       ", endOneElement=" + endOneElement +
                       ", endTwoElement=" + endTwoElement +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataManagerOutboundEvent that = (DataManagerOutboundEvent) objectToCompare;
        return eventType == that.eventType &&
                       Objects.equals(principleElement, that.principleElement) &&
                       Objects.equals(classificationName, that.classificationName) &&
                       Objects.equals(endOneElement, that.endOneElement) &&
                       Objects.equals(endTwoElement, that.endTwoElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), eventType, principleElement, classificationName, endOneElement, endTwoElement);
    }
}
