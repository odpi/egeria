/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "items",
        "paging"
})
public class IsTypeOf {
    @JsonProperty("items")
    private List<Item> items = null;
    @JsonProperty("paging")
    private Paging paging;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("paging")
    public Paging getPaging() {
        return paging;
    }

    @JsonProperty("paging")
    public void setPaging(Paging paging) {
        this.paging = paging;
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
