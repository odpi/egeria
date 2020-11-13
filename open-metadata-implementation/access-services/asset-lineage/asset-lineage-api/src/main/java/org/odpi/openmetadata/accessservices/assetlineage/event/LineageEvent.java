/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage event contains new or updated lineage graph context from assets.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageEvent extends AssetLineageEventHeader {

    private Map<String, Set<GraphContext>> assetContext;
    private LineageEntity lineageEntity;

    /**
     * Gets asset context.
     *
     * @return the asset context
     */
    public Map<String, Set<GraphContext>> getAssetContext() {
        return assetContext;
    }

    /**
     * Sets asset context.
     *
     * @param assetContext the asset context
     */
    public void setAssetContext(Map<String, Set<GraphContext>> assetContext) {

        this.assetContext = assetContext;
    }

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
        LineageEvent that = (LineageEvent) o;
        return Objects.equals(assetContext, that.assetContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetContext);
    }

    @Override
    public String toString() {
        return "LineageEvent{" +
                "assetContext=" + assetContext +
                "} " + super.toString();
    }
}

