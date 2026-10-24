/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.darwin;

import java.util.Set;

/**
 * The owner function of a boundary between two layers: it maps an element of the lower layer to the elements of the
 * upper layer that it belongs to.  For example, a schema attribute belongs to the data asset it is anchored to, and an
 * asset belongs to the digital products it is a member of.
 */
@FunctionalInterface
interface LineageOwners
{
    /**
     * Return the upper-layer elements that a lower-layer element belongs to.
     *
     * @param elementGUID unique identifier of the lower-layer element
     * @return unique identifiers of its owners - empty if it belongs to none
     * @throws Exception a retrieval failed - reported by the caller
     */
    Set<String> getOwners(String elementGUID) throws Exception;
}
