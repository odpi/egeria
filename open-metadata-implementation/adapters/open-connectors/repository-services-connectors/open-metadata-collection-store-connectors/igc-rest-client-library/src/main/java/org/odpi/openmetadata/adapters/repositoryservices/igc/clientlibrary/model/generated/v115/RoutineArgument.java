/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'routine_argument' asset type in IGC, displayed as 'Routine Argument' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RoutineArgument extends MainObject {

    public static final String IGC_TYPE_ID = "routine_argument";

    /**
     * The 'function' property, displayed as 'Function' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Routine} object.
     */
    protected Reference function;


    /** @see #function */ @JsonProperty("function")  public Reference getFunction() { return this.function; }
    /** @see #function */ @JsonProperty("function")  public void setFunction(Reference function) { this.function = function; }


    public static final Boolean isRoutineArgument(Object obj) { return (obj.getClass() == RoutineArgument.class); }

}
