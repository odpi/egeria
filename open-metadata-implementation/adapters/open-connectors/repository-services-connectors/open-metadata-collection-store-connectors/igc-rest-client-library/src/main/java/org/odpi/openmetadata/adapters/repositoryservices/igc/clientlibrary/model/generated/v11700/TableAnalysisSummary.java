/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code table_analysis_summary} asset type in IGC, displayed as '{@literal Table Analysis Summary}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableAnalysisSummary extends Reference {

    public static String getIgcTypeId() { return "table_analysis_summary"; }
    public static String getIgcTypeDisplayName() { return "Table Analysis Summary"; }

    /**
     * The {@code short_&_long_description} property, displayed as '{@literal Short & Long Description}' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code steward} property, displayed as '{@literal Steward}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList steward;

    /**
     * The {@code analyzed_table} property, displayed as '{@literal Analyzed Table}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference analyzed_table;

    /**
     * The {@code project_name} property, displayed as '{@literal Analysis Project}' in the IGC UI.
     */
    protected String project_name;

    /**
     * The {@code review_date} property, displayed as '{@literal Review Date}' in the IGC UI.
     */
    protected Date review_date;

    /**
     * The {@code promoted_by_principal} property, displayed as '{@literal Promoted By Principal}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference promoted_by_principal;

    /**
     * The {@code reviewed_by_principal} property, displayed as '{@literal Reviewed By Principal}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference reviewed_by_principal;

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

    /**
     * The {@code number_of_fields} property, displayed as '{@literal Number of Fields}' in the IGC UI.
     */
    protected Number number_of_fields;

    /**
     * The {@code number_of_rows} property, displayed as '{@literal Number of Rows}' in the IGC UI.
     */
    protected Number number_of_rows;

    /**
     * The {@code inferred_primary_keys} property, displayed as '{@literal Inferred Primary Keys}' in the IGC UI.
     */
    protected String inferred_primary_keys;

    /**
     * The {@code primary_key_duplicates} property, displayed as '{@literal Primary Key Duplicates}' in the IGC UI.
     */
    protected Number primary_key_duplicates;

    /**
     * The {@code inferred_foreign_keys} property, displayed as '{@literal Inferred Foreign Keys}' in the IGC UI.
     */
    protected String inferred_foreign_keys;

    /**
     * The {@code foreign_key_violations} property, displayed as '{@literal Foreign Key Violations}' in the IGC UI.
     */
    protected Number foreign_key_violations;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #steward */ @JsonProperty("steward")  public ReferenceList getSteward() { return this.steward; }
    /** @see #steward */ @JsonProperty("steward")  public void setSteward(ReferenceList steward) { this.steward = steward; }

    /** @see #analyzed_table */ @JsonProperty("analyzed_table")  public Reference getAnalyzedTable() { return this.analyzed_table; }
    /** @see #analyzed_table */ @JsonProperty("analyzed_table")  public void setAnalyzedTable(Reference analyzed_table) { this.analyzed_table = analyzed_table; }

    /** @see #project_name */ @JsonProperty("project_name")  public String getProjectName() { return this.project_name; }
    /** @see #project_name */ @JsonProperty("project_name")  public void setProjectName(String project_name) { this.project_name = project_name; }

    /** @see #review_date */ @JsonProperty("review_date")  public Date getReviewDate() { return this.review_date; }
    /** @see #review_date */ @JsonProperty("review_date")  public void setReviewDate(Date review_date) { this.review_date = review_date; }

    /** @see #promoted_by_principal */ @JsonProperty("promoted_by_principal")  public Reference getPromotedByPrincipal() { return this.promoted_by_principal; }
    /** @see #promoted_by_principal */ @JsonProperty("promoted_by_principal")  public void setPromotedByPrincipal(Reference promoted_by_principal) { this.promoted_by_principal = promoted_by_principal; }

    /** @see #reviewed_by_principal */ @JsonProperty("reviewed_by_principal")  public Reference getReviewedByPrincipal() { return this.reviewed_by_principal; }
    /** @see #reviewed_by_principal */ @JsonProperty("reviewed_by_principal")  public void setReviewedByPrincipal(Reference reviewed_by_principal) { this.reviewed_by_principal = reviewed_by_principal; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    /** @see #number_of_fields */ @JsonProperty("number_of_fields")  public Number getNumberOfFields() { return this.number_of_fields; }
    /** @see #number_of_fields */ @JsonProperty("number_of_fields")  public void setNumberOfFields(Number number_of_fields) { this.number_of_fields = number_of_fields; }

    /** @see #number_of_rows */ @JsonProperty("number_of_rows")  public Number getNumberOfRows() { return this.number_of_rows; }
    /** @see #number_of_rows */ @JsonProperty("number_of_rows")  public void setNumberOfRows(Number number_of_rows) { this.number_of_rows = number_of_rows; }

    /** @see #inferred_primary_keys */ @JsonProperty("inferred_primary_keys")  public String getInferredPrimaryKeys() { return this.inferred_primary_keys; }
    /** @see #inferred_primary_keys */ @JsonProperty("inferred_primary_keys")  public void setInferredPrimaryKeys(String inferred_primary_keys) { this.inferred_primary_keys = inferred_primary_keys; }

    /** @see #primary_key_duplicates */ @JsonProperty("primary_key_duplicates")  public Number getPrimaryKeyDuplicates() { return this.primary_key_duplicates; }
    /** @see #primary_key_duplicates */ @JsonProperty("primary_key_duplicates")  public void setPrimaryKeyDuplicates(Number primary_key_duplicates) { this.primary_key_duplicates = primary_key_duplicates; }

    /** @see #inferred_foreign_keys */ @JsonProperty("inferred_foreign_keys")  public String getInferredForeignKeys() { return this.inferred_foreign_keys; }
    /** @see #inferred_foreign_keys */ @JsonProperty("inferred_foreign_keys")  public void setInferredForeignKeys(String inferred_foreign_keys) { this.inferred_foreign_keys = inferred_foreign_keys; }

    /** @see #foreign_key_violations */ @JsonProperty("foreign_key_violations")  public Number getForeignKeyViolations() { return this.foreign_key_violations; }
    /** @see #foreign_key_violations */ @JsonProperty("foreign_key_violations")  public void setForeignKeyViolations(Number foreign_key_violations) { this.foreign_key_violations = foreign_key_violations; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "short_&_long_description",
        "short_description",
        "long_description",
        "project_name",
        "review_date",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on",
        "number_of_fields",
        "number_of_rows",
        "inferred_primary_keys",
        "primary_key_duplicates",
        "inferred_foreign_keys",
        "foreign_key_violations"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "short_&_long_description",
        "short_description",
        "long_description",
        "project_name",
        "created_by",
        "modified_by",
        "inferred_primary_keys",
        "inferred_foreign_keys"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "assigned_to_terms",
        "steward"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "short_&_long_description",
        "short_description",
        "long_description",
        "assigned_to_terms",
        "steward",
        "analyzed_table",
        "project_name",
        "review_date",
        "promoted_by_principal",
        "reviewed_by_principal",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on",
        "number_of_fields",
        "number_of_rows",
        "inferred_primary_keys",
        "primary_key_duplicates",
        "inferred_foreign_keys",
        "foreign_key_violations"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTableAnalysisSummary(Object obj) { return (obj.getClass() == TableAnalysisSummary.class); }

}
