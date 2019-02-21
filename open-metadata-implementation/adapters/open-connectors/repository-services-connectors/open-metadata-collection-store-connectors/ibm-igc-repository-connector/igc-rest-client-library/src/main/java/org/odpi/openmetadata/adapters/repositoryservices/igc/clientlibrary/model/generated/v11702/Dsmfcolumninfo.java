/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code dsmfcolumninfo} asset type in IGC, displayed as '{@literal DSMFColumnInfo}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsmfcolumninfo extends Reference {

    public static String getIgcTypeId() { return "dsmfcolumninfo"; }
    public static String getIgcTypeDisplayName() { return "DSMFColumnInfo"; }

    /**
     * The {@code column_value} property, displayed as '{@literal Column Value}' in the IGC UI.
     */
    protected String column_value;

    /**
     * The {@code a_xmeta_locking_root} property, displayed as '{@literal A XMeta Locking Root}' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The {@code of_ds_stage} property, displayed as '{@literal Of DS Stage}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference of_ds_stage;

    /**
     * The {@code sort_order} property, displayed as '{@literal Sort Order}' in the IGC UI.
     */
    protected Number sort_order;

    /**
     * The {@code usage_class} property, displayed as '{@literal Usage Class}' in the IGC UI.
     */
    protected String usage_class;

    /**
     * The {@code sort_link} property, displayed as '{@literal Sort Link}' in the IGC UI.
     */
    protected String sort_link;

    /**
     * The {@code aggregation} property, displayed as '{@literal Aggregation}' in the IGC UI.
     */
    protected String aggregation;

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

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


    /** @see #column_value */ @JsonProperty("column_value")  public String getColumnValue() { return this.column_value; }
    /** @see #column_value */ @JsonProperty("column_value")  public void setColumnValue(String column_value) { this.column_value = column_value; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public Reference getOfDsStage() { return this.of_ds_stage; }
    /** @see #of_ds_stage */ @JsonProperty("of_ds_stage")  public void setOfDsStage(Reference of_ds_stage) { this.of_ds_stage = of_ds_stage; }

    /** @see #sort_order */ @JsonProperty("sort_order")  public Number getSortOrder() { return this.sort_order; }
    /** @see #sort_order */ @JsonProperty("sort_order")  public void setSortOrder(Number sort_order) { this.sort_order = sort_order; }

    /** @see #usage_class */ @JsonProperty("usage_class")  public String getUsageClass() { return this.usage_class; }
    /** @see #usage_class */ @JsonProperty("usage_class")  public void setUsageClass(String usage_class) { this.usage_class = usage_class; }

    /** @see #sort_link */ @JsonProperty("sort_link")  public String getSortLink() { return this.sort_link; }
    /** @see #sort_link */ @JsonProperty("sort_link")  public void setSortLink(String sort_link) { this.sort_link = sort_link; }

    /** @see #aggregation */ @JsonProperty("aggregation")  public String getAggregation() { return this.aggregation; }
    /** @see #aggregation */ @JsonProperty("aggregation")  public void setAggregation(String aggregation) { this.aggregation = aggregation; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

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
        "column_value",
        "a_xmeta_locking_root",
        "sort_order",
        "usage_class",
        "sort_link",
        "aggregation",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "column_value",
        "a_xmeta_locking_root",
        "usage_class",
        "sort_link",
        "aggregation",
        "name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "column_value",
        "a_xmeta_locking_root",
        "of_ds_stage",
        "sort_order",
        "usage_class",
        "sort_link",
        "aggregation",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDsmfcolumninfo(Object obj) { return (obj.getClass() == Dsmfcolumninfo.class); }

}
