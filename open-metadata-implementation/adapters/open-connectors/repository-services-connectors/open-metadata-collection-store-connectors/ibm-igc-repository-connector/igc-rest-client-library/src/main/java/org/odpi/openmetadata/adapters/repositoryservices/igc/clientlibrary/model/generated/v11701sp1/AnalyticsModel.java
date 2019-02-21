/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code analytics_model} asset type in IGC, displayed as '{@literal Data Science Model}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsModel extends Reference {

    public static String getIgcTypeId() { return "analytics_model"; }
    public static String getIgcTypeDisplayName() { return "Data Science Model"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code analytics_project} property, displayed as '{@literal Data Science Project}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsProject} object.
     */
    protected Reference analytics_project;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code input_training_notebook} property, displayed as '{@literal Notebooks}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Notebook} objects.
     */
    protected ReferenceList input_training_notebook;

    /**
     * The {@code input_training_script} property, displayed as '{@literal Scripts}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsScript} objects.
     */
    protected ReferenceList input_training_script;

    /**
     * The {@code input_training_column} property, displayed as '{@literal Input Data Assets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList input_training_column;

    /**
     * The {@code label_training_column} property, displayed as '{@literal Output Data Assets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList label_training_column;

    /**
     * The {@code eval_metric} property, displayed as '{@literal Eval Metric}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ModelEvalMetric} objects.
     */
    protected ReferenceList eval_metric;

    /**
     * The {@code system_equiv_id} property, displayed as '{@literal System Equiv Id}' in the IGC UI.
     */
    protected String system_equiv_id;

    /**
     * The {@code first_published_date} property, displayed as '{@literal First Published Date}' in the IGC UI.
     */
    protected Date first_published_date;

    /**
     * The {@code package_name} property, displayed as '{@literal Package Name}' in the IGC UI.
     */
    protected String package_name;

    /**
     * The {@code logical_name} property, displayed as '{@literal Logical Name}' in the IGC UI.
     */
    protected String logical_name;

    /**
     * The {@code url} property, displayed as '{@literal URL}' in the IGC UI.
     */
    protected String url;

    /**
     * The {@code model_type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String model_type;

    /**
     * The {@code model_quality} property, displayed as '{@literal Quality}' in the IGC UI.
     */
    protected String model_quality;

    /**
     * The {@code version} property, displayed as '{@literal Version}' in the IGC UI.
     */
    protected Number version;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #analytics_project */ @JsonProperty("analytics_project")  public Reference getAnalyticsProject() { return this.analytics_project; }
    /** @see #analytics_project */ @JsonProperty("analytics_project")  public void setAnalyticsProject(Reference analytics_project) { this.analytics_project = analytics_project; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "system_equiv_id",
        "first_published_date",
        "package_name",
        "logical_name",
        "url",
        "model_type",
        "model_quality",
        "version",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "system_equiv_id",
        "package_name",
        "logical_name",
        "url",
        "model_type",
        "model_quality",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "input_training_notebook",
        "input_training_script",
        "input_training_column",
        "label_training_column",
        "eval_metric",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "analytics_project",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "input_training_notebook",
        "input_training_script",
        "input_training_column",
        "label_training_column",
        "eval_metric",
        "system_equiv_id",
        "first_published_date",
        "package_name",
        "logical_name",
        "url",
        "model_type",
        "model_quality",
        "version",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isAnalyticsModel(Object obj) { return (obj.getClass() == AnalyticsModel.class); }

}
