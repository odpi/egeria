/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceClassificationProperties defines the properties for a Confidentiality, Confidence, Criticality
 * Governance Action Classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CriticalityProperties.class, name = "CriticalityProperties"),
                @JsonSubTypes.Type(value = ConfidentialityProperties.class, name = "ConfidentialityProperties"),
                @JsonSubTypes.Type(value = ConfidenceProperties.class, name = "ConfidenceProperties"),
                @JsonSubTypes.Type(value = ImpactProperties.class, name = "ImpactProperties"),
        })
public class GovernanceClassificationProperties extends GovernanceClassificationBase
{
    private int levelIdentifier = 0;

    /**
     * Default constructor
     */
    public GovernanceClassificationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GovernanceClassificationProperties(GovernanceClassificationProperties template)
    {
        super(template);

        if (template != null)
        {
            levelIdentifier = template.getLevelIdentifier();
        }
    }


    /**
     * Return the level assigned to this element for this classification.
     *
     * @return int
     */
    public int getLevelIdentifier()
    {
        return levelIdentifier;
    }


    /**
     * Set up the level assigned to this element for this classification.
     *
     * @param levelIdentifier int
     */
    public void setLevelIdentifier(int levelIdentifier)
    {
        this.levelIdentifier = levelIdentifier;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceClassificationProperties{" +
                "levelIdentifier=" + levelIdentifier +
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
        GovernanceClassificationProperties that = (GovernanceClassificationProperties) objectToCompare;
        return levelIdentifier == that.levelIdentifier;
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), levelIdentifier);
    }
}
