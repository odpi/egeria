/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.ElementStub;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProfileOutboundEvent describes the structure of the events emitted by the Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityProfileOutboundEvent extends CommunityProfileEventHeader
{
    private static final long    serialVersionUID = 1L;

    /*
     * Always set up
     */
    private CommunityProfileOutboundEventType eventType          = null;
    private ElementStub                       principleElement   = null;

    /*
     * For classification events
     */
    private String                            classificationName = null;

    /*
     * for relationship events
     */
    private ElementStub                       endOneElement      = null;
    private ElementStub                       endTwoElement      = null;

    /*
     * For karma point plateau events
     */
    private String                            userId             = null;
    private long                              pointsTotal        = 0;
    private long                              plateau            = 0;


    /**
     * Default constructor
     */
    public CommunityProfileOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityProfileOutboundEvent(CommunityProfileOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            eventType = template.getEventType();
            principleElement = template.getPrincipleElement();

            classificationName = template.getClassificationName();

            endOneElement = template.getEndOneElement();
            endTwoElement = template.getEndTwoElement();

            userId = template.getUserId();
            pointsTotal = template.getPointsTotal();
            plateau = template.getPlateau();
        }
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public CommunityProfileOutboundEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(CommunityProfileOutboundEventType eventType)
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
     * @return
     */
    public ElementStub getEndOneElement()
    {
        return endOneElement;
    }


    public void setEndOneElement(ElementStub endOneElement)
    {
        this.endOneElement = endOneElement;
    }


    public ElementStub getEndTwoElement()
    {
        return endTwoElement;
    }


    public void setEndTwoElement(ElementStub endTwoElement)
    {
        this.endTwoElement = endTwoElement;
    }


    /**
     * Return user identity of associated user.
     *
     * @return user id
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the user id of the associated user.
     *
     * @param userId user id
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return the total number of karma points that an individual has achieved.
     *
     * @return long
     */
    public long getPointsTotal()
    {
        return pointsTotal;
    }


    /**
     * Set up the total number of karma points that an individual has achieved.
     *
     * @param pointsTotal long
     */
    public void setPointsTotal(long pointsTotal)
    {
        this.pointsTotal = pointsTotal;
    }


    /**
     * Return the current karma point plateau for this individual.
     *
     * @return long
     */
    public long getPlateau()
    {
        return plateau;
    }


    /**
     * Set up the current karma point plateau for this individual.
     *
     * @param plateau long
     */
    public void setPlateau(long plateau)
    {
        this.plateau = plateau;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityProfileOutboundEvent{" +
                "eventType=" + eventType +
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
        if (!(objectToCompare instanceof CommunityProfileOutboundEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CommunityProfileOutboundEvent that = (CommunityProfileOutboundEvent) objectToCompare;
        return getEventType() == that.getEventType();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEventType());
    }
}
