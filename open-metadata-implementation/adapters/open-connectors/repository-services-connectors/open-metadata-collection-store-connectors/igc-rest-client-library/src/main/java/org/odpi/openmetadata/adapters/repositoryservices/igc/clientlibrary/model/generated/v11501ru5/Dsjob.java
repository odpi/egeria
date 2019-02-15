/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code dsjob} asset type in IGC, displayed as '{@literal Job}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsjob extends Reference {

    public static String getIgcTypeId() { return "dsjob"; }
    public static String getIgcTypeDisplayName() { return "Job"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code transformation_project} property, displayed as '{@literal Transformation Project}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

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
     * The {@code stages} property, displayed as '{@literal Stages}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The {@code references_local_or_shared_containers} property, displayed as '{@literal References Local or Shared Containers}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ReferencedContainer} objects.
     */
    protected ReferenceList references_local_or_shared_containers;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code annotations} property, displayed as '{@literal Annotations}' in the IGC UI.
     */
    protected ArrayList<String> annotations;

    /**
     * The {@code folder} property, displayed as '{@literal Folder}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The {@code reads_from_(static)} property, displayed as '{@literal Reads from (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(static)") protected ReferenceList reads_from__static_;

    /**
     * The {@code writes_to_(static)} property, displayed as '{@literal Writes to (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(static)") protected ReferenceList writes_to__static_;

    /**
     * The {@code reads_from_(design)} property, displayed as '{@literal Reads from (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(design)") protected ReferenceList reads_from__design_;

    /**
     * The {@code writes_to_(design)} property, displayed as '{@literal Writes to (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(design)") protected ReferenceList writes_to__design_;

    /**
     * The {@code reads_from_(operational)} property, displayed as '{@literal Reads from (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(operational)") protected ReferenceList reads_from__operational_;

    /**
     * The {@code writes_to_(operational)} property, displayed as '{@literal Writes to (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(operational)") protected ReferenceList writes_to__operational_;

    /**
     * The {@code reads_from_(user_defined)} property, displayed as '{@literal Reads from (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(user_defined)") protected ReferenceList reads_from__user_defined_;

    /**
     * The {@code writes_to_(user_defined)} property, displayed as '{@literal Writes to (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(user_defined)") protected ReferenceList writes_to__user_defined_;

    /**
     * The {@code impacted_by} property, displayed as '{@literal Impacted by}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacted_by;

    /**
     * The {@code impacts_on} property, displayed as '{@literal Impacts on}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacts_on;

    /**
     * The {@code design_parameters} property, displayed as '{@literal Design Parameters}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsparameterJob} objects.
     */
    protected ReferenceList design_parameters;

    /**
     * The {@code parameter_sets} property, displayed as '{@literal Parameter Sets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ParameterSetDefinition} objects.
     */
    protected ReferenceList parameter_sets;

    /**
     * The {@code references_table_definitions} property, displayed as '{@literal References Table Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList references_table_definitions;

    /**
     * The {@code runtime_column_propagation} property, displayed as '{@literal Runtime Column Propagation}' in the IGC UI.
     */
    protected Boolean runtime_column_propagation;

    /**
     * The {@code optimizes_job} property, displayed as '{@literal Optimizes Job}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList optimizes_job;

    /**
     * The {@code optimized_by_jobs} property, displayed as '{@literal Optimized by Jobs}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList optimized_by_jobs;

    /**
     * The {@code sequenced_by_jobs} property, displayed as '{@literal Sequenced by Jobs}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SequenceJob} objects.
     */
    protected ReferenceList sequenced_by_jobs;

    /**
     * The {@code job_runs} property, displayed as '{@literal Job Runs}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link JobRun} objects.
     */
    protected ReferenceList job_runs;

    /**
     * The {@code include_for_lineage} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
     */
    protected Boolean include_for_lineage;

    /**
     * The {@code include_for_lineage__edit} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
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
     * The {@code include_for_lineage_description} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
     */
    protected String include_for_lineage_description;

    /**
     * The {@code inherits_lineage_setting_from_transformation_project} property, displayed as '{@literal Inherits Lineage Setting from Transformation Project}' in the IGC UI.
     */
    protected Boolean inherits_lineage_setting_from_transformation_project;

    /**
     * The {@code lineage_service_last_run_date} property, displayed as '{@literal Lineage Service Last Run Date}' in the IGC UI.
     */
    protected ArrayList<Date> lineage_service_last_run_date;

    /**
     * The {@code lineage_service_status} property, displayed as '{@literal Lineage Service Status}' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_status;

    /**
     * The {@code lineage_service_information} property, displayed as '{@literal Lineage Service Information}' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_information;

    /**
     * The {@code mapping_specifications} property, displayed as '{@literal Mapping Specifications}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList mapping_specifications;

    /**
     * The {@code web_service_enabled} property, displayed as '{@literal Web Service Enabled}' in the IGC UI.
     */
    protected Boolean web_service_enabled;

    /**
     * The {@code information_services_operations} property, displayed as '{@literal Information Services Operations}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesOperation} objects.
     */
    protected ReferenceList information_services_operations;

    /**
     * The {@code blueprint_elements} property, displayed as '{@literal Blueprint Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

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

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #annotations */ @JsonProperty("annotations")  public ArrayList<String> getAnnotations() { return this.annotations; }
    /** @see #annotations */ @JsonProperty("annotations")  public void setAnnotations(ArrayList<String> annotations) { this.annotations = annotations; }

    /** @see #folder */ @JsonProperty("folder")  public Reference getFolder() { return this.folder; }
    /** @see #folder */ @JsonProperty("folder")  public void setFolder(Reference folder) { this.folder = folder; }

    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public ReferenceList getReadsFromStatic() { return this.reads_from__static_; }
    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public void setReadsFromStatic(ReferenceList reads_from__static_) { this.reads_from__static_ = reads_from__static_; }

    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public ReferenceList getWritesToStatic() { return this.writes_to__static_; }
    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public void setWritesToStatic(ReferenceList writes_to__static_) { this.writes_to__static_ = writes_to__static_; }

    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public ReferenceList getReadsFromDesign() { return this.reads_from__design_; }
    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public void setReadsFromDesign(ReferenceList reads_from__design_) { this.reads_from__design_ = reads_from__design_; }

    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public ReferenceList getWritesToDesign() { return this.writes_to__design_; }
    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public void setWritesToDesign(ReferenceList writes_to__design_) { this.writes_to__design_ = writes_to__design_; }

    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public ReferenceList getReadsFromOperational() { return this.reads_from__operational_; }
    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public void setReadsFromOperational(ReferenceList reads_from__operational_) { this.reads_from__operational_ = reads_from__operational_; }

    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public ReferenceList getWritesToOperational() { return this.writes_to__operational_; }
    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public void setWritesToOperational(ReferenceList writes_to__operational_) { this.writes_to__operational_ = writes_to__operational_; }

    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public ReferenceList getReadsFromUserDefined() { return this.reads_from__user_defined_; }
    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public void setReadsFromUserDefined(ReferenceList reads_from__user_defined_) { this.reads_from__user_defined_ = reads_from__user_defined_; }

    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public ReferenceList getWritesToUserDefined() { return this.writes_to__user_defined_; }
    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public void setWritesToUserDefined(ReferenceList writes_to__user_defined_) { this.writes_to__user_defined_ = writes_to__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

    /** @see #design_parameters */ @JsonProperty("design_parameters")  public ReferenceList getDesignParameters() { return this.design_parameters; }
    /** @see #design_parameters */ @JsonProperty("design_parameters")  public void setDesignParameters(ReferenceList design_parameters) { this.design_parameters = design_parameters; }

    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public ReferenceList getParameterSets() { return this.parameter_sets; }
    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public void setParameterSets(ReferenceList parameter_sets) { this.parameter_sets = parameter_sets; }

    /** @see #references_table_definitions */ @JsonProperty("references_table_definitions")  public ReferenceList getReferencesTableDefinitions() { return this.references_table_definitions; }
    /** @see #references_table_definitions */ @JsonProperty("references_table_definitions")  public void setReferencesTableDefinitions(ReferenceList references_table_definitions) { this.references_table_definitions = references_table_definitions; }

    /** @see #runtime_column_propagation */ @JsonProperty("runtime_column_propagation")  public Boolean getRuntimeColumnPropagation() { return this.runtime_column_propagation; }
    /** @see #runtime_column_propagation */ @JsonProperty("runtime_column_propagation")  public void setRuntimeColumnPropagation(Boolean runtime_column_propagation) { this.runtime_column_propagation = runtime_column_propagation; }

    /** @see #optimizes_job */ @JsonProperty("optimizes_job")  public ReferenceList getOptimizesJob() { return this.optimizes_job; }
    /** @see #optimizes_job */ @JsonProperty("optimizes_job")  public void setOptimizesJob(ReferenceList optimizes_job) { this.optimizes_job = optimizes_job; }

    /** @see #optimized_by_jobs */ @JsonProperty("optimized_by_jobs")  public ReferenceList getOptimizedByJobs() { return this.optimized_by_jobs; }
    /** @see #optimized_by_jobs */ @JsonProperty("optimized_by_jobs")  public void setOptimizedByJobs(ReferenceList optimized_by_jobs) { this.optimized_by_jobs = optimized_by_jobs; }

    /** @see #sequenced_by_jobs */ @JsonProperty("sequenced_by_jobs")  public ReferenceList getSequencedByJobs() { return this.sequenced_by_jobs; }
    /** @see #sequenced_by_jobs */ @JsonProperty("sequenced_by_jobs")  public void setSequencedByJobs(ReferenceList sequenced_by_jobs) { this.sequenced_by_jobs = sequenced_by_jobs; }

    /** @see #job_runs */ @JsonProperty("job_runs")  public ReferenceList getJobRuns() { return this.job_runs; }
    /** @see #job_runs */ @JsonProperty("job_runs")  public void setJobRuns(ReferenceList job_runs) { this.job_runs = job_runs; }

    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public Boolean getIncludeForLineage() { return this.include_for_lineage; }
    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public void setIncludeForLineage(Boolean include_for_lineage) { this.include_for_lineage = include_for_lineage; }

    /** @see #include_for_lineage__edit */ @JsonProperty("include_for_lineage__edit")  public String getIncludeForLineageEdit() { return this.include_for_lineage__edit; }
    /** @see #include_for_lineage__edit */ @JsonProperty("include_for_lineage__edit")  public void setIncludeForLineageEdit(String include_for_lineage__edit) { this.include_for_lineage__edit = include_for_lineage__edit; }

    /** @see #include_for_lineage_description */ @JsonProperty("include_for_lineage_description")  public String getIncludeForLineageDescription() { return this.include_for_lineage_description; }
    /** @see #include_for_lineage_description */ @JsonProperty("include_for_lineage_description")  public void setIncludeForLineageDescription(String include_for_lineage_description) { this.include_for_lineage_description = include_for_lineage_description; }

    /** @see #inherits_lineage_setting_from_transformation_project */ @JsonProperty("inherits_lineage_setting_from_transformation_project")  public Boolean getInheritsLineageSettingFromTransformationProject() { return this.inherits_lineage_setting_from_transformation_project; }
    /** @see #inherits_lineage_setting_from_transformation_project */ @JsonProperty("inherits_lineage_setting_from_transformation_project")  public void setInheritsLineageSettingFromTransformationProject(Boolean inherits_lineage_setting_from_transformation_project) { this.inherits_lineage_setting_from_transformation_project = inherits_lineage_setting_from_transformation_project; }

    /** @see #lineage_service_last_run_date */ @JsonProperty("lineage_service_last_run_date")  public ArrayList<Date> getLineageServiceLastRunDate() { return this.lineage_service_last_run_date; }
    /** @see #lineage_service_last_run_date */ @JsonProperty("lineage_service_last_run_date")  public void setLineageServiceLastRunDate(ArrayList<Date> lineage_service_last_run_date) { this.lineage_service_last_run_date = lineage_service_last_run_date; }

    /** @see #lineage_service_status */ @JsonProperty("lineage_service_status")  public ArrayList<String> getLineageServiceStatus() { return this.lineage_service_status; }
    /** @see #lineage_service_status */ @JsonProperty("lineage_service_status")  public void setLineageServiceStatus(ArrayList<String> lineage_service_status) { this.lineage_service_status = lineage_service_status; }

    /** @see #lineage_service_information */ @JsonProperty("lineage_service_information")  public ArrayList<String> getLineageServiceInformation() { return this.lineage_service_information; }
    /** @see #lineage_service_information */ @JsonProperty("lineage_service_information")  public void setLineageServiceInformation(ArrayList<String> lineage_service_information) { this.lineage_service_information = lineage_service_information; }

    /** @see #mapping_specifications */ @JsonProperty("mapping_specifications")  public ReferenceList getMappingSpecifications() { return this.mapping_specifications; }
    /** @see #mapping_specifications */ @JsonProperty("mapping_specifications")  public void setMappingSpecifications(ReferenceList mapping_specifications) { this.mapping_specifications = mapping_specifications; }

    /** @see #web_service_enabled */ @JsonProperty("web_service_enabled")  public Boolean getWebServiceEnabled() { return this.web_service_enabled; }
    /** @see #web_service_enabled */ @JsonProperty("web_service_enabled")  public void setWebServiceEnabled(Boolean web_service_enabled) { this.web_service_enabled = web_service_enabled; }

    /** @see #information_services_operations */ @JsonProperty("information_services_operations")  public ReferenceList getInformationServicesOperations() { return this.information_services_operations; }
    /** @see #information_services_operations */ @JsonProperty("information_services_operations")  public void setInformationServicesOperations(ReferenceList information_services_operations) { this.information_services_operations = information_services_operations; }

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
        "type",
        "annotations",
        "runtime_column_propagation",
        "include_for_lineage",
        "include_for_lineage__edit",
        "include_for_lineage_description",
        "inherits_lineage_setting_from_transformation_project",
        "lineage_service_last_run_date",
        "lineage_service_status",
        "lineage_service_information",
        "web_service_enabled",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "type",
        "annotations",
        "include_for_lineage_description",
        "lineage_service_status",
        "lineage_service_information",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "stages",
        "references_local_or_shared_containers",
        "reads_from_(static)",
        "writes_to_(static)",
        "reads_from_(design)",
        "writes_to_(design)",
        "reads_from_(operational)",
        "writes_to_(operational)",
        "reads_from_(user_defined)",
        "writes_to_(user_defined)",
        "impacted_by",
        "impacts_on",
        "design_parameters",
        "parameter_sets",
        "references_table_definitions",
        "optimizes_job",
        "optimized_by_jobs",
        "sequenced_by_jobs",
        "job_runs",
        "mapping_specifications",
        "information_services_operations",
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
        "type",
        "annotations",
        "folder",
        "reads_from_(static)",
        "writes_to_(static)",
        "reads_from_(design)",
        "writes_to_(design)",
        "reads_from_(operational)",
        "writes_to_(operational)",
        "reads_from_(user_defined)",
        "writes_to_(user_defined)",
        "impacted_by",
        "impacts_on",
        "design_parameters",
        "parameter_sets",
        "references_table_definitions",
        "runtime_column_propagation",
        "optimizes_job",
        "optimized_by_jobs",
        "sequenced_by_jobs",
        "job_runs",
        "include_for_lineage",
        "include_for_lineage__edit",
        "include_for_lineage_description",
        "inherits_lineage_setting_from_transformation_project",
        "lineage_service_last_run_date",
        "lineage_service_status",
        "lineage_service_information",
        "mapping_specifications",
        "web_service_enabled",
        "information_services_operations",
        "blueprint_elements",
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
    public static Boolean isDsjob(Object obj) { return (obj.getClass() == Dsjob.class); }

}
