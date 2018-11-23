/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_model' asset type in IGC, displayed as 'BI Model' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiModel extends MainObject {

    public static final String IGC_TYPE_ID = "bi_model";

    /**
     * The 'bi_folder_or_bi_model' property, displayed as 'BI Folder or BI Model' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList bi_folder_or_bi_model;

    /**
     * The 'bi_folder' property, displayed as 'BI Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiFolder} object.
     */
    protected Reference bi_folder;

    /**
     * The 'bi_model' property, displayed as 'BI Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The 'child_bi_models' property, displayed as 'Child BI Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiModel} objects.
     */
    protected ReferenceList child_bi_models;

    /**
     * The 'bi_cubes' property, displayed as 'BI Cubes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCube} objects.
     */
    protected ReferenceList bi_cubes;

    /**
     * The 'bi_collections' property, displayed as 'BI Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList bi_collections;

    /**
     * The 'bi_reports' property, displayed as 'BI Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReport} objects.
     */
    protected ReferenceList bi_reports;

    /**
     * The 'bi_report_queries' property, displayed as 'BI Report Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList bi_report_queries;

    /**
     * The 'bi_filters' property, displayed as 'BI Filters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiFilter} objects.
     */
    protected ReferenceList bi_filters;

    /**
     * The 'bi_hierarchies' property, displayed as 'BI Hierarchies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiHierarchy} objects.
     */
    protected ReferenceList bi_hierarchies;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'bi_model_creation_date' property, displayed as 'BI Model Creation Date' in the IGC UI.
     */
    protected Date bi_model_creation_date;

    /**
     * The 'bi_model_modification_date' property, displayed as 'BI Model Modification Date' in the IGC UI.
     */
    protected Date bi_model_modification_date;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'used_by_bi_reports' property, displayed as 'Used by BI Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReport} objects.
     */
    protected ReferenceList used_by_bi_reports;

    /**
     * The 'uses_databases' property, displayed as 'Uses Databases' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Database} objects.
     */
    protected ReferenceList uses_databases;

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
     * The 'include_for_business_lineage' property, displayed as 'Include for Business Lineage' in the IGC UI.
     */
    protected Boolean include_for_business_lineage;

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


    /** @see #bi_folder_or_bi_model */ @JsonProperty("bi_folder_or_bi_model")  public ReferenceList getBiFolderOrBiModel() { return this.bi_folder_or_bi_model; }
    /** @see #bi_folder_or_bi_model */ @JsonProperty("bi_folder_or_bi_model")  public void setBiFolderOrBiModel(ReferenceList bi_folder_or_bi_model) { this.bi_folder_or_bi_model = bi_folder_or_bi_model; }

    /** @see #bi_folder */ @JsonProperty("bi_folder")  public Reference getBiFolder() { return this.bi_folder; }
    /** @see #bi_folder */ @JsonProperty("bi_folder")  public void setBiFolder(Reference bi_folder) { this.bi_folder = bi_folder; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #child_bi_models */ @JsonProperty("child_bi_models")  public ReferenceList getChildBiModels() { return this.child_bi_models; }
    /** @see #child_bi_models */ @JsonProperty("child_bi_models")  public void setChildBiModels(ReferenceList child_bi_models) { this.child_bi_models = child_bi_models; }

    /** @see #bi_cubes */ @JsonProperty("bi_cubes")  public ReferenceList getBiCubes() { return this.bi_cubes; }
    /** @see #bi_cubes */ @JsonProperty("bi_cubes")  public void setBiCubes(ReferenceList bi_cubes) { this.bi_cubes = bi_cubes; }

    /** @see #bi_collections */ @JsonProperty("bi_collections")  public ReferenceList getBiCollections() { return this.bi_collections; }
    /** @see #bi_collections */ @JsonProperty("bi_collections")  public void setBiCollections(ReferenceList bi_collections) { this.bi_collections = bi_collections; }

    /** @see #bi_reports */ @JsonProperty("bi_reports")  public ReferenceList getBiReports() { return this.bi_reports; }
    /** @see #bi_reports */ @JsonProperty("bi_reports")  public void setBiReports(ReferenceList bi_reports) { this.bi_reports = bi_reports; }

    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public ReferenceList getBiReportQueries() { return this.bi_report_queries; }
    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public void setBiReportQueries(ReferenceList bi_report_queries) { this.bi_report_queries = bi_report_queries; }

    /** @see #bi_filters */ @JsonProperty("bi_filters")  public ReferenceList getBiFilters() { return this.bi_filters; }
    /** @see #bi_filters */ @JsonProperty("bi_filters")  public void setBiFilters(ReferenceList bi_filters) { this.bi_filters = bi_filters; }

    /** @see #bi_hierarchies */ @JsonProperty("bi_hierarchies")  public ReferenceList getBiHierarchies() { return this.bi_hierarchies; }
    /** @see #bi_hierarchies */ @JsonProperty("bi_hierarchies")  public void setBiHierarchies(ReferenceList bi_hierarchies) { this.bi_hierarchies = bi_hierarchies; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #bi_model_creation_date */ @JsonProperty("bi_model_creation_date")  public Date getBiModelCreationDate() { return this.bi_model_creation_date; }
    /** @see #bi_model_creation_date */ @JsonProperty("bi_model_creation_date")  public void setBiModelCreationDate(Date bi_model_creation_date) { this.bi_model_creation_date = bi_model_creation_date; }

    /** @see #bi_model_modification_date */ @JsonProperty("bi_model_modification_date")  public Date getBiModelModificationDate() { return this.bi_model_modification_date; }
    /** @see #bi_model_modification_date */ @JsonProperty("bi_model_modification_date")  public void setBiModelModificationDate(Date bi_model_modification_date) { this.bi_model_modification_date = bi_model_modification_date; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #used_by_bi_reports */ @JsonProperty("used_by_bi_reports")  public ReferenceList getUsedByBiReports() { return this.used_by_bi_reports; }
    /** @see #used_by_bi_reports */ @JsonProperty("used_by_bi_reports")  public void setUsedByBiReports(ReferenceList used_by_bi_reports) { this.used_by_bi_reports = used_by_bi_reports; }

    /** @see #uses_databases */ @JsonProperty("uses_databases")  public ReferenceList getUsesDatabases() { return this.uses_databases; }
    /** @see #uses_databases */ @JsonProperty("uses_databases")  public void setUsesDatabases(ReferenceList uses_databases) { this.uses_databases = uses_databases; }

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

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isBiModel(Object obj) { return (obj.getClass() == BiModel.class); }

}
