/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * POJO for the 'analytics_model' asset type in IGC, displayed as 'Data Science Model' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsModel extends MainObject {

    public static final String IGC_TYPE_ID = "analytics_model";

    /**
     * The 'analytics_project' property, displayed as 'Data Science Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsProject} object.
     */
    protected Reference analytics_project;

    /**
     * The 'input_training_notebook' property, displayed as 'Notebooks' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Notebook} objects.
     */
    protected ReferenceList input_training_notebook;

    /**
     * The 'input_training_script' property, displayed as 'Scripts' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsScript} objects.
     */
    protected ReferenceList input_training_script;

    /**
     * The 'input_training_column' property, displayed as 'Input Data Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList input_training_column;

    /**
     * The 'label_training_column' property, displayed as 'Output Data Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList label_training_column;

    /**
     * The 'eval_metric' property, displayed as 'Eval Metric' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ModelEvalMetric} objects.
     */
    protected ReferenceList eval_metric;

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
     * The 'model_type' property, displayed as 'Type' in the IGC UI.
     */
    protected String model_type;

    /**
     * The 'model_quality' property, displayed as 'Quality' in the IGC UI.
     */
    protected String model_quality;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected Number version;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #analytics_project */ @JsonProperty("analytics_project")  public Reference getAnalyticsProject() { return this.analytics_project; }
    /** @see #analytics_project */ @JsonProperty("analytics_project")  public void setAnalyticsProject(Reference analytics_project) { this.analytics_project = analytics_project; }

    /** @see #input_training_notebook */ @JsonProperty("input_training_notebook")  public ReferenceList getInputTrainingNotebook() { return this.input_training_notebook; }
    /** @see #input_training_notebook */ @JsonProperty("input_training_notebook")  public void setInputTrainingNotebook(ReferenceList input_training_notebook) { this.input_training_notebook = input_training_notebook; }

    /** @see #input_training_script */ @JsonProperty("input_training_script")  public ReferenceList getInputTrainingScript() { return this.input_training_script; }
    /** @see #input_training_script */ @JsonProperty("input_training_script")  public void setInputTrainingScript(ReferenceList input_training_script) { this.input_training_script = input_training_script; }

    /** @see #input_training_column */ @JsonProperty("input_training_column")  public ReferenceList getInputTrainingColumn() { return this.input_training_column; }
    /** @see #input_training_column */ @JsonProperty("input_training_column")  public void setInputTrainingColumn(ReferenceList input_training_column) { this.input_training_column = input_training_column; }

    /** @see #label_training_column */ @JsonProperty("label_training_column")  public ReferenceList getLabelTrainingColumn() { return this.label_training_column; }
    /** @see #label_training_column */ @JsonProperty("label_training_column")  public void setLabelTrainingColumn(ReferenceList label_training_column) { this.label_training_column = label_training_column; }

    /** @see #eval_metric */ @JsonProperty("eval_metric")  public ReferenceList getEvalMetric() { return this.eval_metric; }
    /** @see #eval_metric */ @JsonProperty("eval_metric")  public void setEvalMetric(ReferenceList eval_metric) { this.eval_metric = eval_metric; }

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

    /** @see #model_type */ @JsonProperty("model_type")  public String getModelType() { return this.model_type; }
    /** @see #model_type */ @JsonProperty("model_type")  public void setModelType(String model_type) { this.model_type = model_type; }

    /** @see #model_quality */ @JsonProperty("model_quality")  public String getModelQuality() { return this.model_quality; }
    /** @see #model_quality */ @JsonProperty("model_quality")  public void setModelQuality(String model_quality) { this.model_quality = model_quality; }

    /** @see #version */ @JsonProperty("version")  public Number getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(Number version) { this.version = version; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isAnalyticsModel(Object obj) { return (obj.getClass() == AnalyticsModel.class); }

}
