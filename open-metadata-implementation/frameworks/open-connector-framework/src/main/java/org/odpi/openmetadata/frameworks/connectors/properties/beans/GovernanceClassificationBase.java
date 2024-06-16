/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceClassificationBase defines the common properties for the governance action classifications
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConfidenceGovernanceClassification.class, name = "ConfidenceGovernanceClassification"),
                @JsonSubTypes.Type(value = ConfidentialityGovernanceClassification.class, name = "ConfidentialityGovernanceClassification"),
                @JsonSubTypes.Type(value = CriticalityGovernanceClassification.class, name = "CriticalityGovernanceClassification"),
                @JsonSubTypes.Type(value = RetentionGovernanceClassification.class, name = "RetentionGovernanceClassification"),
        })
public class GovernanceClassificationBase extends ElementClassificationHeader
{
    private int    governanceStatus    = 0;
    private int    confidence          = 0;
    private String steward             = null;
    private String stewardTypeName     = null;
    private String stewardPropertyName = null;
    private String source              = null;
    private String notes               = null;
    private int    levelIdentifier     = 0;


    /**
     * Default constructor
     */
    public GovernanceClassificationBase()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public GovernanceClassificationBase(GovernanceClassificationBase template)
    {
        super(template);

        if (template != null)
        {
            governanceStatus = template.getGovernanceStatus();
            confidence = template.getConfidence();
            steward = template.getSteward();
            stewardTypeName = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            source = template.getSource();
            notes = template.getNotes();
            levelIdentifier = template.getLevelIdentifier();
        }
    }


    /**
     * Return the status of this classification.
     *
     * @return status identifier
     */
    public int getGovernanceStatus()
    {
        return governanceStatus;
    }


    /**
     * Set up the status of the classification.
     *
     * @param governanceStatus enum
     */
    public void setGovernanceStatus(int governanceStatus)
    {
        this.governanceStatus = governanceStatus;
    }


    /**
     * Return the level of confidence in the classification (0=none to 100=excellent).
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the level of confidence in the classification (0=none to 100=excellent).
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the identifier for the person responsible for maintaining this classification.
     *
     * @return string user identifier
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the identifier for the person responsible for maintaining this classification.
     *
     * @param steward string user identifier
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the type name of the element representing the steward.
     *
     * @return string name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type name of the element representing the steward.
     *
     * @param stewardTypeName string name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property identifying the steward.
     *
     * @return string name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property identifying the steward.
     *
     * @param stewardPropertyName string name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return the source of this classification.
     *
     * @return string identifier
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of this classification.
     *
     * @param source string identifier
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return additional information relating to this classification.
     *
     * @return text from the steward(s)
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up additional information relating to this classification.
     *
     * @param notes text from the steward(s)
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * Return the unique identifier that defines the level of the classification that is applied.
     * It may come from our standard enum, or be a set of values defined by the organization.
     *
     * @return int
     */
    public int getLevelIdentifier()
    {
        return levelIdentifier;
    }


    /**
     * Set up the unique identifier that defines the level of the classification that is applied.
     * It may come from our standard enum, or be a set of values defined by the organization.
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
        return "GovernanceClassificationBase{" +
                       "classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", governanceStatus=" + governanceStatus +
                       ", confidence=" + confidence +
                       ", steward='" + steward + '\'' +
                       ", steward='" + steward + '\'' +
                       ", stewardTypeName='" + stewardTypeName + '\'' +
                       ", stewardPropertyName='" + stewardPropertyName + '\'' +
                       ", source='" + source + '\'' +
                       ", notes='" + notes + '\'' +
                       ", levelIdentifier=" + levelIdentifier +
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
        GovernanceClassificationBase that = (GovernanceClassificationBase) objectToCompare;
        return confidence == that.confidence &&
                governanceStatus == that.governanceStatus &&
                       Objects.equals(steward, that.steward) &&
                       Objects.equals(stewardTypeName, that.stewardTypeName) &&
                       Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(source, that.source) &&
                Objects.equals(notes, that.notes);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceStatus, confidence, steward, stewardTypeName, stewardPropertyName, source, notes);
    }
}