/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageEventType describes the different types of events can be produced by the Asset Lineage OMAS.
 * Events are limited to assets that are in the zones listed in the supportedZones property
 * passed to the Asset Lineage OMAS at start up (a null value here means all zones).
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssetLineageEventType implements Serializable {

    PROCESS_CONTEXT_EVENT                   (0, "ProcessContextEvent", "Has the full context for a Process"),
    TECHNICAL_ELEMENT_CONTEXT_EVENT         (1, "TechnicalElementContextEvent", "Has the full context for a technical element"),
    CLASSIFICATION_CONTEXT_EVENT            (2, "ClassificationContextEvent", "Has the full context for a classified element"),
    UPDATE_ENTITY_EVENT                     (3, "UpdateEvent", "Has the entity that is being updated"),
    DELETE_ENTITY_EVENT                     (4, "DeleteEvent", "Has the entity to be deleted"),
    NEW_RELATIONSHIP_EVENT                  (5, "NewRelationship", "Has the relationship that is being created"),
    UPDATE_RELATIONSHIP_EVENT               (6, "UpdateRelationship", "Has the relationship that is being updated"),
    DELETE_RELATIONSHIP_EVENT               (7, "DeleteRelationship", "Has the relationship to be deleted"),
    DECLASSIFIED_ENTITY_EVENT               (8, "DeclassifiedEntityEvent", "All relevant lineage classifications for this entity have been removed"),
    UNKNOWN_ASSET_LINEAGE_EVENT             (100, "UnknownAssetLineageEvent", "An AssetLineage OMAS event that is not recognized by the local handlers.");

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
