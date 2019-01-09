/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'information_services_argument' asset type in IGC, displayed as 'Information Services Argument' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesArgument extends Reference {

    public static String getIgcTypeId() { return "information_services_argument"; }
    public static String getIgcTypeDisplayName() { return "Information Services Argument"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

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


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

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
        "name",
        "description",
        "argument_name",
        "argument_type",
        "reference_type",
        "default_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "information_services_operation"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "information_services_operation",
        "argument_name",
        "argument_type",
        "reference_type",
        "default_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInformationServicesArgument(Object obj) { return (obj.getClass() == InformationServicesArgument.class); }

}
