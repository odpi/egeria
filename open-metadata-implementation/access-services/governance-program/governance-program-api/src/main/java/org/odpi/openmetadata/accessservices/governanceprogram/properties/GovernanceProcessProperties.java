/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance process is an automated multi-step process that coordinates people and other processing
 * in order to implement all or part of a policy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceProcessProperties extends TechnicalControl
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public GovernanceProcessProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceProcessProperties(GovernanceProcessProperties template)
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
        return "GovernanceProcessProperties{" +
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
