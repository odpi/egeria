/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SecurityTags;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Meaning is a cut-down summary of a glossary term to aid the asset consumer in understanding the content
 * of an asset.
 */
public class AssetSecurityTags extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    private SecurityTags securityTags;


    /**
     * Bean constructor
     *
     * @param securityTags - bean containing all of the properties
     */
    public AssetSecurityTags(SecurityTags securityTags)
    {
        super(null);

        if (securityTags == null)
        {
            this.securityTags = new SecurityTags();
        }
        else
        {
            this.securityTags = new SecurityTags(securityTags);
        }
    }


    /**
     * Bean constructor
     *
     * @param parentAsset descriptor for parent asset
     * @param securityTags - bean containing all of the properties
     */
    public AssetSecurityTags(AssetDescriptor  parentAsset,
                             SecurityTags     securityTags)
    {
        super(parentAsset);

        if (securityTags == null)
        {
            this.securityTags = new SecurityTags();
        }
        else
        {
            this.securityTags = new SecurityTags(securityTags);
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param template element to copy
     */
    public AssetSecurityTags(AssetDescriptor parentAsset, AssetSecurityTags template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.securityTags = new SecurityTags();
        }
        else
        {
            this.securityTags = template.getSecurityTags();
        }
    }


    /**
     * Return the bean containing all of the properties
     *
     * @return  bean
     */
    protected SecurityTags getSecurityTags()
    {
        return securityTags;
    }


    /**
     * Return the list of security labels attached to the element.
     *
     * @return list of label strings
     */
    public List<String> getSecurityLabels()
    {
        if (securityTags == null)
        {
            return null;
        }
        else
        {
            return securityTags.getSecurityLabels();
        }
    }



    /**
     * Return the security properties associated with the element.  These are name-value pairs.
     *
     * @return map of properties
     */
    public Map<String, Object> getSecurityProperties()
    {
        if (securityTags == null)
        {
            return null;
        }
        else
        {
            return securityTags.getSecurityProperties();
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        if (securityTags == null)
        {
            return new SecurityTags().toString();
        }

        return securityTags.toString();
    }





    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        AssetSecurityTags that = (AssetSecurityTags) objectToCompare;
        return Objects.equals(securityTags, that.securityTags);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(securityTags);
    }
}