/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRoleAppointmentProperties provides details of a PersonRoleAppointment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleAppointmentProperties extends RelationshipBeanProperties
{
    int     expectedTimeAllocationPercent = 100;

    /**
     * Default constructor
     */
    public PersonRoleAppointmentProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleAppointmentProperties(PersonRoleAppointmentProperties template)
    {
        super(template);

        if (template != null)
        {
            this.expectedTimeAllocationPercent = template.getExpectedTimeAllocationPercent();
        }
    }


    /**
     * Return the expected percentage of a person's time that should be allocated to the role.
     *
     * @return int
     */
    public int getExpectedTimeAllocationPercent()
    {
        return expectedTimeAllocationPercent;
    }


    /**
     * Set up the expected percentage of a person's time that should be allocated to the role.
     *
     * @param expectedTimeAllocationPercent int
     */
    public void setExpectedTimeAllocationPercent(int expectedTimeAllocationPercent)
    {
        this.expectedTimeAllocationPercent = expectedTimeAllocationPercent;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonRoleAppointmentProperties{" +
                "expectedTimeAllocationPercent=" + expectedTimeAllocationPercent +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        PersonRoleAppointmentProperties that = (PersonRoleAppointmentProperties) objectToCompare;
        return  Objects.equals(expectedTimeAllocationPercent, that.expectedTimeAllocationPercent);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), expectedTimeAllocationPercent);
    }
}
