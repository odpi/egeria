/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

/**
 * Define the relationship between the solution roles and the solution components
 */
public enum EgeriaSolutionComponentActor implements SolutionComponentActorDefinition
{
    COMMUNITY_TO_ARCHIVE(EgeriaRoleDefinition.EGERIA_COMMUNITY,
                         EgeriaSolutionComponent.OPEN_METADATA_ARCHIVE,
                         "develops",
                         "Develops the content for predefined open metadata types and instances that enable the Egeria services and solutions."),

    USER_TO_JUPYTER(EgeriaRoleDefinition.PYTHON_PROGRAMMER,
                     EgeriaSolutionComponent.JUPYTER_NOTEBOOK,
                     "writes python code for",
                     "Coders developing new function in an interactive style."),

    USER_TO_MY_EGERIA(EgeriaRoleDefinition.OPEN_METADATA_USER,
                      EgeriaSolutionComponent.MY_EGERIA,
                      "interacts with",
                      "Navigates to the desired function, fills in forms, executes commands and retrieves results."),

    USER_TO_HEY_EGERIA(EgeriaRoleDefinition.OPEN_METADATA_USER,
                      EgeriaSolutionComponent.HEY_EGERIA,
                      "interacts with",
                      "Navigates to the desired function, fills in forms, executes commands and retrieves results."),

    USER_TO_DR_EGERIA(EgeriaRoleDefinition.OPEN_METADATA_USER,
                      EgeriaSolutionComponent.DR_EGERIA,
                      "work with markdown",
                         "Authors markdown documents for new and updates to open metadata plus commands to create reports and perform open governance actions."),

    USER_TO_LOAD_ARCHIVE(EgeriaRoleDefinition.OPEN_METADATA_USER,
                      EgeriaSolutionComponent.LOAD_ARCHIVE,
                      "calls",
                      "Requests that a specific open metadata archive is loaded."),

    USER_TO_WEB_SERVER(EgeriaRoleDefinition.OPEN_METADATA_USER,
                         EgeriaSolutionComponent.APACHE_WEB_SERVER,
                         "accesses content",
                         "Browses HTML encoded webpages."),

    ;

    final ActorRoleDefinition         solutionRole;
    final SolutionComponentDefinition solutionComponent;
    final String                      role;
    final String                      description;

    EgeriaSolutionComponentActor(EgeriaRoleDefinition    solutionRole,
                                 EgeriaSolutionComponent solutionComponent,
                                 String                  role,
                                 String                  description)
    {
        this.solutionRole      = solutionRole;
        this.solutionComponent = solutionComponent;
        this.role              = role;
        this.description       = description;
    }


    /**
     * Return the solution role to link to.
     *
     * @return role definition
     */
    @Override
    public ActorRoleDefinition getSolutionRole()
    {
        return solutionRole;
    }


    /**
     * Return the solution component to link to.
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getSolutionComponent()
    {
        return solutionComponent;
    }


    /**
     * Return the role relationship label.
     *
     * @return string
     */
    @Override
    public String getRole()
    {
        return role;
    }


    /**
     * Return the role relationship description.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "EgeriaSolutionComponentActor{" +
                "solutionRole=" + solutionRole +
                ", productSolutionComponent=" + solutionComponent +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                "}";
    }
}
