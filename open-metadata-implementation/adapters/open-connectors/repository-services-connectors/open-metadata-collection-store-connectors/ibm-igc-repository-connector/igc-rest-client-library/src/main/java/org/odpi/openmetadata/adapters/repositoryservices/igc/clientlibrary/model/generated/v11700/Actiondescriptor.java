/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

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
 * POJO for the {@code actiondescriptor} asset type in IGC, displayed as '{@literal ActionDescriptor}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("actiondescriptor")
public class Actiondescriptor extends Reference {

    public static String getIgcTypeDisplayName() { return "ActionDescriptor"; }

    /**
     * The {@code instance_constraint_expression} property, displayed as '{@literal Instance Constraint Expression}' in the IGC UI.
     */
    protected String instance_constraint_expression;

    /**
     * The {@code parameters} property, displayed as '{@literal Parameters}' in the IGC UI.
     */
    protected String parameters;

    /**
     * The {@code has_privilege_system_role} property, displayed as '{@literal Has Privilege System Role}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SystemRole} objects.
     */
    protected ReferenceList has_privilege_system_role;

    /**
     * The {@code has_privilege_contact_role} property, displayed as '{@literal Has Privilege Contact Role}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link UserRole} objects.
     */
    protected ReferenceList has_privilege_contact_role;

    /**
     * The {@code of_class_descriptor} property, displayed as '{@literal Of Class Descriptor}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code display_name} property, displayed as '{@literal Display Name}' in the IGC UI.
     */
    protected String display_name;

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code operation} property, displayed as '{@literal Operation}' in the IGC UI.
     */
    protected String operation;

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


    /** @see #instance_constraint_expression */ @JsonProperty("instance_constraint_expression")  public String getInstanceConstraintExpression() { return this.instance_constraint_expression; }
    /** @see #instance_constraint_expression */ @JsonProperty("instance_constraint_expression")  public void setInstanceConstraintExpression(String instance_constraint_expression) { this.instance_constraint_expression = instance_constraint_expression; }

    /** @see #parameters */ @JsonProperty("parameters")  public String getParameters() { return this.parameters; }
    /** @see #parameters */ @JsonProperty("parameters")  public void setParameters(String parameters) { this.parameters = parameters; }

    /** @see #has_privilege_system_role */ @JsonProperty("has_privilege_system_role")  public ReferenceList getHasPrivilegeSystemRole() { return this.has_privilege_system_role; }
    /** @see #has_privilege_system_role */ @JsonProperty("has_privilege_system_role")  public void setHasPrivilegeSystemRole(ReferenceList has_privilege_system_role) { this.has_privilege_system_role = has_privilege_system_role; }

    /** @see #has_privilege_contact_role */ @JsonProperty("has_privilege_contact_role")  public ReferenceList getHasPrivilegeContactRole() { return this.has_privilege_contact_role; }
    /** @see #has_privilege_contact_role */ @JsonProperty("has_privilege_contact_role")  public void setHasPrivilegeContactRole(ReferenceList has_privilege_contact_role) { this.has_privilege_contact_role = has_privilege_contact_role; }

    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public Reference getOfClassDescriptor() { return this.of_class_descriptor; }
    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public void setOfClassDescriptor(Reference of_class_descriptor) { this.of_class_descriptor = of_class_descriptor; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #display_name */ @JsonProperty("display_name")  public String getDisplayName() { return this.display_name; }
    /** @see #display_name */ @JsonProperty("display_name")  public void setDisplayName(String display_name) { this.display_name = display_name; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #operation */ @JsonProperty("operation")  public String getOperation() { return this.operation; }
    /** @see #operation */ @JsonProperty("operation")  public void setOperation(String operation) { this.operation = operation; }

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
        "instance_constraint_expression",
        "parameters",
        "description",
        "display_name",
        "name",
        "operation",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "instance_constraint_expression",
        "parameters",
        "description",
        "display_name",
        "name",
        "operation",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_privilege_system_role",
        "has_privilege_contact_role"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "instance_constraint_expression",
        "parameters",
        "has_privilege_system_role",
        "has_privilege_contact_role",
        "of_class_descriptor",
        "description",
        "display_name",
        "name",
        "operation",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isActiondescriptor(Object obj) { return (obj.getClass() == Actiondescriptor.class); }

}
