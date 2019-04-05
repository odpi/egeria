/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code data_file_folder} asset type in IGC, displayed as '{@literal Data File Folder}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileFolder extends Reference {

    public static String getIgcTypeId() { return "data_file_folder"; }
    public static String getIgcTypeDisplayName() { return "Data File Folder"; }

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
     * The {@code parent_folder_or_host} property, displayed as '{@literal Parent Folder or Host}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference parent_folder_or_host;

    /**
     * The {@code parent_folder} property, displayed as '{@literal Parent Folder}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference parent_folder;

    /**
     * The {@code host} property, displayed as '{@literal Host}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

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
     * The {@code data_file_folders} property, displayed as '{@literal Data File Folders}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileFolder} objects.
     */
    protected ReferenceList data_file_folders;

    /**
     * The {@code data_files} property, displayed as '{@literal Data Files}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFile} objects.
     */
    protected ReferenceList data_files;

    /**
     * The {@code uses_data_file_definitions} property, displayed as '{@literal Uses Data File Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileDefinition} objects.
     */
    protected ReferenceList uses_data_file_definitions;

    /**
     * The {@code location} property, displayed as '{@literal Location}' in the IGC UI.
     */
    protected String location;

    /**
     * The {@code source_creation_date} property, displayed as '{@literal Source Creation Date}' in the IGC UI.
     */
    protected Date source_creation_date;

    /**
     * The {@code source_modification_date} property, displayed as '{@literal Source Modification Date}' in the IGC UI.
     */
    protected Date source_modification_date;

    /**
     * The {@code store_type} property, displayed as '{@literal Store Type}' in the IGC UI.
     */
    protected String store_type;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code data_connection} property, displayed as '{@literal Data Connection}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnection} objects.
     */
    protected ReferenceList data_connection;

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

    /** @see #parent_folder_or_host */ @JsonProperty("parent_folder_or_host")  public Reference getParentFolderOrHost() { return this.parent_folder_or_host; }
    /** @see #parent_folder_or_host */ @JsonProperty("parent_folder_or_host")  public void setParentFolderOrHost(Reference parent_folder_or_host) { this.parent_folder_or_host = parent_folder_or_host; }

    /** @see #parent_folder */ @JsonProperty("parent_folder")  public Reference getParentFolder() { return this.parent_folder; }
    /** @see #parent_folder */ @JsonProperty("parent_folder")  public void setParentFolder(Reference parent_folder) { this.parent_folder = parent_folder; }

    /** @see #host */ @JsonProperty("host")  public Reference getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(Reference host) { this.host = host; }

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

    /** @see #data_file_folders */ @JsonProperty("data_file_folders")  public ReferenceList getDataFileFolders() { return this.data_file_folders; }
    /** @see #data_file_folders */ @JsonProperty("data_file_folders")  public void setDataFileFolders(ReferenceList data_file_folders) { this.data_file_folders = data_file_folders; }

    /** @see #data_files */ @JsonProperty("data_files")  public ReferenceList getDataFiles() { return this.data_files; }
    /** @see #data_files */ @JsonProperty("data_files")  public void setDataFiles(ReferenceList data_files) { this.data_files = data_files; }

    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public ReferenceList getUsesDataFileDefinitions() { return this.uses_data_file_definitions; }
    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public void setUsesDataFileDefinitions(ReferenceList uses_data_file_definitions) { this.uses_data_file_definitions = uses_data_file_definitions; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public Date getSourceCreationDate() { return this.source_creation_date; }
    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public void setSourceCreationDate(Date source_creation_date) { this.source_creation_date = source_creation_date; }

    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public Date getSourceModificationDate() { return this.source_modification_date; }
    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public void setSourceModificationDate(Date source_modification_date) { this.source_modification_date = source_modification_date; }

    /** @see #store_type */ @JsonProperty("store_type")  public String getStoreType() { return this.store_type; }
    /** @see #store_type */ @JsonProperty("store_type")  public void setStoreType(String store_type) { this.store_type = store_type; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #data_connection */ @JsonProperty("data_connection")  public ReferenceList getDataConnection() { return this.data_connection; }
    /** @see #data_connection */ @JsonProperty("data_connection")  public void setDataConnection(ReferenceList data_connection) { this.data_connection = data_connection; }

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

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public ReferenceList getSuggestedTermAssignments() { return this.suggested_term_assignments; }
    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public void setSuggestedTermAssignments(ReferenceList suggested_term_assignments) { this.suggested_term_assignments = suggested_term_assignments; }

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
        "location",
        "source_creation_date",
        "source_modification_date",
        "store_type",
        "type",
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
        "location",
        "store_type",
        "type",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "data_file_folders",
        "data_files",
        "uses_data_file_definitions",
        "data_connection",
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
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_folder_or_host",
        "parent_folder",
        "host",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "data_file_folders",
        "data_files",
        "uses_data_file_definitions",
        "location",
        "source_creation_date",
        "source_modification_date",
        "store_type",
        "type",
        "data_connection",
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
        "include_for_business_lineage",
        "suggested_term_assignments",
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
    public static Boolean isDataFileFolder(Object obj) { return (obj.getClass() == DataFileFolder.class); }

}
