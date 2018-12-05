/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'bi_collection_fact' asset type in IGC, displayed as 'BI Collection Fact' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiCollectionFact extends MainObject {

    public static final String IGC_TYPE_ID = "bi_collection_fact";

    /**
     * The 'bi_cube' property, displayed as 'BI Cube' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCube} object.
     */
    protected Reference bi_cube;

    /**
     * The 'references_bi_member' property, displayed as 'References BI Member' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollectionMember} object.
     */
    protected Reference references_bi_member;

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

    /** @see #references_bi_member */ @JsonProperty("references_bi_member")  public Reference getReferencesBiMember() { return this.references_bi_member; }
    /** @see #references_bi_member */ @JsonProperty("references_bi_member")  public void setReferencesBiMember(Reference references_bi_member) { this.references_bi_member = references_bi_member; }

    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isBiCollectionFact(Object obj) { return (obj.getClass() == BiCollectionFact.class); }

}
