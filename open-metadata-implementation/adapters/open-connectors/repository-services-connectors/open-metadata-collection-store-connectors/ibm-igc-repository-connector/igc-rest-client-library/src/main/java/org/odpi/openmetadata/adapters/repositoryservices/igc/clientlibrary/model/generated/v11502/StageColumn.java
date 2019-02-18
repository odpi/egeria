/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code stage_column} asset type in IGC, displayed as '{@literal Stage Column}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StageColumn extends Reference {

    public static String getIgcTypeId() { return "stage_column"; }
    public static String getIgcTypeDisplayName() { return "Stage Column"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code link} property, displayed as '{@literal Link}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Link} object.
     */
    protected Reference link;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code odbc_type} property, displayed as '{@literal SQL Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHAR (displayed in the UI as 'CHAR')</li>
     *     <li>VARCHAR (displayed in the UI as 'VARCHAR')</li>
     *     <li>LONGVARCHAR (displayed in the UI as 'LONGVARCHAR')</li>
     *     <li>WCHAR (displayed in the UI as 'WCHAR')</li>
     *     <li>WVARCHAR (displayed in the UI as 'WVARCHAR')</li>
     *     <li>WLONGVARCHAR (displayed in the UI as 'WLONGVARCHAR')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>NUMERIC (displayed in the UI as 'NUMERIC')</li>
     *     <li>SMALLINT (displayed in the UI as 'SMALLINT')</li>
     *     <li>INTEGER (displayed in the UI as 'INTEGER')</li>
     *     <li>REAL (displayed in the UI as 'REAL')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>DOUBLE (displayed in the UI as 'DOUBLE')</li>
     *     <li>BIT (displayed in the UI as 'BIT')</li>
     *     <li>TINYINT (displayed in the UI as 'TINYINT')</li>
     *     <li>BIGINT (displayed in the UI as 'BIGINT')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>VARBINARY (displayed in the UI as 'VARBINARY')</li>
     *     <li>LONGVARBINARY (displayed in the UI as 'LONGVARBINARY')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>TIMESTAMP (displayed in the UI as 'TIMESTAMP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     * </ul>
     */
    protected String odbc_type;

    /**
     * The {@code key} property, displayed as '{@literal Key}' in the IGC UI.
     */
    protected Boolean key;

    /**
     * The {@code length} property, displayed as '{@literal Length}' in the IGC UI.
     */
    protected Number length;

    /**
     * The {@code minimum_length} property, displayed as '{@literal Minimum Length}' in the IGC UI.
     */
    protected Number minimum_length;

    /**
     * The {@code allows_null_values} property, displayed as '{@literal Allow Null Values}' in the IGC UI.
     */
    protected Boolean allows_null_values;

    /**
     * The {@code expression} property, displayed as '{@literal Expression}' in the IGC UI.
     */
    protected ArrayList<String> expression;

    /**
     * The {@code constraints} property, displayed as '{@literal Constraints}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link JobConstraint} objects.
     */
    protected ReferenceList constraints;

    /**
     * The {@code next_stage_columns} property, displayed as '{@literal Next Stage Columns}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList next_stage_columns;

    /**
     * The {@code previous_stage_columns} property, displayed as '{@literal Previous Stage Columns}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList previous_stage_columns;

    /**
     * The {@code reads_from_(static)} property, displayed as '{@literal Reads from (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(static)") protected ReferenceList reads_from__static_;

    /**
     * The {@code writes_to_(static)} property, displayed as '{@literal Writes to (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(static)") protected ReferenceList writes_to__static_;

    /**
     * The {@code reads_from_(design)} property, displayed as '{@literal Reads from (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(design)") protected ReferenceList reads_from__design_;

    /**
     * The {@code writes_to_(design)} property, displayed as '{@literal Writes to (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(design)") protected ReferenceList writes_to__design_;

    /**
     * The {@code reads_from_(operational)} property, displayed as '{@literal Reads from (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(operational)") protected ReferenceList reads_from__operational_;

    /**
     * The {@code writes_to_(operational)} property, displayed as '{@literal Writes to (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(operational)") protected ReferenceList writes_to__operational_;

    /**
     * The {@code reads_from_(user_defined)} property, displayed as '{@literal Reads from (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(user_defined)") protected ReferenceList reads_from__user_defined_;

    /**
     * The {@code writes_to_(user_defined)} property, displayed as '{@literal Writes to (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(user_defined)") protected ReferenceList writes_to__user_defined_;

    /**
     * The {@code impacted_by} property, displayed as '{@literal Impacted by}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacted_by;

    /**
     * The {@code impacts_on} property, displayed as '{@literal Impacts on}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacts_on;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #link */ @JsonProperty("link")  public Reference getLink() { return this.link; }
    /** @see #link */ @JsonProperty("link")  public void setLink(Reference link) { this.link = link; }

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

    /** @see #odbc_type */ @JsonProperty("odbc_type")  public String getOdbcType() { return this.odbc_type; }
    /** @see #odbc_type */ @JsonProperty("odbc_type")  public void setOdbcType(String odbc_type) { this.odbc_type = odbc_type; }

    /** @see #key */ @JsonProperty("key")  public Boolean getKey() { return this.key; }
    /** @see #key */ @JsonProperty("key")  public void setKey(Boolean key) { this.key = key; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #minimum_length */ @JsonProperty("minimum_length")  public Number getMinimumLength() { return this.minimum_length; }
    /** @see #minimum_length */ @JsonProperty("minimum_length")  public void setMinimumLength(Number minimum_length) { this.minimum_length = minimum_length; }

    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public Boolean getAllowsNullValues() { return this.allows_null_values; }
    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public void setAllowsNullValues(Boolean allows_null_values) { this.allows_null_values = allows_null_values; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }

    /** @see #constraints */ @JsonProperty("constraints")  public ReferenceList getConstraints() { return this.constraints; }
    /** @see #constraints */ @JsonProperty("constraints")  public void setConstraints(ReferenceList constraints) { this.constraints = constraints; }

    /** @see #next_stage_columns */ @JsonProperty("next_stage_columns")  public ReferenceList getNextStageColumns() { return this.next_stage_columns; }
    /** @see #next_stage_columns */ @JsonProperty("next_stage_columns")  public void setNextStageColumns(ReferenceList next_stage_columns) { this.next_stage_columns = next_stage_columns; }

    /** @see #previous_stage_columns */ @JsonProperty("previous_stage_columns")  public ReferenceList getPreviousStageColumns() { return this.previous_stage_columns; }
    /** @see #previous_stage_columns */ @JsonProperty("previous_stage_columns")  public void setPreviousStageColumns(ReferenceList previous_stage_columns) { this.previous_stage_columns = previous_stage_columns; }

    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public ReferenceList getReadsFromStatic() { return this.reads_from__static_; }
    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public void setReadsFromStatic(ReferenceList reads_from__static_) { this.reads_from__static_ = reads_from__static_; }

    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public ReferenceList getWritesToStatic() { return this.writes_to__static_; }
    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public void setWritesToStatic(ReferenceList writes_to__static_) { this.writes_to__static_ = writes_to__static_; }

    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public ReferenceList getReadsFromDesign() { return this.reads_from__design_; }
    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public void setReadsFromDesign(ReferenceList reads_from__design_) { this.reads_from__design_ = reads_from__design_; }

    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public ReferenceList getWritesToDesign() { return this.writes_to__design_; }
    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public void setWritesToDesign(ReferenceList writes_to__design_) { this.writes_to__design_ = writes_to__design_; }

    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public ReferenceList getReadsFromOperational() { return this.reads_from__operational_; }
    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public void setReadsFromOperational(ReferenceList reads_from__operational_) { this.reads_from__operational_ = reads_from__operational_; }

    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public ReferenceList getWritesToOperational() { return this.writes_to__operational_; }
    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public void setWritesToOperational(ReferenceList writes_to__operational_) { this.writes_to__operational_ = writes_to__operational_; }

    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public ReferenceList getReadsFromUserDefined() { return this.reads_from__user_defined_; }
    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public void setReadsFromUserDefined(ReferenceList reads_from__user_defined_) { this.reads_from__user_defined_ = reads_from__user_defined_; }

    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public ReferenceList getWritesToUserDefined() { return this.writes_to__user_defined_; }
    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public void setWritesToUserDefined(ReferenceList writes_to__user_defined_) { this.writes_to__user_defined_ = writes_to__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "odbc_type",
        "key",
        "length",
        "minimum_length",
        "allows_null_values",
        "expression",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "expression",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "constraints",
        "next_stage_columns",
        "previous_stage_columns",
        "reads_from_(static)",
        "writes_to_(static)",
        "reads_from_(design)",
        "writes_to_(design)",
        "reads_from_(operational)",
        "writes_to_(operational)",
        "reads_from_(user_defined)",
        "writes_to_(user_defined)",
        "impacted_by",
        "impacts_on",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "link",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "odbc_type",
        "key",
        "length",
        "minimum_length",
        "allows_null_values",
        "expression",
        "constraints",
        "next_stage_columns",
        "previous_stage_columns",
        "reads_from_(static)",
        "writes_to_(static)",
        "reads_from_(design)",
        "writes_to_(design)",
        "reads_from_(operational)",
        "writes_to_(operational)",
        "reads_from_(user_defined)",
        "writes_to_(user_defined)",
        "impacted_by",
        "impacts_on",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isStageColumn(Object obj) { return (obj.getClass() == StageColumn.class); }

}
