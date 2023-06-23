/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
/**
 * Topic is a java bean used to create Topics associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Topic extends Asset {

    /**
     * The description of the topic type
     * -- GETTER --
     * Return a description of the topic type.
     * @return string type name
     * -- SETTER --
     * Set up a description of the topic type.
     * @param topicType string type name
     */
    private String topicType;

    /**
     * The list of event types
     * -- GETTER --
     * Gets event type list.
     * @return the event type list
     * -- SETTER --
     * Sets event type list.
     * @param eventTypes the event type list
     */
    @JsonProperty("eventTypes")
    private List<EventType> eventTypes;
}