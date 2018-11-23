/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
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
public class InformationServerReportAnalysisProject extends MainObject {

    public static final String IGC_TYPE_ID = "information_server_report_(analysis_project)";

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


    public static final Boolean isInformationServerReportAnalysisProject(Object obj) { return (obj.getClass() == InformationServerReportAnalysisProject.class); }

}
