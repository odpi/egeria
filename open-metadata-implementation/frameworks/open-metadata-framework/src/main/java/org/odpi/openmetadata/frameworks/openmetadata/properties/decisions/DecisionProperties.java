/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.decisions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DecisionProperties documents a decision that has been made, why it was made, and when it should be reviewed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DecisionProperties extends AuthoredReferenceableProperties
{
    private String       summary        = null;
    private String       scope          = null;
    private String       usage          = null;
    private String       importance     = null;
    private String       disposition    = null;
    private List<String> implications   = null;
    private String       notes          = null;
    private Date         startTime      = null;
    private Date         lastReviewTime = null;
    private Date         nextReviewTime = null;
    private Date         expiryTime     = null;


    /**
     * Default constructor
     */
    public DecisionProperties()
    {
        super();
        super.typeName = OpenMetadataType.DECISION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DecisionProperties(DecisionProperties template)
    {
        super(template);

        if (template != null)
        {
            summary        = template.getSummary();
            scope          = template.getScope();
            usage          = template.getUsage();
            importance     = template.getImportance();
            disposition    = template.getDisposition();
            implications   = template.getImplications();
            notes          = template.getNotes();
            startTime      = template.getStartTime();
            lastReviewTime = template.getLastReviewTime();
            nextReviewTime = template.getNextReviewTime();
            expiryTime     = template.getExpiryTime();
        }
    }


    /**
     * Return the short description of the decision for summary tables.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the short description of the decision for summary tables.
     *
     * @param summary string
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the breadth of impact or coverage of the decision.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the breadth of impact or coverage of the decision.
     *
     * @param scope string
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the guidance on how the decision should be used.
     *
     * @return string
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the guidance on how the decision should be used.
     *
     * @param usage string
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the relative importance of this decision for the organization.
     *
     * @return string
     */
    public String getImportance()
    {
        return importance;
    }


    /**
     * Set up the relative importance of this decision for the organization.
     *
     * @param importance string
     */
    public void setImportance(String importance)
    {
        this.importance = importance;
    }


    /**
     * Return the status of the decision.
     *
     * @return string
     */
    public String getDisposition()
    {
        return disposition;
    }


    /**
     * Set up the status of the decision.
     *
     * @param disposition string
     */
    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }


    /**
     * Return the impact on the organization, people and services when adopting this decision.
     *
     * @return list
     */
    public List<String> getImplications()
    {
        return implications;
    }


    /**
     * Set up the impact on the organization, people and services when adopting this decision.
     *
     * @param implications list
     */
    public void setImplications(List<String> implications)
    {
        this.implications = implications;
    }


    /**
     * Return the notes on why the decision was made.
     *
     * @return string
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the notes on why the decision was made.
     *
     * @param notes string
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Return the expected start time of the decision's implementation.
     *
     * @return date
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the expected start time of the decision's implementation.
     *
     * @param startTime date
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the last time the decision was reviewed.
     *
     * @return date
     */
    public Date getLastReviewTime()
    {
        return lastReviewTime;
    }


    /**
     * Set up the last time the decision was reviewed.
     *
     * @param lastReviewTime date
     */
    public void setLastReviewTime(Date lastReviewTime)
    {
        this.lastReviewTime = lastReviewTime;
    }


    /**
     * Return the next time the decision will be reviewed.
     *
     * @return date
     */
    public Date getNextReviewTime()
    {
        return nextReviewTime;
    }


    /**
     * Set up the next time the decision will be reviewed.
     *
     * @param nextReviewTime date
     */
    public void setNextReviewTime(Date nextReviewTime)
    {
        this.nextReviewTime = nextReviewTime;
    }


    /**
     * Return the time when the decision will be obsolete and should no longer be relied on.
     *
     * @return date
     */
    public Date getExpiryTime()
    {
        return expiryTime;
    }


    /**
     * Set up the time when the decision will be obsolete and should no longer be relied on.
     *
     * @param expiryTime date
     */
    public void setExpiryTime(Date expiryTime)
    {
        this.expiryTime = expiryTime;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DecisionProperties{" +
                "summary='" + summary + '\'' +
                ", scope='" + scope + '\'' +
                ", usage='" + usage + '\'' +
                ", importance='" + importance + '\'' +
                ", disposition='" + disposition + '\'' +
                ", implications=" + implications +
                ", notes='" + notes + '\'' +
                ", startTime=" + startTime +
                ", lastReviewTime=" + lastReviewTime +
                ", nextReviewTime=" + nextReviewTime +
                ", expiryTime=" + expiryTime +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DecisionProperties that = (DecisionProperties) objectToCompare;
        return Objects.equals(summary, that.summary) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(importance, that.importance) &&
                Objects.equals(disposition, that.disposition) &&
                Objects.equals(implications, that.implications) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(lastReviewTime, that.lastReviewTime) &&
                Objects.equals(nextReviewTime, that.nextReviewTime) &&
                Objects.equals(expiryTime, that.expiryTime);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), summary, scope, usage, importance, disposition, implications, notes, startTime, lastReviewTime, nextReviewTime, expiryTime);
    }
}
