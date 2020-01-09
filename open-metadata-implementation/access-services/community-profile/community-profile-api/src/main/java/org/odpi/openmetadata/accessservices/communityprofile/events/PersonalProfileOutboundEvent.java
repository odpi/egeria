/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalProfileOutboundEvent provides a bean for representing one of the events about personal profiles
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = FavouriteCollectionOutboundEvent.class, name = "FavouriteCollectionOutboundEvent"),
                      @JsonSubTypes.Type(value = KarmaPointOutboundEvent.class, name = "KarmaPointOutboundEvent"),
              })
public class PersonalProfileOutboundEvent extends CommunityProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    private PersonalProfile personalProfile = null;


    /**
     * Default constructor
     */
    public PersonalProfileOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileOutboundEvent(PersonalProfileOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            personalProfile = template.getPersonalProfile();
        }
    }


    /**
     * Return the personal profile for the individual.
     *
     * @return PersonalProfile bean
     */
    public PersonalProfile getPersonalProfile()
    {
        return personalProfile;
    }


    /**
     * Set up the personal profile for the individual.
     *
     * @param personalProfile PersonalProfile bean
     */
    public void setPersonalProfile(PersonalProfile personalProfile)
    {
        this.personalProfile = personalProfile;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonalProfileOutboundEvent{" +
                       "personalProfile=" + personalProfile +
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
        PersonalProfileOutboundEvent that = (PersonalProfileOutboundEvent) objectToCompare;
        return Objects.equals(getPersonalProfile(), that.getPersonalProfile());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getPersonalProfile());
    }
}
