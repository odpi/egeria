/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code application_install} asset type in IGC, displayed as '{@literal Application Install}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApplicationInstall extends Reference {

    public static String getIgcTypeId() { return "application_install"; }
    public static String getIgcTypeDisplayName() { return "Application Install"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code vendor_name} property, displayed as '{@literal Vendor Name}' in the IGC UI.
     */
    protected String vendor_name;

    /**
     * The {@code instance_name} property, displayed as '{@literal Instance Name}' in the IGC UI.
     */
    protected String instance_name;

    /**
     * The {@code default_credential} property, displayed as '{@literal Default Credential}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credential} object.
     */
    protected Reference default_credential;

    /**
     * The {@code location_name} property, displayed as '{@literal Location Name}' in the IGC UI.
     */
    protected String location_name;

    /**
     * The {@code installation_date} property, displayed as '{@literal Installation Date}' in the IGC UI.
     */
    protected String installation_date;

    /**
     * The {@code platform_identifier} property, displayed as '{@literal Platform Identifier}' in the IGC UI.
     */
    protected String platform_identifier;

    /**
     * The {@code has_credential} property, displayed as '{@literal Has Credential}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Credential} objects.
     */
    protected ReferenceList has_credential;

    /**
     * The {@code installed_on_host} property, displayed as '{@literal Installed On Host}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference installed_on_host;

    /**
     * The {@code installation_path} property, displayed as '{@literal Installation Path}' in the IGC UI.
     */
    protected String installation_path;

    /**
     * The {@code release_number} property, displayed as '{@literal Release Number}' in the IGC UI.
     */
    protected String release_number;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "vendor_name",
        "instance_name",
        "location_name",
        "installation_date",
        "platform_identifier",
        "installation_path",
        "release_number"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "vendor_name",
        "instance_name",
        "location_name",
        "installation_date",
        "platform_identifier",
        "installation_path",
        "release_number"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "has_credential"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "vendor_name",
        "instance_name",
        "default_credential",
        "location_name",
        "installation_date",
        "platform_identifier",
        "has_credential",
        "installed_on_host",
        "installation_path",
        "release_number"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isApplicationInstall(Object obj) { return (obj.getClass() == ApplicationInstall.class); }

}
