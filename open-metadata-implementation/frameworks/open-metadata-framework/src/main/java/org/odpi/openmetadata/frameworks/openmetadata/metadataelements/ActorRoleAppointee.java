/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AppointmentProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActorRoleAppointee is the bean used to return a role and current appointee(s).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                  @JsonSubTypes.Type(value = ActorRoleHistory.class, name = "ActorRoleHistory"),
                  @JsonSubTypes.Type(value = AgreementRoleAppointee.class, name = "AgreementRoleAppointee"),
              })
public class ActorRoleAppointee extends ActorRoleElement
{
    private AppointmentProperties appointmentProperties = null;
    private ActorProfileElement   profile           = null;

    /**
     * Default constructor
     */
    public ActorRoleAppointee()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorRoleAppointee(ActorRoleAppointee template)
    {
        super(template);

        if (template != null)
        {
            this.profile               = template.getProfile();
            this.appointmentProperties = template.getAppointmentProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorRoleAppointee(ActorRoleElement template)
    {
        super(template);
    }


    /**
     * Return the properties of the appointment relationship.
     *
     * @return properties
     */
    public AppointmentProperties getAppointmentProperties()
    {
        return appointmentProperties;
    }


    /**
     * Set up the properties of the appointment relationship.
     *
     * @param appointmentProperties properties
     */
    public void setAppointmentProperties(AppointmentProperties appointmentProperties)
    {
        this.appointmentProperties = appointmentProperties;
    }


    /**
     * Return the profile information for the individual.
     *
     * @return personal profile object
     */
    public ActorProfileElement getProfile()
    {
        return profile;
    }


    /**
     * Set up the profile information for the individual.
     *
     * @param profile personal profile object
     */
    public void setProfile(ActorProfileElement profile)
    {
        this.profile = profile;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActorRoleAppointee{" +
                ", appointmentProperties=" + appointmentProperties +
                ", profile=" + profile +
                "} " + super.toString();
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
        ActorRoleAppointee that = (ActorRoleAppointee) objectToCompare;
        return Objects.equals(appointmentProperties, that.appointmentProperties) &&
                Objects.equals(profile, that.profile);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), appointmentProperties, profile);
    }
}
