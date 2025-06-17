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
 * A governance obligation defines a policy that describes a requirement, often coming from regulations,
 * or from the organization's core values, that must be met.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceObligationProperties extends GovernancePolicyProperties
{
    /**
     * Default Constructor
     */
    public GovernanceObligationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_OBLIGATION.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceObligationProperties(GovernanceObligationProperties template)
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
        return "GovernanceObligationProperties{" +
                "} " + super.toString();
    }
}
