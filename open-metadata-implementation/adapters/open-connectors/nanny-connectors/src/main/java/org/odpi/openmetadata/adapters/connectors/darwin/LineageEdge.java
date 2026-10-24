/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

/**
 * One lineage relationship in a layer, oriented the way the data (or control) flows: from the supplier to the
 * consumer.  For most lineage relationship types the supplier is at end 1, but not all - see LineageGraph.
 *
 * @param relationshipGUID unique identifier of the relationship
 * @param relationshipTypeName type of the relationship
 * @param supplierGUID unique identifier of the element the data flows from
 * @param consumerGUID unique identifier of the element the data flows to
 * @param iscQualifiedName information supply chain the relationship belongs to - may be null
 */
record LineageEdge(String relationshipGUID,
                   String relationshipTypeName,
                   String supplierGUID,
                   String consumerGUID,
                   String iscQualifiedName)
{
}
