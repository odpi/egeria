/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.InformationAsset;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'logical_variable' asset type in IGC, displayed as 'Logical Variable' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalVariable extends MainObject {

    public static final String IGC_TYPE_ID = "logical_variable";

    /**
     * The 'short_&amp;_long_description' property, displayed as 'Short &amp; Long Description' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CANDIDATE (displayed in the UI as 'CANDIDATE')</li>
     *     <li>ACCEPTED (displayed in the UI as 'ACCEPTED')</li>
     *     <li>STANDARD (displayed in the UI as 'STANDARD')</li>
     *     <li>DEPRECATED (displayed in the UI as 'DEPRECATED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>IN_PROCESS (displayed in the UI as 'IN_PROCESS')</li>
     *     <li>REJECTED (displayed in the UI as 'REJECTED')</li>
     *     <li>ERROR (displayed in the UI as 'ERROR')</li>
     * </ul>
     */
    protected String status;

    /**
     * The 'binding' property, displayed as 'Binding' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList binding;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INTEGER (displayed in the UI as 'INTEGER')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>NUMBER (displayed in the UI as 'NUMBER')</li>
     *     <li>FIXED_STRING (displayed in the UI as 'FIXED_STRING')</li>
     *     <li>VARIABLE_STRING (displayed in the UI as 'VARIABLE_STRING')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>ANY (displayed in the UI as 'ANY')</li>
     * </ul>
     */
    protected String data_type;

    /**
     * The 'type' property, displayed as 'Example' in the IGC UI.
     */
    protected String type;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #binding */ @JsonProperty("binding")  public ReferenceList getBinding() { return this.binding; }
    /** @see #binding */ @JsonProperty("binding")  public void setBinding(ReferenceList binding) { this.binding = binding; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }


    public static final Boolean isLogicalVariable(Object obj) { return (obj.getClass() == LogicalVariable.class); }

}
