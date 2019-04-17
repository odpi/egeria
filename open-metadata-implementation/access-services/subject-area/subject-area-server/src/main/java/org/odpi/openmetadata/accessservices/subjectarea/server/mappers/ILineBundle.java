/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

/**
 * Interface to hold the Line mapping and response object for the type of Line.
 */
public interface ILineBundle
{

    /**
     * The associated Line mapper
     * @return line mapper
     */
    ILineMapper getMapper();

    /**
     * Get Line name
     * @return line name
     */
    String getName();

    /**
     * Get the line ttype name
      * @return type name
     */
    String getTypeName();

}
