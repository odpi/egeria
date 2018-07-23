/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numTotal",
        "pageSize",
        "end",
        "begin"
})
public class Paging {

    @JsonProperty("numTotal")
    private Integer numTotal;
    @JsonProperty("pageSize")
    private Integer pageSize;
    @JsonProperty("end")
    private Integer end;
    @JsonProperty("begin")
    private Integer begin;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("numTotal")
    public Integer getNumTotal() {
        return numTotal;
    }

    @JsonProperty("numTotal")
    public void setNumTotal(Integer numTotal) {
        this.numTotal = numTotal;
    }

    @JsonProperty("pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    @JsonProperty("pageSize")
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @JsonProperty("end")
    public Integer getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(Integer end) {
        this.end = end;
    }

    @JsonProperty("begin")
    public Integer getBegin() {
        return begin;
    }

    @JsonProperty("begin")
    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}