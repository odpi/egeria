/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'directoryproviderproperty' asset type in IGC, displayed as 'DirectoryProviderProperty' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Directoryproviderproperty extends MainObject {

    public static final String IGC_TYPE_ID = "directoryproviderproperty";

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected String value;

    /**
     * The 'of_provider_property_type' property, displayed as 'Of Provider Property Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Providerpropertytype} object.
     */
    protected Reference of_provider_property_type;

    /**
     * The 'of_provider_property_info' property, displayed as 'Of Provider Property Info' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Providerpropertyinfo} object.
     */
    protected Reference of_provider_property_info;

    /**
     * The 'of_directory_provider_configuration' property, displayed as 'Of Directory Provider Configuration' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Directoryproviderconfiguration} object.
     */
    protected Reference of_directory_provider_configuration;


    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }

    /** @see #of_provider_property_type */ @JsonProperty("of_provider_property_type")  public Reference getOfProviderPropertyType() { return this.of_provider_property_type; }
    /** @see #of_provider_property_type */ @JsonProperty("of_provider_property_type")  public void setOfProviderPropertyType(Reference of_provider_property_type) { this.of_provider_property_type = of_provider_property_type; }

    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public Reference getOfProviderPropertyInfo() { return this.of_provider_property_info; }
    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public void setOfProviderPropertyInfo(Reference of_provider_property_info) { this.of_provider_property_info = of_provider_property_info; }

    /** @see #of_directory_provider_configuration */ @JsonProperty("of_directory_provider_configuration")  public Reference getOfDirectoryProviderConfiguration() { return this.of_directory_provider_configuration; }
    /** @see #of_directory_provider_configuration */ @JsonProperty("of_directory_provider_configuration")  public void setOfDirectoryProviderConfiguration(Reference of_directory_provider_configuration) { this.of_directory_provider_configuration = of_directory_provider_configuration; }


    public static final Boolean isDirectoryproviderproperty(Object obj) { return (obj.getClass() == Directoryproviderproperty.class); }

}
