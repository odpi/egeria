/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'sequence_job' asset type in IGC, displayed as 'Sequence Job' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SequenceJob extends Reference {

    public static String getIgcTypeId() { return "sequence_job"; }
    public static String getIgcTypeDisplayName() { return "Sequence Job"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'references_local_or_shared_containers' property, displayed as 'References Local or Shared Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ReferencedContainer} objects.
     */
    protected ReferenceList references_local_or_shared_containers;

    /**
     * The 'sequences_jobs' property, displayed as 'Sequences Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList sequences_jobs;

    /**
     * The 'folder' property, displayed as 'Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The 'design_parameters' property, displayed as 'Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsparameterJob} objects.
     */
    protected ReferenceList design_parameters;

    /**
     * The 'parameter_sets' property, displayed as 'Parameter Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ParameterSetDefinition} objects.
     */
    protected ReferenceList parameter_sets;

    /**
     * The 'annotations' property, displayed as 'Annotations' in the IGC UI.
     */
    protected ArrayList<String> annotations;

    /**
     * The 'job_runs' property, displayed as 'Job Runs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link JobRun} objects.
     */
    protected ReferenceList job_runs;

    /**
     * The 'include_for_lineage' property, displayed as 'Include for Lineage' in the IGC UI.
     */
    protected Boolean include_for_lineage;

    /**
     * The 'include_for_lineage_description' property, displayed as 'Include for Lineage' in the IGC UI.
     */
    protected String include_for_lineage_description;

    /**
     * The 'include_for_lineage__edit' property, displayed as 'Include for Lineage' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>setToTrue (displayed in the UI as 'True')</li>
     *     <li>setToFalse (displayed in the UI as 'False')</li>
     *     <li>defaultToProject (displayed in the UI as 'Default (Transformation Project)')</li>
     * </ul>
     */
    protected String include_for_lineage__edit;

    /**
     * The 'inherits_lineage_setting_from_transformation_project' property, displayed as 'Inherits Lineage Setting from Transformation Project' in the IGC UI.
     */
    protected Boolean inherits_lineage_setting_from_transformation_project;

    /**
     * The 'blueprint_elements' property, displayed as 'Blueprint Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

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

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #references_local_or_shared_containers */ @JsonProperty("references_local_or_shared_containers")  public ReferenceList getReferencesLocalOrSharedContainers() { return this.references_local_or_shared_containers; }
    /** @see #references_local_or_shared_containers */ @JsonProperty("references_local_or_shared_containers")  public void setReferencesLocalOrSharedContainers(ReferenceList references_local_or_shared_containers) { this.references_local_or_shared_containers = references_local_or_shared_containers; }

    /** @see #sequences_jobs */ @JsonProperty("sequences_jobs")  public ReferenceList getSequencesJobs() { return this.sequences_jobs; }
    /** @see #sequences_jobs */ @JsonProperty("sequences_jobs")  public void setSequencesJobs(ReferenceList sequences_jobs) { this.sequences_jobs = sequences_jobs; }

    /** @see #folder */ @JsonProperty("folder")  public Reference getFolder() { return this.folder; }
    /** @see #folder */ @JsonProperty("folder")  public void setFolder(Reference folder) { this.folder = folder; }

    /** @see #design_parameters */ @JsonProperty("design_parameters")  public ReferenceList getDesignParameters() { return this.design_parameters; }
    /** @see #design_parameters */ @JsonProperty("design_parameters")  public void setDesignParameters(ReferenceList design_parameters) { this.design_parameters = design_parameters; }

    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public ReferenceList getParameterSets() { return this.parameter_sets; }
    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public void setParameterSets(ReferenceList parameter_sets) { this.parameter_sets = parameter_sets; }

    /** @see #annotations */ @JsonProperty("annotations")  public ArrayList<String> getAnnotations() { return this.annotations; }
    /** @see #annotations */ @JsonProperty("annotations")  public void setAnnotations(ArrayList<String> annotations) { this.annotations = annotations; }

    /** @see #job_runs */ @JsonProperty("job_runs")  public ReferenceList getJobRuns() { return this.job_runs; }
    /** @see #job_runs */ @JsonProperty("job_runs")  public void setJobRuns(ReferenceList job_runs) { this.job_runs = job_runs; }

    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public Boolean getIncludeForLineage() { return this.include_for_lineage; }
    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public void setIncludeForLineage(Boolean include_for_lineage) { this.include_for_lineage = include_for_lineage; }

    /** @see #include_for_lineage_description */ @JsonProperty("include_for_lineage_description")  public String getIncludeForLineageDescription() { return this.include_for_lineage_description; }
    /** @see #include_for_lineage_description */ @JsonProperty("include_for_lineage_description")  public void setIncludeForLineageDescription(String include_for_lineage_description) { this.include_for_lineage_description = include_for_lineage_description; }

    /** @see #include_for_lineage__edit */ @JsonProperty("include_for_lineage__edit")  public String getIncludeForLineageEdit() { return this.include_for_lineage__edit; }
    /** @see #include_for_lineage__edit */ @JsonProperty("include_for_lineage__edit")  public void setIncludeForLineageEdit(String include_for_lineage__edit) { this.include_for_lineage__edit = include_for_lineage__edit; }

    /** @see #inherits_lineage_setting_from_transformation_project */ @JsonProperty("inherits_lineage_setting_from_transformation_project")  public Boolean getInheritsLineageSettingFromTransformationProject() { return this.inherits_lineage_setting_from_transformation_project; }
    /** @see #inherits_lineage_setting_from_transformation_project */ @JsonProperty("inherits_lineage_setting_from_transformation_project")  public void setInheritsLineageSettingFromTransformationProject(Boolean inherits_lineage_setting_from_transformation_project) { this.inherits_lineage_setting_from_transformation_project = inherits_lineage_setting_from_transformation_project; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

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
        "annotations",
        "include_for_lineage",
        "include_for_lineage_description",
        "include_for_lineage__edit",
        "inherits_lineage_setting_from_transformation_project",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "stages",
        "references_local_or_shared_containers",
        "sequences_jobs",
        "design_parameters",
        "parameter_sets",
        "job_runs",
        "blueprint_elements",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "transformation_project",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "stages",
        "references_local_or_shared_containers",
        "sequences_jobs",
        "folder",
        "design_parameters",
        "parameter_sets",
        "annotations",
        "job_runs",
        "include_for_lineage",
        "include_for_lineage_description",
        "include_for_lineage__edit",
        "inherits_lineage_setting_from_transformation_project",
        "blueprint_elements",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isSequenceJob(Object obj) { return (obj.getClass() == SequenceJob.class); }

}
