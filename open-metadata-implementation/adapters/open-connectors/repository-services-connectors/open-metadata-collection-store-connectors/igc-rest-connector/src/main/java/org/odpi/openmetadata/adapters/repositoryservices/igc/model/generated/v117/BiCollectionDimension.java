/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_collection_dimension' asset type in IGC, displayed as 'BI Collection Dimension' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiCollectionDimension extends MainObject {

    public static final String IGC_TYPE_ID = "bi_collection_dimension";

    /**
     * The 'bi_cube' property, displayed as 'BI Cube' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCube} object.
     */
    protected Reference bi_cube;

    /**
     * The 'references_bi_collection' property, displayed as 'References BI Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollection} object.
     */
    protected Reference references_bi_collection;

    /**
     * The 'uses_olap_hierarchy' property, displayed as 'Uses OLAP Hierarchy' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiHierarchy} object.
     */
    protected Reference uses_olap_hierarchy;

    /**
     * The 'business_name' property, displayed as 'Business Name' in the IGC UI.
     */
    protected String business_name;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #bi_cube */ @JsonProperty("bi_cube")  public Reference getBiCube() { return this.bi_cube; }
    /** @see #bi_cube */ @JsonProperty("bi_cube")  public void setBiCube(Reference bi_cube) { this.bi_cube = bi_cube; }

    /** @see #references_bi_collection */ @JsonProperty("references_bi_collection")  public Reference getReferencesBiCollection() { return this.references_bi_collection; }
    /** @see #references_bi_collection */ @JsonProperty("references_bi_collection")  public void setReferencesBiCollection(Reference references_bi_collection) { this.references_bi_collection = references_bi_collection; }

    /** @see #uses_olap_hierarchy */ @JsonProperty("uses_olap_hierarchy")  public Reference getUsesOlapHierarchy() { return this.uses_olap_hierarchy; }
    /** @see #uses_olap_hierarchy */ @JsonProperty("uses_olap_hierarchy")  public void setUsesOlapHierarchy(Reference uses_olap_hierarchy) { this.uses_olap_hierarchy = uses_olap_hierarchy; }

    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isBiCollectionDimension(Object obj) { return (obj.getClass() == BiCollectionDimension.class); }

}
