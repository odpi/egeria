/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

public class Link extends Property
{
    private String domainConceptGUID = null;
    private String domainConceptName = null;
    private String rangeConceptGUID  = null;
    private String rangeConceptName  = null;


    public Link(String              guid,
                String              technicalName,
                PropertyDescription propertyDescription)
    {
        super(guid, technicalName, propertyDescription);
    }

    public String getDomainConceptGUID()
    {
        return domainConceptGUID;
    }


    public void setDomainConceptGUID(String domainConceptGUID)
    {
        this.domainConceptGUID = domainConceptGUID;
    }


    public String getDomainConceptName()
    {
        return domainConceptName;
    }


    public void setDomainConceptName(String domainConceptName)
    {
        this.domainConceptName = domainConceptName;
    }


    public String getRangeConceptGUID()
    {
        return rangeConceptGUID;
    }


    public void setRangeConceptGUID(String rangeConceptGUID)
    {
        this.rangeConceptGUID = rangeConceptGUID;
    }


    public String getRangeConceptName()
    {
        return rangeConceptName;
    }


    public void setRangeConceptName(String rangeConceptName)
    {
        this.rangeConceptName = rangeConceptName;
    }
}
