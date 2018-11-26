/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'term' asset type in IGC, displayed as 'Term' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Term extends MainObject {

    public static final String IGC_TYPE_ID = "term";

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
     * The 'referencing_categories' property, displayed as 'Referencing Categories' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList referencing_categories;

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CANDIDATE (displayed in the UI as 'Candidate')</li>
     *     <li>ACCEPTED (displayed in the UI as 'Accepted')</li>
     *     <li>STANDARD (displayed in the UI as 'Standard')</li>
     *     <li>DEPRECATED (displayed in the UI as 'Deprecated')</li>
     * </ul>
     */
    protected String status;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'abbreviation' property, displayed as 'Abbreviation' in the IGC UI.
     */
    protected String abbreviation;

    /**
     * The 'additional_abbreviation' property, displayed as 'Additional Abbreviation' in the IGC UI.
     */
    protected String additional_abbreviation;

    /**
     * The 'example' property, displayed as 'Example' in the IGC UI.
     */
    protected String example;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     */
    protected String usage;

    /**
     * The 'is_modifier' property, displayed as 'Is Modifier' in the IGC UI.
     */
    protected Boolean is_modifier;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>NONE (displayed in the UI as 'None')</li>
     *     <li>PRIME (displayed in the UI as 'Primary')</li>
     *     <li>CLASS (displayed in the UI as 'Secondary')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'translations' property, displayed as 'Translations' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList translations;

    /**
     * The 'is_a_type_of' property, displayed as 'Is a Type Of' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList is_a_type_of;

    /**
     * The 'has_types' property, displayed as 'Has Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList has_types;

    /**
     * The 'is_of' property, displayed as 'Is Of' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList is_of;

    /**
     * The 'has_a' property, displayed as 'Has A' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference has_a;

    /**
     * The 'has_a_term' property, displayed as 'Has A' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList has_a_term;

    /**
     * The 'synonyms' property, displayed as 'Synonyms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList synonyms;

    /**
     * The 'preferred_synonym' property, displayed as 'Preferred Synonym' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference preferred_synonym;

    /**
     * The 'related_terms' property, displayed as 'Related Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList related_terms;

    /**
     * The 'replaces' property, displayed as 'Replaces' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList replaces;

    /**
     * The 'replaced_by' property, displayed as 'Replaced By' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference replaced_by;

    /**
     * The 'assigned_terms' property, displayed as 'Assigned Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_terms;

    /**
     * The 'assigned_assets' property, displayed as 'Assigned Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList assigned_assets;

    /**
     * The 'blueprint_elements' property, displayed as 'Blueprint Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'workflow_current_state' property, displayed as 'Workflow Current State' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_current_state;


    /** @see #parent_category */ @JsonProperty("parent_category")  public Reference getParentCategory() { return this.parent_category; }
    /** @see #parent_category */ @JsonProperty("parent_category")  public void setParentCategory(Reference parent_category) { this.parent_category = parent_category; }

    /** @see #category_path */ @JsonProperty("category_path")  public ReferenceList getCategoryPath() { return this.category_path; }
    /** @see #category_path */ @JsonProperty("category_path")  public void setCategoryPath(ReferenceList category_path) { this.category_path = category_path; }

    /** @see #referencing_categories */ @JsonProperty("referencing_categories")  public ReferenceList getReferencingCategories() { return this.referencing_categories; }
    /** @see #referencing_categories */ @JsonProperty("referencing_categories")  public void setReferencingCategories(ReferenceList referencing_categories) { this.referencing_categories = referencing_categories; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #abbreviation */ @JsonProperty("abbreviation")  public String getAbbreviation() { return this.abbreviation; }
    /** @see #abbreviation */ @JsonProperty("abbreviation")  public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    /** @see #additional_abbreviation */ @JsonProperty("additional_abbreviation")  public String getAdditionalAbbreviation() { return this.additional_abbreviation; }
    /** @see #additional_abbreviation */ @JsonProperty("additional_abbreviation")  public void setAdditionalAbbreviation(String additional_abbreviation) { this.additional_abbreviation = additional_abbreviation; }

    /** @see #example */ @JsonProperty("example")  public String getExample() { return this.example; }
    /** @see #example */ @JsonProperty("example")  public void setExample(String example) { this.example = example; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #is_modifier */ @JsonProperty("is_modifier")  public Boolean getIsModifier() { return this.is_modifier; }
    /** @see #is_modifier */ @JsonProperty("is_modifier")  public void setIsModifier(Boolean is_modifier) { this.is_modifier = is_modifier; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #translations */ @JsonProperty("translations")  public ReferenceList getTranslations() { return this.translations; }
    /** @see #translations */ @JsonProperty("translations")  public void setTranslations(ReferenceList translations) { this.translations = translations; }

    /** @see #is_a_type_of */ @JsonProperty("is_a_type_of")  public ReferenceList getIsATypeOf() { return this.is_a_type_of; }
    /** @see #is_a_type_of */ @JsonProperty("is_a_type_of")  public void setIsATypeOf(ReferenceList is_a_type_of) { this.is_a_type_of = is_a_type_of; }

    /** @see #has_types */ @JsonProperty("has_types")  public ReferenceList getHasTypes() { return this.has_types; }
    /** @see #has_types */ @JsonProperty("has_types")  public void setHasTypes(ReferenceList has_types) { this.has_types = has_types; }

    /** @see #is_of */ @JsonProperty("is_of")  public ReferenceList getIsOf() { return this.is_of; }
    /** @see #is_of */ @JsonProperty("is_of")  public void setIsOf(ReferenceList is_of) { this.is_of = is_of; }

    /** @see #has_a */ @JsonProperty("has_a")  public Reference getHasA() { return this.has_a; }
    /** @see #has_a */ @JsonProperty("has_a")  public void setHasA(Reference has_a) { this.has_a = has_a; }

    /** @see #has_a_term */ @JsonProperty("has_a_term")  public ReferenceList getHasATerm() { return this.has_a_term; }
    /** @see #has_a_term */ @JsonProperty("has_a_term")  public void setHasATerm(ReferenceList has_a_term) { this.has_a_term = has_a_term; }

    /** @see #synonyms */ @JsonProperty("synonyms")  public ReferenceList getSynonyms() { return this.synonyms; }
    /** @see #synonyms */ @JsonProperty("synonyms")  public void setSynonyms(ReferenceList synonyms) { this.synonyms = synonyms; }

    /** @see #preferred_synonym */ @JsonProperty("preferred_synonym")  public Reference getPreferredSynonym() { return this.preferred_synonym; }
    /** @see #preferred_synonym */ @JsonProperty("preferred_synonym")  public void setPreferredSynonym(Reference preferred_synonym) { this.preferred_synonym = preferred_synonym; }

    /** @see #related_terms */ @JsonProperty("related_terms")  public ReferenceList getRelatedTerms() { return this.related_terms; }
    /** @see #related_terms */ @JsonProperty("related_terms")  public void setRelatedTerms(ReferenceList related_terms) { this.related_terms = related_terms; }

    /** @see #replaces */ @JsonProperty("replaces")  public ReferenceList getReplaces() { return this.replaces; }
    /** @see #replaces */ @JsonProperty("replaces")  public void setReplaces(ReferenceList replaces) { this.replaces = replaces; }

    /** @see #replaced_by */ @JsonProperty("replaced_by")  public Reference getReplacedBy() { return this.replaced_by; }
    /** @see #replaced_by */ @JsonProperty("replaced_by")  public void setReplacedBy(Reference replaced_by) { this.replaced_by = replaced_by; }

    /** @see #assigned_terms */ @JsonProperty("assigned_terms")  public ReferenceList getAssignedTerms() { return this.assigned_terms; }
    /** @see #assigned_terms */ @JsonProperty("assigned_terms")  public void setAssignedTerms(ReferenceList assigned_terms) { this.assigned_terms = assigned_terms; }

    /** @see #assigned_assets */ @JsonProperty("assigned_assets")  public ReferenceList getAssignedAssets() { return this.assigned_assets; }
    /** @see #assigned_assets */ @JsonProperty("assigned_assets")  public void setAssignedAssets(ReferenceList assigned_assets) { this.assigned_assets = assigned_assets; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public ArrayList<String> getWorkflowCurrentState() { return this.workflow_current_state; }
    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public void setWorkflowCurrentState(ArrayList<String> workflow_current_state) { this.workflow_current_state = workflow_current_state; }


    public static final Boolean isTerm(Object obj) { return (obj.getClass() == Term.class); }

}
