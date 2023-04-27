/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

/**
 * AtlasGlossaryMemberBaseProperties is the base class for terms and categories.
 */
public class AtlasGlossaryMemberBaseProperties extends AtlasGlossaryBaseProperties
{
    private AtlasGlossaryAnchorElement anchor = null;


    /**
     * Standard constructor
     */
    public AtlasGlossaryMemberBaseProperties()
    {
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object to copy
     */
    public AtlasGlossaryMemberBaseProperties(AtlasGlossaryMemberBaseProperties template)
    {
        super(template);

        if (template != null)
        {
            anchor = template.getAnchor();
        }

    }


    public AtlasGlossaryAnchorElement getAnchor()
    {
        return anchor;
    }


    public void setAnchor(AtlasGlossaryAnchorElement anchor)
    {
        this.anchor = anchor;
    }
}
