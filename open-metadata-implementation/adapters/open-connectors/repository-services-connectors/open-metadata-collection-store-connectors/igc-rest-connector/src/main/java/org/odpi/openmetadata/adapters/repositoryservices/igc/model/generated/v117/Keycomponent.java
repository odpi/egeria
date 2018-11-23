/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'keycomponent' asset type in IGC, displayed as 'KeyComponent' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Keycomponent extends MainObject {

    public static final String IGC_TYPE_ID = "keycomponent";

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


    public static final Boolean isKeycomponent(Object obj) { return (obj.getClass() == Keycomponent.class); }

}
