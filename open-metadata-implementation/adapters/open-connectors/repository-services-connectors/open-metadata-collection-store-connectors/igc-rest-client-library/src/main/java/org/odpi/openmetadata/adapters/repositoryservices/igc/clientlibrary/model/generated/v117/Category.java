/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'category' asset type in IGC, displayed as 'Category' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Category extends Reference {

    public static String getIgcTypeId() { return "category"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'parent_category' property, displayed as 'Parent Category' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Category} object.
     */
    protected Reference parent_category;

    /**
     * The 'category_path' property, displayed as 'Category Path' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList category_path;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'subcategories' property, displayed as 'Subcategories' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList subcategories;

    /**
     * The 'terms' property, displayed as 'Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList terms;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'workflow_current_state' property, displayed as 'Workflow Current State' in the IGC UI.
     */
    protected ArrayList<String> workflow_current_state;

    /**
     * The 'workflow_stored_state' property, displayed as 'Workflow Stored State' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_stored_state;

    /**
     * The 'glossary_type' property, displayed as 'Glossary Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>PUBLISHED (displayed in the UI as 'PUBLISHED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     * </ul>
     */
    protected String glossary_type;

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

    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public ArrayList<String> getWorkflowStoredState() { return this.workflow_stored_state; }
    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public void setWorkflowStoredState(ArrayList<String> workflow_stored_state) { this.workflow_stored_state = workflow_stored_state; }

    /** @see #glossary_type */ @JsonProperty("glossary_type")  public String getGlossaryType() { return this.glossary_type; }
    /** @see #glossary_type */ @JsonProperty("glossary_type")  public void setGlossaryType(String glossary_type) { this.glossary_type = glossary_type; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean isCategory(Object obj) { return (obj.getClass() == Category.class); }

}
