/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

/**
 * The identity of one of the relationships the Darwin Product Dependency Manager maintains: the elements at its two
 * ends and, because every one of these relationship types is multi-link, the information supply chain that tells the
 * relationships between the same two elements apart.  For a DataFlow, end 1 is the supplier and end 2 the consumer;
 * for a DigitalProductDependency, end 1 is the dependent product and end 2 the product it depends on.  The information
 * supply chain may be null, which is a relationship derived from finer-grained lineage that belongs to no information
 * supply chain.
 *
 * @param end1GUID unique identifier of the element at end 1
 * @param end2GUID unique identifier of the element at end 2
 * @param iscQualifiedName information supply chain that the relationship belongs to - may be null
 */
record LinkKey(String end1GUID,
               String end2GUID,
               String iscQualifiedName)
{
}
