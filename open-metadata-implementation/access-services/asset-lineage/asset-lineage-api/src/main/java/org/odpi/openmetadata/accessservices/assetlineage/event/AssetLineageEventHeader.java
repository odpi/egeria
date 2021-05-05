/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageEventHeader provides a common base for all events from the Data Engine access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineageEntityEvent.class, name = "LineageEntityEvent"),
        @JsonSubTypes.Type(value = LineageRelationshipEvent.class, name = "LineageRelationshipEvent"),
        @JsonSubTypes.Type(value = LineageRelationshipsEvent.class, name = "LineageRelationshipsEvent"),
        @JsonSubTypes.Type(value = LineageSyncEvent.class, name = "LineageSyncEvent")
})

public abstract class AssetLineageEventHeader {

    private long eventVersionId = 1L;

    private AssetLineageEventType assetLineageEventType;

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
     * Gets asset lineage event type.
     *
     * @return the asset lineage event type
     */
    public AssetLineageEventType getAssetLineageEventType() {
        return assetLineageEventType;
    }

    /**
     * Sets asset lineage event type.
     *
     * @param assetLineageEventType the asset lineage event type
     */
    public void setAssetLineageEventType(AssetLineageEventType assetLineageEventType) {
        this.assetLineageEventType = assetLineageEventType;
    }

    @Override
    public String toString() {
        return "AssetLineageEventHeader{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }
}
