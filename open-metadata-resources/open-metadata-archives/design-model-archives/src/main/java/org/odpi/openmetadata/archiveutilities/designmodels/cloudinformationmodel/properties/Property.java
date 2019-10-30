/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;

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


    public Property(PropertyDescription propertyDescription)
    {
        this.propertyDescription = propertyDescription;

        super.setGUID(UUID.randomUUID().toString());
        super.setId(propertyDescription.getId());
        super.setDisplayName(propertyDescription.getDisplayName());
        super.setDescription(propertyDescription.getDescription());
    }


    public boolean isOptional()
    {
        return optional;
    }


    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }


    public boolean isSingleton()
    {
        return singleton;
    }


    public void setSingleton(boolean singleton)
    {
        this.singleton = singleton;
    }


    public String getDataTypeId()
    {
        return propertyDescription.getDataTypeId();
    }
}
