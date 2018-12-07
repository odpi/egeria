/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'directory' asset type in IGC, displayed as 'Directory' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Directory extends MainObject {

    public static final String IGC_TYPE_ID = "directory";

    /**
     * The 'has_directory_provider_configuration' property, displayed as 'Has Directory Provider Configuration' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Directoryproviderconfiguration} object.
     */
    protected Reference has_directory_provider_configuration;

    /**
     * The 'has_principal' property, displayed as 'Has Principal' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList has_principal;


    /** @see #has_directory_provider_configuration */ @JsonProperty("has_directory_provider_configuration")  public Reference getHasDirectoryProviderConfiguration() { return this.has_directory_provider_configuration; }
    /** @see #has_directory_provider_configuration */ @JsonProperty("has_directory_provider_configuration")  public void setHasDirectoryProviderConfiguration(Reference has_directory_provider_configuration) { this.has_directory_provider_configuration = has_directory_provider_configuration; }

    /** @see #has_principal */ @JsonProperty("has_principal")  public ReferenceList getHasPrincipal() { return this.has_principal; }
    /** @see #has_principal */ @JsonProperty("has_principal")  public void setHasPrincipal(ReferenceList has_principal) { this.has_principal = has_principal; }


    public static final Boolean isDirectory(Object obj) { return (obj.getClass() == Directory.class); }

}
