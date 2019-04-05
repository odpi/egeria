/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code job_output_pin} asset type in IGC, displayed as '{@literal Job Output Pin}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobOutputPin extends Reference {

    public static String getIgcTypeId() { return "job_output_pin"; }
    public static String getIgcTypeDisplayName() { return "Job Output Pin"; }

    /**
     * The {@code left_text_pos} property, displayed as '{@literal Left Text Pos}' in the IGC UI.
     */
    protected Number left_text_pos;

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
     * The {@code top_text_pos} property, displayed as '{@literal Top Text Pos}' in the IGC UI.
     */
    protected Number top_text_pos;

    /**
     * The {@code internal_id} property, displayed as '{@literal Internal ID}' in the IGC UI.
     */
    protected String internal_id;

    /**
     * The {@code has_ds_argument_map} property, displayed as '{@literal Has DS Argument Map}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsargumentmap} objects.
     */
    protected ReferenceList has_ds_argument_map;

    /**
     * The {@code of_job_component} property, displayed as '{@literal Of Job Component}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_job_component;

    /**
     * The {@code is_source_of_link} property, displayed as '{@literal Is Source Of Link}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference is_source_of_link;

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
        "left_text_pos",
        "partner",
        "a_xmeta_locking_root",
        "pin_type",
        "top_text_pos",
        "internal_id",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "partner",
        "a_xmeta_locking_root",
        "pin_type",
        "internal_id",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_ds_argument_map"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "left_text_pos",
        "partner",
        "a_xmeta_locking_root",
        "has_ds_meta_bag",
        "pin_type",
        "top_text_pos",
        "internal_id",
        "has_ds_argument_map",
        "of_job_component",
        "is_source_of_link",
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
    public static Boolean isJobOutputPin(Object obj) { return (obj.getClass() == JobOutputPin.class); }

}
