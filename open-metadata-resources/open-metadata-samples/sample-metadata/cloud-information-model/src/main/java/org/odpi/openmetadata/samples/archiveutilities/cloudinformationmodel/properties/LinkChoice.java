/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Return a link that can be to a choice of elements
 */
public class LinkChoice extends Link
{
    private List<Link> linkChoices = new ArrayList<>();

    public LinkChoice(String              guid,
                      String              technicalName,
                      PropertyDescription propertyDescription)
    {
        super(guid, technicalName, propertyDescription);
    }

    public List<Link> getLinkChoices()
    {
        if (linkChoices.isEmpty())
        {
            return null;
        }

        return linkChoices;
    }

    public void addLinkChoice(Link link)
    {
        this.linkChoices.add(link);
    }
}
