/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.base.properties;

/**
 * ConceptModelDecoration defines an enum for the type of concept bead link.
 */
public enum ConceptModelDecoration
{
    /**
     * None
     */
    NONE(0),

    /**
     * AGGREGATION
     */
    AGGREGATION(1),

    /**
     * COMPOSITION
     */
    COMPOSITION(2),

    /**
     * EXTENSION
     */
    EXTENSION(3);


    ConceptModelDecoration(int  identifier)
    {
    }
}
