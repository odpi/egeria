/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;


public class Link extends Property
{
    private String domainConceptId = null;
    private String rangeConceptId;


    public Link(PropertyDescription propertyDescription)
    {
        super(propertyDescription);

        rangeConceptId = propertyDescription.getDataTypeId();
    }


    public String getDomainConceptId()
    {
        return domainConceptId;
    }


    public void setDomainConceptId(String domainConceptId)
    {
        this.domainConceptId = domainConceptId;
    }


    public String getRangeConceptId()
    {
        return rangeConceptId;
    }


    public void setRangeConceptId(String rangeConceptId)
    {
        this.rangeConceptId = rangeConceptId;
    }
}
