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
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an OpenLineage dataset event.  A dataset event describes a dataset
 * independently of any job run, for example when a dataset is created or its schema changes.  It follows
 * https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/DatasetEvent.  It is used internally in Egeria to pass
 * this information to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetEvent
{
    private String                   eventTime = null;
    private URI                      producer = null;
    private URI                      schemaURL = URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/DatasetEvent");
    private OpenLineageStaticDataSet dataset = null;
    private Map<String, Object>      additionalProperties = null;


    /**
     * Default constructor
     */
    public OpenLineageDataSetEvent()
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
     * Return the dataset that the event describes.
     *
     * @return value
     */
    public OpenLineageStaticDataSet getDataset()
    {
        return dataset;
    }


    /**
     * Set up the dataset that the event describes.
     *
     * @param dataset value
     */
    public void setDataset(OpenLineageStaticDataSet dataset)
    {
        this.dataset = dataset;
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
        return "OpenLineageDataSetEvent{" +
                       "eventTime='" + eventTime + '\'' +
                       ", producer=" + producer +
                       ", schemaURL=" + schemaURL +
                       ", dataset=" + dataset +
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
        OpenLineageDataSetEvent that = (OpenLineageDataSetEvent) objectToCompare;
        return Objects.equals(eventTime, that.eventTime) &&
                       Objects.equals(producer, that.producer) &&
                       Objects.equals(schemaURL, that.schemaURL) &&
                       Objects.equals(dataset, that.dataset) &&
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
        return Objects.hash(eventTime, producer, schemaURL, dataset, additionalProperties);
    }
}
