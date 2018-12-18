/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsmetabag' asset type in IGC, displayed as 'DSMetaBag' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsmetabag extends Reference {

    public static String getIgcTypeId() { return "dsmetabag"; }

    /**
     * The 'of_ds_table_definition' property, displayed as 'Of DS Table Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TableDefinition} object.
     */
    protected Reference of_ds_table_definition;

    /**
     * The 'of_ds_job_def' property, displayed as 'Of DS Job Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference of_ds_job_def;

    /**
     * The 'of_ds_routine' property, displayed as 'Of DS Routine' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Routine} object.
     */
    protected Reference of_ds_routine;

    /**
     * The 'of_ds_stage' property, displayed as 'Of DS Stage' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference of_ds_stage;

    /**
     * The 'of_ds_input_pin' property, displayed as 'Of DS Input Pin' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link JobInputPin} object.
     */
    protected Reference of_ds_input_pin;

    /**
     * The 'values' property, displayed as 'Values' in the IGC UI.
     */
    protected String values;

    /**
     * The 'of_ds_data_connection' property, displayed as 'Of DS Data Connection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsdataConnection} object.
     */
    protected Reference of_ds_data_connection;

    /**
     * The 'of_ds_data_quality_spec' property, displayed as 'Of DS Data Quality Spec' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StandardizationObject} object.
     */
    protected Reference of_ds_data_quality_spec;

    /**
     * The 'of_ds_data_element' property, displayed as 'Of DS Data Element' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataElement} object.
     */
    protected Reference of_ds_data_element;

    /**
     * The 'a_xmeta_locking_root' property, displayed as 'A XMeta Locking Root' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The 'of_ds_design_view' property, displayed as 'Of DS Design View' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsdesignView} object.
     */
    protected Reference of_ds_design_view;

    /**
     * The 'of_ds_stage_type' property, displayed as 'Of DS Stage Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsstageType} object.
     */
    protected Reference of_ds_stage_type;

    /**
     * The 'owners' property, displayed as 'Owners' in the IGC UI.
     */
    protected String owners;

    /**
     * The 'names' property, displayed as 'Names' in the IGC UI.
     */
    protected String names;

    /**
     * The 'of_ds_transform' property, displayed as 'Of DS Transform' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformsFunction} object.
     */
    protected Reference of_ds_transform;

    /**
     * The 'of_ds_output_pin' property, displayed as 'Of DS Output Pin' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link JobOutputPin} object.
     */
    protected Reference of_ds_output_pin;

    /**
     * The 'conditions' property, displayed as 'Conditions' in the IGC UI.
     */
    protected String conditions;

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


    /** @see #of_ds_table_definition */ @JsonProperty("of_ds_table_definition")  public Reference getOfDsTableDefinition() { return this.of_ds_table_definition; }
    /** @see #of_ds_table_definition */ @JsonProperty("of_ds_table_definition")  public void setOfDsTableDefinition(Reference of_ds_table_definition) { this.of_ds_table_definition = of_ds_table_definition; }

    /** @see #of_ds_job_def */ @JsonProperty("of_ds_job_def")  public Reference getOfDsJobDef() { return this.of_ds_job_def; }
    /** @see #of_ds_job_def */ @JsonProperty("of_ds_job_def")  public void setOfDsJobDef(Reference of_ds_job_def) { this.of_ds_job_def = of_ds_job_def; }

    /** @see #of_ds_routine */ @JsonProperty("of_ds_routine")  public Reference getOfDsRoutine() { return this.of_ds_routine; }
    /** @see #of_ds_routine */ @JsonProperty("of_ds_routine")  public void setOfDsRoutine(Reference of_ds_routine) { this.of_ds_routine = of_ds_routine; }

    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public Reference getOfDsStage() { return this.of_ds_stage; }
    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public void setOfDsStage(Reference of_ds_stage) { this.of_ds_stage = of_ds_stage; }

    /** @see #of_ds_input_pin */ @JsonProperty("of_ds_input_pin")  public Reference getOfDsInputPin() { return this.of_ds_input_pin; }
    /** @see #of_ds_input_pin */ @JsonProperty("of_ds_input_pin")  public void setOfDsInputPin(Reference of_ds_input_pin) { this.of_ds_input_pin = of_ds_input_pin; }

    /** @see #values */ @JsonProperty("values")  public String getValues() { return this.values; }
    /** @see #values */ @JsonProperty("values")  public void setValues(String values) { this.values = values; }

    /** @see #of_ds_data_connection */ @JsonProperty("of_ds_data_connection")  public Reference getOfDsDataConnection() { return this.of_ds_data_connection; }
    /** @see #of_ds_data_connection */ @JsonProperty("of_ds_data_connection")  public void setOfDsDataConnection(Reference of_ds_data_connection) { this.of_ds_data_connection = of_ds_data_connection; }

    /** @see #of_ds_data_quality_spec */ @JsonProperty("of_ds_data_quality_spec")  public Reference getOfDsDataQualitySpec() { return this.of_ds_data_quality_spec; }
    /** @see #of_ds_data_quality_spec */ @JsonProperty("of_ds_data_quality_spec")  public void setOfDsDataQualitySpec(Reference of_ds_data_quality_spec) { this.of_ds_data_quality_spec = of_ds_data_quality_spec; }

    /** @see #of_ds_data_element */ @JsonProperty("of_ds_data_element")  public Reference getOfDsDataElement() { return this.of_ds_data_element; }
    /** @see #of_ds_data_element */ @JsonProperty("of_ds_data_element")  public void setOfDsDataElement(Reference of_ds_data_element) { this.of_ds_data_element = of_ds_data_element; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #of_ds_design_view */ @JsonProperty("of_ds_design_view")  public Reference getOfDsDesignView() { return this.of_ds_design_view; }
    /** @see #of_ds_design_view */ @JsonProperty("of_ds_design_view")  public void setOfDsDesignView(Reference of_ds_design_view) { this.of_ds_design_view = of_ds_design_view; }

    /** @see #of_ds_stage_type */ @JsonProperty("of_ds_stage_type")  public Reference getOfDsStageType() { return this.of_ds_stage_type; }
    /** @see #of_ds_stage_type */ @JsonProperty("of_ds_stage_type")  public void setOfDsStageType(Reference of_ds_stage_type) { this.of_ds_stage_type = of_ds_stage_type; }

    /** @see #owners */ @JsonProperty("owners")  public String getOwners() { return this.owners; }
    /** @see #owners */ @JsonProperty("owners")  public void setOwners(String owners) { this.owners = owners; }

    /** @see #names */ @JsonProperty("names")  public String getNames() { return this.names; }
    /** @see #names */ @JsonProperty("names")  public void setNames(String names) { this.names = names; }

    /** @see #of_ds_transform */ @JsonProperty("of_ds_transform")  public Reference getOfDsTransform() { return this.of_ds_transform; }
    /** @see #of_ds_transform */ @JsonProperty("of_ds_transform")  public void setOfDsTransform(Reference of_ds_transform) { this.of_ds_transform = of_ds_transform; }

    /** @see #of_ds_output_pin */ @JsonProperty("of_ds_output_pin")  public Reference getOfDsOutputPin() { return this.of_ds_output_pin; }
    /** @see #of_ds_output_pin */ @JsonProperty("of_ds_output_pin")  public void setOfDsOutputPin(Reference of_ds_output_pin) { this.of_ds_output_pin = of_ds_output_pin; }

    /** @see #conditions */ @JsonProperty("conditions")  public String getConditions() { return this.conditions; }
    /** @see #conditions */ @JsonProperty("conditions")  public void setConditions(String conditions) { this.conditions = conditions; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean isDsmetabag(Object obj) { return (obj.getClass() == Dsmetabag.class); }

}
