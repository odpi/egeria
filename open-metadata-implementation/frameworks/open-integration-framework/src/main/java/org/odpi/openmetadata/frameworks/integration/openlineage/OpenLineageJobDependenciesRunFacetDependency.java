/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single upstream or downstream job dependency.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobDependenciesRunFacetDependency
{
    private OpenLineageJobDependenciesRunFacetJobIdentifier job = null;
    private OpenLineageJobDependenciesRunFacetRunIdentifier run = null;
    private String                                          dependencyType = null;
    private String                                          sequenceTriggerRule = null;
    private String                                          statusTriggerRule = null;


    /**
     * Default constructor
     */
    public OpenLineageJobDependenciesRunFacetDependency()
    {
    }


    /**
     * Return the job that this run depends on, or that depends on this run.
     *
     * @return bean
     */
    public OpenLineageJobDependenciesRunFacetJobIdentifier getJob()
    {
        return job;
    }


    /**
     * Set up the job that this run depends on, or that depends on this run.
     *
     * @param job bean
     */
    public void setJob(OpenLineageJobDependenciesRunFacetJobIdentifier job)
    {
        this.job = job;
    }


    /**
     * Return the specific run of the job, if known.
     *
     * @return bean
     */
    public OpenLineageJobDependenciesRunFacetRunIdentifier getRun()
    {
        return run;
    }


    /**
     * Set up the specific run of the job, if known.
     *
     * @param run bean
     */
    public void setRun(OpenLineageJobDependenciesRunFacetRunIdentifier run)
    {
        this.run = run;
    }


    /**
     * Return the whether the upstream job directly triggers the downstream job (TRIGGER) or the dependency is implicit (DEPENDENCY).
     *
     * @return string
     */
    @JsonProperty("dependency_type")
    public String getDependencyType()
    {
        return dependencyType;
    }


    /**
     * Set up the whether the upstream job directly triggers the downstream job (TRIGGER) or the dependency is implicit (DEPENDENCY).
     *
     * @param dependencyType string
     */
    @JsonProperty("dependency_type")
    public void setDependencyType(String dependencyType)
    {
        this.dependencyType = dependencyType;
    }


    /**
     * Return the sequence condition on which the downstream job can be executed, for example FINISH_TO_START.
     *
     * @return string
     */
    @JsonProperty("sequence_trigger_rule")
    public String getSequenceTriggerRule()
    {
        return sequenceTriggerRule;
    }


    /**
     * Set up the sequence condition on which the downstream job can be executed, for example FINISH_TO_START.
     *
     * @param sequenceTriggerRule string
     */
    @JsonProperty("sequence_trigger_rule")
    public void setSequenceTriggerRule(String sequenceTriggerRule)
    {
        this.sequenceTriggerRule = sequenceTriggerRule;
    }


    /**
     * Return the status of the upstream job that permits the downstream job to run, for example ALL_SUCCESS.
     *
     * @return string
     */
    @JsonProperty("status_trigger_rule")
    public String getStatusTriggerRule()
    {
        return statusTriggerRule;
    }


    /**
     * Set up the status of the upstream job that permits the downstream job to run, for example ALL_SUCCESS.
     *
     * @param statusTriggerRule string
     */
    @JsonProperty("status_trigger_rule")
    public void setStatusTriggerRule(String statusTriggerRule)
    {
        this.statusTriggerRule = statusTriggerRule;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobDependenciesRunFacetDependency{" +
                       "job=" + job +
                       ", run=" + run +
                       ", dependencyType='" + dependencyType + '\'' +
                       ", sequenceTriggerRule='" + sequenceTriggerRule + '\'' +
                       ", statusTriggerRule='" + statusTriggerRule + '\'' +
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
        OpenLineageJobDependenciesRunFacetDependency that = (OpenLineageJobDependenciesRunFacetDependency) objectToCompare;
        return Objects.equals(job, that.job) &&
                       Objects.equals(run, that.run) &&
                       Objects.equals(dependencyType, that.dependencyType) &&
                       Objects.equals(sequenceTriggerRule, that.sequenceTriggerRule) &&
                       Objects.equals(statusTriggerRule, that.statusTriggerRule);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(job, run, dependencyType, sequenceTriggerRule, statusTriggerRule);
    }
}
