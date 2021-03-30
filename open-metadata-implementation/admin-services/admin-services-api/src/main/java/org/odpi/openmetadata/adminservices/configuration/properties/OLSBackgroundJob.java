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

    public OLSBackgroundJob() {
    }

    public OLSBackgroundJob(String jobName, int jobInterval) {
        this.jobName = jobName;
        this.jobInterval = jobInterval;
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

    @Override
    public String toString() {
        return "OLSBackgroundJob{" +
                "jobName=" + jobName +
                ", jobInterval='" + jobInterval + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OLSBackgroundJob that = (OLSBackgroundJob) o;
        return Objects.equals(jobName, that.jobName) &&
                jobInterval == that.jobInterval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName, jobInterval);
    }
}
