/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Provides a definition of a pre-defined collection
 */
public interface CollectionDefinition extends ReferenceableDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    @Override
    default String getTypeName() { return OpenMetadataType.COLLECTION.typeName; }


    /**
     * Return the name of a classification for the collection.
     *
     * @return string
     */
    default String getCollectionClassification()
    {
        return null;
    }
}
