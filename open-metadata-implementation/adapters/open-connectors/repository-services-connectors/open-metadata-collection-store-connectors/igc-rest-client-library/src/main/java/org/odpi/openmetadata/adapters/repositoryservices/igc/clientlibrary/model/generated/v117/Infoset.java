/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * POJO for the 'infoset' asset type in IGC, displayed as 'InfoSet' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Infoset extends MainObject {

    public static final String IGC_TYPE_ID = "infoset";

    /**
     * The 'instance' property, displayed as 'Instance' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Instance} object.
     */
    protected Reference instance;

    /**
     * The 'sync_type' property, displayed as 'Include For Governance' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Regular (displayed in the UI as 'Regular')</li>
     *     <li>InferredPartial (displayed in the UI as 'Inferred Partial')</li>
     *     <li>InferredFull (displayed in the UI as 'Inferred Full')</li>
     * </ul>
     */
    protected String sync_type;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>User (displayed in the UI as 'User')</li>
     *     <li>System (displayed in the UI as 'System')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'composition' property, displayed as 'Composition' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>MixedLevel (displayed in the UI as 'MixedLevel')</li>
     *     <li>TopLevel (displayed in the UI as 'TopLevel')</li>
     * </ul>
     */
    protected String composition;

    /**
     * The 'object_count' property, displayed as 'Number of Objects' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The 'size' property, displayed as 'Size (Bytes)' in the IGC UI.
     */
    protected Number size;

    /**
     * The 'creator' property, displayed as 'Creator' in the IGC UI.
     */
    protected String creator;

    /**
     * The 'created' property, displayed as 'Created' in the IGC UI.
     */
    protected Date created;

    /**
     * The 'applied_data_classes' property, displayed as 'Data Classifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ClassificationContribution} objects.
     */
    protected ReferenceList applied_data_classes;

    /**
     * The 'tool_id' property, displayed as 'Tool ID' in the IGC UI.
     */
    protected String tool_id;

    /**
     * The 'sync_state' property, displayed as 'State' in the IGC UI.
     */
    protected String sync_state;

    /**
     * The 'url' property, displayed as 'URL' in the IGC UI.
     */
    protected String url;

    /**
     * The 'contributing_volumes' property, displayed as 'Volumes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link VolumeContribution} objects.
     */
    protected ReferenceList contributing_volumes;

    /**
     * The 'created_with' property, displayed as 'Originating Operation' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InfosetOperation} object.
     */
    protected Reference created_with;

    /**
     * The 'contributing_operations' property, displayed as 'Applied Operations' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InfosetOperation} objects.
     */
    protected ReferenceList contributing_operations;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #instance */ @JsonProperty("instance")  public Reference getInstance() { return this.instance; }
    /** @see #instance */ @JsonProperty("instance")  public void setInstance(Reference instance) { this.instance = instance; }

    /** @see #sync_type */ @JsonProperty("sync_type")  public String getSyncType() { return this.sync_type; }
    /** @see #sync_type */ @JsonProperty("sync_type")  public void setSyncType(String sync_type) { this.sync_type = sync_type; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #composition */ @JsonProperty("composition")  public String getComposition() { return this.composition; }
    /** @see #composition */ @JsonProperty("composition")  public void setComposition(String composition) { this.composition = composition; }

    /** @see #object_count */ @JsonProperty("object_count")  public Number getObjectCount() { return this.object_count; }
    /** @see #object_count */ @JsonProperty("object_count")  public void setObjectCount(Number object_count) { this.object_count = object_count; }

    /** @see #size */ @JsonProperty("size")  public Number getSize() { return this.size; }
    /** @see #size */ @JsonProperty("size")  public void setSize(Number size) { this.size = size; }

    /** @see #creator */ @JsonProperty("creator")  public String getCreator() { return this.creator; }
    /** @see #creator */ @JsonProperty("creator")  public void setCreator(String creator) { this.creator = creator; }

    /** @see #created */ @JsonProperty("created")  public Date getCreated() { return this.created; }
    /** @see #created */ @JsonProperty("created")  public void setCreated(Date created) { this.created = created; }

    /** @see #applied_data_classes */ @JsonProperty("applied_data_classes")  public ReferenceList getAppliedDataClasses() { return this.applied_data_classes; }
    /** @see #applied_data_classes */ @JsonProperty("applied_data_classes")  public void setAppliedDataClasses(ReferenceList applied_data_classes) { this.applied_data_classes = applied_data_classes; }

    /** @see #tool_id */ @JsonProperty("tool_id")  public String getToolId() { return this.tool_id; }
    /** @see #tool_id */ @JsonProperty("tool_id")  public void setToolId(String tool_id) { this.tool_id = tool_id; }

    /** @see #sync_state */ @JsonProperty("sync_state")  public String getSyncState() { return this.sync_state; }
    /** @see #sync_state */ @JsonProperty("sync_state")  public void setSyncState(String sync_state) { this.sync_state = sync_state; }

    /** @see #url */ @JsonProperty("url")  public String getTheUrl() { return this.url; }
    /** @see #url */ @JsonProperty("url")  public void setTheUrl(String url) { this.url = url; }

    /** @see #contributing_volumes */ @JsonProperty("contributing_volumes")  public ReferenceList getContributingVolumes() { return this.contributing_volumes; }
    /** @see #contributing_volumes */ @JsonProperty("contributing_volumes")  public void setContributingVolumes(ReferenceList contributing_volumes) { this.contributing_volumes = contributing_volumes; }

    /** @see #created_with */ @JsonProperty("created_with")  public Reference getCreatedWith() { return this.created_with; }
    /** @see #created_with */ @JsonProperty("created_with")  public void setCreatedWith(Reference created_with) { this.created_with = created_with; }

    /** @see #contributing_operations */ @JsonProperty("contributing_operations")  public ReferenceList getContributingOperations() { return this.contributing_operations; }
    /** @see #contributing_operations */ @JsonProperty("contributing_operations")  public void setContributingOperations(ReferenceList contributing_operations) { this.contributing_operations = contributing_operations; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isInfoset(Object obj) { return (obj.getClass() == Infoset.class); }

}
