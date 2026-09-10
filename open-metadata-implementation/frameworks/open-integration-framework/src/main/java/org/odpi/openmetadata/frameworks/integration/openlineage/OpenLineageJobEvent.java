/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an OpenLineage job event.  A job event describes a job and its inputs and
 * outputs independently of any particular run, for example when a pipeline is deployed.  It follows
 * https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/JobEvent.  It is used internally in Egeria to pass
 * this information to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobEvent
{
    private String                         eventTime = null;
    private URI                            producer = null;
    private URI                            schemaURL = URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/JobEvent");
    private OpenLineageJob                 job = null;
    private List<OpenLineageInputDataSet>  inputs = null;
    private List<OpenLineageOutputDataSet> outputs = null;
    private Map<String, Object>            additionalProperties = null;


    /**
     * Default constructor
     */
    public OpenLineageJobEvent()
    {
    }


    /**
     * Return the time the event occurred at (ISO-8601).
     *
     * @return value
     */
    public String getEventTime()
    {
        return eventTime;
    }


    /**
     * Set up the time the event occurred at (ISO-8601).
     *
     * @param eventTime value
     */
    public void setEventTime(String eventTime)
    {
        this.eventTime = eventTime;
    }


    /**
     * Return the URI identifying the producer of this metadata. For example this could be a git url with a given tag or sha.
     *
     * @return value
     */
    public URI getProducer()
    {
        return producer;
    }


    /**
     * Set up the URI identifying the producer of this metadata. For example this could be a git url with a given tag or sha.
     *
     * @param producer value
     */
    public void setProducer(URI producer)
    {
        this.producer = producer;
    }


    /**
     * Return the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this event.
     *
     * @return value
     */
    public URI getSchemaURL()
    {
        return schemaURL;
    }


    /**
     * Set up the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this event.
     *
     * @param schemaURL value
     */
    public void setSchemaURL(URI schemaURL)
    {
        this.schemaURL = schemaURL;
    }


    /**
     * Return the job that the event describes.
     *
     * @return value
     */
    public OpenLineageJob getJob()
    {
        return job;
    }


    /**
     * Set up the job that the event describes.
     *
     * @param job value
     */
    public void setJob(OpenLineageJob job)
    {
        this.job = job;
    }


    /**
     * Return the set of input datasets.
     *
     * @return value
     */
    public List<OpenLineageInputDataSet> getInputs()
    {
        return inputs;
    }


    /**
     * Set up the set of input datasets.
     *
     * @param inputs value
     */
    public void setInputs(List<OpenLineageInputDataSet> inputs)
    {
        this.inputs = inputs;
    }


    /**
     * Return the set of output datasets.
     *
     * @return value
     */
    public List<OpenLineageOutputDataSet> getOutputs()
    {
        return outputs;
    }


    /**
     * Set up the set of output datasets.
     *
     * @param outputs value
     */
    public void setOutputs(List<OpenLineageOutputDataSet> outputs)
    {
        this.outputs = outputs;
    }


    /**
     * Return any properties that are not modelled by the bean.  They are serialized as top-level properties
     * alongside the modelled properties.
     *
     * @return map of property name to value
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any properties that are not modelled by the bean.
     *
     * @param additionalProperties map of property name to value
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a property that is not modelled by the bean.  Jackson calls this for each unrecognized property found in the JSON.
     *
     * @param propertyName name of the property
     * @param propertyValue value of the property
     */
    @JsonAnySetter
    public void setAdditionalProperty(String propertyName,
                                      Object propertyValue)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new LinkedHashMap<>();
        }

        additionalProperties.put(propertyName, propertyValue);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobEvent{" +
                       "eventTime='" + eventTime + '\'' +
                       ", producer=" + producer +
                       ", schemaURL=" + schemaURL +
                       ", job=" + job +
                       ", inputs=" + inputs +
                       ", outputs=" + outputs +
                       ", additionalProperties=" + additionalProperties +
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
        OpenLineageJobEvent that = (OpenLineageJobEvent) objectToCompare;
        return Objects.equals(eventTime, that.eventTime) &&
                       Objects.equals(producer, that.producer) &&
                       Objects.equals(schemaURL, that.schemaURL) &&
                       Objects.equals(job, that.job) &&
                       Objects.equals(inputs, that.inputs) &&
                       Objects.equals(outputs, that.outputs) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(eventTime, producer, schemaURL, job, inputs, outputs, additionalProperties);
    }
}
