/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;

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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
public class LineageEvent extends AssetLineageEventHeader {

   private Map<String, Set<GraphContext>> assetContext;

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

