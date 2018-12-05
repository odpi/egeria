/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'root_tuple' asset type in IGC, displayed as 'Tuple' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RootTuple extends MainObject {

    public static final String IGC_TYPE_ID = "root_tuple";

    /**
     * The 'endpoint' property, displayed as 'Endpoint' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Endpoint} object.
     */
    protected Reference endpoint;


    /** @see #endpoint */ @JsonProperty("endpoint")  public Reference getEndpoint() { return this.endpoint; }
    /** @see #endpoint */ @JsonProperty("endpoint")  public void setEndpoint(Reference endpoint) { this.endpoint = endpoint; }


    public static final Boolean isRootTuple(Object obj) { return (obj.getClass() == RootTuple.class); }

}
