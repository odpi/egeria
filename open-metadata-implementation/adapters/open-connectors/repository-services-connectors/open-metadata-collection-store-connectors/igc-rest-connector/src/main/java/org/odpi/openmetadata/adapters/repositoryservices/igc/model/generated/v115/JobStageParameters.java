/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_stage_parameters' asset type in IGC, displayed as 'Job Stage Parameters' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobStageParameters extends MainObject {

    public static final String IGC_TYPE_ID = "job_stage_parameters";

    /**
     * The 'transaction_groupable' property, displayed as 'Transaction Groupable' in the IGC UI.
     */
    protected Number transaction_groupable;

    /**
     * The 'of_ds_parameter_def' property, displayed as 'Of DS Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Parameter} object.
     */
    protected Reference of_ds_parameter_def;

    /**
     * The 'a_xmeta_locking_root' property, displayed as 'A XMeta Locking Root' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The 'locked' property, displayed as 'Locked' in the IGC UI.
     */
    protected Boolean locked;

    /**
     * The 'hidden' property, displayed as 'Hidden' in the IGC UI.
     */
    protected Boolean hidden;

    /**
     * The 'for_output' property, displayed as 'For Output' in the IGC UI.
     */
    protected Number for_output;

    /**
     * The 'for_stage' property, displayed as 'For Stage' in the IGC UI.
     */
    protected Number for_stage;

    /**
     * The 'quote_string' property, displayed as 'Quote String' in the IGC UI.
     */
    protected Number quote_string;

    /**
     * The 'conv_type' property, displayed as 'Conv Type' in the IGC UI.
     */
    protected Number conv_type;

    /**
     * The 'for_input' property, displayed as 'For Input' in the IGC UI.
     */
    protected Number for_input;

    /**
     * The 'display_style' property, displayed as 'Display Style' in the IGC UI.
     */
    protected Number display_style;

    /**
     * The 'view_data' property, displayed as 'View Data' in the IGC UI.
     */
    protected Number view_data;


    /** @see #transaction_groupable */ @JsonProperty("transaction_groupable")  public Number getTransactionGroupable() { return this.transaction_groupable; }
    /** @see #transaction_groupable */ @JsonProperty("transaction_groupable")  public void setTransactionGroupable(Number transaction_groupable) { this.transaction_groupable = transaction_groupable; }

    /** @see #of_ds_parameter_def */ @JsonProperty("of_ds_parameter_def")  public Reference getOfDsParameterDef() { return this.of_ds_parameter_def; }
    /** @see #of_ds_parameter_def */ @JsonProperty("of_ds_parameter_def")  public void setOfDsParameterDef(Reference of_ds_parameter_def) { this.of_ds_parameter_def = of_ds_parameter_def; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #locked */ @JsonProperty("locked")  public Boolean getLocked() { return this.locked; }
    /** @see #locked */ @JsonProperty("locked")  public void setLocked(Boolean locked) { this.locked = locked; }

    /** @see #hidden */ @JsonProperty("hidden")  public Boolean getHidden() { return this.hidden; }
    /** @see #hidden */ @JsonProperty("hidden")  public void setHidden(Boolean hidden) { this.hidden = hidden; }

    /** @see #for_output */ @JsonProperty("for_output")  public Number getForOutput() { return this.for_output; }
    /** @see #for_output */ @JsonProperty("for_output")  public void setForOutput(Number for_output) { this.for_output = for_output; }

    /** @see #for_stage */ @JsonProperty("for_stage")  public Number getForStage() { return this.for_stage; }
    /** @see #for_stage */ @JsonProperty("for_stage")  public void setForStage(Number for_stage) { this.for_stage = for_stage; }

    /** @see #quote_string */ @JsonProperty("quote_string")  public Number getQuoteString() { return this.quote_string; }
    /** @see #quote_string */ @JsonProperty("quote_string")  public void setQuoteString(Number quote_string) { this.quote_string = quote_string; }

    /** @see #conv_type */ @JsonProperty("conv_type")  public Number getConvType() { return this.conv_type; }
    /** @see #conv_type */ @JsonProperty("conv_type")  public void setConvType(Number conv_type) { this.conv_type = conv_type; }

    /** @see #for_input */ @JsonProperty("for_input")  public Number getForInput() { return this.for_input; }
    /** @see #for_input */ @JsonProperty("for_input")  public void setForInput(Number for_input) { this.for_input = for_input; }

    /** @see #display_style */ @JsonProperty("display_style")  public Number getDisplayStyle() { return this.display_style; }
    /** @see #display_style */ @JsonProperty("display_style")  public void setDisplayStyle(Number display_style) { this.display_style = display_style; }

    /** @see #view_data */ @JsonProperty("view_data")  public Number getViewData() { return this.view_data; }
    /** @see #view_data */ @JsonProperty("view_data")  public void setViewData(Number view_data) { this.view_data = view_data; }


    public static final Boolean isJobStageParameters(Object obj) { return (obj.getClass() == JobStageParameters.class); }

}
