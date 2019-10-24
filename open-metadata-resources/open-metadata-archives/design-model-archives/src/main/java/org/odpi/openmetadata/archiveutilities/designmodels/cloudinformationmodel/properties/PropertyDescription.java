/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;

public class PropertyDescription extends ModelElement
{
    boolean  primitive   = false;
    String   dataTypeId  = null;

    public boolean isPrimitive()
    {
        return primitive;
    }


    public void setPrimitive(boolean primitive)
    {
        this.primitive = primitive;
    }


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
}
