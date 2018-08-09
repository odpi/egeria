/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.jackson;

import com.fasterxml.jackson.annotation.*;

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
        "ASSET_NAME"
})
public class IGCKafkaEvent {

    @JsonProperty("ASSET_TYPE")
    private String aSSETTYPE;
    @JsonProperty("ASSET_RID")
    private String aSSETRID;
    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("_isEncrypted")
    private Boolean isEncrypted;
    @JsonProperty("ASSET_CONTEXT")
    private String aSSETCONTEXT;
    @JsonProperty("ACTION")
    private String aCTION;
    @JsonProperty("ASSET_NAME")
    private String aSSETNAME;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ASSET_TYPE")
    public String getASSETTYPE() {
        return aSSETTYPE;
    }

    @JsonProperty("ASSET_TYPE")
    public void setASSETTYPE(String aSSETTYPE) {
        this.aSSETTYPE = aSSETTYPE;
    }

    @JsonProperty("ASSET_RID")
    public String getASSETRID() {
        return aSSETRID;
    }

    @JsonProperty("ASSET_RID")
    public void setASSETRID(String aSSETRID) {
        this.aSSETRID = aSSETRID;
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
    public String getASSETCONTEXT() {
        return aSSETCONTEXT;
    }

    @JsonProperty("ASSET_CONTEXT")
    public void setASSETCONTEXT(String aSSETCONTEXT) {
        this.aSSETCONTEXT = aSSETCONTEXT;
    }

    @JsonProperty("ACTION")
    public String getACTION() {
        return aCTION;
    }

    @JsonProperty("ACTION")
    public void setACTION(String aCTION) {
        this.aCTION = aCTION;
    }

    @JsonProperty("ASSET_NAME")
    public String getASSETNAME() {
        return aSSETNAME;
    }

    @JsonProperty("ASSET_NAME")
    public void setASSETNAME(String aSSETNAME) {
        this.aSSETNAME = aSSETNAME;
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
