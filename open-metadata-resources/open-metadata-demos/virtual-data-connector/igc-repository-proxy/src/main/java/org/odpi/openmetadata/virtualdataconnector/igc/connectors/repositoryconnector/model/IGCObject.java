/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "created_by",
        "position",
        "modified_on",
        "unique",
        "assigned_to_terms",
        "related_terms",
        "assigned_assets",
        "_context",
        "created_on",
        "allows_null_values",
        "_name",
        "type",
        "_type",
        "_id",
        "modified_by",
        "_url",
        "data_type",
        "name",
        "minimum_length",
        "length",
        "odbc_type",
        "database_table_or_view",
        "is_a_type_of",
        "short_description",
        "long_description",
        "database_columns",
        "usage",
        "example",
        "abbreviation",
        "data_connections",
        "data_connectors"
})

public class IGCObject {

    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("modified_on")
    private String modifiedOn;
    @JsonProperty("unique")
    private Boolean unique;
    @JsonProperty("assigned_to_terms")
    private Terms assignedToTerms;
    @JsonProperty("_context")
    private List<Context> context = new ArrayList<>();
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("allows_null_values")
    private Boolean allowsNullValues;
    @JsonProperty("_name")
    private String _name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("_type")
    private String _type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("_url")
    private String url;
    @JsonProperty("data_type")
    private String dataType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("minimum_length")
    private Integer minimumLength;
    @JsonProperty("length")
    private String length;
    @JsonProperty("odbc_type")
    private String odbcType;
    @JsonProperty("database_table_or_view")
    private DatabaseTableOrView databaseTableOrView;
    @JsonProperty("is_a_type_of")
    private Terms isTypeOf;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("long_description")
    private String longDescription;
    @JsonProperty("related_terms")
    private Terms relatedTerms;
    @JsonProperty("assigned_assets")
    private Terms assignedAssets;
    @JsonProperty("database_columns")
    private Terms databaseColumns;
    @JsonProperty("data_connectors")
    private DataConnectors dataConnectors;
    @JsonProperty("data_connections")
    private DataConnections dataConnections;
    @JsonProperty("usage")
    private String usage;
    @JsonProperty("example")
    private String example;
    @JsonProperty("abbreviation")
    private String abbreviation;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("data_connectors")
    public DataConnectors getDataConnectors() {
        return dataConnectors;
    }

    @JsonProperty("data_connections")
    public DataConnections getDataConnections() {
        return dataConnections;
    }

    @JsonProperty("database_columns")
    public Terms getDatabaseColumns() {
        return databaseColumns;
    }


    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("long_description")
    public String getLongDescription() {
        return longDescription;
    }

    @JsonProperty("created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("created_by")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

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

    @JsonProperty("unique")
    public Boolean getUnique() {
        return unique;
    }

    @JsonProperty("unique")
    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    @JsonProperty("assigned_to_terms")
    public Terms getAssignedToTerms() {
        return assignedToTerms;
    }

    @JsonProperty("assigned_to_terms")
    public void setAssignedToTerms(Terms assignedToTerms) {
        this.assignedToTerms = assignedToTerms;
    }

    @JsonProperty("_context")
    public List<Context> getContext() {
        return context;
    }

    @JsonProperty("_context")
    public void setContext(List<Context> context) {
        this.context = context;
    }

    @JsonProperty("created_on")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @JsonProperty("allows_null_values")
    public Boolean getAllowsNullValues() {
        return allowsNullValues;
    }

    @JsonProperty("allows_null_values")
    public void setAllowsNullValues(Boolean allowsNullValues) {
        this.allowsNullValues = allowsNullValues;
    }

    @JsonProperty("_name")
    public String get_Name() {
        return _name;
    }

    @JsonProperty("_name")
    public void set_Name(String _name) {
        this._name = _name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("_type")
    public String get_Type() {
        return _type;
    }

    @JsonProperty("_type")
    public void set_Type(String _type) {
        this._type = _type;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("modified_by")
    public String getModifiedBy() {
        return modifiedBy;
    }

    @JsonProperty("modified_by")
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty("_url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("_url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("data_type")
    public String getDataType() {
        return dataType;
    }

    @JsonProperty("data_type")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("minimum_length")
    public Integer getMinimumLength() {
        return minimumLength;
    }

    @JsonProperty("minimum_length")
    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    @JsonProperty("length")
    public String getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("odbc_type")
    public String getOdbcType() {
        return odbcType;
    }

    @JsonProperty("odbc_type")
    public void setOdbcType(String odbcType) {
        this.odbcType = odbcType;
    }

    @JsonProperty("database_table_or_view")
    public DatabaseTableOrView getDatabaseTableOrView() {
        return databaseTableOrView;
    }

    @JsonProperty("database_table_or_view")
    public void setDatabaseTableOrView(DatabaseTableOrView databaseTableOrView) {
        this.databaseTableOrView = databaseTableOrView;
    }

    @JsonProperty("is_a_type_of")
    public Terms getIsTypeOf() {
        return isTypeOf;
    }

    @JsonProperty("is_a_type_of")
    public void setIsTypeOf(Terms isTypeOf) {
        this.isTypeOf = isTypeOf;
    }

    @JsonProperty("assigned_assets")
    public Terms getAssignedAssets() {
        return assignedAssets;
    }

    @JsonProperty("related_terms")
    public Terms getRelatedTerms() {
        return relatedTerms;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setRelatedTerms(Terms relatedTerms) {
        this.relatedTerms = relatedTerms;
    }

    public void setAssignedAssets(Terms assignedAssets) {
        this.assignedAssets = assignedAssets;
    }

    public void setDatabaseColumns(Terms databaseColumns) {
        this.databaseColumns = databaseColumns;
    }

    public void setDataConnectors(DataConnectors dataConnectors) {
        this.dataConnectors = dataConnectors;
    }

    public void setDataConnections(DataConnections dataConnections) {
        this.dataConnections = dataConnections;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("usage")
    public String getUsage() {
        return usage;
    }

    @JsonProperty("usage")
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @JsonProperty("example")
    public String getExample() {
        return example;
    }

    @JsonProperty("example")
    public void setExample(String example) {
        this.example = example;
    }

    @JsonProperty("abbreviation")
    public String getAbbreviation() {
        return abbreviation;
    }

    @JsonProperty("abbreviation")
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}