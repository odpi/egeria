/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'filterconstraint' asset type in IGC, displayed as 'FilterConstraint' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Filterconstraint extends MainObject {

    public static final String IGC_TYPE_ID = "filterconstraint";

    /**
     * The 'of_link' property, displayed as 'Of Link' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_link;

    /**
     * The 'has_function_call' property, displayed as 'Has Function Call' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link FunctionCall2} objects.
     */
    protected ReferenceList has_function_call;

    /**
     * The 'uses_flow_variable' property, displayed as 'Uses Flow Variable' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList uses_flow_variable;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>IN (displayed in the UI as 'IN')</li>
     *     <li>OUT (displayed in the UI as 'OUT')</li>
     *     <li>INOUT (displayed in the UI as 'INOUT')</li>
     *     <li>LINK (displayed in the UI as 'LINK')</li>
     * </ul>
     */
    protected String usage;

    /**
     * The 'filter_expression' property, displayed as 'Filter Expression' in the IGC UI.
     */
    protected String filter_expression;


    /** @see #of_link */ @JsonProperty("of_link")  public Reference getOfLink() { return this.of_link; }
    /** @see #of_link */ @JsonProperty("of_link")  public void setOfLink(Reference of_link) { this.of_link = of_link; }

    /** @see #has_function_call */ @JsonProperty("has_function_call")  public ReferenceList getHasFunctionCall() { return this.has_function_call; }
    /** @see #has_function_call */ @JsonProperty("has_function_call")  public void setHasFunctionCall(ReferenceList has_function_call) { this.has_function_call = has_function_call; }

    /** @see #uses_flow_variable */ @JsonProperty("uses_flow_variable")  public ReferenceList getUsesFlowVariable() { return this.uses_flow_variable; }
    /** @see #uses_flow_variable */ @JsonProperty("uses_flow_variable")  public void setUsesFlowVariable(ReferenceList uses_flow_variable) { this.uses_flow_variable = uses_flow_variable; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #filter_expression */ @JsonProperty("filter_expression")  public String getFilterExpression() { return this.filter_expression; }
    /** @see #filter_expression */ @JsonProperty("filter_expression")  public void setFilterExpression(String filter_expression) { this.filter_expression = filter_expression; }


    public static final Boolean isFilterconstraint(Object obj) { return (obj.getClass() == Filterconstraint.class); }

}
