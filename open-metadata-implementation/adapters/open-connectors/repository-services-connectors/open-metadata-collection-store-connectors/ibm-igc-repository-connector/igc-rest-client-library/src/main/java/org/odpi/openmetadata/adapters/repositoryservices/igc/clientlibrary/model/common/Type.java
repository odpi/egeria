/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the metadata related to an object type in IGC.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Type extends ObjectPrinter {

    /**
     * Indicates whether the object type is included in business lineage (true) or not (false).
     */
    protected Boolean _businessLineage;

    /**
     * The display name of the object type.
     */
    protected String _name;

    /**
     * The plural display name (if any) of the object type.
     */
    protected String _plural;

    /**
     * The unique REST name of the object type.
     */
    protected String _id;

    /**
     * The meta-model class name of the object type.
     */
    protected String _class;

    /**
     * The URL to access more details about the object type.
     */
    protected String _url;

    /**
     * Indicates whether the object type is included in data lineage (true) or not (false).
     */
    protected Boolean _dataLineage;

    /**
     * The hierarchy (if any) under which the object type is contained.
     */
    protected String _group;

    /**
     * The parent object types (if any) of the object type.
     */
    protected List<String> _parents;

    /** @see #_businessLineage */ @JsonProperty("_businessLineage") public Boolean getBusinessLineage() { return this._businessLineage; }
    /** @see #_businessLineage */ @JsonProperty("_businessLineage") public void setBusinessLineage(Boolean _businessLineage) { this._businessLineage = _businessLineage; }

    /** @see #_name */ @JsonProperty("_name") public String getName() { return this._name; }
    /** @see #_name */ @JsonProperty("_name") public void setName(String _name) { this._name = _name; }

    /** @see #_plural */ @JsonProperty("_plural") public String getPlural() { return this._plural; }
    /** @see #_plural */ @JsonProperty("_plural") public void setPlural(String _plural) { this._plural = _plural; }

    /** @see #_id */ @JsonProperty("_id") public String getId() { return this._id; }
    /** @see #_id */ @JsonProperty("_id") public void setId(String _id) { this._id = _id; }

    /** @see #_class */ @JsonProperty("_class") public String getTheClass() { return this._class; }
    /** @see #_class */ @JsonProperty("_class") public void setTheClass(String _class) { this._class = _class; }

    /** @see #_url */ @JsonProperty("_url") public String getURL() { return this._url; }
    /** @see #_url */ @JsonProperty("_url") public void setURL(String _url) { this._url = _url; }

    /** @see #_dataLineage */ @JsonProperty("_dataLineage") public Boolean getDataLineage() { return this._dataLineage; }
    /** @see #_dataLineage */ @JsonProperty("_dataLineage") public void setDataLineage(Boolean _dataLineage) { this._dataLineage = _dataLineage; }

    /** @see #_group */ @JsonProperty("_group") public String getGroup() { return this._group; }
    /** @see #_group */ @JsonProperty("_group") public void setGroup(String _group) { this._group = _group; }

    /** @see #_parents */ @JsonProperty("_parents") public List<String> getParents() { return this._parents; }
    /** @see #_parents */ @JsonProperty("_parents") public void setParents(List<String> _parents) { this._parents = _parents; }

}
