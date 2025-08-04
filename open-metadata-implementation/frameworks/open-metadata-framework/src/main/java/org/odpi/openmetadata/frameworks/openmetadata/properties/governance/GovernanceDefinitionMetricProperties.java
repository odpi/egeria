/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDefinitionMetricProperties defines the properties that link a metric to a governance definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionMetricProperties extends RelationshipBeanProperties
{
    private String rationale = null;


    /**
     * Default constructor
     */
    public GovernanceDefinitionMetricProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionMetricProperties(GovernanceDefinitionMetricProperties template)
    {
        super(template);

        if (template != null)
        {
            this.rationale = getRationale();
        }
    }


    /**
     * Return the rationale as to why this metric is a good measure of the activity associated with the
     * governance definition.
     *
     * @return string description
     */
    public String getRationale()
    {
        return rationale;
    }


    /**
     * Set up the rationale as to why this metric is a good measure of the activity associated with the
     * governance definition.
     *
     * @param rationale string description
     */
    public void setRationale(String rationale)
    {
        this.rationale = rationale;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionMetricProperties{" +
                "rationale='" + rationale + '\'' +
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
        GovernanceDefinitionMetricProperties that = (GovernanceDefinitionMetricProperties) objectToCompare;
        return Objects.equals(rationale, that.rationale);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rationale);
    }
}
