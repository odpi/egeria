/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfidenceGovernanceClassification defines the level of confidence that should be placed in the accuracy of related data items.
 * This limits the scope that the data can be used in.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfidenceGovernanceClassification extends GovernanceClassificationBase
{
    private static final long     serialVersionUID = 1L;

    private int confidenceLevel = 0;

    /**
     * Default constructor
     */
    public ConfidenceGovernanceClassification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ConfidenceGovernanceClassification(ConfidenceGovernanceClassification template)
    {
        super(template);

        if (template != null)
        {
            confidenceLevel = template.getConfidenceLevel();
        }
    }


    /**
     * Return the level of confidence in the quality of this data.
     *
     * @return int
     */
    public int getConfidenceLevel()
    {
        return confidenceLevel;
    }


    /**
     * Set up the level of confidence in the quality of this data.
     *
     * @param confidenceLevel int
     */
    public void setConfidenceLevel(int confidenceLevel)
    {
        this.confidenceLevel = confidenceLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfidenceGovernanceClassification{" +
                "confidenceLevel=" + confidenceLevel +
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
        ConfidenceGovernanceClassification that = (ConfidenceGovernanceClassification) objectToCompare;
        return confidenceLevel == that.confidenceLevel;
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), confidenceLevel);
    }
}
