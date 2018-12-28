/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'changed_properties' asset type in IGC, displayed as 'Changed Properties' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChangedProperties extends Reference {

    public static String getIgcTypeId() { return "changed_properties"; }
    public static String getIgcTypeDisplayName() { return "Changed Properties"; }

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


    /** @see #term_history */ @JsonProperty("term_history")  public ReferenceList getTermHistory() { return this.term_history; }
    /** @see #term_history */ @JsonProperty("term_history")  public void setTermHistory(ReferenceList term_history) { this.term_history = term_history; }

    /** @see #property_name */ @JsonProperty("property_name")  public ArrayList<String> getPropertyName() { return this.property_name; }
    /** @see #property_name */ @JsonProperty("property_name")  public void setPropertyName(ArrayList<String> property_name) { this.property_name = property_name; }

    /** @see #previous_value */ @JsonProperty("previous_value")  public ArrayList<String> getPreviousValue() { return this.previous_value; }
    /** @see #previous_value */ @JsonProperty("previous_value")  public void setPreviousValue(ArrayList<String> previous_value) { this.previous_value = previous_value; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("property_name");
        add("previous_value");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("term_history");
    }};
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("term_history");
        add("property_name");
        add("previous_value");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isChangedProperties(Object obj) { return (obj.getClass() == ChangedProperties.class); }

}
