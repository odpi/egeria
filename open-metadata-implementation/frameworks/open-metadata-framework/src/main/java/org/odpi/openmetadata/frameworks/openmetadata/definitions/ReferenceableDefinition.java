/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The ReferenceableDefinition is used to describe a predefined element that is a referencable.
 */
public interface ReferenceableDefinition
{
    /**
     * Return the unique identifier of this element.  It is only needed if the elements are to be loaded
     * into an open metadata archive.
     *
     * @return string
     */
    default String getGUID() { return null; }


    /**
     * Return the type of this element.
     *
     * @return string
     */
    default String getTypeName() { return OpenMetadataType.REFERENCEABLE.typeName; }


    /**
     * Return the unique name of the element.
     *
     * @return string
     */
    default String getQualifiedName()
    {
        return getTypeName() + "::" + getIdentifier() + "::" + getDisplayName();
    }


    /**
     * Version of Egeria that this element belongs.
     */
    String EGERIA_VERSION_IDENTIFIER="5.4-SNAPSHOT";


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    default String getVersionIdentifier()
    {
        return EGERIA_VERSION_IDENTIFIER;
    }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    String getIdentifier();


    /**
     * Return the short display name that is used in tables and titles.
     *
     * @return string
     */
    String getDisplayName();


    /**
     * Return the details description of this element.
     *
     * @return string
     */
    String getDescription();


    /**
     * Return the optional collection that this element is a part of.
     *
     * @return collection definition
     */
    default CollectionDefinition getParentCollection()
    {
        return null;
    }


    /**
     * Return whether this is a template or not.
     *
     * @return boolean
     */
    default boolean isTemplate()
    {
        return false;
    }
}
