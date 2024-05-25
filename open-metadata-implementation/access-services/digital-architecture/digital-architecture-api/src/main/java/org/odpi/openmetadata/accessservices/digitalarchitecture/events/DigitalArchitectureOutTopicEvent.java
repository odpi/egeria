/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalArchitectureOutTopicEvent describes the structure of the events emitted by the Digital Architecture OMAS that are about assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalArchitectureOutTopicEvent
{
    private long                         eventVersionId    = 1L;
    private DigitalArchitectureEventType eventType         = null;
    private Date                         eventTime         = null;
    private ElementHeader                elementHeader     = null;
    private Map<String, Object>          elementProperties = null;

    private ElementHeader       previousElementHeader     = null;
    private Map<String, Object> previousElementProperties = null;

    private String                 classificationName               = null;
    private Map<String, Object>    previousClassificationProperties = null;



    /**
     * Default Constructor
     */
    public DigitalArchitectureOutTopicEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalArchitectureOutTopicEvent(DigitalArchitectureOutTopicEvent template)
    {
        if (template != null)
        {
            eventVersionId = template.getEventVersionId();
            eventType = template.getEventType();
            elementHeader = template.getElementHeader();
            elementProperties = template.getElementProperties();
            previousElementHeader = template.getPreviousElementHeader();
            previousElementProperties = template.getPreviousElementProperties();
            classificationName = template.getClassificationName();
            previousClassificationProperties = template.getPreviousClassificationProperties();
        }
    }


    /**
     * Return the event version id.
     *
     * @return long
     */
    public long getEventVersionId()
    {
        return eventVersionId;
    }


    /**
     * Set up the event version id.
     *
     * @param eventVersionId long
     */
    public void setEventVersionId(long eventVersionId)
    {
        /* provided for Jackson */
    }


    /**
     * Return the event type.
     *
     * @return the event type enum
     */
    public DigitalArchitectureEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the event type.
     *
     * @param eventType event type enum
     */
    public void setEventType(DigitalArchitectureEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the time that the element was updated.
     *
     * @return date/time
     */
    public Date getEventTime()
    {
        return eventTime;
    }


    /**
     * Set up the time that the element was updated.
     *
     * @param eventTime date/time
     */
    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }


    /**
     * Return details of the subject of the event.
     *
     * @return element header
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up details of the subject of the event.
     *
     * @param elementHeader element header
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the map of properties for the element provided with the event.  These values are not guaranteed to be current.
     *
     * @return property map
     */
    public Map<String, Object> getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Set up the map of properties for the element provided with the event.  These values are not guaranteed to be current.
     *
     * @param elementProperties property map
     */
    public void setElementProperties(Map<String, Object> elementProperties)
    {
        this.elementProperties = elementProperties;
    }


    /**
     * Return the previous version of the element's header (if the event is related to an element update).
     *
     * @return element header
     */
    public ElementHeader getPreviousElementHeader()
    {
        return previousElementHeader;
    }


    /**
     * Set up the previous version of the element's header (if the event is related to an element update).
     *
     * @param previousElementHeader element header
     */
    public void setPreviousElementHeader(ElementHeader previousElementHeader)
    {
        this.previousElementHeader = previousElementHeader;
    }


    /**
     * Return the previous version of the element's properties (if the event is related to an element update).
     *
     * @return property map
     */
    public Map<String, Object> getPreviousElementProperties()
    {
        return previousElementProperties;
    }


    /**
     * Set up the previous version of the element's properties (if the event is related to an element update).
     *
     * @param previousElementProperties property map
     */
    public void setPreviousElementProperties(Map<String, Object> previousElementProperties)
    {
        this.previousElementProperties = previousElementProperties;
    }


    /**
     * Return the name of the classification if the event relates to classifications.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up the name of the classification if the event relates to classifications.
     *
     * @param classificationName string name
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the property map for the previous version of a classification's properties (used for reclassify events).
     *
     * @return property map
     */
    public Map<String, Object> getPreviousClassificationProperties()
    {
        return previousClassificationProperties;
    }


    /**
     * Set up the property map for the previous version of a classification's properties (used for reclassify events).
     *
     * @param previousClassificationProperties property map
     */
    public void setPreviousClassificationProperties(Map<String, Object> previousClassificationProperties)
    {
        this.previousClassificationProperties = previousClassificationProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalArchitectureOutTopicEvent{" +
                       "eventVersionId=" + eventVersionId +
                       ", eventType=" + eventType +
                       ", eventTime=" + eventTime +
                       ", elementHeader=" + elementHeader +
                       ", elementProperties=" + elementProperties +
                       ", previousElementHeader=" + previousElementHeader +
                       ", previousElementProperties=" + previousElementProperties +
                       ", classificationName='" + classificationName + '\'' +
                       ", previousClassificationProperties=" + previousClassificationProperties +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DigitalArchitectureOutTopicEvent that = (DigitalArchitectureOutTopicEvent) objectToCompare;
        return eventVersionId == that.eventVersionId &&
                       eventType == that.eventType &&
                       Objects.equals(eventTime, that.eventTime) &&
                       Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(elementProperties, that.elementProperties) &&
                       Objects.equals(previousElementHeader, that.previousElementHeader) &&
                       Objects.equals(previousElementProperties, that.previousElementProperties) &&
                       Objects.equals(classificationName, that.classificationName) &&
                       Objects.equals(previousClassificationProperties, that.previousClassificationProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(eventVersionId, eventType, eventTime, elementHeader, elementProperties, classificationName, previousElementHeader,
                            previousElementProperties, previousClassificationProperties);
    }

}
