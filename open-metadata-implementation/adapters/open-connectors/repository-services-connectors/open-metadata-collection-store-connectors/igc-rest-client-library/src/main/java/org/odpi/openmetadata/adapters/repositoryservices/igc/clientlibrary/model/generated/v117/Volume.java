/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'volume' asset type in IGC, displayed as 'Volume' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Volume extends MainObject {

    public static final String IGC_TYPE_ID = "volume";

    /**
     * The 'instance' property, displayed as 'Instance' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Instance} object.
     */
    protected Reference instance;

    /**
     * The 'data_server' property, displayed as 'Data Server' in the IGC UI.
     */
    protected String data_server;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
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
     * The 'source_type' property, displayed as 'Source Type' in the IGC UI.
     */
    protected String source_type;

    /**
     * The 'server_name' property, displayed as 'Server' in the IGC UI.
     */
    protected String server_name;

    /**
     * The 'object_count' property, displayed as 'Number of Objects' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The 'size' property, displayed as 'Size (Bytes)' in the IGC UI.
     */
    protected Number size;

    /**
     * The 'last_harvested' property, displayed as 'Last Harvest Date' in the IGC UI.
     */
    protected Date last_harvested;

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
     * The 'infosets' property, displayed as 'InfoSets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Infoset} objects.
     */
    protected ReferenceList infosets;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #instance */ @JsonProperty("instance")  public Reference getInstance() { return this.instance; }
    /** @see #instance */ @JsonProperty("instance")  public void setInstance(Reference instance) { this.instance = instance; }

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


    public static final Boolean isVolume(Object obj) { return (obj.getClass() == Volume.class); }

}
