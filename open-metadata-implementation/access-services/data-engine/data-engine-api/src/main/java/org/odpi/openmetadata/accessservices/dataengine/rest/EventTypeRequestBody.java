/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EventTypeRequestBody describes the request body used to create/update event types.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class EventTypeRequestBody extends DataEngineOMASAPIRequestBody {

    /**
     * The event type to be created
     * -- GETTER --
     * Return the event type bean
     *
     * @return the event type
     * -- SETTER --
     * Set up the event type bean
     * @param eventType the event type
     */
    @JsonProperty("eventType")
    private EventType eventType;

    /**
     * The qualified name of the topic
     * -- GETTER --
     * Return the topic qualified name
     *
     * @return String - qualified name of the topic
     * -- SETTER --
     * Set up the qualified name of the topic
     * @param topicQualifiedName of the topic
     */
    private String topicQualifiedName;
}


