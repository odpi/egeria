/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'changed_properties' asset type in IGC, displayed as 'Changed Properties' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChangedProperties extends MainObject {

    public static final String IGC_TYPE_ID = "changed_properties";

    /**
     * The 'term_history' property, displayed as 'Term History' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TermHistory} objects.
     */
    protected ReferenceList term_history;

    /**
     * The 'property_name' property, displayed as 'Property Name' in the IGC UI.
     */
    protected ArrayList<String> property_name;

    /**
     * The 'previous_value' property, displayed as 'Previous Value' in the IGC UI.
     */
    protected ArrayList<String> previous_value;


    /** @see #term_history */ @JsonProperty("term_history")  public ReferenceList getTermHistory() { return this.term_history; }
    /** @see #term_history */ @JsonProperty("term_history")  public void setTermHistory(ReferenceList term_history) { this.term_history = term_history; }

    /** @see #property_name */ @JsonProperty("property_name")  public ArrayList<String> getPropertyName() { return this.property_name; }
    /** @see #property_name */ @JsonProperty("property_name")  public void setPropertyName(ArrayList<String> property_name) { this.property_name = property_name; }

    /** @see #previous_value */ @JsonProperty("previous_value")  public ArrayList<String> getPreviousValue() { return this.previous_value; }
    /** @see #previous_value */ @JsonProperty("previous_value")  public void setPreviousValue(ArrayList<String> previous_value) { this.previous_value = previous_value; }


    public static final Boolean isChangedProperties(Object obj) { return (obj.getClass() == ChangedProperties.class); }

}
