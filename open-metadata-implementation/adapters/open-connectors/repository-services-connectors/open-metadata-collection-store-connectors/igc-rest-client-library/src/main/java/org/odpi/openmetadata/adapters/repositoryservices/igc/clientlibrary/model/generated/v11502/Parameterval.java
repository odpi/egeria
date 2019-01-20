/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'parameterval' asset type in IGC, displayed as 'ParameterVal' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Parameterval extends Reference {

    public static String getIgcTypeId() { return "parameterval"; }
    public static String getIgcTypeDisplayName() { return "ParameterVal"; }

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

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "parameter_name",
        "value_expression",
        "usage",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "parameter_name",
        "value_expression",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_function_call"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "parameter_name",
        "value_expression",
        "binds_parameter_def",
        "has_function_call",
        "for_function_call",
        "for_job_object",
        "usage",
        "for_data_connection",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isParameterval(Object obj) { return (obj.getClass() == Parameterval.class); }

}
