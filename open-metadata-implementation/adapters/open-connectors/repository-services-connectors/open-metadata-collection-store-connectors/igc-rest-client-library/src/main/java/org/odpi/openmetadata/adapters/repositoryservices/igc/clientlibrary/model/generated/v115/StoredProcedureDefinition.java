/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'stored_procedure_definition' asset type in IGC, displayed as 'Stored Procedure Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StoredProcedureDefinition extends Reference {

    public static String getIgcTypeId() { return "stored_procedure_definition"; }
    public static String getIgcTypeDisplayName() { return "Stored Procedure Definition"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'in_parameters' property, displayed as 'In Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InParameter} objects.
     */
    protected ReferenceList in_parameters;

    /**
     * The 'out_parameters' property, displayed as 'Out Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link OutParameter} objects.
     */
    protected ReferenceList out_parameters;

    /**
     * The 'inout_parameters' property, displayed as 'InOut Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InoutParameter} objects.
     */
    protected ReferenceList inout_parameters;

    /**
     * The 'result_columns' property, displayed as 'Result Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ResultColumn} objects.
     */
    protected ReferenceList result_columns;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'reads_from_(static)' property, displayed as 'Reads from (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(static)") protected ReferenceList reads_from__static_;

    /**
     * The 'writes_to_(static)' property, displayed as 'Writes to (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(static)") protected ReferenceList writes_to__static_;

    /**
     * The 'reads_from_(design)' property, displayed as 'Reads from (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(design)") protected ReferenceList reads_from__design_;

    /**
     * The 'writes_to_(design)' property, displayed as 'Writes to (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(design)") protected ReferenceList writes_to__design_;

    /**
     * The 'reads_from_(operational)' property, displayed as 'Reads from (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(operational)") protected ReferenceList reads_from__operational_;

    /**
     * The 'writes_to_(operational)' property, displayed as 'Writes to (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(operational)") protected ReferenceList writes_to__operational_;

    /**
     * The 'reads_from_(user_defined)' property, displayed as 'Reads from (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(user_defined)") protected ReferenceList reads_from__user_defined_;

    /**
     * The 'writes_to_(user_defined)' property, displayed as 'Writes to (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(user_defined)") protected ReferenceList writes_to__user_defined_;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

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

    /** @see #in_parameters */ @JsonProperty("in_parameters")  public ReferenceList getInParameters() { return this.in_parameters; }
    /** @see #in_parameters */ @JsonProperty("in_parameters")  public void setInParameters(ReferenceList in_parameters) { this.in_parameters = in_parameters; }

    /** @see #out_parameters */ @JsonProperty("out_parameters")  public ReferenceList getOutParameters() { return this.out_parameters; }
    /** @see #out_parameters */ @JsonProperty("out_parameters")  public void setOutParameters(ReferenceList out_parameters) { this.out_parameters = out_parameters; }

    /** @see #inout_parameters */ @JsonProperty("inout_parameters")  public ReferenceList getInoutParameters() { return this.inout_parameters; }
    /** @see #inout_parameters */ @JsonProperty("inout_parameters")  public void setInoutParameters(ReferenceList inout_parameters) { this.inout_parameters = inout_parameters; }

    /** @see #result_columns */ @JsonProperty("result_columns")  public ReferenceList getResultColumns() { return this.result_columns; }
    /** @see #result_columns */ @JsonProperty("result_columns")  public void setResultColumns(ReferenceList result_columns) { this.result_columns = result_columns; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

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

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

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

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("short_description");
        add("long_description");
        add("alias_(business_name)");
        add("include_for_business_lineage");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("labels");
        add("stewards");
        add("assigned_to_terms");
        add("implements_rules");
        add("governed_by_rules");
        add("in_parameters");
        add("out_parameters");
        add("inout_parameters");
        add("result_columns");
        add("reads_from_(static)");
        add("writes_to_(static)");
        add("reads_from_(design)");
        add("writes_to_(design)");
        add("reads_from_(operational)");
        add("writes_to_(operational)");
        add("reads_from_(user_defined)");
        add("writes_to_(user_defined)");
        add("impacted_by");
        add("impacts_on");
        add("blueprint_elements");
        add("in_collections");
    }};
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("short_description");
        add("long_description");
        add("labels");
        add("stewards");
        add("assigned_to_terms");
        add("implements_rules");
        add("governed_by_rules");
        add("in_parameters");
        add("out_parameters");
        add("inout_parameters");
        add("result_columns");
        add("alias_(business_name)");
        add("reads_from_(static)");
        add("writes_to_(static)");
        add("reads_from_(design)");
        add("writes_to_(design)");
        add("reads_from_(operational)");
        add("writes_to_(operational)");
        add("reads_from_(user_defined)");
        add("writes_to_(user_defined)");
        add("impacted_by");
        add("impacts_on");
        add("include_for_business_lineage");
        add("blueprint_elements");
        add("in_collections");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isStoredProcedureDefinition(Object obj) { return (obj.getClass() == StoredProcedureDefinition.class); }

}
