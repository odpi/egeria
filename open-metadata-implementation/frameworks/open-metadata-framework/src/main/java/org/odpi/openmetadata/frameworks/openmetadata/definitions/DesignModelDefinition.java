/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * A description of the predefined design models.
 */
public interface DesignModelDefinition extends CollectionDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    default String getTypeName() { return OpenMetadataType.DESIGN_MODEL.typeName; }


    /**
     * Return the authors of the design model.  It defaults to the Egeria community but can be overridden.
     *
     * @return string
     */
    default List<String> authors()
    {
        return List.of("LF AI & Data Egeria Community");
    }
}
