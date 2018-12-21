/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'table_analysis' asset type in IGC, displayed as 'Table Analysis' in the IGC UI.
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
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'project' property, displayed as 'Project' in the IGC UI.
     */
    protected String project;

    /**
     * The 'database_table_or_view' property, displayed as 'Database Table or View' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference database_table_or_view;

    /**
     * The 'review_date' property, displayed as 'Review Date' in the IGC UI.
     */
    protected Date review_date;

    /**
     * The 'number_of_fields' property, displayed as 'Number of Fields' in the IGC UI.
     */
    protected Number number_of_fields;

    /**
     * The 'number_of_rows' property, displayed as 'Number of Rows' in the IGC UI.
     */
    protected Number number_of_rows;

    /**
     * The 'primary_key_duplicates' property, displayed as 'Primary Key Duplicates' in the IGC UI.
     */
    protected Number primary_key_duplicates;

    /**
     * The 'foreign_key_violations' property, displayed as 'Foreign Key Violations' in the IGC UI.
     */
    protected Number foreign_key_violations;

    /**
     * The 'selected_primary_key' property, displayed as 'User Selected Primary Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_primary_key;

    /**
     * The 'selected_foreign_key' property, displayed as 'User Selected Foreign Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_foreign_key;

    /**
     * The 'selected_natural_key' property, displayed as 'User Selected Natural Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_natural_key;

    /**
     * The 'quality_score_percent' property, displayed as 'Quality Score' in the IGC UI.
     */
    protected String quality_score_percent;

    /**
     * The 'nb_record_tested' property, displayed as 'Number of Records Tested' in the IGC UI.
     */
    protected Number nb_record_tested;

    /**
     * The 'quality_score_problems' property, displayed as 'Quality Score Problems' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link QualityProblem} objects.
     */
    protected ReferenceList quality_score_problems;

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

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("project");
        add("review_date");
        add("number_of_fields");
        add("number_of_rows");
        add("primary_key_duplicates");
        add("foreign_key_violations");
        add("quality_score_percent");
        add("nb_record_tested");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isTableAnalysis(Object obj) { return (obj.getClass() == TableAnalysis.class); }

}
