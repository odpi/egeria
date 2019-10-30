/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.io.Serializable;

public enum AssetLineageEventType implements Serializable {

    PROCESS_CONTEXT_EVENT(0, "ProcessContextEvent", "Has the full context for a Process"),
    TECHNICAL_ELEMENT_CONTEXT_EVENT(1, "ProcessContextEvent", "Has the full context for a technical element"),
    UNKNOWN_ASSET_LINEAGE_EVENT(100, "UnknownAssetLineageEvent", "An AssetLineage OMAS event that is not recognized by the local handlers.");

    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;

    AssetLineageEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }

    public int getEventTypeCode() {
        return eventTypeCode;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public String getEventTypeDescription() {
        return eventTypeDescription;
    }
}
