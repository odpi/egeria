/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance responsibility describes a responsibility that is assigned to a person or team. It could be a
 * requirement to take certain action in specific circumstances, or to make particular types of decisions or
 * to give approvals for particular actions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceResponsibilityProperties extends OrganizationalControlProperties
{
    /**
     * Default Constructor
     */
    public GovernanceResponsibilityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceResponsibilityProperties(GovernanceResponsibilityProperties template)
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
        return "GovernanceResponsibilityProperties{" +
                "} " + super.toString();
    }
}
