/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

/**
 * Attribute describes a primitive attribute in the
 */
public class Attribute extends Property
{
    private String  dataType = null;

    public Attribute(String              guid,
                     String              technicalName,
                     PropertyDescription propertyDescription)
    {
        super(guid, technicalName, propertyDescription);
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
