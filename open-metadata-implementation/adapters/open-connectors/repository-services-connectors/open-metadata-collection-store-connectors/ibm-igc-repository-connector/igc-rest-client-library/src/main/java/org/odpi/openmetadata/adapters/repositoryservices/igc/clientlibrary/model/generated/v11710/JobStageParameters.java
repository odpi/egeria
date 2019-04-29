/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code job_stage_parameters} asset type in IGC, displayed as '{@literal Job Stage Parameters}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("job_stage_parameters")
public class JobStageParameters extends Reference {

    public static String getIgcTypeDisplayName() { return "Job Stage Parameters"; }

    /**
     * The {@code transaction_groupable} property, displayed as '{@literal Transaction Groupable}' in the IGC UI.
     */
    protected Number transaction_groupable;

    /**
     * The {@code of_ds_parameter_def} property, displayed as '{@literal Of DS Parameter Def}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Parameter} object.
     */
    protected Reference of_ds_parameter_def;

    /**
     * The {@code a_xmeta_locking_root} property, displayed as '{@literal A XMeta Locking Root}' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The {@code locked} property, displayed as '{@literal Locked}' in the IGC UI.
     */
    protected Boolean locked;

    /**
     * The {@code hidden} property, displayed as '{@literal Hidden}' in the IGC UI.
     */
    protected Boolean hidden;

    /**
     * The {@code for_output} property, displayed as '{@literal For Output}' in the IGC UI.
     */
    protected Number for_output;

    /**
     * The {@code for_stage} property, displayed as '{@literal For Stage}' in the IGC UI.
     */
    protected Number for_stage;

    /**
     * The {@code quote_string} property, displayed as '{@literal Quote String}' in the IGC UI.
     */
    protected Number quote_string;

    /**
     * The {@code conv_type} property, displayed as '{@literal Conv Type}' in the IGC UI.
     */
    protected Number conv_type;

    /**
     * The {@code for_input} property, displayed as '{@literal For Input}' in the IGC UI.
     */
    protected Number for_input;

    /**
     * The {@code display_style} property, displayed as '{@literal Display Style}' in the IGC UI.
     */
    protected Number display_style;

    /**
     * The {@code view_data} property, displayed as '{@literal View Data}' in the IGC UI.
     */
    protected Number view_data;

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
        "transaction_groupable",
        "a_xmeta_locking_root",
        "locked",
        "hidden",
        "for_output",
        "for_stage",
        "quote_string",
        "conv_type",
        "for_input",
        "display_style",
        "view_data",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "a_xmeta_locking_root",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "transaction_groupable",
        "of_ds_parameter_def",
        "a_xmeta_locking_root",
        "locked",
        "hidden",
        "for_output",
        "for_stage",
        "quote_string",
        "conv_type",
        "for_input",
        "display_style",
        "view_data",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isJobStageParameters(Object obj) { return (obj.getClass() == JobStageParameters.class); }

}
