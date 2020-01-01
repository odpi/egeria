/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Community;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityOutboundEvent supports the events related to communities
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityOutboundEvent extends CommunityProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    private Community community = null;


    /**
     * Default constructor
     */
    public CommunityOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityOutboundEvent(CommunityOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            community = template.getCommunity();
        }
    }


    /**
     * Return the community details.
     *
     * @return Community bean
     */
    public Community getCommunity()
    {
        return community;
    }


    /**
     * Set up the community details.
     *
     * @param community Community bean
     */
    public void setCommunity(Community community)
    {
        this.community = community;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityOutboundEvent{" +
                       "community=" + community +
                       ", eventType=" + getEventType() +
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
        CommunityOutboundEvent that = (CommunityOutboundEvent) objectToCompare;
        return Objects.equals(getCommunity(), that.getCommunity());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCommunity());
    }
}
