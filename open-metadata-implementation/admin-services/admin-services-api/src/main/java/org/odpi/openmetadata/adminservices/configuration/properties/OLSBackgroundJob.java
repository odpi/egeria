/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OLSBackgroundJob {
    private String jobName;
    private int jobInterval;
    private boolean jobEnabled = true;
    private String jobDefaultValue;

    public OLSBackgroundJob() {
    }

    public OLSBackgroundJob(String jobName, int jobInterval, boolean jobEnabled, String jobDefaultValue) {
        this.jobName = jobName;
        this.jobInterval = jobInterval;
        this.jobEnabled = jobEnabled;
        this.jobDefaultValue = jobDefaultValue;
    }

    /**
     * Gets job name.
     *
     * @return the job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets job name.
     *
     * @param jobName the job name
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Gets job interval.
     *
     * @return the job interval
     */
    public int getJobInterval() {
        return jobInterval;
    }

    /**
     * Sets job interval.
     *
     * @param jobInterval the job interval
     */
    public void setJobInterval(int jobInterval) {
        this.jobInterval = jobInterval;
    }

    /**
     * Is job enabled.
     *
     * @return if the job is enabled or not
     */
    public boolean isJobEnabled() {
        return jobEnabled;
    }

    /**
     * Sets job enabled value.
     *
     * @param jobEnabled true/false for enabling/disabling the job
     */
    public void setJobEnabled(boolean jobEnabled) {
        this.jobEnabled = jobEnabled;
    }

    /**
     * Gets job default value.
     *
     * @return the job default value
     */
    public String getJobDefaultValue() {
        return jobDefaultValue;
    }

    /**
     * Sets job default value.
     *
     * @param jobDefaultValue the job default value
     */
    public void setJobDefaultValue(String jobDefaultValue) {
        this.jobDefaultValue = jobDefaultValue;
    }

    @Override
    public String toString() {
        return "OLSBackgroundJob{" +
                "jobName=" + jobName +
                ", jobInterval='" + jobInterval + '\'' +
                ", jobEnabled='" + jobEnabled + '\'' +
                ", jobDefaultValue='" + jobDefaultValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OLSBackgroundJob that = (OLSBackgroundJob) o;
        return Objects.equals(jobName, that.jobName) &&
                jobInterval == that.jobInterval &&
                jobEnabled == that.jobEnabled &&
                Objects.equals(jobDefaultValue, that.jobDefaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName, jobInterval, jobEnabled, jobDefaultValue);
    }
}
