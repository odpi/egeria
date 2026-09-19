/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the properties for the Promise classification.  It marks an element whose real-world digital
 * resource/artifact has not yet been delivered.  Such an element is only visible in lineage requests
 * (forLineage=true) - it is the counterpart of the Memento classification at the other end of an element's life.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PromiseProperties extends ClassificationBeanProperties
{
    private DeploymentStatus    deploymentStatus            = null;
    private String              userDefinedDeploymentStatus = null;
    private Date                startTime                   = null;
    private Date                dueTime                     = null;
    private Date                lastReviewTime              = null;
    private Date                completionTime              = null;
    private Map<String, String> additionalProperties        = null;


    /**
     * Default constructor
     */
    public PromiseProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROMISE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PromiseProperties(PromiseProperties template)
    {
        super(template);

        if (template != null)
        {
            this.deploymentStatus            = template.getDeploymentStatus();
            this.userDefinedDeploymentStatus = template.getUserDefinedDeploymentStatus();
            this.startTime                   = template.getStartTime();
            this.dueTime                     = template.getDueTime();
            this.lastReviewTime              = template.getLastReviewTime();
            this.completionTime              = template.getCompletionTime();
            this.additionalProperties        = template.getAdditionalProperties();
        }
    }


    /**
     * Return the current status of the promised digital resource/artifact.
     *
     * @return status enum
     */
    public DeploymentStatus getDeploymentStatus()
    {
        return deploymentStatus;
    }


    /**
     * Set up the current status of the promised digital resource/artifact.
     *
     * @param deploymentStatus status enum
     */
    public void setDeploymentStatus(DeploymentStatus deploymentStatus)
    {
        this.deploymentStatus = deploymentStatus;
    }


    /**
     * Return the user-defined status that extends the standard deployment statuses.
     *
     * @return string
     */
    public String getUserDefinedDeploymentStatus()
    {
        return userDefinedDeploymentStatus;
    }


    /**
     * Set up the user-defined status that extends the standard deployment statuses.
     *
     * @param userDefinedDeploymentStatus string
     */
    public void setUserDefinedDeploymentStatus(String userDefinedDeploymentStatus)
    {
        this.userDefinedDeploymentStatus = userDefinedDeploymentStatus;
    }


    /**
     * Return the time that work on the promised digital resource/artifact started.
     *
     * @return date
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that work on the promised digital resource/artifact started.
     *
     * @param startTime date
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the time that the promised digital resource/artifact is expected to be delivered.
     *
     * @return date
     */
    public Date getDueTime()
    {
        return dueTime;
    }


    /**
     * Set up the time that the promised digital resource/artifact is expected to be delivered.
     *
     * @param dueTime date
     */
    public void setDueTime(Date dueTime)
    {
        this.dueTime = dueTime;
    }


    /**
     * Return the time that progress on the delivery was last reviewed.
     *
     * @return date
     */
    public Date getLastReviewTime()
    {
        return lastReviewTime;
    }


    /**
     * Set up the time that progress on the delivery was last reviewed.
     *
     * @param lastReviewTime date
     */
    public void setLastReviewTime(Date lastReviewTime)
    {
        this.lastReviewTime = lastReviewTime;
    }


    /**
     * Return the time that the promised digital resource/artifact was delivered.
     *
     * @return date
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the time that the promised digital resource/artifact was delivered.
     *
     * @param completionTime date
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }


    /**
     * Return any additional properties that describe the promise.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional properties that describe the promise.
     *
     * @param additionalProperties map of name value pairs, all strings
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PromiseProperties{" +
                "deploymentStatus=" + deploymentStatus +
                ", userDefinedDeploymentStatus='" + userDefinedDeploymentStatus + '\'' +
                ", startTime=" + startTime +
                ", dueTime=" + dueTime +
                ", lastReviewTime=" + lastReviewTime +
                ", completionTime=" + completionTime +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        PromiseProperties that = (PromiseProperties) objectToCompare;
        return deploymentStatus == that.deploymentStatus &&
                Objects.equals(userDefinedDeploymentStatus, that.userDefinedDeploymentStatus) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(dueTime, that.dueTime) &&
                Objects.equals(lastReviewTime, that.lastReviewTime) &&
                Objects.equals(completionTime, that.completionTime) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentStatus, userDefinedDeploymentStatus, startTime, dueTime,
                            lastReviewTime, completionTime, additionalProperties);
    }
}
