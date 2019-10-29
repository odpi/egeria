/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.*;

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
        @JsonSubTypes.Type(value = PortListEvent.class, name = "PortListEvent"),
        @JsonSubTypes.Type(value = ProcessesEvent.class, name = "ProcessesEvent"),
        @JsonSubTypes.Type(value = ProcessListEvent.class, name = "ProcessListEvent")
})
public abstract class DataEngineEventHeader {

    private long eventVersionId = 1L;
    private DataEngineEventType eventType = null;

    public DataEngineEventHeader() {
    }

    public DataEngineEventHeader(long eventVersionId, DataEngineEventType eventType) {
        this.eventVersionId = eventVersionId;
        this.eventType = eventType;
    }

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    public DataEngineEventType getEventType() {
        return eventType;
    }

    public void setEventType(DataEngineEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "DataEngineEventType{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                '}';
    }
}
