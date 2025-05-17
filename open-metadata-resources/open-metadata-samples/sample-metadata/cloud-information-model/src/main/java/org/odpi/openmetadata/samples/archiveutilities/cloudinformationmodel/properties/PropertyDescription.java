/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Property description represents the glossary term for a particular type of attribute (represented by a data field
 * and a concept).  It is set up if the description is not null.  The GUID is used got yhe glossary term guid.
 */
public class PropertyDescription extends ModelElement
{
    boolean  primitive   = false;
    String   dataTypeId  = null;



    public PropertyDescription()
    {
        super.setGUID(UUID.randomUUID().toString());
    }

    List<Attribute> attributes = new ArrayList<>();

    public boolean isPrimitive()
    {
        return primitive;
    }


    public void setPrimitive(boolean primitive)
    {
        this.primitive = primitive;
    }


    /**
     * Return the type - only set if the attribute is a link to another table - for primitives,
     * the type is set in the attribute.
     *
     * @return string
     */
    public String getDataTypeId()
    {
        return dataTypeId;
    }


    public void setDataTypeId(String dataTypeId)
    {
        if (this.dataTypeId != null)
        {
            if (! this.dataTypeId.equals(dataTypeId))
            {
                System.out.println("WARN: Property has multiple data types, current datatypeId is " + this.dataTypeId + "; new datatypeId: " + dataTypeId);
            }
        }

        this.dataTypeId = dataTypeId;
    }


    public List<Attribute> getAttributes()
    {
        return attributes;
    }


    public void addAttribute(Attribute attribute)
    {
        attributes.add(attribute);
    }
}
