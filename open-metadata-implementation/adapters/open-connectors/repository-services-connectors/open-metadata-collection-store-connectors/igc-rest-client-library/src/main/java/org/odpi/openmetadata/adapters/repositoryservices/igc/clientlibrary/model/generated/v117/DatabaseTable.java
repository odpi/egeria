/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'database_table' asset type in IGC, displayed as 'Database Table' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseTable extends Reference {

    public static String getIgcTypeId() { return "database_table"; }

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
     * The 'database_schema' property, displayed as 'Database Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DatabaseSchema} object.
     */
    protected Reference database_schema;

    /**
     * The 'qualityScore' property, displayed as 'Quality Score' in the IGC UI.
     */
    protected String qualityScore;

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
     * The 'database_columns' property, displayed as 'Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList database_columns;

    /**
     * The 'implements_logical_entities' property, displayed as 'Implements Logical Entities' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalEntity} objects.
     */
    protected ReferenceList implements_logical_entities;

    /**
     * The 'implements_design_tables_or_views' property, displayed as 'Implements Design Tables or Design Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList implements_design_tables_or_views;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'same_as_data_sources' property, displayed as 'Same as Data Sources' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList same_as_data_sources;

    /**
     * The 'referenced_by_views' property, displayed as 'Referenced by Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link View} objects.
     */
    protected ReferenceList referenced_by_views;

    /**
     * The 'database_aliases' property, displayed as 'Database Aliases' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList database_aliases;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'defined_primary_key' property, displayed as 'Defined Primary Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link CandidateKey} objects.
     */
    protected ReferenceList defined_primary_key;

    /**
     * The 'defined_non_primary_key' property, displayed as 'Defined Non Primary Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link CandidateKey} objects.
     */
    protected ReferenceList defined_non_primary_key;

    /**
     * The 'selected_primary_key' property, displayed as 'User Selected Primary Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList selected_primary_key;

    /**
     * The 'defined_foreign_key' property, displayed as 'Defined Foreign Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList defined_foreign_key;

    /**
     * The 'selected_foreign_key' property, displayed as 'User Selected Foreign Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_foreign_key;

    /**
     * The 'selected_natural_key' property, displayed as 'User Selected Natural Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList selected_natural_key;

    /**
     * The 'multi_column_analysis' property, displayed as 'Multi Column Analysis' in the IGC UI.
     */
    protected ArrayList<String> multi_column_analysis;

    /**
     * The 'database_indexes' property, displayed as 'Indexes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseIndex} objects.
     */
    protected ReferenceList database_indexes;

    /**
     * The 'indexes' property, displayed as 'Indexes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseIndex} objects.
     */
    protected ReferenceList indexes;

    /**
     * The 'reviewDate' property, displayed as 'Review Date' in the IGC UI.
     */
    protected ArrayList<Date> reviewDate;

    /**
     * The 'fieldCount' property, displayed as 'Number of Fields' in the IGC UI.
     */
    protected ArrayList<Number> fieldCount;

    /**
     * The 'rowCount' property, displayed as 'Number of Rows' in the IGC UI.
     */
    protected ArrayList<Number> rowCount;

    /**
     * The 'PKDuplicateCount' property, displayed as 'Primary Key Duplicates' in the IGC UI.
     */
    protected ArrayList<Number> PKDuplicateCount;

    /**
     * The 'FKViolationCount' property, displayed as 'Foreign Key Violations' in the IGC UI.
     */
    protected ArrayList<Number> FKViolationCount;

    /**
     * The 'nbRecordTested' property, displayed as 'Number of Records Tested' in the IGC UI.
     */
    protected ArrayList<Number> nbRecordTested;

    /**
     * The 'qualityScore_bubble' property, displayed as 'Quality Score' in the IGC UI.
     */
    protected String qualityScore_bubble;

    /**
     * The 'quality_dimension' property, displayed as 'Quality Dimensions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link QualityProblem} objects.
     */
    protected ReferenceList quality_dimension;

    /**
     * The 'table_definitions' property, displayed as 'Table Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList table_definitions;

    /**
     * The 'mapped_to_physical_objects' property, displayed as 'Mapped to Physical Objects' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalObject} objects.
     */
    protected ReferenceList mapped_to_physical_objects;

    /**
     * The 'read_by_(static)' property, displayed as 'Read by (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(static)") protected ReferenceList read_by__static_;

    /**
     * The 'written_by_(static)' property, displayed as 'Written by (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(static)") protected ReferenceList written_by__static_;

    /**
     * The 'read_by_(design)' property, displayed as 'Read by (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(design)") protected ReferenceList read_by__design_;

    /**
     * The 'written_by_(design)' property, displayed as 'Written by (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(design)") protected ReferenceList written_by__design_;

    /**
     * The 'read_by_(operational)' property, displayed as 'Read by (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(operational)") protected ReferenceList read_by__operational_;

    /**
     * The 'written_by_(operational)' property, displayed as 'Written by (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(operational)") protected ReferenceList written_by__operational_;

    /**
     * The 'read_by_(user_defined)' property, displayed as 'Read by (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(user_defined)") protected ReferenceList read_by__user_defined_;

    /**
     * The 'written_by_(user_defined)' property, displayed as 'Written by (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(user_defined)") protected ReferenceList written_by__user_defined_;

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
     * The 'bi_report_queries' property, displayed as 'BI Report Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList bi_report_queries;

    /**
     * The 'bi_model_collections' property, displayed as 'BI Model Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList bi_model_collections;

    /**
     * The 'source_mapping_specifications' property, displayed as 'Source Mapping Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingSpecification} objects.
     */
    protected ReferenceList source_mapping_specifications;

    /**
     * The 'target_mapping_specifications' property, displayed as 'Target Mapping Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingSpecification} objects.
     */
    protected ReferenceList target_mapping_specifications;

    /**
     * The 'suggested_term_assignments' property, displayed as 'Suggested Term Assignments' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TermAssignment} objects.
     */
    protected ReferenceList suggested_term_assignments;

    /**
     * The 'data_policies' property, displayed as 'Data Policies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList data_policies;

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

    /** @see #database_schema */ @JsonProperty("database_schema")  public Reference getDatabaseSchema() { return this.database_schema; }
    /** @see #database_schema */ @JsonProperty("database_schema")  public void setDatabaseSchema(Reference database_schema) { this.database_schema = database_schema; }

    /** @see #qualityScore */ @JsonProperty("qualityScore")  public String getQualityScore() { return this.qualityScore; }
    /** @see #qualityScore */ @JsonProperty("qualityScore")  public void setQualityScore(String qualityScore) { this.qualityScore = qualityScore; }

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

    /** @see #database_columns */ @JsonProperty("database_columns")  public ReferenceList getDatabaseColumns() { return this.database_columns; }
    /** @see #database_columns */ @JsonProperty("database_columns")  public void setDatabaseColumns(ReferenceList database_columns) { this.database_columns = database_columns; }

    /** @see #implements_logical_entities */ @JsonProperty("implements_logical_entities")  public ReferenceList getImplementsLogicalEntities() { return this.implements_logical_entities; }
    /** @see #implements_logical_entities */ @JsonProperty("implements_logical_entities")  public void setImplementsLogicalEntities(ReferenceList implements_logical_entities) { this.implements_logical_entities = implements_logical_entities; }

    /** @see #implements_design_tables_or_views */ @JsonProperty("implements_design_tables_or_views")  public ReferenceList getImplementsDesignTablesOrViews() { return this.implements_design_tables_or_views; }
    /** @see #implements_design_tables_or_views */ @JsonProperty("implements_design_tables_or_views")  public void setImplementsDesignTablesOrViews(ReferenceList implements_design_tables_or_views) { this.implements_design_tables_or_views = implements_design_tables_or_views; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public ReferenceList getSameAsDataSources() { return this.same_as_data_sources; }
    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public void setSameAsDataSources(ReferenceList same_as_data_sources) { this.same_as_data_sources = same_as_data_sources; }

    /** @see #referenced_by_views */ @JsonProperty("referenced_by_views")  public ReferenceList getReferencedByViews() { return this.referenced_by_views; }
    /** @see #referenced_by_views */ @JsonProperty("referenced_by_views")  public void setReferencedByViews(ReferenceList referenced_by_views) { this.referenced_by_views = referenced_by_views; }

    /** @see #database_aliases */ @JsonProperty("database_aliases")  public ReferenceList getDatabaseAliases() { return this.database_aliases; }
    /** @see #database_aliases */ @JsonProperty("database_aliases")  public void setDatabaseAliases(ReferenceList database_aliases) { this.database_aliases = database_aliases; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #defined_primary_key */ @JsonProperty("defined_primary_key")  public ReferenceList getDefinedPrimaryKey() { return this.defined_primary_key; }
    /** @see #defined_primary_key */ @JsonProperty("defined_primary_key")  public void setDefinedPrimaryKey(ReferenceList defined_primary_key) { this.defined_primary_key = defined_primary_key; }

    /** @see #defined_non_primary_key */ @JsonProperty("defined_non_primary_key")  public ReferenceList getDefinedNonPrimaryKey() { return this.defined_non_primary_key; }
    /** @see #defined_non_primary_key */ @JsonProperty("defined_non_primary_key")  public void setDefinedNonPrimaryKey(ReferenceList defined_non_primary_key) { this.defined_non_primary_key = defined_non_primary_key; }

    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public ReferenceList getSelectedPrimaryKey() { return this.selected_primary_key; }
    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public void setSelectedPrimaryKey(ReferenceList selected_primary_key) { this.selected_primary_key = selected_primary_key; }

    /** @see #defined_foreign_key */ @JsonProperty("defined_foreign_key")  public ReferenceList getDefinedForeignKey() { return this.defined_foreign_key; }
    /** @see #defined_foreign_key */ @JsonProperty("defined_foreign_key")  public void setDefinedForeignKey(ReferenceList defined_foreign_key) { this.defined_foreign_key = defined_foreign_key; }

    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public ReferenceList getSelectedForeignKey() { return this.selected_foreign_key; }
    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public void setSelectedForeignKey(ReferenceList selected_foreign_key) { this.selected_foreign_key = selected_foreign_key; }

    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public ReferenceList getSelectedNaturalKey() { return this.selected_natural_key; }
    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public void setSelectedNaturalKey(ReferenceList selected_natural_key) { this.selected_natural_key = selected_natural_key; }

    /** @see #multi_column_analysis */ @JsonProperty("multi_column_analysis")  public ArrayList<String> getMultiColumnAnalysis() { return this.multi_column_analysis; }
    /** @see #multi_column_analysis */ @JsonProperty("multi_column_analysis")  public void setMultiColumnAnalysis(ArrayList<String> multi_column_analysis) { this.multi_column_analysis = multi_column_analysis; }

    /** @see #database_indexes */ @JsonProperty("database_indexes")  public ReferenceList getDatabaseIndexes() { return this.database_indexes; }
    /** @see #database_indexes */ @JsonProperty("database_indexes")  public void setDatabaseIndexes(ReferenceList database_indexes) { this.database_indexes = database_indexes; }

    /** @see #indexes */ @JsonProperty("indexes")  public ReferenceList getIndexes() { return this.indexes; }
    /** @see #indexes */ @JsonProperty("indexes")  public void setIndexes(ReferenceList indexes) { this.indexes = indexes; }

    /** @see #reviewDate */ @JsonProperty("reviewDate")  public ArrayList<Date> getReviewDate() { return this.reviewDate; }
    /** @see #reviewDate */ @JsonProperty("reviewDate")  public void setReviewDate(ArrayList<Date> reviewDate) { this.reviewDate = reviewDate; }

    /** @see #fieldCount */ @JsonProperty("fieldCount")  public ArrayList<Number> getFieldCount() { return this.fieldCount; }
    /** @see #fieldCount */ @JsonProperty("fieldCount")  public void setFieldCount(ArrayList<Number> fieldCount) { this.fieldCount = fieldCount; }

    /** @see #rowCount */ @JsonProperty("rowCount")  public ArrayList<Number> getRowCount() { return this.rowCount; }
    /** @see #rowCount */ @JsonProperty("rowCount")  public void setRowCount(ArrayList<Number> rowCount) { this.rowCount = rowCount; }

    /** @see #PKDuplicateCount */ @JsonProperty("PKDuplicateCount")  public ArrayList<Number> getPkDuplicateCount() { return this.PKDuplicateCount; }
    /** @see #PKDuplicateCount */ @JsonProperty("PKDuplicateCount")  public void setPkDuplicateCount(ArrayList<Number> PKDuplicateCount) { this.PKDuplicateCount = PKDuplicateCount; }

    /** @see #FKViolationCount */ @JsonProperty("FKViolationCount")  public ArrayList<Number> getFkViolationCount() { return this.FKViolationCount; }
    /** @see #FKViolationCount */ @JsonProperty("FKViolationCount")  public void setFkViolationCount(ArrayList<Number> FKViolationCount) { this.FKViolationCount = FKViolationCount; }

    /** @see #nbRecordTested */ @JsonProperty("nbRecordTested")  public ArrayList<Number> getNbRecordTested() { return this.nbRecordTested; }
    /** @see #nbRecordTested */ @JsonProperty("nbRecordTested")  public void setNbRecordTested(ArrayList<Number> nbRecordTested) { this.nbRecordTested = nbRecordTested; }

    /** @see #qualityScore_bubble */ @JsonProperty("qualityScore_bubble")  public String getQualityScoreBubble() { return this.qualityScore_bubble; }
    /** @see #qualityScore_bubble */ @JsonProperty("qualityScore_bubble")  public void setQualityScoreBubble(String qualityScore_bubble) { this.qualityScore_bubble = qualityScore_bubble; }

    /** @see #quality_dimension */ @JsonProperty("quality_dimension")  public ReferenceList getQualityDimension() { return this.quality_dimension; }
    /** @see #quality_dimension */ @JsonProperty("quality_dimension")  public void setQualityDimension(ReferenceList quality_dimension) { this.quality_dimension = quality_dimension; }

    /** @see #table_definitions */ @JsonProperty("table_definitions")  public ReferenceList getTableDefinitions() { return this.table_definitions; }
    /** @see #table_definitions */ @JsonProperty("table_definitions")  public void setTableDefinitions(ReferenceList table_definitions) { this.table_definitions = table_definitions; }

    /** @see #mapped_to_physical_objects */ @JsonProperty("mapped_to_physical_objects")  public ReferenceList getMappedToPhysicalObjects() { return this.mapped_to_physical_objects; }
    /** @see #mapped_to_physical_objects */ @JsonProperty("mapped_to_physical_objects")  public void setMappedToPhysicalObjects(ReferenceList mapped_to_physical_objects) { this.mapped_to_physical_objects = mapped_to_physical_objects; }

    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public ReferenceList getReadByStatic() { return this.read_by__static_; }
    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public void setReadByStatic(ReferenceList read_by__static_) { this.read_by__static_ = read_by__static_; }

    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public ReferenceList getWrittenByStatic() { return this.written_by__static_; }
    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public void setWrittenByStatic(ReferenceList written_by__static_) { this.written_by__static_ = written_by__static_; }

    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public ReferenceList getReadByDesign() { return this.read_by__design_; }
    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public void setReadByDesign(ReferenceList read_by__design_) { this.read_by__design_ = read_by__design_; }

    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public ReferenceList getWrittenByDesign() { return this.written_by__design_; }
    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public void setWrittenByDesign(ReferenceList written_by__design_) { this.written_by__design_ = written_by__design_; }

    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public ReferenceList getReadByOperational() { return this.read_by__operational_; }
    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public void setReadByOperational(ReferenceList read_by__operational_) { this.read_by__operational_ = read_by__operational_; }

    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public ReferenceList getWrittenByOperational() { return this.written_by__operational_; }
    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public void setWrittenByOperational(ReferenceList written_by__operational_) { this.written_by__operational_ = written_by__operational_; }

    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public ReferenceList getReadByUserDefined() { return this.read_by__user_defined_; }
    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public void setReadByUserDefined(ReferenceList read_by__user_defined_) { this.read_by__user_defined_ = read_by__user_defined_; }

    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public ReferenceList getWrittenByUserDefined() { return this.written_by__user_defined_; }
    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public void setWrittenByUserDefined(ReferenceList written_by__user_defined_) { this.written_by__user_defined_ = written_by__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public ReferenceList getBiReportQueries() { return this.bi_report_queries; }
    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public void setBiReportQueries(ReferenceList bi_report_queries) { this.bi_report_queries = bi_report_queries; }

    /** @see #bi_model_collections */ @JsonProperty("bi_model_collections")  public ReferenceList getBiModelCollections() { return this.bi_model_collections; }
    /** @see #bi_model_collections */ @JsonProperty("bi_model_collections")  public void setBiModelCollections(ReferenceList bi_model_collections) { this.bi_model_collections = bi_model_collections; }

    /** @see #source_mapping_specifications */ @JsonProperty("source_mapping_specifications")  public ReferenceList getSourceMappingSpecifications() { return this.source_mapping_specifications; }
    /** @see #source_mapping_specifications */ @JsonProperty("source_mapping_specifications")  public void setSourceMappingSpecifications(ReferenceList source_mapping_specifications) { this.source_mapping_specifications = source_mapping_specifications; }

    /** @see #target_mapping_specifications */ @JsonProperty("target_mapping_specifications")  public ReferenceList getTargetMappingSpecifications() { return this.target_mapping_specifications; }
    /** @see #target_mapping_specifications */ @JsonProperty("target_mapping_specifications")  public void setTargetMappingSpecifications(ReferenceList target_mapping_specifications) { this.target_mapping_specifications = target_mapping_specifications; }

    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public ReferenceList getSuggestedTermAssignments() { return this.suggested_term_assignments; }
    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public void setSuggestedTermAssignments(ReferenceList suggested_term_assignments) { this.suggested_term_assignments = suggested_term_assignments; }

    /** @see #data_policies */ @JsonProperty("data_policies")  public ReferenceList getDataPolicies() { return this.data_policies; }
    /** @see #data_policies */ @JsonProperty("data_policies")  public void setDataPolicies(ReferenceList data_policies) { this.data_policies = data_policies; }

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

    public static final Boolean isDatabaseTable(Object obj) { return (obj.getClass() == DatabaseTable.class); }

}
