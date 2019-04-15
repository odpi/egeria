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
 * POJO for the {@code category} asset type in IGC, displayed as '{@literal Category}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("category")
public class Category extends Reference {

    public static String getIgcTypeDisplayName() { return "Category"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code parent_category} property, displayed as '{@literal Parent Category}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Category} object.
     */
    protected Reference parent_category;

    /**
     * The {@code category_path} property, displayed as '{@literal Category Path}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList category_path;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code language} property, displayed as '{@literal Language}' in the IGC UI.
     */
    protected String language;

    /**
     * The {@code subcategories} property, displayed as '{@literal Subcategories}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList subcategories;

    /**
     * The {@code terms} property, displayed as '{@literal Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList terms;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code workflow_current_state} property, displayed as '{@literal Workflow Current State}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_current_state;

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


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #parent_category */ @JsonProperty("parent_category")  public Reference getParentCategory() { return this.parent_category; }
    /** @see #parent_category */ @JsonProperty("parent_category")  public void setParentCategory(Reference parent_category) { this.parent_category = parent_category; }

    /** @see #category_path */ @JsonProperty("category_path")  public ReferenceList getCategoryPath() { return this.category_path; }
    /** @see #category_path */ @JsonProperty("category_path")  public void setCategoryPath(ReferenceList category_path) { this.category_path = category_path; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #subcategories */ @JsonProperty("subcategories")  public ReferenceList getSubcategories() { return this.subcategories; }
    /** @see #subcategories */ @JsonProperty("subcategories")  public void setSubcategories(ReferenceList subcategories) { this.subcategories = subcategories; }

    /** @see #terms */ @JsonProperty("terms")  public ReferenceList getTerms() { return this.terms; }
    /** @see #terms */ @JsonProperty("terms")  public void setTerms(ReferenceList terms) { this.terms = terms; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public ArrayList<String> getWorkflowCurrentState() { return this.workflow_current_state; }
    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public void setWorkflowCurrentState(ArrayList<String> workflow_current_state) { this.workflow_current_state = workflow_current_state; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return true; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "language",
        "workflow_current_state",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "category_path",
        "labels",
        "stewards",
        "assigned_to_terms",
        "subcategories",
        "terms",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_category",
        "category_path",
        "labels",
        "stewards",
        "assigned_to_terms",
        "language",
        "subcategories",
        "terms",
        "in_collections",
        "workflow_current_state",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isCategory(Object obj) { return (obj.getClass() == Category.class); }

}
