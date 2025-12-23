/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceStrategyProperties defines the strategy that is driving governance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceStrategyProperties extends GovernanceDefinitionProperties
{
    private List<String> businessImperatives = null;


    /**
     * Default Constructor
     */
    public GovernanceStrategyProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_STRATEGY.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceStrategyProperties(GovernanceStrategyProperties template)
    {
        super(template);

        if (template != null)
        {
            this.businessImperatives = template.getBusinessImperatives();
        }
    }


    /**
     * Return the business imperatives driving this strategy.
     *
     * @return list
     */
    public List<String> getBusinessImperatives()
    {
        return businessImperatives;
    }


    /**
     * Set up the business imperatives driving this strategy.
     *
     * @param businessImperatives list
     */
    public void setBusinessImperatives(List<String> businessImperatives)
    {
        this.businessImperatives = businessImperatives;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceStrategyProperties{" +
                "businessImperatives=" + businessImperatives +
                "} " + super.toString();
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
        GovernanceStrategyProperties that = (GovernanceStrategyProperties) objectToCompare;
        return Objects.equals(businessImperatives, that.businessImperatives);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), businessImperatives);
    }
}
