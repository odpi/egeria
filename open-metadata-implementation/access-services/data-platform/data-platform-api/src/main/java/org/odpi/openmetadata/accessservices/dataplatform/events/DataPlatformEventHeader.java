/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.*;

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

    /* unique name for the external data platforms as source */
    private String externalSouceName;

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
     * Gets external souce name.
     *
     * @return the external souce name
     */
    public String getExternalSouceName() {
        return externalSouceName;
    }

    /**
     * Sets external souce name.
     *
     * @param externalSouceName the external souce name
     */
    public void setExternalSouceName(String externalSouceName) {
        this.externalSouceName = externalSouceName;
    }

    @Override
    public String toString() {
        return "DataPlatformEventHeader{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                ", externalSouceName='" + externalSouceName + '\'' +
                '}';
    }
}
