/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseType defines a license that the organization recognizes and has governance
 * definitions to support it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionMetric extends GovernanceMetric
{
    private static final long    serialVersionUID = 1L;

    private String   rationale = null;


    /**
     * Default constructor
     */
    public GovernanceDefinitionMetric()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionMetric(GovernanceDefinitionMetric  template)
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
        return "GovernanceDefinitionMetric{" +
                       "rationale='" + rationale + '\'' +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", measurement='" + getMeasurement() + '\'' +
                       ", target='" + getTarget() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        GovernanceDefinitionMetric that = (GovernanceDefinitionMetric) objectToCompare;
        return Objects.equals(rationale, that.rationale);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rationale);
    }
}
