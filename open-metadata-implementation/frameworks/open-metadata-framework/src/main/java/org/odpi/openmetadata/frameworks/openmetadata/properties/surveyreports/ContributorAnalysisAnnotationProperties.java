/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContributorAnalysisAnnotationProperties describes the level of activity around a code repository like GitHub.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContributorAnalysisAnnotationProperties extends AnnotationProperties
{
    private int     busFactor                  = 0;
    private long    totalContributorCount      = 0;
    private long    activeContributorCount     = 0;
    private long    commitCount                = 0;
    private long    activeCommitCount          = 0;
    private long    issueCount                 = 0;
    private long    activeIssueCount           = 0;
    private long    contributionCount          = 0;
    private long    activeContributionCount    = 0;
    private long    copyCount                  = 0;
    private long    activeCopyCount            = 0;
    private long    stargazerCount             = 0;


    /**
     * Default constructor
     */
    public ContributorAnalysisAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTRIBUTOR_ANALYSIS_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ContributorAnalysisAnnotationProperties(ContributorAnalysisAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.busFactor = template.getBusFactor();
            this.totalContributorCount = template.getTotalContributorCount();
            this.activeContributorCount = template.getActiveContributorCount();
            this.commitCount = template.getCommitCount();
            this.activeCommitCount = template.getActiveCommitCount();
            this.issueCount = template.getIssueCount();
            this.activeIssueCount = template.getActiveIssueCount();
            this.contributionCount = template.getContributionCount();
            this.activeContributionCount = template.getActiveContributionCount();
            this.copyCount = template.getCopyCount();
            this.activeCopyCount = template.getActiveCopyCount();
            this.stargazerCount = template.getStargazerCount();
        }
    }


    /**
     * Return the minimum number of team members that have to suddenly disappear from a project before the project stalls due to lack of knowledgeable or competent personnel.
     *
     * @return int
     */
    public int getBusFactor()
    {
        return busFactor;
    }


    /**
     * Set up the minimum number of team members that have to suddenly disappear from a project before the project stalls due to lack of knowledgeable or competent personnel.
     *
     * @param busFactor int
     */
    public void setBusFactor(int busFactor)
    {
        this.busFactor = busFactor;
    }


    /**
     * Return the number of distinct contributors to the repository during its lifetime.
     *
     * @return long
     */
    public long getTotalContributorCount()
    {
        return totalContributorCount;
    }


    /**
     * Set up the number of distinct contributors to the repository during its lifetime.
     *
     * @param totalContributorCount long
     */
    public void setTotalContributorCount(long totalContributorCount)
    {
        this.totalContributorCount = totalContributorCount;
    }


    /**
     * Return the number of distinct contributors to the repository in the last year.
     *
     * @return long
     */
    public long getActiveContributorCount()
    {
        return activeContributorCount;
    }


    /**
     * Set up the number of distinct contributors to the repository in the last year.
     *
     * @param activeContributorCount long
     */
    public void setActiveContributorCount(long activeContributorCount)
    {
        this.activeContributorCount = activeContributorCount;
    }


    /**
     * Return the number of commits to the repository during its lifetime.
     *
     * @return long
     */
    public long getCommitCount()
    {
        return commitCount;
    }


    /**
     * Set up the number of commits to the repository during its lifetime.
     *
     * @param commitCount long
     */
    public void setCommitCount(long commitCount)
    {
        this.commitCount = commitCount;
    }


    /**
     * Return the number of commits to the repository in the last year.
     *
     * @return long
     */
    public long getActiveCommitCount()
    {
        return activeCommitCount;
    }


    /**
     * Set up the number of commits to the repository in the last year.
     *
     * @param activeCommitCount long
     */
    public void setActiveCommitCount(long activeCommitCount)
    {
        this.activeCommitCount = activeCommitCount;
    }


    /**
     * Return the number of issues reported in the repository during its lifetime.
     *
     * @return long
     */
    public long getIssueCount()
    {
        return issueCount;
    }


    /**
     * Set up the number of issues reported in the repository during its lifetime.
     *
     * @param issueCount long
     */
    public void setIssueCount(long issueCount)
    {
        this.issueCount = issueCount;
    }


    /**
     * Return the number of issues reported in the repository in the last year.
     *
     * @return long
     */
    public long getActiveIssueCount()
    {
        return activeIssueCount;
    }


    /**
     * Set up the number of issues reported in the repository in the last year.
     *
     * @param activeIssueCount long
     */
    public void setActiveIssueCount(long activeIssueCount)
    {
        this.activeIssueCount = activeIssueCount;
    }


    /**
     * Return the number of pull requests opened in the repository during its lifetime.
     *
     * @return long
     */
    public long getContributionCount()
    {
        return contributionCount;
    }


    /**
     * Set up the number of pull requests opened in the repository during its lifetime.
     *
     * @param contributionCount long
     */
    public void setContributionCount(long contributionCount)
    {
        this.contributionCount = contributionCount;
    }


    /**
     * Return the number of pull requests opened in the repository in the last year.
     *
     * @return long
     */
    public long getActiveContributionCount()
    {
        return activeContributionCount;
    }


    /**
     * Set up the number of pull requests opened in the repository in the last year.
     *
     * @param activeContributionCount long
     */
    public void setActiveContributionCount(long activeContributionCount)
    {
        this.activeContributionCount = activeContributionCount;
    }


    /**
     * Return the number of forks of the repository during its lifetime.
     *
     * @return long
     */
    public long getCopyCount()
    {
        return copyCount;
    }


    /**
     * Set up the number of forks of the repository during its lifetime.
     *
     * @param copyCount long
     */
    public void setCopyCount(long copyCount)
    {
        this.copyCount = copyCount;
    }


    /**
     * Return the number of forks of the repository in the last year.
     *
     * @return long
     */
    public long getActiveCopyCount()
    {
        return activeCopyCount;
    }


    /**
     * Set up the number of forks of the repository in the last year.
     *
     * @param activeCopyCount long
     */
    public void setActiveCopyCount(long activeCopyCount)
    {
        this.activeCopyCount = activeCopyCount;
    }


    /**
     * Return the number of people who have starred the repository during its lifetime.
     *
     * @return long
     */
    public long getStargazerCount()
    {
        return stargazerCount;
    }


    /**
     * Set up the number of people who have starred the repository during its lifetime.
     *
     * @param stargazerCount long
     */
    public void setStargazerCount(long stargazerCount)
    {
        this.stargazerCount = stargazerCount;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ContributorAnalysisAnnotationProperties{" +
                "busFactor=" + busFactor +
                ", totalContributorCount=" + totalContributorCount +
                ", activeContributorCount=" + activeContributorCount +
                ", commitCount=" + commitCount +
                ", activeCommitCount=" + activeCommitCount +
                ", issueCount=" + issueCount +
                ", activeIssueCount=" + activeIssueCount +
                ", contributionCount=" + contributionCount +
                ", activeContributionCount=" + activeContributionCount +
                ", copyCount=" + copyCount +
                ", activeCopyCount=" + activeCopyCount +
                ", stargazerCount=" + stargazerCount +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ContributorAnalysisAnnotationProperties that = (ContributorAnalysisAnnotationProperties) objectToCompare;
        return busFactor == that.busFactor &&
               totalContributorCount == that.totalContributorCount &&
               activeContributorCount == that.activeContributorCount &&
               commitCount == that.commitCount &&
               activeCommitCount == that.activeCommitCount &&
               issueCount == that.issueCount &&
               activeIssueCount == that.activeIssueCount &&
               contributionCount == that.contributionCount &&
               activeContributionCount == that.activeContributionCount &&
               copyCount == that.copyCount &&
               activeCopyCount == that.activeCopyCount &&
               stargazerCount == that.stargazerCount;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), busFactor, totalContributorCount, activeContributorCount, commitCount, activeCommitCount, issueCount, activeIssueCount, contributionCount, activeContributionCount, copyCount, activeCopyCount, stargazerCount);
    }
}
