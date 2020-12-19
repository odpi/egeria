/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
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
public class DataStrategy extends GovernanceDriverProperties
{
    private static final long    serialVersionUID = 1L;

    private List<String> businessImperatives = null;


    /**
     * Default constructor
     */
    public DataStrategy()
    {
        super();
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
                "businessImperatives=" + businessImperatives +
                ", relatedGovernanceDrivers=" + getRelatedGovernanceDrivers() +
                ", governancePolicies=" + getGovernancePolicies() +
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
        DataStrategy that = (DataStrategy) objectToCompare;
        return Objects.equals(businessImperatives, that.businessImperatives);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), businessImperatives);
    }
}
