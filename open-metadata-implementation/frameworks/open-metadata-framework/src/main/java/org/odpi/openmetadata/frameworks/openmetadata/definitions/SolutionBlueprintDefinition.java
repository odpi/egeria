/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public interface SolutionBlueprintDefinition extends DesignModelDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    default String getTypeName() { return OpenMetadataType.SOLUTION_BLUEPRINT.typeName; }


    /**
     * Return the list of components that are members of the solution blueprint.
     *
     * @return list of component definitions
     */
    List<SolutionComponentDefinition> getSolutionComponents();

}
