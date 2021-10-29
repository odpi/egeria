/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage run event as defined in JSON
 * spec https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.  It is used internally in Egeria to pass this information
 * to the Lineage Integrator OMIS's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageRunEvent extends OpenLineageFacet
{
    private String                         eventType;
    private ZonedDateTime                  eventTime;
    private OpenLineageRun                 run;
    private OpenLineageJob                 job;
    private List<OpenLineageInputDataSet>  inputs;
    private List<OpenLineageOutputDataset> outputs;

    /**
     * Default constructor
     */
    public OpenLineageRunEvent()
    {
        super(URI.create("https://openlineage.io/spec/1-0-2/OpenLineage.json#/$defs/RunEvent"));
    }


    /**
     * Return the current transition of the run state. It is required to issue 1 START event and 1 of [ COMPLETE, ABORT, FAIL ] event per run.
     * Additional events with OTHER eventType can be added to the same run. For example to send additional metadata after the run is complete.
     *
     * @return string ("START", "COMPLETE", "ABORT", "FAIL", "OTHER")
     */
    public String getEventType()
    {
        return eventType;
    }


    /**
     * Set up the current transition of the run state. It is required to issue 1 START event and 1 of [ COMPLETE, ABORT, FAIL ] event per run.
     * Additional events with OTHER eventType can be added to the same run. For example to send additional metadata after the run is complete.
     *
     * @param eventType string ("START", "COMPLETE", "ABORT", "FAIL", "OTHER")
     */
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the time the event occurred at.
     *
     * @return zoned time
     */
    public ZonedDateTime getEventTime()
    {
        return eventTime;
    }


    /**
     * Set up the time the event occurred at.
     *
     * @param eventTime zoned time
     */
    public void setEventTime(ZonedDateTime eventTime)
    {
        this.eventTime = eventTime;
    }


    /**
     * Return the details of the job run.
     *
     * @return run structure
     */
    public OpenLineageRun getRun()
    {
        return run;
    }


    /**
     * Set up details of the run.
     *
     * @param run run structure
     */
    public void setRun(OpenLineageRun run)
    {
        this.run = run;
    }


    /**
     * Return the description of the job.
     *
     * @return job structure
     */
    public OpenLineageJob getJob()
    {
        return job;
    }


    /**
     * Set up the description of the job.
     *
     * @param job job structure
     */
    public void setJob(OpenLineageJob job)
    {
        this.job = job;
    }


    /**
     * Return the set of input data sets.
     *
     * @return list of data set descriptions
     */
    public List<OpenLineageInputDataSet> getInputs()
    {
        return inputs;
    }


    /**
     * Set up the set of output data sets.
     *
     * @param inputs list of data set descriptions
     */
    public void setInputs(List<OpenLineageInputDataSet> inputs)
    {
        this.inputs = inputs;
    }


    /**
     * Return the set of output data sets.
     *
     * @return list of data set descriptions
     */
    public List<OpenLineageOutputDataset> getOutputs()
    {
        return outputs;
    }


    /**
     * Set up the list of output data sets.
     *
     * @param outputs list of data set descriptions
     */
    public void setOutputs(List<OpenLineageOutputDataset> outputs)
    {
        this.outputs = outputs;
    }
}
