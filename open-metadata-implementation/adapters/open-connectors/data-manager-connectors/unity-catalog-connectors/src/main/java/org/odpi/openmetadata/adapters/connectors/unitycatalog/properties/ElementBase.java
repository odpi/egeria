/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;


/**
 * Common properties of an element returned from UC.
 */
public interface ElementBase
{

    /**
     * Return the unique name of the element within its name space.
     *
     * @return string
     */
    String getName();


    /**
     * Set up the unique name of the element within its name space.
     *
     * @param name string name
     */
    void setName(String name);


    /**
     * Return a comment describing the element within its name space.
     *
     * @return text
     */
    String getComment();


    /**
     * Set up a comment describing the element within its name space.
     *
     * @param comment text
     */
    void setComment(String comment);


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


    /**
     * Return the userId that created the element.
     *
     * @return string name
     */
    String getCreated_by();


    /**
     * Set up the userId that created the element.
     *
     * @param created_by string name
     */
    void setCreated_by(String created_by);


    /**
     * Return the element that last updated the element.
     *
     * @return string name
     */
    String getUpdated_by();


    /**
     * Set up the element that last updated the element.
     *
     * @param updated_by string name
     */
    void setUpdated_by(String updated_by);


    /**
     * Return the owner of the element.
     *
     * @return string name
     */
    String getOwner();


    /**
     * Set up the owner of the element.
     *
     * @param owner string name
     */
    void setOwner(String owner);
}
