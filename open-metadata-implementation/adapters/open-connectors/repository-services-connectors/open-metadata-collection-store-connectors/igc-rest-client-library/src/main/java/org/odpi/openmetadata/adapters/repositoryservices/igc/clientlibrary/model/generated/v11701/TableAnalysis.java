/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code table_analysis} asset type in IGC, displayed as '{@literal Table Analysis}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableAnalysis extends Reference {

    public static String getIgcTypeId() { return "table_analysis"; }
    public static String getIgcTypeDisplayName() { return "Table Analysis"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code project} property, displayed as '{@literal Project}' in the IGC UI.
     */
    protected String project;

    /**
     * The {@code database_table_or_view} property, displayed as '{@literal Database Table or View}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference database_table_or_view;

    /**
     * The {@code review_date} property, displayed as '{@literal Review Date}' in the IGC UI.
     */
    protected Date review_date;

    /**
     * The {@code number_of_fields} property, displayed as '{@literal Number of Fields}' in the IGC UI.
     */
    protected Number number_of_fields;

    /**
     * The {@code number_of_rows} property, displayed as '{@literal Number of Rows}' in the IGC UI.
     */
    protected Number number_of_rows;

    /**
     * The {@code primary_key_duplicates} property, displayed as '{@literal Primary Key Duplicates}' in the IGC UI.
     */
    protected Number primary_key_duplicates;

    /**
     * The {@code foreign_key_violations} property, displayed as '{@literal Foreign Key Violations}' in the IGC UI.
     */
    protected Number foreign_key_violations;

    /**
     * The {@code selected_primary_key} property, displayed as '{@literal User Selected Primary Key}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_primary_key;

    /**
     * The {@code selected_foreign_key} property, displayed as '{@literal User Selected Foreign Key}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_foreign_key;

    /**
     * The {@code selected_natural_key} property, displayed as '{@literal User Selected Natural Key}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_natural_key;

    /**
     * The {@code quality_score_percent} property, displayed as '{@literal Quality Score}' in the IGC UI.
     */
    protected String quality_score_percent;

    /**
     * The {@code nb_record_tested} property, displayed as '{@literal Number of Records Tested}' in the IGC UI.
     */
    protected Number nb_record_tested;

    /**
     * The {@code quality_score_problems} property, displayed as '{@literal Quality Score Problems}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link QualityProblem} objects.
     */
    protected ReferenceList quality_score_problems;

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

    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #database_table_or_view */ @JsonProperty("database_table_or_view")  public Reference getDatabaseTableOrView() { return this.database_table_or_view; }
    /** @see #database_table_or_view */ @JsonProperty("database_table_or_view")  public void setDatabaseTableOrView(Reference database_table_or_view) { this.database_table_or_view = database_table_or_view; }

    /** @see #review_date */ @JsonProperty("review_date")  public Date getReviewDate() { return this.review_date; }
    /** @see #review_date */ @JsonProperty("review_date")  public void setReviewDate(Date review_date) { this.review_date = review_date; }

    /** @see #number_of_fields */ @JsonProperty("number_of_fields")  public Number getNumberOfFields() { return this.number_of_fields; }
    /** @see #number_of_fields */ @JsonProperty("number_of_fields")  public void setNumberOfFields(Number number_of_fields) { this.number_of_fields = number_of_fields; }

    /** @see #number_of_rows */ @JsonProperty("number_of_rows")  public Number getNumberOfRows() { return this.number_of_rows; }
    /** @see #number_of_rows */ @JsonProperty("number_of_rows")  public void setNumberOfRows(Number number_of_rows) { this.number_of_rows = number_of_rows; }

    /** @see #primary_key_duplicates */ @JsonProperty("primary_key_duplicates")  public Number getPrimaryKeyDuplicates() { return this.primary_key_duplicates; }
    /** @see #primary_key_duplicates */ @JsonProperty("primary_key_duplicates")  public void setPrimaryKeyDuplicates(Number primary_key_duplicates) { this.primary_key_duplicates = primary_key_duplicates; }

    /** @see #foreign_key_violations */ @JsonProperty("foreign_key_violations")  public Number getForeignKeyViolations() { return this.foreign_key_violations; }
    /** @see #foreign_key_violations */ @JsonProperty("foreign_key_violations")  public void setForeignKeyViolations(Number foreign_key_violations) { this.foreign_key_violations = foreign_key_violations; }

    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public ReferenceList getSelectedPrimaryKey() { return this.selected_primary_key; }
    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public void setSelectedPrimaryKey(ReferenceList selected_primary_key) { this.selected_primary_key = selected_primary_key; }

    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public ReferenceList getSelectedForeignKey() { return this.selected_foreign_key; }
    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public void setSelectedForeignKey(ReferenceList selected_foreign_key) { this.selected_foreign_key = selected_foreign_key; }

    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public ReferenceList getSelectedNaturalKey() { return this.selected_natural_key; }
    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public void setSelectedNaturalKey(ReferenceList selected_natural_key) { this.selected_natural_key = selected_natural_key; }

    /** @see #quality_score_percent */ @JsonProperty("quality_score_percent")  public String getQualityScorePercent() { return this.quality_score_percent; }
    /** @see #quality_score_percent */ @JsonProperty("quality_score_percent")  public void setQualityScorePercent(String quality_score_percent) { this.quality_score_percent = quality_score_percent; }

    /** @see #nb_record_tested */ @JsonProperty("nb_record_tested")  public Number getNbRecordTested() { return this.nb_record_tested; }
    /** @see #nb_record_tested */ @JsonProperty("nb_record_tested")  public void setNbRecordTested(Number nb_record_tested) { this.nb_record_tested = nb_record_tested; }

    /** @see #quality_score_problems */ @JsonProperty("quality_score_problems")  public ReferenceList getQualityScoreProblems() { return this.quality_score_problems; }
    /** @see #quality_score_problems */ @JsonProperty("quality_score_problems")  public void setQualityScoreProblems(ReferenceList quality_score_problems) { this.quality_score_problems = quality_score_problems; }

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
        "project",
        "review_date",
        "number_of_fields",
        "number_of_rows",
        "primary_key_duplicates",
        "foreign_key_violations",
        "quality_score_percent",
        "nb_record_tested",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "project",
        "quality_score_percent",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "selected_primary_key",
        "selected_foreign_key",
        "selected_natural_key",
        "quality_score_problems"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "project",
        "database_table_or_view",
        "review_date",
        "number_of_fields",
        "number_of_rows",
        "primary_key_duplicates",
        "foreign_key_violations",
        "selected_primary_key",
        "selected_foreign_key",
        "selected_natural_key",
        "quality_score_percent",
        "nb_record_tested",
        "quality_score_problems",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTableAnalysis(Object obj) { return (obj.getClass() == TableAnalysis.class); }

}
