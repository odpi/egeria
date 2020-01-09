/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * KarmaPointOutboundEvent publicises the karma point plateau achievement.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class KarmaPointOutboundEvent extends PersonalProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    private String userId              = null;
    private long   pointsTotal         = 0;
    private long   plateau             = 0;

    /**
     * Default constructor
     */
    public KarmaPointOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public KarmaPointOutboundEvent(KarmaPointOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            userId = template.getUserId();
            pointsTotal = template.getPointsTotal();
            plateau = template.getPlateau();
        }
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
        return "KarmaPointOutboundEvent{" +
                       "userId='" + userId + '\'' +
                       ", pointsTotal=" + pointsTotal +
                       ", plateau=" + plateau +
                       ", personalProfile=" + getPersonalProfile() +
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
        KarmaPointOutboundEvent that = (KarmaPointOutboundEvent) objectToCompare;
        return getPointsTotal() == that.getPointsTotal() &&
                       getPlateau() == that.getPlateau() &&
                       Objects.equals(getUserId(), that.getUserId());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserId(), getPointsTotal(), getPlateau());
    }
}
