/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataEngineEventHeader provides a common base for all events from the Data Engine access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataEngineRegistrationEvent.class, name = "DataEngineRegistrationEvent"),
        @JsonSubTypes.Type(value = LineageMappingsEvent.class, name = "LineageMappingsEvent"),
        @JsonSubTypes.Type(value = PortAliasEvent.class, name = "PortAliasEvent"),
        @JsonSubTypes.Type(value = PortImplementationEvent.class, name = "PortImplementationEvent"),
        @JsonSubTypes.Type(value = ProcessesEvent.class, name = "ProcessesEvent"),
        @JsonSubTypes.Type(value = ProcessListEvent.class, name = "ProcessListEvent"),
        @JsonSubTypes.Type(value = SchemaTypeEvent.class, name = "SchemaTypeEvent"),
        @JsonSubTypes.Type(value = ProcessHierarchyEvent.class, name = "ProcessHierarchyEvent")
})
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public abstract class DataEngineEventHeader {

    /**
     * Event version ID
     * -- GETTER --
     * Gets the event version ID
     * @return the event version ID
     * -- SETTER --
     * Sets the event version ID
     * @param eventVersionId the event version ID
     */
    private long eventVersionId = 1L;

    /* different data engine event types */
    /**
     * The Data Engine event type
     * -- GETTER --
     * Gets the Data Engine event type
     * @return the Data Engine event type
     * -- SETTER --
     * Sets the Data Engine event type
     * @param eventType the Data Engine event type
     */
    private DataEngineEventType eventType = null;

    /**
     * The external source type unique name
     * -- GETTER --
     * Gets the external source type unique name
     * @return the external source type unique name
     * -- SETTER --
     * Sets the external source type unique name
     * @param externalSourceName the external source type unique name
     */
    private String externalSourceName;

    /**
     * Producer user ID
     * -- GETTER --
     * Gets user ID
     * @return the user ID
     * -- SETTER --
     * Sets the user ID
     * @param userId the user ID
     */
    private String userId;

}
