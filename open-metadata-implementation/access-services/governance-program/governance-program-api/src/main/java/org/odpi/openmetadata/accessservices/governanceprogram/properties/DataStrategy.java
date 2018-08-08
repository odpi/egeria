/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The DataStrategy defines how data and related activities around data will deliver value to the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataStrategy extends GovernanceDriver
{
    private List<String> businessImperatives = null;


    /**
     * Default constructor
     */
    public DataStrategy()
    {
        super();
        setType("DataStrategy");
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataStrategy(DataStrategy   template)
    {
        super(template);

        if (template != null)
        {
            this.businessImperatives = template.getBusinessImperatives();
        }
    }


    /**
     * Return the description of the business imperative(s) that is/are driving the data strategy.
     * These may be specific outcomes of the business strategy, or specific challenges that the
     * organization faces.
     *
     * @return string description
     */
    public List<String> getBusinessImperatives()
    {
        if (businessImperatives == null)
        {
            return null;
        }
        else if (businessImperatives.isEmpty())
        {
            return null;
        }
        else
        {
            return businessImperatives;
        }
    }


    /**
     * Set up the description of the business imperative(s) that is/are driving the data strategy.
     * These may be specific outcomes of the business strategy, or specific challenges that the
     * organization faces.
     *
     * @param businessImperative string description
     */
    public void setBusinessImperatives(List<String> businessImperative)
    {
        this.businessImperatives = businessImperative;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "DataStrategy{" +
                "businessImperatives='" + businessImperatives + '\'' +
                ", relatedGovernanceDrivers=" + getRelatedGovernanceDrivers() +
                ", governancePolicies=" + getGovernancePolicies() +
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
        if (!(objectToCompare instanceof DataStrategy))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataStrategy that = (DataStrategy) objectToCompare;
        return Objects.equals(getBusinessImperatives(), that.getBusinessImperatives());
    }
}
