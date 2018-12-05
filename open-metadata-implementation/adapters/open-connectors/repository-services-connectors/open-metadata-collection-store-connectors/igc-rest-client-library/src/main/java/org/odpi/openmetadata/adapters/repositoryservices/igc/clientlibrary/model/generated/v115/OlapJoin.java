/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'olap_join' asset type in IGC, displayed as 'OLAP Join' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class OlapJoin extends MainObject {

    public static final String IGC_TYPE_ID = "olap_join";

    /**
     * The 'business_name' property, displayed as 'Business Name' in the IGC UI.
     */
    protected String business_name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INNER (displayed in the UI as 'INNER')</li>
     *     <li>FULL_OUTER (displayed in the UI as 'FULL_OUTER')</li>
     *     <li>LEFT_OUTER (displayed in the UI as 'LEFT_OUTER')</li>
     *     <li>RIGHT_OUTER (displayed in the UI as 'RIGHT_OUTER')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'condition' property, displayed as 'Condition' in the IGC UI.
     */
    protected String condition;

    /**
     * The 'bi_model' property, displayed as 'OLAP Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The 'contains_references' property, displayed as 'Contains References' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Olapjoinref} objects.
     */
    protected ReferenceList contains_references;

    /**
     * The 'referenced_by_an_olap_cube' property, displayed as 'Referenced by an OLAP Cube' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCube} objects.
     */
    protected ReferenceList referenced_by_an_olap_cube;


    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #condition */ @JsonProperty("condition")  public String getCondition() { return this.condition; }
    /** @see #condition */ @JsonProperty("condition")  public void setCondition(String condition) { this.condition = condition; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #contains_references */ @JsonProperty("contains_references")  public ReferenceList getContainsReferences() { return this.contains_references; }
    /** @see #contains_references */ @JsonProperty("contains_references")  public void setContainsReferences(ReferenceList contains_references) { this.contains_references = contains_references; }

    /** @see #referenced_by_an_olap_cube */ @JsonProperty("referenced_by_an_olap_cube")  public ReferenceList getReferencedByAnOlapCube() { return this.referenced_by_an_olap_cube; }
    /** @see #referenced_by_an_olap_cube */ @JsonProperty("referenced_by_an_olap_cube")  public void setReferencedByAnOlapCube(ReferenceList referenced_by_an_olap_cube) { this.referenced_by_an_olap_cube = referenced_by_an_olap_cube; }


    public static final Boolean isOlapJoin(Object obj) { return (obj.getClass() == OlapJoin.class); }

}
