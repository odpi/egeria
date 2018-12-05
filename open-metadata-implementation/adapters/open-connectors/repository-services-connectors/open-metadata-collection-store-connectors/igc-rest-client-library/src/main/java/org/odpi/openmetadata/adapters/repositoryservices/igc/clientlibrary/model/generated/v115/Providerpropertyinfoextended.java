/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'providerpropertyinfoextended' asset type in IGC, displayed as 'ProviderPropertyInfoExtended' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Providerpropertyinfoextended extends MainObject {

    public static final String IGC_TYPE_ID = "providerpropertyinfoextended";

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


    /** @see #str_value */ @JsonProperty("str_value")  public String getStrValue() { return this.str_value; }
    /** @see #str_value */ @JsonProperty("str_value")  public void setStrValue(String str_value) { this.str_value = str_value; }

    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public Reference getOfProviderPropertyInfo() { return this.of_provider_property_info; }
    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public void setOfProviderPropertyInfo(Reference of_provider_property_info) { this.of_provider_property_info = of_provider_property_info; }


    public static final Boolean isProviderpropertyinfoextended(Object obj) { return (obj.getClass() == Providerpropertyinfoextended.class); }

}
