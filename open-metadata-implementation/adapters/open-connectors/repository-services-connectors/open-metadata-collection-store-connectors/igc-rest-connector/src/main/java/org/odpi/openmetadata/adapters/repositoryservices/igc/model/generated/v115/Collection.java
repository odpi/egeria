/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'collection' asset type in IGC, displayed as 'Collection' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Collection extends MainObject {

    public static final String IGC_TYPE_ID = "collection";

    /**
     * The 'owners' property, displayed as 'Owners' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList owners;

    /**
     * The 'visibility' property, displayed as 'Visibility' in the IGC UI.
     */
    protected Boolean visibility;

    /**
     * The 'viewers' property, displayed as 'Viewers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList viewers;

    /**
     * The 'assets' property, displayed as 'Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList assets;

    /**
     * The 'references_assets' property, displayed as 'Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList references_assets;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #owners */ @JsonProperty("owners")  public ReferenceList getOwners() { return this.owners; }
    /** @see #owners */ @JsonProperty("owners")  public void setOwners(ReferenceList owners) { this.owners = owners; }

    /** @see #visibility */ @JsonProperty("visibility")  public Boolean getVisibility() { return this.visibility; }
    /** @see #visibility */ @JsonProperty("visibility")  public void setVisibility(Boolean visibility) { this.visibility = visibility; }

    /** @see #viewers */ @JsonProperty("viewers")  public ReferenceList getViewers() { return this.viewers; }
    /** @see #viewers */ @JsonProperty("viewers")  public void setViewers(ReferenceList viewers) { this.viewers = viewers; }

    /** @see #assets */ @JsonProperty("assets")  public ReferenceList getAssets() { return this.assets; }
    /** @see #assets */ @JsonProperty("assets")  public void setAssets(ReferenceList assets) { this.assets = assets; }

    /** @see #references_assets */ @JsonProperty("references_assets")  public ReferenceList getReferencesAssets() { return this.references_assets; }
    /** @see #references_assets */ @JsonProperty("references_assets")  public void setReferencesAssets(ReferenceList references_assets) { this.references_assets = references_assets; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isCollection(Object obj) { return (obj.getClass() == Collection.class); }

}
