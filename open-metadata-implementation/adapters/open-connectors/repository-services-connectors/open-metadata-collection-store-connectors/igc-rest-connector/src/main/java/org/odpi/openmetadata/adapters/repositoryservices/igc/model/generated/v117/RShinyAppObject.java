/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'r_shiny_app_object' asset type in IGC, displayed as 'RShiny App Object' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RShinyAppObject extends MainObject {

    public static final String IGC_TYPE_ID = "r_shiny_app_object";

    /**
     * The 'from_r_shiny_app' property, displayed as 'RShiny App' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link RShinyApp} object.
     */
    protected Reference from_r_shiny_app;

    /**
     * The 'system_equiv_id' property, displayed as 'System Equiv Id' in the IGC UI.
     */
    protected String system_equiv_id;

    /**
     * The 'studio_asset_type' property, displayed as 'Studio Asset Type' in the IGC UI.
     */
    protected String studio_asset_type;

    /**
     * The 'path' property, displayed as 'Path' in the IGC UI.
     */
    protected String path;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #from_r_shiny_app */ @JsonProperty("from_r_shiny_app")  public Reference getFromRShinyApp() { return this.from_r_shiny_app; }
    /** @see #from_r_shiny_app */ @JsonProperty("from_r_shiny_app")  public void setFromRShinyApp(Reference from_r_shiny_app) { this.from_r_shiny_app = from_r_shiny_app; }

    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public String getSystemEquivId() { return this.system_equiv_id; }
    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public void setSystemEquivId(String system_equiv_id) { this.system_equiv_id = system_equiv_id; }

    /** @see #studio_asset_type */ @JsonProperty("studio_asset_type")  public String getStudioAssetType() { return this.studio_asset_type; }
    /** @see #studio_asset_type */ @JsonProperty("studio_asset_type")  public void setStudioAssetType(String studio_asset_type) { this.studio_asset_type = studio_asset_type; }

    /** @see #path */ @JsonProperty("path")  public String getPath() { return this.path; }
    /** @see #path */ @JsonProperty("path")  public void setPath(String path) { this.path = path; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isRShinyAppObject(Object obj) { return (obj.getClass() == RShinyAppObject.class); }

}
