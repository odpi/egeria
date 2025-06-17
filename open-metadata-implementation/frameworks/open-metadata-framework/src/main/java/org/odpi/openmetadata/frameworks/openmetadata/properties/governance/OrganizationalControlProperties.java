/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An organizational control describes a business activity that supports the governance program implementation.
 * Examples include training, responsibility, buddy-checking etc.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceResponsibilityProperties.class, name = "GovernanceResponsibilityProperties"),
                @JsonSubTypes.Type(value = GovernanceProcedureProperties.class, name = "GovernanceProcedureProperties"),
        })
public class OrganizationalControlProperties extends GovernanceControlProperties
{
    /**
     * Default Constructor
     */
    public OrganizationalControlProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ORGANIZATIONAL_CONTROL.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public OrganizationalControlProperties(OrganizationalControlProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "OrganizationalControlProperties{" +
                "} " + super.toString();
    }
}
