/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ProductRoleDefinition is used to feed the definition of the actor roles for
 * Coco Pharmaceuticals' product catalog.
 */
public interface ActorRoleDefinition extends ActorDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    @Override
    default String getTypeName() { return OpenMetadataType.ACTOR_ROLE.typeName; }


    /**
     * Returns scope of the role.
     *
     * @return description
     */
    String getScope();
}
