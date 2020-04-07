/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataPlatformEventHeader provides a common base for all events from the Data Platform OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NewViewEvent.class, name = "NewViewEvent"),
        @JsonSubTypes.Type(value = NewDeployedDatabaseSchemaEvent.class, name = "NewDeployedDatabaseSchemaEvent"),
        @JsonSubTypes.Type(value = NewTabularColumnEvent.class, name = "NewTabularColumnEvent"),
        @JsonSubTypes.Type(value = NewTabularSchemaEvent.class, name = "NewTabularSchemaEvent"),
})
public abstract class DataPlatformEventHeader implements java.io.Serializable{

    private long eventVersionId = 1L;

    /* event types for different metadata changes */
    private DataPlatformEventType eventType = null;

    /* unique name for the data platforms as external source */
    private String externalSourceName;

    /**
     * Instantiates a new Data platform event header.
     */
    public DataPlatformEventHeader() {
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
    public DataPlatformEventType getEventType() {
        return eventType;
    }

    /**
     * Sets event type.
     *
     * @param eventType the event type
     */
    public void setEventType(DataPlatformEventType eventType) {
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

    @Override
    public String toString() {
        return "DataPlatformEventHeader{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                ", externalSourceName='" + externalSourceName + '\'' +
                '}';
    }
}
