/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'job_stage_record' asset type in IGC, displayed as 'Job Stage Record' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobStageRecord extends MainObject {

    public static final String IGC_TYPE_ID = "job_stage_record";

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


    public static final Boolean isJobStageRecord(Object obj) { return (obj.getClass() == JobStageRecord.class); }

}
