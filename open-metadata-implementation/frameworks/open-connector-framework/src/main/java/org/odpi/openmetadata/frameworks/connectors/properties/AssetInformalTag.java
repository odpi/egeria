/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;

import java.util.Objects;

/**
 * InformalTag stores information about a tag connected to an asset.
 * InformalTags provide informal classifications to assets
 * and can be added at any time.
 *
 * InformalTags have the userId of the person who added the tag, the name of the tag and its description.
 *
 * The content of the tag is a personal judgement (which is why the user's id is in the tag)
 * and there is no formal review of the tags.  However, they can be used as a basis for crowd-sourcing
 * Glossary terms.
 *
 * Private InformalTags are only returned to the user that created them.
 */
public class AssetInformalTag extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected InformalTag   informalTagBean;


    /**
     * Bean constructor
     *
     * @param informalTagBean bean containing the properties
     */
    public AssetInformalTag(InformalTag   informalTagBean)
    {
        super(informalTagBean);

        if (informalTagBean == null)
        {
            this.informalTagBean = new InformalTag();
        }
        else
        {
            this.informalTagBean = informalTagBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param informalTagBean bean containing the properties
     */
    public AssetInformalTag(AssetDescriptor  parentAsset,
                            InformalTag      informalTagBean)
    {
        super(parentAsset, informalTagBean);

        if (informalTagBean == null)
        {
            this.informalTagBean = new InformalTag();
        }
        else
        {
            this.informalTagBean = informalTagBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateInformalTag element to copy
     */
    public AssetInformalTag(AssetDescriptor parentAsset, AssetInformalTag templateInformalTag)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateInformalTag);

        if (templateInformalTag == null)
        {
            this.informalTagBean = new InformalTag();
        }
        else
        {
            this.informalTagBean = templateInformalTag.getInformalTagBean();
        }
    }


    /**
     * Return the bean with all of the properties in it
     *
     * @return informal tag bean
     */
    protected  InformalTag   getInformalTagBean()
    {
        return informalTagBean;
    }


    /**
     * Return if the link to the tag is private to the creating user.
     *
     * @return boolean
     */
    public boolean isPublic()
    {
        return informalTagBean.getIsPublic();
    }


    /**
     * Return boolean flag to say whether the tag is private or not.  A private tag is only seen by the
     * person who set it up.  Public tags are visible to everyone who can see the asset description.
     *
     * @return boolean is private flag
     */
    public boolean isPrivateTag() { return informalTagBean.getIsPrivateTag(); }


    /**
     * Return the user id of the person who created the tag.  Null means the user id is not known.
     *
     * @return String tagging user
     */
    public String getUser() { return informalTagBean.getUser(); }


    /**
     * Return the name of the tag.  It is not valid to have a tag with no name.  However, there is a point where
     * the tag object is created and the tag name not set, so null is a possible response.
     *
     * @return String tag name
     */
    public String getName() { return informalTagBean.getName(); }


    /**
     * Return the tag description null means no description is available.
     *
     * @return String tag description
     */
    public String getDescription() { return informalTagBean.getDescription(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return informalTagBean.toString();
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
        AssetInformalTag that = (AssetInformalTag) objectToCompare;
        return Objects.equals(informalTagBean, that.informalTagBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), informalTagBean);
    }
}