/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.SyncUpdateContext;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage event contains information used for internal processing of data
 *
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageSyncEvent extends AssetLineageEventHeader {
    
    private SyncUpdateContext syncUpdateContext;

    public LineageSyncEvent() {
    }

    public LineageSyncEvent(SyncUpdateContext syncUpdateContext) {
        this.syncUpdateContext = syncUpdateContext;

    }

    public SyncUpdateContext getSyncUpdate() {
        return syncUpdateContext;
    }

    public void setSyncUpdate(SyncUpdateContext syncUpdateContext) {
        this.syncUpdateContext = syncUpdateContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageSyncEvent that = (LineageSyncEvent) o;
        return Objects.equals(syncUpdateContext, that.syncUpdateContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syncUpdateContext);
    }

    @Override
    public String toString() {
        return "LineageSyncEvent{" +
                "syncUpdateContext=" + syncUpdateContext +
                '}';
    }
}
