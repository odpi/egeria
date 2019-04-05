/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code term} asset type in IGC, displayed as '{@literal Term}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Term extends Reference {

    public static String getIgcTypeId() { return "term"; }
    public static String getIgcTypeDisplayName() { return "Term"; }

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
     * The {@code referencing_categories} property, displayed as '{@literal Referencing Categories}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList referencing_categories;

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
     * The {@code status} property, displayed as '{@literal Status}' in the IGC UI.
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
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code language} property, displayed as '{@literal Language}' in the IGC UI.
     */
    protected String language;

    /**
     * The {@code abbreviation} property, displayed as '{@literal Abbreviation}' in the IGC UI.
     */
    protected String abbreviation;

    /**
     * The {@code additional_abbreviation} property, displayed as '{@literal Additional Abbreviation}' in the IGC UI.
     */
    protected String additional_abbreviation;

    /**
     * The {@code example} property, displayed as '{@literal Example}' in the IGC UI.
     */
    protected String example;

    /**
     * The {@code usage} property, displayed as '{@literal Usage}' in the IGC UI.
     */
    protected String usage;

    /**
     * The {@code is_modifier} property, displayed as '{@literal Is Modifier}' in the IGC UI.
     */
    protected Boolean is_modifier;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
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
     * The {@code translations} property, displayed as '{@literal Translations}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList translations;

    /**
     * The {@code is_a_type_of} property, displayed as '{@literal Is a Type Of}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList is_a_type_of;

    /**
     * The {@code has_types} property, displayed as '{@literal Has Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList has_types;

    /**
     * The {@code is_of} property, displayed as '{@literal Is Of}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList is_of;

    /**
     * The {@code has_a} property, displayed as '{@literal Has A}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference has_a;

    /**
     * The {@code has_a_term} property, displayed as '{@literal Has A}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList has_a_term;

    /**
     * The {@code synonyms} property, displayed as '{@literal Synonyms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList synonyms;

    /**
     * The {@code preferred_synonym} property, displayed as '{@literal Preferred Synonym}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference preferred_synonym;

    /**
     * The {@code related_terms} property, displayed as '{@literal Related Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList related_terms;

    /**
     * The {@code replaces} property, displayed as '{@literal Replaces}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList replaces;

    /**
     * The {@code replaced_by} property, displayed as '{@literal Replaced By}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Term} object.
     */
    protected Reference replaced_by;

    /**
     * The {@code assigned_terms} property, displayed as '{@literal Assigned Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_terms;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code assigned_assets} property, displayed as '{@literal Assigned Assets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList assigned_assets;

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

    /** @see #referencing_categories */ @JsonProperty("referencing_categories")  public ReferenceList getReferencingCategories() { return this.referencing_categories; }
    /** @see #referencing_categories */ @JsonProperty("referencing_categories")  public void setReferencingCategories(ReferenceList referencing_categories) { this.referencing_categories = referencing_categories; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #assigned_assets */ @JsonProperty("assigned_assets")  public ReferenceList getAssignedAssets() { return this.assigned_assets; }
    /** @see #assigned_assets */ @JsonProperty("assigned_assets")  public void setAssignedAssets(ReferenceList assigned_assets) { this.assigned_assets = assigned_assets; }

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
        "status",
        "language",
        "abbreviation",
        "additional_abbreviation",
        "example",
        "usage",
        "is_modifier",
        "type",
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
        "abbreviation",
        "additional_abbreviation",
        "example",
        "usage",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "category_path",
        "referencing_categories",
        "labels",
        "stewards",
        "governed_by_rules",
        "translations",
        "is_a_type_of",
        "has_types",
        "is_of",
        "has_a_term",
        "synonyms",
        "related_terms",
        "replaces",
        "assigned_terms",
        "assigned_to_terms",
        "assigned_assets",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_category",
        "category_path",
        "referencing_categories",
        "labels",
        "stewards",
        "status",
        "governed_by_rules",
        "language",
        "abbreviation",
        "additional_abbreviation",
        "example",
        "usage",
        "is_modifier",
        "type",
        "translations",
        "is_a_type_of",
        "has_types",
        "is_of",
        "has_a",
        "has_a_term",
        "synonyms",
        "preferred_synonym",
        "related_terms",
        "replaces",
        "replaced_by",
        "assigned_terms",
        "assigned_to_terms",
        "assigned_assets",
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
    public static Boolean isTerm(Object obj) { return (obj.getClass() == Term.class); }

}
