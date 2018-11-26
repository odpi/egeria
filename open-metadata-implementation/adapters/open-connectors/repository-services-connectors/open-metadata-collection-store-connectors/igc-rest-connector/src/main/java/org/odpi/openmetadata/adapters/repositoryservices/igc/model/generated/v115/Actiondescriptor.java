/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'actiondescriptor' asset type in IGC, displayed as 'ActionDescriptor' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Actiondescriptor extends MainObject {

    public static final String IGC_TYPE_ID = "actiondescriptor";

    /**
     * The 'instance_constraint_expression' property, displayed as 'Instance Constraint Expression' in the IGC UI.
     */
    protected String instance_constraint_expression;

    /**
     * The 'parameters' property, displayed as 'Parameters' in the IGC UI.
     */
    protected String parameters;

    /**
     * The 'has_privilege_system_role' property, displayed as 'Has Privilege System Role' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SystemRole} objects.
     */
    protected ReferenceList has_privilege_system_role;

    /**
     * The 'has_privilege_contact_role' property, displayed as 'Has Privilege Contact Role' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link UserRole} objects.
     */
    protected ReferenceList has_privilege_contact_role;

    /**
     * The 'of_class_descriptor' property, displayed as 'Of Class Descriptor' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'display_name' property, displayed as 'Display Name' in the IGC UI.
     */
    protected String display_name;

    /**
     * The 'operation' property, displayed as 'Operation' in the IGC UI.
     */
    protected String operation;


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

    /** @see #operation */ @JsonProperty("operation")  public String getOperation() { return this.operation; }
    /** @see #operation */ @JsonProperty("operation")  public void setOperation(String operation) { this.operation = operation; }


    public static final Boolean isActiondescriptor(Object obj) { return (obj.getClass() == Actiondescriptor.class); }

}
