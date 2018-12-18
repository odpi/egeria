/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'providerpropertyinfoextended' asset type in IGC, displayed as 'ProviderPropertyInfoExtended' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Providerpropertyinfoextended extends Reference {

    public static String getIgcTypeId() { return "providerpropertyinfoextended"; }

    /**
     * The 'str_value' property, displayed as 'Str Value' in the IGC UI.
     */
    protected String str_value;

    /**
     * The 'of_provider_property_info' property, displayed as 'Of Provider Property Info' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Providerpropertyinfo} object.
     */
    protected Reference of_provider_property_info;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

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


    /** @see #str_value */ @JsonProperty("str_value")  public String getStrValue() { return this.str_value; }
    /** @see #str_value */ @JsonProperty("str_value")  public void setStrValue(String str_value) { this.str_value = str_value; }

    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public Reference getOfProviderPropertyInfo() { return this.of_provider_property_info; }
    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public void setOfProviderPropertyInfo(Reference of_provider_property_info) { this.of_provider_property_info = of_provider_property_info; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean includesModificationDetails() { return true; }
    public static final Boolean isProviderpropertyinfoextended(Object obj) { return (obj.getClass() == Providerpropertyinfoextended.class); }

}
