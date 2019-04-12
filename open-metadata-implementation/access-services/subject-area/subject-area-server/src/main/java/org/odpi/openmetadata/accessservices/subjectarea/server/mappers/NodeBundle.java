/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

/**
 * A NodeBundle is an object that holds the mapper and the name of the node. This object holds specific node information
 * for a type, and is used in conjunction with the Factory.
 * allows the code to work
 *
 */
public class NodeBundle implements  INodeBundle
{
    private final INodeMapper mapper;
    private final String name;

    protected NodeBundle(INodeMapper mapper, String name) {

        this.mapper = mapper;
        this.name = name;
    }
    @Override
    public INodeMapper getMapper()
    {
       return mapper;
    }

    @Override
    public String getName()
    {
        return name;
    }

}
