/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'directoryproviderconfiguration' asset type in IGC, displayed as 'DirectoryProviderConfiguration' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Directoryproviderconfiguration extends MainObject {

    public static final String IGC_TYPE_ID = "directoryproviderconfiguration";

    /**
     * The 'provider_name' property, displayed as 'Provider Name' in the IGC UI.
     */
    protected String provider_name;

    /**
     * The 'of_directory' property, displayed as 'Of Directory' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Directory} objects.
     */
    protected ReferenceList of_directory;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'has_directory_provider_property' property, displayed as 'Has Directory Provider Property' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Directoryproviderproperty} objects.
     */
    protected ReferenceList has_directory_provider_property;


    /** @see #provider_name */ @JsonProperty("provider_name")  public String getProviderName() { return this.provider_name; }
    /** @see #provider_name */ @JsonProperty("provider_name")  public void setProviderName(String provider_name) { this.provider_name = provider_name; }

    /** @see #of_directory */ @JsonProperty("of_directory")  public ReferenceList getOfDirectory() { return this.of_directory; }
    /** @see #of_directory */ @JsonProperty("of_directory")  public void setOfDirectory(ReferenceList of_directory) { this.of_directory = of_directory; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public ReferenceList getHasDirectoryProviderProperty() { return this.has_directory_provider_property; }
    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public void setHasDirectoryProviderProperty(ReferenceList has_directory_provider_property) { this.has_directory_provider_property = has_directory_provider_property; }


    public static final Boolean isDirectoryproviderconfiguration(Object obj) { return (obj.getClass() == Directoryproviderconfiguration.class); }

}
