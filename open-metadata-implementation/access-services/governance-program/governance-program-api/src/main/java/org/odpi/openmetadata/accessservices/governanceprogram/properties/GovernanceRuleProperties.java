/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance rule is an automated component that runs in the production environment and implements all or
 * part of a policy
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceRuleProperties extends TechnicalControl
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public GovernanceRuleProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRuleProperties(GovernanceRuleProperties template)
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
        return "GovernanceRuleProperties{" +
                       "relatedGovernanceDrivers=" + getRelatedGovernanceDrivers() +
                       ", governanceControls=" + getGovernanceControls() +
                       ", title='" + getTitle() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", scope='" + getScope() + '\'' +
                       ", status=" + getStatus() +
                       ", priority='" + getPriority() + '\'' +
                       ", implications=" + getImplications() +
                       ", outcomes=" + getOutcomes() +
                       ", governanceMetrics=" + getGovernanceMetrics() +
                       ", governanceZones=" + getGovernanceZones() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
