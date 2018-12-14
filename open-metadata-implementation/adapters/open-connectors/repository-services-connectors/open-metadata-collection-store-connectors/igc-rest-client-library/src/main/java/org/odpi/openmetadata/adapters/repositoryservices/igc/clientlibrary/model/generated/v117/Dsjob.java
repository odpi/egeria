/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsjob' asset type in IGC, displayed as 'Job' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsjob extends MainObject {

    public static final String IGC_TYPE_ID = "dsjob";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

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
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'annotations' property, displayed as 'Annotations' in the IGC UI.
     */
    protected ArrayList<String> annotations;

    /**
     * The 'folder' property, displayed as 'Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The 'reads_from_(static)' property, displayed as 'Reads from (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(static)") protected ReferenceList reads_from__static_;

    /**
     * The 'writes_to_(static)' property, displayed as 'Writes to (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(static)") protected ReferenceList writes_to__static_;

    /**
     * The 'reads_from_(design)' property, displayed as 'Reads from (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(design)") protected ReferenceList reads_from__design_;

    /**
     * The 'writes_to_(design)' property, displayed as 'Writes to (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(design)") protected ReferenceList writes_to__design_;

    /**
     * The 'reads_from_(operational)' property, displayed as 'Reads from (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(operational)") protected ReferenceList reads_from__operational_;

    /**
     * The 'writes_to_(operational)' property, displayed as 'Writes to (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(operational)") protected ReferenceList writes_to__operational_;

    /**
     * The 'reads_from_(user_defined)' property, displayed as 'Reads from (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(user_defined)") protected ReferenceList reads_from__user_defined_;

    /**
     * The 'writes_to_(user_defined)' property, displayed as 'Writes to (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(user_defined)") protected ReferenceList writes_to__user_defined_;

    /**
     * The 'impacted_by' property, displayed as 'Impacted by' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacted_by;

    /**
     * The 'impacts_on' property, displayed as 'Impacts on' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacts_on;

    /**
     * The 'design_parameters' property, displayed as 'Design Parameters' in the IGC UI.
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
     * The 'references_table_definitions' property, displayed as 'References Table Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList references_table_definitions;

    /**
     * The 'runtime_column_propagation' property, displayed as 'Runtime Column Propagation' in the IGC UI.
     */
    protected Boolean runtime_column_propagation;

    /**
     * The 'optimizes_job' property, displayed as 'Optimizes Job' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList optimizes_job;

    /**
     * The 'optimized_by_jobs' property, displayed as 'Optimized by Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList optimized_by_jobs;

    /**
     * The 'sequenced_by_jobs' property, displayed as 'Sequenced by Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SequenceJob} objects.
     */
    protected ReferenceList sequenced_by_jobs;

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
     * The 'include_for_lineage_description' property, displayed as 'Include for Lineage' in the IGC UI.
     */
    protected String include_for_lineage_description;

    /**
     * The 'inherits_lineage_setting_from_transformation_project' property, displayed as 'Inherits Lineage Setting from Transformation Project' in the IGC UI.
     */
    protected Boolean inherits_lineage_setting_from_transformation_project;

    /**
     * The 'lineage_service_last_run_date' property, displayed as 'Lineage Service Last Run Date' in the IGC UI.
     */
    protected ArrayList<Date> lineage_service_last_run_date;

    /**
     * The 'lineage_service_status' property, displayed as 'Lineage Service Status' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_status;

    /**
     * The 'lineage_service_information' property, displayed as 'Lineage Service Information' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_information;

    /**
     * The 'mapping_specifications' property, displayed as 'Mapping Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList mapping_specifications;

    /**
     * The 'web_service_enabled' property, displayed as 'Web Service Enabled' in the IGC UI.
     */
    protected Boolean web_service_enabled;

    /**
     * The 'information_services_operations' property, displayed as 'Information Services Operations' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesOperation} objects.
     */
    protected ReferenceList information_services_operations;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

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

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDsjob(Object obj) { return (obj.getClass() == Dsjob.class); }

}
