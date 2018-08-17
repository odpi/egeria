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
                @JsonSubTypes.Type(value = TechnicalControl.class, name = "TechnicalControl"),
                @JsonSubTypes.Type(value = OrganizationalControl.class, name = "OrganizationalControl")
        })
public abstract class GovernanceControl extends GovernanceDefinition
{
    private List<GovernanceRelationship> relatedGovernanceDrivers = null;
    private List<GovernanceRelationship> governanceControls = null;

    /**
     * Default Constructor
     */
    public GovernanceControl()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceControl(GovernanceControl template)
    {
        super(template);

        if (template != null)
        {
            relatedGovernanceDrivers = template.getRelatedGovernanceDrivers();
            governanceControls = template.getGovernanceControls();
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
        return "GovernanceControl{" +
                "relatedGovernanceDrivers=" + relatedGovernanceDrivers +
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
        if (!(objectToCompare instanceof GovernanceControl))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceControl that = (GovernanceControl) objectToCompare;
        return Objects.equals(getRelatedGovernanceDrivers(), that.getRelatedGovernanceDrivers()) &&
                Objects.equals(getGovernanceControls(), that.getGovernanceControls());
    }
}
