/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'mapping' asset type in IGC, displayed as 'Mapping' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Mapping extends MainObject {

    public static final String IGC_TYPE_ID = "mapping";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected ArrayList<String> description;

    /**
     * The 'mapping_specification' property, displayed as 'Mapping Specification' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList mapping_specification;

    /**
     * The 'source_column_names' property, displayed as 'Source Column Names' in the IGC UI.
     */
    protected ArrayList<String> source_column_names;

    /**
     * The 'source_columns' property, displayed as 'Source Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList source_columns;

    /**
     * The 'source_terms' property, displayed as 'Source Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList source_terms;

    /**
     * The 'target_column_names' property, displayed as 'Target Column Names' in the IGC UI.
     */
    protected ArrayList<String> target_column_names;

    /**
     * The 'target_columns' property, displayed as 'Target Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList target_columns;

    /**
     * The 'target_terms' property, displayed as 'Target Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList target_terms;

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     */
    protected ArrayList<String> status;

    /**
     * The 'tags' property, displayed as 'Tags' in the IGC UI.
     */
    protected String tags;

    /**
     * The 'rule_description' property, displayed as 'Rule Description' in the IGC UI.
     */
    protected ArrayList<String> rule_description;

    /**
     * The 'rule_expression' property, displayed as 'Rule Expression' in the IGC UI.
     */
    protected ArrayList<String> rule_expression;

    /**
     * The 'last_update_description' property, displayed as 'Last Update Description' in the IGC UI.
     */
    protected ArrayList<String> last_update_description;


    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #mapping_specification */ @JsonProperty("mapping_specification")  public ReferenceList getMappingSpecification() { return this.mapping_specification; }
    /** @see #mapping_specification */ @JsonProperty("mapping_specification")  public void setMappingSpecification(ReferenceList mapping_specification) { this.mapping_specification = mapping_specification; }

    /** @see #source_column_names */ @JsonProperty("source_column_names")  public ArrayList<String> getSourceColumnNames() { return this.source_column_names; }
    /** @see #source_column_names */ @JsonProperty("source_column_names")  public void setSourceColumnNames(ArrayList<String> source_column_names) { this.source_column_names = source_column_names; }

    /** @see #source_columns */ @JsonProperty("source_columns")  public ReferenceList getSourceColumns() { return this.source_columns; }
    /** @see #source_columns */ @JsonProperty("source_columns")  public void setSourceColumns(ReferenceList source_columns) { this.source_columns = source_columns; }

    /** @see #source_terms */ @JsonProperty("source_terms")  public ReferenceList getSourceTerms() { return this.source_terms; }
    /** @see #source_terms */ @JsonProperty("source_terms")  public void setSourceTerms(ReferenceList source_terms) { this.source_terms = source_terms; }

    /** @see #target_column_names */ @JsonProperty("target_column_names")  public ArrayList<String> getTargetColumnNames() { return this.target_column_names; }
    /** @see #target_column_names */ @JsonProperty("target_column_names")  public void setTargetColumnNames(ArrayList<String> target_column_names) { this.target_column_names = target_column_names; }

    /** @see #target_columns */ @JsonProperty("target_columns")  public ReferenceList getTargetColumns() { return this.target_columns; }
    /** @see #target_columns */ @JsonProperty("target_columns")  public void setTargetColumns(ReferenceList target_columns) { this.target_columns = target_columns; }

    /** @see #target_terms */ @JsonProperty("target_terms")  public ReferenceList getTargetTerms() { return this.target_terms; }
    /** @see #target_terms */ @JsonProperty("target_terms")  public void setTargetTerms(ReferenceList target_terms) { this.target_terms = target_terms; }

    /** @see #status */ @JsonProperty("status")  public ArrayList<String> getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(ArrayList<String> status) { this.status = status; }

    /** @see #tags */ @JsonProperty("tags")  public String getTags() { return this.tags; }
    /** @see #tags */ @JsonProperty("tags")  public void setTags(String tags) { this.tags = tags; }

    /** @see #rule_description */ @JsonProperty("rule_description")  public ArrayList<String> getRuleDescription() { return this.rule_description; }
    /** @see #rule_description */ @JsonProperty("rule_description")  public void setRuleDescription(ArrayList<String> rule_description) { this.rule_description = rule_description; }

    /** @see #rule_expression */ @JsonProperty("rule_expression")  public ArrayList<String> getRuleExpression() { return this.rule_expression; }
    /** @see #rule_expression */ @JsonProperty("rule_expression")  public void setRuleExpression(ArrayList<String> rule_expression) { this.rule_expression = rule_expression; }

    /** @see #last_update_description */ @JsonProperty("last_update_description")  public ArrayList<String> getLastUpdateDescription() { return this.last_update_description; }
    /** @see #last_update_description */ @JsonProperty("last_update_description")  public void setLastUpdateDescription(ArrayList<String> last_update_description) { this.last_update_description = last_update_description; }


    public static final Boolean isMapping(Object obj) { return (obj.getClass() == Mapping.class); }

}
