/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'application_install' asset type in IGC, displayed as 'Application Install' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApplicationInstall extends MainObject {

    public static final String IGC_TYPE_ID = "application_install";

    /**
     * The 'vendor_name' property, displayed as 'Vendor Name' in the IGC UI.
     */
    protected String vendor_name;

    /**
     * The 'instance_name' property, displayed as 'Instance Name' in the IGC UI.
     */
    protected String instance_name;

    /**
     * The 'default_credential' property, displayed as 'Default Credential' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credential} object.
     */
    protected Reference default_credential;

    /**
     * The 'location_name' property, displayed as 'Location Name' in the IGC UI.
     */
    protected String location_name;

    /**
     * The 'installation_date' property, displayed as 'Installation Date' in the IGC UI.
     */
    protected String installation_date;

    /**
     * The 'platform_identifier' property, displayed as 'Platform Identifier' in the IGC UI.
     */
    protected String platform_identifier;

    /**
     * The 'has_credential' property, displayed as 'Has Credential' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Credential} objects.
     */
    protected ReferenceList has_credential;

    /**
     * The 'installed_on_host' property, displayed as 'Installed On Host' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference installed_on_host;

    /**
     * The 'installation_path' property, displayed as 'Installation Path' in the IGC UI.
     */
    protected String installation_path;

    /**
     * The 'release_number' property, displayed as 'Release Number' in the IGC UI.
     */
    protected String release_number;


    /** @see #vendor_name */ @JsonProperty("vendor_name")  public String getVendorName() { return this.vendor_name; }
    /** @see #vendor_name */ @JsonProperty("vendor_name")  public void setVendorName(String vendor_name) { this.vendor_name = vendor_name; }

    /** @see #instance_name */ @JsonProperty("instance_name")  public String getInstanceName() { return this.instance_name; }
    /** @see #instance_name */ @JsonProperty("instance_name")  public void setInstanceName(String instance_name) { this.instance_name = instance_name; }

    /** @see #default_credential */ @JsonProperty("default_credential")  public Reference getDefaultCredential() { return this.default_credential; }
    /** @see #default_credential */ @JsonProperty("default_credential")  public void setDefaultCredential(Reference default_credential) { this.default_credential = default_credential; }

    /** @see #location_name */ @JsonProperty("location_name")  public String getLocationName() { return this.location_name; }
    /** @see #location_name */ @JsonProperty("location_name")  public void setLocationName(String location_name) { this.location_name = location_name; }

    /** @see #installation_date */ @JsonProperty("installation_date")  public String getInstallationDate() { return this.installation_date; }
    /** @see #installation_date */ @JsonProperty("installation_date")  public void setInstallationDate(String installation_date) { this.installation_date = installation_date; }

    /** @see #platform_identifier */ @JsonProperty("platform_identifier")  public String getPlatformIdentifier() { return this.platform_identifier; }
    /** @see #platform_identifier */ @JsonProperty("platform_identifier")  public void setPlatformIdentifier(String platform_identifier) { this.platform_identifier = platform_identifier; }

    /** @see #has_credential */ @JsonProperty("has_credential")  public ReferenceList getHasCredential() { return this.has_credential; }
    /** @see #has_credential */ @JsonProperty("has_credential")  public void setHasCredential(ReferenceList has_credential) { this.has_credential = has_credential; }

    /** @see #installed_on_host */ @JsonProperty("installed_on_host")  public Reference getInstalledOnHost() { return this.installed_on_host; }
    /** @see #installed_on_host */ @JsonProperty("installed_on_host")  public void setInstalledOnHost(Reference installed_on_host) { this.installed_on_host = installed_on_host; }

    /** @see #installation_path */ @JsonProperty("installation_path")  public String getInstallationPath() { return this.installation_path; }
    /** @see #installation_path */ @JsonProperty("installation_path")  public void setInstallationPath(String installation_path) { this.installation_path = installation_path; }

    /** @see #release_number */ @JsonProperty("release_number")  public String getReleaseNumber() { return this.release_number; }
    /** @see #release_number */ @JsonProperty("release_number")  public void setReleaseNumber(String release_number) { this.release_number = release_number; }


    public static final Boolean isApplicationInstall(Object obj) { return (obj.getClass() == ApplicationInstall.class); }

}
