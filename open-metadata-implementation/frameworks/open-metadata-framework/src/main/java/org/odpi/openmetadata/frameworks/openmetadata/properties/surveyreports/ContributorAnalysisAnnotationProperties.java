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
    private Integer busFactor                  = null;
    private Long    totalContributorCount      = null;
    private Long    activeContributorCount     = null;
    private Long    commitCount                = null;
    private Long    activeCommitCount          = null;
    private Long    issueCount                 = null;
    private Long    activeIssueCount           = null;
    private Long    contributionCount          = null;
    private Long    activeContributionCount    = null;
    private Long    copyCount                  = null;
    private Long    activeCopyCount            = null;
    private Long    stargazerCount             = null;


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
    public Integer getBusFactor()
    {
        return busFactor;
    }


    /**
     * Set up the minimum number of team members that have to suddenly disappear from a project before the project stalls due to lack of knowledgeable or competent personnel.
     *
     * @param busFactor int
     */
    public void setBusFactor(Integer busFactor)
    {
        this.busFactor = busFactor;
    }


    /**
     * Return the number of distinct contributors to the repository during its lifetime.
     *
     * @return long
     */
    public Long getTotalContributorCount()
    {
        return totalContributorCount;
    }


    /**
     * Set up the number of distinct contributors to the repository during its lifetime.
     *
     * @param totalContributorCount long
     */
    public void setTotalContributorCount(Long totalContributorCount)
    {
        this.totalContributorCount = totalContributorCount;
    }


    /**
     * Return the number of distinct contributors to the repository in the last year.
     *
     * @return long
     */
    public Long getActiveContributorCount()
    {
        return activeContributorCount;
    }


    /**
     * Set up the number of distinct contributors to the repository in the last year.
     *
     * @param activeContributorCount long
     */
    public void setActiveContributorCount(Long activeContributorCount)
    {
        this.activeContributorCount = activeContributorCount;
    }


    /**
     * Return the number of commits to the repository during its lifetime.
     *
     * @return long
     */
    public Long getCommitCount()
    {
        return commitCount;
    }


    /**
     * Set up the number of commits to the repository during its lifetime.
     *
     * @param commitCount long
     */
    public void setCommitCount(Long commitCount)
    {
        this.commitCount = commitCount;
    }


    /**
     * Return the number of commits to the repository in the last year.
     *
     * @return long
     */
    public Long getActiveCommitCount()
    {
        return activeCommitCount;
    }


    /**
     * Set up the number of commits to the repository in the last year.
     *
     * @param activeCommitCount long
     */
    public void setActiveCommitCount(Long activeCommitCount)
    {
        this.activeCommitCount = activeCommitCount;
    }


    /**
     * Return the number of issues reported in the repository during its lifetime.
     *
     * @return long
     */
    public Long getIssueCount()
    {
        return issueCount;
    }


    /**
     * Set up the number of issues reported in the repository during its lifetime.
     *
     * @param issueCount long
     */
    public void setIssueCount(Long issueCount)
    {
        this.issueCount = issueCount;
    }


    /**
     * Return the number of issues reported in the repository in the last year.
     *
     * @return long
     */
    public Long getActiveIssueCount()
    {
        return activeIssueCount;
    }


    /**
     * Set up the number of issues reported in the repository in the last year.
     *
     * @param activeIssueCount long
     */
    public void setActiveIssueCount(Long activeIssueCount)
    {
        this.activeIssueCount = activeIssueCount;
    }


    /**
     * Return the number of pull requests opened in the repository during its lifetime.
     *
     * @return long
     */
    public Long getContributionCount()
    {
        return contributionCount;
    }


    /**
     * Set up the number of pull requests opened in the repository during its lifetime.
     *
     * @param contributionCount long
     */
    public void setContributionCount(Long contributionCount)
    {
        this.contributionCount = contributionCount;
    }


    /**
     * Return the number of pull requests opened in the repository in the last year.
     *
     * @return long
     */
    public Long getActiveContributionCount()
    {
        return activeContributionCount;
    }


    /**
     * Set up the number of pull requests opened in the repository in the last year.
     *
     * @param activeContributionCount long
     */
    public void setActiveContributionCount(Long activeContributionCount)
    {
        this.activeContributionCount = activeContributionCount;
    }


    /**
     * Return the number of forks of the repository during its lifetime.
     *
     * @return long
     */
    public Long getCopyCount()
    {
        return copyCount;
    }


    /**
     * Set up the number of forks of the repository during its lifetime.
     *
     * @param copyCount long
     */
    public void setCopyCount(Long copyCount)
    {
        this.copyCount = copyCount;
    }


    /**
     * Return the number of forks of the repository in the last year.
     *
     * @return long
     */
    public Long getActiveCopyCount()
    {
        return activeCopyCount;
    }


    /**
     * Set up the number of forks of the repository in the last year.
     *
     * @param activeCopyCount long
     */
    public void setActiveCopyCount(Long activeCopyCount)
    {
        this.activeCopyCount = activeCopyCount;
    }


    /**
     * Return the number of people who have starred the repository during its lifetime.
     *
     * @return long
     */
    public Long getStargazerCount()
    {
        return stargazerCount;
    }


    /**
     * Set up the number of people who have starred the repository during its lifetime.
     *
     * @param stargazerCount long
     */
    public void setStargazerCount(Long stargazerCount)
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
        return Objects.equals(busFactor, that.busFactor) &&
               Objects.equals(totalContributorCount, that.totalContributorCount) &&
               Objects.equals(activeContributorCount, that.activeContributorCount) &&
               Objects.equals(commitCount, that.commitCount) &&
               Objects.equals(activeCommitCount, that.activeCommitCount) &&
               Objects.equals(issueCount, that.issueCount) &&
               Objects.equals(activeIssueCount, that.activeIssueCount) &&
               Objects.equals(contributionCount, that.contributionCount) &&
               Objects.equals(activeContributionCount, that.activeContributionCount) &&
               Objects.equals(copyCount, that.copyCount) &&
               Objects.equals(activeCopyCount, that.activeCopyCount) &&
               Objects.equals(stargazerCount, that.stargazerCount);
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
