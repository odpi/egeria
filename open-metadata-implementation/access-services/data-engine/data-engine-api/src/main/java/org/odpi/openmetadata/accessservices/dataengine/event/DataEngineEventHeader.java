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
        @JsonSubTypes.Type(value = ProcessesEvent.class, name = "ProcessesEvent"),
        @JsonSubTypes.Type(value = ProcessListEvent.class, name = "ProcessListEvent"),
        @JsonSubTypes.Type(value = SchemaTypeEvent.class, name = "SchemaTypeEvent"),
        @JsonSubTypes.Type(value = ProcessHierarchyEvent.class, name = "ProcessHierarchyEvent")
})
public abstract class DataEngineEventHeader {

    private long eventVersionId = 1L;

    /* different data engine event types */
    private DataEngineEventType eventType = null;

    /* unique name for the external source */
    private String externalSourceName;

    /* producer user id */
    private String userId ;

    /**
     * Instantiates a new Data engine event header.
     */
    public DataEngineEventHeader() {
    }

    /**
     * Gets event version id.
     *
     * @return the event version id
     */
    public long getEventVersionId() {
        return eventVersionId;
    }

    /**
     * Sets event version id.
     *
     * @param eventVersionId the event version id
     */
    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    public DataEngineEventType getEventType() {
        return eventType;
    }

    /**
     * Sets event type.
     *
     * @param eventType the event type
     */
    public void setEventType(DataEngineEventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets external source name.
     *
     * @return the external source name
     */
    public String getExternalSourceName() {
        return externalSourceName;
    }

    /**
     * Sets external source name.
     *
     * @param externalSourceName the external source name
     */
    public void setExternalSourceName(String externalSourceName) {
        this.externalSourceName = externalSourceName;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DataEngineEventHeader{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                ", externalSourceName='" + externalSourceName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
