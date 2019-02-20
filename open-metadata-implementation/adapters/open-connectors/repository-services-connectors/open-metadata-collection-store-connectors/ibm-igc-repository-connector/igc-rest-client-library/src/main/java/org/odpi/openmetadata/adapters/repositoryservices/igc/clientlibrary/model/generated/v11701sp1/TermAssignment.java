/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code term_assignment} asset type in IGC, displayed as '{@literal Suggested Term Assignment}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermAssignment extends Reference {

    public static String getIgcTypeId() { return "term_assignment"; }
    public static String getIgcTypeDisplayName() { return "Suggested Term Assignment"; }

    /**
     * The {@code assign_to_term} property, displayed as '{@literal Term}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference assign_to_term;

    /**
     * The {@code assign_to_object} property, displayed as '{@literal Asset}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference assign_to_object;

    /**
     * The {@code confidence_percent} property, displayed as '{@literal Confidence (Percent)}' in the IGC UI.
     */
    protected Number confidence_percent;

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


    /** @see #assign_to_term */ @JsonProperty("assign_to_term")  public Reference getAssignToTerm() { return this.assign_to_term; }
    /** @see #assign_to_term */ @JsonProperty("assign_to_term")  public void setAssignToTerm(Reference assign_to_term) { this.assign_to_term = assign_to_term; }

    /** @see #assign_to_object */ @JsonProperty("assign_to_object")  public Reference getAssignToObject() { return this.assign_to_object; }
    /** @see #assign_to_object */ @JsonProperty("assign_to_object")  public void setAssignToObject(Reference assign_to_object) { this.assign_to_object = assign_to_object; }

    /** @see #confidence_percent */ @JsonProperty("confidence_percent")  public Number getConfidencePercent() { return this.confidence_percent; }
    /** @see #confidence_percent */ @JsonProperty("confidence_percent")  public void setConfidencePercent(Number confidence_percent) { this.confidence_percent = confidence_percent; }

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
        "confidence_percent",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "assign_to_term",
        "assign_to_object",
        "confidence_percent",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTermAssignment(Object obj) { return (obj.getClass() == TermAssignment.class); }

}
