/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;

/**
 * Attribute describes a primitive attribute in the
 */
public class Attribute extends Property
{
    private String  dataType = null;


    public Attribute(PropertyDescription propertyDescription)
    {
        super(propertyDescription);
    }


    public String getDataType()
    {
        return dataType;
    }


    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
}
