/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'transform_argument' asset type in IGC, displayed as 'Transform Argument' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransformArgument extends MainObject {

    public static final String IGC_TYPE_ID = "transform_argument";

    /**
     * The 'transform' property, displayed as 'Transform' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformsFunction} object.
     */
    protected Reference transform;

    /**
     * The 'data_elements' property, displayed as 'Data Elements' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataElement} object.
     */
    protected Reference data_elements;


    /** @see #transform */ @JsonProperty("transform")  public Reference getTransform() { return this.transform; }
    /** @see #transform */ @JsonProperty("transform")  public void setTransform(Reference transform) { this.transform = transform; }

    /** @see #data_elements */ @JsonProperty("data_elements")  public Reference getDataElements() { return this.data_elements; }
    /** @see #data_elements */ @JsonProperty("data_elements")  public void setDataElements(Reference data_elements) { this.data_elements = data_elements; }


    public static final Boolean isTransformArgument(Object obj) { return (obj.getClass() == TransformArgument.class); }

}
