/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_report' asset type in IGC, displayed as 'BI Report' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiReport extends MainObject {

    public static final String IGC_TYPE_ID = "bi_report";

    /**
     * The 'bi_folder_or_bi_model_or_cube' property, displayed as 'BI Folder or BI Model or Cube' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList bi_folder_or_bi_model_or_cube;

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
     * The 'bi_cube' property, displayed as 'BI Cube' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCube} object.
     */
    protected Reference bi_cube;

    /**
     * The 'bi_report_queries' property, displayed as 'BI Report Queries' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportQuery} objects.
     */
    protected ReferenceList bi_report_queries;

    /**
     * The 'referenced_by_bi_reports' property, displayed as 'Referenced by BI Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReport} objects.
     */
    protected ReferenceList referenced_by_bi_reports;

    /**
     * The 'references_bi_reports' property, displayed as 'References BI Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReport} objects.
     */
    protected ReferenceList references_bi_reports;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'author' property, displayed as 'Author' in the IGC UI.
     */
    protected String author;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'format' property, displayed as 'Format' in the IGC UI.
     */
    protected String format;

    /**
     * The 'bi_report_creation_date' property, displayed as 'BI Report Creation Date' in the IGC UI.
     */
    protected Date bi_report_creation_date;

    /**
     * The 'bi_report_modification_date' property, displayed as 'BI Report Modification Date' in the IGC UI.
     */
    protected Date bi_report_modification_date;

    /**
     * The 'bi_report_run_date' property, displayed as 'BI Report Run Date' in the IGC UI.
     */
    protected Date bi_report_run_date;

    /**
     * The 'number_of_runs' property, displayed as 'Number of Runs' in the IGC UI.
     */
    protected Number number_of_runs;

    /**
     * The 'number_of_versions' property, displayed as 'Number of Versions' in the IGC UI.
     */
    protected Number number_of_versions;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

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
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #bi_folder_or_bi_model_or_cube */ @JsonProperty("bi_folder_or_bi_model_or_cube")  public ReferenceList getBiFolderOrBiModelOrCube() { return this.bi_folder_or_bi_model_or_cube; }
    /** @see #bi_folder_or_bi_model_or_cube */ @JsonProperty("bi_folder_or_bi_model_or_cube")  public void setBiFolderOrBiModelOrCube(ReferenceList bi_folder_or_bi_model_or_cube) { this.bi_folder_or_bi_model_or_cube = bi_folder_or_bi_model_or_cube; }

    /** @see #bi_folder */ @JsonProperty("bi_folder")  public Reference getBiFolder() { return this.bi_folder; }
    /** @see #bi_folder */ @JsonProperty("bi_folder")  public void setBiFolder(Reference bi_folder) { this.bi_folder = bi_folder; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #bi_cube */ @JsonProperty("bi_cube")  public Reference getBiCube() { return this.bi_cube; }
    /** @see #bi_cube */ @JsonProperty("bi_cube")  public void setBiCube(Reference bi_cube) { this.bi_cube = bi_cube; }

    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public ReferenceList getBiReportQueries() { return this.bi_report_queries; }
    /** @see #bi_report_queries */ @JsonProperty("bi_report_queries")  public void setBiReportQueries(ReferenceList bi_report_queries) { this.bi_report_queries = bi_report_queries; }

    /** @see #referenced_by_bi_reports */ @JsonProperty("referenced_by_bi_reports")  public ReferenceList getReferencedByBiReports() { return this.referenced_by_bi_reports; }
    /** @see #referenced_by_bi_reports */ @JsonProperty("referenced_by_bi_reports")  public void setReferencedByBiReports(ReferenceList referenced_by_bi_reports) { this.referenced_by_bi_reports = referenced_by_bi_reports; }

    /** @see #references_bi_reports */ @JsonProperty("references_bi_reports")  public ReferenceList getReferencesBiReports() { return this.references_bi_reports; }
    /** @see #references_bi_reports */ @JsonProperty("references_bi_reports")  public void setReferencesBiReports(ReferenceList references_bi_reports) { this.references_bi_reports = references_bi_reports; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #format */ @JsonProperty("format")  public String getFormat() { return this.format; }
    /** @see #format */ @JsonProperty("format")  public void setFormat(String format) { this.format = format; }

    /** @see #bi_report_creation_date */ @JsonProperty("bi_report_creation_date")  public Date getBiReportCreationDate() { return this.bi_report_creation_date; }
    /** @see #bi_report_creation_date */ @JsonProperty("bi_report_creation_date")  public void setBiReportCreationDate(Date bi_report_creation_date) { this.bi_report_creation_date = bi_report_creation_date; }

    /** @see #bi_report_modification_date */ @JsonProperty("bi_report_modification_date")  public Date getBiReportModificationDate() { return this.bi_report_modification_date; }
    /** @see #bi_report_modification_date */ @JsonProperty("bi_report_modification_date")  public void setBiReportModificationDate(Date bi_report_modification_date) { this.bi_report_modification_date = bi_report_modification_date; }

    /** @see #bi_report_run_date */ @JsonProperty("bi_report_run_date")  public Date getBiReportRunDate() { return this.bi_report_run_date; }
    /** @see #bi_report_run_date */ @JsonProperty("bi_report_run_date")  public void setBiReportRunDate(Date bi_report_run_date) { this.bi_report_run_date = bi_report_run_date; }

    /** @see #number_of_runs */ @JsonProperty("number_of_runs")  public Number getNumberOfRuns() { return this.number_of_runs; }
    /** @see #number_of_runs */ @JsonProperty("number_of_runs")  public void setNumberOfRuns(Number number_of_runs) { this.number_of_runs = number_of_runs; }

    /** @see #number_of_versions */ @JsonProperty("number_of_versions")  public Number getNumberOfVersions() { return this.number_of_versions; }
    /** @see #number_of_versions */ @JsonProperty("number_of_versions")  public void setNumberOfVersions(Number number_of_versions) { this.number_of_versions = number_of_versions; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

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

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isBiReport(Object obj) { return (obj.getClass() == BiReport.class); }

}
