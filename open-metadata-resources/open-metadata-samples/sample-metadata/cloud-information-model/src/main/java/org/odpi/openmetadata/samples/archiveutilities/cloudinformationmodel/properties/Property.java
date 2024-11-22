/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.UUID;

/**
 * Property describes an instance of an attribute or a link to a concept.  These properties are private to
 * a concept.  The property description is shared amongst properties of the same name.
 */
public class Property extends ModelElement
{
    private boolean             optional = true;   // minCount != 1
    private boolean             singleton = false; // maxCount != 1
    private PropertyDescription propertyDescription;
    private String              minCount = null;
    private String              maxCount = null;


    public Property(String              guid,
                    String              technicalName,
                    PropertyDescription propertyDescription)
    {
        this.propertyDescription = propertyDescription;

        super.setGUID(guid);
        super.setTechnicalName(technicalName);
        super.setDisplayName(propertyDescription.getDisplayName());
        super.setDescription(propertyDescription.getDescription());
    }


    public String getMinCount()
    {
        return minCount;
    }


    public void setMinCount(String minCount)
    {
        this.minCount = minCount;
        optional = ((minCount == null) || "0".equals(minCount));
    }


    public String getMaxCount()
    {
        return maxCount;
    }


    public void setMaxCount(String maxCount)
    {
        this.maxCount = maxCount;
        singleton = ("1".equals(maxCount));
    }


    public boolean isOptional()
    {
        return optional;
    }

    public boolean isSingleton()
    {
        return singleton;
    }

}
