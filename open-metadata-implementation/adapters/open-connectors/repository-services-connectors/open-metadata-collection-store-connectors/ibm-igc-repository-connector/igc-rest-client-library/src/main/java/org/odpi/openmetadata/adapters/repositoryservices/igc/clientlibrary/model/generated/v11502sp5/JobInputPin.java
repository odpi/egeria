/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

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
 * POJO for the {@code job_input_pin} asset type in IGC, displayed as '{@literal Job Input Pin}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("job_input_pin")
public class JobInputPin extends Reference {

    public static String getIgcTypeDisplayName() { return "Job Input Pin"; }

    /**
     * The {@code condition_not_met} property, displayed as '{@literal Condition Not Met}' in the IGC UI.
     */
    protected String condition_not_met;

    /**
     * The {@code partner} property, displayed as '{@literal Partner}' in the IGC UI.
     */
    protected String partner;

    /**
     * The {@code a_xmeta_locking_root} property, displayed as '{@literal A XMeta Locking Root}' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The {@code has_ds_meta_bag} property, displayed as '{@literal Has DS Meta Bag}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsmetabag} object.
     */
    protected Reference has_ds_meta_bag;

    /**
     * The {@code pin_type} property, displayed as '{@literal Pin Type}' in the IGC UI.
     */
    protected String pin_type;

    /**
     * The {@code has_dsmf_column_info} property, displayed as '{@literal Has DSMF Column Info}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsmfcolumninfo} objects.
     */
    protected ReferenceList has_dsmf_column_info;

    /**
     * The {@code lookup_fail} property, displayed as '{@literal Lookup Fail}' in the IGC UI.
     */
    protected String lookup_fail;

    /**
     * The {@code internal_id} property, displayed as '{@literal Internal ID}' in the IGC UI.
     */
    protected String internal_id;

    /**
     * The {@code transaction_size} property, displayed as '{@literal Transaction Size}' in the IGC UI.
     */
    protected Number transaction_size;

    /**
     * The {@code link_type} property, displayed as '{@literal Link Type}' in the IGC UI.
     */
    protected Number link_type;

    /**
     * The {@code txn_behaviour} property, displayed as '{@literal TXN Behaviour}' in the IGC UI.
     */
    protected Number txn_behaviour;

    /**
     * The {@code enable_tx_group} property, displayed as '{@literal Enable Tx Group}' in the IGC UI.
     */
    protected Number enable_tx_group;

    /**
     * The {@code of_job_component} property, displayed as '{@literal Of Job Component}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_job_component;

    /**
     * The {@code is_target_of_link} property, displayed as '{@literal Is Target Of Link}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference is_target_of_link;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;

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


    /** @see #condition_not_met */ @JsonProperty("condition_not_met")  public String getConditionNotMet() { return this.condition_not_met; }
    /** @see #condition_not_met */ @JsonProperty("condition_not_met")  public void setConditionNotMet(String condition_not_met) { this.condition_not_met = condition_not_met; }

    /** @see #partner */ @JsonProperty("partner")  public String getPartner() { return this.partner; }
    /** @see #partner */ @JsonProperty("partner")  public void setPartner(String partner) { this.partner = partner; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #has_ds_meta_bag */ @JsonProperty("has_ds_meta_bag")  public Reference getHasDsMetaBag() { return this.has_ds_meta_bag; }
    /** @see #has_ds_meta_bag */ @JsonProperty("has_ds_meta_bag")  public void setHasDsMetaBag(Reference has_ds_meta_bag) { this.has_ds_meta_bag = has_ds_meta_bag; }

    /** @see #pin_type */ @JsonProperty("pin_type")  public String getPinType() { return this.pin_type; }
    /** @see #pin_type */ @JsonProperty("pin_type")  public void setPinType(String pin_type) { this.pin_type = pin_type; }

    /** @see #has_dsmf_column_info */ @JsonProperty("has_dsmf_column_info")  public ReferenceList getHasDsmfColumnInfo() { return this.has_dsmf_column_info; }
    /** @see #has_dsmf_column_info */ @JsonProperty("has_dsmf_column_info")  public void setHasDsmfColumnInfo(ReferenceList has_dsmf_column_info) { this.has_dsmf_column_info = has_dsmf_column_info; }

    /** @see #lookup_fail */ @JsonProperty("lookup_fail")  public String getLookupFail() { return this.lookup_fail; }
    /** @see #lookup_fail */ @JsonProperty("lookup_fail")  public void setLookupFail(String lookup_fail) { this.lookup_fail = lookup_fail; }

    /** @see #internal_id */ @JsonProperty("internal_id")  public String getInternalId() { return this.internal_id; }
    /** @see #internal_id */ @JsonProperty("internal_id")  public void setInternalId(String internal_id) { this.internal_id = internal_id; }

    /** @see #transaction_size */ @JsonProperty("transaction_size")  public Number getTransactionSize() { return this.transaction_size; }
    /** @see #transaction_size */ @JsonProperty("transaction_size")  public void setTransactionSize(Number transaction_size) { this.transaction_size = transaction_size; }

    /** @see #link_type */ @JsonProperty("link_type")  public Number getLinkType() { return this.link_type; }
    /** @see #link_type */ @JsonProperty("link_type")  public void setLinkType(Number link_type) { this.link_type = link_type; }

    /** @see #txn_behaviour */ @JsonProperty("txn_behaviour")  public Number getTxnBehaviour() { return this.txn_behaviour; }
    /** @see #txn_behaviour */ @JsonProperty("txn_behaviour")  public void setTxnBehaviour(Number txn_behaviour) { this.txn_behaviour = txn_behaviour; }

    /** @see #enable_tx_group */ @JsonProperty("enable_tx_group")  public Number getEnableTxGroup() { return this.enable_tx_group; }
    /** @see #enable_tx_group */ @JsonProperty("enable_tx_group")  public void setEnableTxGroup(Number enable_tx_group) { this.enable_tx_group = enable_tx_group; }

    /** @see #of_job_component */ @JsonProperty("of_job_component")  public Reference getOfJobComponent() { return this.of_job_component; }
    /** @see #of_job_component */ @JsonProperty("of_job_component")  public void setOfJobComponent(Reference of_job_component) { this.of_job_component = of_job_component; }

    /** @see #is_target_of_link */ @JsonProperty("is_target_of_link")  public Reference getIsTargetOfLink() { return this.is_target_of_link; }
    /** @see #is_target_of_link */ @JsonProperty("is_target_of_link")  public void setIsTargetOfLink(Reference is_target_of_link) { this.is_target_of_link = is_target_of_link; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

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
        "condition_not_met",
        "partner",
        "a_xmeta_locking_root",
        "pin_type",
        "lookup_fail",
        "internal_id",
        "transaction_size",
        "link_type",
        "txn_behaviour",
        "enable_tx_group",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "condition_not_met",
        "partner",
        "a_xmeta_locking_root",
        "pin_type",
        "lookup_fail",
        "internal_id",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_dsmf_column_info"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "condition_not_met",
        "partner",
        "a_xmeta_locking_root",
        "has_ds_meta_bag",
        "pin_type",
        "has_dsmf_column_info",
        "lookup_fail",
        "internal_id",
        "transaction_size",
        "link_type",
        "txn_behaviour",
        "enable_tx_group",
        "of_job_component",
        "is_target_of_link",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isJobInputPin(Object obj) { return (obj.getClass() == JobInputPin.class); }

}
