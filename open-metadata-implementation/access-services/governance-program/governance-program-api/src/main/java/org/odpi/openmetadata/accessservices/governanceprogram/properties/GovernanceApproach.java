/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceApproach defines a best practice that should be adopted in the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceApproach extends GovernancePolicy
{
    /**
     * Default constructor
     */
    public GovernanceApproach()
    {
        super();
        setType("GovernanceApproach");
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceApproach(GovernanceApproach template)
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
        return "GovernanceApproach{" +
                "governanceDrivers=" + getGovernanceDrivers() +
                ", relatedGovernancePolicies=" + getRelatedGovernancePolicies() +
                ", governanceControls=" + getGovernanceControls() +
                ", description='" + getDescription() + '\'' +
                ", scope='" + getScope() + '\'' +
                ", status=" + getStatus() +
                ", priority='" + getPriority() + '\'' +
                ", implications=" + getImplications() +
                ", outcomes=" + getOutcomes() +
                ", externalReferences=" + getExternalReferences() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", governanceMetrics=" + getGovernanceMetrics() +
                ", governanceZones=" + getGovernanceZones() +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", documentId='" + getDocumentId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", summary='" + getSummary() + '\'' +
                '}';
    }
}
