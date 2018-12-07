/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_output_pin' asset type in IGC, displayed as 'Job Output Pin' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobOutputPin extends MainObject {

    public static final String IGC_TYPE_ID = "job_output_pin";

    /**
     * The 'left_text_pos' property, displayed as 'Left Text Pos' in the IGC UI.
     */
    protected Number left_text_pos;

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
     * The 'top_text_pos' property, displayed as 'Top Text Pos' in the IGC UI.
     */
    protected Number top_text_pos;

    /**
     * The 'internal_id' property, displayed as 'Internal ID' in the IGC UI.
     */
    protected String internal_id;

    /**
     * The 'has_ds_argument_map' property, displayed as 'Has DS Argument Map' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsargumentmap} objects.
     */
    protected ReferenceList has_ds_argument_map;

    /**
     * The 'of_job_component' property, displayed as 'Of Job Component' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_job_component;

    /**
     * The 'is_source_of_link' property, displayed as 'Is Source Of Link' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference is_source_of_link;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #left_text_pos */ @JsonProperty("left_text_pos")  public Number getLeftTextPos() { return this.left_text_pos; }
    /** @see #left_text_pos */ @JsonProperty("left_text_pos")  public void setLeftTextPos(Number left_text_pos) { this.left_text_pos = left_text_pos; }

    /** @see #partner */ @JsonProperty("partner")  public String getPartner() { return this.partner; }
    /** @see #partner */ @JsonProperty("partner")  public void setPartner(String partner) { this.partner = partner; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #has_ds_meta_bag */ @JsonProperty("has_ds_meta_bag")  public Reference getHasDsMetaBag() { return this.has_ds_meta_bag; }
    /** @see #has_ds_meta_bag */ @JsonProperty("has_ds_meta_bag")  public void setHasDsMetaBag(Reference has_ds_meta_bag) { this.has_ds_meta_bag = has_ds_meta_bag; }

    /** @see #pin_type */ @JsonProperty("pin_type")  public String getPinType() { return this.pin_type; }
    /** @see #pin_type */ @JsonProperty("pin_type")  public void setPinType(String pin_type) { this.pin_type = pin_type; }

    /** @see #top_text_pos */ @JsonProperty("top_text_pos")  public Number getTopTextPos() { return this.top_text_pos; }
    /** @see #top_text_pos */ @JsonProperty("top_text_pos")  public void setTopTextPos(Number top_text_pos) { this.top_text_pos = top_text_pos; }

    /** @see #internal_id */ @JsonProperty("internal_id")  public String getInternalId() { return this.internal_id; }
    /** @see #internal_id */ @JsonProperty("internal_id")  public void setInternalId(String internal_id) { this.internal_id = internal_id; }

    /** @see #has_ds_argument_map */ @JsonProperty("has_ds_argument_map")  public ReferenceList getHasDsArgumentMap() { return this.has_ds_argument_map; }
    /** @see #has_ds_argument_map */ @JsonProperty("has_ds_argument_map")  public void setHasDsArgumentMap(ReferenceList has_ds_argument_map) { this.has_ds_argument_map = has_ds_argument_map; }

    /** @see #of_job_component */ @JsonProperty("of_job_component")  public Reference getOfJobComponent() { return this.of_job_component; }
    /** @see #of_job_component */ @JsonProperty("of_job_component")  public void setOfJobComponent(Reference of_job_component) { this.of_job_component = of_job_component; }

    /** @see #is_source_of_link */ @JsonProperty("is_source_of_link")  public Reference getIsSourceOfLink() { return this.is_source_of_link; }
    /** @see #is_source_of_link */ @JsonProperty("is_source_of_link")  public void setIsSourceOfLink(Reference is_source_of_link) { this.is_source_of_link = is_source_of_link; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isJobOutputPin(Object obj) { return (obj.getClass() == JobOutputPin.class); }

}
