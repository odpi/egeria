/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_services_argument' asset type in IGC, displayed as 'Information Services Argument' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesArgument extends MainObject {

    public static final String IGC_TYPE_ID = "information_services_argument";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'information_services_operation' property, displayed as 'Information Services Operation' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesOperation} objects.
     */
    protected ReferenceList information_services_operation;

    /**
     * The 'argument_name' property, displayed as 'Argument Name' in the IGC UI.
     */
    protected ArrayList<String> argument_name;

    /**
     * The 'argument_type' property, displayed as 'Argument Type' in the IGC UI.
     */
    protected ArrayList<String> argument_type;

    /**
     * The 'reference_type' property, displayed as 'Reference Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>BIGDECIMAL (displayed in the UI as 'BIGDECIMAL')</li>
     *     <li>BIGINTEGER (displayed in the UI as 'BIGINTEGER')</li>
     *     <li>BOOLEAN (displayed in the UI as 'BOOLEAN')</li>
     *     <li>BYTE (displayed in the UI as 'BYTE')</li>
     *     <li>BYTEARRAY (displayed in the UI as 'BYTEARRAY')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>DOUBLE (displayed in the UI as 'DOUBLE')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>INT (displayed in the UI as 'INT')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>TIMESTAMP (displayed in the UI as 'TIMESTAMP')</li>
     *     <li>ENCRYPTED (displayed in the UI as 'ENCRYPTED')</li>
     *     <li>XML (displayed in the UI as 'XML')</li>
     * </ul>
     */
    protected String reference_type;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #information_services_operation */ @JsonProperty("information_services_operation")  public ReferenceList getInformationServicesOperation() { return this.information_services_operation; }
    /** @see #information_services_operation */ @JsonProperty("information_services_operation")  public void setInformationServicesOperation(ReferenceList information_services_operation) { this.information_services_operation = information_services_operation; }

    /** @see #argument_name */ @JsonProperty("argument_name")  public ArrayList<String> getArgumentName() { return this.argument_name; }
    /** @see #argument_name */ @JsonProperty("argument_name")  public void setArgumentName(ArrayList<String> argument_name) { this.argument_name = argument_name; }

    /** @see #argument_type */ @JsonProperty("argument_type")  public ArrayList<String> getArgumentType() { return this.argument_type; }
    /** @see #argument_type */ @JsonProperty("argument_type")  public void setArgumentType(ArrayList<String> argument_type) { this.argument_type = argument_type; }

    /** @see #reference_type */ @JsonProperty("reference_type")  public String getReferenceType() { return this.reference_type; }
    /** @see #reference_type */ @JsonProperty("reference_type")  public void setReferenceType(String reference_type) { this.reference_type = reference_type; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }


    public static final Boolean isInformationServicesArgument(Object obj) { return (obj.getClass() == InformationServicesArgument.class); }

}
