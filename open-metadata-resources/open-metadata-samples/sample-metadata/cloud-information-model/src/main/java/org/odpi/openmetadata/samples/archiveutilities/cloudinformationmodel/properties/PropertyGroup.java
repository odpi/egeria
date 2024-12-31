/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyGroup extends ModelElement
{
    private Map<String, PropertyDescription> propertyDescriptions = new HashMap<>();


    public List<PropertyDescription> getPropertyDescriptions()
    {
        if (propertyDescriptions.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(propertyDescriptions.values());
        }
    }

    public void addPropertyDescription(String               id,
                                       PropertyDescription  propertyDescription)
    {
        propertyDescriptions.put(id, propertyDescription);
    }

    public void setPropertyDescription(Map<String, PropertyDescription> propertyDescriptions)
    {
        this.propertyDescriptions = propertyDescriptions;
    }
}
