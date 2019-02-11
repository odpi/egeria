/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code mapping_specification} asset type in IGC, displayed as '{@literal Mapping Specification}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingSpecification extends Reference {

    public static String getIgcTypeId() { return "mapping_specification"; }
    public static String getIgcTypeDisplayName() { return "Mapping Specification"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected ArrayList<String> description;

    /**
     * The {@code mapping_project} property, displayed as '{@literal Mapping Project}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingProject} objects.
     */
    protected ReferenceList mapping_project;

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
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code mappings} property, displayed as '{@literal Mappings}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Mapping} objects.
     */
    protected ReferenceList mappings;

    /**
     * The {@code source_database_tables_or_views} property, displayed as '{@literal Source Database Tables or Views}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList source_database_tables_or_views;

    /**
     * The {@code target_database_tables_or_views} property, displayed as '{@literal Target Database Tables or Views}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList target_database_tables_or_views;

    /**
     * The {@code status} property, displayed as '{@literal Status}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>PROCESSING_STARTED (displayed in the UI as 'PROCESSING_STARTED')</li>
     *     <li>REVIEW_COMPLETED (displayed in the UI as 'REVIEW_COMPLETED')</li>
     *     <li>DEPLOYMENT_COMPLETED (displayed in the UI as 'DEPLOYMENT_COMPLETED')</li>
     * </ul>
     */
    protected String status;

    /**
     * The {@code owner} property, displayed as '{@literal Owner}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference owner;

    /**
     * The {@code include_for_lineage} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>setToTrue (displayed in the UI as 'True')</li>
     *     <li>setToFalse (displayed in the UI as 'False')</li>
     * </ul>
     */
    protected String include_for_lineage;

    /**
     * The {@code joins} property, displayed as '{@literal Joins}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingJoin} objects.
     */
    protected ReferenceList joins;

    /**
     * The {@code filters} property, displayed as '{@literal Filters}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingFilter} objects.
     */
    protected ReferenceList filters;

    /**
     * The {@code generated_jobs} property, displayed as '{@literal Generated Jobs}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList generated_jobs;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

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

    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #mapping_project */ @JsonProperty("mapping_project")  public ReferenceList getMappingProject() { return this.mapping_project; }
    /** @see #mapping_project */ @JsonProperty("mapping_project")  public void setMappingProject(ReferenceList mapping_project) { this.mapping_project = mapping_project; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

    /** @see #mappings */ @JsonProperty("mappings")  public ReferenceList getMappings() { return this.mappings; }
    /** @see #mappings */ @JsonProperty("mappings")  public void setMappings(ReferenceList mappings) { this.mappings = mappings; }

    /** @see #source_database_tables_or_views */ @JsonProperty("source_database_tables_or_views")  public ReferenceList getSourceDatabaseTablesOrViews() { return this.source_database_tables_or_views; }
    /** @see #source_database_tables_or_views */ @JsonProperty("source_database_tables_or_views")  public void setSourceDatabaseTablesOrViews(ReferenceList source_database_tables_or_views) { this.source_database_tables_or_views = source_database_tables_or_views; }

    /** @see #target_database_tables_or_views */ @JsonProperty("target_database_tables_or_views")  public ReferenceList getTargetDatabaseTablesOrViews() { return this.target_database_tables_or_views; }
    /** @see #target_database_tables_or_views */ @JsonProperty("target_database_tables_or_views")  public void setTargetDatabaseTablesOrViews(ReferenceList target_database_tables_or_views) { this.target_database_tables_or_views = target_database_tables_or_views; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #owner */ @JsonProperty("owner")  public Reference getOwner() { return this.owner; }
    /** @see #owner */ @JsonProperty("owner")  public void setOwner(Reference owner) { this.owner = owner; }

    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public String getIncludeForLineage() { return this.include_for_lineage; }
    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public void setIncludeForLineage(String include_for_lineage) { this.include_for_lineage = include_for_lineage; }

    /** @see #joins */ @JsonProperty("joins")  public ReferenceList getJoins() { return this.joins; }
    /** @see #joins */ @JsonProperty("joins")  public void setJoins(ReferenceList joins) { this.joins = joins; }

    /** @see #filters */ @JsonProperty("filters")  public ReferenceList getFilters() { return this.filters; }
    /** @see #filters */ @JsonProperty("filters")  public void setFilters(ReferenceList filters) { this.filters = filters; }

    /** @see #generated_jobs */ @JsonProperty("generated_jobs")  public ReferenceList getGeneratedJobs() { return this.generated_jobs; }
    /** @see #generated_jobs */ @JsonProperty("generated_jobs")  public void setGeneratedJobs(ReferenceList generated_jobs) { this.generated_jobs = generated_jobs; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

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
        "name",
        "description",
        "status",
        "include_for_lineage",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "mapping_project",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "mappings",
        "source_database_tables_or_views",
        "target_database_tables_or_views",
        "joins",
        "filters",
        "generated_jobs",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "mapping_project",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "mappings",
        "source_database_tables_or_views",
        "target_database_tables_or_views",
        "status",
        "owner",
        "include_for_lineage",
        "joins",
        "filters",
        "generated_jobs",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isMappingSpecification(Object obj) { return (obj.getClass() == MappingSpecification.class); }

}
