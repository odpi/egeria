/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AppointmentProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * PersonRoleAppointee describes an individual's appointment to a specific governance role.  It includes their personal details along with the
 * start and end date of their appointment.  The elementHeader is from the PersonRoleAppointment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleAppointee implements Serializable, MetadataElement
{
    private static final long          serialVersionUID = 1L;

    private ElementHeader         elementHeader = null;
    private AppointmentProperties properties = null;
    private ActorProfileElement   profile   = null;



    /**
     * Default constructor
     */
    public PersonRoleAppointee()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleAppointee(PersonRoleAppointee template)
    {
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.profile = template.getProfile();
            this.properties = template.getProperties();
        }
    }



    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the appointment relationship.
     *
     * @return properties
     */
    public AppointmentProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the appointment relationship.
     *
     * @param properties properties
     */
    public void setProperties(AppointmentProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the profile information for the individual.
     *
     * @return personal profile object
     */
    public ActorProfileElement getProfile()
    {
        if (profile == null)
        {
            return null;
        }
        else
        {
            return profile;
        }
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonRoleAppointee{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", profile=" + profile +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        PersonRoleAppointee that = (PersonRoleAppointee) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(profile, that.profile);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, profile);
    }
}
