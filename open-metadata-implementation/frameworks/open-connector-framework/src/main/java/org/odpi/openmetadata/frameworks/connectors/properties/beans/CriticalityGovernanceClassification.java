/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CriticalityLevel;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CriticalityGovernanceClassification defines how critical the related data items are to the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CriticalityGovernanceClassification extends GovernanceClassificationBase
{
    private static final long     serialVersionUID = 1L;

    private CriticalityLevel criticalityLevel = null;

    /**
     * Default constructor
     */
    public CriticalityGovernanceClassification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public CriticalityGovernanceClassification(CriticalityGovernanceClassification template)
    {
        super(template);

        if (template != null)
        {
            criticalityLevel = template.getCriticalityLevel();
        }
    }


    /**
     * Return the definition of how critical this data item is to the organization.
     *
     * @return enum
     */
    public CriticalityLevel getCriticalityLevel()
    {
        return criticalityLevel;
    }


    /**
     * Set up the definition of how critical this data item is to the organization.
     *
     * @param criticalityLevel enum
     */
    public void setCriticalityLevel(CriticalityLevel criticalityLevel)
    {
        this.criticalityLevel = criticalityLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CriticalityGovernanceClassification{" +
                       "criticalityLevel=" + criticalityLevel +
                       ", classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", governanceStatus=" + getGovernanceStatus() +
                       ", confidence=" + getConfidence() +
                       ", steward='" + getSteward() + '\'' +
                       ", source='" + getSource() + '\'' +
                       ", notes='" + getNotes() + '\'' +
                       ", levelIdentifier=" + getLevelIdentifier() +
                       ", headerVersion=" + getHeaderVersion() +
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
        CriticalityGovernanceClassification that = (CriticalityGovernanceClassification) objectToCompare;
        return criticalityLevel == that.criticalityLevel;
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), criticalityLevel);
    }
}
