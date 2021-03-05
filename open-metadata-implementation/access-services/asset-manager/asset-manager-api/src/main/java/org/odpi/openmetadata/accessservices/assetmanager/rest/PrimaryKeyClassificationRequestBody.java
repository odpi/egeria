/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.PrimaryKeyProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PrimaryKeyClassificationRequestBody is used to classify a schema attribute as a primary key.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryKeyClassificationRequestBody extends AssetManagerIdentifiersRequestBody
{
    private static final long   serialVersionUID = 1L;

    private PrimaryKeyProperties primaryKeyProperties = null;



    /**
     * Default constructor
     */
    public PrimaryKeyClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public PrimaryKeyClassificationRequestBody(PrimaryKeyClassificationRequestBody template)
    {
        if (template != null)
        {
            primaryKeyProperties          = template.getPrimaryKeyProperties();
        }
    }


    /**
     * Return the properties associated with the primary key.
     *
     * @return properties
     */
    public PrimaryKeyProperties getPrimaryKeyProperties()
    {
        return primaryKeyProperties;
    }


    /**
     * Set up the properties associated with the primary key.
     *
     * @param primaryKeyProperties properties
     */
    public void setPrimaryKeyProperties(PrimaryKeyProperties primaryKeyProperties)
    {
        this.primaryKeyProperties = primaryKeyProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PrimaryKeyClassificationRequestBody{" +
                       "primaryKeyProperties=" + primaryKeyProperties +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        PrimaryKeyClassificationRequestBody that = (PrimaryKeyClassificationRequestBody) objectToCompare;
        return Objects.equals(getPrimaryKeyProperties(), that.getPrimaryKeyProperties());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), primaryKeyProperties);
    }
}
