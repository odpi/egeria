/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Provides a definition of a pre-defined collection
 */
public interface ActorDefinition extends ReferenceableDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    default String getTypeName() { return OpenMetadataType.ACTOR.typeName; }
}
