/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code inv_data_rule_set_definition} asset type in IGC, displayed as '{@literal Data Rule Set Definition}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("inv_data_rule_set_definition")
public class InvDataRuleSetDefinition extends Reference {

    public static String getIgcTypeDisplayName() { return "Data Rule Set Definition"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_&_long_description} property, displayed as '{@literal Short & Long Description}' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code project} property, displayed as '{@literal Project}' in the IGC UI.
     */
    protected String project;

    /**
     * The {@code status} property, displayed as '{@literal Status}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CANDIDATE (displayed in the UI as 'CANDIDATE')</li>
     *     <li>ACCEPTED (displayed in the UI as 'ACCEPTED')</li>
     *     <li>STANDARD (displayed in the UI as 'STANDARD')</li>
     *     <li>DEPRECATED (displayed in the UI as 'DEPRECATED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>IN_PROCESS (displayed in the UI as 'IN_PROCESS')</li>
     *     <li>REJECTED (displayed in the UI as 'REJECTED')</li>
     *     <li>SUGGESTED (displayed in the UI as 'SUGGESTED')</li>
     *     <li>ERROR (displayed in the UI as 'ERROR')</li>
     * </ul>
     */
    protected String status;

    /**
     * The {@code contact} property, displayed as '{@literal Contact}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList contact;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code data_rule_definitions} property, displayed as '{@literal Data Rule Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList data_rule_definitions;

    /**
     * The {@code data_rule_sets} property, displayed as '{@literal Data Rule Sets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList data_rule_sets;

    /**
     * The {@code published} property, displayed as '{@literal Published}' in the IGC UI.
     */
    protected Boolean published;

    /**
     * The {@code native_id} property, displayed as '{@literal Native ID}' in the IGC UI.
     */
    protected String native_id;

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

    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #contact */ @JsonProperty("contact")  public ReferenceList getContact() { return this.contact; }
    /** @see #contact */ @JsonProperty("contact")  public void setContact(ReferenceList contact) { this.contact = contact; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public ReferenceList getDataRuleDefinitions() { return this.data_rule_definitions; }
    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public void setDataRuleDefinitions(ReferenceList data_rule_definitions) { this.data_rule_definitions = data_rule_definitions; }

    /** @see #data_rule_sets */ @JsonProperty("data_rule_sets")  public ReferenceList getDataRuleSets() { return this.data_rule_sets; }
    /** @see #data_rule_sets */ @JsonProperty("data_rule_sets")  public void setDataRuleSets(ReferenceList data_rule_sets) { this.data_rule_sets = data_rule_sets; }

    /** @see #published */ @JsonProperty("published")  public Boolean getPublished() { return this.published; }
    /** @see #published */ @JsonProperty("published")  public void setPublished(Boolean published) { this.published = published; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

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
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "status",
        "published",
        "native_id",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "native_id",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "contact",
        "assigned_to_terms",
        "data_rule_definitions",
        "data_rule_sets"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "status",
        "contact",
        "assigned_to_terms",
        "data_rule_definitions",
        "data_rule_sets",
        "published",
        "native_id",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInvDataRuleSetDefinition(Object obj) { return (obj.getClass() == InvDataRuleSetDefinition.class); }

}
