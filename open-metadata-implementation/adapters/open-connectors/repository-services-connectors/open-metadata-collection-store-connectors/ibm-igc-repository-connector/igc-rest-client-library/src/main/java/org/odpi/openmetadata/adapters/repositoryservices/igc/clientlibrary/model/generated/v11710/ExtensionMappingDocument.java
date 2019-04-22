/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

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
 * POJO for the {@code extension_mapping_document} asset type in IGC, displayed as '{@literal Extension Mapping Document}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("extension_mapping_document")
public class ExtensionMappingDocument extends Reference {

    public static String getIgcTypeDisplayName() { return "Extension Mapping Document"; }

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
     * The {@code parent_folder} property, displayed as '{@literal Parent Folder}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Folder} objects.
     */
    protected ReferenceList parent_folder;

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
     * The {@code extension_mappings} property, displayed as '{@literal Extension Mappings}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ExtensionMapping} objects.
     */
    protected ReferenceList extension_mappings;

    /**
     * The {@code file_name} property, displayed as '{@literal File Name}' in the IGC UI.
     */
    protected String file_name;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code native_id} property, displayed as '{@literal Native ID}' in the IGC UI.
     */
    protected String native_id;

    /**
     * The {@code lineage_service_last_run_date} property, displayed as '{@literal Lineage Service Last Run Date}' in the IGC UI.
     */
    protected ArrayList<Date> lineage_service_last_run_date;

    /**
     * The {@code lineage_service_status} property, displayed as '{@literal Lineage Service Status}' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_status;

    /**
     * The {@code lineage_service_information} property, displayed as '{@literal Lineage Service Information}' in the IGC UI.
     */
    protected ArrayList<String> lineage_service_information;

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

    /** @see #parent_folder */ @JsonProperty("parent_folder")  public ReferenceList getParentFolder() { return this.parent_folder; }
    /** @see #parent_folder */ @JsonProperty("parent_folder")  public void setParentFolder(ReferenceList parent_folder) { this.parent_folder = parent_folder; }

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

    /** @see #extension_mappings */ @JsonProperty("extension_mappings")  public ReferenceList getExtensionMappings() { return this.extension_mappings; }
    /** @see #extension_mappings */ @JsonProperty("extension_mappings")  public void setExtensionMappings(ReferenceList extension_mappings) { this.extension_mappings = extension_mappings; }

    /** @see #file_name */ @JsonProperty("file_name")  public String getFileName() { return this.file_name; }
    /** @see #file_name */ @JsonProperty("file_name")  public void setFileName(String file_name) { this.file_name = file_name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    /** @see #lineage_service_last_run_date */ @JsonProperty("lineage_service_last_run_date")  public ArrayList<Date> getLineageServiceLastRunDate() { return this.lineage_service_last_run_date; }
    /** @see #lineage_service_last_run_date */ @JsonProperty("lineage_service_last_run_date")  public void setLineageServiceLastRunDate(ArrayList<Date> lineage_service_last_run_date) { this.lineage_service_last_run_date = lineage_service_last_run_date; }

    /** @see #lineage_service_status */ @JsonProperty("lineage_service_status")  public ArrayList<String> getLineageServiceStatus() { return this.lineage_service_status; }
    /** @see #lineage_service_status */ @JsonProperty("lineage_service_status")  public void setLineageServiceStatus(ArrayList<String> lineage_service_status) { this.lineage_service_status = lineage_service_status; }

    /** @see #lineage_service_information */ @JsonProperty("lineage_service_information")  public ArrayList<String> getLineageServiceInformation() { return this.lineage_service_information; }
    /** @see #lineage_service_information */ @JsonProperty("lineage_service_information")  public void setLineageServiceInformation(ArrayList<String> lineage_service_information) { this.lineage_service_information = lineage_service_information; }

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
        "file_name",
        "type",
        "native_id",
        "lineage_service_last_run_date",
        "lineage_service_status",
        "lineage_service_information",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "file_name",
        "type",
        "native_id",
        "lineage_service_status",
        "lineage_service_information",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "parent_folder",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "extension_mappings",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_folder",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "extension_mappings",
        "file_name",
        "type",
        "native_id",
        "lineage_service_last_run_date",
        "lineage_service_status",
        "lineage_service_information",
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
    public static Boolean isExtensionMappingDocument(Object obj) { return (obj.getClass() == ExtensionMappingDocument.class); }

}
