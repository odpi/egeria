/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

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
 * POJO for the {@code database_schema} asset type in IGC, displayed as '{@literal Database Schema}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("database_schema")
public class DatabaseSchema extends Reference {

    public static String getIgcTypeDisplayName() { return "Database Schema"; }

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
     * The {@code database} property, displayed as '{@literal Database}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference database;

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
     * The {@code database_tables} property, displayed as '{@literal Database Tables}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseTable} objects.
     */
    protected ReferenceList database_tables;

    /**
     * The {@code views} property, displayed as '{@literal Views}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link View} objects.
     */
    protected ReferenceList views;

    /**
     * The {@code database_aliases} property, displayed as '{@literal Database Aliases}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseAlias} objects.
     */
    protected ReferenceList database_aliases;

    /**
     * The {@code stored_procedures} property, displayed as '{@literal Stored Procedures}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StoredProcedure} objects.
     */
    protected ReferenceList stored_procedures;

    /**
     * The {@code implements_logical_data_models} property, displayed as '{@literal Implements Logical Data Models}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalDataModel} objects.
     */
    protected ReferenceList implements_logical_data_models;

    /**
     * The {@code implements_physical_data_models} property, displayed as '{@literal Implements Physical Data Models}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDataModel} objects.
     */
    protected ReferenceList implements_physical_data_models;

    /**
     * The {@code alias_(business_name)} property, displayed as '{@literal Alias (Business Name)}' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The {@code owner} property, displayed as '{@literal Owner}' in the IGC UI.
     */
    protected String owner;

    /**
     * The {@code imported_from} property, displayed as '{@literal Imported From}' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The {@code mapped_to_mdm_models} property, displayed as '{@literal Mapped to MDM Models}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MdmModel} objects.
     */
    protected ReferenceList mapped_to_mdm_models;

    /**
     * The {@code read_by_(static)} property, displayed as '{@literal Read by (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(static)") protected ReferenceList read_by__static_;

    /**
     * The {@code written_by_(static)} property, displayed as '{@literal Written by (Static)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(static)") protected ReferenceList written_by__static_;

    /**
     * The {@code read_by_(design)} property, displayed as '{@literal Read by (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(design)") protected ReferenceList read_by__design_;

    /**
     * The {@code written_by_(design)} property, displayed as '{@literal Written by (Design)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(design)") protected ReferenceList written_by__design_;

    /**
     * The {@code read_by_(operational)} property, displayed as '{@literal Read by (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(operational)") protected ReferenceList read_by__operational_;

    /**
     * The {@code written_by_(operational)} property, displayed as '{@literal Written by (Operational)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(operational)") protected ReferenceList written_by__operational_;

    /**
     * The {@code read_by_(user_defined)} property, displayed as '{@literal Read by (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(user_defined)") protected ReferenceList read_by__user_defined_;

    /**
     * The {@code written_by_(user_defined)} property, displayed as '{@literal Written by (User-Defined)}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(user_defined)") protected ReferenceList written_by__user_defined_;

    /**
     * The {@code impacted_by} property, displayed as '{@literal Impacted by}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacted_by;

    /**
     * The {@code impacts_on} property, displayed as '{@literal Impacts on}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacts_on;

    /**
     * The {@code same_as_data_sources} property, displayed as '{@literal Same as Data Sources}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList same_as_data_sources;

    /**
     * The {@code preferred_database_schema} property, displayed as '{@literal Preferred Database Schema}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DatabaseSchema} object.
     */
    protected Reference preferred_database_schema;

    /**
     * The {@code include_for_business_lineage} property, displayed as '{@literal Include for Business Lineage}' in the IGC UI.
     */
    protected Boolean include_for_business_lineage;

    /**
     * The {@code suggested_term_assignments} property, displayed as '{@literal Suggested Term Assignments}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TermAssignment} objects.
     */
    protected ReferenceList suggested_term_assignments;

    /**
     * The {@code database_domains} property, displayed as '{@literal Database Domains}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItemDefinition} objects.
     */
    protected ReferenceList database_domains;

    /**
     * The {@code data_policies} property, displayed as '{@literal Data Policies}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList data_policies;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #database */ @JsonProperty("database")  public Reference getDatabase() { return this.database; }
    /** @see #database */ @JsonProperty("database")  public void setDatabase(Reference database) { this.database = database; }

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

    /** @see #database_tables */ @JsonProperty("database_tables")  public ReferenceList getDatabaseTables() { return this.database_tables; }
    /** @see #database_tables */ @JsonProperty("database_tables")  public void setDatabaseTables(ReferenceList database_tables) { this.database_tables = database_tables; }

    /** @see #views */ @JsonProperty("views")  public ReferenceList getViews() { return this.views; }
    /** @see #views */ @JsonProperty("views")  public void setViews(ReferenceList views) { this.views = views; }

    /** @see #database_aliases */ @JsonProperty("database_aliases")  public ReferenceList getDatabaseAliases() { return this.database_aliases; }
    /** @see #database_aliases */ @JsonProperty("database_aliases")  public void setDatabaseAliases(ReferenceList database_aliases) { this.database_aliases = database_aliases; }

    /** @see #stored_procedures */ @JsonProperty("stored_procedures")  public ReferenceList getStoredProcedures() { return this.stored_procedures; }
    /** @see #stored_procedures */ @JsonProperty("stored_procedures")  public void setStoredProcedures(ReferenceList stored_procedures) { this.stored_procedures = stored_procedures; }

    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public ReferenceList getImplementsLogicalDataModels() { return this.implements_logical_data_models; }
    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public void setImplementsLogicalDataModels(ReferenceList implements_logical_data_models) { this.implements_logical_data_models = implements_logical_data_models; }

