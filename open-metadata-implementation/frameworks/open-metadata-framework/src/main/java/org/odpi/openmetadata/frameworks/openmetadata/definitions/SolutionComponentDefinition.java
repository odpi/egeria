/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * A description of the predefined solution components.
 */
public interface SolutionComponentDefinition extends ReferenceableDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    default String getTypeName() { return OpenMetadataType.SOLUTION_COMPONENT.typeName; }


    /**
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    String getComponentType();


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    String getImplementationType();


    /**
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    List<SolutionComponentDefinition> getSubComponents();


    /**
     * Return the GUID of the implementation element (or null)
     *
     * @return guid
     */
    String getImplementationResource();
}
