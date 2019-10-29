/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model.assetContext;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineageEvent.class, name = "LineageEvent")
})
public abstract class AssetLineageEvent {

    private long eventVersionId = 1L;

    private AssetLineageEventType assetLineageEventType;

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    public AssetLineageEventType getAssetLineageEventType() {
        return assetLineageEventType;
    }

    public void setAssetLineageEventType(AssetLineageEventType assetLineageEventType) {
        this.assetLineageEventType = assetLineageEventType;
    }

    @Override
    public String toString() {
        return "AssetLineageEvent{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }
}
