/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage mappings event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageMappingsEvent extends DataEngineEventHeader {

    private List<LineageMapping> lineageMappings;

    /**
     * Gets lineage mappings.
     *
     * @return the lineage mappings
     */
    public List<LineageMapping> getLineageMappings() {
        return lineageMappings;
    }

    /**
     * Sets lineage mappings.
     *
     * @param lineageMappings the lineage mappings
     */
    public void setLineageMappings(List<LineageMapping> lineageMappings) {
        this.lineageMappings = lineageMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageMappingsEvent that = (LineageMappingsEvent) o;
        return Objects.equals(lineageMappings, that.lineageMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineageMappings);
    }

    @Override
    public String toString() {
        return "LineageMappingsEvent{" +
                "lineageMappings=" + lineageMappings +
                "} " + super.toString();
    }
}
