/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the jobDependencies run facet.  It records the job runs that must complete before this run starts, and those that start after it completes.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/JobDependenciesRunFacet.json#/$defs/JobDependenciesRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobDependenciesRunFacet extends OpenLineageRunFacet
{
    private List<OpenLineageJobDependenciesRunFacetDependency> upstream = null;
    private List<OpenLineageJobDependenciesRunFacetDependency> downstream = null;
    private String                                             triggerRule = null;


    /**
     * Default constructor
     */
    public OpenLineageJobDependenciesRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/JobDependenciesRunFacet.json#/$defs/JobDependenciesRunFacet"));
    }


    /**
     * Return the job runs that must complete before the current run can start.
     *
     * @return list
     */
    public List<OpenLineageJobDependenciesRunFacetDependency> getUpstream()
    {
        return upstream;
    }


    /**
     * Set up the job runs that must complete before the current run can start.
     *
     * @param upstream list
     */
    public void setUpstream(List<OpenLineageJobDependenciesRunFacetDependency> upstream)
    {
        this.upstream = upstream;
    }


    /**
     * Return the job runs that will start after completion of the current run.
     *
     * @return list
     */
    public List<OpenLineageJobDependenciesRunFacetDependency> getDownstream()
    {
        return downstream;
    }


    /**
     * Set up the job runs that will start after completion of the current run.
     *
     * @param downstream list
     */
    public void setDownstream(List<OpenLineageJobDependenciesRunFacetDependency> downstream)
    {
        this.downstream = downstream;
    }


    /**
     * Return the condition under which this job will run based on the status of its upstream jobs.
     *
     * @return string
     */
    @JsonProperty("trigger_rule")
    public String getTriggerRule()
    {
        return triggerRule;
    }


    /**
     * Set up the condition under which this job will run based on the status of its upstream jobs.
     *
     * @param triggerRule string
     */
    @JsonProperty("trigger_rule")
    public void setTriggerRule(String triggerRule)
    {
        this.triggerRule = triggerRule;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobDependenciesRunFacet{" +
                       "upstream=" + upstream +
                       ", downstream=" + downstream +
                       ", triggerRule='" + triggerRule + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageJobDependenciesRunFacet that = (OpenLineageJobDependenciesRunFacet) objectToCompare;
        return Objects.equals(upstream, that.upstream) &&
                       Objects.equals(downstream, that.downstream) &&
                       Objects.equals(triggerRule, that.triggerRule);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), upstream, downstream, triggerRule);
    }
}
