/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'stage' asset type in IGC, displayed as 'Stage' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Stage extends Reference {

    public static String getIgcTypeId() { return "stage"; }
    public static String getIgcTypeDisplayName() { return "Stage"; }

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
     * The 'job_or_container' property, displayed as 'Job or Container' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList job_or_container;

    /**
     * The 'job' property, displayed as 'Job' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList job;

    /**
     * The 'shared_or_local_container' property, displayed as 'Shared or Local Container' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ReferencedContainer} object.
     */
    protected Reference shared_or_local_container;

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
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsstageType} object.
     */
    protected Reference type;

    /**
     * The 'type_definition' property, displayed as 'Type Definition' in the IGC UI.
     */
    protected String type_definition;

    /**
     * The 'constraints' property, displayed as 'Constraints' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link JobConstraint} objects.
     */
    protected ReferenceList constraints;

    /**
     * The 'references_container' property, displayed as 'References Container' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ReferencedContainer} object.
     */
    protected Reference references_container;

    /**
     * The 'stage_variable' property, displayed as 'Stage Variables' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StageVariable} objects.
     */
    protected ReferenceList stage_variable;

    /**
     * The 'input_links' property, displayed as 'Input Links' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Link} objects.
     */
    protected ReferenceList input_links;

    /**
     * The 'output_links' property, displayed as 'Output Links' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Link} objects.
     */
    protected ReferenceList output_links;

    /**
     * The 'next_stages' property, displayed as 'Next Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList next_stages;

    /**
     * The 'previous_stages' property, displayed as 'Previous Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList previous_stages;

    /**
     * The 'manually_bound_to' property, displayed as 'Manually Bound To' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList manually_bound_to;

    /**
     * The 'references_data_connection_mapping' property, displayed as 'References Data Connection Mapping' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnectionMapping} objects.
     */
    protected ReferenceList references_data_connection_mapping;

    /**
     * The 'runs_sequences_jobs' property, displayed as 'Runs Sequences Jobs' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference runs_sequences_jobs;

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
     * The 'references_table_definitions' property, displayed as 'References Table Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList references_table_definitions;

    /**
     * The 'file' property, displayed as 'File' in the IGC UI.
     */
    protected String file;

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     */
    protected String host;

    /**
     * The 'data_source_or_server' property, displayed as 'Data Source or Server' in the IGC UI.
     */
    protected String data_source_or_server;

    /**
     * The 'schema' property, displayed as 'Schema' in the IGC UI.
     */
    protected String schema;

    /**
     * The 'table' property, displayed as 'Table' in the IGC UI.
     */
    protected String table;

    /**
     * The 'sql_statement' property, displayed as 'SQL Statement' in the IGC UI.
     */
    protected String sql_statement;

    /**
     * The 'insert_sql' property, displayed as 'Insert SQL' in the IGC UI.
     */
    protected String insert_sql;

    /**
     * The 'update_sql' property, displayed as 'Update SQL' in the IGC UI.
     */
    protected String update_sql;

    /**
     * The 'delete_sql' property, displayed as 'Delete SQL' in the IGC UI.
     */
    protected String delete_sql;

    /**
     * The 'before_sql' property, displayed as 'Before SQL' in the IGC UI.
     */
    protected String before_sql;

    /**
     * The 'after_sql' property, displayed as 'After SQL' in the IGC UI.
     */
    protected String after_sql;

    /**
     * The 'all_sql_statements' property, displayed as 'All SQL Statements' in the IGC UI.
     */
    protected String all_sql_statements;

    /**
     * The 'standardization_rule_sets' property, displayed as 'Standardization Rule Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationRuleSet} objects.
     */
    protected ReferenceList standardization_rule_sets;

    /**
     * The 'match_specifications' property, displayed as 'Match Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MatchSpecification} objects.
     */
    protected ReferenceList match_specifications;

    /**
     * The 'data_rule_definition' property, displayed as 'Data Rule Definition' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StageDataRuleDefinition} objects.
     */
    protected ReferenceList data_rule_definition;

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

    /** @see #job_or_container */ @JsonProperty("job_or_container")  public ReferenceList getJobOrContainer() { return this.job_or_container; }
    /** @see #job_or_container */ @JsonProperty("job_or_container")  public void setJobOrContainer(ReferenceList job_or_container) { this.job_or_container = job_or_container; }

    /** @see #job */ @JsonProperty("job")  public ReferenceList getJob() { return this.job; }
    /** @see #job */ @JsonProperty("job")  public void setJob(ReferenceList job) { this.job = job; }

    /** @see #shared_or_local_container */ @JsonProperty("shared_or_local_container")  public Reference getSharedOrLocalContainer() { return this.shared_or_local_container; }
    /** @see #shared_or_local_container */ @JsonProperty("shared_or_local_container")  public void setSharedOrLocalContainer(Reference shared_or_local_container) { this.shared_or_local_container = shared_or_local_container; }

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

    /** @see #type */ @JsonProperty("type")  public Reference getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(Reference type) { this.type = type; }

    /** @see #type_definition */ @JsonProperty("type_definition")  public String getTypeDefinition() { return this.type_definition; }
    /** @see #type_definition */ @JsonProperty("type_definition")  public void setTypeDefinition(String type_definition) { this.type_definition = type_definition; }

    /** @see #constraints */ @JsonProperty("constraints")  public ReferenceList getConstraints() { return this.constraints; }
    /** @see #constraints */ @JsonProperty("constraints")  public void setConstraints(ReferenceList constraints) { this.constraints = constraints; }

    /** @see #references_container */ @JsonProperty("references_container")  public Reference getReferencesContainer() { return this.references_container; }
    /** @see #references_container */ @JsonProperty("references_container")  public void setReferencesContainer(Reference references_container) { this.references_container = references_container; }

    /** @see #stage_variable */ @JsonProperty("stage_variable")  public ReferenceList getStageVariable() { return this.stage_variable; }
    /** @see #stage_variable */ @JsonProperty("stage_variable")  public void setStageVariable(ReferenceList stage_variable) { this.stage_variable = stage_variable; }

    /** @see #input_links */ @JsonProperty("input_links")  public ReferenceList getInputLinks() { return this.input_links; }
    /** @see #input_links */ @JsonProperty("input_links")  public void setInputLinks(ReferenceList input_links) { this.input_links = input_links; }

    /** @see #output_links */ @JsonProperty("output_links")  public ReferenceList getOutputLinks() { return this.output_links; }
    /** @see #output_links */ @JsonProperty("output_links")  public void setOutputLinks(ReferenceList output_links) { this.output_links = output_links; }

    /** @see #next_stages */ @JsonProperty("next_stages")  public ReferenceList getNextStages() { return this.next_stages; }
    /** @see #next_stages */ @JsonProperty("next_stages")  public void setNextStages(ReferenceList next_stages) { this.next_stages = next_stages; }

    /** @see #previous_stages */ @JsonProperty("previous_stages")  public ReferenceList getPreviousStages() { return this.previous_stages; }
    /** @see #previous_stages */ @JsonProperty("previous_stages")  public void setPreviousStages(ReferenceList previous_stages) { this.previous_stages = previous_stages; }

    /** @see #manually_bound_to */ @JsonProperty("manually_bound_to")  public ReferenceList getManuallyBoundTo() { return this.manually_bound_to; }
    /** @see #manually_bound_to */ @JsonProperty("manually_bound_to")  public void setManuallyBoundTo(ReferenceList manually_bound_to) { this.manually_bound_to = manually_bound_to; }

    /** @see #references_data_connection_mapping */ @JsonProperty("references_data_connection_mapping")  public ReferenceList getReferencesDataConnectionMapping() { return this.references_data_connection_mapping; }
    /** @see #references_data_connection_mapping */ @JsonProperty("references_data_connection_mapping")  public void setReferencesDataConnectionMapping(ReferenceList references_data_connection_mapping) { this.references_data_connection_mapping = references_data_connection_mapping; }

    /** @see #runs_sequences_jobs */ @JsonProperty("runs_sequences_jobs")  public Reference getRunsSequencesJobs() { return this.runs_sequences_jobs; }
    /** @see #runs_sequences_jobs */ @JsonProperty("runs_sequences_jobs")  public void setRunsSequencesJobs(Reference runs_sequences_jobs) { this.runs_sequences_jobs = runs_sequences_jobs; }

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

    /** @see #references_table_definitions */ @JsonProperty("references_table_definitions")  public ReferenceList getReferencesTableDefinitions() { return this.references_table_definitions; }
    /** @see #references_table_definitions */ @JsonProperty("references_table_definitions")  public void setReferencesTableDefinitions(ReferenceList references_table_definitions) { this.references_table_definitions = references_table_definitions; }

    /** @see #file */ @JsonProperty("file")  public String getFile() { return this.file; }
    /** @see #file */ @JsonProperty("file")  public void setFile(String file) { this.file = file; }

    /** @see #host */ @JsonProperty("host")  public String getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(String host) { this.host = host; }

    /** @see #data_source_or_server */ @JsonProperty("data_source_or_server")  public String getDataSourceOrServer() { return this.data_source_or_server; }
    /** @see #data_source_or_server */ @JsonProperty("data_source_or_server")  public void setDataSourceOrServer(String data_source_or_server) { this.data_source_or_server = data_source_or_server; }

    /** @see #schema */ @JsonProperty("schema")  public String getSchema() { return this.schema; }
    /** @see #schema */ @JsonProperty("schema")  public void setSchema(String schema) { this.schema = schema; }

    /** @see #table */ @JsonProperty("table")  public String getTable() { return this.table; }
    /** @see #table */ @JsonProperty("table")  public void setTable(String table) { this.table = table; }

    /** @see #sql_statement */ @JsonProperty("sql_statement")  public String getSqlStatement() { return this.sql_statement; }
    /** @see #sql_statement */ @JsonProperty("sql_statement")  public void setSqlStatement(String sql_statement) { this.sql_statement = sql_statement; }

    /** @see #insert_sql */ @JsonProperty("insert_sql")  public String getInsertSql() { return this.insert_sql; }
    /** @see #insert_sql */ @JsonProperty("insert_sql")  public void setInsertSql(String insert_sql) { this.insert_sql = insert_sql; }

    /** @see #update_sql */ @JsonProperty("update_sql")  public String getUpdateSql() { return this.update_sql; }
    /** @see #update_sql */ @JsonProperty("update_sql")  public void setUpdateSql(String update_sql) { this.update_sql = update_sql; }

    /** @see #delete_sql */ @JsonProperty("delete_sql")  public String getDeleteSql() { return this.delete_sql; }
    /** @see #delete_sql */ @JsonProperty("delete_sql")  public void setDeleteSql(String delete_sql) { this.delete_sql = delete_sql; }

    /** @see #before_sql */ @JsonProperty("before_sql")  public String getBeforeSql() { return this.before_sql; }
    /** @see #before_sql */ @JsonProperty("before_sql")  public void setBeforeSql(String before_sql) { this.before_sql = before_sql; }

    /** @see #after_sql */ @JsonProperty("after_sql")  public String getAfterSql() { return this.after_sql; }
    /** @see #after_sql */ @JsonProperty("after_sql")  public void setAfterSql(String after_sql) { this.after_sql = after_sql; }

    /** @see #all_sql_statements */ @JsonProperty("all_sql_statements")  public String getAllSqlStatements() { return this.all_sql_statements; }
    /** @see #all_sql_statements */ @JsonProperty("all_sql_statements")  public void setAllSqlStatements(String all_sql_statements) { this.all_sql_statements = all_sql_statements; }

    /** @see #standardization_rule_sets */ @JsonProperty("standardization_rule_sets")  public ReferenceList getStandardizationRuleSets() { return this.standardization_rule_sets; }
    /** @see #standardization_rule_sets */ @JsonProperty("standardization_rule_sets")  public void setStandardizationRuleSets(ReferenceList standardization_rule_sets) { this.standardization_rule_sets = standardization_rule_sets; }

    /** @see #match_specifications */ @JsonProperty("match_specifications")  public ReferenceList getMatchSpecifications() { return this.match_specifications; }
    /** @see #match_specifications */ @JsonProperty("match_specifications")  public void setMatchSpecifications(ReferenceList match_specifications) { this.match_specifications = match_specifications; }

    /** @see #data_rule_definition */ @JsonProperty("data_rule_definition")  public ReferenceList getDataRuleDefinition() { return this.data_rule_definition; }
    /** @see #data_rule_definition */ @JsonProperty("data_rule_definition")  public void setDataRuleDefinition(ReferenceList data_rule_definition) { this.data_rule_definition = data_rule_definition; }

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
        "type_definition",
        "file",
        "host",
        "data_source_or_server",
        "schema",
        "table",
        "sql_statement",
        "insert_sql",
        "update_sql",
        "delete_sql",
        "before_sql",
        "after_sql",
        "all_sql_statements",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "job_or_container",
        "job",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "constraints",
        "stage_variable",
        "input_links",
        "output_links",
        "next_stages",
        "previous_stages",
        "manually_bound_to",
        "references_data_connection_mapping",
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
        "references_table_definitions",
        "standardization_rule_sets",
        "match_specifications",
        "data_rule_definition",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "job_or_container",
        "job",
        "shared_or_local_container",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "type",
        "type_definition",
        "constraints",
        "references_container",
        "stage_variable",
        "input_links",
        "output_links",
        "next_stages",
        "previous_stages",
        "manually_bound_to",
        "references_data_connection_mapping",
        "runs_sequences_jobs",
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
        "references_table_definitions",
        "file",
        "host",
        "data_source_or_server",
        "schema",
        "table",
        "sql_statement",
        "insert_sql",
        "update_sql",
        "delete_sql",
        "before_sql",
        "after_sql",
        "all_sql_statements",
        "standardization_rule_sets",
        "match_specifications",
        "data_rule_definition",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isStage(Object obj) { return (obj.getClass() == Stage.class); }

}
