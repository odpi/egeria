/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.base.properties;

/**
 * ConceptModelDecoration defines an enum for the type of concept bead link.
 */
public enum ConceptModelDecoration
{
    NONE(0),
    AGGREGATION(1),
    COMPOSITION(2),
    EXTENSION(3);


    ConceptModelDecoration(int  identifier)
    {
    }
}
