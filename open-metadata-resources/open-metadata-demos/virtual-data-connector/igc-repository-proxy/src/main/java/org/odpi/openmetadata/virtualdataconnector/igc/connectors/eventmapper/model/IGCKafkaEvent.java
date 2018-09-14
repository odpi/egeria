/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ASSET_TYPE",
        "ASSET_RID",
        "eventType",
        "_isEncrypted",
        "ASSET_CONTEXT",
        "ACTION",
        "ASSET_NAME",
        "createdRIDs"
})
public class IGCKafkaEvent {

    @JsonProperty("ASSET_TYPE")
    private String assetType;
    @JsonProperty("ASSET_RID")
    private String assetRID;
    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("_isEncrypted")
    private Boolean isEncrypted;
    @JsonProperty("ASSET_CONTEXT")
    private String assetContext;
    @JsonProperty("ACTION")
    private String action;
    @JsonProperty("ASSET_NAME")
    private String assetName;
    @JsonProperty("createdRIDs")
    private String createdRIDs;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("createdRIDs")
    public String getDatacollectionRID() {
        return createdRIDs.split(":")[1];
    }

    @JsonProperty("ASSET_TYPE")
    public String getAssetType() {
        return assetType;
    }

    @JsonProperty("ASSET_TYPE")
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    @JsonProperty("ASSET_RID")
    public String getAssetRID() {
        return assetRID;
    }

    @JsonProperty("ASSET_RID")
    public void setAssetRID(String assetRID) {
        this.assetRID = assetRID;
    }

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @JsonProperty("_isEncrypted")
    public Boolean getIsEncrypted() {
        return isEncrypted;
    }

    @JsonProperty("_isEncrypted")
    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    @JsonProperty("ASSET_CONTEXT")
    public String getAssetContext() {
        return assetContext;
    }

    @JsonProperty("ASSET_CONTEXT")
    public void setAssetContext(String assetContext) {
        this.assetContext = assetContext;
    }

    @JsonProperty("ACTION")
    public String getAction() {
        return action;
    }

    @JsonProperty("ACTION")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("ASSET_NAME")
    public String getAssetName() {
        return assetName;
    }

    @JsonProperty("ASSET_NAME")
    public void setAssetName(String assetName) {
        this.assetName = assetName;
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
