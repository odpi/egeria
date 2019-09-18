/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model.assetContext;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.DeletePurgedRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;

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
        @JsonSubTypes.Type(value = RelationshipEvent.class, name = "RelationshipEvent"),
        @JsonSubTypes.Type(value = DeletePurgedRelationshipEvent.class, name = "DeletePurgedRelationshipEvent"),
        @JsonSubTypes.Type(value = AssetLineageEntityEvent.class, name = "AssetLineageEntityEvent")
})
public abstract class AssetLineageEvent {

    private long eventVersionId = 1L;

    private OMRSInstanceEventType omrsInstanceEventType;

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    public OMRSInstanceEventType getOmrsInstanceEventType() {
        return omrsInstanceEventType;
    }

    public void setOmrsInstanceEventType(OMRSInstanceEventType omrsInstanceEventType) {
        this.omrsInstanceEventType = omrsInstanceEventType;
    }

    @Override
    public String toString() {
        return "AssetLineageEvent{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }
}
