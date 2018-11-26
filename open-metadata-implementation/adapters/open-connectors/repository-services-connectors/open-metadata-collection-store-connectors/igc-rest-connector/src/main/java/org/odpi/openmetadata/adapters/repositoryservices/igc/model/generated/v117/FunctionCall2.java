/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'functioncall' asset type in IGC, displayed as 'FunctionCall' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionCall2 extends MainObject {

    public static final String IGC_TYPE_ID = "functioncall";

    /**
     * The 'call_context' property, displayed as 'Call Context' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>BEFORE (displayed in the UI as 'BEFORE')</li>
     *     <li>AFTER (displayed in the UI as 'AFTER')</li>
     *     <li>DEFAULT (displayed in the UI as 'DEFAULT')</li>
     * </ul>
     */
    protected String call_context;

    /**
     * The 'executes_function' property, displayed as 'Executes Function' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference executes_function;

    /**
     * The 'used_in_function' property, displayed as 'Used In Function' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference used_in_function;

    /**
     * The 'used_in_filter_constraint' property, displayed as 'Used In Filter Constraint' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Filterconstraint} object.
     */
    protected Reference used_in_filter_constraint;


    /** @see #call_context */ @JsonProperty("call_context")  public String getCallContext() { return this.call_context; }
    /** @see #call_context */ @JsonProperty("call_context")  public void setCallContext(String call_context) { this.call_context = call_context; }

    /** @see #executes_function */ @JsonProperty("executes_function")  public Reference getExecutesFunction() { return this.executes_function; }
    /** @see #executes_function */ @JsonProperty("executes_function")  public void setExecutesFunction(Reference executes_function) { this.executes_function = executes_function; }

    /** @see #used_in_function */ @JsonProperty("used_in_function")  public Reference getUsedInFunction() { return this.used_in_function; }
    /** @see #used_in_function */ @JsonProperty("used_in_function")  public void setUsedInFunction(Reference used_in_function) { this.used_in_function = used_in_function; }

    /** @see #used_in_filter_constraint */ @JsonProperty("used_in_filter_constraint")  public Reference getUsedInFilterConstraint() { return this.used_in_filter_constraint; }
    /** @see #used_in_filter_constraint */ @JsonProperty("used_in_filter_constraint")  public void setUsedInFilterConstraint(Reference used_in_filter_constraint) { this.used_in_filter_constraint = used_in_filter_constraint; }


    public static final Boolean isFunctionCall2(Object obj) { return (obj.getClass() == FunctionCall2.class); }

}
