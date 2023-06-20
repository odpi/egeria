/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfidentialityGovernanceClassification defines the level of confidentiality of related data items.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfidentialityGovernanceClassification extends GovernanceClassificationBase
{
    private int confidentialityLevel = 0;

    /**
     * Default constructor
     */
    public ConfidentialityGovernanceClassification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ConfidentialityGovernanceClassification(ConfidentialityGovernanceClassification template)
    {
        super(template);

        if (template != null)
        {
            confidentialityLevel = template.getConfidentialityLevel();
        }
    }


    /**
     * Return how confidential this data item is to the organization.
     *
     * @return int defined by the GovernanceConfidentialityLevel
     */
    public int getConfidentialityLevel()
    {
        return confidentialityLevel;
    }


    /**
     * Set up how confidential this data item is to the organization.
     *
     * @param confidentialityLevel int defined by the GovernanceConfidentialityLevel
     */
    public void setConfidentialityLevel(int confidentialityLevel)
    {
        this.confidentialityLevel = confidentialityLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfidentialityGovernanceClassification{" +
                "confidentialityLevel=" + confidentialityLevel +
                ", status=" + getStatus() +
                ", confidence=" + getConfidence() +
                ", steward='" + getSteward() + '\'' +
                ", source='" + getSource() + '\'' +
                ", notes='" + getNotes() +
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
        ConfidentialityGovernanceClassification that = (ConfidentialityGovernanceClassification) objectToCompare;
        return confidentialityLevel == that.confidentialityLevel;
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), confidentialityLevel);
    }
}
