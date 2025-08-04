/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITProfileRoleAppointmentProperties describes the properties for the ITProfileRoleAppointment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ITProfileRoleAppointmentProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor
     */
    public ITProfileRoleAppointmentProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ITProfileRoleAppointmentProperties(ITProfileRoleAppointmentProperties template)
    {
        super(template);
    }

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ITProfileRoleAppointmentProperties{} " + super.toString();
    }
}
