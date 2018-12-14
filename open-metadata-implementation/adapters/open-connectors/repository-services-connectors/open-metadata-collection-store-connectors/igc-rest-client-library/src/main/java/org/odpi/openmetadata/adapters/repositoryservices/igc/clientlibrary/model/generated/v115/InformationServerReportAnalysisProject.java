/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_server_report_(analysis_project)' asset type in IGC, displayed as 'Information Server Report (Analysis Project)' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServerReportAnalysisProject extends Reference {

    public static String getIgcTypeId() { return "information_server_report_(analysis_project)"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'creator' property, displayed as 'Creator' in the IGC UI.
     */
    protected String creator;

    /**
     * The 'product' property, displayed as 'Product' in the IGC UI.
     */
    protected String product;

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     */
    protected ArrayList<String> host;

    /**
     * The 'database' property, displayed as 'Database' in the IGC UI.
     */
    protected ArrayList<String> database;

    /**
     * The 'schema' property, displayed as 'Schema' in the IGC UI.
     */
    protected ArrayList<String> schema;

    /**
     * The 'table' property, displayed as 'Table' in the IGC UI.
     */
    protected ArrayList<String> table;

    /**
     * The 'analysis_project' property, displayed as 'Analysis Project' in the IGC UI.
     */
    protected ArrayList<String> analysis_project;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #creator */ @JsonProperty("creator")  public String getCreator() { return this.creator; }
    /** @see #creator */ @JsonProperty("creator")  public void setCreator(String creator) { this.creator = creator; }

    /** @see #product */ @JsonProperty("product")  public String getProduct() { return this.product; }
    /** @see #product */ @JsonProperty("product")  public void setProduct(String product) { this.product = product; }

    /** @see #host */ @JsonProperty("host")  public ArrayList<String> getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(ArrayList<String> host) { this.host = host; }

    /** @see #database */ @JsonProperty("database")  public ArrayList<String> getDatabase() { return this.database; }
    /** @see #database */ @JsonProperty("database")  public void setDatabase(ArrayList<String> database) { this.database = database; }

    /** @see #schema */ @JsonProperty("schema")  public ArrayList<String> getSchema() { return this.schema; }
    /** @see #schema */ @JsonProperty("schema")  public void setSchema(ArrayList<String> schema) { this.schema = schema; }

    /** @see #table */ @JsonProperty("table")  public ArrayList<String> getTable() { return this.table; }
    /** @see #table */ @JsonProperty("table")  public void setTable(ArrayList<String> table) { this.table = table; }

    /** @see #analysis_project */ @JsonProperty("analysis_project")  public ArrayList<String> getAnalysisProject() { return this.analysis_project; }
    /** @see #analysis_project */ @JsonProperty("analysis_project")  public void setAnalysisProject(ArrayList<String> analysis_project) { this.analysis_project = analysis_project; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isInformationServerReportAnalysisProject(Object obj) { return (obj.getClass() == InformationServerReportAnalysisProject.class); }

}
