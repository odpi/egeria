/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_collection' asset type in IGC, displayed as 'BI Collection' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiCollection extends MainObject {

    public static final String IGC_TYPE_ID = "bi_collection";

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'bi_model_or_bi_collection' property, displayed as 'BI Model or BI Collection' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Olapobject} objects.
     */
    protected ReferenceList bi_model_or_bi_collection;

    /**
     * The 'bi_model' property, displayed as 'BI Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The 'bi_collection' property, displayed as 'BI Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollection} object.
     */
    protected Reference bi_collection;

    /**
     * The 'has_olap_collection' property, displayed as 'Has OLAP Collection' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList has_olap_collection;

    /**
     * The 'bi_collection_members' property, displayed as 'BI Collection Members' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollectionMember} objects.
     */
    protected ReferenceList bi_collection_members;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'type_definition' property, displayed as 'Type Definition' in the IGC UI.
     */
    protected String type_definition;

    /**
     * The 'filter_expression' property, displayed as 'Filter Expression' in the IGC UI.
     */
    protected ArrayList<String> filter_expression;

    /**
     * The 'join_condition' property, displayed as 'Join Condition' in the IGC UI.
     */
    protected ArrayList<String> join_condition;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'referenced_by_bi_hierarchies' property, displayed as 'Referenced by BI Hierarchies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiHierarchy} objects.
     */
    protected ReferenceList referenced_by_bi_hierarchies;

    /**
     * The 'bi_hierarchies' property, displayed as 'BI Hierarchies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiHierarchy} objects.
     */
    protected ReferenceList bi_hierarchies;

    /**
     * The 'bi_levels' property, displayed as 'BI Levels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList bi_levels;

    /**
     * The 'bi_filters' property, displayed as 'BI Filters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiFilter} objects.
     */
    protected ReferenceList bi_filters;

    /**
     * The 'references_bi_collections' property, displayed as 'References BI Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList references_bi_collections;

    /**
     * The 'referenced_by_bi_collection' property, displayed as 'Referenced by BI Collection' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList referenced_by_bi_collection;

    /**
     * The 'used_by_bi_report_queries' property, displayed as 'Used by BI Report Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList used_by_bi_report_queries;

    /**
     * The 'used_by_bi_cubes' property, displayed as 'Used by BI Cubes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCube} objects.
     */
    protected ReferenceList used_by_bi_cubes;

    /**
     * The 'uses_database_tables_or_views' property, displayed as 'Uses Database Tables or Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList uses_database_tables_or_views;

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


    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #bi_model_or_bi_collection */ @JsonProperty("bi_model_or_bi_collection")  public ReferenceList getBiModelOrBiCollection() { return this.bi_model_or_bi_collection; }
    /** @see #bi_model_or_bi_collection */ @JsonProperty("bi_model_or_bi_collection")  public void setBiModelOrBiCollection(ReferenceList bi_model_or_bi_collection) { this.bi_model_or_bi_collection = bi_model_or_bi_collection; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #bi_collection */ @JsonProperty("bi_collection")  public Reference getBiCollection() { return this.bi_collection; }
    /** @see #bi_collection */ @JsonProperty("bi_collection")  public void setBiCollection(Reference bi_collection) { this.bi_collection = bi_collection; }

    /** @see #has_olap_collection */ @JsonProperty("has_olap_collection")  public ReferenceList getHasOlapCollection() { return this.has_olap_collection; }
    /** @see #has_olap_collection */ @JsonProperty("has_olap_collection")  public void setHasOlapCollection(ReferenceList has_olap_collection) { this.has_olap_collection = has_olap_collection; }

    /** @see #bi_collection_members */ @JsonProperty("bi_collection_members")  public ReferenceList getBiCollectionMembers() { return this.bi_collection_members; }
    /** @see #bi_collection_members */ @JsonProperty("bi_collection_members")  public void setBiCollectionMembers(ReferenceList bi_collection_members) { this.bi_collection_members = bi_collection_members; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #type_definition */ @JsonProperty("type_definition")  public String getTypeDefinition() { return this.type_definition; }
    /** @see #type_definition */ @JsonProperty("type_definition")  public void setTypeDefinition(String type_definition) { this.type_definition = type_definition; }

    /** @see #filter_expression */ @JsonProperty("filter_expression")  public ArrayList<String> getFilterExpression() { return this.filter_expression; }
    /** @see #filter_expression */ @JsonProperty("filter_expression")  public void setFilterExpression(ArrayList<String> filter_expression) { this.filter_expression = filter_expression; }

    /** @see #join_condition */ @JsonProperty("join_condition")  public ArrayList<String> getJoinCondition() { return this.join_condition; }
    /** @see #join_condition */ @JsonProperty("join_condition")  public void setJoinCondition(ArrayList<String> join_condition) { this.join_condition = join_condition; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #referenced_by_bi_hierarchies */ @JsonProperty("referenced_by_bi_hierarchies")  public ReferenceList getReferencedByBiHierarchies() { return this.referenced_by_bi_hierarchies; }
    /** @see #referenced_by_bi_hierarchies */ @JsonProperty("referenced_by_bi_hierarchies")  public void setReferencedByBiHierarchies(ReferenceList referenced_by_bi_hierarchies) { this.referenced_by_bi_hierarchies = referenced_by_bi_hierarchies; }

    /** @see #bi_hierarchies */ @JsonProperty("bi_hierarchies")  public ReferenceList getBiHierarchies() { return this.bi_hierarchies; }
    /** @see #bi_hierarchies */ @JsonProperty("bi_hierarchies")  public void setBiHierarchies(ReferenceList bi_hierarchies) { this.bi_hierarchies = bi_hierarchies; }

    /** @see #bi_levels */ @JsonProperty("bi_levels")  public ReferenceList getBiLevels() { return this.bi_levels; }
    /** @see #bi_levels */ @JsonProperty("bi_levels")  public void setBiLevels(ReferenceList bi_levels) { this.bi_levels = bi_levels; }

    /** @see #bi_filters */ @JsonProperty("bi_filters")  public ReferenceList getBiFilters() { return this.bi_filters; }
    /** @see #bi_filters */ @JsonProperty("bi_filters")  public void setBiFilters(ReferenceList bi_filters) { this.bi_filters = bi_filters; }

    /** @see #references_bi_collections */ @JsonProperty("references_bi_collections")  public ReferenceList getReferencesBiCollections() { return this.references_bi_collections; }
    /** @see #references_bi_collections */ @JsonProperty("references_bi_collections")  public void setReferencesBiCollections(ReferenceList references_bi_collections) { this.references_bi_collections = references_bi_collections; }

    /** @see #referenced_by_bi_collection */ @JsonProperty("referenced_by_bi_collection")  public ReferenceList getReferencedByBiCollection() { return this.referenced_by_bi_collection; }
    /** @see #referenced_by_bi_collection */ @JsonProperty("referenced_by_bi_collection")  public void setReferencedByBiCollection(ReferenceList referenced_by_bi_collection) { this.referenced_by_bi_collection = referenced_by_bi_collection; }

    /** @see #used_by_bi_report_queries */ @JsonProperty("used_by_bi_report_queries")  public ReferenceList getUsedByBiReportQueries() { return this.used_by_bi_report_queries; }
    /** @see #used_by_bi_report_queries */ @JsonProperty("used_by_bi_report_queries")  public void setUsedByBiReportQueries(ReferenceList used_by_bi_report_queries) { this.used_by_bi_report_queries = used_by_bi_report_queries; }

    /** @see #used_by_bi_cubes */ @JsonProperty("used_by_bi_cubes")  public ReferenceList getUsedByBiCubes() { return this.used_by_bi_cubes; }
    /** @see #used_by_bi_cubes */ @JsonProperty("used_by_bi_cubes")  public void setUsedByBiCubes(ReferenceList used_by_bi_cubes) { this.used_by_bi_cubes = used_by_bi_cubes; }

    /** @see #uses_database_tables_or_views */ @JsonProperty("uses_database_tables_or_views")  public ReferenceList getUsesDatabaseTablesOrViews() { return this.uses_database_tables_or_views; }
    /** @see #uses_database_tables_or_views */ @JsonProperty("uses_database_tables_or_views")  public void setUsesDatabaseTablesOrViews(ReferenceList uses_database_tables_or_views) { this.uses_database_tables_or_views = uses_database_tables_or_views; }

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

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isBiCollection(Object obj) { return (obj.getClass() == BiCollection.class); }

}
