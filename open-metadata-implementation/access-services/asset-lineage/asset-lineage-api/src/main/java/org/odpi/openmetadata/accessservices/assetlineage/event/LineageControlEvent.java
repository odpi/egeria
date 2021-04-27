/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageControlEvent extends AssetLineageEventHeader {

    private List<String> publishSummary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageControlEvent that = (LineageControlEvent) o;
        return Objects.equals(getPublishSummary(), that.getPublishSummary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPublishSummary());
    }

    @Override
    public String toString() {
        return "LineageControlEvent{" +
                "publishSummary=" + getPublishSummary() +
                '}';
    }

    public List<String> getPublishSummary() {
        return publishSummary;
    }

    public void setPublishSummary(List<String> publishSummary) {
        this.publishSummary = publishSummary;
    }
}
