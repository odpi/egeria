/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

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
 * POJO for the {@code volume} asset type in IGC, displayed as '{@literal Volume}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("volume")
public class Volume extends Reference {

    public static String getIgcTypeDisplayName() { return "Volume"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code instance} property, displayed as '{@literal Instance}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Instance} object.
     */
    protected Reference instance;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

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
     * The {@code data_server} property, displayed as '{@literal Data Server}' in the IGC UI.
     */
    protected String data_server;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Primary (displayed in the UI as 'Primary')</li>
     *     <li>Retention (displayed in the UI as 'Retention')</li>
     *     <li>DiscoveryExport (displayed in the UI as 'Discovery Export')</li>
     *     <li>System (displayed in the UI as 'System')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code source_type} property, displayed as '{@literal Source Type}' in the IGC UI.
     */
    protected String source_type;

    /**
     * The {@code server_name} property, displayed as '{@literal Server}' in the IGC UI.
     */
    protected String server_name;

    /**
     * The {@code object_count} property, displayed as '{@literal Number of Objects}' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The {@code size} property, displayed as '{@literal Size (Bytes)}' in the IGC UI.
     */
    protected Number size;

    /**
     * The {@code last_harvested} property, displayed as '{@literal Last Harvest Date}' in the IGC UI.
     */
    protected Date last_harvested;

    /**
     * The {@code tool_id} property, displayed as '{@literal Tool ID}' in the IGC UI.
     */
    protected String tool_id;

    /**
     * The {@code sync_state} property, displayed as '{@literal State}' in the IGC UI.
     */
    protected String sync_state;

    /**
     * The {@code url} property, displayed as '{@literal URL}' in the IGC UI.
     */
    protected String url;

    /**
     * The {@code infosets} property, displayed as '{@literal InfoSets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Infoset} objects.
     */
    protected ReferenceList infosets;

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

    /** @see #instance */ @JsonProperty("instance")  public Reference getInstance() { return this.instance; }
    /** @see #instance */ @JsonProperty("instance")  public void setInstance(Reference instance) { this.instance = instance; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

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

    /** @see #data_server */ @JsonProperty("data_server")  public String getDataServer() { return this.data_server; }
    /** @see #data_server */ @JsonProperty("data_server")  public void setDataServer(String data_server) { this.data_server = data_server; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #source_type */ @JsonProperty("source_type")  public String getSourceType() { return this.source_type; }
    /** @see #source_type */ @JsonProperty("source_type")  public void setSourceType(String source_type) { this.source_type = source_type; }

    /** @see #server_name */ @JsonProperty("server_name")  public String getServerName() { return this.server_name; }
    /** @see #server_name */ @JsonProperty("server_name")  public void setServerName(String server_name) { this.server_name = server_name; }

    /** @see #object_count */ @JsonProperty("object_count")  public Number getObjectCount() { return this.object_count; }
    /** @see #object_count */ @JsonProperty("object_count")  public void setObjectCount(Number object_count) { this.object_count = object_count; }

    /** @see #size */ @JsonProperty("size")  public Number getSize() { return this.size; }
    /** @see #size */ @JsonProperty("size")  public void setSize(Number size) { this.size = size; }

    /** @see #last_harvested */ @JsonProperty("last_harvested")  public Date getLastHarvested() { return this.last_harvested; }
    /** @see #last_harvested */ @JsonProperty("last_harvested")  public void setLastHarvested(Date last_harvested) { this.last_harvested = last_harvested; }

    /** @see #tool_id */ @JsonProperty("tool_id")  public String getToolId() { return this.tool_id; }
    /** @see #tool_id */ @JsonProperty("tool_id")  public void setToolId(String tool_id) { this.tool_id = tool_id; }

    /** @see #sync_state */ @JsonProperty("sync_state")  public String getSyncState() { return this.sync_state; }
    /** @see #sync_state */ @JsonProperty("sync_state")  public void setSyncState(String sync_state) { this.sync_state = sync_state; }

    /** @see #url */ @JsonProperty("url")  public String getTheUrl() { return this.url; }
    /** @see #url */ @JsonProperty("url")  public void setTheUrl(String url) { this.url = url; }

    /** @see #infosets */ @JsonProperty("infosets")  public ReferenceList getInfosets() { return this.infosets; }
    /** @see #infosets */ @JsonProperty("infosets")  public void setInfosets(ReferenceList infosets) { this.infosets = infosets; }

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
        "data_server",
        "type",
        "source_type",
        "server_name",
        "object_count",
        "size",
        "last_harvested",
        "tool_id",
        "sync_state",
        "url",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "data_server",
        "source_type",
        "server_name",
        "tool_id",
        "sync_state",
        "url",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "infosets",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "instance",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "data_server",
        "type",
        "source_type",
        "server_name",
        "object_count",
        "size",
        "last_harvested",
        "tool_id",
        "sync_state",
        "url",
        "infosets",
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
    public static Boolean isVolume(Object obj) { return (obj.getClass() == Volume.class); }

}
