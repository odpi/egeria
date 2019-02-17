/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code amazon_s3_bucket} asset type in IGC, displayed as '{@literal Amazon S3 Bucket}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AmazonS3Bucket extends Reference {

    public static String getIgcTypeId() { return "amazon_s3_bucket"; }
    public static String getIgcTypeDisplayName() { return "Amazon S3 Bucket"; }

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
     * The {@code contains_amazon_s3_data_file_folders} property, displayed as '{@literal Contains Amazon S3 Data File Folders}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_amazon_s3_data_file_folders;

    /**
     * The {@code contains_amazon_s3_data_files} property, displayed as '{@literal Contains Amazon S3 Data Files}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AmazonS3DataFile} objects.
     */
    protected ReferenceList contains_amazon_s3_data_files;

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
     * The {@code data_connection} property, displayed as '{@literal Data Connection}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnection} objects.
     */
    protected ReferenceList data_connection;

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

    /** @see #contains_amazon_s3_data_file_folders */ @JsonProperty("contains_amazon_s3_data_file_folders")  public ReferenceList getContainsAmazonS3DataFileFolders() { return this.contains_amazon_s3_data_file_folders; }
    /** @see #contains_amazon_s3_data_file_folders */ @JsonProperty("contains_amazon_s3_data_file_folders")  public void setContainsAmazonS3DataFileFolders(ReferenceList contains_amazon_s3_data_file_folders) { this.contains_amazon_s3_data_file_folders = contains_amazon_s3_data_file_folders; }

    /** @see #contains_amazon_s3_data_files */ @JsonProperty("contains_amazon_s3_data_files")  public ReferenceList getContainsAmazonS3DataFiles() { return this.contains_amazon_s3_data_files; }
    /** @see #contains_amazon_s3_data_files */ @JsonProperty("contains_amazon_s3_data_files")  public void setContainsAmazonS3DataFiles(ReferenceList contains_amazon_s3_data_files) { this.contains_amazon_s3_data_files = contains_amazon_s3_data_files; }

    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public ReferenceList getUsesDataFileDefinitions() { return this.uses_data_file_definitions; }
    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public void setUsesDataFileDefinitions(ReferenceList uses_data_file_definitions) { this.uses_data_file_definitions = uses_data_file_definitions; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public Date getSourceCreationDate() { return this.source_creation_date; }
    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public void setSourceCreationDate(Date source_creation_date) { this.source_creation_date = source_creation_date; }

    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public Date getSourceModificationDate() { return this.source_modification_date; }
    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public void setSourceModificationDate(Date source_modification_date) { this.source_modification_date = source_modification_date; }

    /** @see #data_connection */ @JsonProperty("data_connection")  public ReferenceList getDataConnection() { return this.data_connection; }
    /** @see #data_connection */ @JsonProperty("data_connection")  public void setDataConnection(ReferenceList data_connection) { this.data_connection = data_connection; }

    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public ReferenceList getSameAsDataSources() { return this.same_as_data_sources; }
    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public void setSameAsDataSources(ReferenceList same_as_data_sources) { this.same_as_data_sources = same_as_data_sources; }

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

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
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_amazon_s3_data_file_folders",
        "contains_amazon_s3_data_files",
        "uses_data_file_definitions",
        "data_connection",
        "same_as_data_sources",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "host",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_amazon_s3_data_file_folders",
        "contains_amazon_s3_data_files",
        "uses_data_file_definitions",
        "location",
        "source_creation_date",
        "source_modification_date",
        "data_connection",
        "same_as_data_sources",
        "include_for_business_lineage",
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
    public static Boolean isAmazonS3Bucket(Object obj) { return (obj.getClass() == AmazonS3Bucket.class); }

}
