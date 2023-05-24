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

import java.io.Serializable;

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
        @JsonSubTypes.Type(value = DataFlowsEvent.class, name = "DataFlowsEvent"),
        @JsonSubTypes.Type(value = PortImplementationEvent.class, name = "PortImplementationEvent"),
        @JsonSubTypes.Type(value = ProcessEvent.class, name = "ProcessEvent"),
        @JsonSubTypes.Type(value = ProcessListEvent.class, name = "ProcessListEvent"),
        @JsonSubTypes.Type(value = SchemaTypeEvent.class, name = "SchemaTypeEvent"),
        @JsonSubTypes.Type(value = ProcessHierarchyEvent.class, name = "ProcessHierarchyEvent"),
        @JsonSubTypes.Type(value = DeleteEvent.class, name = "DeleteEvent"),
        @JsonSubTypes.Type(value = DatabaseEvent.class, name = "DatabaseEvent"),
        @JsonSubTypes.Type(value = DatabaseSchemaEvent.class, name = "DatabaseSchemaEvent"),
        @JsonSubTypes.Type(value = RelationalTableEvent.class, name = "RelationalTableEvent"),
        @JsonSubTypes.Type(value = DataFileEvent.class, name = "DataFileEvent"),
        @JsonSubTypes.Type(value = TopicEvent.class, name = "TopicEvent"),
        @JsonSubTypes.Type(value = EventTypeEvent.class, name = "EventTypeEvent"),
        @JsonSubTypes.Type(value = ProcessingStateEvent.class, name = "ProcessingStateEvent")
})
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public abstract class DataEngineEventHeader implements Serializable {

    /**
     * Serial version UID
     * -- GETTER --
     * Gets the serial version UID
     * @return the serial version UID
     * -- SETTER --
     * Sets the serial version UID
     * @param serialVersionUID the serial version UID
     */
    private static final long serialVersionUID = 1L;

    /* different data engine event types */
    /**
     * The Data Engine event type
     * -- GETTER --
     * Gets the Data Engine event type
     * @return the Data Engine event type
     * -- SETTER --
     * Sets the Data Engine event type
     * @param dataEngineEventType the Data Engine event type
     */
    private DataEngineEventType dataEngineEventType;

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
