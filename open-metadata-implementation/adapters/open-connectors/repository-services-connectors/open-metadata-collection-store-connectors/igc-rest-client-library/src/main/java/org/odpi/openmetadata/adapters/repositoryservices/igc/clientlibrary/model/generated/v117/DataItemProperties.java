/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'data_item_properties' asset type in IGC, displayed as 'Data Item Properties' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataItemProperties extends MainObject {

    public static final String IGC_TYPE_ID = "data_item_properties";

    /**
     * The 'belonging_to_parameter_definition' property, displayed as 'Belonging to Parameter Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Parameter} object.
     */
    protected Reference belonging_to_parameter_definition;

    /**
     * The 'flow_variable' property, displayed as 'Flow Variable' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItem} object.
     */
    protected Reference flow_variable;

    /**
     * The 'column_definition' property, displayed as 'Column Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ColumnDefinition} object.
     */
    protected Reference column_definition;

    /**
     * The 'filler_parents' property, displayed as 'Filler Parents' in the IGC UI.
     */
    protected String filler_parents;

    /**
     * The 'nls_map' property, displayed as 'NLS Map' in the IGC UI.
     */
    protected String nls_map;

    /**
     * The 'sync_indicator' property, displayed as 'Sync Indicator' in the IGC UI.
     */
    protected Boolean sync_indicator;

    /**
     * The 'redefined_field' property, displayed as 'Redefined Field' in the IGC UI.
     */
    protected String redefined_field;

    /**
     * The 'association' property, displayed as 'Association' in the IGC UI.
     */
    protected String association;

    /**
     * The 'depend_field' property, displayed as 'Depend Field' in the IGC UI.
     */
    protected String depend_field;

    /**
     * The 'scd_purpose' property, displayed as 'SCD Purpose' in the IGC UI.
     */
    protected Number scd_purpose;

    /**
     * The 'field_type' property, displayed as 'Field Type' in the IGC UI.
     */
    protected String field_type;

    /**
     * The 'date_mask' property, displayed as 'Date Mask' in the IGC UI.
     */
    protected String date_mask;

    /**
     * The 'apt_field_prop' property, displayed as 'APT Field Prop' in the IGC UI.
     */
    protected String apt_field_prop;

    /**
     * The 'has_sign_indicator' property, displayed as 'Has Sign Indicator' in the IGC UI.
     */
    protected Boolean has_sign_indicator;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     */
    protected String usage;

    /**
     * The 'scale' property, displayed as 'Scale' in the IGC UI.
     */
    protected Number scale;

    /**
     * The 'is_u_string' property, displayed as 'Is U String' in the IGC UI.
     */
    protected Boolean is_u_string;

    /**
     * The 'sign_option' property, displayed as 'Sign Option' in the IGC UI.
     */
    protected Number sign_option;

    /**
     * The 'pad_char' property, displayed as 'Pad Char' in the IGC UI.
     */
    protected String pad_char;


    /** @see #belonging_to_parameter_definition */ @JsonProperty("belonging_to_parameter_definition")  public Reference getBelongingToParameterDefinition() { return this.belonging_to_parameter_definition; }
    /** @see #belonging_to_parameter_definition */ @JsonProperty("belonging_to_parameter_definition")  public void setBelongingToParameterDefinition(Reference belonging_to_parameter_definition) { this.belonging_to_parameter_definition = belonging_to_parameter_definition; }

    /** @see #flow_variable */ @JsonProperty("flow_variable")  public Reference getFlowVariable() { return this.flow_variable; }
    /** @see #flow_variable */ @JsonProperty("flow_variable")  public void setFlowVariable(Reference flow_variable) { this.flow_variable = flow_variable; }

    /** @see #column_definition */ @JsonProperty("column_definition")  public Reference getColumnDefinition() { return this.column_definition; }
    /** @see #column_definition */ @JsonProperty("column_definition")  public void setColumnDefinition(Reference column_definition) { this.column_definition = column_definition; }

    /** @see #filler_parents */ @JsonProperty("filler_parents")  public String getFillerParents() { return this.filler_parents; }
    /** @see #filler_parents */ @JsonProperty("filler_parents")  public void setFillerParents(String filler_parents) { this.filler_parents = filler_parents; }

    /** @see #nls_map */ @JsonProperty("nls_map")  public String getNlsMap() { return this.nls_map; }
    /** @see #nls_map */ @JsonProperty("nls_map")  public void setNlsMap(String nls_map) { this.nls_map = nls_map; }

    /** @see #sync_indicator */ @JsonProperty("sync_indicator")  public Boolean getSyncIndicator() { return this.sync_indicator; }
    /** @see #sync_indicator */ @JsonProperty("sync_indicator")  public void setSyncIndicator(Boolean sync_indicator) { this.sync_indicator = sync_indicator; }

    /** @see #redefined_field */ @JsonProperty("redefined_field")  public String getRedefinedField() { return this.redefined_field; }
    /** @see #redefined_field */ @JsonProperty("redefined_field")  public void setRedefinedField(String redefined_field) { this.redefined_field = redefined_field; }

    /** @see #association */ @JsonProperty("association")  public String getAssociation() { return this.association; }
    /** @see #association */ @JsonProperty("association")  public void setAssociation(String association) { this.association = association; }

    /** @see #depend_field */ @JsonProperty("depend_field")  public String getDependField() { return this.depend_field; }
    /** @see #depend_field */ @JsonProperty("depend_field")  public void setDependField(String depend_field) { this.depend_field = depend_field; }

    /** @see #scd_purpose */ @JsonProperty("scd_purpose")  public Number getScdPurpose() { return this.scd_purpose; }
    /** @see #scd_purpose */ @JsonProperty("scd_purpose")  public void setScdPurpose(Number scd_purpose) { this.scd_purpose = scd_purpose; }

    /** @see #field_type */ @JsonProperty("field_type")  public String getFieldType() { return this.field_type; }
    /** @see #field_type */ @JsonProperty("field_type")  public void setFieldType(String field_type) { this.field_type = field_type; }

    /** @see #date_mask */ @JsonProperty("date_mask")  public String getDateMask() { return this.date_mask; }
    /** @see #date_mask */ @JsonProperty("date_mask")  public void setDateMask(String date_mask) { this.date_mask = date_mask; }

    /** @see #apt_field_prop */ @JsonProperty("apt_field_prop")  public String getAptFieldProp() { return this.apt_field_prop; }
    /** @see #apt_field_prop */ @JsonProperty("apt_field_prop")  public void setAptFieldProp(String apt_field_prop) { this.apt_field_prop = apt_field_prop; }

    /** @see #has_sign_indicator */ @JsonProperty("has_sign_indicator")  public Boolean getHasSignIndicator() { return this.has_sign_indicator; }
    /** @see #has_sign_indicator */ @JsonProperty("has_sign_indicator")  public void setHasSignIndicator(Boolean has_sign_indicator) { this.has_sign_indicator = has_sign_indicator; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #scale */ @JsonProperty("scale")  public Number getScale() { return this.scale; }
    /** @see #scale */ @JsonProperty("scale")  public void setScale(Number scale) { this.scale = scale; }

    /** @see #is_u_string */ @JsonProperty("is_u_string")  public Boolean getIsUString() { return this.is_u_string; }
    /** @see #is_u_string */ @JsonProperty("is_u_string")  public void setIsUString(Boolean is_u_string) { this.is_u_string = is_u_string; }

    /** @see #sign_option */ @JsonProperty("sign_option")  public Number getSignOption() { return this.sign_option; }
    /** @see #sign_option */ @JsonProperty("sign_option")  public void setSignOption(Number sign_option) { this.sign_option = sign_option; }

    /** @see #pad_char */ @JsonProperty("pad_char")  public String getPadChar() { return this.pad_char; }
    /** @see #pad_char */ @JsonProperty("pad_char")  public void setPadChar(String pad_char) { this.pad_char = pad_char; }


    public static final Boolean isDataItemProperties(Object obj) { return (obj.getClass() == DataItemProperties.class); }

}
