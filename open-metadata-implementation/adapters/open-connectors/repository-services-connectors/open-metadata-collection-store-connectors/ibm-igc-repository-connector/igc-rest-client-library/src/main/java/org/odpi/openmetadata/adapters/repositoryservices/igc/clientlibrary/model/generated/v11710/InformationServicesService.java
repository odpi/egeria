/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code information_services_service} asset type in IGC, displayed as '{@literal Information Services Service}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesService extends Reference {

    public static String getIgcTypeId() { return "information_services_service"; }
    public static String getIgcTypeDisplayName() { return "Information Services Service"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code information_services_application} property, displayed as '{@literal Information Services Application}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationServicesApplication} object.
     */
    protected Reference information_services_application;

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
     * The {@code operations} property, displayed as '{@literal Operations}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesOperation} objects.
     */
    protected ReferenceList operations;

    /**
     * The {@code deployment_status} property, displayed as '{@literal Deployment Status}' in the IGC UI.
     */
    protected Boolean deployment_status;

    /**
     * The {@code deployment_location} property, displayed as '{@literal Deployment Location}' in the IGC UI.
     */
    protected String deployment_location;

    /**
     * The {@code primary_contact} property, displayed as '{@literal Primary Contact}' in the IGC UI.
     */
    protected String primary_contact;

    /**
     * The {@code contact_email} property, displayed as '{@literal Contact Email}' in the IGC UI.
     */
    protected String contact_email;

    /**
     * The {@code base_package} property, displayed as '{@literal Base Package}' in the IGC UI.
     */
    protected String base_package;

    /**
     * The {@code home_page} property, displayed as '{@literal Home Page}' in the IGC UI.
     */
    protected String home_page;

    /**
     * The {@code bindings} property, displayed as '{@literal Bindings}' in the IGC UI.
     */
    protected ArrayList<String> bindings;

    /**
     * The {@code requires_authentication} property, displayed as '{@literal Requires Authentication}' in the IGC UI.
     */
    protected ArrayList<String> requires_authentication;

    /**
     * The {@code requires_confidentiality} property, displayed as '{@literal Requires Confidentiality}' in the IGC UI.
     */
    protected ArrayList<String> requires_confidentiality;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #information_services_application */ @JsonProperty("information_services_application")  public Reference getInformationServicesApplication() { return this.information_services_application; }
    /** @see #information_services_application */ @JsonProperty("information_services_application")  public void setInformationServicesApplication(Reference information_services_application) { this.information_services_application = information_services_application; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #operations */ @JsonProperty("operations")  public ReferenceList getOperations() { return this.operations; }
    /** @see #operations */ @JsonProperty("operations")  public void setOperations(ReferenceList operations) { this.operations = operations; }

    /** @see #deployment_status */ @JsonProperty("deployment_status")  public Boolean getDeploymentStatus() { return this.deployment_status; }
    /** @see #deployment_status */ @JsonProperty("deployment_status")  public void setDeploymentStatus(Boolean deployment_status) { this.deployment_status = deployment_status; }

    /** @see #deployment_location */ @JsonProperty("deployment_location")  public String getDeploymentLocation() { return this.deployment_location; }
    /** @see #deployment_location */ @JsonProperty("deployment_location")  public void setDeploymentLocation(String deployment_location) { this.deployment_location = deployment_location; }

    /** @see #primary_contact */ @JsonProperty("primary_contact")  public String getPrimaryContact() { return this.primary_contact; }
    /** @see #primary_contact */ @JsonProperty("primary_contact")  public void setPrimaryContact(String primary_contact) { this.primary_contact = primary_contact; }

    /** @see #contact_email */ @JsonProperty("contact_email")  public String getContactEmail() { return this.contact_email; }
    /** @see #contact_email */ @JsonProperty("contact_email")  public void setContactEmail(String contact_email) { this.contact_email = contact_email; }

    /** @see #base_package */ @JsonProperty("base_package")  public String getBasePackage() { return this.base_package; }
    /** @see #base_package */ @JsonProperty("base_package")  public void setBasePackage(String base_package) { this.base_package = base_package; }

    /** @see #home_page */ @JsonProperty("home_page")  public String getHomePage() { return this.home_page; }
    /** @see #home_page */ @JsonProperty("home_page")  public void setHomePage(String home_page) { this.home_page = home_page; }

    /** @see #bindings */ @JsonProperty("bindings")  public ArrayList<String> getBindings() { return this.bindings; }
    /** @see #bindings */ @JsonProperty("bindings")  public void setBindings(ArrayList<String> bindings) { this.bindings = bindings; }

    /** @see #requires_authentication */ @JsonProperty("requires_authentication")  public ArrayList<String> getRequiresAuthentication() { return this.requires_authentication; }
    /** @see #requires_authentication */ @JsonProperty("requires_authentication")  public void setRequiresAuthentication(ArrayList<String> requires_authentication) { this.requires_authentication = requires_authentication; }

    /** @see #requires_confidentiality */ @JsonProperty("requires_confidentiality")  public ArrayList<String> getRequiresConfidentiality() { return this.requires_confidentiality; }
    /** @see #requires_confidentiality */ @JsonProperty("requires_confidentiality")  public void setRequiresConfidentiality(ArrayList<String> requires_confidentiality) { this.requires_confidentiality = requires_confidentiality; }

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
        "deployment_status",
        "deployment_location",
        "primary_contact",
        "contact_email",
        "base_package",
        "home_page",
        "bindings",
        "requires_authentication",
        "requires_confidentiality",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "deployment_location",
        "primary_contact",
        "contact_email",
        "base_package",
        "home_page",
        "bindings",
        "requires_authentication",
        "requires_confidentiality",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "operations"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "information_services_application",
        "labels",
        "stewards",
        "operations",
        "deployment_status",
        "deployment_location",
        "primary_contact",
        "contact_email",
        "base_package",
        "home_page",
        "bindings",
        "requires_authentication",
        "requires_confidentiality",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInformationServicesService(Object obj) { return (obj.getClass() == InformationServicesService.class); }

}
