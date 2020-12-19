/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDriverProperties documents the strategic or regulatory requirement that is driving an aspect of the
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
public abstract class GovernanceDriverProperties extends GovernanceDefinitionProperties
{
    private static final long    serialVersionUID = 1L;

    private List<GovernanceRelationship> relatedGovernanceDrivers = null;
    private List<GovernanceRelationship> governancePolicies       = null;

    /**
     * Default Constructor
     */
    public GovernanceDriverProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDriverProperties(GovernanceDriverProperties template)
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
        return "GovernanceDriverProperties{" +
                "relatedGovernanceDrivers=" + relatedGovernanceDrivers +
                ", governancePolicies=" + governancePolicies +
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



    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceDriverProperties that = (GovernanceDriverProperties) objectToCompare;
        return Objects.equals(relatedGovernanceDrivers, that.relatedGovernanceDrivers) &&
                       Objects.equals(governancePolicies, that.governancePolicies);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedGovernanceDrivers, governancePolicies);
    }
}
