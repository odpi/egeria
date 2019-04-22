/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

/**
 * Interface to hold the Node mapping and response object for the type of Node.
 */
public interface INodeBundle
{

    /**
     * The associated Node mapper
     * @return node mapper
     */
    INodeMapper getMapper();

    /**
     * Get Node name
     * @return node name
     */
    String getName();

}
