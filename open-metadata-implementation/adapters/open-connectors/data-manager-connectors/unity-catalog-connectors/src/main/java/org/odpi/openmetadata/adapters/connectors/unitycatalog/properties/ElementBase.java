/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

/**
 * Common properties of an element returned from UC.
 */
public interface ElementBase
{
    /**
     * Return the time that the element was created.
     *
     * @return date/time as long
     */
    long getCreated_at();


    /**
     * Set up the time that the element was created.
     *
     * @param created_at date/time as long
     */
    void setCreated_at(long created_at);


    /**
     * Return the time that the element was last updated.
     *
     * @return date/time as long
     */
    long getUpdated_at();


    /**
     * Set up the time that the element was last updated.
     *
     * @param updated_at  date/time as long
     */
    void setUpdated_at(long updated_at);
}
