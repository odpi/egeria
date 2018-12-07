/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'table_analysis_summary' asset type in IGC, displayed as 'Table Analysis Summary' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableAnalysisSummary extends MainObject {

    public static final String IGC_TYPE_ID = "table_analysis_summary";

    /**
     * The 'short_&amp;_long_description' property, displayed as 'Short &amp; Long Description' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The 'steward' property, displayed as 'Steward' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList steward;

    /**
     * The 'analyzed_table' property, displayed as 'Analyzed Table' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference analyzed_table;

    /**
     * The 'project_name' property, displayed as 'Analysis Project' in the IGC UI.
     */
    protected String project_name;

    /**
     * The 'review_date' property, displayed as 'Review Date' in the IGC UI.
     */
    protected Date review_date;

    /**
     * The 'promoted_by_principal' property, displayed as 'Promoted By Principal' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference promoted_by_principal;

    /**
     * The 'reviewed_by_principal' property, displayed as 'Reviewed By Principal' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference reviewed_by_principal;

    /**
     * The 'number_of_fields' property, displayed as 'Number of Fields' in the IGC UI.
     */
    protected Number number_of_fields;

    /**
     * The 'number_of_rows' property, displayed as 'Number of Rows' in the IGC UI.
     */
    protected Number number_of_rows;

    /**
     * The 'inferred_primary_keys' property, displayed as 'Inferred Primary Keys' in the IGC UI.
     */
    protected String inferred_primary_keys;

    /**
     * The 'primary_key_duplicates' property, displayed as 'Primary Key Duplicates' in the IGC UI.
     */
    protected Number primary_key_duplicates;

    /**
     * The 'inferred_foreign_keys' property, displayed as 'Inferred Foreign Keys' in the IGC UI.
     */
    protected String inferred_foreign_keys;

    /**
     * The 'foreign_key_violations' property, displayed as 'Foreign Key Violations' in the IGC UI.
     */
    protected Number foreign_key_violations;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

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


    public static final Boolean isTableAnalysisSummary(Object obj) { return (obj.getClass() == TableAnalysisSummary.class); }

}
