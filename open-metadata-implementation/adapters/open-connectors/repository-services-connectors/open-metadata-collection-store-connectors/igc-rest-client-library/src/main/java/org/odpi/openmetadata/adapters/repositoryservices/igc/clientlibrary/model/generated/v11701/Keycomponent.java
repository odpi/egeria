/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'keycomponent' asset type in IGC, displayed as 'KeyComponent' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Keycomponent extends Reference {

    public static String getIgcTypeId() { return "keycomponent"; }
    public static String getIgcTypeDisplayName() { return "KeyComponent"; }

    /**
     * The 'sorting_order' property, displayed as 'Sorting Order' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ASCENDING (displayed in the UI as 'ASCENDING')</li>
     *     <li>DESCENDING (displayed in the UI as 'DESCENDING')</li>
     *     <li>NONE (displayed in the UI as 'NONE')</li>
     * </ul>
     */
    protected String sorting_order;

    /**
     * The 'of_key' property, displayed as 'Of Key' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_key;

    /**
     * The 'uses_data_field' property, displayed as 'Uses Data Field' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference uses_data_field;

    /**
     * The 'references_data_field' property, displayed as 'References Data Field' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference references_data_field;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

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


    /** @see #sorting_order */ @JsonProperty("sorting_order")  public String getSortingOrder() { return this.sorting_order; }
    /** @see #sorting_order */ @JsonProperty("sorting_order")  public void setSortingOrder(String sorting_order) { this.sorting_order = sorting_order; }

    /** @see #of_key */ @JsonProperty("of_key")  public Reference getOfKey() { return this.of_key; }
    /** @see #of_key */ @JsonProperty("of_key")  public void setOfKey(Reference of_key) { this.of_key = of_key; }

    /** @see #uses_data_field */ @JsonProperty("uses_data_field")  public Reference getUsesDataField() { return this.uses_data_field; }
    /** @see #uses_data_field */ @JsonProperty("uses_data_field")  public void setUsesDataField(Reference uses_data_field) { this.uses_data_field = uses_data_field; }

    /** @see #references_data_field */ @JsonProperty("references_data_field")  public Reference getReferencesDataField() { return this.references_data_field; }
    /** @see #references_data_field */ @JsonProperty("references_data_field")  public void setReferencesDataField(Reference references_data_field) { this.references_data_field = references_data_field; }

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
        "sorting_order",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "sorting_order",
        "of_key",
        "uses_data_field",
        "references_data_field",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isKeycomponent(Object obj) { return (obj.getClass() == Keycomponent.class); }

}