    /** @see #implements_physical_data_models */ @JsonProperty("implements_physical_data_models")  public ReferenceList getImplementsPhysicalDataModels() { return this.implements_physical_data_models; }
    /** @see #implements_physical_data_models */ @JsonProperty("implements_physical_data_models")  public void setImplementsPhysicalDataModels(ReferenceList implements_physical_data_models) { this.implements_physical_data_models = implements_physical_data_models; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #owner */ @JsonProperty("owner")  public String getOwner() { return this.owner; }
    /** @see #owner */ @JsonProperty("owner")  public void setOwner(String owner) { this.owner = owner; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #mapped_to_mdm_models */ @JsonProperty("mapped_to_mdm_models")  public ReferenceList getMappedToMdmModels() { return this.mapped_to_mdm_models; }
    /** @see #mapped_to_mdm_models */ @JsonProperty("mapped_to_mdm_models")  public void setMappedToMdmModels(ReferenceList mapped_to_mdm_models) { this.mapped_to_mdm_models = mapped_to_mdm_models; }

    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public ReferenceList getReadByStatic() { return this.read_by__static_; }
    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public void setReadByStatic(ReferenceList read_by__static_) { this.read_by__static_ = read_by__static_; }

    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public ReferenceList getWrittenByStatic() { return this.written_by__static_; }
    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public void setWrittenByStatic(ReferenceList written_by__static_) { this.written_by__static_ = written_by__static_; }

    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public ReferenceList getReadByDesign() { return this.read_by__design_; }
    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public void setReadByDesign(ReferenceList read_by__design_) { this.read_by__design_ = read_by__design_; }

    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public ReferenceList getWrittenByDesign() { return this.written_by__design_; }
    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public void setWrittenByDesign(ReferenceList written_by__design_) { this.written_by__design_ = written_by__design_; }

    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public ReferenceList getReadByOperational() { return this.read_by__operational_; }
    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public void setReadByOperational(ReferenceList read_by__operational_) { this.read_by__operational_ = read_by__operational_; }

    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public ReferenceList getWrittenByOperational() { return this.written_by__operational_; }
    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public void setWrittenByOperational(ReferenceList written_by__operational_) { this.written_by__operational_ = written_by__operational_; }

    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public ReferenceList getReadByUserDefined() { return this.read_by__user_defined_; }
    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public void setReadByUserDefined(ReferenceList read_by__user_defined_) { this.read_by__user_defined_ = read_by__user_defined_; }

    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public ReferenceList getWrittenByUserDefined() { return this.written_by__user_defined_; }
    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public void setWrittenByUserDefined(ReferenceList written_by__user_defined_) { this.written_by__user_defined_ = written_by__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public ReferenceList getSameAsDataSources() { return this.same_as_data_sources; }
    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public void setSameAsDataSources(ReferenceList same_as_data_sources) { this.same_as_data_sources = same_as_data_sources; }

    /** @see #preferred_database_schema */ @JsonProperty("preferred_database_schema")  public Reference getPreferredDatabaseSchema() { return this.preferred_database_schema; }
    /** @see #preferred_database_schema */ @JsonProperty("preferred_database_schema")  public void setPreferredDatabaseSchema(Reference preferred_database_schema) { this.preferred_database_schema = preferred_database_schema; }

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public ReferenceList getSuggestedTermAssignments() { return this.suggested_term_assignments; }
    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public void setSuggestedTermAssignments(ReferenceList suggested_term_assignments) { this.suggested_term_assignments = suggested_term_assignments; }

    /** @see #database_domains */ @JsonProperty("database_domains")  public ReferenceList getDatabaseDomains() { return this.database_domains; }
    /** @see #database_domains */ @JsonProperty("database_domains")  public void setDatabaseDomains(ReferenceList database_domains) { this.database_domains = database_domains; }

    /** @see #data_policies */ @JsonProperty("data_policies")  public ReferenceList getDataPolicies() { return this.data_policies; }
    /** @see #data_policies */ @JsonProperty("data_policies")  public void setDataPolicies(ReferenceList data_policies) { this.data_policies = data_policies; }

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
        "short_description",
        "long_description",
        "alias_(business_name)",
        "owner",
        "imported_from",
        "include_for_business_lineage",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "alias_(business_name)",
        "owner",
        "imported_from",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "database_tables",
        "views",
        "database_aliases",
        "stored_procedures",
        "implements_logical_data_models",
        "implements_physical_data_models",
        "mapped_to_mdm_models",
        "read_by_(static)",
        "written_by_(static)",
        "read_by_(design)",
        "written_by_(design)",
        "read_by_(operational)",
        "written_by_(operational)",
        "read_by_(user_defined)",
        "written_by_(user_defined)",
        "impacted_by",
        "impacts_on",
        "same_as_data_sources",
        "suggested_term_assignments",
        "database_domains",
        "data_policies",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "database",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "database_tables",
        "views",
        "database_aliases",
        "stored_procedures",
        "implements_logical_data_models",
        "implements_physical_data_models",
        "alias_(business_name)",
        "owner",
        "imported_from",
        "mapped_to_mdm_models",
        "read_by_(static)",
        "written_by_(static)",
        "read_by_(design)",
        "written_by_(design)",
        "read_by_(operational)",
        "written_by_(operational)",
        "read_by_(user_defined)",
        "written_by_(user_defined)",
        "impacted_by",
        "impacts_on",
        "same_as_data_sources",
        "preferred_database_schema",
        "include_for_business_lineage",
        "suggested_term_assignments",
        "database_domains",
        "data_policies",
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
    public static Boolean isDatabaseSchema(Object obj) { return (obj.getClass() == DatabaseSchema.class); }

}
