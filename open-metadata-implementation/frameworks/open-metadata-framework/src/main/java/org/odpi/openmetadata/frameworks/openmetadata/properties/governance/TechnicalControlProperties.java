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
 * A technical control describes an IT implemented function that supports the governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceRuleProperties.class, name = "GovernanceRuleProperties"),
                @JsonSubTypes.Type(value = GovernanceMetricProperties.class, name = "GovernanceMetricProperties"),
                @JsonSubTypes.Type(value = GovernanceActionProperties.class, name = "GovernanceActionProperties"),
                @JsonSubTypes.Type(value = ServiceLevelObjectiveProperties.class, name = "ServiceLevelObjectiveProperties"),
                @JsonSubTypes.Type(value = SecurityAccessControlProperties.class, name = "SecurityAccessControlProperties"),
                @JsonSubTypes.Type(value = SecurityGroupProperties.class, name = "SecurityGroupProperties"),
        })
public class TechnicalControlProperties extends GovernanceControlProperties
{
    /**
     * Default Constructor
     */
    public TechnicalControlProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TECHNICAL_CONTROL.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public TechnicalControlProperties(TechnicalControlProperties template)
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
        return "TechnicalControlProperties{" +
                "} " + super.toString();
    }
}
