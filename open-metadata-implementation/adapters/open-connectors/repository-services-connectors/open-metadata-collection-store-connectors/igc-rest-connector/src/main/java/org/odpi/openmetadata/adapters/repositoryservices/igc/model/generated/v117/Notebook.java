/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'notebook' asset type in IGC, displayed as 'Notebook' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Notebook extends MainObject {

    public static final String IGC_TYPE_ID = "notebook";

    /**
     * The 'analytics_project' property, displayed as 'Data Science Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsProject} object.
     */
    protected Reference analytics_project;

    /**
     * The 'input_training_notebook_inv' property, displayed as 'Data Science Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsModel} objects.
     */
    protected ReferenceList input_training_notebook_inv;

    /**
     * The 'model_input_training_column' property, displayed as 'Data Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList model_input_training_column;

    /**
     * The 'system_equiv_id' property, displayed as 'System Equiv Id' in the IGC UI.
     */
    protected String system_equiv_id;

    /**
     * The 'first_published_date' property, displayed as 'First Published Date' in the IGC UI.
     */
    protected Date first_published_date;

    /**
     * The 'package_name' property, displayed as 'Package Name' in the IGC UI.
     */
    protected String package_name;

    /**
     * The 'logical_name' property, displayed as 'Logical Name' in the IGC UI.
     */
    protected String logical_name;

    /**
     * The 'url' property, displayed as 'URL' in the IGC UI.
     */
    protected String url;

    /**
     * The 'environment' property, displayed as 'Environment' in the IGC UI.
     */
    protected String environment;

    /**
     * The 'tool' property, displayed as 'Tool' in the IGC UI.
     */
    protected String tool;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #analytics_project */ @JsonProperty("analytics_project")  public Reference getAnalyticsProject() { return this.analytics_project; }
    /** @see #analytics_project */ @JsonProperty("analytics_project")  public void setAnalyticsProject(Reference analytics_project) { this.analytics_project = analytics_project; }

    /** @see #input_training_notebook_inv */ @JsonProperty("input_training_notebook_inv")  public ReferenceList getInputTrainingNotebookInv() { return this.input_training_notebook_inv; }
    /** @see #input_training_notebook_inv */ @JsonProperty("input_training_notebook_inv")  public void setInputTrainingNotebookInv(ReferenceList input_training_notebook_inv) { this.input_training_notebook_inv = input_training_notebook_inv; }

    /** @see #model_input_training_column */ @JsonProperty("model_input_training_column")  public ReferenceList getModelInputTrainingColumn() { return this.model_input_training_column; }
    /** @see #model_input_training_column */ @JsonProperty("model_input_training_column")  public void setModelInputTrainingColumn(ReferenceList model_input_training_column) { this.model_input_training_column = model_input_training_column; }

    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public String getSystemEquivId() { return this.system_equiv_id; }
    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public void setSystemEquivId(String system_equiv_id) { this.system_equiv_id = system_equiv_id; }

    /** @see #first_published_date */ @JsonProperty("first_published_date")  public Date getFirstPublishedDate() { return this.first_published_date; }
    /** @see #first_published_date */ @JsonProperty("first_published_date")  public void setFirstPublishedDate(Date first_published_date) { this.first_published_date = first_published_date; }

    /** @see #package_name */ @JsonProperty("package_name")  public String getPackageName() { return this.package_name; }
    /** @see #package_name */ @JsonProperty("package_name")  public void setPackageName(String package_name) { this.package_name = package_name; }

    /** @see #logical_name */ @JsonProperty("logical_name")  public String getLogicalName() { return this.logical_name; }
    /** @see #logical_name */ @JsonProperty("logical_name")  public void setLogicalName(String logical_name) { this.logical_name = logical_name; }

    /** @see #url */ @JsonProperty("url")  public String getTheUrl() { return this.url; }
    /** @see #url */ @JsonProperty("url")  public void setTheUrl(String url) { this.url = url; }

    /** @see #environment */ @JsonProperty("environment")  public String getEnvironment() { return this.environment; }
    /** @see #environment */ @JsonProperty("environment")  public void setEnvironment(String environment) { this.environment = environment; }

    /** @see #tool */ @JsonProperty("tool")  public String getTool() { return this.tool; }
    /** @see #tool */ @JsonProperty("tool")  public void setTool(String tool) { this.tool = tool; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isNotebook(Object obj) { return (obj.getClass() == Notebook.class); }

}
