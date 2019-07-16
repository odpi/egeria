/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataPlatformEventHeader provides a common base for all events from the access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NewViewEvent.class, name = "NewViewEvent")
})
public abstract class DataPlatformEventHeader {

    private long eventVersionId = 1L;
    private DataPlatformEventType eventType = null;

    public DataPlatformEventHeader() {
    }

    public DataPlatformEventHeader(long eventVersionId, DataPlatformEventType eventType) {
        this.eventVersionId = eventVersionId;
        this.eventType = eventType;
    }

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    public DataPlatformEventType getEventType() {
        return eventType;
    }

    public void setEventType(DataPlatformEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "DataPlatformEventHeader{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                '}';
    }
}
