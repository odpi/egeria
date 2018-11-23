/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_input_pin' asset type in IGC, displayed as 'Job Input Pin' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobInputPin extends MainObject {

    public static final String IGC_TYPE_ID = "job_input_pin";

    /**
     * The 'condition_not_met' property, displayed as 'Condition Not Met' in the IGC UI.
     */
    protected String condition_not_met;

    /**
     * The 'partner' property, displayed as 'Partner' in the IGC UI.
     */
    protected String partner;

    /**
     * The 'a_xmeta_locking_root' property, displayed as 'A XMeta Locking Root' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The 'has_ds_meta_bag' property, displayed as 'Has DS Meta Bag' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsmetabag} object.
     */
    protected Reference has_ds_meta_bag;

    /**
     * The 'pin_type' property, displayed as 'Pin Type' in the IGC UI.
     */
    protected String pin_type;

    /**
     * The 'has_dsmf_column_info' property, displayed as 'Has DSMF Column Info' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsmfcolumninfo} objects.
     */
    protected ReferenceList has_dsmf_column_info;

    /**
     * The 'lookup_fail' property, displayed as 'Lookup Fail' in the IGC UI.
     */
    protected String lookup_fail;

    /**
     * The 'internal_id' property, displayed as 'Internal ID' in the IGC UI.
     */
    protected String internal_id;

    /**
     * The 'transaction_size' property, displayed as 'Transaction Size' in the IGC UI.
     */
    protected Number transaction_size;

    /**
     * The 'link_type' property, displayed as 'Link Type' in the IGC UI.
     */
    protected Number link_type;

    /**
     * The 'txn_behaviour' property, displayed as 'TXN Behaviour' in the IGC UI.
     */
    protected Number txn_behaviour;

    /**
     * The 'enable_tx_group' property, displayed as 'Enable Tx Group' in the IGC UI.
     */
    protected Number enable_tx_group;

    /**
     * The 'of_job_component' property, displayed as 'Of Job Component' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_job_component;

    /**
     * The 'is_target_of_link' property, displayed as 'Is Target Of Link' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference is_target_of_link;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


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


    public static final Boolean isJobInputPin(Object obj) { return (obj.getClass() == JobInputPin.class); }

}
