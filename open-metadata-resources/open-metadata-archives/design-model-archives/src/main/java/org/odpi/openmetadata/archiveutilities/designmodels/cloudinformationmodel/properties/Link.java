/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties;

import java.util.List;

public class Link extends Property
{
    private List<String> domainConceptIds = null;
    private List<String> rangeConceptIds  = null;


    public Link(PropertyDescription propertyDescription)
    {
        super(propertyDescription);


    }


    public List<String> getDomainConceptIds()
    {
        return domainConceptIds;
    }


    public void setDomainElementId(String domainElementId)
    {
        this.domainConceptIds.add(domainElementId);
    }


    public List<String> getRangeConceptIds()
    {
        return rangeConceptIds;
    }


    public void setRangeElementId(String rangeElementId)
    {
        this.rangeConceptIds.add(rangeElementId);
    }
}
