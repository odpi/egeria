/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedAsset describes the relationship to other assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedAsset extends PropertyBase
{
    private static final long     serialVersionUID = 1L;

    private String   typeName = null;
    private String   attributeName = null;
    private Asset    relatedAsset = null;


    /**
     * Default constructor
     */
    public RelatedAsset()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedAsset(RelatedAsset template)
    {
        super(template);

        if (template != null)
        {
            typeName = template.getTypeName();
            relatedAsset = template.getRelatedAsset();
        }
    }


    /**
     * Return the type of relationship to the asset.
     *
     * @return type name string
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the type of relationship to the asset.
     *
     * @param typeName type name string
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the attribute name for the related asset.
     *
     * @return string name
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the attribute name for the related asset.
     *
     * @param attributeName string name
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the bean that described the related asset
     *
     * @return asset bean
     */
    public Asset getRelatedAsset()
    {
        return relatedAsset;
    }


    /**
     * Set up the bean that described the related asset
     *
     * @param relatedAsset asset bean
     */
    public void setRelatedAsset(Asset relatedAsset)
    {
        this.relatedAsset = relatedAsset;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedAsset{" +
                "typeName='" + typeName + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", relatedAsset=" + relatedAsset +
                '}';
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
        RelatedAsset that = (RelatedAsset) objectToCompare;
        return Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getAttributeName(), that.getAttributeName()) &&
                Objects.equals(getRelatedAsset(), that.getRelatedAsset());
    }



    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeName(), getAttributeName(), getRelatedAsset());
    }
}
