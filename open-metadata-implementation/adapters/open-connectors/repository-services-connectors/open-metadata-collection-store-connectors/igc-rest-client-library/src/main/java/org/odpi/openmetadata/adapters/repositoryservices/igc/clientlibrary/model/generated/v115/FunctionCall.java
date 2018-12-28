/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'function_call' asset type in IGC, displayed as 'Function Call' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionCall extends Reference {

    public static String getIgcTypeId() { return "function_call"; }
    public static String getIgcTypeDisplayName() { return "Function Call"; }

    /**
     * The 'function_name' property, displayed as 'Function Name' in the IGC UI.
     */
    protected String function_name;

    /**
     * The 'for_transforms' property, displayed as 'For Transforms' in the IGC UI.
     */
    protected Boolean for_transforms;

    /**
     * The 'stage_trigger' property, displayed as 'Stage Trigger' in the IGC UI.
     */
    protected Boolean stage_trigger;

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


    /** @see #function_name */ @JsonProperty("function_name")  public String getFunctionName() { return this.function_name; }
    /** @see #function_name */ @JsonProperty("function_name")  public void setFunctionName(String function_name) { this.function_name = function_name; }

    /** @see #for_transforms */ @JsonProperty("for_transforms")  public Boolean getForTransforms() { return this.for_transforms; }
    /** @see #for_transforms */ @JsonProperty("for_transforms")  public void setForTransforms(Boolean for_transforms) { this.for_transforms = for_transforms; }

    /** @see #stage_trigger */ @JsonProperty("stage_trigger")  public Boolean getStageTrigger() { return this.stage_trigger; }
    /** @see #stage_trigger */ @JsonProperty("stage_trigger")  public void setStageTrigger(Boolean stage_trigger) { this.stage_trigger = stage_trigger; }

    /** @see #call_context */ @JsonProperty("call_context")  public String getCallContext() { return this.call_context; }
    /** @see #call_context */ @JsonProperty("call_context")  public void setCallContext(String call_context) { this.call_context = call_context; }

    /** @see #executes_function */ @JsonProperty("executes_function")  public Reference getExecutesFunction() { return this.executes_function; }
    /** @see #executes_function */ @JsonProperty("executes_function")  public void setExecutesFunction(Reference executes_function) { this.executes_function = executes_function; }

    /** @see #used_in_function */ @JsonProperty("used_in_function")  public Reference getUsedInFunction() { return this.used_in_function; }
    /** @see #used_in_function */ @JsonProperty("used_in_function")  public void setUsedInFunction(Reference used_in_function) { this.used_in_function = used_in_function; }

    /** @see #used_in_filter_constraint */ @JsonProperty("used_in_filter_constraint")  public Reference getUsedInFilterConstraint() { return this.used_in_filter_constraint; }
    /** @see #used_in_filter_constraint */ @JsonProperty("used_in_filter_constraint")  public void setUsedInFilterConstraint(Reference used_in_filter_constraint) { this.used_in_filter_constraint = used_in_filter_constraint; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("function_name");
        add("for_transforms");
        add("stage_trigger");
        add("call_context");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("function_name");
        add("for_transforms");
        add("stage_trigger");
        add("call_context");
        add("executes_function");
        add("used_in_function");
        add("used_in_filter_constraint");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isFunctionCall(Object obj) { return (obj.getClass() == FunctionCall.class); }

}
