/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;

import java.util.List;
import java.util.Objects;

/**
 * Meaning is a cut-down summary of a glossary term to aid the asset consumer in understanding the content
 * of an asset.
 */
public class AssetMeaning extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected Meaning   meaningBean;


    /**
     * Bean constructor
     *
     * @param meaningBean - bean containing all of the properties
     */
    public AssetMeaning(Meaning   meaningBean)
    {
        super(meaningBean);

        if (meaningBean == null)
        {
            this.meaningBean = new Meaning();
        }
        else
        {
            this.meaningBean = new Meaning(meaningBean);
        }
    }


    /**
     * Bean constructor
     *
     * @param parentAsset descriptor for parent asset
     * @param meaningBean - bean containing all of the properties
     */
    public AssetMeaning(AssetDescriptor  parentAsset,
                        Meaning          meaningBean)
    {
        super(parentAsset, meaningBean);

        if (meaningBean == null)
        {
            this.meaningBean = new Meaning();
        }
        else
        {
            this.meaningBean = new Meaning(meaningBean);
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateMeaning element to copy
     */
    public AssetMeaning(AssetDescriptor parentAsset, AssetMeaning templateMeaning)
    {
        super(parentAsset, templateMeaning);

        if (templateMeaning == null)
        {
            this.meaningBean = new Meaning();
        }
        else
        {
            this.meaningBean = templateMeaning.getMeaningBean();
        }
    }


    /**
     * Return the bean containing all of the properties
     *
     * @return meaning bean
     */
    protected Meaning  getMeaningBean()
    {
        return meaningBean;
    }


    /**
     * Return the glossary term name.
     *
     * @return String name
     */
    public String getName()
    {
        return meaningBean.getName();
    }


    /**
     * Return the description of the glossary term.
     *
     * @return String description
     */
    public String getDescription()
    {
        return meaningBean.getDescription();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return meaningBean.toString();
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetMeaning that = (AssetMeaning) objectToCompare;
        return Objects.equals(meaningBean, that.meaningBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), meaningBean);
    }
}