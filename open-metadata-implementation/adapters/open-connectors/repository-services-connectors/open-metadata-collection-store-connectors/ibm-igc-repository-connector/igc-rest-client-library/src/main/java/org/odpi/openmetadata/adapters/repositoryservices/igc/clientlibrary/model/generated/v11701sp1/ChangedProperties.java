/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

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
 * POJO for the {@code changed_properties} asset type in IGC, displayed as '{@literal Changed Properties}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("changed_properties")
public class ChangedProperties extends Reference {

    public static String getIgcTypeDisplayName() { return "Changed Properties"; }

    /**
     * The {@code term_history} property, displayed as '{@literal Term History}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TermHistory} objects.
     */
    protected ReferenceList term_history;

    /**
     * The {@code property_name} property, displayed as '{@literal Property Name}' in the IGC UI.
     */
    protected ArrayList<String> property_name;

    /**
     * The {@code previous_value} property, displayed as '{@literal Previous Value}' in the IGC UI.
     */
    protected ArrayList<String> previous_value;

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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "property_name",
        "previous_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "property_name",
        "previous_value",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "term_history"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "term_history",
        "property_name",
        "previous_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isChangedProperties(Object obj) { return (obj.getClass() == ChangedProperties.class); }

}
