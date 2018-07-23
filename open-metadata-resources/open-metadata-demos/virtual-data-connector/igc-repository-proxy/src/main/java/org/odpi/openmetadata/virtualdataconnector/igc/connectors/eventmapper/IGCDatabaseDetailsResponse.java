/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IGCDatabaseDetailsResponse {


    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("modified_on")
    private String modifiedOn;

    @JsonProperty("imported_from")
    private String importedFrom;

    @JsonProperty("host")
    private Map<String, String> host;

    @JsonProperty("_context")
    private List<Context> context = null;

    @JsonProperty("dbms_server_instance")
    private String dbmsServerInstance;

    @JsonProperty("dbms_vendor")
    private String dbmsVendor;

    @JsonProperty("dbms")
    private String dbms;

    @JsonProperty("_id")
    private String _id;

    @JsonProperty("name")
    private String name;

    public String getDbmsServerInstance() {
        return dbmsServerInstance;
    }

    public void setDbmsServerInstance(String dbmsServerInstance) {
        this.dbmsServerInstance = dbmsServerInstance;
    }

    public String getDbmsVendor() {
        return dbmsVendor;
    }

    public void setDbmsVendor(String dbmsVendor) {
        this.dbmsVendor = dbmsVendor;
    }

    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
