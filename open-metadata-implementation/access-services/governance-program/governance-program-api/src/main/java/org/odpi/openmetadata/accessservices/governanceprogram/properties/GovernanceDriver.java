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
                @JsonSubTypes.Type(value = DataStrategy.class, name = "DataStrategy"),
                @JsonSubTypes.Type(value = Regulation.class, name = "Regulation")
        })
public abstract class GovernanceDriver extends GovernanceDefinition
{
    private List<GovernanceRelationship> relatedGovernanceDrivers = null;
    private List<GovernanceRelationship> governancePolicies       = null;

    /**
     * Default Constructor
     */
    public GovernanceDriver()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDriver(GovernanceDriver  template)
    {
        super(template);

        if (template != null)
        {
            relatedGovernanceDrivers = template.getRelatedGovernanceDrivers();
            governancePolicies = template.getGovernancePolicies();
        }
    }


    /**
     * Return the list of governance drivers that are related to this governance driver.
     *
     * @return list of governance drivers
     */
    public List<GovernanceRelationship> getRelatedGovernanceDrivers()
    {
        if (relatedGovernanceDrivers == null)
        {
            return null;
        }
        else if (relatedGovernanceDrivers.isEmpty())
        {
            return null;
        }
        else
        {
            return relatedGovernanceDrivers;
        }
    }


    /**
     * Set up the list of governance drivers that are related to this governance driver.
     *
     * @param relatedGovernanceDrivers list of governance drivers
     */
    public void setRelatedGovernanceDrivers(List<GovernanceRelationship> relatedGovernanceDrivers)
    {
        this.relatedGovernanceDrivers = relatedGovernanceDrivers;
    }


    /**
     * Return the governance policies that have been linked to this governance driver.
     *
     * @return list of governance policy summaries.
     */
    public List<GovernanceRelationship> getGovernancePolicies()
    {
        if (governancePolicies == null)
        {
            return null;
        }
        else if (governancePolicies.isEmpty())
        {
            return null;
        }
        else
        {
            return governancePolicies;
        }
    }


    /**
     * Set up the governance policies that have been linked to this governance driver.
     *
     * @param governancePolicies list of governance policy summaries.
     */
    public void setGovernancePolicies(List<GovernanceRelationship> governancePolicies)
    {
        this.governancePolicies = governancePolicies;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceDriver{" +
                "relatedGovernanceDrivers=" + relatedGovernanceDrivers +
                ", governancePolicies=" + governancePolicies +
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
     * Test the properties of the governance driver to determine if the supplied object is equal to this one.
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
        if (!(objectToCompare instanceof GovernanceDriver))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceDriver that = (GovernanceDriver) objectToCompare;
        return Objects.equals(getRelatedGovernanceDrivers(), that.getRelatedGovernanceDrivers()) &&
                Objects.equals(getGovernancePolicies(), that.getGovernancePolicies());
    }
}
