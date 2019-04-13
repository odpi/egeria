/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

/**
 * I LineBundle is a
 */
public class LineBundle implements  ILineBundle
{
    private final ILineMapper mapper;
    private final String name;
    private final String typeName;

    protected LineBundle(ILineMapper mapper, String name,String typeName ) {

        this.mapper = mapper;
        this.name = name;
        this.typeName = typeName;
    }


    @Override
    public ILineMapper getMapper()
    {
       return mapper;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getTypeName()
    {
        return typeName;
    }


}
