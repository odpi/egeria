/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model;

import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;

public final class GraphRelationship {
    private final LineageEntity fromEntity;
    private final LineageEntity toEntity;
    private final String relationshipLabel;
    private final String relationshipGuid;

    public GraphRelationship(LineageEntity fromEntity, LineageEntity toEntity, String relationshipLabel, String relationshipGuid) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
        this.relationshipLabel = relationshipLabel;
        this.relationshipGuid = relationshipGuid;
    }

    public GraphRelationship(LineageRelationship lineageRelationship) {
        this.fromEntity = lineageRelationship.getSourceEntity();
        this.toEntity = lineageRelationship.getTargetEntity();
        this.relationshipLabel = lineageRelationship.getTypeDefName();
        this.relationshipGuid = lineageRelationship.getGuid();
    }

    public LineageEntity getFromEntity() {
        return fromEntity;
    }

    public LineageEntity getToEntity() {
        return toEntity;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public String getRelationshipGuid() {
        return relationshipGuid;
    }
}
