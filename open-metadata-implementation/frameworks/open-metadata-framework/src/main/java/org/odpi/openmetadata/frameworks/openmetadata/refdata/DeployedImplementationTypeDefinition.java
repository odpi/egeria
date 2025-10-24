/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * CoreContentPack.omarchive and are available in the open metadata repository as valid values.
 */
public interface DeployedImplementationTypeDefinition
{
    /**
     * Return preferred value for deployed implementation type.
     * 
     * @return string
     */
    String getDeployedImplementationType();


    /**
     * Return the optional deployed implementation type that this technology is a tye of.
     *
     * @return deployed implementation type enum
     */
    DeployedImplementationTypeDefinition getIsATypeOf();


    /**
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    String getAssociatedTypeName();


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    String getAssociatedClassification();


    /**
     * Return the qualified name for this deployed implementation type.
     *
     * @return string
     */
    String getQualifiedName();


    /**
     * Return the description for this value.
     * 
     * @return string
     */
    String getDescription();


    /**
     * Return the URL to more information.
     *
     * @return string url
     */
    String getWikiLink();
}
