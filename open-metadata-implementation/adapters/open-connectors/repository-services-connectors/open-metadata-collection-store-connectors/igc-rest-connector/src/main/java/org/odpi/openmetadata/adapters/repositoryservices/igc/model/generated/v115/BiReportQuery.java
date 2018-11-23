/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_report_query' asset type in IGC, displayed as 'BI Report Query' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiReportQuery extends MainObject {

    public static final String IGC_TYPE_ID = "bi_report_query";

    /**
     * The 'context' property, displayed as 'Context' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList context;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'bi_report' property, displayed as 'BI Report' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiReport} object.
     */
    protected Reference bi_report;

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
     * The 'bi_report_query_items' property, displayed as 'BI Report Query Items' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQueryItem} objects.
     */
    protected ReferenceList bi_report_query_items;

    /**
     * The 'bi_report_query_detail_filters' property, displayed as 'BI Query Detail Filters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQueryDetailFilter} objects.
     */
    protected ReferenceList bi_report_query_detail_filters;

    /**
     * The 'bi_report_query_summary_filters' property, displayed as 'BI Query Summary Filters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuerySummaryFilter} objects.
     */
    protected ReferenceList bi_report_query_summary_filters;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'condition' property, displayed as 'Condition' in the IGC UI.
     */
    protected String condition;

    /**
     * The 'contains_bi_queries' property, displayed as 'Contains BI Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList contains_bi_queries;

    /**
     * The 'contained_by_bi_report_queries' property, displayed as 'Contained by BI Report Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList contained_by_bi_report_queries;

    /**
     * The 'uses_bi_collections' property, displayed as 'Uses BI Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList uses_bi_collections;

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
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #context */ @JsonProperty("context")  public ReferenceList getTheContext() { return this.context; }
    /** @see #context */ @JsonProperty("context")  public void setTheContext(ReferenceList context) { this.context = context; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #bi_report */ @JsonProperty("bi_report")  public Reference getBiReport() { return this.bi_report; }
    /** @see #bi_report */ @JsonProperty("bi_report")  public void setBiReport(Reference bi_report) { this.bi_report = bi_report; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #bi_collection */ @JsonProperty("bi_collection")  public Reference getBiCollection() { return this.bi_collection; }
    /** @see #bi_collection */ @JsonProperty("bi_collection")  public void setBiCollection(Reference bi_collection) { this.bi_collection = bi_collection; }

    /** @see #bi_report_query_items */ @JsonProperty("bi_report_query_items")  public ReferenceList getBiReportQueryItems() { return this.bi_report_query_items; }
    /** @see #bi_report_query_items */ @JsonProperty("bi_report_query_items")  public void setBiReportQueryItems(ReferenceList bi_report_query_items) { this.bi_report_query_items = bi_report_query_items; }

    /** @see #bi_report_query_detail_filters */ @JsonProperty("bi_report_query_detail_filters")  public ReferenceList getBiReportQueryDetailFilters() { return this.bi_report_query_detail_filters; }
    /** @see #bi_report_query_detail_filters */ @JsonProperty("bi_report_query_detail_filters")  public void setBiReportQueryDetailFilters(ReferenceList bi_report_query_detail_filters) { this.bi_report_query_detail_filters = bi_report_query_detail_filters; }

    /** @see #bi_report_query_summary_filters */ @JsonProperty("bi_report_query_summary_filters")  public ReferenceList getBiReportQuerySummaryFilters() { return this.bi_report_query_summary_filters; }
    /** @see #bi_report_query_summary_filters */ @JsonProperty("bi_report_query_summary_filters")  public void setBiReportQuerySummaryFilters(ReferenceList bi_report_query_summary_filters) { this.bi_report_query_summary_filters = bi_report_query_summary_filters; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #condition */ @JsonProperty("condition")  public String getCondition() { return this.condition; }
    /** @see #condition */ @JsonProperty("condition")  public void setCondition(String condition) { this.condition = condition; }

    /** @see #contains_bi_queries */ @JsonProperty("contains_bi_queries")  public ReferenceList getContainsBiQueries() { return this.contains_bi_queries; }
    /** @see #contains_bi_queries */ @JsonProperty("contains_bi_queries")  public void setContainsBiQueries(ReferenceList contains_bi_queries) { this.contains_bi_queries = contains_bi_queries; }

    /** @see #contained_by_bi_report_queries */ @JsonProperty("contained_by_bi_report_queries")  public ReferenceList getContainedByBiReportQueries() { return this.contained_by_bi_report_queries; }
    /** @see #contained_by_bi_report_queries */ @JsonProperty("contained_by_bi_report_queries")  public void setContainedByBiReportQueries(ReferenceList contained_by_bi_report_queries) { this.contained_by_bi_report_queries = contained_by_bi_report_queries; }

    /** @see #uses_bi_collections */ @JsonProperty("uses_bi_collections")  public ReferenceList getUsesBiCollections() { return this.uses_bi_collections; }
    /** @see #uses_bi_collections */ @JsonProperty("uses_bi_collections")  public void setUsesBiCollections(ReferenceList uses_bi_collections) { this.uses_bi_collections = uses_bi_collections; }

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

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isBiReportQuery(Object obj) { return (obj.getClass() == BiReportQuery.class); }

}
