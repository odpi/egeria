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
 * POJO for the {@code information_services_operation} asset type in IGC, displayed as '{@literal Information Services Operation}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesOperation extends Reference {

    public static String getIgcTypeId() { return "information_services_operation"; }
    public static String getIgcTypeDisplayName() { return "Information Services Operation"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code information_services_service} property, displayed as '{@literal Information Services Service}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationServicesService} object.
     */
    protected Reference information_services_service;

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
     * The {@code input_arguments} property, displayed as '{@literal Input Arguments}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesArgument} objects.
     */
    protected ReferenceList input_arguments;

    /**
     * The {@code output_arguments} property, displayed as '{@literal Output Arguments}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServicesArgument} objects.
     */
    protected ReferenceList output_arguments;

    /**
     * The {@code deployment_status} property, displayed as '{@literal Deployment Status}' in the IGC UI.
     */
    protected Boolean deployment_status;

    /**
     * The {@code provider_description} property, displayed as '{@literal Provider Description}' in the IGC UI.
     */
    protected String provider_description;

    /**
     * The {@code provider_server} property, displayed as '{@literal Provider Server}' in the IGC UI.
     */
    protected ArrayList<String> provider_server;

    /**
     * The {@code provider_database} property, displayed as '{@literal Provider Database}' in the IGC UI.
     */
    protected ArrayList<String> provider_database;

    /**
     * The {@code provider_sql_statement} property, displayed as '{@literal Provider SQL Statement}' in the IGC UI.
     */
    protected ArrayList<String> provider_sql_statement;

    /**
     * The {@code references_job} property, displayed as '{@literal References Job}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference references_job;

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

    /** @see #information_services_service */ @JsonProperty("information_services_service")  public Reference getInformationServicesService() { return this.information_services_service; }
    /** @see #information_services_service */ @JsonProperty("information_services_service")  public void setInformationServicesService(Reference information_services_service) { this.information_services_service = information_services_service; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #input_arguments */ @JsonProperty("input_arguments")  public ReferenceList getInputArguments() { return this.input_arguments; }
    /** @see #input_arguments */ @JsonProperty("input_arguments")  public void setInputArguments(ReferenceList input_arguments) { this.input_arguments = input_arguments; }

    /** @see #output_arguments */ @JsonProperty("output_arguments")  public ReferenceList getOutputArguments() { return this.output_arguments; }
    /** @see #output_arguments */ @JsonProperty("output_arguments")  public void setOutputArguments(ReferenceList output_arguments) { this.output_arguments = output_arguments; }

    /** @see #deployment_status */ @JsonProperty("deployment_status")  public Boolean getDeploymentStatus() { return this.deployment_status; }
    /** @see #deployment_status */ @JsonProperty("deployment_status")  public void setDeploymentStatus(Boolean deployment_status) { this.deployment_status = deployment_status; }

    /** @see #provider_description */ @JsonProperty("provider_description")  public String getProviderDescription() { return this.provider_description; }
    /** @see #provider_description */ @JsonProperty("provider_description")  public void setProviderDescription(String provider_description) { this.provider_description = provider_description; }

    /** @see #provider_server */ @JsonProperty("provider_server")  public ArrayList<String> getProviderServer() { return this.provider_server; }
    /** @see #provider_server */ @JsonProperty("provider_server")  public void setProviderServer(ArrayList<String> provider_server) { this.provider_server = provider_server; }

    /** @see #provider_database */ @JsonProperty("provider_database")  public ArrayList<String> getProviderDatabase() { return this.provider_database; }
    /** @see #provider_database */ @JsonProperty("provider_database")  public void setProviderDatabase(ArrayList<String> provider_database) { this.provider_database = provider_database; }

    /** @see #provider_sql_statement */ @JsonProperty("provider_sql_statement")  public ArrayList<String> getProviderSqlStatement() { return this.provider_sql_statement; }
    /** @see #provider_sql_statement */ @JsonProperty("provider_sql_statement")  public void setProviderSqlStatement(ArrayList<String> provider_sql_statement) { this.provider_sql_statement = provider_sql_statement; }

    /** @see #references_job */ @JsonProperty("references_job")  public Reference getReferencesJob() { return this.references_job; }
    /** @see #references_job */ @JsonProperty("references_job")  public void setReferencesJob(Reference references_job) { this.references_job = references_job; }

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
        "provider_description",
        "provider_server",
        "provider_database",
        "provider_sql_statement",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "provider_description",
        "provider_server",
        "provider_database",
        "provider_sql_statement",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "input_arguments",
        "output_arguments"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "information_services_service",
        "labels",
        "stewards",
        "input_arguments",
        "output_arguments",
        "deployment_status",
        "provider_description",
        "provider_server",
        "provider_database",
        "provider_sql_statement",
        "references_job",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInformationServicesOperation(Object obj) { return (obj.getClass() == InformationServicesOperation.class); }

}
