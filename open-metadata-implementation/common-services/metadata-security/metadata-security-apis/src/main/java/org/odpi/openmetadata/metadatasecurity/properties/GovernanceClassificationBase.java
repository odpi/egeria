/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceClassificationBase defines the common properties for the governance action classifications
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceClassificationBase implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int    status     = 0;
    private int    confidence = 0;
    private String steward    = null;
    private String source     = null;
    private String notes      = null;


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
        if (template != null)
        {
            status     = template.getStatus();
            confidence = template.getConfidence();
            steward    = template.getSteward();
            source     = template.getSource();
            notes      = template.getNotes();
        }
    }


    /**
     * Return the status of this classification.
     *
     * @return enum
     */
    public int getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the classification.
     *
     * @param status enum
     */
    public void setStatus(int status)
    {
        this.status = status;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceClassificationBase{" +
                "status=" + status +
                ", confidence=" + confidence +
                ", steward='" + steward + '\'' +
                ", source='" + source + '\'' +
                ", notes='" + notes +
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
                status == that.status &&
                Objects.equals(steward, that.steward) &&
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
        return Objects.hash(super.hashCode(), status, confidence, steward, source, notes);
    }
}