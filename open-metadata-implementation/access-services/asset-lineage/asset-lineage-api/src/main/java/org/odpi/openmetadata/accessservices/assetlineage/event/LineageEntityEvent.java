/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage event contains new or updated lineage graph context from assets.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageEntityEvent extends AssetLineageEventHeader {

    private LineageEntity lineageEntity;

    /**
     * Gets lineage entity.
     *
     * @return the lineage entity
     */
    public LineageEntity getLineageEntity() {
        return lineageEntity;
    }

    /**
     * Sets lineage entity.
     *
     * @param lineageEntity the lineage entity
     */
    public void setLineageEntity(LineageEntity lineageEntity) {
        this.lineageEntity = lineageEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageEntityEvent that = (LineageEntityEvent) o;
        return Objects.equals(lineageEntity, that.lineageEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineageEntity);
    }

    @Override
    public String toString() {
        return "LineageEntityEvent{" +
                "lineageEntity=" + lineageEntity +
                '}';
    }
}

