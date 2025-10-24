/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import java.util.List;

/**
 * Define the linkage between solution components in a blueprint.
 */
public interface SolutionComponentWireDefinition
{
    /**
     * Return the component for end 1
     *
     * @return component definition
     */
    SolutionComponentDefinition getComponent1();


    /**
     * Return the component for end 2
     *
     * @return component definition
     */
    SolutionComponentDefinition getComponent2();


    /**
     * Return the relationship label.
     *
     * @return string
     */
    String getLabel();


    /**
     * Return the relationship description.
     *
     * @return string
     */
    String getDescription();


    /**
     * Return the list of ISC qualified names that the wire belongs to.
     *
     * @return list of strings
     */
    List<String> getISCQualifiedNames();
}
