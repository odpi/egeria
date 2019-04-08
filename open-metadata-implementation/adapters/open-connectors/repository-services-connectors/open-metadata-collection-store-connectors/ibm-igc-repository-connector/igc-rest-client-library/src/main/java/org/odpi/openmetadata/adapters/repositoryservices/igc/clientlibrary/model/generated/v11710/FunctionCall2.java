/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code functioncall} asset type in IGC, displayed as '{@literal FunctionCall}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionCall2 extends Reference {

    public static String getIgcTypeId() { return "functioncall"; }
    public static String getIgcTypeDisplayName() { return "FunctionCall"; }

    /**
     * The {@code call_context} property, displayed as '{@literal Call Context}' in the IGC UI.
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
     * The {@code executes_function} property, displayed as '{@literal Executes Function}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference executes_function;

    /**
     * The {@code used_in_function} property, displayed as '{@literal Used In Function}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference used_in_function;

    /**
     * The {@code used_in_filter_constraint} property, displayed as '{@literal Used In Filter Constraint}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "call_context"
    );
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "call_context",
        "executes_function",
        "used_in_function",
        "used_in_filter_constraint"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isFunctionCall2(Object obj) { return (obj.getClass() == FunctionCall2.class); }

}
