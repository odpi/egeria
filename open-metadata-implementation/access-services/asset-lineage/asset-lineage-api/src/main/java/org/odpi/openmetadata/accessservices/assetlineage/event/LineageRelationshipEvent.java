/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage relationship event contains lineage graph context from relationships.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageRelationshipEvent extends AssetLineageEventHeader {
    private LineageRelationship lineageRelationship;

    /**
     * Gets lineage relationship.
     *
     * @return the lineage relationship
     */
    public LineageRelationship getLineageRelationship() {
        return lineageRelationship;
    }

    /**
     * Sets lineage relationship.
     *
     * @param lineageRelationship the lineage relationship
     */
    public void setLineageRelationship(LineageRelationship lineageRelationship) {
        this.lineageRelationship = lineageRelationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageRelationshipEvent that = (LineageRelationshipEvent) o;
        return Objects.equals(lineageRelationship, that.lineageRelationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineageRelationship);
    }

    @Override
    public String toString() {
        return "LineageRelationshipEvent{" +
                "lineageRelationship=" + lineageRelationship +
                '}';
    }
}
