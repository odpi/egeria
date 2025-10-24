/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

/**
 * Define the relationship between the solution roles and the solution components
 */
public interface SolutionComponentActorDefinition
{
    /**
     * Return the solution role to link to.
     *
     * @return role definition
     */
    ActorRoleDefinition getSolutionRole();

    /**
     * Return the solution component to link to.
     *
     * @return component definition
     */
    SolutionComponentDefinition getSolutionComponent();


    /**
     * Return the role relationship label.
     *
     * @return string
     */
    String getRole();


    /**
     * Return the role relationship description.
     *
     * @return string
     */
    String getDescription();
}
