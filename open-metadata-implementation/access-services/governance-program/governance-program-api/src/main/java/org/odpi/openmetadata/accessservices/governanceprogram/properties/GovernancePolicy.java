/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDriver documents the strategic or regulatory requirement that is driving an aspect of the
 * governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernancePrinciple.class, name = "GovernancePrinciple"),
                @JsonSubTypes.Type(value = GovernanceObligation.class, name = "GovernanceObligation"),
                @JsonSubTypes.Type(value = GovernanceApproach.class, name = "GovernanceApproach")
        })
public abstract class GovernancePolicy extends GovernanceDefinition
{
    private List<GovernanceRelationship> governanceDrivers = null;
    private List<GovernanceRelationship> relatedGovernancePolicies = null;
    private List<GovernanceRelationship> governanceControls = null;

    /**
     * Default Constructor
     */
    public GovernancePolicy()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernancePolicy(GovernancePolicy template)
    {
        super(template);

        if (template != null)
        {
            governanceDrivers = template.getGovernanceDrivers();
            relatedGovernancePolicies = template.getRelatedGovernancePolicies();
            governanceControls = template.getGovernanceControls();
        }
    }


    /**
     * Return the list of governance drivers that are requirements for this governance policy.
     *
     * @return list of governance drivers.
     */
    public List<GovernanceRelationship> getGovernanceDrivers()
    {
        if (governanceDrivers == null)
        {
            return null;
        }
        else if (governanceDrivers.isEmpty())
        {
            return null;
        }
        else
        {
            return governanceDrivers;
        }
    }


    /**
     * Set up the list of governance drivers that are requirements for this governance policy.
     *
     * @param governanceDrivers list of governance drivers.
     */
    public void setGovernanceDrivers(List<GovernanceRelationship> governanceDrivers)
    {
        this.governanceDrivers = governanceDrivers;
    }


    /**
     * Return the list of governance policies that are related to this governance policy.
     *
     * @return list of governance policies
     */
    public List<GovernanceRelationship> getRelatedGovernancePolicies()
    {
        if (relatedGovernancePolicies == null)
        {
            return null;
        }
        else if (relatedGovernancePolicies.isEmpty())
        {
            return null;
        }
        else
        {
            return relatedGovernancePolicies;
        }
    }


    /**
     * Set up the list of governance policies that are related to this governance policy.
     *
     * @param relatedGovernancePolicies list of governance policies
     */
    public void setRelatedGovernancePolicies(List<GovernanceRelationship> relatedGovernancePolicies)
    {
        this.relatedGovernancePolicies = relatedGovernancePolicies;
    }


    /**
     * Return the governance controls that have been linked to this governance driver.
     *
     * @return list of governance control summaries.
     */
    public List<GovernanceRelationship> getGovernanceControls()
    {
        if (governanceControls == null)
        {
            return null;
        }
        else if (governanceControls.isEmpty())
        {
            return null;
        }
        else
        {
            return governanceControls;
        }
    }


    /**
     * Set up the governance controls that have been linked to this governance driver.
     *
     * @param governanceControls list of governance control summaries.
     */
    public void setGovernanceControls(List<GovernanceRelationship> governanceControls)
    {
        this.governanceControls = governanceControls;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernancePolicy{" +
                "governanceDrivers=" + governanceDrivers +
                ", relatedGovernancePolicies=" + relatedGovernancePolicies +
                ", governanceControls=" + governanceControls +
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


    /**
     * Test the properties of the DataStrategy to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof GovernancePolicy))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernancePolicy that = (GovernancePolicy) objectToCompare;
        return Objects.equals(getGovernanceDrivers(), that.getGovernanceDrivers()) &&
                Objects.equals(getRelatedGovernancePolicies(), that.getRelatedGovernancePolicies()) &&
                Objects.equals(getGovernanceControls(), that.getGovernanceControls());
    }
}
