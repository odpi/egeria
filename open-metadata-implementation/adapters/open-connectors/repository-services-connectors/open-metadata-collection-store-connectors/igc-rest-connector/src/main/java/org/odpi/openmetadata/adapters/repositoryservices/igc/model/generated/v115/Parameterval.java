/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'parameterval' asset type in IGC, displayed as 'ParameterVal' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Parameterval extends MainObject {

    public static final String IGC_TYPE_ID = "parameterval";

    /**
     * The 'parameter_name' property, displayed as 'Parameter Name' in the IGC UI.
     */
    protected String parameter_name;

    /**
     * The 'value_expression' property, displayed as 'Value Expression' in the IGC UI.
     */
    protected String value_expression;

    /**
     * The 'binds_parameter_def' property, displayed as 'Binds Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Parameter} object.
     */
    protected Reference binds_parameter_def;

    /**
     * The 'has_function_call' property, displayed as 'Has Function Call' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link FunctionCall2} objects.
     */
    protected ReferenceList has_function_call;

    /**
     * The 'for_function_call' property, displayed as 'For Function Call' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link FunctionCall2} object.
     */
    protected Reference for_function_call;

    /**
     * The 'for_job_object' property, displayed as 'For Job Object' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference for_job_object;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DEFAULT (displayed in the UI as 'DEFAULT')</li>
     *     <li>IN (displayed in the UI as 'IN')</li>
     *     <li>OUT (displayed in the UI as 'OUT')</li>
     *     <li>INOUT (displayed in the UI as 'INOUT')</li>
     *     <li>RETURN (displayed in the UI as 'RETURN')</li>
     * </ul>
     */
    protected String usage;

    /**
     * The 'for_data_connection' property, displayed as 'For Data Connection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataConnection} object.
     */
    protected Reference for_data_connection;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #parameter_name */ @JsonProperty("parameter_name")  public String getParameterName() { return this.parameter_name; }
    /** @see #parameter_name */ @JsonProperty("parameter_name")  public void setParameterName(String parameter_name) { this.parameter_name = parameter_name; }

    /** @see #value_expression */ @JsonProperty("value_expression")  public String getValueExpression() { return this.value_expression; }
    /** @see #value_expression */ @JsonProperty("value_expression")  public void setValueExpression(String value_expression) { this.value_expression = value_expression; }

    /** @see #binds_parameter_def */ @JsonProperty("binds_parameter_def")  public Reference getBindsParameterDef() { return this.binds_parameter_def; }
    /** @see #binds_parameter_def */ @JsonProperty("binds_parameter_def")  public void setBindsParameterDef(Reference binds_parameter_def) { this.binds_parameter_def = binds_parameter_def; }

    /** @see #has_function_call */ @JsonProperty("has_function_call")  public ReferenceList getHasFunctionCall() { return this.has_function_call; }
    /** @see #has_function_call */ @JsonProperty("has_function_call")  public void setHasFunctionCall(ReferenceList has_function_call) { this.has_function_call = has_function_call; }

    /** @see #for_function_call */ @JsonProperty("for_function_call")  public Reference getForFunctionCall() { return this.for_function_call; }
    /** @see #for_function_call */ @JsonProperty("for_function_call")  public void setForFunctionCall(Reference for_function_call) { this.for_function_call = for_function_call; }

    /** @see #for_job_object */ @JsonProperty("for_job_object")  public Reference getForJobObject() { return this.for_job_object; }
    /** @see #for_job_object */ @JsonProperty("for_job_object")  public void setForJobObject(Reference for_job_object) { this.for_job_object = for_job_object; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #for_data_connection */ @JsonProperty("for_data_connection")  public Reference getForDataConnection() { return this.for_data_connection; }
    /** @see #for_data_connection */ @JsonProperty("for_data_connection")  public void setForDataConnection(Reference for_data_connection) { this.for_data_connection = for_data_connection; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isParameterval(Object obj) { return (obj.getClass() == Parameterval.class); }

}
