/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'tuple' asset type in IGC, displayed as 'Tuple' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tuple extends MainObject {

    public static final String IGC_TYPE_ID = "tuple";

    /**
     * The 'tuple_attributes' property, displayed as 'Tuple Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TupleAttribute} objects.
     */
    protected ReferenceList tuple_attributes;


    /** @see #tuple_attributes */ @JsonProperty("tuple_attributes")  public ReferenceList getTupleAttributes() { return this.tuple_attributes; }
    /** @see #tuple_attributes */ @JsonProperty("tuple_attributes")  public void setTupleAttributes(ReferenceList tuple_attributes) { this.tuple_attributes = tuple_attributes; }


    public static final Boolean isTuple(Object obj) { return (obj.getClass() == Tuple.class); }

}
