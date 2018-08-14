/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.jackson;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "position",
        "modified_on",
        "_type",
        "assigned_to_terms",
        "_id",
        "_context",
        "data_type",
        "_url",
        "name",
        "length",
        "_name",
        "database_table_or_view.assigned_to_terms"
})
public class DatabaseSchema {

    @JsonProperty("position")
    private Integer position;
    @JsonProperty("modified_on")
    private String modifiedOn;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("assigned_to_terms")
    private AssignedToTerms assignedToTerms;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_context")
    private List<Context> context = null;
    @JsonProperty("data_type")
    private String dataType;
    @JsonProperty("_url")
    private String url;
    @JsonProperty("length")
    private String length;
    @JsonProperty("_name")
    private String name;
    @JsonProperty("database_table_or_view.assigned_to_terms")
    private DatabaseTableOrViewAssignedToTerms databaseTableOrViewAssignedToTerms;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("position")
    public Integer getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Integer position) {
        this.position = position;
    }

    @JsonProperty("modified_on")
    public String getModifiedOn() {
        return modifiedOn;
    }

    @JsonProperty("modified_on")
    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("assigned_to_terms")
    public AssignedToTerms getAssignedToTerms() {
        return assignedToTerms;
    }

    @JsonProperty("assigned_to_terms")
    public void setAssignedToTerms(AssignedToTerms assignedToTerms) {
        this.assignedToTerms = assignedToTerms;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_context")
    public List<Context> getContext() {
        return context;
    }

    @JsonProperty("_context")
    public void setContext(List<Context> context) {
        this.context = context;
    }

    @JsonProperty("data_type")
    public String getDataType() {
        return dataType;
    }

    @JsonProperty("data_type")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("_url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("_url")
    public void setUrl(String url) {
        this.url = url;
    }


    @JsonProperty("length")
    public String getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("_name")
    public String getName() {
        return name;
    }

    @JsonProperty("_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("database_table_or_view.assigned_to_terms")
    public DatabaseTableOrViewAssignedToTerms getDatabaseTableOrViewAssignedToTerms() {
        return databaseTableOrViewAssignedToTerms;
    }

    @JsonProperty("database_table_or_view.assigned_to_terms")
    public void setDatabaseTableOrViewAssignedToTerms(DatabaseTableOrViewAssignedToTerms databaseTableOrViewAssignedToTerms) {
        this.databaseTableOrViewAssignedToTerms = databaseTableOrViewAssignedToTerms;
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


