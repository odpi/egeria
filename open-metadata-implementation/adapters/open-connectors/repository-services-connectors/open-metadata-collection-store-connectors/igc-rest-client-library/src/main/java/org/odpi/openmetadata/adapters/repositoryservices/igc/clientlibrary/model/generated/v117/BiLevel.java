/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_level' asset type in IGC, displayed as 'BI Level' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiLevel extends MainObject {

    public static final String IGC_TYPE_ID = "bi_level";

    /**
     * The 'bi_collection' property, displayed as 'BI Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollection} object.
     */
    protected Reference bi_collection;

    /**
     * The 'parent_bi_level' property, displayed as 'Parent BI Level' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList parent_bi_level;

    /**
     * The 'children_levels' property, displayed as 'Children Levels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList children_levels;

    /**
     * The 'depth' property, displayed as 'Depth' in the IGC UI.
     */
    protected Number depth;

    /**
     * The 'bi_hierarchy' property, displayed as 'BI Hierarchy' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiHierarchy} objects.
     */
    protected ReferenceList bi_hierarchy;


    /** @see #bi_collection */ @JsonProperty("bi_collection")  public Reference getBiCollection() { return this.bi_collection; }
    /** @see #bi_collection */ @JsonProperty("bi_collection")  public void setBiCollection(Reference bi_collection) { this.bi_collection = bi_collection; }

    /** @see #parent_bi_level */ @JsonProperty("parent_bi_level")  public ReferenceList getParentBiLevel() { return this.parent_bi_level; }
    /** @see #parent_bi_level */ @JsonProperty("parent_bi_level")  public void setParentBiLevel(ReferenceList parent_bi_level) { this.parent_bi_level = parent_bi_level; }

    /** @see #children_levels */ @JsonProperty("children_levels")  public ReferenceList getChildrenLevels() { return this.children_levels; }
    /** @see #children_levels */ @JsonProperty("children_levels")  public void setChildrenLevels(ReferenceList children_levels) { this.children_levels = children_levels; }

    /** @see #depth */ @JsonProperty("depth")  public Number getDepth() { return this.depth; }
    /** @see #depth */ @JsonProperty("depth")  public void setDepth(Number depth) { this.depth = depth; }

    /** @see #bi_hierarchy */ @JsonProperty("bi_hierarchy")  public ReferenceList getBiHierarchy() { return this.bi_hierarchy; }
    /** @see #bi_hierarchy */ @JsonProperty("bi_hierarchy")  public void setBiHierarchy(ReferenceList bi_hierarchy) { this.bi_hierarchy = bi_hierarchy; }


    public static final Boolean isBiLevel(Object obj) { return (obj.getClass() == BiLevel.class); }

}
