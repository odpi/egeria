/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'job_stage_record' asset type in IGC, displayed as 'Job Stage Record' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobStageRecord extends Reference {

    public static String getIgcTypeId() { return "job_stage_record"; }
    public static String getIgcTypeDisplayName() { return "Job Stage Record"; }

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
     * The 'record_id_name' property, displayed as 'Record ID Name' in the IGC UI.
     */
    protected String record_id_name;

    /**
     * The 'a_xmeta_locking_root' property, displayed as 'A XMeta Locking Root' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The 'other_records_initialization_flag' property, displayed as 'Other Records Initialization Flag' in the IGC UI.
     */
    protected Number other_records_initialization_flag;

    /**
     * The 'of_ds_stage' property, displayed as 'Of DS Stage' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference of_ds_stage;

    /**
     * The 'has_ds_flow_variable' property, displayed as 'Has DS Flow Variable' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList has_ds_flow_variable;

    /**
     * The 'record_id_value' property, displayed as 'Record ID Value' in the IGC UI.
     */
    protected String record_id_value;

    /**
     * The 'internal_id' property, displayed as 'Internal ID' in the IGC UI.
     */
    protected String internal_id;

    /**
     * The 'record_name' property, displayed as 'Record Name' in the IGC UI.
     */
    protected String record_name;

    /**
     * The 'record_id_name_value_relation' property, displayed as 'Record ID Name Value Relation' in the IGC UI.
     */
    protected String record_id_name_value_relation;


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

    /** @see #record_id_name */ @JsonProperty("record_id_name")  public String getRecordIdName() { return this.record_id_name; }
    /** @see #record_id_name */ @JsonProperty("record_id_name")  public void setRecordIdName(String record_id_name) { this.record_id_name = record_id_name; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #other_records_initialization_flag */ @JsonProperty("other_records_initialization_flag")  public Number getOtherRecordsInitializationFlag() { return this.other_records_initialization_flag; }
    /** @see #other_records_initialization_flag */ @JsonProperty("other_records_initialization_flag")  public void setOtherRecordsInitializationFlag(Number other_records_initialization_flag) { this.other_records_initialization_flag = other_records_initialization_flag; }

    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public Reference getOfDsStage() { return this.of_ds_stage; }
    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public void setOfDsStage(Reference of_ds_stage) { this.of_ds_stage = of_ds_stage; }

    /** @see #has_ds_flow_variable */ @JsonProperty("has_ds_flow_variable")  public ReferenceList getHasDsFlowVariable() { return this.has_ds_flow_variable; }
    /** @see #has_ds_flow_variable */ @JsonProperty("has_ds_flow_variable")  public void setHasDsFlowVariable(ReferenceList has_ds_flow_variable) { this.has_ds_flow_variable = has_ds_flow_variable; }

    /** @see #record_id_value */ @JsonProperty("record_id_value")  public String getRecordIdValue() { return this.record_id_value; }
    /** @see #record_id_value */ @JsonProperty("record_id_value")  public void setRecordIdValue(String record_id_value) { this.record_id_value = record_id_value; }

    /** @see #internal_id */ @JsonProperty("internal_id")  public String getInternalId() { return this.internal_id; }
    /** @see #internal_id */ @JsonProperty("internal_id")  public void setInternalId(String internal_id) { this.internal_id = internal_id; }

    /** @see #record_name */ @JsonProperty("record_name")  public String getRecordName() { return this.record_name; }
    /** @see #record_name */ @JsonProperty("record_name")  public void setRecordName(String record_name) { this.record_name = record_name; }

    /** @see #record_id_name_value_relation */ @JsonProperty("record_id_name_value_relation")  public String getRecordIdNameValueRelation() { return this.record_id_name_value_relation; }
    /** @see #record_id_name_value_relation */ @JsonProperty("record_id_name_value_relation")  public void setRecordIdNameValueRelation(String record_id_name_value_relation) { this.record_id_name_value_relation = record_id_name_value_relation; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "record_id_name",
        "a_xmeta_locking_root",
        "other_records_initialization_flag",
        "record_id_value",
        "internal_id",
        "record_name",
        "record_id_name_value_relation"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "record_id_name",
        "a_xmeta_locking_root",
        "record_id_value",
        "internal_id",
        "record_name",
        "record_id_name_value_relation"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "has_ds_flow_variable"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "record_id_name",
        "a_xmeta_locking_root",
        "other_records_initialization_flag",
        "of_ds_stage",
        "has_ds_flow_variable",
        "record_id_value",
        "internal_id",
        "record_name",
        "record_id_name_value_relation"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isJobStageRecord(Object obj) { return (obj.getClass() == JobStageRecord.class); }

}
