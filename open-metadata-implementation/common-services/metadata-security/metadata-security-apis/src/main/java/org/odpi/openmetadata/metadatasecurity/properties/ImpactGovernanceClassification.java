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
 * CriticalityGovernanceClassification defines how critical the related data items are to the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImpactGovernanceClassification extends GovernanceClassificationBase
{
    private static final long     serialVersionUID = 1L;

    private int impactLevel = 0;

    /**
     * Default constructor
     */
    public ImpactGovernanceClassification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ImpactGovernanceClassification(ImpactGovernanceClassification template)
    {
        super(template);

        if (template != null)
        {
            impactLevel = template.getImpactLevel();
        }
    }


    /**
     * Return the definition of how critical this data item is to the organization.
     *
     * @return enum
     */
    public int getImpactLevel()
    {
        return impactLevel;
    }


    /**
     * Set up the definition of how critical this data item is to the organization.
     *
     * @param impactLevel int
     */
    public void setImpactLevel(int impactLevel)
    {
        this.impactLevel = impactLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ImpactGovernanceClassification{" +
                "impactLevel=" + impactLevel +
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
        ImpactGovernanceClassification that = (ImpactGovernanceClassification) objectToCompare;
        return impactLevel == that.impactLevel;
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), impactLevel);
    }
}
