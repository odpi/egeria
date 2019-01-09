/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The payload contents of all asset-specific events on the InfosphereEvents topic.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsAssetEvent extends InfosphereEvents {

    @JsonIgnore public static final String ACTION_CREATE = "CREATE";
    @JsonIgnore public static final String ACTION_MODIFY = "MODIFY";
    @JsonIgnore public static final String ACTION_DELETE = "DELETE";
    @JsonIgnore public static final String ACTION_ASSIGNED_RELATIONSHIP = "ASSIGNED_RELATIONSHIP";

    /**
     * The 'ASSET_TYPE' property of an event gives the display name of the asset type as known within IGC.
     */
    protected String ASSET_TYPE;

    /**
     * The 'ASSET_RID' property of an event gives the IGC Repository ID (RID) unique id of the asset within IGC.
     */
    protected String ASSET_RID;

    /**
     * The 'ASSET_CONTEXT' property of an event gives the identity of the asset in question, without relying on IDs.
     */
    protected String ASSET_CONTEXT;

    /**
     * The 'ACTION' property of an event gives the type of action that was taken that this event represents.
     * For example 'CREATE' or 'ASSIGNED_RELATIONSHIP'.
     */
    protected String ACTION;

    /**
     * The 'ASSET_NAME' property of an event gives the name (non-unique) of the asset that was involved.
     */
    protected String ASSET_NAME;

    /** @see #ASSET_TYPE */ @JsonProperty("ASSET_TYPE") public String getAssetType() { return this.ASSET_TYPE; }
    /** @see #ASSET_TYPE */ @JsonProperty("ASSET_TYPE") public void setAssetType(String ASSET_TYPE) { this.ASSET_TYPE = ASSET_TYPE; }

    /** @see #ASSET_RID */ @JsonProperty("ASSET_RID") public String getAssetRid() { return this.ASSET_RID; }
    /** @see #ASSET_RID */ @JsonProperty("ASSET_RID") public void setAssetRid(String ASSET_RID) { this.ASSET_RID = ASSET_RID; }

    /** @see #ASSET_CONTEXT */ @JsonProperty("ASSET_CONTEXT") public String getAssetContext() { return this.ASSET_CONTEXT; }
    /** @see #ASSET_CONTEXT */ @JsonProperty("ASSET_CONTEXT") public void setAssetContext(String ASSET_CONTEXT) { this.ASSET_CONTEXT = ASSET_CONTEXT; }

    /** @see #ACTION */ @JsonProperty("ACTION") public String getAction() { return this.ACTION; }
    /** @see #ACTION */ @JsonProperty("ACTION") public void setAction(String ACTION) { this.ACTION = ACTION; }

    /** @see #ASSET_NAME */ @JsonProperty("ASSET_NAME") public String getAssetName() { return this.ASSET_NAME; }
    /** @see #ASSET_NAME */ @JsonProperty("ASSET_NAME") public void setAssetName(String ASSET_NAME) { this.ASSET_NAME = ASSET_NAME; }

}
