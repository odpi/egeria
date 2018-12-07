/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'filter' asset type in IGC, displayed as 'Filter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Filter extends MainObject {

    public static final String IGC_TYPE_ID = "filter";

    /**
     * The 'instance' property, displayed as 'Instance' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Instance} object.
     */
    protected Reference instance;

    /**
     * The 'filter_expression' property, displayed as 'Filter Expression' in the IGC UI.
     */
    protected String filter_expression;

    /**
     * The 'creator' property, displayed as 'Creator' in the IGC UI.
     */
    protected String creator;

    /**
     * The 'detects_data_class' property, displayed as 'Data Classification' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference detects_data_class;

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

    /** @see #filter_expression */ @JsonProperty("filter_expression")  public String getFilterExpression() { return this.filter_expression; }
    /** @see #filter_expression */ @JsonProperty("filter_expression")  public void setFilterExpression(String filter_expression) { this.filter_expression = filter_expression; }

    /** @see #creator */ @JsonProperty("creator")  public String getCreator() { return this.creator; }
    /** @see #creator */ @JsonProperty("creator")  public void setCreator(String creator) { this.creator = creator; }

    /** @see #detects_data_class */ @JsonProperty("detects_data_class")  public Reference getDetectsDataClass() { return this.detects_data_class; }
    /** @see #detects_data_class */ @JsonProperty("detects_data_class")  public void setDetectsDataClass(Reference detects_data_class) { this.detects_data_class = detects_data_class; }

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


    public static final Boolean isFilter(Object obj) { return (obj.getClass() == Filter.class); }

}
