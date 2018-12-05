/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'lineage_container' asset type in IGC, displayed as 'Business Lineage Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LineageContainer extends MainObject {

    public static final String IGC_TYPE_ID = "lineage_container";

    /**
     * The 'parent_container' property, displayed as 'Parent Container' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LineageContainer} object.
     */
    protected Reference parent_container;

    /**
     * The 'sub_business_container' property, displayed as 'Sub Business Container' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Category} objects.
     */
    protected ReferenceList sub_business_container;

    /**
     * The 'information_assets' property, displayed as 'Contains Information Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList information_assets;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #parent_container */ @JsonProperty("parent_container")  public Reference getParentContainer() { return this.parent_container; }
    /** @see #parent_container */ @JsonProperty("parent_container")  public void setParentContainer(Reference parent_container) { this.parent_container = parent_container; }

    /** @see #sub_business_container */ @JsonProperty("sub_business_container")  public ReferenceList getSubBusinessContainer() { return this.sub_business_container; }
    /** @see #sub_business_container */ @JsonProperty("sub_business_container")  public void setSubBusinessContainer(ReferenceList sub_business_container) { this.sub_business_container = sub_business_container; }

    /** @see #information_assets */ @JsonProperty("information_assets")  public ReferenceList getInformationAssets() { return this.information_assets; }
    /** @see #information_assets */ @JsonProperty("information_assets")  public void setInformationAssets(ReferenceList information_assets) { this.information_assets = information_assets; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isLineageContainer(Object obj) { return (obj.getClass() == LineageContainer.class); }

}
