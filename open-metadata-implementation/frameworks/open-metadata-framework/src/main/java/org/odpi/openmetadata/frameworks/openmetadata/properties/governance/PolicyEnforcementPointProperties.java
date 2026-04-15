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
 * Policy enforcement point properties - a policy management classification from XACML.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PolicyEnforcementPointProperties extends PolicyManagementPointProperties
{
    /**
     * Default Constructor
     */
    public PolicyEnforcementPointProperties()
    {
        super();
        super.typeName = OpenMetadataType.POLICY_ENFORCEMENT_POINT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public PolicyEnforcementPointProperties(PolicyEnforcementPointProperties template)
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
        return "PolicyEnforcementPointProperties{" +
                "} " + super.toString();
    }
}
